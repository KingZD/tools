//package com.zed.tools.gsy;
//
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//
//import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
//import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer;
//import com.zed.tools.BaseFragment;
//import com.zed.tools.LogUtil;
//import com.zed.tools.R;
//import com.zed.tools.ToastUtil;
//
//import butterknife.BindView;
//
///**
// * remark:视频播放界面
// */
//public class VideoPlayFragment extends BaseFragment {
//    private static final String TAG = "VideoPlayFragment";
//
//    @BindView(R.id.detail_player)
//    FrameLayout detailPlayerParent;
//    MultiSampleVideo detailPlayer;
//    @BindView(R.id.view_main)
//    RelativeLayout activityDetailPlayer;
//
//    private boolean isPlay;
//    private boolean isPause;
//    private String videoUrl;
//    private String videoName;
//    private GSYVideoOptionBuilder gsyVideoOptionBuilder;
//
//    @Override
//    protected int getLayout() {
//        return R.layout.fragment_video_play;
//    }
//
//    @Override
//    protected void onViewCreate() {
//        videoUrl = getArguments().getString("url");
//        videoName = getArguments().getString("title");
//        LogUtil.e(TAG, "接收到的RTMP: " + videoUrl);
//        initView();
//    }
//
//    private void initView() {
//        detailPlayer = new MultiSampleVideo(getActivity());
//        detailPlayer.bindParentView(detailPlayerParent);
//        //增加封面
//        ImageView imageView = new ImageView(getActivity());
//        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        imageView.setImageResource(R.drawable.xxx1);
//        gsyVideoOptionBuilder = new GSYVideoOptionBuilder()
//                .setPlayPosition(getArguments().getInt("tag"))
//                .setPlayTag(TAG + getArguments().getInt("tag"))
//                .setUrl(videoUrl)
//                .setVideoTitle(videoName)
//                .setThumbImageView(imageView)
//                .setRotateViewAuto(false)
//                .setIsTouchWiget(false)
//                .setLockLand(false)
//                .setNeedLockFull(true)
//                .setSeekRatio(1)
//                .setShowFullAnimation(false)
//                .setCacheWithPlay(false)
//                .setVideoAllCallBack(new SampleListener() {
//                    @Override
//                    public void onPrepared(String url, Object... objects) {
//                        super.onPrepared(url, objects);
//                        //开始播放了才能旋转和全屏
//                        isPlay = true;
//                        LogUtil.i(TAG, "开始播放");
//                    }
//
//                    @Override
//                    public void onClickQuitSmallWidget() {
//                        LogUtil.i(TAG, "退出小屏幕之前的回调");
////                        detailPlayer.bindParentView(detailPlayerParent);
////                        detailPlayerParent.removeAllViews();
////                        ViewGroup viewGroup = (ViewGroup) detailPlayer.getParent();
////                        if (viewGroup != null)
////                            viewGroup.removeAllViews();
////                        detailPlayerParent.addView(detailPlayer);
//                    }
//
//                    @Override
//                    public void onQuitSmallWidget(String url, Object... objects) {
//                        LogUtil.i(TAG, "退出小屏幕");
//                        playVideo(0);
////                        detailPlayerParent.removeAllViews();
////                        ViewGroup viewGroup = (ViewGroup) detailPlayer.getParent();
////                        if (viewGroup != null)
////                            viewGroup.removeAllViews();
////                        detailPlayerParent.addView(detailPlayer);
//                    }
//
//                    @Override
//                    public void onQuitFullscreen(String url, Object... objects) {
//                        super.onQuitFullscreen(url, objects);
//                        LogUtil.i(TAG, "退出全屏");
//                    }
//
//                    @Override
//                    public void onEnterFullscreen(String url, Object... objects) {
//                        super.onEnterFullscreen(url, objects);
//                        LogUtil.i(TAG, "进入全屏");
//                    }
//
//                    @Override
//                    public void onClickStop(String url, Object... objects) {
//                        super.onClickStop(url, objects);
//                        ToastUtil.showToast("暂停");
//                        stopVideoResult("暂停");
//                    }
//
//                    @Override
//                    public void onClickStopFullscreen(String url, Object... objects) {
//                        super.onClickStopFullscreen(url, objects);
//                        ToastUtil.showToast("全屏暂停");
//                        stopVideoResult("全屏暂停");
//                    }
//                });
//        gsyVideoOptionBuilder.build(detailPlayer);
////        if (getArguments().getInt("tag") == 0)
//        playVideo(500);
//    }
//
//    @Override
//    public void onPause() {
//        getCurPlay().onVideoPause();
//        super.onPause();
//        isPause = true;
//    }
//
//    @Override
//    public void onResume() {
//        getCurPlay().onVideoResume(false);
//        super.onResume();
//        isPause = false;
//    }
//
//    @Override
//    public void onDestroy() {
//        try {
//            if (isPlay) {
//                getCurPlay().release();
//                if (detailPlayer != null)
//                    detailPlayer.releaseVideos();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        super.onDestroy();
//    }
//
//    public void onBackPressed() {
//        try {
//            if (detailPlayer != null && detailPlayer.isSmall()) {
//                return;
//            }
//            //释放所有
//            if (isPlay) {
//                getCurPlay().release();
//                if (detailPlayer != null)
//                    detailPlayer.releaseVideos();
//            }
//            getCurPlay().setVideoAllCallBack(null);
//            getActivity().finish();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private GSYBaseVideoPlayer getCurPlay() {
//        if (detailPlayer.getCurrentPlayer() != null) {
//            return detailPlayer.getCurrentPlayer();
//        }
//        return detailPlayer;
//    }
//
//    private void playVideo(int delay) {
//        detailPlayer.release();
//        detailPlayer.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                detailPlayer.startPlayLogic();
//            }
//        }, delay);
//    }
//
//    public void stopVideoResult(String msg) {
//        ToastUtil.showToast(msg);
//    }
//}
//
