package com.example.gameframework.gameframework.implementations;

import java.util.List;

import android.content.Context;
import android.view.View;

import com.example.gameframework.gameframework.interfaces.Input;


public class AndroidInput implements Input {    
    TouchHandler mTouchHandler;

    public AndroidInput(Context context, View view, float scaleX, float scaleY) {
        mTouchHandler = new SingleTouchHandler(view, scaleX, scaleY);
    }


    @Override
    public boolean isTouchDown(int pointer) {
        return mTouchHandler.isTouchDown(pointer);
    }

    @Override
    public int getTouchX(int pointer) {
        return mTouchHandler.getTouchX(pointer);
    }

    @Override
    public int getTouchY(int pointer) {
        return mTouchHandler.getTouchY(pointer);
    }

    @Override
    public List<TouchEvent> getTouchEvents() {
        return mTouchHandler.getTouchEvents();
    }
}