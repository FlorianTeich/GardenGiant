package com.example.gameframework.gameframework.interfaces;

import com.example.gameframework.gameframework.implementations.Font;

import android.graphics.Paint;

public interface Graphics {
    public Image newImage(String fileName);
    public void  clearScreen(int color);
    public void  drawImage(Image image, int x, int y, int srcX, int srcY, int srcWidth, int srcHeight);
    public void  drawImage(Image Image, int x, int y);
    public void  drawScaledImage(Image Image, int x, int y, int width, int height, int srcX, int srcY, int  srcWidth, int srcHeight);
    public void  drawString(String text, int x, int y, Paint paint);
    public void  drawBitmapFontString(String text, int x, int y, Font font);
	public void  drawLine(int x, int y, int x2, int y2, int color);
	public void  drawRect(int x, int y, int width, int height, int color);
	public void  drawARGB(int a, int r, int g, int b);
    public int   getWidth();
    public int   getHeight();
}
