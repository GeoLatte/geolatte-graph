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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * <p>
 * An implementation of the Pairing Heap as discussed in 'The Pairing Heap: A New Form of Self-Adjusting Heap' by
 * Fredman, e.a., Algorithmica (1986) 1: 111 - 129.
 * <p/>
 * A pairing heap is an alternative to a Fibonacci heap, but is faster in practice and easier to implement. It is a
 * semi-ordered tree, where a nodes is always smaller than its left child.
 * </p>
 * <p>
 * This implementation is based on the implementation in the GNU C++ standard library (libstdc++)
 * </p>
 * <p>
 * Elements in the heap are compared using a given {@link Comparator} or uses the elements' natural ordering. In the
 * latter case, the elements must implement {@link Comparable}.
 * </p>
 * <p/>
 * <p>
 * This pairing heap uses a left child / right sibling representation:
 * <p/>
 * 2
 * *
 * *
 * 6 <---> 3 <---> 4 <---> 5 <---> 9 <---> 7
 * *       *       *
 * *         *       *
 * 10 <-> 13   8       11 <-> 15
 * *      *
 * *      *
 * 12     16 <-> 18
 * <p/>
 * Remarks:
 * - The root never has a sibling
 * - PairNode.getNextSibling() navigates to the right through siblings. For the example, starting from 6, this would
 * yield:
 * 6->3->4->5->9->7; starting from 10: 10->13; from 5: 5->11->12
 * - PairNode.getLeft() navigates through children. For the example, starting from 2, this would yield: 2->6
 * - PairNode.getPrev() navigates through previous siblings and through the parent (if there is no previous sibling)
 * until
 * the root is reached. For the example this would yield: 18->16->15->11->5->4->3->6->2
 * <p/>
 * </p>
 *
 * @param <E> The type of elements stored in the heap. These serve as the key in the tree and hence need to be
 *            comparable.
 * @author karel  Maesen, Geovise BVBA
 */
public class PairingHeap<E> {

    private PairNode<E> root;
    private Comparator<E> comparator;

    /**
     * Constructs a PairingHeap which uses the natural ordering of its elements. Therefore, elements are expected to
     * implement the {@link Comparable} interface. If they do not, you must use
     * {@link PairingHeap#PairingHeap(java.util.Comparator)} to provide a {@link Comparator}.
     */
    public PairingHeap() {
    }

    /**
     * Constructs a PairingHeap with the given comparator.
     *
     * @param comparator The comparator that will be used to compare the heap elements.
     */
    public PairingHeap(Comparator<E> comparator) {

        this.comparator = comparator;
    }

    /**
     * Creates a new node for the given element and inserts in into the heap. If the heap was empty, this node becomes
     * the root.
     *
     * @param element The element to insert.
     * @return The node created for the inserted element.
     */
    public PairNode<E> insert(E element) {

        PairNode<E> node = new PairNode<E>(element, this.comparator);
        if (root == null) {
            root = node;
        } else {
            root = compareAndLink(root, node);
        }

        return node;
    }

    /**
     * @return True if the heap is empty, false otherwise.
     */
    public boolean isEmpty() {
        return (root == null);
    }

    /**
     * @return The top element from the heap when not empty.
     * @throws IllegalStateException When the heap is empty.
     */
    public E findMin() {
        if (isEmpty())
            throw new IllegalStateException("Heap is empty.");
        return root.getElement();
    }

