package com.example.gameframework.gameframework.interfaces;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.SharedPreferences;


public interface FileIO {
    public InputStream readFile(String fileName)   throws IOException;
    public OutputStream writeFile(String fileName) throws IOException;
    public InputStream readAsset(String fileName)  throws IOException;
    public SharedPreferences getSharedPref();
}
