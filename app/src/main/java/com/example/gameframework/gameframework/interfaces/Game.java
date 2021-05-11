package com.example.gameframework.gameframework.interfaces;

import com.example.gameframework.gameframework.implementations.Screen;



public interface Game {
    public Audio    getAudio();
    public Input    getInput();
    public FileIO   getFileIO();
    public Graphics getGraphics();
    public void     setScreen(Screen screen);
    public Screen   getCurrentScreen();
    public Screen   getInitScreen();
}
