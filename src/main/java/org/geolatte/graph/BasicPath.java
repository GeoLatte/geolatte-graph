package org.geolatte.graph;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class BasicPath<N extends Nodal> implements Path<N> {

	private List<N> nodes = new LinkedList<N>();

	private boolean valid = false;

	private float weight;

	public void insert(N nd) {
		nodes.add(0, nd);
	}

	public void setValid(boolean v) {
		this.valid = v;
	}

	public void setTotalWeight(float w) {
		this.weight = w;
	}


	public N getDestination() {
		return this.nodes.get(this.nodes.size() - 1);
	}

	public N getSource() {
		return this.nodes.get(0);
	}

	public boolean isValid() {
		return this.valid;
	}


	public float totalWeight() {

		return this.weight;
	}

	public Iterator<N> iterator() {
		return this.nodes.iterator();
	}

	public String toString(){
		StringBuilder stBuf = new StringBuilder();
		stBuf.append("Nodes: ");
		for (N nd: this){
			stBuf.append(nd)
			.append("\n");
		}
		return stBuf.toString();
	}
	
}
