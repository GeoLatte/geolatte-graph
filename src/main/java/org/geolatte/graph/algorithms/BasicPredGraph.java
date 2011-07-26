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

package org.geolatte.graph.algorithms;

import org.geolatte.graph.InternalNode;
import org.geolatte.graph.PredGraph;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <p>
 * Basic implementation of a PredGraph.
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
class BasicPredGraph<N, E> implements PredGraph<N, E> {

    private final InternalNode<N, E> internalNode;
    private PredGraph<N, E> predecessor = null;
    private float weight;

    BasicPredGraph(InternalNode<N, E> n, float weight) {
        this.internalNode = n;
        this.weight = weight;
    }

    public PredGraph<N, E> getPredecessor() {
        return predecessor;
    }

    public float getWeight() {
        return this.weight;
    }

    public void setWeight(float w) {
        this.weight = w;
    }

    public InternalNode<N, E> getInternalNode() {
        return this.internalNode;
    }

    public Iterator<InternalNode<N, E>> iterator() {
        return new Iterator<InternalNode<N, E>>() {

            // initialize to the current predgraph
            private PredGraph<N, E> currentPredGraph = BasicPredGraph.this;

            public boolean hasNext() {

                return currentPredGraph != null;
            }

            public InternalNode<N, E> next() {

                if (currentPredGraph == null) {
                    throw new NoSuchElementException();
                }

                PredGraph<N, E> current = currentPredGraph;
                currentPredGraph = currentPredGraph.getPredecessor();

                return current.getInternalNode();
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public PredGraph<N, E> setPredecessor(PredGraph<N, E> pred) {
        this.predecessor = pred;
        return pred;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((internalNode == null) ? 0 : internalNode.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BasicPredGraph<N, E> other = (BasicPredGraph<N, E>) obj;
        if (internalNode == null) {
            if (other.internalNode != null)
                return false;
        } else if (!internalNode.equals(other.internalNode))
            return false;
        return true;
    }

    public String toString() {
        return String.format("PredGraph for node: %s, weight: %.1f", this.internalNode.toString(), this.weight);
    }


    public InternalNode<N, E> getFirst() {

        return getInternalNode();
    }
}
