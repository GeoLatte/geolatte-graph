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
 * Allows routing algorithms to dynamically determine whether a neighboring target node can be reached from a source
 * node, knowing the starting node and the destination node.
 * <p/>
 * TODO: Document thread safety requirements
 *
 * @param <N> The domain node type.
 * @param <C> The type of the context object.
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public interface RoutingContextualReachability<N, C> extends ContextualReachability<N, C> {

    /**
     * Sets the source and destination of the route to be calculated. This method may only be called once during the
     * execution of a routing algorithm.
     *
     * @param origin      The origin node.
     * @param destination The destination node.
     */
    void setOriginDestination(N origin, N destination);
}
