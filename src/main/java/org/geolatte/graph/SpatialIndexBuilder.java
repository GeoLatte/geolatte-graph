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

/**
 * A builder for spatial indexes.
 *
 * @param <N> The type of the domain node. Must be locatable.
 */
public interface SpatialIndexBuilder<N extends Locatable> {

    /**
     * Adds a node to the index under construction.
     *
     * @param node The node to add.
     */
    public void insert(Locatable node);

    /**
     * Builds the spatial index.
     *
     * @return The spatial index.
     * @throws BuilderException If the spatial index could not be built.
     */
    public SpatialIndex<N> build() throws BuilderException;

}
