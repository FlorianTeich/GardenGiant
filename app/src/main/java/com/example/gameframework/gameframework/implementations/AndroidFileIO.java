package com.example.gameframework.gameframework.implementations;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Environment;
import android.preference.PreferenceManager;

import com.example.gameframework.gameframework.interfaces.FileIO;


public class AndroidFileIO implements FileIO {
    private Context      mContext;
    private AssetManager mAssets;
    private String       mExternalStoragePath;

    public AndroidFileIO(Context context) {
        mContext             = context;
        mAssets              = context.getAssets();
        mExternalStoragePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
    }

    @Override
    public InputStream readAsset(String fileName) throws IOException {
        return mAssets.open(fileName);
    }

    @Override
    public InputStream readFile(String fileName) throws IOException {
        return new FileInputStream(mExternalStoragePath + fileName);
    }

    @Override
    public OutputStream writeFile(String fileName) throws IOException {
        return new FileOutputStream(mExternalStoragePath + fileName);
    }

    public SharedPreferences getSharedPref() {
        return PreferenceManager.getDefaultSharedPreferences(mContext);
    }
}