package com.example.gameframework.gameframework.interfaces;

public interface Movement {
	public void  setX(int x);
	public void  setY(int y);
	public void  setXY(int x, int y);
	public int   getX();
	public int   getY();
	public void  setSpeedX(float speedX);
	public void  setSpeedY(float speedY);
	public float getSpeedX();
	public float getSpeedY();
	public void  update(float deltaTime);
}
