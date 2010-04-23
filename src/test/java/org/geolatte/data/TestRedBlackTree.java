package org.geolatte.data;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class TestRedBlackTree {

	static class Data {
		Integer data;
		Data(Integer data){
			this.data = data;
		}
		public String toString(){
			return this.data.toString();
		}
	}

	static final Comparator<Integer> comp = new Comparator<Integer>(){

		public int compare(Integer o1, Integer o2) {
//			System.out.println(String.format(" comparing %d and %d : %d", o1, o2, o1.compareTo(o2)));
			return o1.compareTo(o2);
		}
	};

	RedBlackTree<Integer, Data> simpleTree;
	final static Map<Integer,Data> simpleNodeMap = new HashMap<Integer, Data>();

	static {
		//this is the tree on p.g. 263, Introduction to Algorithms, Cormed e.a (2e edition)
		simpleNodeMap.put(3, new Data(3));
		simpleNodeMap.put(5, new Data(5));
		simpleNodeMap.put(6, new Data(6));
		simpleNodeMap.put(7, new Data(7));
		simpleNodeMap.put(10, new Data(10));
		simpleNodeMap.put(12, new Data(12));
		simpleNodeMap.put(13, new Data(13));
		simpleNodeMap.put(15, new Data(15));
		simpleNodeMap.put(16, new Data(16));
		simpleNodeMap.put(18, new Data(18));
		simpleNodeMap.put(20, new Data(20));
		simpleNodeMap.put(23, new Data(23));

	}

	@Before
	public void setUp() throws Exception {
		simpleTree = new RedBlackTree<Integer, Data> (comp);
		int[] keys = new int[simpleNodeMap.keySet().size()];
		int i=0;
		for (Integer k : simpleNodeMap.keySet()){
			keys[i++] = k;
		}
		keys = Permutation.randomPermutation(keys);
		for (Integer k : keys){
			simpleTree.insert(k, simpleNodeMap.get(k));
		}
	}

	@After
	public void tearDown() throws Exception {
		simpleTree = null;
	}

	@Test
	public void testPrintTree(){
		simpleTree.printTree(System.out);
	}

	@Test
	public void testRedBlackTree() {
		RedBlackTree<Integer, Data> tree = new RedBlackTree<Integer, Data>(comp);
		tree.isEmpty();

		assertTrue(simpleTree.checkRedBlack());

	}

	@Test
	public void testGet() {
		simpleTree.printTree(System.out);
		Integer key = Integer.valueOf(15);
		Data d = simpleTree.get(key);
		Data exp = simpleNodeMap.get(key);
		assertEquals(exp, d);
		assertTrue( d == exp);

		key = Integer.valueOf(3);
		d = simpleTree.get(key);
		exp = simpleNodeMap.get(key);
		assertEquals(exp, d);
		assertTrue( d == exp);

		key = Integer.valueOf(23);
		d = simpleTree.get(key);
		exp = simpleNodeMap.get(key);
		assertEquals(exp, d);
		assertTrue( d == exp);

		key = Integer.valueOf(10);
		d = simpleTree.get(key);
		exp = simpleNodeMap.get(key);
		assertEquals(exp, d);
		assertTrue( d == exp);

		key = Integer.valueOf(14);
		d = simpleTree.get(key);
		exp = simpleNodeMap.get(key);
		assertEquals(exp, d);
		assertTrue( d == exp);

		key = simpleTree.getRoot().getKey();
		d = simpleTree.get(key);
		assertEquals(simpleNodeMap.get(key), d);

	}

	@Test
	public void testGetMinimum() {
		assertEquals(Integer.valueOf(3), simpleTree.getMinimum().data);
	}

	@Test
	public void testGetMaximum() {
		assertEquals(Integer.valueOf(23), simpleTree.getMaximum().data);
	}

	@Test
	public void testGetSuccessor() {
		assertEquals( Integer.valueOf(5), simpleTree.getSuccessor(3).data);
		assertEquals(Integer.valueOf(18), simpleTree.getSuccessor(16).data);
		assertNull(simpleTree.getSuccessor(23));
	}

	@Test
	public void testGetPredecessor() {
		assertNull(simpleTree.getPredecessor(3));
		assertEquals(Integer.valueOf(3), simpleTree.getPredecessor(5).data);
		assertEquals(Integer.valueOf(13), simpleTree.getPredecessor(15).data);
		assertEquals(Integer.valueOf(20), simpleTree.getPredecessor(23).data);
	}

	@Test
	public void testInsert() {
		try {
			simpleTree.insert(6, new Data(6));
			throw new IllegalStateException("Should have thrown exception for double entry");
		} catch(RuntimeException ex){
			//OK
		}

		//see pp 282 - Cormen e.a.
		RedBlackTree<Integer, Data> rdt = new RedBlackTree<Integer, Data>(comp);

		rdt.insert(11,new Data(11));
		rdt.insert(2, new Data(2));
		rdt.insert(14, new Data(14));
		rdt.insert(15, new Data(15));
		rdt.insert(1, new Data(1));
		rdt.insert(7, new Data(7));
		rdt.insert(5, new Data(5));
		rdt.insert(8, new Data(8));
		rdt.print(System.out);
		System.out.println();
		System.out.println("INSERTING 4");
		System.out.println();
		rdt.insert(4, new Data(4));
		rdt.printTree(System.out);

	}

	@Test
	public void testCompPerformance(){
		int size = 100000;
		int[] inVals = Permutation.randomPermutation(size);
		int testVal = inVals[size/2];
		TreeMap<Integer, Integer> treeMap = new TreeMap<Integer,Integer>(comp);
		long t1 = System.currentTimeMillis();
		for (int i = 0; i < inVals.length; i++){
			treeMap.put(inVals[i], inVals[i]);
		}
		long t2 = System.currentTimeMillis();
		System.out.println(String.format("TreeMap loaded %d items in %d ms.", size, t2-t1));

		RedBlackTree<Integer, Integer> rbTree = new RedBlackTree<Integer, Integer>(comp);
		t1 = System.currentTimeMillis();
		for (int i = 0; i < inVals.length; i++){
			rbTree.insert(inVals[i], inVals[i]);
		}
		t2 = System.currentTimeMillis();
		System.out.println(String.format("RedBlackTree loaded %d items in %d ms.", size, t2-t1));


		t1 = System.nanoTime();
		Integer v = rbTree.get(testVal);
		t2 = System.nanoTime();
		System.out.println(String.format("RBT found key in %d ms", (t2-t1)/1000));


		t1 = System.nanoTime();
		v = treeMap.get(testVal);
		t2 = System.nanoTime();
		System.out.println(String.format("TreeMap found key in %d ms", (t2-t1)/1000));

		t1 = System.nanoTime();
		int removeSize = size/10;
		for (int i = 0; i < removeSize; i++){
			treeMap.remove(inVals[i]);
		}
		t2 = System.nanoTime();
		System.out.println(String.format("Treemap removed %d in %d ms.", removeSize, (t2-t1)/1000));

		t1 = System.nanoTime();
		for (int i = 0; i < removeSize; i++){
			rbTree.delete(inVals[i]);
		}
		t2 = System.nanoTime();
		System.out.println(String.format("RedBlackTree removed %d in %d ms.", removeSize, (t2-t1)/1000));

		assertTrue(rbTree.checkRedBlack());

	}

	@Test
	public void testLeftRotate(){
		//TODO -- make a more formal testcase
//		TreeNode<Integer, Data> lrnd = simpleTree.iterativeSearch(simpleTree.getRoot(),5);
//		TreeNode<Integer, Data> rrnd = lrnd.getRightChild();
//		if (simpleTree.isNil(rrnd)){
//			System.out.println("condition not satisfied");
//			return;
//		}
//
//		simpleTree.print(System.out);
//		simpleTree.leftRotate(lrnd);
//
//		System.out.println("AFTER LEFT ROTATE");
//		simpleTree.print(System.out);
//		simpleTree.rightRotate(rrnd);
//		System.out.println("AFTER RIGHT ROTATE");
//		simpleTree.print(System.out);
	}

	@Test
	public void testDelete() {
		simpleTree.delete(10);
		assertTrue(simpleTree.NIL.color == RedBlackTree.BLACK);
		assertTrue(simpleTree.checkRedBlack());
		simpleTree.delete(3);
		assertTrue(simpleTree.NIL.color == RedBlackTree.BLACK);
		assertTrue(simpleTree.checkRedBlack());
		simpleTree.delete(23);
		assertTrue(simpleTree.checkRedBlack());
		assertTrue(simpleTree.NIL.color == RedBlackTree.BLACK);
	}

//	@Test
//	public void testGetData() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetKeys() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testHashCode() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testEquals() {
//		fail("Not yet implemented");
//	}

	@Test
	public void testToString() {
		int[] keys = new int[simpleNodeMap.keySet().size()];
		int i = 0;
		for (Integer k : simpleNodeMap.keySet()){
			keys[i++] = k;
		}
		Arrays.sort(keys);
		StringBuilder stb = new StringBuilder();
		for (Integer k : keys){
			stb.append(k).append(" ");
		}
		String expected = stb.toString().trim();
		String evaluated = simpleTree.toString().trim();
		System.out.println(evaluated);
		assertEquals(expected,evaluated);
	}
//
//	@Test
//	public void testPrint(){
//		System.out.println("Tree:");
//		simpleTree.print(System.out);
//	}

}
