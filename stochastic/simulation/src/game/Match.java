package game;

import java.io.*;

public class Match {
	private int nbCouples;
	/*If couples[a] = b this means the couple a -- b is in the match*/
	private int[] couples;

	/**
	* Creates a match
	*/
	public Match(int nbCouples, int[] couples){
		this.nbCouples = nbCouples;
		if (couples.length != nbCouples) {
			System.out.println("Number of couples not coherent with couples given");
		}
		else {
			this.couples = new int[nbCouples];
			for (int i = 0; i < nbCouples; i++) {
				this.couples[i] = couples[i];
			}
		}
	}

	/**
	* Gives the number of common couples with the match test
	*/
	public int nbCommon(Match test) {
		int common = 0;
		int [] couplesTest = test.getCouples();
		for (int i = 0; i < nbCouples; i++) {
			if(couples[i] == couplesTest[i]){
				common += 1;
			}
		}
		return common;
	}

	public int[] getCouples(){
		return couples;
	}

	public int getNbCouples(){
		return nbCouples;
	}

	public void displayMatch() {
		for (int i = 0; i < this.nbCouples; i++) {
			System.out.println(i + " --- " + this.couples[i]);
		}
	}

	/*Tels if the couple a -- b is present in the match*/
	public boolean couplePresent(int a, int b) {
		if(a > nbCouples - 1 || b > nbCouples - 1) {
			return false;
		}
		return couples[a] == b;
	}

	/**
	* Returns true if the match is possible given that
	* the test match has k right couples
	*/
	public boolean isPossible(Match test, int k) {
		return this.nbCommon(test) == k;
	}
}