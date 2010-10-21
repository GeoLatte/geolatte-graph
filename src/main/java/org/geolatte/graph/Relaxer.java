package org.geolatte.graph;

public interface Relaxer<N extends Nodal, E extends EdgeLabel<M>, M> {
	
	public boolean relax(PredGraph<N> u, PredGraph<N> v, E label, M modus);
	
	public float newTotalWeight();

}
