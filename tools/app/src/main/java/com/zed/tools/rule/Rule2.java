package com.zed.tools.rule;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import com.zed.tools.util.LogUtil;
import com.zed.tools.R;

/**
 * 改编于
 * https://github.com/PandaQAQ/LoopScale/blob/master/loopscaleview/src/main/java/com/pandaq/loopscaleview/LoopScaleView.java
 * 刻度尺
 */
public class Rule2 extends View {
    private final static String TAG = "com.pandaq.loopscale";
    private OnValueChangeListener mOnValueChangeListener;
    //画底线的画笔
    private Paint paint;
    //尺子控件总宽度
    private float viewWidth;
    //尺子控件总宽度
    private float viewHeight;
    //中间的标识图片
    private Bitmap cursorMap;
    //标签的位置
    private float cursorLocation;
    //未设置标识图片时默认绘制一条线作为标尺的线的颜色
    private int cursorColor = Color.RED;
    //大刻度线宽，默认为3
    private int cursorWidth = 3;
    //小刻度线宽，默认为2
    private int scaleWidth = 2;
    //设置屏幕宽度内最多显示的大刻度数，默认为3个
    private int showItemSize = 4;
    //标尺开始位置
    private float currLocation = 0;
    //刻度表的最大值，默认为200
    private int maxValue = 50;
    //一个刻度表示的值的大小
    private int oneItemValue = 1;
    //设置刻度线间宽度,大小由 showItemSize确定
    private int scaleDistance;
    //刻度高度，默认值为40
    private float scaleHeight = 40;
    //刻度的颜色刻度色，默认为灰色
    private int lineColor = Color.GRAY;
    //刻度文字的颜色，默认为灰色
    private int scaleTextColor = Color.GRAY;
    //刻度文字的大小,默认为24px
    private int scaleTextSize = 24;
    //手势解析器
    private GestureDetector mGestureDetector;
    //处理惯性滚动
    private Scroller mScroller;
    //scroller 滚动的最大值
    private int maxX;
    //scroller 滚动的最小值
    private int minX;
    //刻度尺上时间文本的显示高度
    private int dateHeight;
    //刻度尺上时间文本的间距
    private int dateSplitHeight = 10;
    //是否需要指向靠近刻度的一边 例如滑动到1.6松开手会自己滑动到2
    private boolean isNeedPoint = false;
    private int mLastX, mMove;
    //画刻度-1左边刻度 1 右边刻度
    private int direct = 1;
    //是否初始化过了
    private boolean isInit = false;

