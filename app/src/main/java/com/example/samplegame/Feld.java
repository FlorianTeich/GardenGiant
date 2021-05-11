package com.example.samplegame;

public class Feld {
	private int id;
	private int x;
	private int y;
	private Pflanze bewachsenVon = null;
	private int pflanzZeitpunkt = 0;
	
	public Feld(Pflanze aPflanze, int aZeitpunkt, int aX, int aY){
		this.bewachsenVon = aPflanze;
		this.pflanzZeitpunkt = aZeitpunkt;
		this.x = aX;
		this.y = aY;
	}
	
	public Pflanze getBewachsenVon() {
		return bewachsenVon;
	}

	public void setBewachsenVon(Pflanze aBewachsenVon) {
		this.bewachsenVon = aBewachsenVon;
	}

	public int getPflanzZeitpunkt() {
		return pflanzZeitpunkt;
	}

	public void setPflanzZeitpunkt(int aPflanzZeitpunkt) {
		this.pflanzZeitpunkt = aPflanzZeitpunkt;
	}
	
	public void setPflanzZeitpunkt() {
		this.pflanzZeitpunkt = (int) (System.currentTimeMillis()/1000);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
}
