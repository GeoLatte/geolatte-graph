/*
 * This file is part of the GeoLatte project.
 *
 *     GeoLatte is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     GeoLatte is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with GeoLatte.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright (C) 2010 - 2011 and Ownership of code is shared by:
 * Qmino bvba - Esperantolaan 4 - 3001 Heverlee  (http://www.qmino.com)
 * Geovise bvba - Generaal Eisenhowerlei 9 - 2140 Antwerpen (http://www.geovise.com)
 */

package org.geolatte.graph.algorithms;

import org.geolatte.graph.EmptyContextualReachability;
import org.geolatte.graph.Graph;
import org.geolatte.graph.GraphTree;
import org.geolatte.graph.GraphTreeIterator;
import org.geolatte.graph.InternalNode;
import org.geolatte.graph.RoutingContextualReachability;

import java.util.*;

/**
 * <p/>
 * Breath-first search algorithm that searches for all nodes within a maximum distance (according to a given weight)
 * from a given starting node. The result of the algorithm is a list of nodes along with the total weight to get to
 * them. Total weights are always lower or equal to the given maximum distance.
 * <p/>
 *
 * @param <N> The type of domain node
 * @param <E> The edge label type.
 */
public class BFSDistanceLimited<N, E> implements GraphAlgorithm<GraphTree<N, E>> {

    private final InternalNode<N, E> source;
    private final float maxDistance;
    private final Graph<N, E> graph;
    private GraphTree<N, E> result;
    private final int weightIndex;
    private final RoutingContextualReachability<N, E, BFSState<N, E>> contextualReachability;

    BFSDistanceLimited(Graph<N, E> graph, N source, float maxDistance, int weightIndex) {
        this(graph, source, maxDistance, weightIndex, new EmptyContextualReachability<N, E, BFSState<N, E>>());
    }

    BFSDistanceLimited(Graph<N, E> graph, N source, float maxDistance, int weightIndex, RoutingContextualReachability<N, E, BFSState<N, E>> contextualReachability) {
        this.graph = graph;
        this.source = graph.getInternalNode(source);
        this.maxDistance = maxDistance;
        this.weightIndex = weightIndex;
        this.contextualReachability = contextualReachability;
        this.contextualReachability.setOriginDestination(source, null);
    }


    public void execute() {


        // List of new nodes with predecessors (as bfs state): nodes where we might still add a successor without going beyond the maxDistance.
        Queue<BFSState<N, E>> greyNodes = new LinkedList<BFSState<N, E>>();
        // Set of nodes that have are certain to lie within the max distance: we cannot add another node without going beyond the mxDistance.
        Set<BFSState<N, E>> blackNodes = new HashSet<BFSState<N, E>>();

        BFSState<N, E> ws = new BFSState<N, E>(this.source);
        ws.distance = 0.f;
        greyNodes.add(ws);

        while (!greyNodes.isEmpty()) {
            BFSState<N, E> wu = greyNodes.remove();


            // TODO : Is the context set correctly here?
            contextualReachability.setContext(wu);
            Iterator<InternalNode<N, E>> outEdges = this.graph.getOutGoingEdges(wu.internalNode, contextualReachability);
            while (outEdges.hasNext()) {
                InternalNode<N, E> v = outEdges.next();
                BFSState<N, E> wv = new BFSState<N, E>(v);
                if (!greyNodes.contains(wv) && !blackNodes.contains(wv)) {
                    wv.distance = wu.distance + wu.internalNode.getWeightTo(v, weightIndex);
                    if (wv.distance <= maxDistance) {
                        wv.setPredecessor(wu);
                        greyNodes.add(wv);
                    }
                }
            }

            blackNodes.add(wu);
        }

        this.result = new GraphTreeImpl<N, E>(ws);

    }

    /**
     * Gets the result of the algorithm execution.
     *
     * @return A map with all nodes mapped to their distance from the source.
     */
    public GraphTree<N, E> getResult() {
        return this.result;
    }

//    protected Map<N, Float> toMap(Set<BFSState<N, E>> nodes) {
//        Map<N, Float> map = new HashMap<N, Float>(nodes.size());
//        for (BFSState<N, E> wu : nodes) {
//            map.put(wu.internalNode.getWrappedNode(), wu.distance);
//        }
//        return map;
//
//    }

    private static class BFSState<N, E> {

        final InternalNode<N, E> internalNode;
        float distance = Float.POSITIVE_INFINITY;
        private BFSState<N, E> predecessor;
        private final List<BFSState<N, E>> children = new LinkedList<BFSState<N, E>>();

        private BFSState(InternalNode<N, E> internalNode) {
            this.internalNode = internalNode;
        }

        /* (non-Javadoc)
           * @see java.lang.Object#hashCode()
           */
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((internalNode == null) ? 0 : internalNode.hashCode());
            return result;
        }

        /* (non-Javadoc)
           * @see java.lang.Object#equals(java.lang.Object)
           */
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (!(obj instanceof BFSState))
                return false;
            BFSState other = (BFSState) obj;
            if (internalNode == null) {
                if (other.internalNode != null)
                    return false;
            } else if (!internalNode.equals(other.internalNode))
                return false;
            return true;
        }

        public double getDistance() {
            return distance;
        }

        public List<BFSState<N, E>> getChildren() {
            return children;
        }

        public void addChild(BFSState<N, E> child) {
            children.add(child);
        }

        public void setPredecessor(BFSState<N, E> predecessor) {
            this.predecessor = predecessor;
            predecessor.addChild(this);
        }
    }

    private static class GraphTreeImpl<N, E> implements GraphTree<N, E> {

        private final BFSState<N, E> root;

        public GraphTreeImpl(BFSState<N, E> root) {
            this.root = root;
        }

        public N getRoot() {
            return this.root.internalNode.getWrappedNode();
        }

        public double getRootDistance() {
            return this.root.getDistance();
        }

        public List<GraphTree<N, E>> getChildren() {
            List<GraphTree<N, E>> children = new ArrayList<GraphTree<N, E>>(root.getChildren().size());
            for (BFSState<N, E> bfsState : root.getChildren()) {
                children.add(new GraphTreeImpl<N, E>(bfsState));
            }
            return children;
        }

        public Map<N, Double> toMap() {
            Map<N,Double> result = new HashMap<N, Double>();

            GraphTreeIterator<N,E> iterator = this.iterator();
            while (iterator.next()){
                result.put(iterator.getCurrentNode(), iterator.getCurrentDistance());
            }
            return result;
        }

        public GraphTreeIterator<N, E> iterator() {
            return new GraphTreeIteratorImpl<N, E>(this.root);
        }
    }

    private static class GraphTreeIteratorImpl<N, E> implements GraphTreeIterator<N, E> {

        Queue<BFSState<N, E>> stack = new LinkedList<BFSState<N, E>>();
        BFSState<N, E> current;


        GraphTreeIteratorImpl(BFSState<N, E> root) {
            stack.add(root);
        }

        public boolean next() {
            current = stack.poll();
            if (current == null) return false;
            stack.addAll(current.getChildren());
            return true;
        }

        public double getCurrentDistance() {
            checkBounds();
            return current.getDistance();
        }

        private void checkBounds() {
            if (current == null) throw new IllegalStateException("No more elements");
        }

        public N getCurrentNode() {
            checkBounds();
            return current.internalNode.getWrappedNode();
        }

        public E getCurrentEdge() {
            checkBounds();
            if (current.predecessor == null) return null;
            return current.predecessor.internalNode.getLabelTo(current.internalNode);
        }

    }


}
