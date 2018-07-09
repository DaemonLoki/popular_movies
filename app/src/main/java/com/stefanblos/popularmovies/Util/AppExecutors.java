package com.stefanblos.popularmovies.Util;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExecutors {

    private static final Object LOCK = new Object();
    private static AppExecutors mInstance;
    private final Executor diskIO;

    private AppExecutors(Executor diskIO) {
        this.diskIO = diskIO;
    }

    public static AppExecutors getInstance() {
        if (mInstance == null) {
            synchronized (LOCK) {
                mInstance = new AppExecutors(Executors.newSingleThreadExecutor());
            }
        }
        return mInstance;
    }

    public Executor getDiskIO() {
        return diskIO;
    }
}
