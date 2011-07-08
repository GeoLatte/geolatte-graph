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

    public static <N extends Located> GraphBuilder<N> createGridIndexedGraphBuilder(Envelope env, int resolution) {

        return new GridIndexedGraphBuilder<N>(env, resolution);
    }

    // Builder implementation
    private static class GridIndexedGraphBuilder<N extends Located> implements GraphBuilder<N> {

        private final SpatialIndexBuilder<N> indexBuilder;
        private final Map<N, Node<N>> map = new HashMap<N, Node<N>>(); // map is used to quickly locate Nodes based on node equality.

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
            Node<N> fNw = this.map.get(fromNode);
            Node<N> toNw = this.map.get(toNode);
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
            fNw.addEdge(toNw, null, edgeWeight);
        }

    }

    // Graph Implementation
    private static class GridIndexedGraph<N extends Located> implements Graph<N> {

        private final SpatialIndex<N> index;

        private GridIndexedGraph(SpatialIndex<N> index) {

            this.index = index;
        }


        public List<Node<N>> getNodesAt(Located loc) {
            return Collections.unmodifiableList(this.index.getObjectAt(loc));
        }


        public Iterator<Node<N>> iterator() {
            return this.index.getObjects();
        }


        public List<Node<N>> getClosestNodes(Located loc, int num, int distance) {
            return Collections.unmodifiableList(this.index.getNClosest(loc, num, distance));
        }


        public Node<N> getInternalNode(N node) {
            for (Node<N> nw : this.index.getObjectAt(node)) {
                if (nw.getWrappedNodal().equals(node)) {
                    return nw;
                }
            }
            return null;
        }


        public OutEdgeIterator<N> getOutGoingEdges(Node<N> node, ContextualReachability contextualReachability) {

            return new OutEdgeIteratorImpl<N>(this, node, contextualReachability);
        }
    }


    private static class OutEdgeIteratorImpl<N extends Located> implements OutEdgeIterator<N> {

        final NodeWrapper<N> fromNw;
        final Graph<N> graph;
        int i = -1;

        private final ContextualReachability contextualReachability;

        private OutEdgeIteratorImpl(Graph<N> graph, Node<N> from, ContextualReachability contextualReachability) {

            this.graph = graph;
            this.fromNw = (NodeWrapper<N>) from;
            this.contextualReachability = contextualReachability;
        }

        public boolean hasNext() {
            return i < fromNw.toNodes.length-1;
        }

        public N next() {

            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return fromNw.toNodes[++i].getWrappedNodal();
        }

        public Node<N> nextInternalNode() {

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
