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
 * <p>
 * Allows routing algoritmhs to dynamically determine reachability of connected nodes in a graph. This is useful when
 * reachability rules cannot be captured by a static graph but depend on previous actions of the algorithm.
 * <p/>
 * Before querying the reachability of a node ({@link #isReachable(InternalNode)}, one must set the current context:
 * - The traversal: how is the current node reached
 * <p/>
 * <p/>
 * Allows to indicate whether a node v is reachable from a node u when there exists an edge from u to v in the context
 * of a given (partially calculated) path through the graph.
 * <p/>
 * Additional state information can be recorded by
 * implementors of this interface.
 * <p/>
 * <p/>
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public interface ContextualReachability<N> {

    boolean isReachable(InternalNode<N> node);

    void setContext(Traversal<N> pu);

    void setOriginDestination(N origin, N destination);
}
