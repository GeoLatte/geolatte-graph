package org.geolatte.data;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;


/**
 * Implements a Min-PriorityQueue in terms of a Pairing Heap.
 *
 * @author Karel Maesen, Geovise BVBA
 * @param <V>
 */
public class PMinQueue<V> {

    private final PairingHeap<Element<V>> heap = new PairingHeap<Element<V>>(new ElementComparator<V>());
    private final Map<V, PairNode<Element<V>>> index = new HashMap<V, PairNode<Element<V>>>();

    public void add(V value, float key) {
        PairNode<Element<V>> pn = heap.insert(new Element(value, key));
        this.index.put(value, pn);
    }


    public V extractMin() {
        V val = heap.extractMin().value;
        this.index.remove(val);
        return val;
    }


    public V get(V value) {
        PairNode<Element<V>> node = this.index.get(value);
        if (node == null) {
            return null;
        }
        return node.getElement().value;
    }


    public boolean isEmpty() {
        return this.heap.isEmpty();
    }

    public void update(V value, float r) {
        PairNode<Element<V>> node = this.index.get(value);
        if (node == null) {
            throw new RuntimeException("Node not in Pairing Heap.");
        }
        Element<V> newElement = new Element(value, r);
        this.heap.decreaseKey(node, newElement);

    }

    static class Element<V> {
        private final Float key;
        private final V value;
        Element(V value, Float key){
            this.key = key;
            this.value = value;
        }
    }

    static class ElementComparator<V> implements Comparator<Element<V>> {
        public int compare(Element<V> o1, Element<V> o2) {
            return o1.key.compareTo(o2.key);
        }
    }


}

