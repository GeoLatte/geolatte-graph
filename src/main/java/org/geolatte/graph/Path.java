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

/**
 * A path between a source and destination node. All nodes from the path can be iterated from source to destination.
 *
 * @param <N> The type of the domain node.
 * @author Karel Maesen
 * @author Bert Vanhooff
 */
public interface Path<N> extends Iterable<N> {

    /**
     * Gets the total weight of the path.
     *
     * @return The total weight of the path.
     */
    public float totalWeight();

    /**
     * Gets the source node.
     *
     * @return The source node.
     */
    public N getSource();

    /**
     * Gets the destination node.
     *
     * @return The destination node.
     */
    public N getDestination();

    /**
     * Gets a value indicating whether this path is valid.
     *
     * @return True if this path is valid, false otherwise.
     */
    public boolean isValid();

}
