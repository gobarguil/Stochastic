package strategy;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;

import game.Match;

public class localSearch {
	
	private static ArrayList<Double> prob = new ArrayList<Double>();
	private static double T=10000; //température init
	private static double lambda=0.99;  //constante de décroissance : Ti+1 = lambda*Ti
	
	private static void nextProb(Match prevTry, List<Match> possible, int tour){
		double p;
		if (prob!=null){
			prob.removeAll(prob);
		}
		if (tour==0){
			p=1/(double)possible.size();
			for(Match match:possible){
				prob.add(p);
			}
			return;
		}
		for(Match match:possible){
			if (prevTry.nbCommon(match)<prevTry.getNbCouples()){
				p=Math.exp(((double)(prevTry.nbCommon(match) - prevTry.getNbCouples())/(T*Math.pow(lambda,tour)))); //modifier T en fonction du tour
			} else {
				p=0;
			}
			prob.add(p);
		}
	}
	
	public static Match nextTry(Match prevTry, List<Match> possible, int tour) {
		
		Random randomGenerator = new Random();
		nextProb(prevTry, possible, tour);
		double p = randomGenerator.nextDouble();
		int i=0;
		double s=0;
		
		for(Match match:possible){
			s+=prob.get(i);
			if (p<s){
				return match;
			}
			++i;
		}
		return null;
	}

}
