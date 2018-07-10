//package com.zed.tools.gsy;
//
//import android.content.Context;
//import android.graphics.Point;
//import android.util.AttributeSet;
//import android.view.MotionEvent;
//import android.view.Surface;
//import android.view.View;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.SeekBar;
//import android.widget.TextView;
//
//import com.shuyu.gsyvideoplayer.utils.CommonUtil;
//import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
//import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer;
//import com.zed.tools.R;
//
///**
// * Created by zed on 2018/7/4.
// */
//
//public class SampleCoverVideo extends StandardGSYVideoPlayer {
//
//    ImageView mCoverImage, mFullscreenButton;
//
//    String mCoverOriginUrl;
//
//    int mDefaultRes;
//
//    TextView currentTimeView, totalView;
//    SeekBar progressView;
//
//    public ImageView getFullscreenButton() {
//        return mFullscreenButton;
//    }
//
//    public TextView getTotalView() {
//        return totalView;
//    }
//
//    public SeekBar getProgressView() {
//        return progressView;
//    }
//
//    public TextView getCurrentTimeView() {
//        return currentTimeView;
//    }
//
//    public SampleCoverVideo(Context context, Boolean fullFlag) {
//        super(context, fullFlag);
//    }
//
//    public SampleCoverVideo(Context context) {
//        super(context);
//    }
//
//    public SampleCoverVideo(Context context, AttributeSet attrs) {
//        super(context, attrs);
//    }
//
//    @Override
//    protected void init(Context context) {
//        super.init(context);
//        mCoverImage = (ImageView) findViewById(R.id.thumbImage);
//        mFullscreenButton = (ImageView) findViewById(R.id.fullscreen);
//        totalView = (TextView) findViewById(R.id.total);
//        progressView = (SeekBar) findViewById(R.id.progress);
//        currentTimeView = (TextView) findViewById(R.id.current);
//
//        if (mThumbImageViewLayout != null &&
//                (mCurrentState == -1 || mCurrentState == CURRENT_STATE_NORMAL || mCurrentState == CURRENT_STATE_ERROR)) {
//            mThumbImageViewLayout.setVisibility(VISIBLE);
//        }
//    }
//
//    @Override
//    public int getLayoutId() {
//        return R.layout.video_layout_cover;
//    }
//
//    public void loadCoverImage(String url, int res) {
//        mCoverImage.setImageResource(R.color.transparent);
//    }
//
//    @Override
//    public GSYBaseVideoPlayer startWindowFullscreen(Context context, boolean actionBar, boolean statusBar) {
//        GSYBaseVideoPlayer gsyBaseVideoPlayer = super.startWindowFullscreen(context, actionBar, statusBar);
//        SampleCoverVideo sampleCoverVideo = (SampleCoverVideo) gsyBaseVideoPlayer;
//        sampleCoverVideo.loadCoverImage(mCoverOriginUrl, mDefaultRes);
//        return gsyBaseVideoPlayer;
//    }
//
//
//    @Override
//    public GSYBaseVideoPlayer showSmallVideo(Point size, boolean actionBar, boolean statusBar) {
//        //下面这里替换成你自己的强制转化
//        SampleCoverVideo sampleCoverVideo = (SampleCoverVideo) super.showSmallVideo(size, actionBar, statusBar);
//        sampleCoverVideo.mStartButton.setVisibility(GONE);
//        sampleCoverVideo.mStartButton = null;
//        FrameLayout.LayoutParams lp = (LayoutParams) sampleCoverVideo.getLayoutParams();
//        int marginLeft = CommonUtil.getScreenWidth(this.mContext) - size.x;
//        lp.setMargins(marginLeft, 0, 0, 0);
//        sampleCoverVideo.setSmallVideoTextureView(new OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return true;
//            }
//        });
//        return sampleCoverVideo;
//    }
//
//    @Override
//    protected void lockTouchLogic() {
//        super.lockTouchLogic();
//    }
//
//    /**
//     * 下方两个重载方法，在播放开始前不屏蔽封面
//     */
//    @Override
//    public void onSurfaceUpdated(Surface surface) {
//        super.onSurfaceUpdated(surface);
//        if (mThumbImageViewLayout != null && mThumbImageViewLayout.getVisibility() == VISIBLE) {
//            mThumbImageViewLayout.setVisibility(INVISIBLE);
//        }
//    }
//
//    @Override
//    protected void setViewShowState(View view, int visibility) {
//        if (view == mThumbImageViewLayout && visibility != VISIBLE) {
//            return;
//        }
//        super.setViewShowState(view, visibility);
//    }
//
//    /**
//     * 下方两个重载方法，在播放开始不显示底部进度
//     */
//
//    @Override
//    protected void changeUiToNormal() {
//        super.changeUiToNormal();
//        setViewShowState(mTopContainer, INVISIBLE);
//        setViewShowState(mBottomContainer, INVISIBLE);
//        setViewShowState(mStartButton, INVISIBLE);
//        setViewShowState(mLoadingProgressBar, INVISIBLE);
//        setViewShowState(mThumbImageViewLayout, INVISIBLE);
//        setViewShowState(mBottomProgressBar, INVISIBLE);
//        setViewShowState(mLockScreen, GONE);
//    }
//
//    @Override
//    protected void changeUiToPreparingShow() {
//        super.changeUiToPreparingShow();
//        setViewShowState(mTopContainer, INVISIBLE);
//        setViewShowState(mBottomContainer, INVISIBLE);
//        setViewShowState(mStartButton, INVISIBLE);
//        setViewShowState(mLoadingProgressBar, VISIBLE);
//        setViewShowState(mThumbImageViewLayout, INVISIBLE);
//        setViewShowState(mBottomProgressBar, INVISIBLE);
//        setViewShowState(mLockScreen, GONE);
//    }
//
//    @Override
//    protected void changeUiToPlayingShow() {
//        super.changeUiToPlayingShow();
//        setViewShowState(mTopContainer, INVISIBLE);
//        setViewShowState(mBottomContainer, INVISIBLE);
//        setViewShowState(mStartButton, INVISIBLE);
//        setViewShowState(mLoadingProgressBar, INVISIBLE);
//        setViewShowState(mThumbImageViewLayout, INVISIBLE);
//        setViewShowState(mBottomProgressBar, INVISIBLE);
//        setViewShowState(mLockScreen, GONE);
//    }
//
//    @Override
//    protected void changeUiToPauseShow() {
//        super.changeUiToPauseShow();
//        setViewShowState(mTopContainer, INVISIBLE);
//        setViewShowState(mBottomContainer, INVISIBLE);
//        setViewShowState(mStartButton, INVISIBLE);
//        setViewShowState(mLoadingProgressBar, INVISIBLE);
//        setViewShowState(mThumbImageViewLayout, INVISIBLE);
//        setViewShowState(mBottomProgressBar, INVISIBLE);
//        setViewShowState(mLockScreen, GONE);
//    }
//
//    @Override
//    protected void changeUiToPlayingBufferingShow() {
//        super.changeUiToPlayingBufferingShow();
//        setViewShowState(mTopContainer, INVISIBLE);
//        setViewShowState(mBottomContainer, INVISIBLE);
//        setViewShowState(mStartButton, INVISIBLE);
//        setViewShowState(mLoadingProgressBar, VISIBLE);
//        setViewShowState(mThumbImageViewLayout, INVISIBLE);
//        setViewShowState(mBottomProgressBar, INVISIBLE);
//        setViewShowState(mLockScreen, GONE);
//    }
//
//    @Override
//    protected void changeUiToCompleteShow() {
//        super.changeUiToCompleteShow();
//        setViewShowState(mTopContainer, INVISIBLE);
//        setViewShowState(mBottomContainer, INVISIBLE);
//        setViewShowState(mStartButton, INVISIBLE);
//        setViewShowState(mLoadingProgressBar, INVISIBLE);
//        setViewShowState(mThumbImageViewLayout, INVISIBLE);
//        setViewShowState(mBottomProgressBar, INVISIBLE);
//        setViewShowState(mLockScreen, GONE);
//    }
//
//    @Override
//    protected void changeUiToError() {
//        super.changeUiToError();
//        setViewShowState(mTopContainer, INVISIBLE);
//        setViewShowState(mBottomContainer, INVISIBLE);
//        setViewShowState(mStartButton, INVISIBLE);
//        setViewShowState(mLoadingProgressBar, INVISIBLE);
//        setViewShowState(mThumbImageViewLayout, INVISIBLE);
//        setViewShowState(mBottomProgressBar, INVISIBLE);
//        setViewShowState(mLockScreen, GONE);
//    }
//
//    @Override
//    public void startAfterPrepared() {
//        super.startAfterPrepared();
//        setViewShowState(mBottomContainer, INVISIBLE);
//        setViewShowState(mBottomProgressBar, INVISIBLE);
//    }
//
//    @Override
//    protected void hideAllWidget() {
//        super.hideAllWidget();
//        setViewShowState(mBottomProgressBar, INVISIBLE);
//    }
//
//    @Override
//    protected void changeUiToCompleteClear() {
//        super.changeUiToCompleteClear();
//        setViewShowState(mBottomProgressBar, INVISIBLE);
//    }
//
//    @Override
//    protected void changeUiToPlayingBufferingClear() {
//        super.changeUiToPlayingBufferingClear();
//        setViewShowState(mBottomProgressBar, INVISIBLE);
//    }
//
//    @Override
//    protected void changeUiToPauseClear() {
//        super.changeUiToPauseClear();
//        setViewShowState(mBottomProgressBar, INVISIBLE);
//    }
//
//    @Override
//    protected void changeUiToPlayingClear() {
//        super.changeUiToPlayingClear();
//        setViewShowState(mBottomProgressBar, INVISIBLE);
//    }
//}
