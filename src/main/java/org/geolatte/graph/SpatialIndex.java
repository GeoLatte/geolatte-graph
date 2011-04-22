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

import com.vividsolutions.jts.geom.Envelope;

import java.util.Iterator;
import java.util.List;

/**
 * A spatial indexes is used to optimize spatial queries.
 *
 * @param <N>
 */
public interface SpatialIndex<N extends Nodal> {

    /**
     * Checks whether the given node is contained in the index.
     *
     * @param node The node to search for.
     * @return True if the given node is contained in the index, false otherwise.
     */
    public boolean contains(InternalNode<N> node);

    /**
     * @param envelope The bounds within which to search for nodes.
     * @return A list of all nodes within the given envelope
     */
    public List<InternalNode<N>> query(Envelope envelope);

    /**
     * Searches the given number of node closest to the given node, within a maximum distance.
     * 
     * @param nodal       The center node.
     * @param num         The number of closest nodes to find.
     * @param maxDistance The maximum distance to search in.
     * @return A list of closest nodes.
     */
    public List<InternalNode<N>> getNClosest(Nodal nodal, int num, int maxDistance);

    public Iterator<InternalNode<N>> getObjects();

    public List<InternalNode<N>> getObjectAt(Nodal loc);

}
