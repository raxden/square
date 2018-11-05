package com.raxdenstudios.square.interceptor.commons.navigationcontentdrawer;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import com.raxdenstudios.square.interceptor.commons.R;
import com.raxdenstudios.square.interceptor.ActivityInterceptor;
import com.raxdenstudios.square.utils.FragmentUtils;

/**
 * Created by agomez on 21/05/2015.
 */
public class NavigationContentDrawerActivityInterceptor<TFragment extends Fragment> extends ActivityInterceptor<NavigationContentDrawerInterceptorCallback<TFragment>> implements NavigationContentDrawerInterceptor {

    private View mContentDrawerView;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    public NavigationContentDrawerActivityInterceptor(@NonNull AppCompatActivity activity) {
        super(activity, null);
    }

    public NavigationContentDrawerActivityInterceptor(@NonNull AppCompatActivity activity, @NonNull NavigationContentDrawerInterceptorCallback<TFragment> callback) {
        super(activity, callback);
    }

    @Override
    public void onConfigurationChanged(Configuration configuration) {
        if (mDrawerToggle != null) mDrawerToggle.onConfigurationChanged(configuration);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContentDrawerView = getCallback().onCreateContentDrawerView(savedInstanceState);
        if (mContentDrawerView != null) {
            mDrawerLayout = getCallback().onCreateDrawerLayout(savedInstanceState);
            Toolbar toolbar = getCallback().onCreateToolbarView(savedInstanceState);
            if (mDrawerLayout != null) {
                // set a custom shadow that overlays the main content when the drawer opens
                mDrawerLayout.setDrawerShadow(R.drawable.square__drawer_shadow, GravityCompat.START);
                // ActionBarDrawerToggle ties together the the proper interactions between the sliding drawer and the action bar app icon
                mDrawerToggle = toolbar != null ? initActionBarDrawerToogle(toolbar) : initActionBarDrawerToogle();
                mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mDrawerToggle != null && !mDrawerToggle.isDrawerIndicatorEnabled()) {
                            getActivity().onBackPressed();
                        }
                    }
                });
                mDrawerLayout.addDrawerListener(mDrawerToggle);
                getCallback().onActionBarDrawerToggleCreated(mDrawerToggle);
                getCallback().onDrawerLayoutCreated(mDrawerLayout);
            }
            TFragment contentDrawerFragment;
            if (savedInstanceState == null) {
                contentDrawerFragment = getCallback().onCreateContentDrawerFragment();
                FragmentUtils.INSTANCE.loadFragment(getActivity().getSupportFragmentManager(), mContentDrawerView.getId(), contentDrawerFragment);
            } else {
                contentDrawerFragment = (TFragment) FragmentUtils.INSTANCE.getFragment(getActivity().getSupportFragmentManager(), mContentDrawerView.getId());
            }
            getCallback().onContentDrawerFragmentLoaded(contentDrawerFragment);
        }
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if (mDrawerToggle != null) mDrawerToggle.syncState();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if (getCallback() != null && mDrawerToggle != null && mDrawerLayout != null) {
            if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0) {
                mDrawerToggle.setDrawerIndicatorEnabled(false);
                ActionBarDrawerToggle.Delegate delegate = getDrawerToggleDelegate();
                if (delegate != null) {
                    mDrawerToggle.setHomeAsUpIndicator(delegate.getThemeUpIndicator());
                }
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            } else {
                mDrawerToggle.setDrawerIndicatorEnabled(true);
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }
        }
    }

    @Override
    public boolean onBackPressed() {
        if (isOpen()) {
            close();
            return true;
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        setCallback(null);
    }

    private void close() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mDrawerLayout != null && mContentDrawerView != null) {
                    mDrawerLayout.closeDrawer(mContentDrawerView);
                }
            }
        });
    }

    private boolean isOpen() {
        if (mDrawerLayout != null && mContentDrawerView != null) {
            return mDrawerLayout.isDrawerOpen(mContentDrawerView);
        }
        return false;
    }

    private ActionBarDrawerToggle initActionBarDrawerToogle() {
        return new ActionBarDrawerToggle(getActivity(), mDrawerLayout, R.string.square__drawer_open, R.string.square__drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                NavigationContentDrawerActivityInterceptor.this.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                NavigationContentDrawerActivityInterceptor.this.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                NavigationContentDrawerActivityInterceptor.this.onDrawerSlide(drawerView, slideOffset);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                super.onDrawerStateChanged(newState);
                NavigationContentDrawerActivityInterceptor.this.onDrawerStateChanged(newState);
            }
        };
    }

    private ActionBarDrawerToggle initActionBarDrawerToogle(Toolbar toolbar) {
        return new ActionBarDrawerToggle(getActivity(), mDrawerLayout, toolbar, R.string.square__drawer_open, R.string.square__drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                NavigationContentDrawerActivityInterceptor.this.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                NavigationContentDrawerActivityInterceptor.this.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                NavigationContentDrawerActivityInterceptor.this.onDrawerSlide(drawerView, slideOffset);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                super.onDrawerStateChanged(newState);
                NavigationContentDrawerActivityInterceptor.this.onDrawerStateChanged(newState);
            }
        };
    }

    private void onDrawerClosed(View drawerView) {
        if (getCallback() != null) {
            getActivity().invalidateOptionsMenu();
            getCallback().onDrawerClosed(drawerView);
        }
    }

    private void onDrawerOpened(View drawerView) {
        if (getCallback() != null) {
            getActivity().invalidateOptionsMenu();
            getCallback().onDrawerOpened(drawerView);
        }
    }

    private void onDrawerSlide(View drawerView, float slideOffset) {
        if (getCallback() != null) {
            getCallback().onDrawerSlide(drawerView, slideOffset);
        }
    }

    private void onDrawerStateChanged(int newState) {
        if (getCallback() != null) {
            getCallback().onDrawerStateChanged(newState);
        }
    }

    private ActionBarDrawerToggle.Delegate getDrawerToggleDelegate() {
        if (getActivity() instanceof AppCompatActivity) {
            return ((AppCompatActivity) getActivity()).getDrawerToggleDelegate();
        }
        return null;
    }

}
