package com.example.samplegame;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Helper class for accessing the underlying SQLite database
 * @author flori_000
 */
public class GartenDBOpener extends SQLiteOpenHelper {
	
	//Tabellen:
	//	- Inventar (ID, Pflanze, Anzahl)
	//	- Garten (ID, Pflanze, Zeit, X, Y)
	//  - Kunden (ID, Kunde, Pflanze, Anzahl)
	//  - Stats (StatName, StatValue) (Taler, Werkzeuge, Settings...)
	
	private static final String DATABASE_NAME = "database.db";
	private static final String TABLE_INVENTAR = "inventar";
	private static final String TABLE_GARTEN = "garten";
	private static final String TABLE_STATS = "stats";
	private static final String TABLE_KUNDEN = "kunden";	
	private static final int DATABASE_VERSION = 25;
	public static final String COLUMN_ID = "_id";	
	public static final String COLUMN_STAT = "stat";
	public static final String COLUMN_VALUE = "wert";
	public static final String COLUMN_TALER = "taler";
	public static final String COLUMN_WERKZEUG = "werkzeug";
	public static final String COLUMN_ZEIT = "zeit";
	public static final String COLUMN_KUNDE = "kunde";
	public static final String COLUMN_PFLANZE = "pflanze";
	public static final String COLUMN_NOTIFICATION = "notification";	
	public static final String COLUMN_XCOORD = "x";
	public static final String COLUMN_YCOORD = "y";
	public static final String COLUMN_ANZAHL = "anzahl";
	
	public String [] ALL_COLUMNS_INVENTAR = {COLUMN_ID, COLUMN_PFLANZE, COLUMN_ANZAHL};
	
	public String [] ALL_COLUMNS_STATS = {COLUMN_ID, COLUMN_STAT, COLUMN_VALUE};
	
	public String [] ALL_COLUMNS_KUNDEN = {COLUMN_ID, COLUMN_KUNDE, COLUMN_PFLANZE, COLUMN_ANZAHL};
	
	public String [] ALL_COLUMNS_GARTEN = {COLUMN_ID, COLUMN_PFLANZE, COLUMN_ZEIT, COLUMN_XCOORD, COLUMN_YCOORD };
	
	private static final String CREATE_TABLE_GARTEN = "CREATE TABLE " + TABLE_GARTEN + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			COLUMN_PFLANZE + " TEXT NOT NULL, " + COLUMN_ZEIT + " INTEGER, " + COLUMN_XCOORD + " INTEGER, " + COLUMN_YCOORD + " INTEGER );";
	
	private static final String CREATE_TABLE_INVENTAR = "CREATE TABLE " + TABLE_INVENTAR + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			COLUMN_PFLANZE + " TEXT NOT NULL, " + COLUMN_ANZAHL + " INTEGER);";
	
	private static final String CREATE_TABLE_KUNDEN = "CREATE TABLE " + TABLE_KUNDEN + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			COLUMN_KUNDE + " INTEGER, " + COLUMN_PFLANZE + " TEXT NOT NULL, " + COLUMN_ANZAHL + " INTEGER);";
	
	private static final String CREATE_TABLE_STATS = "CREATE TABLE " + TABLE_STATS + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			COLUMN_STAT + " TEXT, " + COLUMN_VALUE + " INTEGER);";
		
	private static final String DROP_TABLE_GARTEN = " DROP TABLE IF EXISTS " + TABLE_GARTEN + ";";
	private static final String DROP_TABLE_INVENTAR = " DROP TABLE IF EXISTS " + TABLE_INVENTAR + ";";
	private static final String DROP_TABLE_KUNDEN = " DROP TABLE IF EXISTS " + TABLE_KUNDEN + ";";
	private static final String DROP_TABLE_STATS = " DROP TABLE IF EXISTS " + TABLE_STATS + ";";
	
