import game.*;

public class TestFourCouples {

    public static void main(String[] args) {
    	GameState newGame= new GameState(4);
    	newGame.displayPossibles();
    	newGame.displayProbaCouples();
    	newGame.verifyCouple(0, 0);
    	newGame.displayPossibles();
    	newGame.displayProbaCouples();
    	System.out.println(newGame.getNbPossibles());
    }
}