    /**
     * Removes the current root and merges its subtrees.
     *
     * @return The old root node.
     * @throws IllegalStateException When the heap is empty
     */
    public E deleteMin() {

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

    /**
     * Decreases the value of the given <code>node</code> to the new value. This will remove the tree rooted at the
     * given node and then re-insert that tree in the heap.
     *
     * @param node     The node to decrease to the newValue.
     * @param newValue The new value (must be lower, otherwise, nothing happens).
     * @throws IllegalArgumentException When the given node is null.
     */
    public void decreaseKey(PairNode<E> node, E newValue) {

        if (node == null) {
            throw new IllegalArgumentException("Given node cannot be null.");
        }

        if (compare(node.getElement(), newValue) < 0) {
            return; // newValue must be smaller.
        }

        // Can always decrease value without constraint violation
        node.setElement(newValue);

        if (!node.equals(root)) { // If we are the root, nothing needs to be changed

            // remove the subtree rooted at the node
            if (node.getNextSibling() != null) {
                node.getNextSibling().setPrev(node.getPrev());
            }
            if (node.equals(node.getPrev().getLeftChild())) {
                node.getPrev().setLeftChild(node.getNextSibling());
            } else {
                node.getPrev().setNextSibling(node.getNextSibling());
            }
            node.setNextSibling(null);

            // Re-insert the node
            root = compareAndLink(root, node);
        }
    }

    /**
     * Compares the given elements using the comparator set at construction time. If no comparator is given, the
     * getFirst
     * element is cast to {@link Comparable<E>}.
     *
     * @param element  The getFirst element to be compared.
     * @param newValue The second element to be compared.
     * @return negative integer, zero, or a positive integer as the getFirst argument is less than, equal to, or
     *         greater
     *         than the second.
     * @throws ClassCastException When <code>element</code> does not implement {@link Comparable<E>} and no
     *                            {@link Comparator} was given at construction time.
     */
    private int compare(E element, E newValue) {
        if (this.comparator != null) {
            return this.comparator.compare(element, newValue);
        }
        return ((Comparable<E>) element).compareTo(newValue);
    }

    /**
     * Internal merge method that is the basic operation to maintain order. Links getFirst and second trees together to
     * satisfy heap order.
     * getFirst becomes the result of the tree merge
     *
     * @param first  The root of tree 1 (cannot be null and cannot have a sibling).
     * @param second The root of tree 2 (can be null).
     * @return Whichever of the two given nodes becomes the new root.
     * @throws IllegalArgumentException When <code>getFirst</code> is null or has a sibling.
     */
    private PairNode<E> compareAndLink(PairNode<E> first, PairNode<E> second) {

        if (root == null || root.getNextSibling() != null)
            throw new IllegalArgumentException("First argument must not be null and must not have a sibling");

        if (second == null) return first;

        if (second.compareTo(first) < 0) {
            //attach getFirst as leftmost child of second
            second.setPrev(first.getPrev());
            first.setPrev(second);
            first.setNextSibling(second.getLeftChild());
            if (first.getNextSibling() != null) {
                first.getNextSibling().setPrev(first);
            }
            second.setLeftChild(first);
            return second;
        } else {
            //attach second as leftmost child of getFirst
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

    /**
     * Internal method that implements two-pass merging.
     *
     * @param firstSibling The root of the conglomerate, assumed not null.
     * @return The new root node.
     */
    private PairNode<E> combineSiblings(PairNode<E> firstSibling) {

        if (firstSibling.getNextSibling() == null) { // if there are no siblings, there is nothing to merge
            return firstSibling;
        }

        // cut siblings loose and store subtrees in a list
        List<PairNode<E>> treeList = new ArrayList<PairNode<E>>();
        while (firstSibling != null) {
            treeList.add(firstSibling);
            firstSibling.getPrev().setNextSibling(null); // Cut sibling loose from its previous (sibling/parent)
            firstSibling = firstSibling.getNextSibling();
        }

        //combine subtrees two at a time (in pairs, hence the name pairing heap)
        int i = 0;
        for (; i + 1 < treeList.size(); i += 2) {
            PairNode<E> combined = compareAndLink(treeList.get(i), treeList.get(i + 1));
            treeList.set(i, combined);
        }

        int j = i - 2; // j has the result of last compareAndLink
        if (j == (treeList.size() - 3)) { // if an odd number of trees, get the last one
            PairNode<E> combined = compareAndLink(treeList.get(j), treeList.get(j + 2));
            treeList.set(j, combined);
        }

        // merge right - to left, merging last tree with next to last. The result becomes the new last.
        for (; j >= 2; j -= 2) {
            PairNode<E> combined = compareAndLink(treeList.get(j - 2), treeList.get(j));
            treeList.set(j - 2, combined);
        }

        return treeList.get(0);
    }

}



