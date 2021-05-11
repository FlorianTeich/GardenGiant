package com.example.gameframework.gameframework.implementations;

public abstract class Screen {
    protected final AndroidGame mGame;
    public Screen(AndroidGame game) {
        mGame = game;
    }
    public abstract void update(float deltaTime);
    public abstract void paint(float deltaTime);
    public abstract void pause();
    public abstract void resume();
    public abstract void dispose();
    public abstract void backButton();
}
