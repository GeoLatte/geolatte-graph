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

package org.geolatte.graph;

import org.junit.Assert;
import org.junit.Test;

/**
 * <p>
 * Tests the {@link ArrayEdgeWeight} class.
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class ArrayEdgeWeightTest {

    @Test
    public void testConstructorGetValue() throws Exception {

        float[] weights = new float[]{1, 2, 3};

        ArrayEdgeWeight arrayEdgeWeight = new ArrayEdgeWeight(weights);

        Assert.assertEquals(1.0f, arrayEdgeWeight.getValue(0), 0.005);
        Assert.assertEquals(2.0f, arrayEdgeWeight.getValue(1), 0.005);
        Assert.assertEquals(3.0f, arrayEdgeWeight.getValue(2), 0.005);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueError() throws Exception {

        float[] weights = new float[]{1, 2, 3};

        ArrayEdgeWeight arrayEdgeWeight = new ArrayEdgeWeight(weights);

        arrayEdgeWeight.getValue(3);
    }
}
