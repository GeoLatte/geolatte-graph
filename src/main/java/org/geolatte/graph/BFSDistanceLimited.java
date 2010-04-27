package org.geolatte.graph;

import java.util.*;

public class BFSDistanceLimited<N extends Nodal, E> implements GraphAlgorithm<Map<N, Float>> {

    final Set<BFSState<N>> blackNodes = new HashSet<BFSState<N>>();
    final Queue<BFSState<N>> greyNodes = new LinkedList<BFSState<N>>();
    final InternalNode<N> source;
    final float maxDistance;
    final Graph<N, E> graph;
    Map<N, Float> result;

    protected BFSDistanceLimited(Graph<N, E> graph, N source, float maxDistance) {
        this.graph = graph;
        this.source = graph.getInternalNode(source);
        this.maxDistance = maxDistance;
    }


    public void execute() {

        BFSState<N> ws = new BFSState<N>(this.source);
        ws.distance = 0.f;
        ws.predecessor = null;
        greyNodes.add(ws);

        while (!greyNodes.isEmpty()) {
            BFSState<N> wu = this.greyNodes.remove();

            if (wu.distance > maxDistance) {
                continue; //don't expand when the internalNode is beyond maximum distance.
            }

            OutEdgeIterator<N, E> outEdges = this.graph.getOutGoingEdges(wu.internalNode);
            while (outEdges.next()) {
                InternalNode<N> v = outEdges.getToInternalNode();
                BFSState<N> wv = new BFSState<N>(v);
                if (greyNodes.contains(wv) || blackNodes.contains(wv)) {
                    continue; // do nothing
                } else {
                    wv.distance = wu.distance + outEdges.getWeight();
                    wv.predecessor = wu.internalNode;
                    greyNodes.add(wv);
                }
            }
            blackNodes.add(wu);
        }

        this.result = toMap(blackNodes);

    }


    public Map<N, Float> getResult() {
        return this.result;
    }

    protected Map<N, Float> toMap(Set<BFSState<N>> nodes) {
        Map<N, Float> map = new HashMap<N, Float>(nodes.size());
        for (BFSState<N> wu : nodes) {
            map.put(wu.internalNode.getWrappedNodal(), wu.distance);
        }
        return map;

    }

    private static class BFSState<N extends Nodal> {

        final InternalNode<N> internalNode;
        float distance = Float.POSITIVE_INFINITY;
        InternalNode<N> predecessor;

        private BFSState(InternalNode<N> internalNode) {
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
