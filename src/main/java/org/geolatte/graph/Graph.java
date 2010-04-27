package org.geolatte.graph;

import java.util.Iterator;
import java.util.List;


public interface Graph<N extends Nodal, E> extends Iterable<InternalNode<N>> {

    public Iterator<InternalNode<N>> iterator();

    public InternalNode<N> getInternalNode(N node);

    public List<InternalNode<N>> getNodesAt(Nodal loc);

    public List<InternalNode<N>> getClosestNodes(Nodal loc, int num, int distance);

    public OutEdgeIterator<N, E> getOutGoingEdges(InternalNode<N> node);

    public E getEdgeLabel(InternalNode<N> fromNode, InternalNode<N> toNode);


}
