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
    private NodeWrapper<N>[] fromNodes = new NodeWrapper[0];


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

    public void addReachableFrom(NodeWrapper<N> fromNode) {
        this.fromNodes = Arrays.copyOf(this.fromNodes, this.fromNodes.length + 1);
        this.fromNodes[this.fromNodes.length - 1] = fromNode;
    }

    //NodeWrapper objects should correspond one-to-one to their WrappedNodes.
    //Equals/hashcode implementation is therefore not necessary. For
    // performance reasons, it should not be implemented.

}