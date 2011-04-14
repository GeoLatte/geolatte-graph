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

    public static <N extends Nodal, E extends EdgeLabel<M>, M> GraphBuilder<N, E, M> createGridIndexedGraphBuilder(Envelope env, int resolution) {

        return new GridIndexedGraphBuilder<N, E, M>(env, resolution);
    }

    // Builder implementation
    private static class GridIndexedGraphBuilder<N extends Nodal, E extends EdgeLabel<M>, M> implements GraphBuilder<N, E, M> {

        private final SpatialIndexBuilder<N> indexBuilder;
        private final Map<N, InternalNode<N>> map = new HashMap<N, InternalNode<N>>(); // map is used to quickly locate Nodes based on node equality.

        private GridIndexedGraphBuilder(Envelope env, int resolution) {

            this.indexBuilder = SpatialIndexes.createGridIndexBuilder(env, resolution);
        }


        public Graph<N, E, M> build() throws BuilderException {

            if (map.isEmpty()) {
                throw new IllegalStateException("No nodes added since last built");
            }

            map.clear(); // empty to save on memory.
            SpatialIndex<N> index = this.indexBuilder.build();
            // TODO: 2 last arguments cannot just be null
            return new GridIndexedGraph<N, E, M>(index, null, null);
        }

        public void addEdge(N fromNode, N toNode, E label) {

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
            fNw.addEdge(toNw, label, label.getWeight(null));
        }

    }

    // Graph Implementation
    private static class GridIndexedGraph<N extends Nodal, E extends EdgeLabel<M>, M> implements Graph<N, E, M> {

        private final SpatialIndex<N> index;
        private final ContextualReachability contextualReachability;
        private final M modus;


        private GridIndexedGraph(SpatialIndex<N> index, M modus, ContextualReachability contextualReachability) {

            this.index = index;
            this.modus = modus;
            this.contextualReachability = contextualReachability;
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


        public OutEdgeIterator<N, E> getOutGoingEdges(InternalNode<N> node, M Modus) {

            return new OutEdgeIteratorImpl<N, E, M>(this, node, contextualReachability);
        }

        public E getEdgeLabel(InternalNode<N> from, InternalNode<N> to) {
            OutEdgeIterator<N, E> outEdges = getOutGoingEdges(from, modus);
            while (outEdges.next()) {
                if (outEdges.getToInternalNode().equals(to)) {
                    return outEdges.getEdgeLabel();
                }
            }
            throw new IllegalArgumentException(String.format("No Edge between nodes: %s and %s", from.getWrappedNodal(), to.getWrappedNodal()));
        }

        public M getModus() {
            return modus;
        }
    }


    private static class OutEdgeIteratorImpl<N extends Nodal, E extends EdgeLabel<M>, M> implements OutEdgeIterator<N, E> {

        final NodeWrapper<N> fromNw;
        final GridIndexedGraph<N, E, M> graph;
        int i = -1;

        private final ContextualReachability contextualReachability;

        private OutEdgeIteratorImpl(GridIndexedGraph<N, E, M> graph, InternalNode<N> from, ContextualReachability contextualReachability) {

            this.graph = graph;
            this.fromNw = (NodeWrapper<N>) from;
            this.contextualReachability = contextualReachability;
        }


        public E getEdgeLabel() {
            return (E) fromNw.toLabels[i];
        }

        public InternalNode<N> getToInternalNode() {
            return fromNw.toNodes[i];
        }

        public boolean next() {
            i++;
            return i < fromNw.toNodes.length;
        }

    }


}
