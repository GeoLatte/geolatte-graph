package org.geolatte.graph;



public interface GraphBuilder<N extends Nodal,E> {

	public void addEdge(N fromNode, N toNode, E label, float weight);

    public Graph<N,E> build() throws BuilderException;
	
}
