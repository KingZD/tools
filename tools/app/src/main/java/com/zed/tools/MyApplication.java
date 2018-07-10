package com.zed.tools;

import android.app.Application;
import android.content.Context;

/**
 * Created by zed on 2018/7/4.
 */

public class MyApplication extends Application {
    static Context mContext;

    public static Context getInstance() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }
}
