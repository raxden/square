package com.raxdenstudios.square.activity.module.impl;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

import com.raxdenstudios.square.activity.ModularActivity;
import com.raxdenstudios.square.activity.module.SplashTimerModule;
import com.raxdenstudios.square.activity.module.manager.ModuleActivityImpl;

/**
 * Created by agomez on 11/05/2105.
 */
public class SplashTimerModuleImpl extends ModuleActivityImpl implements SplashTimerModule.SplashTimerModuleListener {

    private static final String TAG = SplashTimerModuleImpl.class.getSimpleName();

    private final static int DEFAULT_TIME_MS = 3000;

    private SplashTimerModule mCallbacks;
    private SplashTimerConfiguration mConfig;
    private Handler mHandler = new Handler();
    private boolean isSplashEnded;
    private Object o = new Object();

    private Runnable splashRunnableTimer = new Runnable() {
        @Override
        public void run() {
            synchronized (o) {
                isSplashEnded = true;
                if (mCallbacks != null) {
                    mCallbacks.onSplashTimerEnd();
                }
            }
            removeRunnable();
        }
    };

    public static class SplashTimerConfiguration {
        public long time;
    }

    public static class DefaultSplashTimerConfiguration extends SplashTimerConfiguration {
        public DefaultSplashTimerConfiguration() {
            this.time = DEFAULT_TIME_MS;
        }
    }

    public SplashTimerModuleImpl(ModularActivity activity) {
        if (!(activity instanceof SplashTimerModule)) {
            throw new IllegalStateException("Activity must implement SplashTimerModule.");
        }
        mCallbacks = (SplashTimerModule)activity;
    }

    @Override
    public void onModuleCreate(Context context, Bundle savedInstanceState) {
        super.onModuleCreate(context, savedInstanceState);

        if (mConfig == null) {
            mConfig = new DefaultSplashTimerConfiguration();
        }

        if (mCallbacks != null) mCallbacks.onModuleLoaded(this);

        removeRunnable();
        postRunnable();
    }

    @Override
    public boolean onModuleBackPressed(Context context) {
        removeRunnable();
        if (mCallbacks != null) mCallbacks.onSplashTimerCancelled();
        return super.onModuleBackPressed(context);
    }

    @Override
    public void onModuleDestroy(Context context) {
        super.onModuleDestroy(context);

        synchronized (o) {
            if (!isSplashEnded) {
                if (mCallbacks != null) mCallbacks.onSplashTimerCancelled();
            }
            removeRunnable();
        }
    }

    private void removeRunnable() {
        if (mHandler != null) mHandler.removeCallbacks(splashRunnableTimer);
    }

    private void postRunnable() {
        if (mHandler != null) mHandler.postDelayed(splashRunnableTimer, mConfig.time);
    }

    @Override
    public SplashTimerConfiguration getConfiguration() {
        return mConfig;
    }

}
