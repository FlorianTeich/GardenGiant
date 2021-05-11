package com.example.gameframework.gameframework.implementations;

import java.io.IOException;

import com.example.gameframework.gameframework.interfaces.Music;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;


public class AndroidMusic implements Music, OnCompletionListener, OnSeekCompleteListener, OnPreparedListener, OnVideoSizeChangedListener {
    MediaPlayer  mMediaPlayer;
    boolean      mIsPrepared = false;

    public AndroidMusic(AssetFileDescriptor assetDescriptor) {
        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(assetDescriptor.getFileDescriptor(),
                    assetDescriptor.getStartOffset(),
                    assetDescriptor.getLength());
            mMediaPlayer.prepare();
            mIsPrepared = true;
            mMediaPlayer.setOnCompletionListener(this);
            mMediaPlayer.setOnSeekCompleteListener(this);
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setOnVideoSizeChangedListener(this);
        } catch (Exception e) {
            throw new RuntimeException("Couldn't load music");
        }
    }

    @Override
    public void dispose() {
        if (mMediaPlayer.isPlaying()) mMediaPlayer.stop();
        mMediaPlayer.release();
    }
    
    @Override
    public boolean isLooping() { return mMediaPlayer.isLooping(); }

    @Override
    public boolean isPlaying() { return mMediaPlayer.isPlaying(); }

    @Override
    public boolean isStopped() { return !mIsPrepared; }

    @Override
    public void pause() {
        if (mMediaPlayer.isPlaying()) mMediaPlayer.pause();
    }

    @Override
    public void play() {
        if (mMediaPlayer.isPlaying()) return;

        try {
            synchronized (this) {
                if (!mIsPrepared) mMediaPlayer.prepare();
                mMediaPlayer.start();
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void setLooping(boolean isLooping) {
        mMediaPlayer.setLooping(isLooping);
    }

    @Override
    public void setVolume(float volume) {
        mMediaPlayer.setVolume(volume, volume);
    }

    @Override
    public void stop() {
        if (mMediaPlayer.isPlaying() == true) {
            mMediaPlayer.stop();
            synchronized (this) {
                mIsPrepared = false;
            }
        }
    }

    @Override
    public void onCompletion(MediaPlayer player) {
        synchronized (this) {
            mIsPrepared = false;
        }
    }
    
    @Override
    public void seekBegin() {
        mMediaPlayer.seekTo(0);
    }

    @Override
    public void onPrepared(MediaPlayer player) {
        synchronized (this) {
            mIsPrepared = true;
        }
    }

    @Override
    public void onSeekComplete(MediaPlayer player) {
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer player, int width, int height) {
    }
}