package com.example.gameframework.gameframework.implementations;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class AndroidFastRenderView extends SurfaceView implements Runnable {
    private  AndroidGame       mGame;
    private  Bitmap            mFramebuffer;
    private  Thread            mRenderThread;
    private  SurfaceHolder     mHolder;
    volatile boolean  running = false;

    
    public AndroidFastRenderView(AndroidGame game, Bitmap framebuffer) {
        super(game);
        mGame = game;
        mFramebuffer = framebuffer;
        mHolder = getHolder();
    }

    public void resume() {
        running = true;
        mRenderThread = new Thread(this);
        mRenderThread.start();
    }

    public void run() {
        Rect dstRect   = new Rect();
        long startTime = System.nanoTime();
        while(running) {
            if(!mHolder.getSurface().isValid()) continue;

            float deltaTime = (System.nanoTime() - startTime) / 10000000.000f;
            startTime       = System.nanoTime();

            if (deltaTime > 3.15){
                deltaTime = (float) 3.15;
            }

            mGame.getCurrentScreen().update(deltaTime);
            mGame.getCurrentScreen().paint(deltaTime);

            Canvas canvas = mHolder.lockCanvas();
            canvas.getClipBounds(dstRect);
            canvas.drawBitmap(mFramebuffer, null, dstRect, null);
            mHolder.unlockCanvasAndPost(canvas);
        }
    }

    public void pause() {
        running = false;
        while(true) {
            try {
                mRenderThread.join();
                break;
            } catch (InterruptedException e) { }
        }
    }
}
