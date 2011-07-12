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
 * Created by IntelliJ IDEA.
 * User: maesenka
 * Date: Apr 22, 2010
 * Time: 5:18:57 PM
 * To change this template use File | Settings | File Templates.
 */
//public class TestPMinQueue {
//
//    PMinQueue<Float> minQueue;
//
//    @Before
//    public void setup(){
//        minQueue = new PMinQueue<Float>();
//        minQueue.add(Float.valueOf(100f), 100f);
//        minQueue.add(Float.valueOf(50f), 50f);
//        minQueue.add(Float.valueOf(10f), 10f);
//        minQueue.add(Float.valueOf(50f), 50f);
//        minQueue.add(Float.valueOf(40f), 40f);
//        minQueue.add(Float.valueOf(80f), 80f);
//        minQueue.add(Float.valueOf(30f), 30f);
//        minQueue.add(Float.valueOf(120f), 120f);
//    }
//
//    @Test
//    public void test_min_queue_min_order() {
//        assertEquals(Float.valueOf(10f), minQueue.deleteMin());
//        minQueue.add(Float.valueOf(1.0f), 1.0f);
//        assertEquals(Float.valueOf(1f), minQueue.deleteMin());
//    }
//
//    @Test
//    public void test_get_value() {
//        Float f = minQueue.get(Float.valueOf(80f));
//        assertEquals(Float.valueOf(80f), f);
//    }
//
//    @Test
//    public void test_isEmpty() {
//        assertTrue(!minQueue.isEmpty());
//        PMinQueue<Float> emptyQueue = new PMinQueue<Float>();
//        assertTrue(emptyQueue.isEmpty());
//    }
//
//
//    @Test
//    public void test_update() {
//        Float value = Float.valueOf(80f);
//        minQueue.update(value, 0.0f);
//        Float minValue = minQueue.deleteMin();
//        assertEquals(value, minValue);
//    }
//
//
//
//
//}
