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
 * Qmino bvba - Romeinsestraat 18 - 3001 Heverlee  (http://www.qmino.com)
 * Geovise bvba - Generaal Eisenhowerlei 9 - 2140 Antwerpen (http://www.geovise.com)
 */

package org.geolatte.graph.algorithms;

import org.geolatte.graph.*;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 * Depth-first search algorithm that covers the graph from a given origin over a given maximum distance, marking
 * the shortest paths to each visited node.
 * <p>
 *
 * @author Peter Rigole
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class Coverage<N extends Nodal, E extends EdgeLabel<M>, M> implements GraphAlgorithm<Set<PredSuccGraph<N>>> {

    private final Set<PredSuccGraph<N>> result;
    private final Graph<N, E, M> graph;
    private final InternalNode<N> origin;
    private final float maxDistance;
    private final HashMap<InternalNode<N>, PredSuccGraph<N>> nodeCache;

    protected Coverage(Graph<N, E, M> graph, N origin, float maxDistance) {
        result = new HashSet<PredSuccGraph<N>>();
        this.graph = graph;
        this.origin = this.graph.getInternalNode(origin);
        this.maxDistance = maxDistance;
        this.nodeCache = new HashMap<InternalNode<N>, PredSuccGraph<N>>();
    }

    public void execute() {
        deepSearch(new PredSuccGraphImpl<N>(origin, 0));

        // Interpolation on the shapes between each node.
        // Wegendatabank sample code for projection:
//        Geometry target = (Geometry) getShape();
//        LocationIndexedLine line = new LocationIndexedLine(target);
//        Coordinate projected = line.project(coordinate).getCoordinate(target);
//        return new GpsLocatie(projected.x, projected.y, this);
    }

    public Set<PredSuccGraph<N>> getResult() {
        return result;
    }

    /**
     * Performs a deep-search through the graph starting from the given predecessor graph.
     *
     * @param predGraph The predecessor graph to start from.
     */
    private void deepSearch(PredSuccGraph<N> predGraph) {
        OutEdgeIterator<N, E> outEdges = this.graph.getOutGoingEdges(predGraph.getInternalNode(), this.graph.getModus());
        while (outEdges.next()) {
            InternalNode<N> toNode = outEdges.getToInternalNode();
            if (nodeCache.containsKey(toNode)) {
                PredSuccGraph<N> existingPredGraph = nodeCache.get(toNode);
                if (existingPredGraph.getWeight() > predGraph.getWeight() + outEdges.getEdgeLabel().getWeight(this.graph.getModus())) {
                    // We found a shorter path. Detach the longer path from the existing node and save it as a
                    // result when it no longer has any successors.
                    if (existingPredGraph.getPredecessor().getSuccessors().size() == 1) {
                        result.add(existingPredGraph.getPredecessor());
                    }
                    existingPredGraph.setPredecessor(predGraph);
                    existingPredGraph.setWeight(predGraph.getWeight() + outEdges.getEdgeLabel().getWeight(this.graph.getModus()));
                    // ToDo: update the weight of all the successors of existingPredGraph
                }
            } else {
                PredSuccGraph<N> nextPredGraph =
                        new PredSuccGraphImpl<N>(toNode, predGraph.getWeight() + outEdges.getEdgeLabel().getWeight(this.graph.getModus()));
                nextPredGraph.setPredecessor(predGraph);
                nodeCache.put(toNode, nextPredGraph);
                if (nextPredGraph.getWeight() < maxDistance) {
                    deepSearch(nextPredGraph);
                } else {
                    result.add(nextPredGraph);
                }
            }
        }
        if (predGraph.getSuccessors().size() == 0) {
            result.add(predGraph);
        }
    }

    static class PredSuccGraphImpl<N extends Nodal> implements PredSuccGraph<N> {
        private final InternalNode<N> node;
        private PredSuccGraph<N> predecessor = null;
        private final Set<PredSuccGraph<N>> successors = new HashSet<PredSuccGraph<N>>();
        private float weight;

        private PredSuccGraphImpl(InternalNode<N> n, float weight) {
            this.node = n;
            this.weight = weight;
        }


        public PredSuccGraph<N> getPredecessor() {
            return predecessor;
        }


        public float getWeight() {
            return this.weight;
        }


        public void setWeight(float w) {
            this.weight = w;
        }


        public InternalNode<N> getInternalNode() {
            return this.node;
        }

        public Set<PredSuccGraph<N>> getSuccessors() {
            return successors;
        }

        public static class PGComparator<N extends Nodal> implements Comparator<PredSuccGraph<N>> {

            public int compare(PredSuccGraph<N> o1, PredSuccGraph<N> o2) {
                if (o1 instanceof PredSuccGraphImpl && o2 instanceof PredSuccGraphImpl) {
                    PredSuccGraphImpl<N> pg1 = (PredSuccGraphImpl<N>) o1;
                    PredSuccGraphImpl<N> pg2 = (PredSuccGraphImpl<N>) o2;
                    if (pg1.node.equals(pg2.node)) {
                        return 0;
                    }
                    return Float.compare(pg1.getWeight(), pg2.getWeight());
                }
                throw new IllegalArgumentException();
            }
        }

        public void setPredecessor(PredSuccGraph<N> pred) {
            if (this.predecessor != null) {
                this.predecessor.removeSuccessor(this);
            }
            this.predecessor = pred;
            this.predecessor.addSuccessor(this);
        }

        public void addSuccessor(PredSuccGraph<N> pred) {
            this.successors.add(pred);
        }

        public void removeSuccessor(PredSuccGraph<N> pred) {
            this.successors.remove(pred);
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((node == null) ? 0 : node.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            PredSuccGraphImpl<N> other = (PredSuccGraphImpl<N>) obj;
            if (node == null) {
                if (other.node != null)
                    return false;
            } else if (!node.equals(other.node))
                return false;
            return true;
        }

        public String toString() {
            return String.format("MyNode: %s, weight: %.1f", this.node,
                    this.weight);
        }
    }


}