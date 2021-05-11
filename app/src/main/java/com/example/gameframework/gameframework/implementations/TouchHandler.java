package com.example.gameframework.gameframework.implementations;

import java.util.List;

import com.example.gameframework.gameframework.interfaces.Input.TouchEvent;

import android.view.View.OnTouchListener;


public interface TouchHandler extends OnTouchListener {
    public boolean isTouchDown(int pointer);
    
    public int getTouchX(int pointer);
    
    public int getTouchY(int pointer);
    
    public List<TouchEvent> getTouchEvents();
}
