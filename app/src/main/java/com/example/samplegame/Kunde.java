package com.example.samplegame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Kunden class
 * @author flori_000
 */
public class Kunde {
	public int timestamp = 0;
	public ArrayList <Pflanze> nachfrage = new ArrayList<Pflanze>();
	public ArrayList <Integer> quantity = new ArrayList<Integer>();
	//public int belohnungTaler;
	public int belohnungWerkzeuge = 1;
	public String name;
	
	/**
	 * Standard constructor for Kunde
	 */
	public Kunde(){
		int anzahlNachfragen = randInt(1,5);
		//get list of all possible plantable plants as for now
		ArrayList<Pflanze> possiblePlants = new ArrayList<Pflanze>();
		
		for (int i = 0; i < GameBoard.pflanzen.size(); i++){
			if (GameBoard.pflanzen.get(i).tier <= GameBoard.werkzeuge){
				possiblePlants.add(GameBoard.pflanzen.get(i));
			}
		}
		
		long seed = System.nanoTime();
		Collections.shuffle(possiblePlants, new Random(seed));
		
		for (int i = 0; i < Math.min(anzahlNachfragen, possiblePlants.size()) ; i++){
			nachfrage.add(possiblePlants.get(i));
			quantity.add(randInt(1,10));
		}
		name = "Kunde";
	}
	
	/**
	 * Helper function for generating a random number between min and max
	 * @param min	lower bound for the generated number
	 * @param max	upper bound for the generated number
	 * @return	generated number
	 */
	public static int randInt(int min, int max) {
	    Random rand = new Random();
	    int randomNum = rand.nextInt((max - min) + 1) + min;
	    return randomNum;
	}
	
	/**
	 * method that will process the deal the given customers offer
	 */
	public void handeln(){
		if (bezahlbar()){
			GameBoard.taler = GameBoard.taler + belohnungTaler();
			GameBoard.werkzeuge = GameBoard.werkzeuge + belohnungWerkzeuge;
			for (int i = 0; i < nachfrage.size(); i++){
				GameBoard.quantity.set(GameBoard.inventar.indexOf(nachfrage.get(i)), GameBoard.quantity.get(GameBoard.inventar.indexOf(nachfrage.get(i))) - quantity.get(i) );
			}
		}
	}
	
	/**
	 * Returns the appropriate amount of taler that should be the reward for a given offer
	 * @return	number
	 */
	public int belohnungTaler(){
		double belohnung = 0;
		for (int i = 0; i < nachfrage.size(); i++){
			belohnung += nachfrage.get(i).kosten * quantity.get(i) * 0.15;
		}
		return (int) Math.max(1, belohnung);
	}
	
	/**
	 * Check if the deal with this Kunde is practically possible
	 * @return	true, if deal is possible, false else
	 */
	public boolean bezahlbar(){
		boolean requirementsMet = true;
		for (int i = 0; i < this.nachfrage.size(); i++){
			if (GameBoard.quantity.get(GameBoard.pflanzen.indexOf(nachfrage.get(i))) < quantity.get(i)){
				requirementsMet = false;
			}
		}
		return requirementsMet;		
	}
}