    public Rule2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public Rule2(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LoopScaleView);
        showItemSize = ta.getInteger(R.styleable.LoopScaleView_maxShowItem, showItemSize);
        maxValue = ta.getInteger(R.styleable.LoopScaleView_maxValue, maxValue);
        oneItemValue = ta.getInteger(R.styleable.LoopScaleView_oneItemValue, oneItemValue);
        scaleTextColor = ta.getColor(R.styleable.LoopScaleView_scaleTextColor, scaleTextColor);
        cursorColor = ta.getColor(R.styleable.LoopScaleView_cursorColor, cursorColor);
        isNeedPoint = ta.getBoolean(R.styleable.LoopScaleView_isNeedPoint, false);
        int cursorMapId = ta.getResourceId(R.styleable.LoopScaleView_cursorMap, -1);
        if (cursorMapId != -1) {
            cursorMap = BitmapFactory.decodeResource(getResources(), cursorMapId);
        }
        ta.recycle();
        paint = new Paint();
        mScroller = new Scroller(context);
        mGestureDetector = new GestureDetector(context, gestureListener);
        dateHeight = getTextRect("0").height();
    }

    /**
     * 得到需要绘制文本的范围
     *
     * @param word
     * @return
     */
    Rect getTextRect(String word) {
        Rect bounds = new Rect();
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(scaleTextColor);
        paint.setTextSize(scaleTextSize);
        paint.getTextBounds(word, 0, word.length(), bounds);
        return bounds;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewHeight = MeasureSpec.getSize(heightMeasureSpec);
        //一个小刻度的宽度（十进制，每5个小刻度为一个大刻度）
        scaleDistance = getMeasuredWidth() / (showItemSize * 5);
        //尺子长度总的个数*一个的宽度
        viewWidth = maxValue / oneItemValue * scaleDistance;
        maxX = getItemsCount() * scaleDistance;
        minX = -maxX;
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.clipRect(getPaddingStart(), getPaddingTop(), getWidth() - getPaddingRight(), viewHeight - getPaddingBottom());
        drawLine(canvas);
        drawCursor(canvas);
        drawCursorTime(canvas);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(scaleWidth);
        for (int i = 0; i <= maxValue / oneItemValue; i++) {
            drawScale(canvas, i, -direct);
        }
        for (int i = 0; i <= maxValue / oneItemValue; i++) {
            drawScale(canvas, i, direct);
        }
        isInit = true;
    }

    /**
     * 绘制主线
     *
     * @param canvas 绘制的画布
     */
    private void drawLine(Canvas canvas) {
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(3);
        paint.setColor(lineColor);
        canvas.drawLine(getPaddingStart(), viewHeight - (dateHeight + dateSplitHeight) * 2 - getPaddingBottom(), viewWidth - getPaddingEnd(), viewHeight - (dateHeight + dateSplitHeight) * 2 - getPaddingBottom(), paint);
    }

    /**
     * 绘制指示标签
     *
     * @param canvas 绘制控件的画布
     */
    private void drawCursor(Canvas canvas) {
        cursorLocation = showItemSize / 2 * 5 * scaleDistance; //屏幕显示Item 数的中间位置
        if (cursorMap == null) { //绘制一条红色的竖线线
            paint.setFlags(Paint.ANTI_ALIAS_FLAG);
            paint.setStrokeWidth(cursorWidth);
            paint.setColor(cursorColor);
            canvas.drawLine(cursorLocation, getPaddingTop() - (dateHeight + dateSplitHeight) * 2 - getPaddingBottom(), cursorLocation, viewHeight - (dateHeight + dateSplitHeight) * 2 - getPaddingBottom(), paint);
        } else { //绘制标识图片
            canvas.drawBitmap(cursorMap, cursorLocation - cursorMap.getWidth() / 2, viewHeight - cursorMap.getHeight() - (dateHeight + dateSplitHeight) * 2 - getPaddingBottom(), paint);
        }
    }

    /**
     * 绘制标签上面的时间
     *
     * @param canvas
     */
    private void drawCursorTime(Canvas canvas) {
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(ContextCompat.getColor(getContext(), android.R.color.holo_green_dark));
        paint.setTextSize(scaleTextSize);
        String drawStrY = "2018-07-09 22:36:00";
        Rect boundsY = new Rect();
        paint.getTextBounds(drawStrY, 0, drawStrY.length(), boundsY);
        if (cursorMap == null) { //绘制一条红色的竖线线
            canvas.drawText(drawStrY, cursorLocation - boundsY.width() / 2, (viewHeight - boundsY.height()) / 2, paint);
        } else {
            canvas.drawText(drawStrY, cursorLocation - boundsY.width() / 2, viewHeight - cursorMap.getHeight() - dateHeight * 2 - getPaddingBottom() - 3 * dateSplitHeight, paint);
        }
    }

    /**
     * 绘制刻度线
     *
     * @param canvas 画布
     * @param value  刻度值
     * @param direct 正向绘制还是逆向绘制
     */

    private void drawScale(Canvas canvas, int value, int direct) {
        if (!isInit) {
            //如果不需要循环且方向为右则累加左边刻度值作为叠加基数
            //并赋予当前位置为左边顶点刻度的距离为初始值
            if (direct > 0)
                currLocation = maxValue * scaleDistance;
            //如果不需要循环则方向保持一致向左
            direct = Math.abs(direct);
        }
        float location = cursorLocation + currLocation + value * scaleDistance * direct;
        LogUtil.i("drawScale", String.valueOf(location));
        if (value % 5 == 0) {
            canvas.drawLine(location, viewHeight - scaleHeight * 5 / 2 - getPaddingBottom(), location, viewHeight - (dateHeight + dateSplitHeight) * 2 - getPaddingBottom(), paint);
            String drawStrY = "2018-07-09";
            Rect boundsY = getTextRect(drawStrY);

            String drawStrD = "22:36:00";
            Rect boundsD = getTextRect(drawStrD);

            canvas.drawText(drawStrY, location - boundsY.width() / 2, viewHeight - boundsY.height() - getPaddingBottom() - dateSplitHeight, paint);
            canvas.drawText(drawStrD, location - boundsD.width() / 2, viewHeight - getPaddingBottom(), paint);
        } else {
            canvas.drawLine(location, viewHeight - scaleHeight * 2 - getPaddingBottom(), location, viewHeight - (dateHeight + dateSplitHeight) * 2 - getPaddingBottom(), paint);
        }
    }

    // 拦截屏幕滑动事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int xPosition = (int) event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mScroller.forceFinished(true);
                mLastX = xPosition;
                mMove = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                //这里不需要累加
                //mMove += (mLastX - xPosition);
                mMove = (xPosition - mLastX);
                /**如果放在
                 * {@link gestureListener#onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)}
                 * 方法里面调用会导致 手指触摸屏幕时间稍长或者滑动缓慢的情况下 不会触发该方法
                 */
                scrollView(mMove);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                //手指抬起是计算出当前滑到第几个位置
                getIntegerPosition();
                break;
            default:
                break;
        }
        mLastX = xPosition;
        mGestureDetector.onTouchEvent(event);
        return true;
    }

    /**
     * 滑动手势处理
     */
    private GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }


        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            /**
             * 将逻辑提取到
             * {@link Rule2#onTouchEvent(MotionEvent event)}
             * MotionEvent.ACTION_MOVE 事件下，避免手势不响应
             */
            // scrollView(distanceX);
            return true;
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (!mScroller.computeScrollOffset()) {
                mScroller.fling((int) currLocation, 0, (int) (velocityX / 1.5), 0, minX, maxX, 0, 0);
                setNextMessage(0);
            }
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return super.onSingleTapUp(e);
        }
    };

    /**
     * 滑动View
     *
     * @param distance 滑动的距离
     */
    private void scrollView(float distance) {
        currLocation += distance;
        //设置新的位置
        setCurrLocation(currLocation);
    }

    /**
     * 获取当前位置最近的整数刻度
     */
    private void getIntegerPosition() {
        if (isNeedPoint) {
            int currentItem = (int) (currLocation / scaleDistance);
            float fraction = currLocation - currentItem * scaleDistance;
            if (Math.abs(fraction) > 0.5 * scaleDistance) {
                if (fraction < 0) {
                    currLocation = (currentItem - 1) * scaleDistance;
                } else {
                    currLocation = (currentItem + 1) * scaleDistance;
                }
            } else {
                currLocation = currentItem * scaleDistance;
            }
            setCurrLocation(currLocation);
        }
    }

    /**
     * 获取一共有多少个刻度
     *
     * @return 总刻度数
     */
    public int getItemsCount() {
        return maxValue / oneItemValue;
    }

    /**
     * 设置标识的颜色
     *
     * @param color 颜色id
     */
    public void setCursorColor(int color) {
        this.cursorColor = color;
        postInvalidate();
    }

    /**
     * 设置标识的宽度
     *
     * @param width 宽度
     */
    public void setCursorWidth(int width) {
        this.cursorWidth = width;
        postInvalidate();
    }

    /**
     * 设置游标的bitmap位图
     *
     * @param cursorMap 位图
     */
    public void setCursorMap(Bitmap cursorMap) {
        this.cursorMap = cursorMap;
        postInvalidate();
    }

    /**
     * 设置刻度线的宽度
     *
     * @param scaleWidth 刻度线的宽度
     */
    public void setScaleWidth(int scaleWidth) {
        this.scaleWidth = scaleWidth;
        postInvalidate();
    }

    /**
     * 设置屏幕宽度内大Item的数量
     *
     * @param showItemSize 屏幕宽度内显示的大 item数量
     */
    public void setShowItemSize(int showItemSize) {
        this.showItemSize = showItemSize;
        postInvalidate();
    }

    /**
     * 设置当前游标所在的值
     *
     * @param currLocation 当前游标所在的值
     */
    private void setCurrLocation(float currLocation) {
        this.currLocation = currLocation;
        LogUtil.i("setCurrLocation", String.valueOf(currLocation));
        int currentItem = (int) (currLocation / scaleDistance) * oneItemValue;
        if (mOnValueChangeListener != null) {
            if (currentItem < 0) {
                currentItem = maxValue + currentItem;
            }
            mOnValueChangeListener.OnValueChange(currentItem);
        }
        postInvalidate();
    }

    /**
     * 设置刻度线的高度
     *
     * @param scaleHeight 刻度线的高度
     */
    public void setScaleHeight(float scaleHeight) {
        this.scaleHeight = scaleHeight;
        postInvalidate();
    }

    /**
     * 设置底部线条的颜色
     *
     * @param lineColor 底部线条的颜色值
     */
    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }

    /**
     * 设置刻度表上文字的颜色
     *
     * @param scaleTextColor 文字颜色id
     */
    public void setScaleTextColor(int scaleTextColor) {
        this.scaleTextColor = scaleTextColor;
        postInvalidate();
    }

    /**
     * 设置刻度标上的文字的大小
     *
     * @param scaleTextSize 文字大小
     */
    public void setScaleTextSize(int scaleTextSize) {
        this.scaleTextSize = scaleTextSize;
        postInvalidate();
    }

    /**
     * 设置刻度的最大值
     *
     * @param maxValue 刻度的最大值
     */
    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
        postInvalidate();
    }

    /**
     * 设置 一刻度所代表的的值的大小
     *
     * @param oneItemValue 一个刻度代表的值的大小
     */
    public void setOneItemValue(int oneItemValue) {
        this.oneItemValue = oneItemValue;
        postInvalidate();
    }

    /**
     * 设置当前刻度的位置
     *
     * @param currValue 当前刻度位置，小于0时取0 大于最大值时取最大值
     */
    public void setCurrentValue(int currValue) {
        if (currLocation < 0) {
            currLocation = 0;
        } else if (currLocation > maxValue) {
            currLocation = maxValue;
        }
        this.currLocation = currValue;
        postInvalidate();
    }


    private void setNextMessage(int message) {
        animationHandler.removeMessages(0);
        animationHandler.sendEmptyMessage(message);
    }

    // 动画处理
    @SuppressLint("HandlerLeak")
    private Handler animationHandler = new Handler() {
        public void handleMessage(Message msg) {
            mScroller.computeScrollOffset();
            int currX = mScroller.getCurrX();
            float delta = currX - currLocation;
            LogUtil.i("animationHandler", String.valueOf(delta));
            if (delta != 0) {
                scrollView(delta);
            }
            // 滚动还没有完成
            if (!mScroller.isFinished()) {
                animationHandler.sendEmptyMessage(msg.what);
            } else {
                //到整数刻度
                getIntegerPosition();
            }
        }
    };

    public void setOnValueChangeListener(OnValueChangeListener onValueChangeListener) {
        mOnValueChangeListener = onValueChangeListener;
    }

    public interface OnValueChangeListener {
        void OnValueChange(int newValue);
    }
}
