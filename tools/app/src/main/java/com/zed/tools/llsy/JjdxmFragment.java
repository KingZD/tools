package com.zed.tools.llsy;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import com.dou361.ijkplayer.listener.OnShowThumbnailListener;
import com.dou361.ijkplayer.widget.PlayStateParams;
import com.zed.tools.BaseFragment;
import com.zed.tools.GlideApp;
import com.zed.tools.LogUtil;
import com.zed.tools.R;


/**
 * Created by zed on 2018/7/6.
 */

public class JjdxmFragment extends BaseFragment implements VideoFragmentContraller {
    private static final String TAG = "JjdxmFragment";
    private PlayerView playerView;
    private String videoUrl;
    private String videoName;
    private int index;
    VideoActivityContraller contraller;

    public void setContraller(VideoActivityContraller contraller) {
        this.contraller = contraller;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_video_jjdxm;
    }

    @Override
    protected void onViewCreate() {
        videoUrl = getArguments().getString("url");
        videoName = getArguments().getString("title");
        index = getArguments().getInt("tag");
        LogUtil.e(TAG, "接收到的RTMP: " + videoUrl);
        playerView = new PlayerView(getActivity(), getCreateView()) {
            @Override
            public PlayerView toggleFullScreen() {
                contraller.toggleFullScreen(index);
                return this;
            }
        };
        playerView.setTitle(videoName)
                .setVolume(0)
                .setProcessDurationOrientation(PlayStateParams.PROCESS_PORTRAIT)
                .setScaleType(PlayStateParams.fillparent)
                .forbidTouch(false)
                .hideSteam(true)
                .hideCenterPlayer(true)
                .showThumbnail(new OnShowThumbnailListener() {
                    @Override
                    public void onShowThumbnail(ImageView ivThumbnail) {
                        GlideApp.with(getActivity())
                                .load("http://pic2.nipic.com/20090413/406638_125424003_2.jpg")
                                .placeholder(R.mipmap.ic_launcher)
                                .error(R.mipmap.ic_launcher)
                                .into(ivThumbnail);
                    }
                })
                .hideBottonBar(true)
                .hideHideTopBar(true)
                .setPlaySource("标清", videoUrl)
                .setChargeTie(false, 60);
        handler.sendEmptyMessageDelayed(VIDEO_PLAY, 500);
    }

    @Override
    public void setVolume(float v) {
        playerView.setVolume(v);
    }

    @Override
    public void onPause() {
        super.onPause();
        setVolume(0);
        onVideoPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        onVideoResume();
        if (contraller.isFullContainer() == index)
            setVolume(1);
    }

    @Override
    public void onVideoPause() {
        handler.sendEmptyMessageDelayed(VIDEO_PAUSE, 500);
    }

    @Override
    public void onVideoResume() {
        handler.sendEmptyMessageDelayed(VIDEO_RESUME, 500);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (playerView != null) {
            playerView.onDestroy();
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == VIDEO_RESUME) {
                playerView.resumePlay();
            } else if (msg.what == VIDEO_PAUSE) {
                playerView.pausePlay();
            } else if (msg.what == VIDEO_PLAY) {
                playerView.startPlay();
            }
        }
    };

    public int VIDEO_RESUME = 0;
    public int VIDEO_PAUSE = 1;
    public int VIDEO_PLAY = 2;
}
