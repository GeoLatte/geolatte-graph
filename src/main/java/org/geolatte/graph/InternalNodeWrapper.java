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

package org.geolatte.graph;

import java.util.Arrays;

/**
 * Supports only one edge between two nodes!
 *
 * @author Karel Maesen, Geovise BVBA
 *         creation-date: Apr 25, 2010
 */
class InternalNodeWrapper<N> implements InternalNode<N> {

//InternalNodeWrapper objects should correspond one-to-one to their WrappedNodes.
    //Equals/hashcode implementation is therefore not necessary. For
    // performance reasons, it should not be implemented.

    final N wrappedNodal;
    InternalNodeWrapper<N>[] toNodes = new InternalNodeWrapper[0];
    EdgeWeight[] toWeights = new EdgeWeight[0];
    private InternalNode<N>[] fromInternalNodes = new InternalNode[0];

    InternalNodeWrapper(N obj) {
        this.wrappedNodal = obj;
    }

    public N getWrappedNode() {
        return this.wrappedNodal;
    }

    public void addEdge(InternalNode<N> toInternalNode, EdgeWeight edgeWeight) {

        // TODO : do not add multiple edges between the same pair of nodes

        //add the outgoing edge (complete information)
        toNodes = Arrays.copyOf(toNodes, toNodes.length + 1);
        toWeights = Arrays.copyOf(toWeights, toWeights.length + 1);
        toNodes[toNodes.length - 1] = (InternalNodeWrapper) toInternalNode;
        toWeights[toWeights.length - 1] = edgeWeight;

        //add the incoming edge info
        toInternalNode.addReachableFrom(this);

    }


    protected InternalNode<? extends N>[] getConnected() {
        return this.toNodes;
    }

    public String toString() {
        return String.format("InternalNodeWrapper, wraps: %s", this.wrappedNodal.toString());
    }

    protected InternalNode<? extends N>[] getReachableFrom() {
        return this.fromInternalNodes;
    }

    public void addReachableFrom(InternalNode<N> fromInternalNode) {
        this.fromInternalNodes = Arrays.copyOf(this.fromInternalNodes, this.fromInternalNodes.length + 1);
        this.fromInternalNodes[this.fromInternalNodes.length - 1] = fromInternalNode;
    }

    public float getWeightTo(InternalNode<N> toInternalNode, int weightKind) {

        for (int i = 0; i < this.toNodes.length; i++) {
            if (this.toNodes[i].wrappedNodal.equals(toInternalNode.getWrappedNode())) {
                return this.toWeights[i].getValue(weightKind);
            }
        }
        return Float.MAX_VALUE;
    }
}