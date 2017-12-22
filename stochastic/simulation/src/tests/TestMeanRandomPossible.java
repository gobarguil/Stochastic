import game.*;
import java.util.Random;

public class TestMeanRandomPossible {

    public static void main(String[] args) {
	    int nbCouples = Integer.parseInt(args[0]);
        int nbGames = Integer.parseInt(args[1]);
        double mean = 0;
        for(int game = 0; game < nbGames; game++) {
        	GameState newGame = new GameState(nbCouples);
        	Random randomGenerator = new Random();
            int nbSteps = 0;
            int nbPossibles = 0;
            int randomChoice = 0;
        	/*Start the game*/
        	int bestMatch = 0;
        	while(bestMatch < nbCouples) {
        		/*Tests the couple with proba closer to one half*/
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

        		/*Plays the first possible match*/
        		nbPossibles = newGame.getNbPossibles();
        		randomChoice = randomGenerator.nextInt(nbPossibles);
        		newGame.playMatch(newGame.getPossibleMatches().get(randomChoice));
                bestMatch = newGame.getBestMatchNumber();
                nbSteps += 1;
        	}
        	mean = mean + newGame.getNbTurns();
        }
        mean = mean/nbGames;
        System.out.println(mean);
    }
}