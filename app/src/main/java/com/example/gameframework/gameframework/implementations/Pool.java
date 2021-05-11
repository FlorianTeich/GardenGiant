package com.example.gameframework.gameframework.implementations;

import java.util.ArrayList;
import java.util.List;

public class Pool<T> {
    public interface PoolObjectFactory<T> {
        public T createObject();
    }

    private final List<T> 			   mFreeObjects;
    private final PoolObjectFactory<T> mFactory;
    private final int                  mMaxSize;

    public Pool(PoolObjectFactory<T> factory, int maxSize) {
        mFactory = factory;
        mMaxSize = maxSize;
        mFreeObjects = new ArrayList<T>(maxSize);
    }

    public T newObject() {
        T object = null;

        if (mFreeObjects.size() == 0) {
            object = mFactory.createObject();
        } else {
            object = mFreeObjects.remove(mFreeObjects.size() - 1);
        }
        return object;
    }

    public void free(T object) {
        if (mFreeObjects.size() < mMaxSize) mFreeObjects.add(object);
    }
}
