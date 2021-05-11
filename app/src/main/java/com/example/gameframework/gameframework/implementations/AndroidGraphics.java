package com.example.gameframework.gameframework.implementations;

import java.io.IOException;
import java.io.InputStream;

import com.example.gameframework.gameframework.interfaces.Graphics;
import com.example.gameframework.gameframework.interfaces.Image;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;


public class AndroidGraphics implements Graphics {
    private AssetManager mAssets;
    private Bitmap       mFrameBuffer;
    private Canvas       mCanvas;
	private Paint 		 mPaint;
    private Rect         mSrcRect = new Rect();
    private Rect         mDstRect = new Rect();

    public AndroidGraphics(AssetManager assets, Bitmap frameBuffer) {
        mAssets      = assets;
        mFrameBuffer = frameBuffer;
        mCanvas      = new Canvas(mFrameBuffer);
        mPaint       = new Paint();
    }
   
    
    @Override
    public Image newImage(String fileName) {
        InputStream in = null;
        Bitmap bitmap  = null;
        try {
            in     = mAssets.open(fileName);
            bitmap = BitmapFactory.decodeStream(in);
            if (bitmap == null) throw new RuntimeException("Error loading " + fileName);
        } catch (IOException e) {
            throw new RuntimeException("Error loading " + fileName);
        } finally {
            if (in != null) {
                try { in.close(); } catch (IOException e) { }
            }
        }
        return new AndroidImage(bitmap);
    }
    
    
    @Override
    public void clearScreen(int color) {
        mCanvas.drawRGB((color & 0xff0000) >> 16, (color & 0xff00) >> 8, (color & 0xff));
    }

    @Override
    public void drawLine(int x, int y, int x2, int y2, int color) {
        mPaint.setColor(color);
        mCanvas.drawLine(x, y, x2, y2, mPaint);
    }

    @Override
    public void drawRect(int x, int y, int width, int height, int color) {
        mPaint.setColor(color);
        mPaint.setStyle(Style.FILL);
        mCanvas.drawRect(x, y, x + width - 1, y + height - 1, mPaint);
    }
        
    
    
    public void drawImage(Image Image, int x, int y, int srcX, int srcY,
            int srcWidth, int srcHeight) {
        mSrcRect.left   = srcX;
        mSrcRect.top    = srcY;
        mSrcRect.right  = srcX + srcWidth;
        mSrcRect.bottom = srcY + srcHeight;

        mDstRect.left   = x;
        mDstRect.top    = y;
        mDstRect.right  = x + srcWidth;
        mDstRect.bottom = y + srcHeight;

        mCanvas.drawBitmap(((AndroidImage) Image).mBitmap, mSrcRect, mDstRect, null);
    }

    
    @Override
    public void drawImage(Image Image, int x, int y) {
        mCanvas.drawBitmap(((AndroidImage)Image).mBitmap, x, y, null);
    }    
    
    
    public void drawScaledImage(Image Image, int x, int y, int width, int height, int srcX, int srcY, int  srcWidth, int srcHeight){

        mSrcRect.left   = srcX;
        mSrcRect.top    = srcY;
        mSrcRect.right  = srcX + srcWidth;
        mSrcRect.bottom = srcY + srcHeight;

        mDstRect.left   = x;
        mDstRect.top    = y;
        mDstRect.right  = x + width;
        mDstRect.bottom = y + height;

        mCanvas.drawBitmap(((AndroidImage) Image).mBitmap, mSrcRect, mDstRect, null);
    }

    @Override
    public void drawString(String text, int x, int y, Paint paint) {
        mCanvas.drawText(text, x, y, paint);
    }

	@Override
	public void drawBitmapFontString(String text, int x, int y, Font font) {
		if (!font.isReady()) throw new IllegalStateException("Font is not loaded!");

		for (int i=0; i < text.length(); i++) {
			Image charImage = font.getChar(text.charAt(i)); 
			drawImage(charImage, x, y);
			x += charImage.getWidth();
		}
	}    
    
    @Override
    public void drawARGB(int a, int r, int g, int b) {
    	mPaint.setStyle(Style.FILL);
    	mCanvas.drawARGB(a, r, g, b);
    }    
    
    @Override
    public int getWidth() { return mFrameBuffer.getWidth(); }

    @Override
    public int getHeight() { return mFrameBuffer.getHeight(); }

}    