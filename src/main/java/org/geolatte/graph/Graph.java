package org.geolatte.graph;

import java.util.Iterator;
import java.util.List;

/**
 *
 * @param <N> User type that is modelled as nodes
 * @param <E> User type that is modelled as edges
 * @param <M> The type that represents the mode (connectivity is a function of mode)
 * @param <S> The type that represents the current state of an execution of a <code>GraphAlgorithm</code>
 */
public interface Graph<N extends Nodal, E extends EdgeLabel<M>, M, S> extends Iterable<InternalNode<N>> {

    public Iterator<InternalNode<N>> iterator();

    public InternalNode<N> getInternalNode(N node);

    public List<InternalNode<N>> getNodesAt(Nodal loc);

    public List<InternalNode<N>> getClosestNodes(Nodal loc, int num, int distance);

    public OutEdgeIterator<N, E, M> getOutGoingEdges(InternalNode<N> node, M modus, S state);

    /**
     * Get the Edges connected to the e
     *
     * @param startNode
     * @param endNode
     * @param Modus
     * @return
     */
    public OutEdgeIterator<N,E, M> getConnectedEdges(InternalNode<N> startNode, InternalNode<N> endNode, M Modus);

    public E getEdgeLabel(InternalNode<N> fromNode, InternalNode<N> toNode);


}
