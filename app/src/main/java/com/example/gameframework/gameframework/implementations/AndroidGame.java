package com.example.gameframework.gameframework.implementations;

import com.example.gameframework.gameframework.interfaces.Audio;
import com.example.gameframework.gameframework.interfaces.FileIO;
import com.example.gameframework.gameframework.interfaces.Game;
import com.example.gameframework.gameframework.interfaces.Graphics;
import com.example.gameframework.gameframework.interfaces.Input;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Point;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;


public abstract class AndroidGame extends Activity implements Game {
    private AndroidFastRenderView mRenderView;
    private Graphics              mGraphics;
    private Audio                 mAudio;
    private Input                 mInput;
    private FileIO                mFileIO;
    private Screen                mScreen;
    private WakeLock              mWakeLock;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //remove title and set application to fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //check if in portrait mode and set framebuffer correspondingly
        boolean isPortrait    = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        int frameBufferWidth  = isPortrait ? 800: 1280;
        int frameBufferHeight = isPortrait ? 1280: 800;
        Bitmap frameBuffer = Bitmap.createBitmap(frameBufferWidth,
                frameBufferHeight, Config.RGB_565);
        //calculate x-scale and y-scale
        Point size = new Point();
        Display display = ((WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        display.getSize(size);
        int height = size.y;
        int width = size.x;
        float scaleX = (float) frameBufferWidth
                / width;//getWidth();
        float scaleY = (float) frameBufferHeight
                / height;
        //initialise all used implementations of defined gaming interfaces
        mRenderView = new AndroidFastRenderView(this, frameBuffer);
        mGraphics   = new AndroidGraphics(getAssets(), frameBuffer);
        mFileIO     = new AndroidFileIO(this);
        mAudio      = new AndroidAudio(this);
        mInput      = new AndroidInput(this, mRenderView, scaleX, scaleY);
        mScreen     = getInitScreen();
        setContentView(mRenderView);
      //prevent device from going into sleep
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "MyGame");
    }
    
    public Context getContext(){
		return getApplicationContext();
    }

    @Override
    public void onResume() {
        super.onResume();
        mWakeLock.acquire();
        mScreen.resume();
        mRenderView.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mWakeLock.release();
        mRenderView.pause();
        mScreen.pause();
        if (isFinishing()) mScreen.dispose();
    }
    
    @Override
    public Input getInput() { return mInput; }

    @Override
    public FileIO getFileIO() { return mFileIO; }

    @Override
    public Graphics getGraphics() { return mGraphics; }

    @Override
    public Audio getAudio() { return mAudio; }

    @Override
    public void setScreen(Screen screen) {
        if (screen == null)
            throw new IllegalArgumentException("Screen must not be null");

        mScreen.pause();
        mScreen.dispose();
        screen.resume();
        screen.update(0);
        mScreen = screen;
    }

    public Screen getCurrentScreen() { return mScreen; }
}	    

