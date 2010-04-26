package org.geolatte.graph;

public interface PredGraph<N extends Nodal> {

    public abstract PredGraph<N> getPredecessor();

    public void setPredecessor(PredGraph<N> pred);

    public abstract float getWeight();

    public abstract void setWeight(float d);

    public abstract InternalNode<N> getInternalNode();

}