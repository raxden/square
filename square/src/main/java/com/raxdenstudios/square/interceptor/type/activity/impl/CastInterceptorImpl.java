package com.raxdenstudios.square.interceptor.type.activity.impl;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastState;
import com.google.android.gms.cast.framework.CastStateListener;
import com.google.android.gms.cast.framework.IntroductoryOverlay;
import com.raxdenstudios.square.interceptor.callback.CastInterceptorCallback;
import com.raxdenstudios.square.interceptor.interactor.CastInterceptorInteractor;
import com.raxdenstudios.square.interceptor.type.ActivityInterceptor;

/**
 * Created by Ángel Gómez on 22/05/2015.
 */
public class CastInterceptorImpl
        extends ActivityInterceptor<CastInterceptorInteractor, CastInterceptorCallback>
        implements CastInterceptorInteractor {

    private IntroductoryOverlay mIntroductoryOverlay;
    private CastStateListener mCastStateListener;
    private CastContext mCastContext;
    private MenuItem mMediaRouteMenuItem;

    public CastInterceptorImpl(Activity activity) {
        super(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCastStateListener = new CastStateListener() {
            @Override
            public void onCastStateChanged(int newState) {
                if (newState != CastState.NO_DEVICES_AVAILABLE) {
                    mCallback.devicesNotAvailable();
                } else {
                    mCallback.devicesAvailable();
                    showIntroductoryOverlay();
                }
            }
        };
        mCastContext = CastContext.getSharedInstance(mActivity);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuItem mediaRoutMenuItem = mCallback.onCreateMediaRouteMenuItem(menu);
        if (mediaRoutMenuItem != null) {
            mMediaRouteMenuItem = CastButtonFactory.setUpMediaRouteButton(mActivity, menu, mediaRoutMenuItem.getItemId());
        }
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (mMediaRouteMenuItem != null) {
            mMediaRouteMenuItem.setVisible(true);
        }
    }

    @Override
    public void onResume() {
        mCastContext.addCastStateListener(mCastStateListener);
        super.onResume();
    }

    @Override
    public void onPause() {
        mCastContext.removeCastStateListener(mCastStateListener);
        super.onPause();
    }

    private void showIntroductoryOverlay() {
        if (mIntroductoryOverlay != null) {
            mIntroductoryOverlay.remove();
        }
        if (mMediaRouteMenuItem != null && mMediaRouteMenuItem.isVisible()) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    mIntroductoryOverlay = new IntroductoryOverlay
                            .Builder(mActivity, mMediaRouteMenuItem)
                            .setTitleText("Introducing Cast")
                            .setSingleTime()
                            .setOnOverlayDismissedListener(
                                    new IntroductoryOverlay.OnOverlayDismissedListener() {
                                        @Override
                                        public void onOverlayDismissed() {
                                            mIntroductoryOverlay = null;
                                        }
                                    })
                            .build();
                    mIntroductoryOverlay.show();
                }
            });
        }
    }

}
