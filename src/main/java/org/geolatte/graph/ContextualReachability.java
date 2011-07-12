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
 * <p>
 * Allows algorithms to dynamically determine reachability of connected nodes in a graph. Can be used in a
 * context-aware
 * filter on the outgoing edges of a given node. This is useful when reachability rules cannot be captured by a static
 * graph but depend on previous actions of the algorithm.
 * </p>
 * <p/>
 * <p>
 * Before querying the reachability of a node ({@link #isReachable(InternalNode)}, one must set the current context (
 * this is usually done by the graph algorithm).
 * </p>
 * <p/>
 * <p>
 * Implementers can use the available context information to determine whether a node is reachable.
 * </p>
 * <p/>
 * <p>
 * <i><b>TODO: Document thread safety requirements</b></i>
 * </p>
 *
 * @param <N> The domain node type.
 * @param <C> The type of the context object.
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public interface ContextualReachability<N, C> {

    /**
     * Determines whether there exists a usable edge from the current node (given by the set context, see
     * {@link #setContext(Object)} to the given node.
     *
     * @param node The node.
     * @return True if the given node is reachable, false otherwise.
     */
    boolean isReachable(InternalNode<N> node);

    /**
     * Sets the current context.
     *
     * @param context The current context.
     */
    void setContext(C context);
}
