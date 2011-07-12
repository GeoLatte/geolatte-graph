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

import java.util.Comparator;

/**
 * <p>
 * The node data structure used by {@link PairingHeap}. A PairNode uses a left child / sibling representation
 * (= binary tree)
 * </p>
 * <p/>
 * <p>
 * n <---> next sibling <---> ..
 * /
 * /
 * left child <---> ..
 * </p>
 * <p>
 * See {@link PairingHeap} for additional documentation.
 * </p>
 * <p/>
 * creation-date: Apr 22, 2010
 *
 * @author Karel Maesen
 * @author Bert Vanhooff
 */
public final class PairNode<E> implements Comparable<PairNode<E>> {

    private E element;
    private PairNode<E> leftChild;
    private PairNode<E> nextSibling;
    private PairNode<E> prev;
    private Comparator<E> comparator;

    /**
     * Constructs an PairNode with the given element and comparator.
     *
     * @param element    The element (key) stored in the node.
     * @param comparator The comparator used to compare nodes
     * @throws IllegalArgumentException When the given element is not comparable or no comparator has been given.
     */
    PairNode(E element, Comparator<E> comparator) {

        if (comparator == null && !(element instanceof Comparable))
            throw new IllegalArgumentException("Require either explicit comparator, or Comparable element object.");

        this.element = element;
        this.comparator = comparator;
    }

    /**
     * Sets the element of this node.
     *
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
     *
     * @param node The left child.
     */
    void setLeftChild(PairNode<E> node) {
        this.leftChild = node;
    }

    /**
     * Sets the previous node.
     *
     * @param node The previous node.
     */
    void setPrev(PairNode<E> node) {
        this.prev = node;
    }

    /**
     * Sets the next sibling of this node.
     *
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
     * Compares this node to the given node using the comparator set at construction time. If no comparator is set,
     * this
     * node must implement {@link Comparable}.
     *
     * @param node The node to compare to this node.
     * @return negative integer, zero, or a positive integer as this node is less than, equal to, or greater than the
     *         given node.
     * @throws ClassCastException When no comparator was set and this node does not implement {@link Comparable}.
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