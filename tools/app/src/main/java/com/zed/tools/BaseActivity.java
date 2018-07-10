package com.zed.tools;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * author：Sun on 2017/8/25/0025.
 * email：1564063766@qq.com
 * remark:MVP activity基类
 */
public abstract class BaseActivity extends AppCompatActivity {
    private FragmentManager fm = null;
    private BaseFragment mCurrentFragment = null;
    private Unbinder mUnBinder;
    private static final String TAG = BaseActivity.class.getName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        long s = System.currentTimeMillis();
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        mUnBinder = ButterKnife.bind(this);
        onViewCreate();
        LogUtil.i(TAG, String.valueOf(System.currentTimeMillis() - s));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnBinder != null)
            mUnBinder.unbind();
    }

    /**
     * 切换fragment
     *
     * @param fragment
     */
    protected void switchFragment(BaseFragment fragment, int containerViewId) {
        if (fragment == null) return;
        fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        //ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        if (!fragment.isAdded()) {
            if (mCurrentFragment == null)
                ft.add(containerViewId, fragment).show(fragment);
            else
                ft.add(containerViewId, fragment).hide(mCurrentFragment).show(fragment);
        } else {
            ft.hide(mCurrentFragment).show(fragment);
        }
        mCurrentFragment = fragment;
        ft.commit();
    }

    /**
     * 替换fragment
     *
     * @param fragment
     */
    protected void replaceFragment(BaseFragment fragment, int containerViewId) {
        if (fragment == null) return;
        fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (mCurrentFragment == null) {
            ft.add(containerViewId, fragment);
        } else {
            ft.remove(mCurrentFragment);
            ft.add(containerViewId, fragment);
        }
        mCurrentFragment = fragment;
        ft.commit();
    }

    /**
     * 替换fragment
     *
     * @param fragment
     */
    protected void addFragment(BaseFragment fragment, int containerViewId) {
        if (fragment == null) return;
        fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(containerViewId, fragment);
        ft.commit();
    }


    protected abstract int getLayout();

    protected abstract void onViewCreate();
}