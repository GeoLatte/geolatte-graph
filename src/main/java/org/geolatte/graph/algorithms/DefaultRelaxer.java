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
import org.geolatte.graph.Locatable;
import org.geolatte.graph.PredGraph;

/**
 * <p>
 * No comment provided yet for this class.
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
class DefaultRelaxer<N extends Locatable, M> implements Relaxer<N, M> {

    float newWeight;

    public boolean relax(PredGraph<N> u, PredGraph<N> v, M modus) {

        // TODO: correct doorgeven van weightKind
        float r = u.getWeight() + u.getInternalNode().getWeightTo(v.getInternalNode(), 0);
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

    protected float update(InternalNode<N> nd, float distance) {

        return distance;
    }

    public float newTotalWeight() {

        return newWeight;
    }

}
