package com.zed.tools.gsy;

import android.content.Context;
import android.media.AudioManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.shuyu.gsyvideoplayer.utils.Debuger;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoViewBridge;

/**
 * Created by zed on 2018/7/4.
 */

public interface MultiSampleVideoContraller {
    void touchDoubleUp();

    boolean fullContainerPlay();
}
