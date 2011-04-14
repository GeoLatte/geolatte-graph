package org.geolatte.graph;

import org.geolatte.data.PairNode;
import org.geolatte.data.PairingHeap;

import java.util.HashMap;
import java.util.Map;


/**
 * Implements a Min-PriorityQueue in terms of a Pairing Heap.
 *
 * @author Karel Maesen, Geovise BVBA
 * @param <V>
 */
public class PMinQueue<V extends Nodal> {

    private final PairingHeap<Element<V>> heap = new PairingHeap<Element<V>>();
    private final Map<InternalNode<V>, PairNode<Element<V>>> index = new HashMap<InternalNode<V>, PairNode<Element<V>>>();

    public void add(PredGraph<V> value, float key) {
        PairNode<Element<V>> pn = heap.insert(new Element(value, key));
        this.index.put(value.getInternalNode(), pn);
    }

    /**
     * Removes and returns the element from the queue with the smallest key.
     * @return The element with the smallest key.
     */
    public PredGraph<V> extractMin() {
        PredGraph<V> val = heap.deleteMin().value;
        this.index.remove(val.getInternalNode());
        return val;
    }


    public PredGraph<V> get(InternalNode<V> value) {
        PairNode<Element<V>> node = this.index.get(value);
        if (node == null) {
            return null;
        }
        return node.getElement().value;
    }

    /**
     * @return True if the queue is empty, false otherwise.
     */
    public boolean isEmpty() {
        return this.heap.isEmpty();
    }

    public void update(PredGraph<V> value, float r) {
        PairNode<Element<V>> node = this.index.get(value.getInternalNode());
        if (node == null) {
            throw new RuntimeException("MyNode not in Pairing Heap.");
        }
        Element<V> newElement = new Element(value, r);
        this.heap.decreaseKey(node, newElement);

    }

    static class Element<V extends Nodal> implements Comparable<Element<V>> {
        private final Float key;
        private final PredGraph<V> value;

        Element(PredGraph<V> value, Float key) {
            this.key = key;
            this.value = value;
        }


        public int compareTo(Element<V> o) {
            return this.key.compareTo(o.key);
        }
    }

}

