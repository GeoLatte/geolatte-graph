package org.geolatte.data;

import org.geolatte.graph.Visitor;

public class TreeNode<K,D> {

	protected byte color = RedBlackTree.BLACK;
	
	protected D data;
	
	public K key = null;
	
	public TreeNode<K,D> left;
	
	public TreeNode<K,D> right;
	
	public TreeNode<K,D> parent;
	
	public TreeNode(){}
	
	public TreeNode(K key, D data, TreeNode<K,D> parent, TreeNode<K,D> sentinel){
		this.color = RedBlackTree.RED;
		this.parent = parent;
		this.left = sentinel;
		this.right = sentinel;
		this.data = data;
		this.key = key;
	}

	/**
	 * @return the color
	 */
	public byte getColor() {
		return color;
	}

	/**
	 * @param color the color to set
	 */
	public void setColor(byte color) {
		this.color = color;
	}

	/**
	 * @return the data
	 */
	public D getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(D data) {
		this.data = data;
	}

	/**
	 * @return the key
	 */
	public K getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(K key) {
		this.key = key;
	}

	/**
	 * @return the leftChild
	 */
	public TreeNode<K, D> getLeftChild() {
		return left;
	}

	/**
	 * @param leftChild the leftChild to set
	 */
	public void setLeftChild(TreeNode<K, D> leftChild) {
		this.left = leftChild;
	}

	/**
	 * @return the rightChild
	 */
	public TreeNode<K, D> getRightChild() {
		return right;
	}

	/**
	 * @param rightChild the rightChild to set
	 */
	public void setRightChild(TreeNode<K, D> rightChild) {
		this.right = rightChild;
	}

	/**
	 * @return the parent
	 */
	public TreeNode<K, D> getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(TreeNode<K, D> parent) {
		this.parent = parent;
	}
	
	public void accept(Visitor<TreeNode<K,D>> visitor){
		visitor.visit(this);
	}
	
	public String toString(){
		return this.key == null ? "NIL" : this.key.toString();
	}
	
}
