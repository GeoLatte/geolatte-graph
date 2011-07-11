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

import java.util.*;

/**
 * <p>
 * Implements the basic Dijkstra shortest path algorithm. By passing in different relaxers, the algorithm can be
 * tweaked.
 * </p>
 *
 * @author Karel Maesen
 */
public class Dijkstra<N, M> implements GraphAlgorithm<Path<N>> {

    private final InternalNode<N> origin;
    private final InternalNode<N> destination;
    private final Graph<N> graph;
    private final M modus;
    private Path<N> result;


    private final PMinQueue<N> minQueue;
    private final Relaxer<N, M> relaxer;
    private ContextualReachability<N> reachability;

    protected Dijkstra(Graph<N> graph, N origin, N destination, Relaxer<N, M> relaxer, M modus, ContextualReachability<N> reachability) {

        this.graph = graph;

        this.origin = this.graph.getInternalNode(origin);
        this.destination = this.graph.getInternalNode(destination);
        this.modus = modus;
        this.relaxer = relaxer;
        this.minQueue = new PMinQueue<N>();
        this.reachability = reachability;
        this.reachability.setOriginDestination(this.origin.getWrappedNode(), this.destination.getWrappedNode());
    }

    protected Dijkstra(Graph<N> graph, N origin, N destination, Relaxer<N, M> relaxer, M modus) {

        this.graph = graph;

        this.origin = this.graph.getInternalNode(origin);
        this.destination = this.graph.getInternalNode(destination);
        this.modus = modus;
        this.relaxer = relaxer;
        this.minQueue = new PMinQueue<N>();
        this.reachability = new EmptyContextualReachability<N>();
    }

    public void execute() {
        Set<InternalNode<N>> closed = new HashSet<InternalNode<N>>();
        PredGraphImpl<N> startPG = new PredGraphImpl<N>(this.origin, 0.0f);
        minQueue.add(startPG, Float.POSITIVE_INFINITY);
        while (!minQueue.isEmpty()) {
            PredGraph<N> pu = minQueue.extractMin();
            closed.add(pu.getInternalNode());
            if (isDone(pu)) {
                return;
            }
            InternalNode<N> u = pu.getInternalNode();
            reachability.setContext(pu);
            Iterator<InternalNode<N>> outEdges = graph.getOutGoingEdges(u, reachability);
            while (outEdges.hasNext()) {
                InternalNode<N> v = outEdges.next();
                if (closed.contains(v)) {
                    continue;
                }
                PredGraph<N> pv = minQueue.get(v);
                if (pv == null) {
                    pv = new PredGraphImpl<N>(v,
                            Float.POSITIVE_INFINITY);
                    minQueue.add(pv, Float.POSITIVE_INFINITY);
                }
                if (this.relaxer.relax(pu, pv, modus)) {
                    this.minQueue.update(pv, this.relaxer.newTotalWeight());
                }
            }
        }
    }

    protected boolean isDone(PredGraph<N> pu) {
        if (pu.getInternalNode().equals(this.destination)) {
            this.result = toPath(pu);
            return true;
        }
        return false;
    }

    private Path<N> toPath(PredGraph<N> p) {
        BasicPath<N> path = new BasicPath<N>();
        path.setTotalWeight(p.getWeight());
        path.insert(p.getInternalNode().getWrappedNode());
        PredGraph<N> next = p.getPredecessor();

        while (next != null) {
            path.insert(next.getInternalNode().getWrappedNode());
            next = next.getPredecessor();
        }
        path.setValid(true);
        return path;
    }

    public Path<N> getResult() {
        return this.result;
    }

    static class PredGraphImpl<N> implements PredGraph<N> {
        private final InternalNode<N> internalNode;
        private PredGraph<N> predecessor = null;
        private float weight;

        private PredGraphImpl(InternalNode<N> n, float weight) {
            this.internalNode = n;
            this.weight = weight;
        }

        public PredGraph<N> getPredecessor() {
            return predecessor;
        }

        public float getWeight() {
            return this.weight;
        }

        public void setWeight(float w) {
            this.weight = w;
        }

        public InternalNode<N> getInternalNode() {
            return this.internalNode;
        }

        /**
         * Returns an iterator over a set of elements of type T.
         *
         * @return an Iterator.
         */
        public Iterator<N> iterator() {
            return new Iterator<N>() {

                // TODO : Unit test initialization of iterator
                private PredGraph<N> predGraph = PredGraphImpl.this;

                public boolean hasNext() {

                    predGraph = predGraph.getPredecessor();
                    return predGraph != null;
                }

                public N next() {
                    if (predGraph == null) {
                        throw new NoSuchElementException();
                    }
                    return predGraph.getInternalNode().getWrappedNode();
                }

                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
        }

        public static class PGComparator<N> implements Comparator<PredGraph<N>> {

            public int compare(PredGraph<N> o1, PredGraph<N> o2) {
                if (o1 instanceof PredGraphImpl && o2 instanceof PredGraphImpl) {
                    PredGraphImpl<N> pg1 = (PredGraphImpl<N>) o1;
                    PredGraphImpl<N> pg2 = (PredGraphImpl<N>) o2;
                    if (pg1.internalNode.equals(pg2.internalNode)) {
                        return 0;
                    }
                    return Float.compare(pg1.getWeight(), pg2.getWeight());
                }
                throw new IllegalArgumentException();
            }
        }

        public void setPredecessor(PredGraph<N> pred) {
            this.predecessor = pred;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((internalNode == null) ? 0 : internalNode.hashCode());
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
            PredGraphImpl<N> other = (PredGraphImpl<N>) obj;
            if (internalNode == null) {
                if (other.internalNode != null)
                    return false;
            } else if (!internalNode.equals(other.internalNode))
                return false;
            return true;
        }

        public String toString() {
            return String.format("MyNode: %s, weight: %.1f", this.internalNode,
                    this.weight);
        }
    }

}
