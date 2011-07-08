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
 */
public interface Graph<N> extends Iterable<InternalNode<N>> {

    public Iterator<InternalNode<N>> iterator();

    public InternalNode<N> getInternalNode(N node);

    public List<InternalNode<N>> getNodesAt(Locatable loc);

    public List<InternalNode<N>> getClosestNodes(Locatable loc, int num, int distance);

    /**
     * Gets the edges that start from the given internalNode, depending on the given modus.
     *
     * @param internalNode  The internalNode.
     * @param contextualReachability
     * @return A internalNode iterator for the outgoing nodes.
     */
    public Iterator<InternalNode<N>> getOutGoingEdges(InternalNode<N> internalNode, ContextualReachability contextualReachability);
}
