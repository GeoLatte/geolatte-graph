/*
 * This file is part of the GeoLatte project.
 *
 * This code is licenced under the Apache License, Version 2.0 (the "License");
 * you may not use  this file except in compliance with the License. You may obtain
 * a copy of the License at  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software  distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright @ 2010 Geovise BVBA, QMINO BVBA.
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
//        assertEquals(Float.valueOf(10f), minQueue.extractMin());
//        minQueue.add(Float.valueOf(1.0f), 1.0f);
//        assertEquals(Float.valueOf(1f), minQueue.extractMin());
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
//        Float minValue = minQueue.extractMin();
//        assertEquals(value, minValue);
//    }
//
//
//
//
//}
