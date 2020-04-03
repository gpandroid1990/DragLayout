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
public class DragLayout extends FrameLayout implements View.OnTouchListener {
    private static final String TAG = DragLayout.class.getSimpleName();
    private FrameLayout mLeftDragContentLayout;
    private FrameLayout mRightDragContentLayout;
    private FrameLayout mTopDragContentLayout;
    private FrameLayout mBottomDragContentLayout;
    private LinearLayout mLeftDragView;
    private LinearLayout mRightDragView;
    private LinearLayout mTopDragView;
    private LinearLayout mBottomDragView;

    private int mWidth = 0;
    private int mLeftWidth = 0;
    private int mRightWidth = 0;
    private int mTopHeight = 0;
    private int mBottomHeight = 0;
    private int mLastX = -1;
    private int mLastY = -1;
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
            mLeftDragView.setOnTouchListener(this);
        }
    }

    /**
     * 初始化右边拖拽事件
     */
    private void initRightDragview() {
        if (null != mRightDragView) {
            mRightDragView.setOnTouchListener(this);
        }
    }

    /**
     * 初始化顶部拖拽view
     */
    private void initTopDragView() {
        if (null != mTopDragView) {
            mTopDragView.setOnTouchListener(this);
        }
    }

    /**
     * 初始化底部拖拽view
     */
    private void initBottomDragView() {
        if (null != mBottomDragView) {
            mBottomDragView.setOnTouchListener(this);
        }
    }

    private void moveLeftView(int width) {
        XLog.d(TAG + " moveLeftView width=" + width);
        if (null != mLeftDragView && mLeftDragContentLayout != null) {
            LayoutParams layoutParams = (LayoutParams) mLeftDragContentLayout.getLayoutParams();
            if (width <= 0) {
                layoutParams.width = 0;
            } else if (mLeftWidth > getWidth()) {
                layoutParams.width = getWidth();
            } else {
                layoutParams.width = mLeftWidth;
            }
            mLeftDragContentLayout.setLayoutParams(layoutParams);
        }
    }

    private void moveRightView(int width) {
        XLog.d(TAG + " moveRightView width=" + width);
        if (null != mRightDragView && mRightDragContentLayout != null) {
            LayoutParams layoutParams = (LayoutParams) mRightDragContentLayout.getLayoutParams();
            if (width <= 0) {
                layoutParams.width = 0;
            } else if (width > getWidth()) {
                layoutParams.width = getWidth();
            } else {
                layoutParams.width = width;
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
            if (height <= 0) {
                layoutParams.height = 0;
            } else if (height > getHeight()) {
                layoutParams.height = getHeight();
            } else {
                layoutParams.height = height;
            }
            mTopDragContentLayout.setLayoutParams(layoutParams);
        }
    }

    public void moveBottomView(int height) {
        XLog.d(TAG + " moveBottomView height=" + height + " getHeight()=" + getHeight());
        if (null != mBottomDragView && mBottomDragContentLayout != null) {
            LayoutParams layoutParams = (LayoutParams) mBottomDragContentLayout.getLayoutParams();
            if (height <= 0) {
                layoutParams.height = 0;
            } else if (height > getHeight()) {
                layoutParams.height = getHeight();
            } else {
                layoutParams.height = height;
            }
            mBottomDragContentLayout.setLayoutParams(layoutParams);
        }
    }

    public void moveRight() {
        mWidth = getWidth();
        moveDistance(mWidth);
        if (null != mDragListener) {
            mDragListener.onDragStateChanged(DragState.DRAG_STATE_TO_RIGHT);
        }
    }

    public void moveCenter() {
        mWidth = getWidth() / 2;
        moveDistance(mWidth);
    }

    public void moveThird() {
        mWidth = getWidth() / 3;
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
                if (mLeftWidth <= 0) {
                    childView.layout(0, 0, measureWidth, measureHeight);
                } else if (mLeftWidth > getWidth()) {
                    childView.layout(getWidth(), 0, measureWidth + getWidth(), measureHeight);
                } else {
                    childView.layout(mLeftWidth, 0, measureWidth + mLeftWidth, measureHeight);
                }
            }
            if (i == 5) {
                if (mRightWidth <= 0) {
                    childView.layout(getWidth() - measureWidth, 0, getWidth(), measureHeight);
                } else if (mRightWidth > getWidth()) {
                    childView.layout(-measureWidth, 0, 0, measureHeight);
                } else {
                    childView.layout(getWidth() - mRightWidth - measureWidth, 0, getWidth() - mRightWidth, measureHeight);
                }
            }
            if (i == 6) {
                if (mTopHeight <= 0) {
                    childView.layout(0, 0, getWidth(), measureHeight);
                } else if (mTopHeight > getHeight()) {
                    childView.layout(0, getHeight(), getWidth(), getHeight() + measureHeight);
                } else {
                    childView.layout(0, mTopHeight, getWidth(), mTopHeight + measureHeight);
                }
            }
            if (i == 7) {
                if (mBottomHeight <= 0) {
                    childView.layout(0, getHeight() - measureHeight, getWidth(), getHeight());
                } else if (mBottomHeight > getHeight()) {
                    childView.layout(0, -measureHeight, getWidth(), 0);
                } else {
                    childView.layout(0, getHeight() - measureHeight - mBottomHeight, getWidth(), getHeight() - mBottomHeight);
                }
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v == mLeftDragView) {
            handleLeftDragViewTouchEvent(event);
        }
        if (v == mRightDragView) {
            handleRightDragViewTouchEvent(event);
        }
        if (v == mTopDragView) {
            handleTopDragViewTouchEvent(event);
        }
        if (v == mBottomDragView) {
            handleBottomDragViewTouchEvent(event);
        }
        return true;
    }

    /**
     * 处理左边拖动按钮事件
     *
     * @param event
     */
    private void handleLeftDragViewTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = (int) event.getRawX();
                break;

            case MotionEvent.ACTION_MOVE:
                int rawX = (int) event.getRawX();
                XLog.d(TAG + " rawX=" + rawX);
                int distance = rawX - mLastX;
                XLog.d(TAG + "getTop()=" + getTop() + " getBottom()=" + getBottom() + " distance=" + distance);
                mLeftWidth += distance;
                moveLeftView(mLeftWidth);
                mLastX = rawX;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:

                break;
        }
    }

    /**
     * 处理右边拖动按钮事件
     *
     * @param event
     */
    private void handleRightDragViewTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = (int) event.getRawX();
                break;

            case MotionEvent.ACTION_MOVE:
                int rawX = (int) event.getRawX();
                XLog.d(TAG + " rawX=" + rawX);
                int distance = mLastX - rawX;
                XLog.d(TAG + "getTop()=" + getTop() + " getBottom()=" + getBottom() + " distance=" + distance);
                mRightWidth += distance;
                moveRightView(mRightWidth);
                mLastX = rawX;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:

                break;
        }
    }

    /**
     * 处理顶部拖动事件
     *
     * @param event
     */
    private void handleTopDragViewTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = (int) event.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:
                int rawY = (int) event.getRawY();
                XLog.d(TAG + " rawY=" + rawY);
                int distance = rawY - mLastY;
                XLog.d(TAG + "getTop()=" + getTop() + " getBottom()=" + getBottom() + " distance=" + distance);
                mTopHeight += distance;
                moveTopView(mTopHeight);
                mLastY = rawY;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:

                break;
        }
    }

    /**
     * 处理底部按钮拖动事件
     *
     * @param event
     */
    private void handleBottomDragViewTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = (int) event.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:
                int rawY = (int) event.getRawY();
                XLog.d(TAG + " rawY=" + rawY);
                int distance = mLastY - rawY;
                XLog.d(TAG + "getTop()=" + getTop() + " getBottom()=" + getBottom() + " distance=" + distance);
                mBottomHeight += distance;
                moveBottomView(mBottomHeight);
                mLastY = rawY;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:

                break;
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
