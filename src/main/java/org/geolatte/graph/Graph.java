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
 * Qmino bvba - Esperantolaan 4 - 3001 Heverlee  (http://www.qmino.com)
 * Geovise bvba - Generaal Eisenhowerlei 9 - 2140 Antwerpen (http://www.geovise.com)
 */

package org.geolatte.graph;

import java.util.Iterator;

/**
 * Representation of a graph of internal nodes.
 *
 * @param <N> The type that represents the nodes.
 * @param <E> The edge label type.
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public interface Graph<N, E> extends Iterable<InternalNode<N, E>> {

    /**
     * Gets the internal node that represents the given domain node.
     *
     * @param node The domain node.
     * @return The internal node that corresponds to the domain node.
     */
    InternalNode<N, E> getInternalNode(N node);

    /**
     * Gets the edges that start from the given internalNode, depending on the given modus.
     *
     * @param internalNode           The internalNode.
     * @param contextualReachability An object to determine whether an edge can be used. Can be null.
     * @return A internalNode iterator for the outgoing nodes.
     */
    Iterator<InternalNode<N, E>> getOutGoingEdges(InternalNode<N, E> internalNode, ContextualReachability<N, E, ?> contextualReachability);
    // TODO : getOutgoingEdges -> return iterable so it can be used in for loops

    // TODO: add this? would be very convenient for clients
    //Iterator<N> getOutgoingEdges(N node, ContextualReachability<N, ?> contextualReachability);

    /**
     * Gets the edges that end at the given internalNode, depending on the given modus.
     *
     * @param internalNode           The internalNode.
     * @param contextualReachability An object to determine whether an edge can be used. Can be null.
     * @return A internalNode iterator for the incoming nodes.
     */
    Iterator<InternalNode<N, E>> getInComingEdges(InternalNode<N, E> internalNode, ContextualReachability<N, E, ?> contextualReachability);

}
