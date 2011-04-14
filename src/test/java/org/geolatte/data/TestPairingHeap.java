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

import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Karel Maesen, Geovise BVBA
 *         creation-date: Apr 22, 2010
 */
public class TestPairingHeap {


    @Test
    public void test_empty_constructor() {
        PairingHeap<Data> ph = new PairingHeap<Data>();
        assertNotNull(ph);
    }

    @Test
    public void test_insert_respects_invariant() {
        PairingHeap<Data> heap = new PairingHeap<Data>();
        assertTrue(heap.isEmpty());
        heap.insert(new Data(10f));
        assertTrue(!heap.isEmpty());
        Data min = heap.findMin();
        assertEquals(Float.valueOf(10f), min.value);
        heap.insert(new Data(1f));
        min = heap.findMin();
        assertEquals(Float.valueOf(1f), min.value);
        heap.insert(new Data(4.f));
        min = heap.findMin();
        assertEquals(Float.valueOf(1f), min.value);
        heap.insert(new Data(12f));
        min = heap.findMin();
        assertEquals(Float.valueOf(1f), min.value);

        //start deleting elements in order
        min = heap.deleteMin();
        assertEquals(Float.valueOf(1f), min.value);
        min = heap.findMin();
        assertEquals(Float.valueOf(4.0f), min.value);
        min = heap.deleteMin();
        assertEquals(Float.valueOf(4.0f), min.value);
        min = heap.deleteMin();
        assertEquals(Float.valueOf(10.0f), min.value);
        min = heap.deleteMin();
        assertEquals(Float.valueOf(12.0f), min.value);
        assertTrue(heap.isEmpty());

    }


    @Test
    public void test_random_order() {
        int size = 100000;
        PairingHeap<Data> heap = new PairingHeap<Data>();
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            int rint = random.nextInt();
            Data d = new Data((float) rint);
            heap.insert(d);
        }

        Data prev = heap.deleteMin();

        while (!heap.isEmpty()) {
            Data min = heap.deleteMin();
            assertTrue(min.value >= prev.value);
        }

    }
    
    @Test
    public void test_random_decrease() {
        int size = 100000;
        PairingHeap<Data> heap = new PairingHeap<Data>();
        Random random = new Random();
        List<PairNode<Data>> nodelist = new ArrayList<PairNode<Data>>();
        for (int i = 0; i < size; i++) {
            int rint = random.nextInt();
            Data d = new Data((float) rint);
            PairNode<Data> pn = heap.insert(d);
            nodelist.add(pn);
        }

        for (int i = 0; i < size; i++){
            PairNode<Data> pn = nodelist.get(i);
            Data data = pn.getElement();
            data.value -= 100f;
            heap.decreaseKey(pn, data);
        }

        Data prev = heap.deleteMin();
        while (!heap.isEmpty()) {
            Data min = heap.deleteMin();
            assertTrue(min.value >= prev.value);
        }

    }

    @Test
    public void test_random_order_with_comparator() {
        int size = 100000;
        PairingHeap<NCData> heap = new PairingHeap<NCData>(new NCDataComparator());
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            int rint = random.nextInt();
            NCData d = new NCData((float) rint);
            heap.insert(d);
        }

        NCData prev = heap.deleteMin();

        while (!heap.isEmpty()) {
            NCData min = heap.deleteMin();
            assertTrue(min.value >= prev.value);
        }

    }

    @Test
    public void test_comparator_or_comparable(){
        PairingHeap<NCData> heap = new PairingHeap<NCData>();
        try {
            heap.insert(new NCData(10f));
            fail("Either comparable, or explicit comparator needed!");
        }catch(IllegalArgumentException e){
            //OK
        }
    }

    @Test
    public void test_decrease_key() {
        PairingHeap<Data> heap = new PairingHeap<Data>();

        Data d1 = new Data(1.0f);
        Data d2 = new Data(2.0f);
        Data d3 = new Data(3.0f);
        Data d4 = new Data(4.0f);

        PairNode<Data> pn2 = heap.insert(d2);
        PairNode<Data> pn4 = heap.insert(d4);
        PairNode<Data> pn1 = heap.insert(d1);
        PairNode<Data> pn3 = heap.insert(d3);

        heap.decreaseKey(pn4, new Data(-1.0f));
        assertEquals(Float.valueOf(-1.0f), heap.findMin().value);

        heap.decreaseKey(pn3, new Data(-2.0f));
        assertEquals(Float.valueOf(-2.0f), heap.findMin().value);

    }

    @Test
    public void test_decrease_key_with_higher_value() {
        PairingHeap<Data> heap = new PairingHeap<Data>();

        Data d1 = new Data(1.0f);
        Data d2 = new Data(2.0f);
        Data d3 = new Data(3.0f);
        Data d4 = new Data(4.0f);

        PairNode<Data> pn2 = heap.insert(d2);
        PairNode<Data> pn4 = heap.insert(d4);
        PairNode<Data> pn1 = heap.insert(d1);
        PairNode<Data> pn3 = heap.insert(d3);

        heap.decreaseKey(pn1, new Data(2.0f));
        heap.decreaseKey(pn2, new Data(3.0f));
        heap.decreaseKey(pn3, new Data(3.0f));

        assertEquals(Float.valueOf(1.0f), heap.deleteMin().value);
        assertEquals(Float.valueOf(2.0f), heap.deleteMin().value);
        assertEquals(Float.valueOf(3.0f), heap.deleteMin().value);
    }

    @Test
    public void test_delete_min_on_empty_throws_exception() {
        PairingHeap<Data> heap = new PairingHeap<Data>();
        try {
            heap.deleteMin();
            fail("Exception should be thrown.");
        } catch (IllegalStateException e) {
            //OK
            return;
        }
        fail("Wrong exception thrown");
    }

    @Test
    public void test_find_min_on_empty_throws_exception() {
        PairingHeap<Data> heap = new PairingHeap<Data>();
        try {
            heap.findMin();
            fail("Exception should be thrown.");
        } catch (IllegalStateException e) {
            //OK
            return;
        }
        fail("Wrong exception thrown");
    }

    static class Data implements Comparable<Data> {

        public Float value;

        Data(Float f) {
            this.value = f;
        }

        public int compareTo(Data o) {
            return this.value.compareTo(o.value);
        }
    }

    static class NCData {
        public final Float value;

        NCData(Float f) {
            this.value = f;
        }
    }


    static class NCDataComparator implements Comparator<NCData> {

        public int compare(NCData o1, NCData o2) {
            return o1.value.compareTo(o2.value);
        }
    }


}

