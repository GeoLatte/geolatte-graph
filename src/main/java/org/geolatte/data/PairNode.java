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
 * @author Karel Maesen, Geovise BVBA
 *         creation-date: Apr 22, 2010
 *
 * Node in a Pairing Heap
 */
public class PairNode<E> implements Comparable<PairNode<E>> {
    private E element;
    private PairNode<E> leftChild;
    private PairNode<E> nextSibling;
    private PairNode<E> prev;
    private Comparator<E> comparator;

    
    protected PairNode(E element, Comparator<E> comparator){
        if (comparator == null && !(element instanceof Comparable))
            throw new IllegalArgumentException("Require either explicit comparator, or Comparable element object.");
        this.element = element;
        this.comparator = comparator;
    }

    public void setElement(E element){
        this.element = element;
    }
    public E getElement(){
        return element;
    }

    public void setLeftChild(PairNode<E> node ){
        this.leftChild = node;
    }

    public void setPrev(PairNode<E> node){
        this.prev = node;
    }

    public void setNextSibling(PairNode<E> node){
        this.nextSibling = node;
    }

    public PairNode<E> getLeftChild(){
        return leftChild;
    }

    public PairNode<E> getNextSibling(){
        return nextSibling;
    }


    public PairNode<E> getPrev(){
        return this.prev;
    }

    public int compareTo(PairNode<E> o) {
        if (this.comparator == null) {
            return ((Comparable)this.element).compareTo(o.getElement());
        } else {
            return this.comparator.compare(this.getElement(), o.getElement());
        }
    }


    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PairNode pairNode = (PairNode) o;

        if (element != null ? !element.equals(pairNode.element) : pairNode.element != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return element != null ? element.hashCode() : 0;
    }
}