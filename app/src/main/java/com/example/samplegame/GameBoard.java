package com.example.samplegame;

import java.util.ArrayList;

import com.example.gameframework.gameframework.implementations.AndroidGame;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

/**
 * The GameBoard class providing the game logic
 * @author flori_000
 */
public class GameBoard {
	public Feld feld[][] = new Feld[7][7];
	public static String message;
	public static boolean messageFlag = false;
	public static Pflanze karotte = new Pflanze("Karotte", Assets.karotte, Assets.karotte, 1, 5*60, 2, 0);
	//public static Pflanze karotte = new Pflanze("Karotte", Assets.karotte, Assets.karotteInv, 1, 5, 2, 0);
	public static Pflanze zucchini = new Pflanze("Zucchini", Assets.zucchini, Assets.zucchini, 2, 10*60, 3, 1);
	public static Pflanze kartoffel = new Pflanze("Kartoffel", Assets.kartoffel, Assets.kartoffel, 3, 15*60, 3, 5);
	public static Pflanze kuerbis = new Pflanze("Kuerbis", Assets.kuerbis, Assets.kuerbis, 9, 30*60, 2, 10);
	public static Pflanze paprika = new Pflanze("Paprika", Assets.paprika, Assets.paprika, 9, 45*60, 3, 20);
	public static Pflanze tomate = new Pflanze("Tomate", Assets.tomate,Assets.tomate, 12, 60*60, 3, 30);
	public static Pflanze broccoli = new Pflanze("Broccoli", Assets.broccoli, Assets.broccoli, 27, 90*60, 2, 40);
	public static Pflanze zwiebel = new Pflanze("Zwiebel", Assets.zwiebel, Assets.zwiebel, 36, 120*60, 2, 50);
	public static Pflanze gurke = new Pflanze("Gurke", Assets.gurke, Assets.gurke, 45, 150*60, 2, 60);
	public static Pflanze aubergine = new Pflanze("Aubergine", Assets.aubergine, Assets.aubergine, 54, 180*60, 2, 70);
	public static Pflanze spargel = new Pflanze("Spargel", Assets.spargel, Assets.spargel, 48, 240*60, 3, 80);
	public static Pflanze radieschen = new Pflanze("Radieschen", Assets.radieschen, Assets.radieschen, 90, 300*60, 2, 90);
	public static Pflanze salat = new Pflanze("Salat", Assets.salat, Assets.salat, 108, 360*60, 2, 100);
	
	public static Pflanze boden = new Pflanze("Boden", Assets.soil, Assets.soil, 0, 0, 0, 0);
	public static ArrayList<Pflanze> pflanzen = new ArrayList<Pflanze>();
	public static ArrayList<Pflanze> inventar = new ArrayList<Pflanze>();
	public static ArrayList<Integer> quantity = new ArrayList<Integer>();
	public static boolean firstRun = false;
	public static int taler = 10;
	public static int werkzeuge = 2;
	private static Context context;
	public static boolean notificationsOn = false;
	public static int REQUEST_CODE = 0;
	public static ArrayList<Kunde> kunden = new ArrayList<Kunde>();
	
	/**
	 * GameBoard constructor
	 * @param game	The given game
	 */
	public GameBoard(AndroidGame game){
		GameBoard.context = game.getApplicationContext();
		
		//Initialize the fields
		for (int i = 1; i <= 7; i++){
        	for (int j = 1; j <= 7; j++){        		
        		feld[i-1][j-1] = new Feld(GameBoard.boden, (int) (System.currentTimeMillis()/1000), i, j);
        	}
        }
		
		//Create a collection of all plants
		pflanzen.clear();
		pflanzen.add(karotte);
		quantity.add(0);
		pflanzen.add(zucchini);
		quantity.add(0);
		pflanzen.add(kartoffel);
		quantity.add(0);
		pflanzen.add(kuerbis);
		quantity.add(0);		
		pflanzen.add(paprika);
		quantity.add(0);
		pflanzen.add(tomate);
		quantity.add(0);		
		pflanzen.add(broccoli);
		quantity.add(0);	
		pflanzen.add(zwiebel);
		quantity.add(0);	
		pflanzen.add(gurke);
		quantity.add(0);
		pflanzen.add(aubergine);
		quantity.add(0);
		pflanzen.add(spargel);
		quantity.add(0);
		pflanzen.add(radieschen);
		quantity.add(0);
		pflanzen.add(salat);
		quantity.add(0);
		
		//Create three customers
		kunden.clear();
		kunden.add(new Kunde());
		kunden.add(new Kunde());
		kunden.add(new Kunde());
		
		load();
	}
	
