package org.geolatte.graph;

import com.vividsolutions.jts.geom.Envelope;

import java.util.*;

public class Graphs {

    public static <N extends Nodal, E> GraphBuilder<N, E> createGridIndexedGraphBuilder(Envelope env,
                                                                                        int resolution) {
        return new GridIndexedGraphBuilder<N, E>(env, resolution);
    }

    // Builder implementation
    private static class GridIndexedGraphBuilder<N extends Nodal, E> implements GraphBuilder<N, E> {

        private final SpatialIndexBuilder<N> indexBuilder;
        // map is used to quickly locate Nodes based on MyNode equality.
        private final Map<N, InternalNode<N>> map = new HashMap<N, InternalNode<N>>();

        private GridIndexedGraphBuilder(Envelope env, int resolution) {
            this.indexBuilder = SpatialIndexes.createGridIndexBuilder(env,
                    resolution);
        }


        public Graph<N, E> build() throws BuilderException {
            if (map.isEmpty()) {
                throw new IllegalStateException(
                        "No myNodes added since last built");
            }
            map.clear(); // empty to save on memory.
            SpatialIndex<N> index = this.indexBuilder.build();
            return new GridIndexedGraph<N, E>(index);
        }


        public void addEdge(N fromNode, N toNode, E label, float weight) {


            if (fromNode.equals(toNode)) {
                return;
                //System.err.println("Tried adding zero-length edge");
            }

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

            fNw.addEdge(toNw, label, weight);
        }

    }

    // Graph Implementation
    private static class GridIndexedGraph<N extends Nodal, E> implements Graph<N, E> {

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


        public EdgeIterator<N, E> getOutGoingEdges(InternalNode<N> node) {
            return new EdgeIteratorImpl<N, E>(this, node);
        }


    }


    private static class EdgeIteratorImpl<N extends Nodal, E> implements EdgeIterator<N, E> {

        final NodeWrapper<N> fromNw;
        final GridIndexedGraph<N, E> graph;
        int i = -1;

        private EdgeIteratorImpl(GridIndexedGraph<N, E> graph, InternalNode<N> from) {
            this.graph = graph;
            this.fromNw = (NodeWrapper<N>) from;
        }


        public E getEdgeLabel() {
            return (E) fromNw.toLabels[i];
        }

        public InternalNode<N> getInternalNode() {
            return fromNw.toNodes[i];
        }


        public float getWeight() {
            return fromNw.toWeights[i];
        }

        public boolean next() {
            i++;
            return i < fromNw.toNodes.length;
        }

    }


}
