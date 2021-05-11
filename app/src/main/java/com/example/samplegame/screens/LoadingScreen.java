package com.example.samplegame.screens;

import com.example.gameframework.gameframework.implementations.AndroidGame;
import com.example.gameframework.gameframework.implementations.Screen;
import com.example.gameframework.gameframework.interfaces.Graphics;
import com.example.samplegame.Assets;

/**
 * LoadingScreen class
 * @author flori_000
 *
 */
public class LoadingScreen extends Screen {

	/**
	 * Constructor for the LoadingScreen
	 * @param game	Given game object
	 */
	public LoadingScreen(AndroidGame game) {
		super(game);
	}

	/**
	 * @see com.example.gameframework.gameframework.implementations.Screen#update(float)
	 */
	@Override
	public void update(float deltaTime) {
        Graphics g  = mGame.getGraphics();
        
        //initialize assets       
        Assets.menu         = g.newImage("garden.png");
        Assets.soil			= g.newImage("soil.png"); 
        Assets.keimling		= g.newImage("keimling.png");
        Assets.karotte		= g.newImage("karotte.png");
        Assets.kuerbis		= g.newImage("kuerbis.png");
        Assets.kartoffel	= g.newImage("kartoffel.png");
        Assets.zucchini		= g.newImage("zucchini.png");
        Assets.paprika		= g.newImage("paprika.png");
        Assets.tomate		= g.newImage("tomate.png");        
        Assets.broccoli		= g.newImage("broccoli.png");
        Assets.zwiebel		= g.newImage("zwiebel.png");
        Assets.gurke		= g.newImage("gurke.png");
        Assets.aubergine	= g.newImage("aubergine.png");
        Assets.spargel		= g.newImage("spargel.png");
        Assets.radieschen	= g.newImage("radieschen.png");
        Assets.salat		= g.newImage("salat.png");        
        Assets.karotteInv	= g.newImage("karotte.png");
        Assets.kuerbisInv	= g.newImage("kuerbis.png");
        Assets.kartoffelInv	= g.newImage("kartoffel.png");
        Assets.zucchiniInv	= g.newImage("zucchini.png");
        Assets.paprikaInv	= g.newImage("paprika.png");
        Assets.tomateInv	= g.newImage("tomate.png");
        Assets.broccoliInv	= g.newImage("broccoli.png");
        Assets.zwiebelInv	= g.newImage("zwiebel.png");
        Assets.gurkeInv		= g.newImage("gurke.png");
        Assets.aubergineInv	= g.newImage("aubergine.png");
        Assets.spargelInv	= g.newImage("spargel.png");
        Assets.radieschenInv= g.newImage("radieschen.png");
        Assets.salatInv		= g.newImage("salat.png");
        Assets.toolKanne    = g.newImage("tools.png");
        Assets.monster01	= g.newImage("kunde.png");
        Assets.arrowLeft	= g.newImage("arrowLeft.png");
        Assets.arrowRight	= g.newImage("arrowRight.png");
        Assets.wood			= g.newImage("wood.png");
        Assets.stones		= g.newImage("stones.png");
        Assets.garden		= g.newImage("garden.png");
        Assets.disabled		= g.newImage("disabled.png");
        Assets.dialogBox	= g.newImage("dialogBox.png");
        Assets.dialogTriangle	
        					= g.newImage("dialogTriangle.png");
        Assets.background	= g.newImage("garden.png");
        Assets.contextMenu	= g.newImage("contextMenu.png");        
        Assets.taler		= g.newImage("taler.png");
        Assets.settings		= g.newImage("settings.png");    
        mGame.setScreen(new GameScreen(mGame));
	}

	/**
	 * @see com.example.gameframework.gameframework.implementations.Screen#paint(float)
	 */
	@Override
	public void paint(float deltaTime) {
		
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
