package com.example.gameframework.gameframework.implementations;

import java.io.IOException;

import com.example.gameframework.gameframework.interfaces.Audio;
import com.example.gameframework.gameframework.interfaces.Music;
import com.example.gameframework.gameframework.interfaces.Sound;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;


public class AndroidAudio implements Audio {
    AssetManager mAssets;
    SoundPool    mSoundPool;

    public AndroidAudio(Activity activity) {
        activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        mAssets    = activity.getAssets();
        mSoundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);
    }

    @Override
    public Music createMusic(String fileName) {
        try {
            AssetFileDescriptor assetDescriptor = mAssets.openFd(fileName);
            return new AndroidMusic(assetDescriptor);
        } catch (IOException e) {
            throw new RuntimeException("Error loading " + fileName);
        }
    }

    @Override
    public Sound createSound(String fileName) {
        try {
            AssetFileDescriptor assetDescriptor = mAssets.openFd(fileName);
            int soundId = mSoundPool.load(assetDescriptor, 0);
            return new AndroidSound(mSoundPool, soundId);
        } catch (IOException e) {
            throw new RuntimeException("Error loading " + fileName);
        }
    }
}