	/**
	 * Constructor for the SQLite access helper object
	 * @param context	the current context
	 */
	public GartenDBOpener(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	/**
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db){
		db.execSQL(CREATE_TABLE_GARTEN);
		db.execSQL(CREATE_TABLE_INVENTAR);
		db.execSQL(CREATE_TABLE_STATS);
		db.execSQL(CREATE_TABLE_KUNDEN);
	}
	
	/**
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(DROP_TABLE_GARTEN);
		db.execSQL(DROP_TABLE_INVENTAR);
		db.execSQL(DROP_TABLE_STATS);
		db.execSQL(DROP_TABLE_KUNDEN);
		db.execSQL(CREATE_TABLE_GARTEN);
		db.execSQL(CREATE_TABLE_INVENTAR);
		db.execSQL(CREATE_TABLE_STATS);
		db.execSQL(CREATE_TABLE_KUNDEN);
	}
	
	/**
	 * Recreate fresh TABLE_STATS
	 */
	public void deleteStats(){
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(DROP_TABLE_STATS);
		db.execSQL(CREATE_TABLE_STATS);
		db.close();
	}
	
	/**
	 * Recreate fresh TABLE_KUNDEN
	 */
	public void deleteKunden(){
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(DROP_TABLE_KUNDEN);
		db.execSQL(CREATE_TABLE_KUNDEN);
		db.close();
	}
	
	/**
	 * Checks if this is the first time, the application is started
	 * @return	true, if the application was not run before, false else
	 */
	public int firstRun(){
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.query(TABLE_STATS, ALL_COLUMNS_STATS, COLUMN_STAT +"=?",
				new String [] {"firstRun"}, null, null, null, null);
		int firstRunFlag = 0;
		while(cursor.moveToNext()){
			firstRunFlag = cursor.getInt(cursor.getColumnIndex(COLUMN_VALUE));
		}
		cursor.close();
		db.close();
		return firstRunFlag;
	}
	
	/**
	 * Saves several general information
	 */
	public void saveStats(){
		insertStats(COLUMN_TALER, GameBoard.taler);
		insertStats(COLUMN_WERKZEUG, GameBoard.werkzeuge);
		int myInt = (GameBoard.notificationsOn) ? 1 : 0;
		insertStats(COLUMN_NOTIFICATION, myInt);
		insertStats("firstRun", 1);
	}
	
	/**
	 * Loads several general information from the database
	 */
	public void loadStats(){
		ArrayList<String> stats = new ArrayList<String>();
		ArrayList<Integer> val = new ArrayList<Integer>();
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.query(TABLE_STATS, ALL_COLUMNS_STATS, null, null, null, null, null);
		while(cursor.moveToNext()){
			stats.add(cursor.getString(cursor.getColumnIndex(COLUMN_STAT)));
			val.add(cursor.getInt(cursor.getColumnIndex(COLUMN_VALUE)));
		}
		cursor.close();
		for (int i = 0; i < stats.size(); i++){		
			if (stats.get(i).equals(COLUMN_TALER)){
				GameBoard.taler = val.get(i);
			}else if (stats.get(i).equals(COLUMN_WERKZEUG)){
				GameBoard.werkzeuge = val.get(i);
			}else if (stats.get(i).equals(COLUMN_NOTIFICATION)){
				if (val.get(i) == 0)
					GameBoard.notificationsOn = false;
				else
					GameBoard.notificationsOn = true;
			}
		}
		db.close();
	}
	
	/**
	 * Inserts the specified attribute into the database
	 * @param stat	attribute
	 * @param val	attributes current value
	 */
	public void insertStats(String stat, int val){
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(COLUMN_STAT, stat);
		values.put(COLUMN_VALUE, val);
		db.insert(TABLE_STATS, null, values);
		db.close();
	}
	
	/**
	 * Loads all Kunden from the database
	 */
	public void loadAllKundenItems(){
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.query(TABLE_KUNDEN, ALL_COLUMNS_KUNDEN, null, null, null, null, null);
		GameBoard.kunden.clear();
		GameBoard.kunden.add(new Kunde());
		GameBoard.kunden.add(new Kunde());
		GameBoard.kunden.add(new Kunde());
		GameBoard.kunden.get(0).nachfrage.clear();
		GameBoard.kunden.get(1).nachfrage.clear();
		GameBoard.kunden.get(2).nachfrage.clear();
		GameBoard.kunden.get(0).quantity.clear();
		GameBoard.kunden.get(1).quantity.clear();
		GameBoard.kunden.get(2).quantity.clear();
		while(cursor.moveToNext()){
			if (cursor.getInt(cursor.getColumnIndex(COLUMN_KUNDE)) < 3){
			GameBoard.kunden.get(cursor.getInt(cursor.getColumnIndex(COLUMN_KUNDE))).nachfrage.add(GameBoard.getPflanzeByName(cursor.getString(cursor.getColumnIndex(COLUMN_PFLANZE))));
			GameBoard.kunden.get(cursor.getInt(cursor.getColumnIndex(COLUMN_KUNDE))).quantity.add(cursor.getInt(cursor.getColumnIndex(COLUMN_ANZAHL)));
			}
		}
		cursor.close();
		db.close();
	}
	
	/**
	 * Inserts a single item of a Kunde into the database
	 * @param aKunde		items Kunde
	 * @param aPflanze		what item to store
	 * @param aAnzahl		amount of this particular item
	 */
	public void insertKundenItem(int aKunde, Pflanze aPflanze, int aAnzahl){
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(COLUMN_KUNDE, aKunde);
		values.put(COLUMN_PFLANZE, aPflanze.name);
		values.put(COLUMN_ANZAHL, aAnzahl);
		db.insert(TABLE_KUNDEN, null, values);
		db.close();
	}
	
	/**
	 * Inserts a single item into the inventory database
	 * @param pflanze	the item
	 * @param anzahl	the amount of this particular item
	 */
	public void insertItem(Pflanze pflanze, int anzahl) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(COLUMN_PFLANZE, pflanze.name);
		values.put(COLUMN_ANZAHL, anzahl);
		db.insert(TABLE_INVENTAR, null, values);
		db.close();
	}
	
	/**
	 * Update an item in the inventory
	 * @param pflanze	item
	 * @param anzahl	amount of this particular item
	 * @return
	 */
	public int updateItem(Pflanze pflanze, int anzahl){
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(COLUMN_ANZAHL, anzahl);
		return db.update(TABLE_INVENTAR, values, COLUMN_PFLANZE + " =?", new String[]{pflanze.name});
	}
	
