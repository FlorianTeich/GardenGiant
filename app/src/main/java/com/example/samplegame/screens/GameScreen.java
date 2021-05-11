package com.example.samplegame.screens;

import java.text.SimpleDateFormat;
import java.util.List;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.gameframework.gameframework.implementations.AndroidGame;
import com.example.gameframework.gameframework.implementations.Screen;
import com.example.gameframework.gameframework.interfaces.Graphics;
import com.example.gameframework.gameframework.interfaces.Input.TouchEvent;
import com.example.samplegame.Assets;
import com.example.samplegame.GameBoard;

/**
 * The GameScreen Main Class that handles all input and output
 * @author flori_000
 */
public class GameScreen extends Screen {
	enum GameState {
        Aufgaben, Garten, Einstellungen
    }
	private static GameState    state = GameState.Garten;
	private Paint 				mPaint;
	private Paint 				nPaint;
	private GameBoard gameBoard;
	private int selectedItem 	= 0;
	private int selectedX 		= 1;
	private int selectedY 		= 1;
	private int inventoryIndex 	= 0;
	private boolean dragEnabled = false;
	private int contextMenu 	= -1;
	
	/**
	 * The GameScreen Main Class constructor
	 * @param game	The given game object
	 */
	public GameScreen(AndroidGame game) {
		super(game);	
		
		//Define game objects
		gameBoard = new GameBoard(game);
		
        //Defining a paint object
        mPaint = new Paint();
        mPaint.setTextSize(30);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.WHITE);
        
