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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author karel  Maesen, Geovise BVBA
 *         <p/>
 *         An implementation of the Pairing Heap as discussed in
 *         'The Pairing Heap: A New Form of Self-Adjusting Heap'
 *         by Fredman, e.a., Algorithmica (1986) 1: 111 - 129.
 *         <p/>
 *         This implementation is based on the implementation
 *         in the GNU C++ standard library (libstdc++)
 */
public class PairingHeap<E> {

    private PairNode<E> root;
    private Comparator<E> comparator;

    public PairingHeap() {
    }

    public PairingHeap(Comparator<E> comparator) {
        this.comparator = comparator;
    }


    public PairNode<E> insert(E element) {
        PairNode<E> node = new PairNode<E>(element, this.comparator);
        if (root == null) {
            root = node;
        } else {
            root = compareAndLink(root, node);
        }
        return node;
    }

    public boolean isEmpty() {
        return (root == null);
    }

    public E findMin() {
        if (isEmpty())
            throw new IllegalStateException("Heap is empty.");
        return root.getElement();
    }


    public E extractMin() {
        if (isEmpty()) {
            throw new IllegalStateException("Heap is empty.");
        }

        PairNode<E> oldRoot = root;

        if (root.getLeftChild() == null) {
            root = null;
        } else {
            root = combineSiblings(root.getLeftChild());
        }
        return oldRoot.getElement();
    }


    public void decreaseKey(PairNode<E> node, E newValue) {
        if (node == null) throw new IllegalArgumentException("MyNode  cannot be NULL.");
        if (compare(node.getElement(), newValue) < 0) {
            return; // newValue must be smaller.
        }
        node.setElement(newValue);
        if (!node.equals(root)) {
            if (node.getNextSibling() != null) {
                node.getNextSibling().setPrev(node.getPrev());
            }
            if (node.equals(node.getPrev().getLeftChild())) {
                node.getPrev().setLeftChild(node.getNextSibling());
            } else {
                node.getPrev().setNextSibling(node.getNextSibling());
            }
            node.setNextSibling(null);
            root = compareAndLink(root, node);

        }
    }

    private int compare(E element, E newValue) {
        if (this.comparator != null) {
            return this.comparator.compare(element, newValue);
        }
        return ((Comparable<E>) element).compareTo(newValue);
    }


    private PairNode<E> compareAndLink(PairNode<E> first, PairNode<E> second) {
        if (root == null || root.getNextSibling() != null)
            throw new IllegalStateException("first argument must not be null, and must not have a sibling");

        if (second == null) return first;

        if (second.compareTo(first) < 0) {
            //attach first as leftmost child of second
            second.setPrev(first.getPrev());
            first.setPrev(second);
            first.setNextSibling(second.getLeftChild());
            if (first.getNextSibling() != null) {
                first.getNextSibling().setPrev(first);
            }
            second.setLeftChild(first);
            return second;
        } else {
            //attach second as leftmost child of first
            second.setPrev(first);
            first.setNextSibling(second.getNextSibling());
            if (first.getNextSibling() != null) {
                first.getNextSibling().setPrev(first);
            }
            second.setNextSibling(first.getLeftChild());
            if (second.getNextSibling() != null) {
                second.getNextSibling().setPrev(second);
            }
            first.setLeftChild(second);
            return first;
        }
    }

    private PairNode<E> combineSiblings(PairNode<E> firstSibling) {
        if (firstSibling.getNextSibling() == null) return firstSibling;

        //store subtrees in a list
        List<PairNode<E>> treeList = new ArrayList<PairNode<E>>();
        while (firstSibling != null) {
            treeList.add(firstSibling);
            firstSibling.getPrev().setNextSibling(null);
            firstSibling = firstSibling.getNextSibling();
        }

        //combine subtrees two at a time
        int lastCombinedIndex = 0;
        int i = 0;
        for (; i + 1 < treeList.size(); i += 2) {
            PairNode<E> combined = compareAndLink(treeList.get(i), treeList.get(i + 1));
            treeList.set(i, combined);
        }

        int j = i - 2;
        if (j == (treeList.size() - 3)) {
            PairNode<E> combined = compareAndLink(treeList.get(j), treeList.get(j + 2));
            treeList.set(j, combined);
        }


        //merge right - to left
        for (; j >= 2; j -= 2) {
            PairNode<E> combined = compareAndLink(treeList.get(j - 2), treeList.get(j));
            treeList.set(j - 2, combined);
        }
        return treeList.get(0);
    }

}



