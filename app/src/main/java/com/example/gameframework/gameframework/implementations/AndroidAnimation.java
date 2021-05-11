package com.example.gameframework.gameframework.implementations;

import java.util.ArrayList;


import com.example.gameframework.gameframework.interfaces.Animation;
import com.example.gameframework.gameframework.interfaces.Image;


public class AndroidAnimation implements Animation {
	private ArrayList<AnimationFrame> mFrames;
    private int       			 	  mCurrentFrame;
    private long      			      mAnimTime;
    private boolean					  mIsLooping;
    private boolean					  mIsEnded;
    
    public AndroidAnimation() {
        mFrames = new ArrayList<AnimationFrame>();
        synchronized (this) {
            mAnimTime     = 0;
            mCurrentFrame = 0;
            mIsLooping    = true;
            mIsEnded      = false;
        }
    }
    
    public synchronized void addFrameFromImage(Image srcImage, int x, int y, int width, int height, long duration) {
    	Image dstImage = ((AndroidImage)srcImage).getSubImage(x, y, width, height);
    	addFrame(dstImage, duration);
    }

    public synchronized void addFrame(Image image, long duration) {
        mFrames.add(new AnimationFrame(image, getTotalDuration() + duration));
    }    
    
    public synchronized void addFrames(ArrayList<AnimationFrame> frames) {
    	mFrames.addAll(frames);
    }

    public synchronized void addConsistentFrames(Image image, int frameCount, int width, int height, long duration) {
    	int x = 0;
    	int y = 0;
    	for (int i=0; i < frameCount; i++) {
    		addFrameFromImage(image, x, y, width, height, duration);
    		x += width; 
    		if (x+width > image.getWidth()) {
    			x = 0;
    			y += height;
    		}
    	}
    }    
    
    public synchronized long getTotalDuration() {
    	if (mFrames.size() == 0) return 0;
    	
    	return mFrames.get(mFrames.size() - 1).getEndTime();
    }    
    
    public synchronized void update(float elapsedTime) {
        if (mFrames.size() < 2) return;

        mAnimTime += elapsedTime;
        if (mAnimTime >= getTotalDuration()) {
        	if (mIsLooping) {
        		//start animation again, if looping is activated
        		mAnimTime     = mAnimTime % getTotalDuration();
        		mCurrentFrame = 0;
        	} else {
        		//animation ended
        		mIsEnded = true;
        	}
        }
        
        if (!mIsEnded) {
        	//if animation not ended, find next frame for current time
        	while (mAnimTime > getFrame(mCurrentFrame).getEndTime()) {
        		mCurrentFrame++;
        	}
        }
    }

    
    public synchronized boolean isLooping() {
    	return mIsLooping;
    }
    
    
    public synchronized void setLooping(boolean isLooping) {
    	mIsLooping = isLooping;
    }
    
    public synchronized Image getCurrentFrame() {
    	if (mFrames.size() == 0) return null;
    	
    	return getFrame(mCurrentFrame).getImage();
    }

    private AnimationFrame getFrame(int i) {
        return (AnimationFrame) mFrames.get(i);
    }

	@Override
	public void dispose() {
		for (AnimationFrame frame : mFrames) frame.dispose();
	}
}	

