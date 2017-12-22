package game;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

/**
* Class recensing all the possible matches left in the game.
*/
public class GameState{
	
	/*The number of couples in the game*/
	private int nbCouples;
	/*The list of remaining possible couples in the game*/
	private List<Match> possibles;
	/*The list of tested couples*/
	private int nbPossibles;
	/*The true match, the goal of the game is to find this match*/
	private List<Match> tested;
	/*The number of remaining possible couples*/
	private Match trueMatch;
	/*The number of rigth couples in the best match tried*/
	private int bestMatchNumber;
	/*Tells if we are at a verification step or at a guess step*/
	private boolean verifyStep;
	/*The number of turns since we started the game*/
	private int nbTurns;
	/*For each person the persons they are possibly matched with in the
	  true match possiblePartners[i][j] gives the probability that i is
	  paired with j*/
	private double[][] possiblePartners;

	/**
	* Initializes the list of all possible matches with nbCouples couples
	*/
	public GameState(int nbCouples){

		/*Initialization of all possible couples*/
		this.nbCouples = nbCouples;
		possibles = new ArrayList<Match>();
		int[] couples = new int[nbCouples];
		for (int i = 0; i < nbCouples; i++){
			couples[0] = i;
			possibles.addAll(recursivePossibleMatches(nbCouples, 1, couples));;
		}
		nbPossibles = this.possibles.size();

		/*Initialization of the true match*/
		int[] trueCouples = new int[nbCouples];
		/*Randomly choose the matches*/
		Random randomGenerator = new Random();
		List<Integer> notChosen = new ArrayList<Integer>();
		int randomChoice;
		for (int i = 0; i < nbCouples; i++) {
			notChosen.add(i);
		}
		for (int i = 0; i < nbCouples; i++) {
			randomChoice = randomGenerator.nextInt(notChosen.size());
			trueCouples[i] = notChosen.get(randomChoice);
			notChosen.remove(randomChoice);
		}
		this.trueMatch = new Match(nbCouples, trueCouples);

		/*The first action must be a verification of a couples*/
		verifyStep = true;
		bestMatchNumber = 0;
		nbTurns = 0;

		/*Initialization of the probabilities of the possible partners*/
		possiblePartners = new double[nbCouples][nbCouples];
		for (int i = 0; i < nbCouples; i++){
			for (int j = 0; j < nbCouples; j++){
				possiblePartners[i][j] = 1.0/(double)nbCouples;
			}
		}
	}

	/**
	* Recursively constructs the list of all possible matches
	*/
	private static List<Match> recursivePossibleMatches(int nbCouples, 
		int offset, int[] chosenCouples) {
		List<Match> possibleCouples = new ArrayList<Match>();
		/*Stopping condition of the recursion*/
		if (offset == nbCouples - 1) {
			for (int i = 0; i < nbCouples; i++) {
				if (!alreadyChosen(i, chosenCouples, offset)) {
					chosenCouples[offset] = i;
					Match possibleMatch = new Match(nbCouples, chosenCouples);
					possibleCouples.add(possibleMatch);
				}
			}
			return possibleCouples;
		}
		else {
			/*We will fix a couple and recursively construct
			  all the possible couples with this new couple fixed*/
			for (int i = 0; i < nbCouples; i++) {
				if (!alreadyChosen(i, chosenCouples, offset)) {
					chosenCouples[offset] = i;
					possibleCouples.addAll(
						recursivePossibleMatches(nbCouples,
						 offset + 1, chosenCouples));
				}
			}
			return possibleCouples;
		}	
	}

	public double[][] getPossiblePartners() {
		return this.possiblePartners;
	}

	public int getBestMatchNumber() {
		return this.bestMatchNumber;
	}

	public int getNbPossibles() {
		return this.nbPossibles;
	}

	public int getNbTurns() {
		return this.nbTurns;
	}

	public List<Match> getPossibleMatches() {
		return this.possibles;
	}


	/**
	* Plays the match test in the game
	*/
	public int playMatch(Match test) {
		/*We cannot play a match in the verify step*/
		if (verifyStep) {
			return 0;
		}

		/*Computes the new possible matches with the tested match*/
		int common = this.trueMatch.nbCommon(test);
		this.computeNewPossibles(test, common);

		if (common > bestMatchNumber) {
			bestMatchNumber = common;
		}
		verifyStep = true;
		nbTurns += 1;
		return common;
	}

