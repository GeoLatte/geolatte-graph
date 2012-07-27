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
 * Qmino bvba - Esperantolaan 4 - 3001 Heverlee  (http://www.qmino.com)
 * Geovise bvba - Generaal Eisenhowerlei 9 - 2140 Antwerpen (http://www.geovise.com)
 */

package org.geolatte.data;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * A Red-Black Tree is a self-balancing binary search tree.
 * <p/>
 * This implementation is based on Introduction to Algorithms, 2nd Ed, Cormed e.a., chapter 13.
 *
 * @param <K> Type of the key value of tree nodes.
 * @param <D> Type of the data contained in a node.
 * @author Karel Maesen, Geovise BVBA
 */
public class RedBlackTree<K, D> {

    public static final byte RED = 0;
    public static final byte BLACK = 1;

    public final TreeNode<K, D> NIL = new TreeNode<K, D>(); // singleton leaf node

    protected final Comparator<K> comparator;
    protected TreeNode<K, D> root = NIL;
    protected final NodeWriter nodeWriter = new NodeWriter();

    /**
     * Constructs a RedBlackTree with the given key comparator.
     *
     * @param comparator A comparator for the key type
     */
    public RedBlackTree(Comparator<K> comparator) {

        if (comparator == null)
            throw new IllegalArgumentException("Must provide a comparator.");

        this.comparator = comparator;
        NIL.left = NIL;
        NIL.right = NIL;
    }

    /**
     * Searches the tree for the data associated with the given key.
     *
     * @param key The key to search for.
     * @return If found, the data associated with the given key, else, null.
     */
    public D get(K key) {
        TreeNode<K, D> nd = iterativeSearch(getRoot(), key);
        if (isNil(nd)) {
            return null;
        } else {
            return nd.getData();
        }
    }

    public D getMinimum() {
        TreeNode<K, D> nd = getMinNode(this.getRoot());
        return isNil(nd) ? null : nd.getData();
    }

    public D getMaximum() {
        TreeNode<K, D> nd = getMaxNode(this.getRoot());
        return isNil(nd) ? null : nd.getData();
    }

    public D getSuccessor(K key) {
        TreeNode<K, D> testNd = iterativeSearch(getRoot(), key);
        TreeNode<K, D> succ = successor(testNd);
        return isNil(succ) ? null : succ.getData();
    }

    public D getPredecessor(K key) {
        TreeNode<K, D> testNd = iterativeSearch(getRoot(), key);
        TreeNode<K, D> pred = predecessor(testNd);
        return isNil(pred) ? null : pred.getData();
    }

    /**
     * Inserts a new node with the given key and data value.
     *
     * @param key  The key value. Must be unique within a tree.
     * @param data The data value.
     * @throws IllegalArgumentException When a node with the given key is already present in the tree.
     */
    public void insert(K key, D data) {

        TreeNode<K, D> parent = NIL;
        TreeNode<K, D> pointer = getRoot();

        //find insertion point
        while (!isNil(pointer)) {
            parent = pointer;
            int cmp = this.comparator.compare(key, pointer.getKey());
            if (cmp < 0) {
                pointer = pointer.left;
            } else if (cmp > 0) {
                pointer = pointer.right;
            } else {
                //throw exception when duplicate key is entered
                throw new IllegalArgumentException("Entry is key " + key + " already present in tree.");
            }
        }
        //create new node
        TreeNode<K, D> newNode = new TreeNode<K, D>(key, data, parent, NIL);
        if (isNil(parent)) { //tree was empty
            this.root = newNode;
        } else {
            if (this.comparator.compare(key, parent.getKey()) < 0) {
                parent.left = newNode;
            } else {
                parent.right = newNode;
            }
        }
        insertFix(newNode);
    }

    public boolean delete(K key) {
        TreeNode<K, D> nd = iterativeSearch(getRoot(), key);
        if (isNil(nd)) return false;
        delete(nd);
        return true;
    }

    /**
     * Not implemented.
     *
     * @return A flat list with all data in this tree.
     */
    public List<D> getData() {
        return null;
    }

    /**
     * Not implemented.
     *
     * @return A flat list with all keys in this tree.
     */
    public List<K> getKeys() {
        return null;
    }


    public String toString() {
        PrintVisitor pv = new PrintVisitor();
        pv.visit(getRoot());
        return pv.stb.toString();
    }

    public boolean isEmpty() {
        return isNil(getRoot());
    }

    protected TreeNode<K, D> getRoot() {
        return this.root;
    }

    /**
     * A factory method for creating new nodes.
     *
     * <p>This is provided to allow subclasses the posibility to create new <code>TreeNode</code>s, without making
     * the <code>TreeNode</code> constructor public. </p>
     *
     * @param key
     * @param data
     * @param parent
     * @param sentinel
     * @return
     */
    protected TreeNode<K, D> newTreeNode(K key, D data, TreeNode<K, D> parent, TreeNode<K, D> sentinel){
        return new TreeNode<K,D>(key, data, parent, sentinel);
    }

    protected boolean isNil(TreeNode<K, D> node) {
        return (node == this.NIL);
    }

    protected void print(PrintStream out) {
        PrintVisitor pv = new PrintVisitor(true);
        pv.visit(getRoot());
        out.print(pv.stb.toString());
    }

    protected void printTree(PrintStream out) {
        PrintTreeVisitor pv = new PrintTreeVisitor();
        pv.visit(getRoot());
        for (StringBuilder stb : pv.rulers) {
            out.print(stb.toString());
            out.println();
        }
    }

    protected String showAsTree() {
        PrintTreeVisitor pv = new PrintTreeVisitor();
        pv.visit(getRoot());
        String tree = "";
        for (StringBuilder stb : pv.rulers) {
            tree = tree + stb.toString();
            tree = tree + "\n";
        }
        return tree;
    }

    protected TreeNode<K, D> iterativeSearch(TreeNode<K, D> root, K k) {
        TreeNode<K, D> pointer = root;
        int side = this.comparator.compare(k, pointer.getKey());
        while (side != 0) {
            if (side < 0) {
                pointer = pointer.getLeftChild();
            } else {
                pointer = pointer.getRightChild();
            }
            if (isNil(pointer)) break;
            side = this.comparator.compare(k, pointer.getKey());
        }
        return pointer;
    }

    protected TreeNode<K, D> getMinNode(TreeNode<K, D> root) {
        if (isNil(root)) return root;

        TreeNode<K, D> pointer = root;
        while (!isNil(pointer.getLeftChild())) {
            pointer = pointer.getLeftChild();
        }
        return pointer;
    }

    protected TreeNode<K, D> getMaxNode(TreeNode<K, D> root) {
        if (isNil(root)) return root;
        TreeNode<K, D> pointer = root;
        while (!isNil(pointer.getRightChild())) {
            pointer = pointer.getRightChild();
        }
        return pointer;
    }

    protected TreeNode<K, D> successor(TreeNode<K, D> node) {
        if (!isNil(node.getRightChild())) {
            return getMinNode(node.getRightChild());
        }
        TreeNode<K, D> parent = node.getParent();
        while (!isNil(parent) && node == parent.getRightChild()) {
            node = parent;
            parent = node.getParent();
        }
        return parent;
    }

    protected TreeNode<K, D> predecessor(TreeNode<K, D> node) {
        if (!isNil(node.getLeftChild())) {
            return getMaxNode(node.getLeftChild());
        }
        TreeNode<K, D> parent = node.getParent();
        while (!isNil(parent) && node == parent.getLeftChild()) {
            node = parent;
            parent = node.getParent();
        }
        return parent;
    }

    protected void delete(TreeNode<K, D> node) {

        TreeNode<K, D> splice; //MyNode to splice out
        if (isNil(node.left) || isNil(node.right)) {
            splice = node;
        } else {
            splice = successor(node);
        }
        TreeNode<K, D> spliceChild;
        if (!isNil(splice.left)) {
            spliceChild = splice.left;
        } else {
            spliceChild = splice.right;
        }
        spliceChild.parent = splice.parent;

        if (isNil(splice.parent)) {
            this.root = spliceChild;
        } else if (splice == splice.parent.left) {
            splice.parent.left = spliceChild;
        } else {
            splice.parent.right = spliceChild;
        }
        if (splice != node) {
            node.key = splice.key;
            node.data = splice.data;
        }
        if (splice.color == BLACK) {
            deleteFix(spliceChild);
        }
    }

    protected void deleteFix(TreeNode<K, D> nd) {
        while (nd != this.root && nd.color == BLACK) {

            if (nd == nd.parent.left) {
                TreeNode<K, D> w = nd.parent.right;
                if (w.color == RED) {
                    w.color = BLACK;
                    nd.parent.color = RED;
                    leftRotate(nd.parent);
                    w = nd.parent.right;
                }
                if (w.left.color == BLACK && w.right.color == BLACK) {
                    w.color = RED;
                    nd = nd.parent;
                } else {
                    if (w.right.color == BLACK) {
                        w.left.color = BLACK;
                        w.color = RED;
                        rightRotate(w);
                        w = nd.parent.right;
                    }
                    w.color = nd.parent.color;
                    nd.parent.color = BLACK;
                    w.right.color = BLACK;
                    leftRotate(nd.parent);
                    nd = this.root;
                }
            } else {
                TreeNode<K, D> w = nd.parent.left;
                if (w.color == RED) {
                    w.color = BLACK;
                    nd.parent.color = RED;
                    rightRotate(nd.parent);
                    w = nd.parent.left;
                }
                if (w.right.color == BLACK && w.left.color == BLACK) {
                    w.color = RED;
                    nd = nd.parent;
                } else {
                    if (w.left.color == BLACK) {
                        w.right.color = BLACK;
                        w.color = RED;
                        leftRotate(w);
                        w = nd.parent.left;
                    }
                    w.color = nd.parent.color;
                    nd.parent.color = BLACK;
                    w.left.color = BLACK;
                    rightRotate(nd.parent);
                    nd = this.root;
                }
            }
        }
        nd.color = BLACK;
        //reset the NIL values (might be modified during this routine)
        NIL.color = BLACK;
        NIL.parent = NIL;
    }

    protected void leftRotate(TreeNode<K, D> nd) {
        TreeNode<K, D> nd2 = nd.right;
        if (isNil(nd2)) {
            return; // do nothing
        }
        //turn nd2's left subtree into nd's right subtree
        nd.right = nd2.left;
        if (!isNil(nd2.left)) {
            nd2.left.parent = nd;
        }
        //link nd's parent to nd2

        nd2.parent = nd.parent;
        if (isNil(nd.parent)) {
            this.root = nd2;
        } else {
            if (nd == nd.parent.left) {
                nd.parent.left = nd2;
            } else {
                nd.parent.right = nd2;
            }
        }
        //put nd on nd2's left
        nd2.setLeftChild(nd);
        nd.setParent(nd2);
    }

    protected void rightRotate(TreeNode<K, D> nd) {
        TreeNode<K, D> nd2 = nd.left;
        if (isNil(nd2)) {
            return; // do nothing
        }
        //turn nd2's right subtree into nd's left subtree
        nd.left = nd2.right;
        if (!isNil(nd2.right)) {
            nd2.right.parent = nd;
        }
        //link nd's parent to nd2
        TreeNode<K, D> px = nd.parent;
        nd2.parent = px;
        if (isNil(px)) {
            this.root = nd2;
        } else {
            if (nd == px.left) {
                px.left = nd2;
            } else {
                px.right = nd2;
            }
        }
        //put nd on nd2's right
        nd2.right = nd;
        nd.parent = nd2;
    }

    protected void insertFix(TreeNode<K, D> nd) {
        while (nd.parent.color == RED) {
            if (nd.parent == nd.parent.parent.left) {
                TreeNode<K, D> nd2 = nd.parent.parent.right;
                if (nd2.color == RED) {
                    nd.parent.color = BLACK;
                    nd2.color = BLACK;
                    nd.parent.parent.color = RED;
                    nd = nd.parent.parent;
                } else {
                    if (nd == nd.parent.right) {
                        nd = nd.parent;
                        leftRotate(nd);
                    }
                    nd.parent.color = BLACK;
                    nd.parent.parent.color = RED;
                    rightRotate(nd.parent.parent);
                }
            } else {
                TreeNode<K, D> nd2 = nd.parent.parent.left;
                if (nd2.color == RED) {
                    nd.parent.color = BLACK;
                    nd2.color = BLACK;
                    nd.parent.parent.color = RED;
                    nd = nd.parent.parent;
                } else {
                    if (nd == nd.parent.left) {
                        nd = nd.parent;
                        rightRotate(nd);
                    }
                    nd.parent.color = BLACK;
                    nd.parent.parent.color = RED;
                    leftRotate(nd.parent.parent);
                }
            }
        }
        getRoot().setColor(BLACK);
    }

    protected boolean checkRedBlack() {
        RedBlackChecker checker = new RedBlackChecker();
        getRoot().accept(checker);
        return checker.satisfied;
    }


    private class PrintVisitor implements Visitor<TreeNode<K, D>> {
        private boolean detailed = false;

        private PrintVisitor() {
        }

        private PrintVisitor(boolean detailed) {
            this.detailed = detailed;
        }

        final StringBuilder stb = new StringBuilder();

        public void visit(TreeNode<K, D> obj) {
            if (obj == NIL) {
                return;
            }
            if (!detailed) {
                visit(obj.getLeftChild());
                stb.append(" ").append(nodeWriter.write(obj));
                visit(obj.getRightChild());
            } else {
                stb.append(obj.getKey()).append(" (").append(obj.getColor()).append(") ")
                        .append(" left-child: ").append(obj.getLeftChild() == NIL ? "NIL" : nodeWriter.write(obj.getLeftChild()))
                        .append(" right-child: ").append(obj.getRightChild() == NIL ? "NIL" : nodeWriter.write(obj.getRightChild()));
                stb.append("\n");
                visit(obj.getLeftChild());
                visit(obj.getRightChild());
            }
        }
    }

    private class NodeWriter {

        public String write(TreeNode<K, D> nd) {
            if (isNil(nd)) {
                return "NIL";
            } else {
                return nd.key.toString();
            }
        }
    }

    private class PrintTreeVisitor implements Visitor<TreeNode<K, D>> {
        private boolean detailed = false;

        private PrintTreeVisitor() {
        }

        final List<StringBuilder> rulers = new ArrayList<StringBuilder>();
        int currentRule = -1;
        int col = 0;

        private static final String space = " ";

        public void visit(TreeNode<K, D> obj) {
            currentRule++;
            if (rulers.size() == currentRule) {
                StringBuilder stb = new StringBuilder();
                this.rulers.add(stb);
            }
            StringBuilder stb = rulers.get(currentRule);
            if (obj == NIL) {
                padToSize(this.col, stb);
                stb.append(space).append(nodeWriter.write(obj)).append(space);
                this.col = stb.length();
            } else {

                visit(obj.getLeftChild());
                padToSize(this.col, this.rulers.get(currentRule));
                rulers.get(currentRule).append(nodeWriter.write(obj));
                this.col = rulers.get(currentRule).length();
                visit(obj.getRightChild());

            }
            currentRule--;
        }

        private void padToSize(int length, StringBuilder stb) {
            while (length > stb.length()) {
                stb.append(space);
            }
        }
    }

    private class RedBlackChecker implements Visitor<TreeNode<K, D>> {

        boolean satisfied = true;

        public void visit(TreeNode<K, D> obj) {
            if (obj == NIL) {
                test(obj);
                return;
            }
            visit(obj.getLeftChild());
            test(obj);
            visit(obj.getRightChild());
        }

        private void test(TreeNode<K, D> nd) {
            if (nd == getRoot()) {
                satisfied = satisfied && nd.getColor() == BLACK;
                return;
            }
            if (isNil(nd)) {
                satisfied = satisfied && nd.getColor() == BLACK;
                return;
            }
            if (nd.getColor() == RED) {
                satisfied = satisfied && nd.getLeftChild().getColor() == BLACK;
                satisfied = satisfied && nd.getRightChild().getColor() == BLACK;
                //if (! satisfied) throw new IllegalStateException();
            }
            //TODO property 5

        }

    }


}
