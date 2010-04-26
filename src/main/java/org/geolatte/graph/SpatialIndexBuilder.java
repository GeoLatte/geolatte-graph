package org.geolatte.graph;

public interface SpatialIndexBuilder<T extends Nodal> {

    public void insert(InternalNode<T> obj);

    public SpatialIndex<T> build() throws BuilderException;

}