	/**
	 * Plants the selected item on the target coordination
	 * @param selectedItem	The selected item
	 * @param targetX		The selected x coordinate
	 * @param targetY		The selected y coordinate
	 */
	public void pflanze(int selectedItem, int targetX, int targetY){
		//Check if planting the selected plant at the selected position is valid
		if (quantity.get(selectedItem) >= 1 && feld[targetX][targetY].getBewachsenVon().name.equals("Boden")){
			//Commit changes
			feld[targetX][targetY].setBewachsenVon(inventar.get(selectedItem));
			feld[targetX][targetY].setPflanzZeitpunkt();
			quantity.set(selectedItem, quantity.get(selectedItem) - 1);
			//Send alarm
			if (notificationsOn){
				AlarmManager alarmManager =	(AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
				Intent intent = new Intent (context, AlarmReceiver.class);
				intent.putExtra("message", feld[targetX][targetY].getBewachsenVon().name + " ist ausgewachsen. Ernte sie jetzt!");
				intent.putExtra("title", "Pflanze ausgewachsen!");
				PendingIntent alarmIntent = PendingIntent.getBroadcast(context, REQUEST_CODE++, intent, 0);
				alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 1000 * feld[targetX][targetY].getBewachsenVon().wachstumsDauer, alarmIntent);
			}
			saveInventoryItem(selectedItem);
			saveFeld(feld[targetX][targetY]);
		}
	}
	
	/**
	 * Returns the entire plant-Object of the name of a plant
	 * @param name	plant name
	 * @return		plant-object
	 */
	public static Pflanze getPflanzeByName(String name){
		for (Pflanze pflanze: pflanzen){
			if (pflanze.name.equals(name)){
				return pflanze;
			}				
		}
		return boden;		
	}
	
	/**
	 * Deals items with a given customer
	 * @param i		index of the Kunde
	 */
	public void deal(int i){
		if (GameBoard.kunden.get(i).bezahlbar()){
			GameBoard.kunden.get(i).handeln();
			//GameScreen.showMessage("Du erhaelst " + GameBoard.kunden.get(i).belohnungTaler + " Taler!");
			GameBoard.kunden.set(i, new Kunde());
			saveKunden();
			saveInventory();
			saveStats();
		}
	}
	
	/**
	 * Sell plants
	 * @param selectedItem		index of item that should be sold
	 */
	public void verkaufePflanze(int selectedItem){
		if (quantity.get(selectedItem) >= 10){
			//Commit changes
			taler = taler + inventar.get(selectedItem).kosten;
			quantity.set(selectedItem, quantity.get(selectedItem) - 10);
		}
		saveInventoryItem(selectedItem);
		saveStats();
	}
	
	/**
	 * Harvest feld
	 * @param targetX	x coordinate of the feld
	 * @param targetY	y coordinate of the feld
	 */
	public void ernte(int targetX, int targetY){
		//Check if feld is ready for harvesting
		if (feld[targetX][targetY].getPflanzZeitpunkt() + feld[targetX][targetY].getBewachsenVon().wachstumsDauer <= System.currentTimeMillis()/1000 && !feld[targetX][targetY].getBewachsenVon().name.equals("Boden")){
			int oldPflanzeIndex = inventar.indexOf(feld[targetX][targetY].getBewachsenVon());
			quantity.set(inventar.indexOf(feld[targetX][targetY].getBewachsenVon()), feld[targetX][targetY].getBewachsenVon().erntenMultiplikator + quantity.get(inventar.indexOf(feld[targetX][targetY].getBewachsenVon())));
			feld[targetX][targetY].setBewachsenVon(boden);
			saveFeld(feld[targetX][targetY]);
			saveInventoryItem(oldPflanzeIndex);
		}
	}
	
	/**
	 * Buy item
	 * @param selectedItem	index of item that should be bought
	 */
	public void kaufePflanze(int selectedItem){
		//Check if enough money exists and required tier level is reached
		if (inventar.get(selectedItem).kosten <= taler && inventar.get(selectedItem).tier <= werkzeuge){
			//Commit changes
			taler = taler - inventar.get(selectedItem).kosten;
			quantity.set(selectedItem, quantity.get(selectedItem) + 1);
		}
		saveInventoryItem(selectedItem);
		saveStats();
	}
	
	/**
	 * Getter for the feld array
	 * @param a	x coordinate
	 * @param b	y coordinate
	 * @return	feld object
	 */
	public Feld getFeld(int a, int b) {
		return feld[a-1][b-1];
	}
	
	/**
	 * Setter for the feld array
	 * @param feld	updated feld object
	 */
	public void setFeld(Feld feld[][]) {
		this.feld = feld;
	}
	
	/**
	 * Saves a single item and its quantity of the inventory
	 * @param i	index of the item in the inventory
	 */
	public void saveInventoryItem(int i){
		GartenDBOpener dbHelper = new GartenDBOpener(context);
		dbHelper.updateItem(inventar.get(i), quantity.get(i));
	}
	
