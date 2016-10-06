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

package org.geolatte.graph;

import java.util.*;

/**
 * Offers a number of static factory methods to create graphs.
 */
public class Graphs {

    /**
     * Creates a builder for directed grid-indexed graphs.
     *
     * @param extent     The extent that determines the bounds of the graph.
     * @param resolution The grid resolution.
     * @param <N>        Type of the domain nodes.
     * @param <E>        The edge label type.
     * @return A builder for grid indexed graphs.
     */
    public static <N extends Locatable, E> GraphBuilder<N, E> createGridIndexedGraphBuilder(Extent extent, int resolution) {

        return new GridIndexedGraphBuilder<N, E>(extent, resolution);
    }

    // Builder implementation
    private static class GridIndexedGraphBuilder<N extends Locatable, E> implements GraphBuilder<N, E> {

        private final SpatialIndexBuilder<InternalNode<N, E>> indexBuilder;
        private final Map<N, InternalNode<N, E>> map = new HashMap<N, InternalNode<N, E>>(); // map is used to quickly locate Nodes based on node equality.

        private GridIndexedGraphBuilder(Extent extent, int resolution) {

            this.indexBuilder = SpatialIndexes.createGridIndexBuilder(extent, resolution);
        }


        public LocateableGraph<N, E> build() throws BuilderException {

            if (map.isEmpty()) {
                throw new IllegalStateException("No nodes added since last built");
            }

            map.clear(); // empty to save on memory.
            SpatialIndex<InternalNode<N, E>> index = this.indexBuilder.build();
            return new GridIndexedGraph<N, E>(index);
        }

        public void addEdge(N fromNode, N toNode, EdgeWeight edgeWeight) {

            addEdge(fromNode, toNode, edgeWeight, null);
        }

        public void addEdge(N fromNode, N toNode, EdgeWeight edgeWeight, E edgeLabel) {

            if (fromNode.equals(toNode)) {
                return;
                //System.err.println("Tried adding zero-length edge");
            }

            // Lookup nodes or create them if new
            InternalNode<N, E> fNw = this.map.get(fromNode);
            InternalNode<N, E> toNw = this.map.get(toNode);
            if (fNw == null) {
                fNw = new LocatedInternalNodeWrapper<N, E>(fromNode);
                this.indexBuilder.insert(fNw);
                this.map.put(fromNode, fNw);
            }
            if (toNw == null) {
                toNw = new LocatedInternalNodeWrapper<N, E>(toNode);
                this.indexBuilder.insert(toNw);
                this.map.put(toNode, toNw);
            }

            // Add the edge between the nodes
            fNw.addEdge(toNw, edgeWeight, edgeLabel);
        }

        private static class GridIndexedGraph<N extends Locatable, E> implements LocateableGraph<N, E> {

            private final SpatialIndex<InternalNode<N, E>> index;

            private GridIndexedGraph(SpatialIndex<InternalNode<N, E>> index) {

                this.index = index;
            }


            public List<InternalNode<N, E>> getNodesAt(Locatable loc) {
                return Collections.unmodifiableList(this.index.getNodeAt(loc));
            }


            public Iterator<InternalNode<N, E>> iterator() {
                return this.index.getNodes();
            }


            public List<InternalNode<N, E>> getClosestNodes(Locatable loc, int num, int distance) {
                return Collections.unmodifiableList(this.index.getNClosest(loc, num, distance));
            }


            public InternalNode<N, E> getInternalNode(N node) {
                for (InternalNode<N, E> nw : this.index.getNodeAt(node)) {
                    if (nw.getWrappedNode().equals(node)) {
                        return nw;
                    }
                }
                return null;
            }


            public Iterator<InternalNode<N, E>> getOutGoingEdges(InternalNode<N, E> internalNode, ContextualReachability<N, E, ?> contextualReachability) {

                if (contextualReachability == null) {
                    contextualReachability = new EmptyContextualReachability<N, E, Object>();
                }
                return new OutEdgeIteratorImpl<N, E>((InternalNodeWrapper<N, E>) internalNode, contextualReachability);
            }

            public Iterator<InternalNode<N, E>> getInComingEdges(InternalNode<N, E> internalNode, ContextualReachability<N, E, ?> contextualReachability) {

                if (contextualReachability == null) {
                    contextualReachability = new EmptyContextualReachability<N, E, Object>();
                }
                return new InEdgeIteratorImpl<N, E>((InternalNodeWrapper<N, E>) internalNode, contextualReachability);
            }
        }

        /**
         * Simple private implementation of OutEdgeIterator.
         *
         * @param <N>
         */
        private static class OutEdgeIteratorImpl<N extends Locatable, E> implements Iterator<InternalNode<N, E>> {

            final Iterator<InternalNode<N, E>> reachableNodesIterator;

            private OutEdgeIteratorImpl(InternalNodeWrapper<N, E> from, ContextualReachability<N, E, ?> contextualReachability) {


                List<InternalNode<N, E>> reachableNodes = new ArrayList<InternalNode<N, E>>();
                for (InternalNode<N, E> node : from.toNodes) {
                    if (contextualReachability.isReachable(node)) {
                        reachableNodes.add(node);
                    }
                }
                reachableNodesIterator = reachableNodes.iterator();
            }

            public boolean hasNext() {
                return reachableNodesIterator.hasNext();
            }

            public InternalNode<N, E> next() {

                return reachableNodesIterator.next();
            }

            public void remove() {

                throw new UnsupportedOperationException();
            }

        }

        /**
         * Simple private implementation of InEdgeIterator.
         *
         * @param <N>
         */
        private static class InEdgeIteratorImpl<N extends Locatable, E> implements Iterator<InternalNode<N, E>> {

            final Iterator<InternalNode<N, E>> reachableNodesIterator;

            private InEdgeIteratorImpl(InternalNodeWrapper<N, E> to, ContextualReachability<N, E, ?> contextualReachability) {


                List<InternalNode<N, E>> reachableFromNodes = new ArrayList<InternalNode<N, E>>();
                for (InternalNode<N, E> node : to.getReachableFrom()) {
                    if (contextualReachability.isReachable(node)) {
                        reachableFromNodes.add(node);
                    }
                }
                reachableNodesIterator = reachableFromNodes.iterator();
            }

            public boolean hasNext() {
                return reachableNodesIterator.hasNext();
            }

            public InternalNode<N, E> next() {

                return reachableNodesIterator.next();
            }

            public void remove() {

                throw new UnsupportedOperationException();
            }

        }

    }


}
