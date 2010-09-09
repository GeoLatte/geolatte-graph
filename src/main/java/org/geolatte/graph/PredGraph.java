package org.geolatte.graph;

public interface PredGraph<N extends Nodal> {

    PredGraph<N> getPredecessor();

    void setPredecessor(PredGraph<N> pred);

    float getWeight();

    void setWeight(float d);

    InternalNode<N> getInternalNode();

}