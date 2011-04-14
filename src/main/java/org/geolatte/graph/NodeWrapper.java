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
 * Qmino bvba - Romeinsestraat 18 - 3001 Heverlee  (http://www.qmino.com)
 * Geovise bvba - Generaal Eisenhowerlei 9 - 2140 Antwerpen (http://www.geovise.com)
 */

package org.geolatte.graph;

import java.util.Arrays;

/**
 * @author Karel Maesen, Geovise BVBA
 *         creation-date: Apr 25, 2010
 */
class NodeWrapper<N extends Nodal> implements InternalNode<N> {
    final N wrappedNodal;
    NodeWrapper<N>[] toNodes = new NodeWrapper[0];
    float[] toWeights = new float[0];
    Object[] toLabels = new Object[0];
    private InternalNode<N>[] fromNodes = new InternalNode[0];
    private float value;


    NodeWrapper(N obj) {
        this.wrappedNodal = obj;
    }

    public N getWrappedNodal() {
        return this.wrappedNodal;
    }

    public void addEdge(InternalNode<N> toNode, Object label, float weight) {

        //if there is already a connection, only remember the least weighted one.
        for (int i = 0; i < toNodes.length; i++) {
            InternalNode<N> nd = this.toNodes[i];
            if (nd.equals(toNode)) {
                if (weight < this.toWeights[i]) {
                    this.toWeights[i] = weight;
                }
                return;
            }
        }

        //add the outgoing edge (complete information)
        toNodes = Arrays.copyOf(toNodes, toNodes.length + 1);
        toWeights = Arrays.copyOf(toWeights, toWeights.length + 1);
        toLabels = Arrays.copyOf(toLabels, toLabels.length + 1);
        toNodes[toNodes.length - 1] = (NodeWrapper) toNode;
        toWeights[toWeights.length - 1] = weight;
        toLabels[toLabels.length - 1] = label;

        //add the incoming edge info
        toNode.addReachableFrom(this);

    }


    protected InternalNode<N>[] getConnected() {
        return this.toNodes;
    }

    protected float getWeightToNode(N toNode) {
        for (int i = 0; i < this.toNodes.length; i++) {
            if (this.toNodes[i].wrappedNodal.equals(toNode)) {
                return this.toWeights[i];
            }
        }
        return Float.NaN;
    }

    protected Object getLabelToNode(N toNode) {
        for (int i = 0; i < this.toNodes.length; i++) {
            if (this.toNodes[i].wrappedNodal.equals(toNode)) {
                return this.toLabels[i];
            }
        }
        return null;
    }

    public int getX() {
        return this.wrappedNodal.getX();
    }

    public int getY() {
        return this.wrappedNodal.getY();
    }

    public String toString() {
        String str = String.format("MyNode: x = %d, y = %d", this.wrappedNodal.getX(), this.wrappedNodal.getY());
        return str;
    }

    protected InternalNode<N>[] getReachableFrom() {
        return this.fromNodes;
    }

    public void addReachableFrom(InternalNode<N> fromNode) {
        this.fromNodes = Arrays.copyOf(this.fromNodes, this.fromNodes.length + 1);
        this.fromNodes[this.fromNodes.length - 1] = fromNode;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value){
        this.value = value;
    }

    //NodeWrapper objects should correspond one-to-one to their WrappedNodes.
    //Equals/hashcode implementation is therefore not necessary. For
    // performance reasons, it should not be implemented.

}