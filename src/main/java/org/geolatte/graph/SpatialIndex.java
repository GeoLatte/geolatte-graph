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

import org.geolatte.geom.Envelope;

import java.util.Iterator;
import java.util.List;

/**
 * A spatial index is used to optimize spatial queries over a set of locatable nodes.
 *
 * @param <T> The type of the domain nodes.
 */
public interface SpatialIndex<T extends Locatable> {

    /**
     * Checks whether the given node is contained in the index.
     *
     * @param node The node to search for.
     * @return True if the given node is contained in the index, false otherwise.
     */
    public boolean contains(T node);

    /**
     * @param envelope The bounds within which to search for nodes.
     * @return A list of all nodes within the given envelope
     */
    public List<T> query(Envelope envelope);

    /**
     * Searches the nodes closest to the given center in straight line distance.
     *
     * @param center      The center from which to start the search.
     * @param num         The (max) number nodes to find.
     * @param maxDistance The maximum distance to search (inclusive).
     * @return A list of closest nodes sorted according to ascending distance.
     */
    public List<T> getNClosest(Locatable center, int num, float maxDistance);

    /**
     * An iterator over all nodes in the index.
     *
     * @return An internal node iterator.
     */
    public Iterator<T> getNodes();

    /**
     * Gets the nodes at the given location.
     *
     * @param loc The location. Null or a location outside the index envelope are allowed, this will yield an empty
     *            list.
     * @return A list of nodes on the given location.
     */
    public List<T> getNodeAt(Locatable loc);

}
