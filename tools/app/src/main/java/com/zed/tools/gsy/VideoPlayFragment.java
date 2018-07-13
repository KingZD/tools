package com.zed.tools.gsy;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer;
import com.zed.tools.BaseFragment;
import com.zed.tools.VideoActivityContraller;
import com.zed.tools.VideoFragmentContraller;
import com.zed.tools.util.LogUtil;
import com.zed.tools.R;
import com.zed.tools.util.ToastUtil;

import butterknife.BindView;

/**
 * remark:视频播放界面
 */
public class VideoPlayFragment extends BaseFragment implements VideoFragmentContraller, MultiSampleVideoContraller {
    private static final String TAG = "VideoPlayFragment";

    @BindView(R.id.detail_player)
    MultiSampleVideo detailPlayer;
    @BindView(R.id.view_main)
    RelativeLayout activityDetailPlayer;

    private boolean isPlay;
    private boolean isPause;
    private String videoUrl;
    private String videoName;
    private int index;
    private GSYVideoOptionBuilder gsyVideoOptionBuilder;
    VideoActivityContraller contraller;

    public void setContraller(VideoActivityContraller contraller) {
        this.contraller = contraller;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_video_play;
    }

    @Override
    protected void onViewCreate() {
        videoUrl = getArguments().getString("url");
        videoName = getArguments().getString("title");
        index = getArguments().getInt("tag");
        LogUtil.e(TAG, "接收到的RTMP: " + videoUrl);
        initView();
    }

    private void initView() {
        //增加封面
        ImageView imageView = new ImageView(getActivity());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(R.drawable.xxx1);
        //设置旋转
        OrientationUtils orientationUtils = new OrientationUtils(getActivity(), detailPlayer);
        gsyVideoOptionBuilder = new GSYVideoOptionBuilder()
                .setPlayPosition(index)
                .setPlayTag(TAG + index)
                .setUrl(videoUrl)
                .setVideoTitle(videoName)
                .setThumbImageView(imageView)
                .setIsTouchWiget(false)
                .setSeekRatio(1)
                .setRotateViewAuto(false)
                .setLockLand(true)
                .setShowFullAnimation(false)
                .setCacheWithPlay(false)
                .setVideoAllCallBack(new SampleListener() {
                    @Override
                    public void onPrepared(String url, Object... objects) {
                        super.onPrepared(url, objects);
                        //开始播放了才能旋转和全屏
                        isPlay = true;
                        LogUtil.i(TAG, "开始播放");
                    }
                });
        gsyVideoOptionBuilder.build(detailPlayer);
        detailPlayer.setContraller(this);
        //设置全屏按键功能,这是使用的是选择屏幕，而不是全屏
        detailPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailPlayer.startWindowFullscreen(getActivity(), true, true);
            }
        });
        playVideo(500);
    }

    @Override
    public void onPause() {
        getCurPlay().onVideoPause();
        super.onPause();
        isPause = true;
    }

    @Override
    public void onResume() {
        getCurPlay().onVideoResume(false);
        super.onResume();
        isPause = false;
    }

    @Override
    public void onDestroy() {
        try {
            if (detailPlayer == null) {
                return;
            }
            if (isPlay) {
                MultiSampleManager.removeManager(detailPlayer.getKey());
                MultiSampleManager.releaseAllVideos(detailPlayer.getKey());
                getCurPlay().release();
                detailPlayer.releaseVideos();
                getCurPlay().setVideoAllCallBack(null);
            }
            detailPlayer = null;
            LogUtil.i(TAG, "onDestroy");
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }


    public void onBackPressed() {
        if (detailPlayer.isIfCurrentIsFullscreen())
            detailPlayer.backFromFull(getActivity());
    }

    private GSYBaseVideoPlayer getCurPlay() {
        if (detailPlayer.getCurrentPlayer() != null) {
            return detailPlayer.getCurrentPlayer();
        }
        return detailPlayer;
    }

    private void playVideo(int delay) {
        setNeedMute(true);
        detailPlayer.release();
        detailPlayer.postDelayed(new Runnable() {
            @Override
            public void run() {
                detailPlayer.startPlayLogic();
            }
        }, delay);
    }

    public void stopVideoResult(String msg) {
        ToastUtil.showToast(msg);
    }

    @Override
    public void onVideoPause() {
        detailPlayer.onVideoPause();
    }

    @Override
    public void onVideoResume() {
        detailPlayer.onVideoResume(false);
    }

    @Override
    public void setNeedMute(boolean v) {
        MultiSampleManager.getCustomManager(detailPlayer.getKey()).setNeedMute(v);
    }

    @Override
    public void touchDoubleUp() {
        contraller.toggleFullScreen(index);
    }

    @Override
    public boolean fullContainerPlay() {
        return contraller.isFullContainer(index);
    }

}

