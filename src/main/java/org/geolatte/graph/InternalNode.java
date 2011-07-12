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
 * Internal representation of a node, used by the various graph algorithms. Should not normally be implemented by
 * clients. Use {@link Graphs} to build graphs based on existing domain objects.
 *
 * @param <N> The domain node type.
 * @author Karel Maesen
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public interface InternalNode<N> {

    /**
     * Gets the wrapped domain node.
     *
     * @return A domain node.
     */
    N getWrappedNode();

    /**
     * Creates an edge from this node to a given node.
     *
     * @param toNode     The node to connect to.
     * @param edgeWeight The weight of the edge between this node and the toNode.
     */
    void addEdge(InternalNode<N> toNode, EdgeWeight edgeWeight);


    void addReachableFrom(InternalNode<N> fromNode);

    /**
     * Gets the edge weight to get from this node to the given <code>toNode</code>, using the given weight index (see
     * also {@link EdgeWeight}).
     *
     * @param toNode      The node to which to get the edge weight.
     * @param weightIndex Indicates which weight to use.
     * @return An edge weight.
     */
    float getWeightTo(InternalNode<N> toNode, int weightIndex);
}
