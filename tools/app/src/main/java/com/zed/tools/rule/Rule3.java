package com.zed.tools.rule;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class Rule3 extends View {
    public Rule3(Context context) {
        super(context);
    }

    public Rule3(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Rule3(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    /**
     * 画刻度线
     */
    void drawLine(Canvas canvas){
    }

    /**
     * 画指针
     */
    void drawCursor(Canvas canvas){

    }

    /**
     * 画刻度
     */
    void drawScale(){

    }
}
