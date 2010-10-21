package org.geolatte.graph;

public interface OutEdgeIterator<N extends Nodal, E, M> {

    public boolean next();

    public E getEdgeLabel();

    public InternalNode<N> getToInternalNode();

}
