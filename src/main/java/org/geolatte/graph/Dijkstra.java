package org.geolatte.graph;

import java.util.*;

import org.geolatte.data.PMinQueue;

/**
 * @author Karel Maesen
 * 
 */
public class Dijkstra<N extends Nodal, E> implements
		GraphAlgorithm<Path<N>> {

	private final N origin;
	private final N destination;
	private final Graph<N, E> graph;
	private Path<N> result;

	private final PMinQueue<PredGraph<N>> minQueue;
	private final Relaxer<N, E> relaxer;

	protected Dijkstra(Graph<N, E> graph, N origin, N destination,
			Relaxer<N, E> relaxer) {

		this.graph = graph;

		this.origin = origin;
		this.destination = destination;
		this.relaxer = relaxer;
		this.minQueue = new PMinQueue<PredGraph<N>>();

	}

	public void execute() {

		Set<N> closed = new HashSet<N>();
        PredGraphImpl<N> startPG = new PredGraphImpl<N>(this.origin, 0.0f);
        minQueue.add(startPG, Float.POSITIVE_INFINITY);

		while (!minQueue.isEmpty()) {
			PredGraph<N> pu = minQueue.extractMin();
			closed.add(pu.getNode());
			if (isDone(pu)) {
				return;
			}
			N u = pu.getNode();
			EdgeIterator<N, E> outEdges = graph.getOutGoingEdges(u);
			while (outEdges.next()) {
				N v = outEdges.getNode();
				if (closed.contains(v)) {
                    continue;
                }
                // search the node on the minqueue
                PredGraph<N> spv = new PredGraphImpl<N>(v,
						Float.POSITIVE_INFINITY);
				PredGraph<N> pv = minQueue.get(spv);
				if (pv == null) {
					pv = spv;
					minQueue.add(pv, Float.POSITIVE_INFINITY);
				}

				if (this.relaxer.relax(pu, pv, null, outEdges.getWeight())) {
					this.minQueue.update(pv, this.relaxer.newTotalWeight());
				}

			}

		}

	}

	protected boolean isDone(PredGraph<N> pu) {
		if (pu.getNode().equals(this.destination)) {
			this.result = toPath(pu);
			return true;
		}
		return false;
	}

	protected PMinQueue<PredGraph<N>> getMinQueue() {
		return this.minQueue;
	}

	private Path<N> toPath(PredGraph<N> p) {
		BasicPath<N> path = new BasicPath<N>();
		path.setTotalWeight(p.getWeight());
		path.insert(p.getNode());
		PredGraph<N> next = p.getPredecessor();

		while (next != null) {
			path.insert(next.getNode());
			next = next.getPredecessor();
		}
		path.setValid(true);
		return path;
	}

	public Path<N> getResult() {
		return this.result;
	}

	static class PredGraphImpl<N> implements PredGraph<N> {
		private final N node;
		private PredGraph<N> predecessor = null;
		private float weight;

		private PredGraphImpl(N n, float weight) {
			this.node = n;
			this.weight = weight;
		}


		public PredGraph<N> getPredecessor() {
			return predecessor;
		}


		public float getWeight() {
			return this.weight;
		}


		public void setWeight(float w) {
			this.weight = w;
		}


		public N getNode() {
			return this.node;
		}

		public static class PGComparator<N> implements Comparator<PredGraph<N>> {

			public int compare(PredGraph<N> o1, PredGraph<N> o2) {
				if (o1 instanceof PredGraphImpl && o2 instanceof PredGraphImpl) {
					PredGraphImpl<N> pg1 = (PredGraphImpl<N>) o1;
					PredGraphImpl<N> pg2 = (PredGraphImpl<N>) o2;
					if (pg1.node.equals(pg2.node)) {
						return 0;
					}
					return Float.compare(pg1.getWeight(), pg2.getWeight());
				}
				throw new IllegalArgumentException();
			}
		}

		public void setPredecessor(PredGraph<N> pred) {
			this.predecessor = pred;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((node == null) ? 0 : node.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			PredGraphImpl<N> other = (PredGraphImpl<N>) obj;
			if (node == null) {
				if (other.node != null)
					return false;
			} else if (!node.equals(other.node))
				return false;
			return true;
		}

		public String toString() {
			return String.format("Node: %s, weight: %.1f", this.node,
					this.weight);
		}
	}

}
