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
 * Decorator that allows to store total weight and a predecessor reference for a node. An instance of PredGraph
 * represents a node in a path through a graph which can be followed back to a source node (which has no predecessor)
 *
 * @author Karel Maesen
 * @author Bert Vanhooff
 *
 * @param <N> The type of node.
 */
public interface PredGraph<N extends Locatable> {

    /**
     * Sets the predecessor of the this node.
     * @param pred The predecessor.
     */
    void setPredecessor(PredGraph<N> pred);

    /**
     * @return The predecessor of this node. Null if this is the first element of the path.
     */
    PredGraph<N> getPredecessor();

    /**
     * Sets the weight of this node. This usually takes into account its predecessors (the path before this node).
     *
     * @param d The new weight.
     */
    void setWeight(float d);

    /**
     * @return The weight of this node.
     */
    float getWeight();

    /**
     * @return The node that is decorated by this predecessor graph.
     */
    InternalNode<N> getInternalNode();

}