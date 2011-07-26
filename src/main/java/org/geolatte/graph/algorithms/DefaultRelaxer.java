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

package org.geolatte.graph.algorithms;

import org.geolatte.graph.InternalNode;
import org.geolatte.graph.PredGraph;

/**
 * <p>
 * Default relaxer simply uses the edge weights to determine whether the shortest path can be improved.
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
class DefaultRelaxer<N, E> implements Relaxer<N, E> {

    private float newWeight;

    public boolean relax(PredGraph<N, E> u, PredGraph<N, E> v, int weightIndex) {

        float r = u.getWeight() + u.getInternalNode().getWeightTo(v.getInternalNode(), weightIndex);
        if (r < v.getWeight()) {
            v.setWeight(r);
            v.setPredecessor(u);
            newWeight = update(v.getInternalNode(), v.getWeight());
            return true;
        } else {
            newWeight = v.getWeight();
            return false;
        }
    }

    /**
     * Method called from {@link #relax(org.geolatte.graph.PredGraph, org.geolatte.graph.PredGraph, int)} to
     * determine the new weight for the given node.
     *
     * @param nd         The node.
     * @param baseWeight The new base weight.
     * @return A new weight.
     */
    protected float update(InternalNode<N, E> nd, float baseWeight) {

        return baseWeight;
    }

    public float newTotalWeight() {

        return newWeight;
    }

}
