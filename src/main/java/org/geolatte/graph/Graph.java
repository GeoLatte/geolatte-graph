package org.geolatte.graph;

import java.util.Iterator;
import java.util.List;


public interface Graph<N extends Nodal, E> extends Iterable<N>{
		
	public Iterator<N> iterator();
	
	public N getNode(N node);
	
	public List<N> getNodesAt(Nodal loc);
	
	public List<N> getClosestNodes(Nodal loc, int num, int distance);
		
	public EdgeIterator<N,E> getOutGoingEdges(N node);
	
	
}