        nPaint = new Paint();
        nPaint.setTextSize(30);
        nPaint.setTextAlign(Paint.Align.CENTER);
        nPaint.setAntiAlias(true);
        nPaint.setColor(Color.BLACK);
	}
	
	/**
	 * @see com.example.gameframework.gameframework.implementations.Screen#update(float)
	 */
	@Override
	public void update(float deltaTime) {
		List<TouchEvent> touchEvents = mGame.getInput().getTouchEvents();
		switch (state) {
			case Aufgaben:
				updateAufgaben(touchEvents);
				break;
			case Garten:
				updateGarten(touchEvents, deltaTime);
				break;
			case Einstellungen:
				updateSettings(touchEvents);
				break;
			default:
				throw new IllegalStateException("Unknwon game state " + state);
		}
	}

	/**
	 * Input logic of Settings
	 * @param touchEvents	List of touch events
	 */
	private void updateSettings(List<TouchEvent> touchEvents) {
		for (int i = 0; i < touchEvents.size(); i++) {
            TouchEvent event = touchEvents.get(i);
            switch (event.type) {
            	case TouchEvent.TOUCH_UP:
            		//Back was pressed
            		if (event.x <= 750 && event.x >= 50 && event.y <= 1230 && event.y >= 1130) {
            			state = GameState.Garten;
            		}
            		//Toggle Notification
            		if (event.x <= 750 && event.x >= 50 && event.y <= 450 && event.y >= 350) {
            			GameBoard.notificationsOn = !GameBoard.notificationsOn;
            			GameBoard.saveStats();
            		}
            		//New Game was pressed
            		if (event.x <= 750 && event.x >= 50 && event.y <= 1080 && event.y >= 980) {
            			gameBoard.newGame();
            			state = GameState.Garten;
            		}
            }
		}
	}

	/**
	 * Input logic of Aufgaben
	 * @param touchEvents	List of TouchEvents
	 */
	private void updateAufgaben(List<TouchEvent> touchEvents) {
		for (int i = 0; i < touchEvents.size(); i++) {
            TouchEvent event = touchEvents.get(i);
            switch (event.type) {
            	case TouchEvent.TOUCH_UP:
            		if (event.x <= 750 && event.x >= 50 && event.y <= 1230 && event.y >= 1130) {
            			state = GameState.Garten;
            		}else if (event.x <= 700 && event.x >= 100 && event.y <= 325 && event.y >= 250) {
            			gameBoard.deal(0);
            		}else if (event.x <= 700 && event.x >= 100 && event.y <= 675 && event.y >= 600) {
            			gameBoard.deal(1);
            		}else if (event.x <= 700 && event.x >= 100 && event.y <= 1025 && event.y >= 950) {
            			gameBoard.deal(2);
            		}
                    break;
            }
		}
	}
	
	
	/**
	 * Helper method for converting a time int into a formated string of the structure HH:mm:ss
	 * @param time	integer that should be converted
	 * @return		converted time
	 */
	@SuppressLint("SimpleDateFormat")
	public String formatTime(int time){
		SimpleDateFormat df = new java.text.SimpleDateFormat("HH:mm:ss");
		df.setTimeZone(java.util.TimeZone.getTimeZone("GMT"));
		return df.format(time * 1000);
	}

	/**
	 * Input logic of the Garten
	 * @param touchEvents	List of TouchEvents
	 * @param deltaTime		Time that passed since the last call of this method
	 */
	private void updateGarten(List<TouchEvent> touchEvents, float deltaTime) {
		//Handle input events
		for (int i = 0; i < touchEvents.size(); i++) {
            TouchEvent event = touchEvents.get(i);
            switch (event.type) {
            	case TouchEvent.TOUCH_UP:
            		
            		//If field was selected, user dragged something from the inventory to here
                    if (event.x <= 750 && event.x >= 50 && event.y <= 750 && event.y >= 50) {
                        //Plant the dropped item
                        if (dragEnabled){
                        	gameBoard.pflanze(selectedItem,(event.x - 50)/100, (event.y - 50)/100);
                        }else{
                        	if (selectedX == (event.x - 50)/100+1 && selectedY == (event.y - 50)/100+1)
                        		gameBoard.ernte(selectedX - 1, selectedY - 1);
                        }
                        selectedX = (event.x - 50)/100+1;
                        selectedY = (event.y - 50)/100+1;
                    }else 
                    	
                    //User swiped in a certain direction
                    if (event.x >= 175 && event.x <= Math.min(675, 175 + GameBoard.inventar.size()*100) && event.y >= 950 && event.y <= 1050){
                        
                    	//Get direction
                    	int diff = (((event.x -175)/100) + inventoryIndex) - selectedItem;
                    	if (diff == 0){
                    		//open contextMenu
                    		contextMenu = selectedItem;
                    	}else{
                    		inventoryIndex = Math.max(0,Math.min(inventoryIndex - diff, GameBoard.inventar.size() - 5));
                    	}
                    }else 
                    
                    //Inventorys left navigation button was pressed
                    if(event.x >= 75 && event.x <= 150 && event.y <= 1055 && event.y >= 945){
                    	if (inventoryIndex > 0){
                    		inventoryIndex -= 1;
                    	}
                    }else 
                    
                    //Inventorys right navigation button was pressed
                    if(event.x >= 650 && event.x <= 725 && event.y <= 1055 && event.y >= 945){
                    	if (inventoryIndex + 5 < GameBoard.inventar.size()){
                    		inventoryIndex += 1;
                    	}
                    }else 
                    	
                    //Customer button was clicked
                    if (event.x >= 600 && event.x <= 700 && event.y <= 1250 && event.y >= 1125) {
                    	state = GameState.Aufgaben;
                    }
                    
                  //Settings button was clicked
                    if (event.x >= 500 && event.x <= 600 && event.y <= 1250 && event.y >= 1125) {
                    	state = GameState.Einstellungen;
                    }
                    
                    //Reset the drag flag
                    dragEnabled = false;
                    break;
                    
            	case TouchEvent.TOUCH_DOWN:
            		
            		//This can either mean:
            		//	- user wants to swipe through inventory
            		//	- user wants to drag n drop a seed to the field            		
            		if (event.x >= 175 && event.x <= Math.min(675, 175 + GameBoard.inventar.size()*100) && event.y >= 950 && event.y <= 1050){
            			selectedItem = (((event.x -175)/100) + inventoryIndex);
            			dragEnabled = true;
            		}else 
            		
            		//User clicked on buy option for seeds
            		if (contextMenu != -1 && event.x >= 425 && event.x <= 675 && event.y >= 810 && event.y <= 885){
            			gameBoard.kaufePflanze(selectedItem);
            		}else if (contextMenu != -1 && event.x >= 125 && event.x <= 675 && event.y >= 810 && event.y <= 885){
            			gameBoard.verkaufePflanze(selectedItem);
            		}else{
            			selectedItem = 0;
            			contextMenu = -1;
            		}
            		break;
            }
        }
	}
	
	/**
	 * @see com.example.gameframework.gameframework.implementations.Screen#paint(float)
	 */
	@Override
	public void paint(float deltaTime) {
        Graphics g = mGame.getGraphics();
        
        //Clear the screen
        g.drawARGB(255, 0, 0, 0);
        g.drawImage(Assets.wood, 0, 0);
        
        //Draw UI above the game objects
        switch (state) {
	    case Aufgaben:
	       	drawAufgabenUI();
	       	break;
	    case Garten:
	      	drawGartenUI();
	       	break;
	    case Einstellungen:
	       	drawSettingsUI();
	       	break;
		default:
			break;
        }
	}

	/**
	 * Draws the Garten screen
	 */
	private void drawGartenUI() {
		Graphics g = mGame.getGraphics();
		 
        //Draw the plants on the fields
        for (int i = 1; i <= 7; i++){
        	for (int j = 1; j <= 7; j++) {
				if (gameBoard.feld[i - 1][j - 1].getPflanzZeitpunkt() + gameBoard.feld[i - 1][j - 1].getBewachsenVon().wachstumsDauer <= System.currentTimeMillis() / 1000 && !gameBoard.feld[i - 1][j - 1].getBewachsenVon().name.equals("Boden")){
					g.drawScaledImage(Assets.soil, (i - 1) * 100 + 50, (j - 1) * 100 + 50, 100, 100, 0, 0, 512, 512);
					g.drawScaledImage(gameBoard.getFeld(i, j).getBewachsenVon().image, (i - 1) * 100 + 50, (j - 1) * 100 + 50, 100, 100, 0, 0, 512, 512);
				}
        		else if(gameBoard.feld[i-1][j-1].getBewachsenVon().name.equals("Boden"))
        			//g.drawImage(Assets.soil, (i-1)*100+50, (j-1)*100+50);
					g.drawScaledImage(Assets.soil, (i-1)*100+50, (j-1)*100+50, 100, 100, 0, 0, 512, 512);
        		else{
					g.drawScaledImage(Assets.soil, (i-1)*100+50, (j-1)*100+50, 100, 100, 0, 0, 512, 512);
					g.drawScaledImage(Assets.keimling, (i-1)*100+50, (j-1)*100+50, 100, 100, 0, 0, 512, 512);
				}
        	}
        }

        //drawScaledImage(Image Image, int x, int y, int width, int height, int srcX, int srcY, int  srcWidth, int srcHeight)

        //Draw the field borders
        g.drawLine(50,  50, 750,  50, Color.WHITE);
        g.drawLine(50, 150, 750, 150, Color.WHITE);
        g.drawLine(50, 250, 750, 250, Color.WHITE);
        g.drawLine(50, 350, 750, 350, Color.WHITE);
        g.drawLine(50, 450, 750, 450, Color.WHITE);
        g.drawLine(50, 550, 750, 550, Color.WHITE);
        g.drawLine(50, 650, 750, 650, Color.WHITE);
        g.drawLine(50, 750, 750, 750, Color.WHITE);        
        g.drawLine(50,  50,  50, 750, Color.WHITE);
        g.drawLine(150, 50, 150, 750, Color.WHITE);
        g.drawLine(250, 50, 250, 750, Color.WHITE);
        g.drawLine(350, 50, 350, 750, Color.WHITE);
        g.drawLine(450, 50, 450, 750, Color.WHITE);
        g.drawLine(550, 50, 550, 750, Color.WHITE);
        g.drawLine(650, 50, 650, 750, Color.WHITE);
        g.drawLine(750, 50, 750, 750, Color.WHITE);
        
        //Draw frame of selected field
        int x = selectedX*100;
        int y = selectedY*100;
        g.drawRect(x-50, y-50, 100, 5, Color.RED);
        g.drawRect(x-50, y+47, 100, 5, Color.RED);
        g.drawRect(x+47, y-50, 5, 100, Color.RED);
        g.drawRect(x-50, y-50, 5, 100, Color.RED);
                
        //Draw ContextMenu
        g.drawImage(Assets.contextMenu, 50, 800);
        
        //Draw taskbar providing information about the current amount of money and tools
        //and containing buttons for the settings menu and the customer screen
        g.drawScaledImage(Assets.contextMenu, 50, 1125, 700, 110, 0, 0, Assets.contextMenu.getWidth(), Assets.contextMenu.getHeight());
        g.drawScaledImage(Assets.taler, 100, 1140, 80,80,0,0, Assets.taler.getWidth(), Assets.taler.getHeight());
        g.drawRect(505, 1135, 90, 90, Color.DKGRAY);
        g.drawRect(605, 1135, 90, 90, Color.DKGRAY);
        g.drawString(String.valueOf(GameBoard.taler), 250, 1190, mPaint);
        g.drawScaledImage(Assets.toolKanne, 300, 1130,100,100,0,0,512,512);
        g.drawString(String.valueOf(GameBoard.werkzeuge), 450, 1190, mPaint);        
        g.drawScaledImage(Assets.monster01, 610, 1140, 80,80, 0,0, Assets.monster01.getWidth(), Assets.monster01.getHeight());        
        g.drawScaledImage(Assets.settings, 510, 1140, 80, 80, 0,0,  Assets.settings.getWidth(), Assets.settings.getHeight());
        
        //Detailed field information
        if (!gameBoard.getFeld(selectedX, selectedY).getBewachsenVon().name.equals("Boden")){
        	//Display name
        	g.drawString(gameBoard.getFeld(selectedX, selectedY).getBewachsenVon().name, 200, 850, mPaint);
        	//Display time progress as text
        	g.drawString(formatTime(Math.min((int) (System.currentTimeMillis()/1000) - gameBoard.getFeld(selectedX, selectedY).getPflanzZeitpunkt(),gameBoard.getFeld(selectedX, selectedY).getBewachsenVon().wachstumsDauer)) + "/" + formatTime(gameBoard.getFeld(selectedX, selectedY).getBewachsenVon().wachstumsDauer), 550, 850, mPaint);
        
        	//Draw progress bar
        	int currTime = (int) (System.currentTimeMillis()/1000);
        	int duration = gameBoard.getFeld(selectedX, selectedY).getBewachsenVon().wachstumsDauer;
        	int plantTime = gameBoard.getFeld(selectedX, selectedY).getPflanzZeitpunkt();            
            g.drawRect(100, 875, 600, 20, Color.WHITE);
            g.drawRect(100, 875, (int) (600 * ((float) Math.min(((currTime - plantTime)/(float) duration), 1.0f))), 20, Color.RED);
        }
        
        //Display inventory
        g.drawRect(75, 945, 650, 110, Color.DKGRAY);
        for (int i = 0; i < Math.min(GameBoard.inventar.size(),5); i++){
        	int index = inventoryIndex+i;
        	//Display thumbnail of the plant
        	g.drawScaledImage(GameBoard.inventar.get(index).inventoryImage, 150 + i*100, 950, 100, 100, 0, 0, 512, 512);
        	if (GameBoard.quantity.get(index) == 0)
        		g.drawImage(Assets.disabled, 150 + i*100, 950);
        	//Amount indicator
        	g.drawRect(150 + i*100, 950, 40, 40, Color.DKGRAY);
        	g.drawString(String.valueOf(GameBoard.quantity.get(index)), 170 + i*100, 980, mPaint);
        }        
        g.drawLine(250, 950, 250, 1050, Color.DKGRAY);
        g.drawLine(350, 950, 350, 1050, Color.DKGRAY);
        g.drawLine(450, 950, 450, 1050, Color.DKGRAY);
        g.drawLine(550, 950, 550, 1050, Color.DKGRAY);
        g.drawImage(Assets.arrowLeft, 90, 950);
        g.drawImage(Assets.arrowRight, 660, 950);
        
        //The context bubble for plants selected from the inventory
        if (contextMenu != -1){
        	g.drawImage(Assets.dialogBox, 100, 550);
        	g.drawString("Name: " + GameBoard.inventar.get(selectedItem).name, 400, 600, nPaint);
        	g.drawString("Kosten: " + String.valueOf(GameBoard.inventar.get(selectedItem).kosten) + " Taler", 400, 650, nPaint);
        	g.drawString("Anforderung: " + String.valueOf(GameBoard.inventar.get(selectedItem).tier) + " Werkzeuge", 400, 700, nPaint);
        	g.drawString("Wachstumsdauer: " + formatTime(GameBoard.inventar.get(selectedItem).wachstumsDauer), 400, 750, nPaint);
        	g.drawString("Samen nach der Ernte: " + String.valueOf(GameBoard.inventar.get(selectedItem).erntenMultiplikator), 400, 800, nPaint);
        	g.drawRect(425, 810, 250, 75, Color.GRAY);
        	g.drawRect(125, 810, 250, 75, Color.GRAY);
        	g.drawString("1x Kaufen", 550, 860, mPaint);
        	g.drawString("10:1 Verkaufen", 250, 860, mPaint);
        	g.drawImage(Assets.dialogTriangle, (selectedItem-inventoryIndex)*100 + 150, 900);
        }
	}

	/**
	 * Draw Settings screen
	 */
	private void drawSettingsUI() {
		Graphics g = mGame.getGraphics();
		g.drawImage(Assets.garden, 0, 0);
		g.drawScaledImage(Assets.contextMenu, 50, 1130, 700, 100, 0, 0, Assets.contextMenu.getWidth(), Assets.contextMenu.getHeight());
		g.drawScaledImage(Assets.contextMenu, 50, 980, 700, 100, 0, 0, Assets.contextMenu.getWidth(), Assets.contextMenu.getHeight());
		g.drawScaledImage(Assets.contextMenu, 50, 50, 700, 880, 0, 0, Assets.contextMenu.getWidth(), Assets.contextMenu.getHeight());
	    
		//Draw button for NEW GAME
		g.drawString("Neues Spiel", 400, 1050, mPaint);
  
		//Draw button for BACK
		g.drawString("Zurueck", 400, 1200, mPaint);
	       
		//Draw Notification Button 
		g.drawRect(75, 350, 650, 100, Color.GRAY);
		g.drawString("Notifications: " + String.valueOf(GameBoard.notificationsOn), 400, 415, mPaint);
	       
	}

	/**
	 * Draws Aufgaben screen
	 */
	private void drawAufgabenUI() {
		Graphics g = mGame.getGraphics();
		g.drawImage(Assets.stones, 0, 0);
		g.drawImage(Assets.contextMenu, 50, 50);
		g.drawImage(Assets.contextMenu, 50, 400);
		g.drawImage(Assets.contextMenu, 50, 750);
		//Draw the three current missions
		for (int i = 0; i < 3; i++){
			g.drawString("Belohnung: " + GameBoard.kunden.get(i).belohnungTaler() + " Taler", 400, 100 + i * 350 - 10, mPaint);
			for (int j = 0; j < GameBoard.kunden.get(i).nachfrage.size(); j++){
				g.drawScaledImage(GameBoard.kunden.get(i).nachfrage.get(j).inventoryImage, j * 100 + 100, i*350 + 100,100,100,0,0,512,512);
				if (GameBoard.quantity.get(GameBoard.inventar.indexOf(GameBoard.kunden.get(i).nachfrage.get(j))) < GameBoard.kunden.get(i).quantity.get(j)){
					g.drawImage(Assets.disabled, j * 100 + 100, i*350 + 100);
				}
				//g.drawRect(j*100+100, i*350+100, width, height, Color.DKGRAY);
				g.drawString(String.valueOf(GameBoard.quantity.get(GameBoard.inventar.indexOf(GameBoard.kunden.get(i).nachfrage.get(j)))) + "/" + String.valueOf(GameBoard.kunden.get(i).quantity.get(j)), (j+1) * 100 + 50, i*350 + 230, mPaint);
			}
			
			//Draw button for accepting a deal
			g.drawRect(100, i * 350 + 250, 600, 75, Color.DKGRAY);
			g.drawString("Handel annehmen", 400, i*350 + 300, mPaint);
			if (GameBoard.kunden.get(i).bezahlbar() == false){
				g.drawScaledImage(Assets.disabled, 100, i * 350 + 250, 600, 75, 0, 0, 100, 100);
			}
		}
		
		//Draw button to navigate back to the garden
		g.drawRect(50, 1130, 700, 100, Color.DKGRAY);
		g.drawString("Zurueck zum Garten", 400, 1190, mPaint);
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
}
