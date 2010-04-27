package org.geolatte.graph;

public interface OutEdgeIterator<N extends Nodal, E> {

    public boolean next();

    public E getEdgeLabel();

    public float getWeight();

    public InternalNode<N> getToInternalNode();

}
