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
 * Representation of a node in a graph, which can be connected to other nodes through edges.
 * This is a decorator {@link Nodal}.
 *
 * @author Karel Maesen
 * @author Bert Vanhooff
 */
public interface InternalNode<N extends Nodal> extends Nodal {

    /**
     * @return The decorated Nodal
     */
    N getWrappedNodal();

    /**
     * Creates an edge from this node to a given node.
     * @param toNode The node to connect to.
     * @param label  An arbitrary object that will be associated to the edge.
     */
    void addEdge(InternalNode<N> toNode, Object label);

    void addReachableFrom(InternalNode<N> fromNode);

    int getX();

    int getY();

}