	/*Verifies if the couple a -- b is in the true match or not*/
	public boolean verifyCouple(int a, int b) {
		/*We cannot verify a couple when not in the verify step*/
		if (!verifyStep) {
			return false;
		}

		boolean couplePresent = trueMatch.couplePresent(a, b);
		this.computePossiblesCoupleTested(a, b, couplePresent);

		verifyStep = false;
		return couplePresent;
	}

	/**
	* Removes all matches where the couple a -- b is not in the match if present
	* is true and otherwise removes all the matches where a -- b is present
	*/
	private void computePossiblesCoupleTested(int a, int b, boolean present){
		/*Erase the old probabilities of presence of couples
		  to allow an easier computation of the new probabilities*/
		this.eraseOldProbaCouples();
		List<Match> toRemove = new ArrayList<Match>();
		for (Match match:this.possibles) {
			if (match.couplePresent(a,b) != present) {
				toRemove.add(match);
			}
			/*The match is possible, we add the contribution of 
			  the match to the count of matches where 
			  each couple is present*/
			else {
				this.updateCountCouplesPresent(match.getCouples());
			}
		}
		this.possibles.removeAll(toRemove);
		nbPossibles = possibles.size();
		/*Resize the count of matches where each couple is present*/
		this.resizeProbaCouples();
	}

	/**
	* Remove all the impossible matches from the list given that
	* the match test has k right couples
	*/
	private void computeNewPossibles(Match test, int k){
		/*Erase the old probabilities of presence of couples
		  to allow an easier computation of the new probabilities*/
		this.eraseOldProbaCouples();
		List<Match> toRemove = new ArrayList<Match>();
		for (Match match:this.possibles) {
			if (!match.isPossible(test, k)) {
				toRemove.add(match);
			}
			/*The match is possible, we add the contribution of 
			  the match to the count of matches where 
			  each couple is present*/
			else {
				this.updateCountCouplesPresent(match.getCouples());
			}
		}
		this.possibles.removeAll(toRemove);
		nbPossibles = possibles.size();
		/*Resize the count of matches where each couple is present*/
		this.resizeProbaCouples();
	}

	/**
	* Tool to compute the probabilites that couples are present
	* in the true match. Suppose that the old probabilities were erased
	* and add 1 to the count of matches where a couple appear 
	* for each couple present in couples.
	* Must be used only with for argument the couples of a possible match. 
	*/
	private void updateCountCouplesPresent(int[] couples) {
		for (int i = 0; i < nbCouples; i++) {
 			this.possiblePartners[i][couples[i]] += 1.0;
 		}
	}

	/**
	* Sets the array of probabilities of presence of a couple to only
	* zeros. This makes the computation of probabilities of presence of
	* couples easier once they change since we can count for each couple the
	* number of matches they are in and then normalize it to obtain probabilities
	*/
	private void eraseOldProbaCouples() {
		for (int i = 0; i < nbCouples; i++) {
			for (int j = 0; j < nbCouples; j++) {
				this.possiblePartners[i][j] = 0.0;
			}
		}
	}

	/**
	* Tool to resize the probability that each couple appear once
	* we filled the array possiblePartners with the number of time 
	* each couple appears in the possible matches
	*/
	private void resizeProbaCouples() {
		for (int i = 0; i < nbCouples; i++) {
			for(int j = 0; j < nbCouples; j++) {
				this.possiblePartners[i][j] /= nbPossibles;
			}
		}
	}

	/**
	* Displays all the possible matches in the game
	*/
	public void displayPossibles() {
		for (Match match:this.possibles){
			match.displayMatch();
			System.out.println();
		}
	}

	/**
	* Displays the probability of presence of each couple
	*/
	public void displayProbaCouples() {
		for (int i = 0; i < nbCouples; i++) {
			for (int j = 0; j < nbCouples; j++) {
				System.out.println("The couple " + i + " -- " + j +
					" is present with probability: " +
					possiblePartners[i][j]);
			}
		}
	}

	/**
	* Gives the probability that the match test has
	* k couples in common with the true match
	*/
	public double probMatch(Match test, int k) {
		/*The number of possible matching which
		have k common couples with test*/
		int fittingMatches = 0;
		for (Match match:this.possibles) {
			if(match.isPossible(test, k)){
					fittingMatches += 1;
			}
		}
		return (double)fittingMatches/(double)nbPossibles;
	}

	/**
	* Helper for the creation of all possible couples, tells for a couple
	* if a person has already been chosen (corresponding to the portion
	* of the array before offset)
	*/
	private static boolean alreadyChosen(int i, int[] chosenCouples, int offset){
		for (int j = 0; j < offset; j++){
			if (chosenCouples[j] == i) {
				return true;
			}
		}
		return false;
	}

}