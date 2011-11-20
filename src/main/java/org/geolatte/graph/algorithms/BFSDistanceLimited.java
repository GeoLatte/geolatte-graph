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
public class BFSDistanceLimited<N, E> implements GraphAlgorithm<Map<N, Float>> {

    private final InternalNode<N, E> source;
    private final float maxDistance;
    private final Graph<N, E> graph;
    private Map<N, Float> result;
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
        ws.predecessor = null;
        greyNodes.add(ws);

        while (!greyNodes.isEmpty()) {
            BFSState<N, E> wu = greyNodes.remove();

            if (wu.distance > maxDistance) {
                continue; //don't expand when the internalNode is beyond maximum distance.
            }

            // TODO : Is the context set correctly here?
            contextualReachability.setContext(wu);
            Iterator<InternalNode<N, E>> outEdges = this.graph.getOutGoingEdges(wu.internalNode, contextualReachability);
            while (outEdges.hasNext()) {
                InternalNode<N, E> v = outEdges.next();
                BFSState<N, E> wv = new BFSState<N, E>(v);
                if (!greyNodes.contains(wv) && !blackNodes.contains(wv)) {
                    wv.distance = wu.distance + wu.internalNode.getWeightTo(v, weightIndex);
                    wv.predecessor = wu.internalNode;
                    greyNodes.add(wv);
                }
            }

            blackNodes.add(wu);
        }

        this.result = toMap(blackNodes);

    }

    /**
     * Gets the result of the algorithm execution.
     * @return A map with all nodes mapped to their distance from the source.
     */
    public Map<N, Float> getResult() {
        return this.result;
    }

    protected Map<N, Float> toMap(Set<BFSState<N, E>> nodes) {
        Map<N, Float> map = new HashMap<N, Float>(nodes.size());
        for (BFSState<N, E> wu : nodes) {
            map.put(wu.internalNode.getWrappedNode(), wu.distance);
        }
        return map;

    }

    private static class BFSState<N, E> {

        final InternalNode<N, E> internalNode;
        float distance = Float.POSITIVE_INFINITY;
        InternalNode<N, E> predecessor;

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


    }

}
