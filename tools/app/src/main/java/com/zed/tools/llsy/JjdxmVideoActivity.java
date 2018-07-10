package com.zed.tools.llsy;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.FrameLayout;

import com.zed.tools.BaseActivity;
import com.zed.tools.R;

import java.util.List;

import butterknife.BindViews;

/**
 * Created by zed on 2018/7/6.
 */

public class JjdxmVideoActivity extends BaseActivity implements VideoActivityContraller {
    @BindViews({R.id.fl1, R.id.fl2, R.id.fl3, R.id.fl4})
    List<FrameLayout> fragments;
    JjdxmFragment f1, f2, f3, f4;
    //第几个视频充满容器 -1表示无
    int fullContainer = -1;

    @Override
    protected int getLayout() {
        return R.layout.activity_video_jjdxm;
    }

    @Override
    protected void onViewCreate() {
        handler.sendEmptyMessageDelayed(1, 50);
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle b1 = new Bundle();
            b1.putString("url", "rtmp://live.hkstv.hk.lxdns.com/live/hks");
            b1.putInt("tag", 0);
            b1.putString("title", "rtmp://live.hkstv.hk.lxdns.com/live/hks");
            Bundle b2 = new Bundle();
            b2.putString("url", "rtmp://v1.one-tv.com/live/mpegts.stream");
            b2.putString("title", "rtmp://v1.one-tv.com/live/mpegts.stream");
            b2.putInt("tag", 1);
            Bundle b3 = new Bundle();
            b3.putString("url", "http://ivi.bupt.edu.cn/hls/cctv1hd.m3u8");
            b3.putString("title", "http://ivi.bupt.edu.cn/hls/cctv1hd.m3u8");
            b3.putInt("tag", 2);
            Bundle b4 = new Bundle();
            b4.putString("url", "rtmp://192.168.3.95:1935/live");
            b4.putString("title", "rtmp://192.168.3.95:1935/live");
            b4.putInt("tag", 3);
            f1 = new JjdxmFragment();
            f1.setArguments(b1);
            f1.setContraller(JjdxmVideoActivity.this);
            f2 = new JjdxmFragment();
            f2.setArguments(b2);
            f2.setContraller(JjdxmVideoActivity.this);
            f3 = new JjdxmFragment();
            f3.setArguments(b3);
            f3.setContraller(JjdxmVideoActivity.this);
            f4 = new JjdxmFragment();
            f4.setArguments(b4);
            f4.setContraller(JjdxmVideoActivity.this);
            addFragment(f1, R.id.fl1);
            addFragment(f2, R.id.fl2);
            addFragment(f3, R.id.fl3);
            addFragment(f4, R.id.fl4);
        }
    };


    //将视频在容器填满
    @Override
    public void showVideoFullContainer(int index) {
        fullContainer = index;
        for (int i = 0; i < fragments.size(); i++) {
            if (i != index) {
                fragments.get(i).setVisibility(View.GONE);
                getFragment(i).setVolume(0);
                getFragment(i).onVideoPause();
                continue;
            }
            getFragment(i).setVolume(1);
        }
    }

    //展示所有视频
    @Override
    public void showAllContainer() {
        fullContainer = -1;
        for (int i = 0; i < fragments.size(); i++) {
            fragments.get(i).setVisibility(View.VISIBLE);
            getFragment(i).setVolume(0);
            getFragment(i).onVideoResume();
        }
    }

    //视频是否填满容器
    @Override
    public int isFullContainer() {
        return fullContainer;
    }

    //铺满还原
    @Override
    public void toggleFullScreen(int index) {
        if (fullContainer > -1)
            showAllContainer();
        else
            showVideoFullContainer(index);
    }

    @Override
    public void onBackPressed() {
        if (fullContainer > -1) {
            showAllContainer();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //获取对应播放窗口
    public JjdxmFragment getFragment(int index) {
        JjdxmFragment fragment = null;
        switch (index) {
            case 0:
                fragment = f1;
                break;
            case 1:
                fragment = f2;
                break;
            case 2:
                fragment = f3;
                break;
            case 3:
                fragment = f4;
                break;
        }
        return fragment;
    }
}
