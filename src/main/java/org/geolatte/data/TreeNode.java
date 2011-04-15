/*
 * This file is part of the GeoLatte project.
 *
 *     GeoLatte is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     GeoLatte is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with GeoLatte.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright (C) 2010 - 2011 and Ownership of code is shared by:
 * Qmino bvba - Romeinsestraat 18 - 3001 Heverlee  (http://www.qmino.com)
 * Geovise bvba - Generaal Eisenhowerlei 9 - 2140 Antwerpen (http://www.geovise.com)
 */

package org.geolatte.data;

/**
 * <p>
 * The node structure used by {@link RedBlackTree}.
 * </p>
 * 
 * @param <K>
 * @param <D>
 */
public class TreeNode<K, D> {

    protected byte color = RedBlackTree.BLACK;

    protected D data;

    public K key = null;

    public TreeNode<K, D> left;

    public TreeNode<K, D> right;

    public TreeNode<K, D> parent;

    public TreeNode() {
    }

    public TreeNode(K key, D data, TreeNode<K, D> parent, TreeNode<K, D> sentinel) {
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

    public void accept(Visitor<TreeNode<K, D>> visitor) {
        visitor.visit(this);
    }

    public String toString() {
        return this.key == null ? "NIL" : this.key.toString();
    }

}
