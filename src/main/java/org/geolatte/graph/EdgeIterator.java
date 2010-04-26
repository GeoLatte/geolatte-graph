package org.geolatte.graph;

public interface EdgeIterator<N extends Nodal, E> {

    public boolean next();

    public E getEdgeLabel();

    public float getWeight();

    public InternalNode<N> getInternalNode();

}
