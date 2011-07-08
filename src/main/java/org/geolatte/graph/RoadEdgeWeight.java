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
 * Base implementation of the {@link EdgeWeight} interface, providing for the most common weights.
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class RoadEdgeWeight implements EdgeWeight {

    public static int DISTANCE = 1;
    public static int TIME_CAR = 2;
    public static int TIME_TRUCK = 3;
    public static int TIME_FOOT = 4;

    private int[] weights = new int[4];

    public RoadEdgeWeight(int[] weights, int defaultWeight) {

        weights[DISTANCE] = defaultWeight;
        weights[TIME_CAR] = defaultWeight;
        weights[TIME_TRUCK] = defaultWeight;
        weights[TIME_FOOT] = defaultWeight;
    }

    public float getValue(int weightKind) {

        return weights[weightKind];
    }

    public void setWeight(int weightKind, int weight) {

        if (weightKind < 0 || weightKind > 3) {
            return;
        }

        weights[weightKind] = weight;
    }
}
