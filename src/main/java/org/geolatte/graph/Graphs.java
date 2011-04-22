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

    public static <N extends Nodal> GraphBuilder<N> createGridIndexedGraphBuilder(Envelope env, int resolution) {

        return new GridIndexedGraphBuilder<N>(env, resolution);
    }

    // Builder implementation
    private static class GridIndexedGraphBuilder<N extends Nodal> implements GraphBuilder<N> {

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

        public void addEdge(N fromNode, N toNode) {

            if (fromNode.equals(toNode)) {
                return;
                //System.err.println("Tried adding zero-length edge");
            }

            // Lookup nodes or create them if new
            InternalNode<N> fNw = this.map.get(fromNode);
            InternalNode<N> toNw = this.map.get(toNode);
            if (fNw == null) {
                fNw = new NodeWrapper<N>(fromNode);
                this.indexBuilder.insert(fNw);
                this.map.put(fromNode, fNw);
            }
            if (toNw == null) {
                toNw = new NodeWrapper<N>(toNode);
                this.indexBuilder.insert(toNw);
                this.map.put(toNode, toNw);
            }

            // TODO: modus should not be null
            // Add the edge between the nodes
            fNw.addEdge(toNw, null);
        }

    }

    // Graph Implementation
    private static class GridIndexedGraph<N extends Nodal> implements Graph<N> {

        private final SpatialIndex<N> index;

        private GridIndexedGraph(SpatialIndex<N> index) {

            this.index = index;
        }


        public List<InternalNode<N>> getNodesAt(Nodal loc) {
            return Collections.unmodifiableList(this.index.getObjectAt(loc));
        }


        public Iterator<InternalNode<N>> iterator() {
            return this.index.getObjects();
        }


        public List<InternalNode<N>> getClosestNodes(Nodal loc, int num, int distance) {
            return Collections.unmodifiableList(this.index.getNClosest(loc, num, distance));
        }


        public InternalNode<N> getInternalNode(N node) {
            for (InternalNode<N> nw : this.index.getObjectAt(node)) {
                if (nw.getWrappedNodal().equals(node)) {
                    return nw;
                }
            }
            return null;
        }


        public OutEdgeIterator<N> getOutGoingEdges(InternalNode<N> node, ContextualReachability contextualReachability) {

            return new OutEdgeIteratorImpl<N>(this, node, contextualReachability);
        }
    }


    private static class OutEdgeIteratorImpl<N extends Nodal> implements OutEdgeIterator<N> {

        final NodeWrapper<N> fromNw;
        final GridIndexedGraph<N> graph;
        int i = -1;

        private final ContextualReachability contextualReachability;

        private OutEdgeIteratorImpl(GridIndexedGraph<N> graph, InternalNode<N> from, ContextualReachability contextualReachability) {

            this.graph = graph;
            this.fromNw = (NodeWrapper<N>) from;
            this.contextualReachability = contextualReachability;
        }

        public boolean hasNext() {
            return i < fromNw.toNodes.length;
        }

        public N next() {

            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return fromNw.toNodes[++i].getWrappedNodal();
        }

        public InternalNode<N> nextInternalNode() {

            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return fromNw.toNodes[++i];
        }

        public void remove() {

            throw new UnsupportedOperationException();
        }

    }


}
