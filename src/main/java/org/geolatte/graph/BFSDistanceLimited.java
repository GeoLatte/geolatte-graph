package org.geolatte.graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class BFSDistanceLimited<N extends Nodal,E> implements GraphAlgorithm<Map<N,Float>> {

	final Set<NodeWrapper<N>> blackNodes = new HashSet<NodeWrapper<N>>();
	final Queue<NodeWrapper<N>> greyNodes = new LinkedList<NodeWrapper<N>>();
	final N source;
	final float maxDistance;
	final Graph<N,E> graph;
	Map<N,Float> result;
	
	protected BFSDistanceLimited(Graph<N,E> graph, N source, float maxDistance){
		this.graph = graph;
		this.source = graph.getNode(source);
		this.maxDistance = maxDistance;
	}
	

	public void execute() {
		
		NodeWrapper<N> ws = new NodeWrapper<N>(this.source);
		ws.distance = 0.f;
		ws.predecessor = null;
		greyNodes.add(ws);
		
		while ( !greyNodes.isEmpty()) {
			NodeWrapper<N> wu = this.greyNodes.remove();
			
			if (wu.distance > maxDistance){
				continue; //don't expand when the node is beyond maximum distance.
			}
			
			EdgeIterator<N,E> outEdges = this.graph.getOutGoingEdges(wu.node);
			while(outEdges.next()){
				N v = outEdges.getNode();
				NodeWrapper<N> wv = new NodeWrapper<N>(v); 
				if ( greyNodes.contains(wv) || blackNodes.contains(wv)){
					continue; // do nothing
				} else {
					wv.distance = wu.distance + outEdges.getWeight();
					wv.predecessor = wu.node;
					greyNodes.add(wv);
				}
			}
			blackNodes.add(wu);
		}
		
		this.result = toMap(blackNodes);
		
	}


	public Map<N,Float> getResult() {
		return this.result;
	}
	
	protected Map<N, Float> toMap(Set<NodeWrapper<N>> nodes){
		Map<N, Float> map = new HashMap<N, Float>(nodes.size());
		for ( NodeWrapper<N> wu : nodes){
			map.put(wu.node, wu.distance);
		}
		return map;
		
	}
	
	private static class NodeWrapper<N>  {
		
		final N node;
		float distance = Float.POSITIVE_INFINITY;
		N predecessor;
		
		private NodeWrapper(N node){
			this.node = node;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((node == null) ? 0 : node.hashCode());
			return result;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof NodeWrapper))
				return false;
			NodeWrapper other = (NodeWrapper) obj;
			if (node == null) {
				if (other.node != null)
					return false;
			} else if (!node.equals(other.node))
				return false;
			return true;
		}
		
		
		
	}

}
