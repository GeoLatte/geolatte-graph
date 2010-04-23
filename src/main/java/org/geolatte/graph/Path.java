package org.geolatte.graph;


public interface Path<N extends Nodal> extends Iterable<N>{
	
	public float totalWeight();
	
	public N getSource();
	
	public N getDestination();
	
	public boolean isValid();

}
