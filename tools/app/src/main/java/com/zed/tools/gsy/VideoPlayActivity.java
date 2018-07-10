//package com.zed.tools.gsy;
//
//import android.os.Bundle;
//
//import com.shuyu.gsyvideoplayer.GSYVideoManager;
//import com.zed.tools.BaseActivity;
//import com.zed.tools.R;
//
///**
// * author：Sun on 2017/8/24/0024.
// * email：1564063766@qq.com
// * remark:视频播放界面
// */
//public class VideoPlayActivity extends BaseActivity {
//    VideoPlayFragment f1, f2, f3, f4;
//
//    @Override
//    protected int getLayout() {
//        return R.layout.activity_video_play;
//    }
//
//    @Override
//    protected void onViewCreate() {
//        Bundle b1 = new Bundle();
//        b1.putString("url", "rtmp://live.hkstv.hk.lxdns.com/live/hks");
//        b1.putInt("tag", 0);
//        b1.putString("title", "rtmp://live.hkstv.hk.lxdns.com/live/hks");
//        Bundle b2 = new Bundle();
//        b2.putString("url", "rtmp://v1.one-tv.com/live/mpegts.stream");
//        b2.putString("title", "rtmp://v1.one-tv.com/live/mpegts.stream");
//        b2.putInt("tag", 1);
//        Bundle b3 = new Bundle();
//        b3.putString("url", "http://ivi.bupt.edu.cn/hls/cctv1hd.m3u8");
//        b3.putString("title", "http://ivi.bupt.edu.cn/hls/cctv1hd.m3u8");
//        b3.putInt("tag", 2);
//        Bundle b4 = new Bundle();
//        b4.putString("url", "rtmp://192.168.3.95:1935/live");
//        b4.putString("title", "rtmp://192.168.3.95:1935/live");
//        b4.putInt("tag", 3);
//        f1 = new VideoPlayFragment();
//        f1.setArguments(b1);
//        f2 = new VideoPlayFragment();
//        f2.setArguments(b2);
//        f3 = new VideoPlayFragment();
//        f3.setArguments(b3);
//        f4 = new VideoPlayFragment();
//        f4.setArguments(b4);
//        addFragment(f1, R.id.fl1);
//        addFragment(f2, R.id.fl2);
//        addFragment(f3, R.id.fl3);
//        addFragment(f4, R.id.fl4);
//    }
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        f1.onBackPressed();
//        f2.onBackPressed();
//        f3.onBackPressed();
//        f4.onBackPressed();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        GSYVideoManager.releaseAllVideos();
//    }
//}
//
