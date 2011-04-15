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

import org.geolatte.graph.InternalNode;
import org.geolatte.graph.Nodal;

/**
 * <p>
 * No comment provided yet for this class.
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
class HeuristicRelaxer<N extends Nodal, M> extends DefaultRelaxer<N, M> {

        private final float heuristicWeight; // weight given to the heuristic
        // component
        private final float factor; // factor for distance
        private final N destination;

        protected HeuristicRelaxer(float heuristicWeight, float factor,
                                 N destination) {
            this.heuristicWeight = heuristicWeight;
            this.factor = factor;
            this.destination = destination;
        }

        protected float update(InternalNode<N> nd, float weight) {
            return weight + this.heuristicWeight * this.factor
                    * (getDistance(nd, this.destination));
        }

        protected float getDistance(Nodal f, Nodal t) {
            double dx = (double) (f.getX() - t.getX());
            double dy = (double) (f.getY() - t.getY());
            return (float) Math.sqrt(dx * dx + dy * dy);
        }

    }
