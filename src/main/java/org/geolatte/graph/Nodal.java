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
 * <code>Nodal</code> captures minimum requirements on the myNodes
 * in a <code>Graph</code>.
 * <p/>
 * <p><em>Implementations should be thread-safe</em>. The <code>getData()</code>-method
 * is intended to be used concurrently by threads executing some graph algorithm.</p>
 *
 * @author Karel Maesen, Geovise BVBA
 */
public interface Nodal<D> {

    /**
     * Returns The X-coordinate of the internalNode
     *
     * @return X-coordinate
     */
    public int getX();

    /**
     * Returns the Y-coordinate of the internalNode
     *
     * @return Y-coordinate
     */
    public int getY();


}
