package org.geolatte.graph;

/**
 * @author Karel Maesen, Geovise BVBA
 *         creation-date: Apr 25, 2010
 */
public interface InternalNode<N extends Nodal> extends Nodal {
    N getWrappedNodal();

    void addEdge(InternalNode<N> toNode, Object label, float weight);

    int getX();

    int getY();

    void addReachableFrom(NodeWrapper<N> fromNode);

}
