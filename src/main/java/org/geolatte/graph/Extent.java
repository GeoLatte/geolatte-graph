/*
 * This file is part of the GeoLatte project.
 *
 *      GeoLatte is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU Lesser General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      GeoLatte is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU Lesser General Public License for more details.
 *
 *      You should have received a copy of the GNU Lesser General Public License
 *      along with GeoLatte.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright (C) 2010 - 2011 and Ownership of code is shared by:
 * Qmino bvba - Esperantolaan 4 - 3001 Heverlee  (http://www.qmino.com)
 * Geovise bvba - Generaal Eisenhowerlei 9 - 2140 Antwerpen (http://www.geovise.com)
 */

package org.geolatte.graph;

/**
 * Defines an area in a 2D plane.
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 */
public class Extent {

    private final double minX;
    private final double maxX;
    private final double minY;
    private final double maxY;

    /**
     * Creates an Extent with lower left point (minX, minY) and upper right point (maxX, maxY).
     *
     * @param minX The minimal X coordinate.
     * @param minY The minimal Y coordinate.
     * @param maxX The maximal X coordinate.
     * @param maxY The maximal Y coordinate.
     *
     * @throws IllegalArgumentException When minX > maxX or minY > maxY
     */
    public Extent(double minX, double minY, double maxX, double maxY) {
        if (minX > maxX || minY > maxY)
            throw new IllegalArgumentException("Valid extent requires minX <= maxX and minY <= maxY");
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    /**
     * @return The minimal X coordinate.
     */
    public double getMinX() {
        return minX;
    }

    /**
     * @return The minimal Y coordinate.
     */
    public double getMinY() {
        return minY;
    }

    /**
     * @return The maximal X coordinate.
     */
    public double getMaxX() {
        return maxX;
    }

    /**
     * @return The maximal Y coordinate.
     */
    public double getMaxY() {
        return maxY;
    }

    /**
     * @return The width of the extent.
     */
    public double getWidth() {
        return maxX - minX;
    }

    /**
     * @return The height of the extent.
     */
    public double getHeight() {
        return maxY - minY;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("LL: ")
               .append(minX)
               .append(",")
               .append(minY)
               .append(" - UR: ")
               .append(maxX)
               .append(",")
               .append(maxY);
        return builder.toString();
    }
}
