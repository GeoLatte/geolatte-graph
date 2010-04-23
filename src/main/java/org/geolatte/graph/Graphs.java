package org.geolatte.graph;

import com.vividsolutions.jts.geom.Envelope;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class Graphs {

	public static <N extends Nodal,E> GraphBuilder<N,E> createGridIndexedGraphBuilder(Envelope env,
			int resolution) {
		return new GridIndexedGraphBuilder<N,E>(env, resolution);
	}

	// Builder implementation
	private static class GridIndexedGraphBuilder<N extends Nodal,E> implements GraphBuilder<N,E> {

		private final SpatialIndexBuilder<NodeWrapper<N>> indexBuilder;
		// map is used to quickly locate Nodes based on Node equality.
		private final Map<N, NodeWrapper<N>> map = new HashMap<N, NodeWrapper<N>>();

		private GridIndexedGraphBuilder(Envelope env, int resolution) {
			this.indexBuilder = SpatialIndexes.createGridIndexBuilder(env,
					resolution);
		}


		public Graph<N,E> build() throws BuilderException {
			if (map.isEmpty()) {
				throw new IllegalStateException(
						"No nodes added since last built");
			}
			map.clear(); // empty to save on memory.
			SpatialIndex<NodeWrapper<N>> index = this.indexBuilder.build();
			return new GridIndexedGraph<N,E>(index);
		}


		public void addEdge(N fromNode, N toNode, E label, float weight) {
			
			
			if (fromNode.equals(toNode)){
				return;
				//System.err.println("Tried adding zero-length edge");
			}
			
			NodeWrapper<N> fNw = this.map.get(fromNode);
			NodeWrapper<N> toNw = this.map.get(toNode);
			if (fNw == null) {
				fNw = new NodeWrapper<N>(fromNode);
				this.indexBuilder.insert(fNw);
				this.map.put(fromNode, fNw);
			}
			if (toNw == null){
				toNw = new NodeWrapper<N>(toNode);
				this.indexBuilder.insert(toNw);
				this.map.put(toNode, toNw);
			}
			
			fNw.addEdge(toNw, label, weight);
		}

	}

	// Graph Implementation
	private static class GridIndexedGraph<N extends Nodal, E> implements Graph<N,E> {

		private final SpatialIndex<NodeWrapper<N>> index;
		

		private GridIndexedGraph(SpatialIndex<NodeWrapper<N>> index) {
			this.index = index;
		}


		public List<N> getNodesAt(Nodal loc) {
			return new UnmodifiableUnwrappingList<N>(this.index.getObjectAt(loc));
		}


		public Iterator<N> iterator() {
			return new UnwrapperIterator<N>(this.index.getObjects());
		}


		public List<N> getClosestNodes(Nodal loc, int num, int distance) {
			return new UnmodifiableUnwrappingList<N>(this.index.getNClosest(loc, num, distance));
		}


		public N getNode(N node) {
			for (NodeWrapper<N> nw : this.index.getObjectAt(node)){
				if (nw.node.equals(node)){
					return nw.node;
				}
			}
			return null;
		}
		
		protected NodeWrapper<N> getWrappedNode(N node){
			for (NodeWrapper<N> w : this.index.getObjectAt(node)){
				if (w.node.equals(node)){
					return w;
				}
			}
			return null;
		}


		public EdgeIterator<N, E> getOutGoingEdges(N node) {
			return new EdgeIteratorImpl<N,E>(this, node);
		}



	}
	
	private static class UnwrapperIterator<N extends Nodal> implements Iterator<N> {
		
		private final Iterator<NodeWrapper<N>> iterator;
		
		private UnwrapperIterator(Iterator<NodeWrapper<N>> iterator){
			this.iterator = iterator;
		}


		public boolean hasNext() {
			return iterator.hasNext();
		}


		public N next() {
			NodeWrapper<N> wrapper = this.iterator.next();
			return wrapper.getNode();
		}


		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	}
	
	
	private static class UnmodifiableUnwrappingList<N extends Nodal> implements List<N> {

		private final List<NodeWrapper<N>> list;
		
		private UnmodifiableUnwrappingList( List<NodeWrapper<N>> list){
			this.list = list;
		}

		public boolean add(N e) {
			throw new UnsupportedOperationException("This list is unmodifiable.");
		}


		public void add(int index, N element) {
			throw new UnsupportedOperationException("This list is unmodifiable.");
			
		}


		public boolean addAll(Collection<? extends N> c) {
			throw new UnsupportedOperationException("This list is unmodifiable.");
		}


		public boolean addAll(int index, Collection<? extends N> c) {
			throw new UnsupportedOperationException("This list is unmodifiable.");		
		}


		public void clear() {
			throw new UnsupportedOperationException("This list is unmodifiable.");	
		}


		public boolean contains(Object o) {
			NodeWrapper<N> nw = new NodeWrapper<N>((N)o);
			return this.list.contains(nw);
		}


		public boolean containsAll(Collection<?> c) {
			
			for ( Object o : c){
				if (!this.contains(o)) {
					return false;
				}
			}
			return true;
		}


		public N get(int index) {
			return this.list.get(index).getNode();
		}


		public int indexOf(Object o) {
			NodeWrapper<N> nw = new NodeWrapper<N>((N)o);
			return this.list.indexOf(nw);
		}


		public boolean isEmpty() {
			return this.list.isEmpty();
		}


		public Iterator<N> iterator() {
			return new UnwrapperIterator<N>(this.list.iterator());
		}


		public int lastIndexOf(Object o) {
			NodeWrapper<N> nw = new NodeWrapper<N>((N)o);
			return this.list.lastIndexOf(nw);
		}


		public ListIterator<N> listIterator() {
			throw new UnsupportedOperationException("Not implemented.");
		}


		public ListIterator<N> listIterator(int index) {
			throw new UnsupportedOperationException("Not implemented.");
		}


		public boolean remove(Object o) {
			throw new UnsupportedOperationException("This list is unmodifiable.");
		}


		public N remove(int index) {
			throw new UnsupportedOperationException("This list is unmodifiable.");
		}


		public boolean removeAll(Collection<?> c) {
			throw new UnsupportedOperationException("This list is unmodifiable.");
		}


		public boolean retainAll(Collection<?> c) {
			throw new UnsupportedOperationException("This list is unmodifiable.");
		}


		public N set(int index, N element) {
			throw new UnsupportedOperationException("This list is unmodifiable.");
		}


		public int size() {
			return this.list.size();
		}


		public List<N> subList(int fromIndex, int toIndex) {
			throw new UnsupportedOperationException("Not implemented.");
		}


		public Object[] toArray() {
			throw new UnsupportedOperationException("Not implemented.");
		}


		public <T> T[] toArray(T[] a) {
			throw new UnsupportedOperationException("Not implemented.");
		}
		
	}
	
	private static class EdgeIteratorImpl<N extends Nodal,E> implements EdgeIterator<N,E> {

		final NodeWrapper<N> fromNw;
		final GridIndexedGraph<N,E> graph;
		int i = -1;
		
		private EdgeIteratorImpl(GridIndexedGraph<N,E> graph, N from){
			this.graph = graph;
			this.fromNw = this.graph.getWrappedNode(from);
		}
		

		public E getEdgeLabel() {	
			return (E)fromNw.toLabels[i];
		}

		@SuppressWarnings("unchecked")
		public N getNode() {
			NodeWrapper<N> nw =  fromNw.toNodes[i];
			return nw.node;
		}


		public float getWeight() {
			return fromNw.toWeights[i];
		}


		public boolean next() {
			i++;
			return i < fromNw.toNodes.length;
		}
		
	}
	
	protected static class NodeWrapper <N extends Nodal> implements Nodal {
		private final N node;		
		private NodeWrapper<N>[] toNodes = new NodeWrapper[0];
		private float[] toWeights = new float[0];
		private Object[] toLabels = new Object[0]; 
		private NodeWrapper<N>[] fromNodes = new NodeWrapper[0];
		
		
		private NodeWrapper(N obj){
			this.node = obj;
		}

		protected N getNode(){
			return this.node;
		}
		
		protected void addEdge(NodeWrapper<N> toNode, Object label, float weight) {
			
			//if there is already a connection, only remember the least weighted one.
			for (int i = 0; i < toNodes.length; i++){
				NodeWrapper<N> nd = this.toNodes[i];
				if (nd.equals(toNode)){
					if (weight < this.toWeights[i]){
						this.toWeights[i] = weight;
					}
					return;
				}
			}
			
			//add the outgoing edge (complete information)
			toNodes = Arrays.copyOf(toNodes, toNodes.length + 1);
			toWeights = Arrays.copyOf(toWeights, toWeights.length + 1);
			toLabels = Arrays.copyOf(toLabels, toLabels.length+1);
			toNodes[toNodes.length - 1] = toNode;
			toWeights[toWeights.length -1] = weight;
			toLabels[toLabels.length -1] = label;
			
			//add the incoming edge info
			toNode.addReachableFrom(this);
			
		} 
		


		protected NodeWrapper<N>[] getConnected() {
			return this.toNodes;
		}

		protected float getWeightToNode(N toNode) {
			for (int i = 0; i < this.toNodes.length; i++){
				if (this.toNodes[i].node.equals(toNode)){
					return this.toWeights[i];
				}
			} 
			return Float.NaN;
		}
		
		protected Object getLabelToNode(N toNode){
			for (int i = 0; i < this.toNodes.length; i++){
				if (this.toNodes[i].node.equals(toNode)){
					return this.toLabels[i];
				}
			} 
			return null;
		}

		
//		public Envelope getEnvelope() {
//			return this.node.getEnvelope();
//		}

		
		public int getX() {
			return this.node.getX();
		}

		public int getY() {
			return this.node.getY();
		}

        public Object getData() {
            return this.node.getData();
        }

        public String toString(){
			String str = String.format("Node: x = %d, y = %d", this.node.getX(), this.node.getY());
			//str += "\n connected to :" ;
			//for (BasicNode nd : this.toNodes){
			//	str += nd.getID() + " " ;
			//}
			return str;
		}

		protected NodeWrapper<N>[] getReachableFrom() {
			return this.fromNodes;
		}

		protected void addReachableFrom(NodeWrapper<N> fromNode) {
			this.fromNodes  = Arrays.copyOf(this.fromNodes, this.fromNodes.length + 1);
			this.fromNodes[this.fromNodes.length - 1] = fromNode;
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
