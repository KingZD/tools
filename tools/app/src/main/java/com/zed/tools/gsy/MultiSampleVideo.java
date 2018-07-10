//package com.zed.tools.gsy;
//
//import android.content.Context;
//import android.graphics.Point;
//import android.media.AudioManager;
//import android.text.TextUtils;
//import android.util.AttributeSet;
//import android.view.ViewGroup;
//
//import com.shuyu.gsyvideoplayer.utils.CommonUtil;
//import com.shuyu.gsyvideoplayer.utils.Debuger;
//import com.shuyu.gsyvideoplayer.video.base.GSYVideoViewBridge;
//
///**
// * Created by zed on 2018/7/4.
// */
//
//public class MultiSampleVideo extends SampleCoverVideo {
//    private ViewGroup parentView;
//    /**
//     * 当前是否小屏
//     */
//    private boolean isSmall;
//
//    private final static String TAG = "MultiSampleVideo";
//
//    public MultiSampleVideo(Context context, Boolean fullFlag) {
//        super(context, fullFlag);
//    }
//
//    public MultiSampleVideo(Context context) {
//        super(context);
//    }
//
//    public MultiSampleVideo(Context context, AttributeSet attrs) {
//        super(context, attrs);
//    }
//
//    @Override
//    protected void init(Context context) {
//        super.init(context);
//        onAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
//            @Override
//            public void onAudioFocusChange(int focusChange) {
//                switch (focusChange) {
//                    case AudioManager.AUDIOFOCUS_GAIN:
//                        break;
//                    case AudioManager.AUDIOFOCUS_LOSS:
//                        post(new Runnable() {
//                            @Override
//                            public void run() {
//                                //todo 判断如果不是外界造成的就不处理
//                            }
//                        });
//                        break;
//                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
//                        post(new Runnable() {
//                            @Override
//                            public void run() {
//                                //todo 判断如果不是外界造成的就不处理
//                            }
//                        });
//                        break;
//                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
//                        break;
//                }
//            }
//        };
//    }
//
//    @Override
//    public GSYVideoViewBridge getGSYVideoManager() {
//        return CustomManager.getCustomManager(getKey());
//    }
//
//    @Override
//    protected boolean backFromFull(Context context) {
//        return CustomManager.backFromWindowFull(context, getKey());
//    }
//
//    @Override
//    protected void releaseVideos() {
//        CustomManager.releaseAllVideos(getKey());
//    }
//
//
//    @Override
//    protected int getFullId() {
//        return CustomManager.FULLSCREEN_ID;
//    }
//
//    @Override
//    protected int getSmallId() {
//        return CustomManager.SMALL_ID;
//    }
//
//    public String getKey() {
//        if (mPlayPosition == -22) {
//            Debuger.printfError(getClass().getSimpleName() + " used getKey() " + "******* PlayPosition never set. ********");
//        }
//        if (TextUtils.isEmpty(mPlayTag)) {
//            Debuger.printfError(getClass().getSimpleName() + " used getKey() " + "******* PlayTag never set. ********");
//        }
//        return TAG + mPlayPosition + mPlayTag;
//    }
//
//    @Override
//    protected void touchDoubleUp() {
//        showSmallVideo(new Point(CommonUtil.getScreenWidth(getContext()), getHeight() * 2 + 4), true, true);
//        isSmall = true;
//    }
//
//    @Override
//    protected void closeSmallVideo() {
//        isSmall = false;
//        hideSmallVideo();
//    }
//
////    @Override
////    public void hideSmallVideo() {
////        addVideoPlayer();
////        super.hideSmallVideo();
////    }
//
//    public void addVideoPlayer() {
//        if (this.parentView != null)
//            this.parentView.removeAllViews();
//        ViewGroup viewGroup = (ViewGroup) this.getParent();
//        if (viewGroup != null)
//            viewGroup.removeAllViews();
//        if (this.parentView != null)
//            this.parentView.addView(this);
//    }
//
//    public boolean isSmall() {
//        return isSmall;
//    }
//
//    public void bindParentView(ViewGroup parentView) {
//        this.parentView = parentView;
//        if (this.getParent() == null)
//            parentView.addView(this);
//    }
//}
