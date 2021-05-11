package com.example.gameframework.gameframework.implementations;


import com.example.gameframework.gameframework.interfaces.Image;
import com.example.gameframework.gameframework.interfaces.Movement;


public class Sprite {
	Image    mImage;
	Movement mMovement;
	
	public Sprite(Movement movement) {
		mMovement = movement;
	}
	
	public Sprite (Image image, Movement movement) {
		mImage    = image;
		mMovement = movement;
	}
	
	public void update(float deltaTime) {
		mMovement.update(deltaTime);
	}
	
	public int getX() {
		return mMovement.getX();
	}
	
	public int getY() {
		return mMovement.getY();
	}
	
	public Image getImage() {
		return mImage;
	}
	
	public Movement getMovement() {
		return mMovement;
	}
}
