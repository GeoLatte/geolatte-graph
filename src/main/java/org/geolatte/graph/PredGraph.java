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
 * Represents the end node in a path through a graph which can be followed back to a start node through a set of
 * predecessor nodes.
 *
 * @param <N> The type of domain node.
 * @author Karel Maesen
 * @author Bert Vanhooff
 */
public interface PredGraph<N> extends Traversal<N> {

    /**
     * @return The end node of the predecessor graph.
     */
    InternalNode<N> getInternalNode();

    /**
     * Sets the predecessor of the current node.
     *
     * @param pred The predecessor.
     * @return The given pred.
     */
    PredGraph<N> setPredecessor(PredGraph<N> pred);

    /**
     * @return The predecessor of the current node. Null if this is the first element of the path.
     */
    PredGraph<N> getPredecessor();

    /**
     * Sets the weight up to the current node. This usually takes into account its predecessors (the path before this
     * node).
     *
     * @param d The new weight.
     */
    void setWeight(float d);

    /**
     * Gets the weight of the path to the current node.
     *
     * @return The weight of this node.
     */
    float getWeight();

}