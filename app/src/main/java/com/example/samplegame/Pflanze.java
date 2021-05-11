package com.example.samplegame;

import com.example.gameframework.gameframework.interfaces.Image;

public class Pflanze {
	public String name = "";
	public Image image = null; 
	public Image inventoryImage = null;
	public Image growingImage = Assets.keimling; 
	public int kosten = 0;
	public int wachstumsDauer = 0;
	public int erntenMultiplikator = 0;
	public int tier = 0;
	
	public Pflanze(String aName, Image aImage, Image bImage, int aKosten, int aWachstumsDauer, int aErntenMultiplikator, int aTier){
		this.name = aName;
		this.image = aImage;
		this.inventoryImage = bImage;
		this.kosten = aKosten;
		this.wachstumsDauer = aWachstumsDauer;
		this.erntenMultiplikator = aErntenMultiplikator;
		this.tier = aTier;
	}
}

