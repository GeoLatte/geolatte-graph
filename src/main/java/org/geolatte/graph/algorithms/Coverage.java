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
 * the shortest paths to each visited locatedNode.
 * <p>
 *
 * @author Peter Rigole
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class Coverage<N extends Located, M> implements GraphAlgorithm<Set<PredSuccGraph<N>>> {

    private final Set<PredSuccGraph<N>> result;
    private final Graph<N> graph;
    private final LocatedNode<N> origin;
    private final float maxDistance;
    private final HashMap<LocatedNode<N>, PredSuccGraph<N>> nodeCache;
    private final M modus;

    protected Coverage(Graph<N> graph, N origin, float maxDistance, M mode) {
        result = new HashSet<PredSuccGraph<N>>();
        this.graph = graph;
        this.origin = this.graph.getInternalNode(origin);
        this.maxDistance = maxDistance;
        this.nodeCache = new HashMap<LocatedNode<N>, PredSuccGraph<N>>();
        this.modus = mode;
    }

    public void execute() {
        deepSearch(new PredSuccGraphImpl<N>(origin, 0));

        // Interpolation on the shapes between each locatedNode.
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
        OutEdgeIterator<N> outEdges = this.graph.getOutGoingEdges(predGraph.getInternalNode(), null); // TODO: contextual reachability
        while (outEdges.hasNext()) {
            LocatedNode<N> toLocatedNode = outEdges.nextInternalNode();
            // TODO: correct doorgeven van weightKind
            float weightToLocatedNode = predGraph.getInternalNode().getWeightTo(toLocatedNode, 0);
            if (nodeCache.containsKey(toLocatedNode)) {
                PredSuccGraph<N> existingPredGraph = nodeCache.get(toLocatedNode);
                if (existingPredGraph.getWeight() > predGraph.getWeight() + weightToLocatedNode) {
                    // We found a shorter path. Detach the longer path from the existing locatedNode and save it as a
                    // result when it no longer has any successors.
                    if (existingPredGraph.getPredecessor().getSuccessors().size() == 1) {
                        result.add(existingPredGraph.getPredecessor());
                    }
                    existingPredGraph.setPredecessor(predGraph);
                    existingPredGraph.setWeight(predGraph.getWeight() + weightToLocatedNode);
                    // ToDo: update the weight of all the successors of existingPredGraph
                }
            } else {
                PredSuccGraph<N> nextPredGraph = new PredSuccGraphImpl<N>(toLocatedNode, predGraph.getWeight() + weightToLocatedNode);
                nextPredGraph.setPredecessor(predGraph);
                nodeCache.put(toLocatedNode, nextPredGraph);
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

    static class PredSuccGraphImpl<N extends Located> implements PredSuccGraph<N> {
        private final LocatedNode<N> locatedNode;
        private PredSuccGraph<N> predecessor = null;
        private final Set<PredSuccGraph<N>> successors = new HashSet<PredSuccGraph<N>>();
        private float weight;

        private PredSuccGraphImpl(LocatedNode<N> n, float weight) {
            this.locatedNode = n;
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


        public LocatedNode<N> getInternalNode() {
            return this.locatedNode;
        }

        public Set<PredSuccGraph<N>> getSuccessors() {
            return successors;
        }

        public static class PGComparator<N extends Located> implements Comparator<PredSuccGraph<N>> {

            public int compare(PredSuccGraph<N> o1, PredSuccGraph<N> o2) {
                if (o1 instanceof PredSuccGraphImpl && o2 instanceof PredSuccGraphImpl) {
                    PredSuccGraphImpl<N> pg1 = (PredSuccGraphImpl<N>) o1;
                    PredSuccGraphImpl<N> pg2 = (PredSuccGraphImpl<N>) o2;
                    if (pg1.locatedNode.equals(pg2.locatedNode)) {
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
            result = prime * result + ((locatedNode == null) ? 0 : locatedNode.hashCode());
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
            if (locatedNode == null) {
                if (other.locatedNode != null)
                    return false;
            } else if (!locatedNode.equals(other.locatedNode))
                return false;
            return true;
        }

        public String toString() {
            return String.format("MyNode: %s, weight: %.1f", this.locatedNode,
                    this.weight);
        }
    }


}