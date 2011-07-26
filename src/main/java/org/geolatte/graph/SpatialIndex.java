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

import com.vividsolutions.jts.geom.Envelope;

import java.util.Iterator;
import java.util.List;

/**
 * A spatial indexes is used to optimize spatial queries.
 *
 * @param <N> The type of the domain nodes.
 * @param <E> The edge label type.
 */
public interface SpatialIndex<N extends Locatable, E> {

    /**
     * Checks whether the given internalNode is contained in the index.
     *
     * @param internalNode The internalNode to search for.
     * @return True if the given internalNode is contained in the index, false otherwise.
     */
    public boolean contains(InternalNode<N, E> internalNode);

    /**
     * @param envelope The bounds within which to search for nodes.
     * @return A list of all nodes within the given envelope
     */
    public List<InternalNode<N, E>> query(Envelope envelope);

    /**
     * Searches the given number of node closest to the given node, within a maximum distance.
     *
     * @param locatable   The center node.
     * @param num         The number of closest nodes to find.
     * @param maxDistance The maximum distance to search in.
     * @return A list of closest nodes.
     */
    public List<InternalNode<N, E>> getNClosest(Locatable locatable, int num, int maxDistance);

    /**
     * An iterator over all internal nodes in the index.
     *
     * @return An internal node iterator.
     */
    public Iterator<InternalNode<N, E>> getInternalNodes();

    /**
     * Gets the internal node with the given location.
     *
     * @param loc The location.
     * @return An internal node.
     */
    public List<InternalNode<N, E>> getNodeAt(Locatable loc);

}