	/**
	 * Saves a single feld object
	 * @param feld	feld that should be saved
	 */
	public void saveFeld(Feld feld){
		GartenDBOpener dbHelper = new GartenDBOpener(context);
		dbHelper.updateFeld(feld);
	}
	
	/**
	 * Saves the entire feld array
	 */
	public void saveInventory(){
		GartenDBOpener dbHelper = new GartenDBOpener(context);
		dbHelper.deleteInventory();
		//Log.e("saveInventory: deleteInventory", inventar);
		for (Pflanze pflanze: inventar){
			dbHelper.insertItem(pflanze, quantity.get(inventar.lastIndexOf(pflanze)));
		}
		dbHelper.close();
	}
	
	/**
	 * Saves the general information
	 */
	public static void saveStats(){
		GartenDBOpener dbHelper = new GartenDBOpener(context);
		dbHelper.deleteStats();
		dbHelper.saveStats();
		dbHelper.close();
	}
	
	/**
	 * Saves the Kunden
	 */
	public void saveKunden(){
		GartenDBOpener dbHelper = new GartenDBOpener(context);
		dbHelper.deleteKunden();
		for (int i = 0; i < 3; i++){
			for (int j = 0; j < kunden.get(i).nachfrage.size(); j++){
				dbHelper.insertKundenItem(i, kunden.get(i).nachfrage.get(j), kunden.get(i).quantity.get(j));
			}
		}
		dbHelper.close();
	}
	
	/**
	 * Saves the entire garden
	 */
	public void saveGarten(){
		GartenDBOpener dbHelper = new GartenDBOpener(context);
		dbHelper.deleteGarten();
		for (int a = 1; a <= 7; a++){
			for (int b = 1; b <= 7; b++){
				dbHelper.insertFeld(feld[a-1][b-1]);
			}
		}
		dbHelper.close();
	}
	
	/**
	 * Loads the entire garden
	 */
	public void loadGarten(){
		GartenDBOpener dbHelper = new GartenDBOpener(context);
		ArrayList<Feld> garten = dbHelper.getAllFelder();
		for (Feld feldBuffer: garten){
			feld[feldBuffer.getX()-1][feldBuffer.getY()-1] = feldBuffer;
		}
		dbHelper.close();
	}
	
	/**
	 * Loads the entire inventory
	 */
	public void loadInventory(){
		quantity.clear();
		inventar.clear();
		GartenDBOpener dbHelper = new GartenDBOpener(context);
		for (Pflanze pflanze: pflanzen){
				inventar.add(pflanze);
				quantity.add(dbHelper.getItemQantity(pflanze));
		}
		dbHelper.close();
	}
	
	/**
	 * Loads all Kunden
	 */
	public void loadKunden(){
		GartenDBOpener dbHelper = new GartenDBOpener(context);
		dbHelper.loadAllKundenItems();
		dbHelper.close();
	}
	
	/**
	 * Loads the general information
	 */
	public void loadStats(){
		GartenDBOpener dbHelper = new GartenDBOpener(context);
		dbHelper.loadStats();
		dbHelper.close();
	}
	
	/**
	 * Load method for all needed data
	 */
	public void load(){
		GartenDBOpener dbHelper = new GartenDBOpener(context);
		if (dbHelper.firstRun() == 0){
			dbHelper.close();
			//this is the first run!
			//save everything (by insert instead of update)
			inventar.clear();
			quantity.clear();
			for (Pflanze pflanze: pflanzen){
				inventar.add(pflanze);
				quantity.add(0);
			}
			quantity.set(inventar.lastIndexOf(karotte), 5);
			quantity.set(inventar.lastIndexOf(zucchini), 5);
			saveGarten();
			saveInventory();
			saveKunden();
			saveStats();
		}
		dbHelper.close();
		loadGarten();
		loadInventory();
		loadKunden();
		loadStats();
		//saveStats();
	}
	
	public void newGame(){
		GartenDBOpener dbHelper = new GartenDBOpener(context);
		dbHelper.close();
		//save everything (by insert instead of update)
		inventar.clear();
		quantity.clear();
		
		//Initialize the fields
		for (int i = 1; i <= 7; i++){
		   	for (int j = 1; j <= 7; j++){        		
		   		feld[i-1][j-1] = new Feld(GameBoard.boden, (int) (System.currentTimeMillis()/1000), i, j);
		   	}
		}
		kunden.clear();
		kunden.add(new Kunde());
		kunden.add(new Kunde());
		kunden.add(new Kunde());
		for (Pflanze pflanze: pflanzen){
			inventar.add(pflanze);
			quantity.add(0);
		}
		quantity.set(inventar.lastIndexOf(karotte), 5);
		quantity.set(inventar.lastIndexOf(zucchini), 5);
		werkzeuge = 0;
		taler = 10;
		saveGarten();
		saveInventory();
		saveKunden();
		saveStats();
		dbHelper.close();
		loadGarten();
		loadInventory();
		loadKunden();
		loadStats();
	}
}
