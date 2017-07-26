package com.raxdenstudios.square.interceptor.commons.injectfragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.raxdenstudios.square.interceptor.ActivityInterceptor;
import com.raxdenstudios.square.utils.FragmentUtils;

/**
 * Created by Ángel Gómez on 20/12/2016.
 */
public class InjectFragmentActivityInterceptorImpl<TFragment extends Fragment> extends ActivityInterceptor<InjectFragmentInterceptorCallback<TFragment>> implements InjectFragmentInterceptor {

    public InjectFragmentActivityInterceptorImpl(@NonNull Activity activity) {
        super(activity);
    }

    public InjectFragmentActivityInterceptorImpl(@NonNull Activity activity, @NonNull InjectFragmentInterceptorCallback<TFragment> callback) {
        super(activity, callback);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View contentView = mCallback.onLoadFragmentContainer(savedInstanceState);
        if (contentView != null) {
            TFragment contentFragment;
            if (savedInstanceState == null) {
                contentFragment = mCallback.onCreateFragment();
                FragmentUtils.loadFragment(mActivity.getFragmentManager(), contentView.getId(), contentFragment);
            } else {
                contentFragment = (TFragment) FragmentUtils.getFragment(mActivity.getFragmentManager(), contentView.getId());
            }
            mCallback.onFragmentLoaded(contentFragment);
        }
    }


}
