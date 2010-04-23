package org.geolatte.data;

/**
 * Static utility to generate a random permutation of input - used in test classes.
 */
public class Permutation {
	
	public static int[] randomPermutation(int[] inAr){
		int[] outAr = (int[])inAr.clone();
		
		for (int k = outAr.length - 1; k > 0; k--) {
		    int w = (int)Math.floor(Math.random() * (k+1));
		    int temp = outAr[w];
		    outAr[w] = outAr[k];
		    outAr[k] = temp;
		}
		return outAr;
	}
	
	public static int[] randomPermutation(int length){
		int[] outAr = new int[length];
		for (int i = 0; i < length; i++){
			outAr[i] = i;
		}
		return randomPermutation(outAr);
	}

}
