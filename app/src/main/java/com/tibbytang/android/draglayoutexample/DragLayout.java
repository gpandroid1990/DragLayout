package com.tibbytang.android.draglayoutexample;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.elvishew.xlog.XLog;

/**
 * 作者:tibbytang
 * 微信:tibbytang19900607
 * 有问题加微信
 * 创建于:2020-04-02 09:54
 */
public class DragLayout extends FrameLayout {
    private static final String TAG = DragLayout.class.getSimpleName();
    private FrameLayout mLeftDragContentLayout;
    private FrameLayout mRightDragContentLayout;
    private FrameLayout mTopDragContentLayout;
    private FrameLayout mBottomDragContentLayout;
    private LinearLayout mLeftDragView;
    private LinearLayout mRightDragView;
    private LinearLayout mTopDragView;
    private LinearLayout mBottomDragView;

    private int mScreenWidth;
    private int mScreenHeight;
    private int mWidth = 0;
    private int mHeight = 0;

    private int mLeftWidth = 0;
    private int mRightWidth = 0;
    private int mTopHeight = 0;
    private int mBottomHeight = 0;
    private DragStateListener mDragListener;

    public DragLayout(@NonNull Context context) {
        super(context);
        init();
    }

    public DragLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DragLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public DragLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mScreenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
        mScreenHeight = getContext().getResources().getDisplayMetrics().heightPixels;
        mRightWidth = mScreenWidth;
        mBottomHeight = mScreenHeight;
    }

    public void addDragStateListener(DragStateListener dragListener) {
        mDragListener = dragListener;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mLeftDragContentLayout = findViewById(R.id.main_left_layout);
        mRightDragContentLayout = findViewById(R.id.main_right_layout);
        mTopDragContentLayout = findViewById(R.id.main_top_layout);
        mBottomDragContentLayout = findViewById(R.id.main_bottom_layout);

        mLeftDragView = findViewById(R.id.main_drag_left_view);
        mRightDragView = findViewById(R.id.main_drag_right_view);
        mTopDragView = findViewById(R.id.main_drag_top_view);
        mBottomDragView = findViewById(R.id.main_drag_bottom_view);
        initLeftDragview();
        initRightDragview();
        initTopDragView();
        initBottomDragView();
    }

    /**
     * 初始化左边拖拽事件
     */
    private void initLeftDragview() {
        if (null != mLeftDragView) {
            final int[] originRawx = {-1};
            mLeftDragView.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_MOVE:
                            int rawX = (int) event.getRawX();
                            XLog.d(TAG + " rawX=" + rawX);
                            mLeftWidth = rawX;
                            if (originRawx[0] != rawX) {
                                moveLeftView(rawX);
                                originRawx[0] = rawX;
                            }
                            break;
                    }
                    return true;
                }
            });
        }
    }

    /**
     * 初始化右边拖拽事件
     */
    private void initRightDragview() {
        if (null != mRightDragView) {
            final int[] originRawx = {-1};
            mRightDragView.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_MOVE:
                            int rawX = (int) event.getRawX();
                            XLog.d(TAG + " rawX=" + rawX);
                            mRightWidth = rawX;
                            if (originRawx[0] != rawX) {
                                moveRightView(rawX);
                                originRawx[0] = rawX;
                            }
                            break;
                    }
                    return true;
                }
            });
        }
    }

    /**
     * 初始化顶部拖拽view
     */
    private void initTopDragView() {
        if (null != mTopDragView) {
            final int[] originRawx = {-1};
            mTopDragView.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_MOVE:
                            int rawY = (int) event.getRawY();
                            XLog.d(TAG + " rawY=" + rawY);
                            mTopHeight = rawY;
                            if (originRawx[0] != rawY) {
                                moveTopView(rawY);
                                originRawx[0] = rawY;
                            }
                            break;
                    }
                    return true;
                }
            });
        }
    }

    /**
     * 初始化底部拖拽view
     */
    private void initBottomDragView() {
        if (null != mBottomDragView) {
            final int[] originRawx = {-1};
            mBottomDragView.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_MOVE:
                            int rawY = (int) event.getRawY();
                            XLog.d(TAG + " rawY=" + rawY);
                            XLog.d(TAG + " x=" + event.getX() + " y=" + event.getY());
                            mBottomHeight = rawY;
                            if (originRawx[0] != rawY) {
                                moveBottomView(rawY);
                                originRawx[0] = rawY;
                            }
                            break;
                    }
                    return true;
                }
            });
        }
    }

    private void moveLeftView(int width) {
        XLog.d(TAG + " moveLeftView width=" + width);
        if (null != mLeftDragView && mLeftDragContentLayout != null) {
            LayoutParams layoutParams = (LayoutParams) mLeftDragContentLayout.getLayoutParams();
            if (width <= 0) {
                layoutParams.width = 0;
            } else {
                layoutParams.width = width;
            }
            mLeftDragContentLayout.setLayoutParams(layoutParams);
        }
    }

    private void moveRightView(int width) {
        XLog.d(TAG + " moveRightView width=" + width);
        if (null != mRightDragView && mRightDragContentLayout != null) {
            LayoutParams layoutParams = (LayoutParams) mRightDragContentLayout.getLayoutParams();
            if (getWidth() - width <= 0) {
                layoutParams.width = 0;
            } else {
                layoutParams.width = getWidth() - width;
            }
            mRightDragContentLayout.setLayoutParams(layoutParams);
        }
    }

    private void moveDistance(int width) {

    }

    public void moveLeft() {
        mWidth = 0;
        moveDistance(0);
        if (null != mDragListener) {
            mDragListener.onDragStateChanged(DragState.DRAG_STATE_TO_LEFT);
        }
    }

    public void moveTopView(int height) {
        XLog.d(TAG + " moveTopView height=" + height);
        if (null != mTopDragView && mTopDragContentLayout != null) {
            LayoutParams layoutParams = (LayoutParams) mTopDragContentLayout.getLayoutParams();
            layoutParams.height = height;
            mTopDragContentLayout.setLayoutParams(layoutParams);
        }
    }

    public void moveBottomView(int height) {
        XLog.d(TAG + " moveBottomView height=" + height);
        if (null != mBottomDragView && mBottomDragContentLayout != null) {
            LayoutParams layoutParams = (LayoutParams) mBottomDragContentLayout.getLayoutParams();
            layoutParams.height = getHeight() - height;
            mBottomDragContentLayout.setLayoutParams(layoutParams);
        }
    }

    public void moveRight() {
        mWidth = mScreenWidth;
        moveDistance(mScreenWidth);
        if (null != mDragListener) {
            mDragListener.onDragStateChanged(DragState.DRAG_STATE_TO_RIGHT);
        }
    }

    public void moveCenter() {
        mWidth = mScreenWidth / 2;
        moveDistance(mWidth);
    }

    public void moveThird() {
        mWidth = mScreenWidth / 3;
        moveDistance(mWidth);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        XLog.d(TAG + " onMeasure");
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(sizeWidth, sizeHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        XLog.d(TAG + " onLayout");

        int childCount = getChildCount();
        XLog.d(TAG + " childCount=" + childCount);

        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            int measureWidth = childView.getMeasuredWidth();
            int measureHeight = childView.getMeasuredHeight();
            if (i == 4) {
                XLog.d(TAG + " measureWidth=" + measureWidth + " measureHeight=" + measureHeight);
                XLog.d(TAG + " mLeftWidth=" + mLeftWidth + " measureWidth + mLeftWidth=" + (measureWidth + mLeftWidth));
                if (mLeftWidth <= 0) {
                    childView.layout(0, 0, measureWidth, measureHeight);
                } else {
                    childView.layout(mLeftWidth, 0, measureWidth + mLeftWidth, measureHeight);
                }
            }
            if (i == 5) {
                XLog.d(TAG + " measureWidth=" + measureWidth + " measureHeight=" + measureHeight);
                if (mRightWidth - measureWidth <= 0) {
                    childView.layout(0, 0, mRightWidth, measureHeight);
                } else {
                    XLog.d("mRightWidth - measureWidth="+(mRightWidth - measureWidth) +" screenWidth="+mScreenWidth);
                    if (mRightWidth>=mScreenWidth){
                        mRightWidth = mScreenWidth;
                    }
                    childView.layout(mRightWidth - measureWidth, 0, mRightWidth, measureHeight);
                }
            }
            if (i == 6) {
                XLog.d(TAG + " measureWidth=" + measureWidth + " measureHeight=" + measureHeight);
                childView.layout(0, mTopHeight, mScreenWidth, mTopHeight + measureHeight);
            }
            if (i == 7) {
                XLog.d(TAG + " measureWidth=" + measureWidth + " measureHeight=" + measureHeight);
                childView.layout(0, getHeight() - measureHeight, mScreenWidth, getHeight());
            }
        }
    }

    /**
     * 拖拽监听
     */
    interface DragStateListener {
        // 当状态变更时监听
        void onDragStateChanged(DragState dragState);
    }

    /**
     * 滑动状态
     */
    enum DragState {
        // 滑动到左边
        DRAG_STATE_TO_LEFT,
        // 滑动到右边
        DRAG_STATE_TO_RIGHT,
        // 滑动到顶部
        DRAG_STATE_TO_TOP,
        // 滑动到底部
        DRAG_STATE_TO_BOTTOM,
        // 左边打开
        DRAG_STATE_LEFT_OPEN,
        // 左边关闭
        DRAG_STATE_LEFT_CLOSE,
    }
}
