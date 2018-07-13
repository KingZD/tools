package com.zed.tools.gsy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.widget.ImageView;

import com.shuyu.gsyvideoplayer.utils.Debuger;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoViewBridge;
import com.zed.tools.R;

/**
 * Created by zed on 2018/7/4.
 */

public class MultiSampleVideo extends StandardGSYVideoPlayer {
    private MultiSampleVideoContraller contraller;
    private final static String TAG = "MultiSampleVideo";

    ImageView mCoverImage;

    String mCoverOriginUrl;

    int mDefaultRes;


    public MultiSampleVideo(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public MultiSampleVideo(Context context) {
        super(context);
    }

    public MultiSampleVideo(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void init(Context context) {
        super.init(context);
        mCoverImage = (ImageView) findViewById(R.id.thumbImage);

        if (mThumbImageViewLayout != null &&
                (mCurrentState == -1 || mCurrentState == CURRENT_STATE_NORMAL || mCurrentState == CURRENT_STATE_ERROR)) {
            mThumbImageViewLayout.setVisibility(VISIBLE);
        }
        onAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
            @Override
            public void onAudioFocusChange(int focusChange) {
                switch (focusChange) {
                    case AudioManager.AUDIOFOCUS_GAIN:
                        break;
                    case AudioManager.AUDIOFOCUS_LOSS:
                        post(new Runnable() {
                            @Override
                            public void run() {
                                //todo 判断如果不是外界造成的就不处理
                            }
                        });
                        break;
                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                        post(new Runnable() {
                            @Override
                            public void run() {
                                //todo 判断如果不是外界造成的就不处理
                            }
                        });
                        break;
                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                        break;
                }
            }
        };
        if (mThumbImageViewLayout != null)
            mThumbImageViewLayout.setOnTouchListener(new OnTouchListener() {
                long startTime = 0;
                int duration = 300;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            if (System.currentTimeMillis() - startTime <= duration) {
                                touchDoubleUp();
                                startTime = 0;
                                break;
                            }
                            startTime = System.currentTimeMillis();
                            break;
                    }
                    return false;
                }
            });
    }

    @Override
    public int getLayoutId() {
        return R.layout.video_layout_cover;
    }

    public void loadCoverImage(String url, int res) {
        mCoverImage.setImageResource(R.color.transparent);
    }

    //全屏
    @Override
    public GSYBaseVideoPlayer startWindowFullscreen(Context context, boolean actionBar, boolean statusBar) {
        GSYBaseVideoPlayer gsyBaseVideoPlayer = super.startWindowFullscreen(context, actionBar, statusBar);
        MultiSampleVideo sampleCoverVideo = (MultiSampleVideo) gsyBaseVideoPlayer;
        sampleCoverVideo.loadCoverImage(mCoverOriginUrl, mDefaultRes);
        return gsyBaseVideoPlayer;
    }

    @Override
    protected void lockTouchLogic() {
        super.lockTouchLogic();
    }

    /**
     * 下方两个重载方法，在播放开始前不屏蔽封面
     */
    @Override
    public void onSurfaceUpdated(Surface surface) {
        super.onSurfaceUpdated(surface);
        if (mThumbImageViewLayout != null && mThumbImageViewLayout.getVisibility() == VISIBLE) {
            mThumbImageViewLayout.setVisibility(INVISIBLE);
        }
    }

    @Override
    public GSYVideoViewBridge getGSYVideoManager() {
        return MultiSampleManager.getCustomManager(getKey());
    }

    @Override
    protected boolean backFromFull(Context context) {
        return MultiSampleManager.backFromWindowFull(context, getKey());
    }

    @Override
    protected void releaseVideos() {
        MultiSampleManager.releaseAllVideos(getKey());
    }


    @Override
    protected int getFullId() {
        return MultiSampleManager.FULLSCREEN_ID;
    }

    @Override
    protected int getSmallId() {
        return MultiSampleManager.SMALL_ID;
    }

    public String getKey() {
        if (mPlayPosition == -22) {
            Debuger.printfError(getClass().getSimpleName() + " used getKey() " + "******* PlayPosition never set. ********");
        }
        if (TextUtils.isEmpty(mPlayTag)) {
            Debuger.printfError(getClass().getSimpleName() + " used getKey() " + "******* PlayTag never set. ********");
        }
        return TAG + mPlayPosition + mPlayTag;
    }

    @Override
    protected void touchDoubleUp() {
        if (contraller != null)
            contraller.touchDoubleUp();
        //如果在双击全屏的模式下存在显示了 底部状态栏 需要在缩回去
        setViewShowState(mBottomContainer, GONE);
    }

    @Override
    protected void setViewShowState(View view, int visibility) {

        if (view == mThumbImageViewLayout && visibility != VISIBLE)
            return;

        if (view == mTopContainer && !isIfCurrentIsFullscreen())
            visibility = GONE;

        //在铺满容器播放时才显示视频控制按钮
        if (view != null && R.id.layout_bottom == view.getId()
                && contraller != null && !contraller.fullContainerPlay())
            visibility = GONE;
        super.setViewShowState(view, visibility);
    }

    public void setContraller(MultiSampleVideoContraller contraller) {
        this.contraller = contraller;
    }
}
