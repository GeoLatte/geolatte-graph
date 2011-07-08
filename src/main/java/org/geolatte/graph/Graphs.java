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

package org.geolatte.graph;

import com.vividsolutions.jts.geom.Envelope;

import java.util.*;


public class Graphs {

    public static <N extends Locatable> GraphBuilder<N> createGridIndexedGraphBuilder(Envelope env, int resolution) {

        return new GridIndexedGraphBuilder<N>(env, resolution);
    }

    // Builder implementation
    private static class GridIndexedGraphBuilder<N extends Locatable> implements GraphBuilder<N> {

        private final SpatialIndexBuilder<N> indexBuilder;
        private final Map<N, InternalNode<N>> map = new HashMap<N, InternalNode<N>>(); // map is used to quickly locate Nodes based on node equality.

        private GridIndexedGraphBuilder(Envelope env, int resolution) {

            this.indexBuilder = SpatialIndexes.createGridIndexBuilder(env, resolution);
        }


        public Graph<N> build() throws BuilderException {

            if (map.isEmpty()) {
                throw new IllegalStateException("No nodes added since last built");
            }

            map.clear(); // empty to save on memory.
            SpatialIndex<N> index = this.indexBuilder.build();
            return new GridIndexedGraph<N>(index);
        }

        public void addEdge(N fromNode, N toNode, EdgeWeight edgeWeight) {

            if (fromNode.equals(toNode)) {
                return;
                //System.err.println("Tried adding zero-length edge");
            }

            // Lookup nodes or create them if new
            InternalNode<N> fNw = this.map.get(fromNode);
            InternalNode<N> toNw = this.map.get(toNode);
            if (fNw == null) {
                fNw = new InternalNodeWrapper<N>(fromNode);
                this.indexBuilder.insert(fNw);
                this.map.put(fromNode, fNw);
            }
            if (toNw == null) {
                toNw = new InternalNodeWrapper<N>(toNode);
                this.indexBuilder.insert(toNw);
                this.map.put(toNode, toNw);
            }

            // TODO: modus should not be null
            // Add the edge between the nodes
            fNw.addEdge(toNw, null, edgeWeight);
        }

        // Graph Implementation
        private static class GridIndexedGraph<N extends Locatable> implements Graph<N> {

            private final SpatialIndex<N> index;

            private GridIndexedGraph(SpatialIndex<N> index) {

                this.index = index;
            }


            public List<InternalNode<N>> getNodesAt(Locatable loc) {
                return Collections.unmodifiableList(this.index.getObjectAt(loc));
            }


            public Iterator<InternalNode<N>> iterator() {
                return this.index.getObjects();
            }


            public List<InternalNode<N>> getClosestNodes(Locatable loc, int num, int distance) {
                return Collections.unmodifiableList(this.index.getNClosest(loc, num, distance));
            }


            public InternalNode<N> getInternalNode(N node) {
                for (InternalNode<N> nw : this.index.getObjectAt(node)) {
                    if (nw.getWrappedNode().equals(node)) {
                        return nw;
                    }
                }
                return null;
            }


            public Iterator<InternalNode<N>> getOutGoingEdges(InternalNode<N> internalNode, ContextualReachability contextualReachability) {
                
                return new OutEdgeIteratorImpl<N>((InternalNodeWrapper<N>) internalNode, contextualReachability);
            }
        }


        private static class OutEdgeIteratorImpl<N extends Locatable> implements Iterator<InternalNode<N>> {

            int i = -1;
            Iterator<InternalNode<N>> reachableNodesIt;

            private OutEdgeIteratorImpl(InternalNodeWrapper<N> from, ContextualReachability<N> contextualReachability) {


                List<InternalNode<N>> reachableNodes = new ArrayList<InternalNode<N>>();
                for (InternalNode<N> node : from.toNodes) {
                    if (contextualReachability.isReachable(node)) {
                        reachableNodes.add(node);
                    }
                }
                reachableNodesIt = reachableNodes.iterator();
            }

            public boolean hasNext() {
                return reachableNodesIt.hasNext();
            }

            public InternalNode<N> next() {

                return reachableNodesIt.next();
            }

            public void remove() {

                throw new UnsupportedOperationException();
            }

        }

    }


}
