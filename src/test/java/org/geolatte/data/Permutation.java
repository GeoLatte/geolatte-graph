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

package org.geolatte.data;

/**
 * Static utility to generate a random permutation of input - used in test classes.
 */
public class Permutation {

    public static int[] randomPermutation(int[] inAr) {
        int[] outAr = (int[]) inAr.clone();

        for (int k = outAr.length - 1; k > 0; k--) {
            int w = (int) Math.floor(Math.random() * (k + 1));
            int temp = outAr[w];
            outAr[w] = outAr[k];
            outAr[k] = temp;
        }
        return outAr;
    }

    public static int[] randomPermutation(int length) {
        int[] outAr = new int[length];
        for (int i = 0; i < length; i++) {
            outAr[i] = i;
        }
        return randomPermutation(outAr);
    }

}
