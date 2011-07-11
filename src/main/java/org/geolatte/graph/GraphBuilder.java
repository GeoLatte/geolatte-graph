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
 * A builder for graphs.
 *
 * @param <N> The type of node.
 * @author Karel Maesen
 * @author Bert Vanhooff
 */
public interface GraphBuilder<N extends Locatable> {

    /**
     * Adds the given nodes and a directed edge with the given label between.
     *
     * @param fromNode   The node from which the edge starts.
     * @param toNode     The destination node for the edge.
     * @param edgeWeight The weight.
     */
    public void addEdge(N fromNode, N toNode, EdgeWeight edgeWeight);

    /**
     * Builds the graph.
     *
     * @return The graph.
     * @throws BuilderException If the graph could not be built.
     */
    public LocateableGraph<N> build() throws BuilderException;

}
