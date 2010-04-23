package org.geolatte.graph;

/**
 * @author Karel Maesen
 * 
 * Interface for a GraphAlgorithm.
 * 
 * 
 *
 */
public interface GraphAlgorithm<P> { 
	
	/**
	 * Executes this graph algorithm. 
	 */
	public void execute();

	/**
	 * Gets the result of this graph algorithm.
	 * 
	 * @return the result
	 * @throws IllegalStateException when this method is invoked before the graph has been executed.
	 * 
	 */
	public P getResult();
}
