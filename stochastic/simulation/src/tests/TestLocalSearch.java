import game.*;
import strategy.*;

public class TestLocalSearch {
	
	public static void main(String[] args) {
		
		int nbCouples = Integer.parseInt(args[0]);
    	GameState newGame= new GameState(nbCouples);
    	//System.out.println("Match à trouver :");
    	//newGame.trueMatch.displayMatch(); pour vérifier, il faut mettre trueMatch en public
    	Match match;
    	Match prevTry=null;
    	int tour = 0;
    	
    	while(true){
    		
    		double[][] probaCouples = newGame.getPossiblePartners();
            int leftToTest = 0;
            int rightToTest = 0;
            double probCloseHalf = 0;
            for (int i = 0; i < nbCouples; i++) {
                for (int j = 0; j < nbCouples; j++) {
                    /*The couple i -- j is closer to the probability one half
                      than the previous couple we were trying to test*/
                    if (Math.abs(probCloseHalf - 0.5) > 
                        Math.abs(probaCouples[i][j] - 0.5)) {
                            probCloseHalf = probaCouples[i][j];
                            leftToTest = i;
                            rightToTest = j;
                    }
                }
            }
            /*Plays the couple to test*/
            newGame.verifyCouple(leftToTest, rightToTest);
    		
    		match=localSearch.nextTry(prevTry,newGame.getPossibleMatches(),tour);
    		
    		if(newGame.playMatch(match)==match.getNbCouples()){
    			//System.out.println("Success");
    			//match.displayMatch();
    			System.out.println(tour);
    			return;
    		}
    		
    		++tour;
    		prevTry=match;
    	}
    }
}
