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

import org.geolatte.graph.LocatedNode;
import org.geolatte.graph.Located;

/**
 * <p>
 * A heuristic relaxer that takes into account:
 * - The path cost (from source to the current locatedNode),
 * - A heuristic calculated by a given strategy.
 *
 * For example, the {@link DistanceHeuristicStrategy} can be used to take the straight-line distance to the target locatedNode
 * as heuristic value.
 * </p>
 *
 * @author Karel Measen
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
class HeuristicRelaxer<N extends Located, M> extends DefaultRelaxer<N, M> {

    private final float heuristicWeight; // weight given to the heuristic component
    private final N destination;
    private HeuristicStrategy<N> heuristicStrategy;

    /**
     * Constructs a heuristic relaxer
     *
     * @param heuristicWeight   The weight for the heuristic component.
     * @param destination       The final destination locatedNode.
     * @param heuristicStrategy The strategy used to calculate the heuristic.
     */
    protected HeuristicRelaxer(float heuristicWeight, N destination, HeuristicStrategy<N> heuristicStrategy) {

        this.heuristicWeight = heuristicWeight;
        this.destination = destination;
        this.heuristicStrategy = heuristicStrategy;
    }

    protected float update(LocatedNode<N> nd, float weight) {

        return weight + this.heuristicWeight * (heuristicStrategy.getValue(nd.getWrappedNodal(), destination));
    }



}
