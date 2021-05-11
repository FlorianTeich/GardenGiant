package com.example.gameframework.gameframework.implementations;

import com.example.gameframework.gameframework.interfaces.Sound;

import android.media.SoundPool;


public class AndroidSound implements Sound {
    int       mSoundId;
    SoundPool mSoundPool;

    public AndroidSound(SoundPool soundPool, int soundId) {
        mSoundId   = soundId;
        mSoundPool = soundPool;
    }

    @Override
    public void play(float volume) {
        mSoundPool.play(mSoundId, volume, volume, 0, 0, 1);
    }

    @Override
    public void dispose() {
        mSoundPool.unload(mSoundId);
    }
}
