package com.example.gameframework.gameframework.implementations;

import com.example.gameframework.gameframework.interfaces.Image;

public class AnimationFrame {
    private Image mImage;
    private long  mEndTime;

    public AnimationFrame(Image image, long endTime) {
    	mImage   = image;
        mEndTime = endTime;
    }
    
    public long getEndTime() {
    	return mEndTime;
    }
    
    public Image getImage() {
    	return mImage;
    }
    
    public void dispose() {
    	mImage.dispose();
    }
} 