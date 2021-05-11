package com.example.samplegame.screens;

import java.util.List;

import com.example.gameframework.gameframework.implementations.AndroidGame;
import com.example.gameframework.gameframework.implementations.Screen;
import com.example.gameframework.gameframework.interfaces.Graphics;
import com.example.gameframework.gameframework.interfaces.Input.TouchEvent;
import com.example.samplegame.Assets;

/**
	 * Main menu screen class
	 * @version 1.0
	 * @author Florian Teich
 */
public class MainMenuScreen extends Screen {

	/**
	 * Constructor for the MainMenuScreen class
	 * @param game	Given Game object
	 */
	public MainMenuScreen(AndroidGame game) {
		super(game);
	}

	/**
	 * @see com.example.gameframework.gameframework.implementations.Screen#update(float)
	 */
	@Override
	public void update(float deltaTime) {
        mGame.getGraphics();
        List<TouchEvent> touchEvents = mGame.getInput().getTouchEvents();
        for (int i = 0; i < touchEvents.size(); i++) {
        	TouchEvent event = touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_UP) {
                if (inBounds(event, 0, 0, 250, 250)) {
                    //start the game
                    mGame.setScreen(new GameScreen(mGame));
                    break;
                }
            }
        }
	}

	/**
	 * @see com.example.gameframework.gameframework.implementations.Screen#paint(float)
	 */
	@Override
	public void paint(float deltaTime) {
        Graphics g = mGame.getGraphics();
        g.drawImage(Assets.menu, 0, 0);
	}

	/**
	 * @see com.example.gameframework.gameframework.implementations.Screen#pause()
	 */
	@Override
	public void pause() {

	}

	/**
	 * @see com.example.gameframework.gameframework.implementations.Screen#resume()
	 */
	@Override
	public void resume() {

	}

	/**
	 * @see com.example.gameframework.gameframework.implementations.Screen#dispose()
	 */
	@Override
	public void dispose() {

	}

	/**
	 * @see com.example.gameframework.gameframework.implementations.Screen#backButton()
	 */
	@Override
	public void backButton() {
		
	}
    
	/**
	 * Helper function to check whether a TouchEvent is 
	 * @param event		TouchEvent
	 * @param x			x coordinate of the upper left corner of the touchbox
	 * @param y			y coordinate of the upper left corner of the touchbox
	 * @param width		width of the touchbox
	 * @param height	height of the touchbox
	 * @return
	 */
	private boolean inBounds(TouchEvent event, int x, int y, int width,
            int height) {
        if (event.x > x && event.x < x + width - 1 && event.y > y
                && event.y < y + height - 1) {
            return true;
        } else {
            return false;
        }
    }
}
