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

import java.util.Comparator;

/**
 * <p>
 * The node data structure used by {@link PairingHeap<E>}. A PairNode uses a left child / sibling representation
 * (= binary tree)
 * </p>
 *
 * <p>
 *              n <---> next sibling <---> ..
 *             /
 *            /
 *      l child <---> ..
 * </p>
 * <p>
 * See {@link PairingHeap<E>} for additional documentation.
 * </p>
 *
 * creation-date: Apr 22, 2010
 *
 * @author Karel Maesen, Geovise BVBA
 */
public class PairNode<E> implements Comparable<PairNode<E>> {

    private E element;
    private PairNode<E> leftChild;
    private PairNode<E> nextSibling;
    private PairNode<E> prev;
    private Comparator<E> comparator;

    /**
     * Constructs an PairNode with the given element and comparator.
     * @param element    The element (key) stored in the node.
     * @param comparator The comparator used to compare nodes
     * @throws IllegalArgumentException When the given element is not comparable or no comparator has been given.
     */
    protected PairNode(E element, Comparator<E> comparator) {

        if (comparator == null && !(element instanceof Comparable))
            throw new IllegalArgumentException("Require either explicit comparator, or Comparable element object.");

        this.element = element;
        this.comparator = comparator;
    }

    /**
     * Sets the element of this node.
     * @param element The element.
     */
    void setElement(E element) {
        this.element = element;
    }

    /**
     * @return The element of this node.
     */
    public E getElement() {
        return element;
    }

    /**
     * Sets the left child of this node.
     * @param node The left child.
     */
    void setLeftChild(PairNode<E> node) {
        this.leftChild = node;
    }

    /**
     * Sets the previous node.
     * @param node The previous node.
     */
    void setPrev(PairNode<E> node) {
        this.prev = node;
    }

    /**
     * Sets the next sibling of this node.
     * @param node The next sibling.
     */
    void setNextSibling(PairNode<E> node) {
        this.nextSibling = node;
    }

    public PairNode<E> getLeftChild() {
        return leftChild;
    }

    /**
     * @return The next sibling of this node.
     */
    public PairNode<E> getNextSibling() {
        return nextSibling;
    }

    /**
     * @return The previous node.
     */
    public PairNode<E> getPrev() {
        return this.prev;
    }

    /**
     * Compares this node to the given node using the comparator set at construction time. If no comparator is set, this
     * node must implement {@link Comparable<E>}.
     * @param node The node to compare to this node.
     * @return negative integer, zero, or a positive integer as this node is less than, equal to, or greater than the
     * given node.
     * @throws ClassCastException When no comparator was set and this node does not implement {@link Comparable<E>}.
     */
    public int compareTo(PairNode<E> node) {
        if (this.comparator == null) {
            return ((Comparable) this.element).compareTo(node.getElement());
        } else {
            return this.comparator.compare(this.getElement(), node.getElement());
        }
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PairNode pairNode = (PairNode) o;

        return !(element != null ? !element.equals(pairNode.element) : pairNode.element != null);
    }

    @Override
    public int hashCode() {
        return element != null ? element.hashCode() : 0;
    }
}