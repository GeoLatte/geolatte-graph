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

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the Extent class.
 *
 * @author Bert Vanhooff [<a href="http://www.qmino.com">Qmino bvba</a>]
 */
public class ExtentTest {

    @Test
    public void testConstructor() {

        Extent extent = new Extent(1, 3, 6, 9);
        Assert.assertEquals(extent.getMinX(), 1, 0.01);
        Assert.assertEquals(extent.getMinY(), 3, 0.01);
        Assert.assertEquals(extent.getMaxX(), 6, 0.01);
        Assert.assertEquals(extent.getMaxY(), 9, 0.01);

        Assert.assertEquals(extent.getWidth(), 5, 0.01);
        Assert.assertEquals(extent.getHeight(), 6, 0.01);
    }

    @Test
    public void testConstructorExceptions() {

        new Extent(1, 1, 1, 1);

        // minX > maxX
        try {
            new Extent(1, 3, 0, 9);
        } catch (IllegalArgumentException e) {
            ; // expected
        }

        // minY > maxY
        try {
            new Extent(1, 3, 2, 2);
        } catch (IllegalArgumentException e) {
            ; // expected
        }
    }
}
