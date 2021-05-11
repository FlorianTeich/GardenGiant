package com.example.gameframework.gameframework.interfaces;

import java.util.ArrayList;

import com.example.gameframework.gameframework.implementations.AnimationFrame;

public interface Animation {
	public void    addFrame(Image image, long duration);
	public void    addFrames(ArrayList<AnimationFrame> frames);
	public void    addConsistentFrames(Image image, int frameCount, int width, int height, long duration);
	public long    getTotalDuration();
	public void    update(float deltaTime);
	public boolean isLooping();
	public void    setLooping(boolean isLooping);
	public Image   getCurrentFrame();
    public void    dispose();
}
