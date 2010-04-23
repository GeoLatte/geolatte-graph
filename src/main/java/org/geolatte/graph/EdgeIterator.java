package org.geolatte.graph;

public interface EdgeIterator<N,E> {

	public boolean next();
	
	public E getEdgeLabel();
	
	public float getWeight();
	
	public N getNode();
	
}
