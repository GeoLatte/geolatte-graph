package org.geolatte.graph;

public interface Relaxer<N extends Nodal, E> {
	
	public boolean relax(PredGraph<N> u, PredGraph<N> v,E label, float weight);
	
	public float newTotalWeight();

}