	/**
	 * Get the quantity of an item in the inventory
	 * @param pflanze	the item in inventory
	 * @return
	 */
	public int getItemQantity(Pflanze pflanze){
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.query(TABLE_INVENTAR, ALL_COLUMNS_INVENTAR, COLUMN_PFLANZE +"=?",
				new String [] {pflanze.name}, null, null, null, null);
		if (cursor.moveToFirst()){			
				return cursor.getInt(cursor.getColumnIndex(COLUMN_ANZAHL));
		}
		cursor.close();
		return 0;
	}
	
	/**
	 * Get a list of all quantities of the items in the inventory
	 * @return	list of quantities
	 */
	public ArrayList<Integer> getAllItemQuantities(){
		ArrayList<Integer> quantityList = new ArrayList<Integer>();
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.query(TABLE_INVENTAR, ALL_COLUMNS_INVENTAR, null, null, null, null, null);
		while(cursor.moveToNext()){
			quantityList.add(
					cursor.getInt(cursor.getColumnIndex(COLUMN_ANZAHL)));
		}
		cursor.close();
		return quantityList;
	}
	
	/**
	 * Insert a given feld
	 * @param feld	the feld that is to be stored into the database
	 */
	public void insertFeld (Feld feld) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(COLUMN_PFLANZE, feld.getBewachsenVon().name);
		values.put(COLUMN_ZEIT, feld.getPflanzZeitpunkt());
		values.put(COLUMN_XCOORD, feld.getX());
		values.put(COLUMN_YCOORD, feld.getY());
		db.insert(TABLE_GARTEN, null, values);
		db.close();
	}
	
	/**
	 * Update the SQLite entry of a certain feld
	 * @param feld		feld that should be updated in the database
	 * @return	
	 */
	public int updateFeld(Feld feld){
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(COLUMN_PFLANZE, feld.getBewachsenVon().name);
		values.put(COLUMN_ZEIT, feld.getPflanzZeitpunkt());
		values.put(COLUMN_XCOORD, feld.getX());
		values.put(COLUMN_YCOORD, feld.getY());
		return db.update(TABLE_GARTEN, values, COLUMN_XCOORD + " =? AND " + COLUMN_YCOORD + " =?", new String[]{String.valueOf(feld.getX()), String.valueOf(feld.getY())});
	}
	
	/**
	 * Delete feld with a given id
	 * @param id	id of feld that should be deleted
	 */
	public void deleteFeld(int id){
		SQLiteDatabase db = getWritableDatabase();
		db.delete(TABLE_GARTEN, COLUMN_ID + " =?", new String []{String.valueOf(id)});
		db.close();
	}
	
	/**
	 * Get a given single feld of the garden 
	 * @param x		x coordinate of the feld
	 * @param y		y coordinate of the feld
	 * @return	feld
	 */
	public Feld getFeld(int x, int y){
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.query(TABLE_GARTEN, ALL_COLUMNS_GARTEN, COLUMN_XCOORD +"=? AND " + COLUMN_YCOORD + "=?",
				new String [] {String.valueOf(x), String.valueOf(y)}, null, null, null, null);
		Feld feld = null;
		if (cursor.moveToFirst()){
			feld = new Feld(
				GameBoard.getPflanzeByName(cursor.getString(cursor.getColumnIndex(COLUMN_PFLANZE))),
				cursor.getInt(cursor.getColumnIndex(COLUMN_ZEIT)),
				cursor.getInt(cursor.getColumnIndex(COLUMN_XCOORD)),
				cursor.getInt(cursor.getColumnIndex(COLUMN_YCOORD)));
		}
		cursor.close();
		return feld;
	}
	
	/**
	 * Get all felder of the garden
	 * @return ArrayList containing all felder; each as single element of the arraylist
	 */
	public ArrayList<Feld> getAllFelder(){
		ArrayList<Feld> feldList = new ArrayList<Feld>();
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.query(TABLE_GARTEN, ALL_COLUMNS_GARTEN, null, null,	null, null, null);
		while(cursor.moveToNext()){
			feldList.add(new Feld(
					GameBoard.getPflanzeByName(cursor.getString(cursor.getColumnIndex(COLUMN_PFLANZE))),
					cursor.getInt(cursor.getColumnIndex(COLUMN_ZEIT)),
					cursor.getInt(cursor.getColumnIndex(COLUMN_XCOORD)),
					cursor.getInt(cursor.getColumnIndex(COLUMN_YCOORD))));
		}
		cursor.close();
		return feldList;
	}

	/**
	 * Recreate the TABLE_GARTEN
	 */
	public void deleteGarten() {
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(DROP_TABLE_GARTEN);
		db.execSQL(CREATE_TABLE_GARTEN);
		db.close();
	}

	/**
	 * Recreate the TABLE_INVENTAR
	 */
	public void deleteInventory() {
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(DROP_TABLE_INVENTAR);
		db.execSQL(CREATE_TABLE_INVENTAR);
		db.close();		
	}
}