package com.example.gameframework.gameframework.implementations;

import com.example.gameframework.gameframework.interfaces.Animation;
import com.example.gameframework.gameframework.interfaces.Image;
import com.example.gameframework.gameframework.interfaces.Movement;

public class AnimatedSprite extends Sprite {
	private Animation mAnimation;
	
	public AnimatedSprite(Movement movement) {
		super(movement);
	}
	
	public AnimatedSprite(Animation animation, Movement movement) {
		super(movement);
		mAnimation = animation;
	}
	
	public Image getImage() {
		return mAnimation.getCurrentFrame();
	}
	
	public void update(float deltaTime) {
		super.update(deltaTime);
		mAnimation.update(deltaTime);
	}
}
