package com.example.gameframework.gameframework.interfaces;

public interface Image {
    public int   getWidth();
    public int   getHeight();
    public Image getSubImage(int x, int y, int width, int height);
    public void  dispose();
}