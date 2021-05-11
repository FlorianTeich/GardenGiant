package com.example.gameframework.gameframework.implementations;

import android.graphics.Bitmap;

import com.example.gameframework.gameframework.interfaces.Image;


public class AndroidImage implements Image {
    Bitmap mBitmap;
    
    public AndroidImage(Bitmap bitmap) {
        this.mBitmap = bitmap;
    }

    @Override
    public int getWidth() {
        return mBitmap.getWidth();
    }

    @Override
    public int getHeight() {
        return mBitmap.getHeight();
    }
    
    public Image getSubImage(int x, int y, int width, int height) {
		return new AndroidImage(Bitmap.createBitmap(
				mBitmap, x, y, width, height));
	}

    @Override
    public void dispose() {
        mBitmap.recycle();
    }      
}
