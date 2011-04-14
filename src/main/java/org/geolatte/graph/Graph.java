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

import java.util.Iterator;
import java.util.List;

/**
 *
 * @param <N> The type that represents the nodes.
 * @param <E> The type that represents the edges.
 * @param <M> The type that represents the mode (connectivity is a function of mode).
 */
public interface Graph<N extends Nodal, E extends EdgeLabel<M>, M> extends Iterable<InternalNode<N>> {

    public Iterator<InternalNode<N>> iterator();

    public InternalNode<N> getInternalNode(N node);

    public List<InternalNode<N>> getNodesAt(Nodal loc);

    public List<InternalNode<N>> getClosestNodes(Nodal loc, int num, int distance);

    /**
     * Gets the edges that start from the given node, depending on the given modus.
     * @param node  The node.
     * @param modus The modus.
     * @return A node iterator for the outgoing nodes.
     */
    public OutEdgeIterator<N, E> getOutGoingEdges(InternalNode<N> node, M modus);

    /*
     * I have no clue what this method is supposed to do.
     * Get the Edges connected to the e
     *
     * @param startNode
     * @param endNode
     * @param Modus
     * @return
     */
    // public OutEdgeIterator<N, E, M> getConnectedEdges(InternalNode<N> startNode, InternalNode<N> endNode, M Modus);

    public E getEdgeLabel(InternalNode<N> fromNode, InternalNode<N> toNode);

    public M getModus();

}
