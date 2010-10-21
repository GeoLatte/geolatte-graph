package org.geolatte.graph;



public interface GraphBuilder<N extends Nodal,E extends EdgeLabel<M>, M> {

	public void addEdge(N fromNode, N toNode, E label);

    public Graph<N,E, M> build() throws BuilderException;
	
}
