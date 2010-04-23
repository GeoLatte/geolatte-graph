package org.geolatte.graph;

interface PredGraph<N> {

	public abstract PredGraph<N> getPredecessor();

	public void setPredecessor(PredGraph<N> pred);
	
	public abstract float getWeight();

	public abstract void setWeight(float d);

	public abstract N getNode();

}