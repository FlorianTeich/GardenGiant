package com.example.gameframework.gameframework.implementations;

import com.example.gameframework.gameframework.interfaces.Movement;

public class LinearMovement implements Movement {
	private int   mX;
	private int   mY;
	private float mSpeedX;
	private float mSpeedY;
	
	public LinearMovement(int x, int y) {
		mX = x;
		mY = y;
	}

	public LinearMovement(int x, int y, float speedX, float speedY) {
		mX      = x;
		mY      = y;
		mSpeedX = speedX;
		mSpeedY = speedY;
	}
	
	
	@Override
	public void setSpeedX(float speedX) {
		mSpeedX = speedX;
	}
	
	@Override
	public void setSpeedY(float speedY) {
		mSpeedY = speedY;
	}
	
	@Override
	public float getSpeedX() {
		return mSpeedX;
	}
	
	@Override
	public float getSpeedY() {
		return mSpeedY;
	}

	@Override
	public void setX(int x) {
		mX = x;
	}

	@Override
	public void setY(int y) {
		mY = y;
	}

	@Override
	public void setXY(int x, int y) {
		mX = x;
		mY = y;
	}

	@Override
	public int getX() {
		return mX;
	}

	@Override
	public int getY() {
		return mY;
	}

	@Override
	public void update(float elapsedTime) {
		mX += mSpeedX * elapsedTime;
		mY += mSpeedY * elapsedTime;
	}
	
	public String toString() {
		return "(" + mX + ", " + mY + ") with speed [" + mSpeedX + ", " + mSpeedY + "]";
	}
}
