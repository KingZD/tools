package com.zed.tools;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * author：Sun on 2017/8/25/0025.
 * email：1564063766@qq.com
 * remark:MVP activity基类
 */
public abstract class BaseFragment extends Fragment {
    View inflate;
    private Unbinder mUnBinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflate = inflater.inflate(getLayout(), container, false);
        mUnBinder = ButterKnife.bind(this, inflate);
        onViewCreate();
        return inflate;
    }

    protected View getCreateView() {
        return inflate;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mUnBinder != null)
            mUnBinder.unbind();
    }

    protected abstract int getLayout();

    protected abstract void onViewCreate();
}