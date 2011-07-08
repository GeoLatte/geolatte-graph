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

package org.geolatte.graph.algorithms;

import org.geolatte.graph.Node;
import org.geolatte.graph.Located;

/**
 * <p>
 * A heuristic relaxer that takes into account:
 * - The path cost (from source to the current node)
 * - A heuristic estimate of the distance to the destination.
 *
 * The heuristic estimate in this case is the straight-line distance to the destination node.
 * </p>
 *
 * @author Karel Measen
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
class HeuristicRelaxer<N extends Located, M> extends DefaultRelaxer<N, M> {

    private final float heuristicWeight; // weight given to the heuristic component
    private final float factor; // factor for distance
    private final N destination;

    /**
     * Constructs a heuristic relaxer
     *
     * @param heuristicWeight The weight for the heuristic component.
     * @param factor          The factor for the distance.
     * @param destination     The final destination node.
     */
    protected HeuristicRelaxer(float heuristicWeight, float factor, N destination) {

        this.heuristicWeight = heuristicWeight;
        this.factor = factor;
        this.destination = destination;
    }

    protected float update(Node<N> nd, float weight) {

        return weight + this.heuristicWeight * this.factor * (getDistance(nd, this.destination));
    }

    /**
     * Gets the straight-line distance between two nodes.
     *
     * @param n1 The first node.
     * @param n2 The second node.
     *
     * @return The straight line distance between the nodes.
     */
    protected float getDistance(Located n1, Located n2) {

        double dx = (double) (n1.getX() - n2.getX());
        double dy = (double) (n1.getY() - n2.getY());
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

}
