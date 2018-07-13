package com.zed.tools.gsy;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.zed.tools.BaseActivity;
import com.zed.tools.R;
import com.zed.tools.VideoActivityContraller;

import java.util.List;

import butterknife.BindViews;

/**
 * author：Sun on 2017/8/24/0024.
 * email：1564063766@qq.com
 * remark:视频播放界面
 */
public class VideoPlayActivity extends BaseActivity implements VideoActivityContraller {
    VideoPlayFragment f1, f2, f3, f4;
    @BindViews({R.id.fl1, R.id.fl2, R.id.fl3, R.id.fl4})
    List<FrameLayout> fragments;
    //第几个视频充满容器 -1表示多屏播放模式
    int fullContainer = -1;

    @Override
    protected int getLayout() {
        return R.layout.activity_video_play;
    }

    @Override
    protected void onViewCreate() {
        f1 = new VideoPlayFragment();
        f2 = new VideoPlayFragment();
        f3 = new VideoPlayFragment();
        f4 = new VideoPlayFragment();
        initData();
        addFragment(f1, R.id.fl1);
        addFragment(f2, R.id.fl2);
        addFragment(f3, R.id.fl3);
        addFragment(f4, R.id.fl4);
    }

    public void initData() {
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
        b4.putString("url", "rtmp://192.168.3.187/mylive/test1");
        b4.putString("title", "rtmp://192.168.3.187/mylive/test1");
        b4.putInt("tag", 3);
        f1.setArguments(b1);
        f1.setContraller(this);
        f2.setArguments(b2);
        f2.setContraller(this);
        f3.setArguments(b3);
        f3.setContraller(this);
        f4.setArguments(b4);
        f4.setContraller(this);
    }

    @Override
    public void onBackPressed() {
        if (MultiSampleManager.isFullState(this)) {
            f1.onBackPressed();
            f2.onBackPressed();
            f3.onBackPressed();
            f4.onBackPressed();
            return;
        }
        if (fullContainer > -1) {
            showAllContainer();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        MultiSampleManager.releaseAllVideos();
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        initData();
    }

    @Override
    public void showAllContainer() {
        fullContainer = -1;
        for (int i = 0; i < fragments.size(); i++) {
            fragments.get(i).setVisibility(View.VISIBLE);
            getFragment(i).setNeedMute(true);
            getFragment(i).onVideoResume();
        }
    }

    @Override
    public void showVideoFullContainer(int index) {
        fullContainer = index;
        for (int i = 0; i < fragments.size(); i++) {
            if (i != index) {
                fragments.get(i).setVisibility(View.GONE);
                getFragment(i).setNeedMute(true);
                getFragment(i).onVideoPause();
                continue;
            }
            getFragment(i).setNeedMute(false);
        }
    }

    @Override
    public int isFullContainer() {
        return fullContainer;
    }

    @Override
    public void toggleFullScreen(int index) {
        if (fullContainer > -1)
            showAllContainer();
        else
            showVideoFullContainer(index);
    }

    //当前哪个视频铺满了容器
    @Override
    public boolean isFullContainer(int index) {
        return fullContainer == index;
    }

    //    //获取对应播放窗口
    public VideoPlayFragment getFragment(int index) {
        VideoPlayFragment fragment = null;
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

