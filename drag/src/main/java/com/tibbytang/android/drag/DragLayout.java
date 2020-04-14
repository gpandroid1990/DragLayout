package com.tibbytang.android.drag;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

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
    private View mContentViewGrop;
    private View mDragView;
    private int mLeftWidth = 0;
    private int mRightWidth = 0;
    private int mTopHeight = 0;
    private int mBottomHeight = 0;
    private int mLastX = -1;
    private int mLastY = -1;
    // 判断面板是否打开
    private boolean mIsOpen = false;
    // 判断是否滑动到边缘
    private boolean mIsScrollToEdge = false;
    private int mDownX = 0;
    private int mDownY = 0;
    private int mMoveX = 0;
    private int mMoveY = 0;
    private long mMoveTime = 0;
    private int mLeftDragViewWidth = 0;
    private int mRightDragViewWidth = 0;
    private int mTopDragViewHeight = 0;
    private int mBottomDragViewHeight = 0;
    // 拖拽方向 0 为left,1 为right 2 为top 3 为bottom
    private int mDragDirection = 0;
    // 最小移动距离 判断是否打开
    private static final int MIN_OPEN_DISTANCE = 2;
    // 判断单击还是move
    private static final long TAP_TIME_OUT = 500L;
    // 移动距离
    private static final int MOVE_DISTANCE = 10;
    // 是否允许拖拽 默认可以拖拽
    private boolean mEnableDrag = true;
    private static final int TIME = 500;
    private static long currentTime = 0L;
    private static int count = 0;

    private DragStateListener mDragListener;

    public DragLayout(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public DragLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DragLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void addDragStateListener(DragStateListener dragListener) {
        mDragListener = dragListener;
    }

    private void init(Context context, AttributeSet attrs) {
        if (null != attrs) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.DragLayout);
            mDragDirection = array.getInt(R.styleable.DragLayout_drag_direction, 0);
        }
    }

    private boolean isNotFastClick() {
        if (Math.abs((System.currentTimeMillis() - currentTime)) > TIME) {
            count = 0;
            currentTime = System.currentTimeMillis();
            XLog.d("isNotFastClick true");
            return true;
        }
        if (count == 0) {
            currentTime = System.currentTimeMillis();
            count++;
        }
        XLog.d("isNotFastClick false");
        return false;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        XLog.d("onFinishInflate");
        if (getChildCount() == 2) {
            mContentViewGrop = getChildAt(0);
            LayoutParams layoutParams = (LayoutParams) mContentViewGrop.getLayoutParams();
            if (mDragDirection == 0 || mDragDirection == 1) {
                layoutParams.width = 0;
            } else {
                layoutParams.height = 0;
            }
            mContentViewGrop.setLayoutParams(layoutParams);
            mDragView = getChildAt(1);
            initDragView();
        }
    }

    private void initDragView() {
        if (null != mDragView) {
            mDragView.setOnTouchListener(this);
        }
    }

    /**
     * 设置是否允许拖拽 默认为true
     *
     * @param enableDrag
     */
    public void setEnableDrag(boolean enableDrag) {
        this.mEnableDrag = enableDrag;
    }

    private void moveLeftView(int width) {
        XLog.d(TAG + " moveLeftView width=" + width);
        if (null != mDragView && mContentViewGrop != null) {
            LayoutParams layoutParams = (LayoutParams) mContentViewGrop.getLayoutParams();
            if (width <= 0) {
                layoutParams.width = 0;
            } else if (mLeftWidth >= getWidth() - mLeftDragViewWidth) {
                layoutParams.width = getWidth() - mLeftDragViewWidth;
            } else {
                layoutParams.width = mLeftWidth;
            }
            mContentViewGrop.setLayoutParams(layoutParams);
            // 判断是否拖拽到边缘
            if (mLeftWidth >= (getWidth() - mLeftDragViewWidth)) {
                if (null != mDragListener && !mIsScrollToEdge) {
                    mIsScrollToEdge = true;
                    mDragListener.onDragEdgeChanged(DragEdgeState.DRAG_STATE_TO_EDGE);
                }
            } else {
                mIsScrollToEdge = false;
            }
            // 判断是否打开
            if (mLeftWidth > MIN_OPEN_DISTANCE && !mIsOpen && null != mDragListener) {
                mIsOpen = true;
                mDragListener.onDragStateChanged(DragState.DRAG_STATE_OPEN);
            }
            // 判断是否关闭
            if (mLeftWidth <= 0 && mIsOpen && null != mDragListener) {
                mIsOpen = false;
                mDragListener.onDragStateChanged(DragState.DRAG_STATE_CLOSE);
            }
        }
    }

    private void moveRightView(int width) {
        XLog.d(TAG + " moveRightView width=" + width);
        if (null != mDragView && mContentViewGrop != null) {
            LayoutParams layoutParams = (LayoutParams) mContentViewGrop.getLayoutParams();
            if (width <= 0) {
                layoutParams.width = 0;
            } else if (width >= getWidth() - mRightDragViewWidth) {
                layoutParams.width = getWidth() - mRightDragViewWidth;
            } else {
                layoutParams.width = width;
            }
            mContentViewGrop.setLayoutParams(layoutParams);

            // 判断是否拖拽到边缘
            if (mRightWidth >= (getWidth() - mRightDragViewWidth)) {
                if (null != mDragListener && !mIsScrollToEdge) {
                    mIsScrollToEdge = true;
                    mDragListener.onDragEdgeChanged(DragEdgeState.DRAG_STATE_TO_EDGE);
                }
            } else {
                mIsScrollToEdge = false;
            }
            // 判断是否打开
            if (mRightWidth > MIN_OPEN_DISTANCE && !mIsOpen && null != mDragListener) {
                mIsOpen = true;
                mDragListener.onDragStateChanged(DragState.DRAG_STATE_OPEN);
            }
            // 判断是否关闭
            if (mRightWidth <= 0 && mIsOpen && null != mDragListener) {
                mIsOpen = false;
                mDragListener.onDragStateChanged(DragState.DRAG_STATE_CLOSE);
            }
        }
    }


    private void moveTopView(int height) {
        XLog.d(TAG + " moveTopView height=" + height);
        if (null != mDragView && mContentViewGrop != null) {
            LayoutParams layoutParams = (LayoutParams) mContentViewGrop.getLayoutParams();
            if (height <= 0) {
                layoutParams.height = 0;
            } else if (height > getHeight() - mTopDragViewHeight) {
                layoutParams.height = getHeight() - mTopDragViewHeight;
                if (null != mDragListener && !mIsScrollToEdge) {
                    mIsScrollToEdge = true;
                    mDragListener.onDragEdgeChanged(DragEdgeState.DRAG_STATE_TO_EDGE);
                }
            } else {
                layoutParams.height = height;
            }
            mContentViewGrop.setLayoutParams(layoutParams);
            // 判断是否拖拽到边缘
            if (mTopHeight >= (getHeight() - mTopDragViewHeight)) {
                if (null != mDragListener && !mIsScrollToEdge) {
                    mIsScrollToEdge = true;
                    mDragListener.onDragEdgeChanged(DragEdgeState.DRAG_STATE_TO_EDGE);
                }
            } else {
                mIsScrollToEdge = false;
            }
            // 判断是否打开
            if (mTopHeight > MIN_OPEN_DISTANCE && !mIsOpen && null != mDragListener) {
                mIsOpen = true;
                mDragListener.onDragStateChanged(DragState.DRAG_STATE_OPEN);
            }
            // 判断是否关闭
            if (mTopHeight <= 0 && mIsOpen && null != mDragListener) {
                mIsOpen = false;
                mDragListener.onDragStateChanged(DragState.DRAG_STATE_CLOSE);
            }
        }
    }

    private void moveBottomView(int height) {
        XLog.d(TAG + " moveBottomView height=" + height + " getHeight()=" + getHeight());
        if (null != mDragView && mContentViewGrop != null) {
            LayoutParams layoutParams = (LayoutParams) mContentViewGrop.getLayoutParams();
            if (height <= 0) {
                layoutParams.height = 0;
            } else if (height > getHeight() - mBottomDragViewHeight) {
                layoutParams.height = getHeight() - mBottomDragViewHeight;
                if (null != mDragListener && !mIsScrollToEdge) {
                    mIsScrollToEdge = true;
                    mDragListener.onDragEdgeChanged(DragEdgeState.DRAG_STATE_TO_EDGE);
                }
            } else {
                layoutParams.height = height;
            }
            mContentViewGrop.setLayoutParams(layoutParams);
            // 判断是否拖拽到边缘
            if (mBottomHeight >= (getHeight() - mBottomDragViewHeight)) {
                if (null != mDragListener && !mIsScrollToEdge) {
                    mIsScrollToEdge = true;
                    mDragListener.onDragEdgeChanged(DragEdgeState.DRAG_STATE_TO_EDGE);
                }
            } else {
                mIsScrollToEdge = false;
            }
            // 判断是否打开
            if (mBottomHeight > MIN_OPEN_DISTANCE && !mIsOpen && null != mDragListener) {
                mIsOpen = true;
                mDragListener.onDragStateChanged(DragState.DRAG_STATE_OPEN);
            }
            // 判断是否关闭
            if (mBottomHeight <= 0 && mIsOpen && null != mDragListener) {
                mIsOpen = false;
                mDragListener.onDragStateChanged(DragState.DRAG_STATE_CLOSE);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        XLog.d(TAG + " onMeasure");
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        XLog.d("sizeWidth=" + sizeWidth + " sizeHeight=" + sizeHeight);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(sizeWidth, sizeHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        XLog.d(TAG + " onLayout");
        int childCount = getChildCount();
        XLog.d(TAG + " childCount=" + childCount);
        if (childCount == 2) {
            for (int i = 0; i < childCount; i++) {
                View childView = getChildAt(i);
                int measureWidth = childView.getMeasuredWidth();
                int measureHeight = childView.getMeasuredHeight();
                if (i == 1) {
                    XLog.d("measureWidth=" + measureWidth + " measureHeight=" + measureHeight);
                    if (mDragDirection == 0) {
                        if (mLeftWidth <= 0) {
                            childView.layout(0, (getHeight() - measureHeight) / 2, measureWidth, getHeight() / 2 + measureHeight / 2);
                        } else if (mLeftWidth >= getWidth() - measureWidth) {
                            childView.layout(getWidth() - measureWidth, (getHeight() - measureHeight) / 2, getWidth(), getHeight() / 2 + measureHeight / 2);
                        } else {
                            childView.layout(mLeftWidth, (getHeight() - measureHeight) / 2, measureWidth + mLeftWidth, getHeight() / 2 + measureHeight / 2);
                        }
                    }
                    if (mDragDirection == 1) {
                        if (mRightWidth <= 0) {
                            childView.layout(getWidth() - measureWidth, (getHeight() - measureHeight) / 2, getWidth(), getHeight() / 2 + measureHeight / 2);
                        } else if (mRightWidth >= getWidth() - mRightDragViewWidth) {
                            childView.layout(0, (getHeight() - measureHeight) / 2, measureWidth, getHeight() / 2 + measureHeight / 2);
                        } else {
                            childView.layout(getWidth() - mRightWidth - measureWidth, (getHeight() - measureHeight) / 2, getWidth() - mRightWidth, getHeight() / 2 + measureHeight / 2);
                        }
                    }
                    if (mDragDirection == 2) {
                        if (mTopHeight <= 0) {
                            childView.layout((getWidth() - measureWidth) / 2, 0, getWidth() / 2 + measureWidth / 2, measureHeight);
                        } else if (mTopHeight >= getHeight() - mTopDragViewHeight) {
                            childView.layout((getWidth() - measureWidth) / 2, getHeight() - measureHeight, getWidth() / 2 + measureWidth / 2, getHeight());
                        } else {
                            childView.layout((getWidth() - measureWidth) / 2, mTopHeight, getWidth() / 2 + measureWidth / 2, mTopHeight + measureHeight);
                        }
                    }
                    if (mDragDirection == 3) {
                        if (mBottomHeight <= 0) {
                            childView.layout((getWidth() - measureWidth) / 2, getHeight() - measureHeight, getWidth() / 2 + measureWidth / 2, getHeight());
                        } else if (mBottomHeight >= getHeight() - mBottomDragViewHeight) {
                            childView.layout((getWidth() - measureWidth) / 2, 0, getWidth() / 2 + measureWidth / 2, measureHeight);
                        } else {
                            childView.layout((getWidth() - measureWidth) / 2, getHeight() - measureHeight - mBottomHeight, getWidth() / 2 + measureWidth / 2, getHeight() - mBottomHeight);
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        XLog.d("onSizeChanged w=" + w + " h=" + h + " oldw=" + oldw + " oldh=" + oldh);
        if (oldw > 0 && oldh > 0) {
            if (mDragDirection == 0) {
                if (mLeftWidth > 0) {
                    mLeftWidth = (int) ((float) w / (float) oldw * mLeftWidth);
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            moveLeftView(mLeftWidth);
                        }
                    },100);
                }
            }
            if (mDragDirection == 1) {
                if (mRightWidth > 0) {
                    mRightWidth = (int) ((float) w / (float) oldw * mRightWidth);
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            moveRightView(mRightWidth);
                        }
                    },100);
                }
            }

            if (mDragDirection == 2) {
                if (mTopHeight > 0) {
                    mTopHeight = (int) ((float) h / (float) oldh * mTopHeight);
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            moveTopView(mTopHeight);
                        }
                    },100);
                }
            }
            if (mDragDirection == 3) {
                if (mBottomHeight > 0) {
                    mBottomHeight = (int) ((float) h / (float) oldh * mBottomHeight);
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            moveBottomView(mBottomHeight);
                        }
                    },100);
                }
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (mDragDirection == 0 && getChildCount() == 2) {
            handleLeftDragViewTouchEvent(v, event);
        }
        if (mDragDirection == 1 && getChildCount() == 2) {
            handleRightDragViewTouchEvent(v, event);
        }
        if (mDragDirection == 2 && getChildCount() == 2) {
            handleTopDragViewTouchEvent(v, event);
        }
        if (mDragDirection == 3 && getChildCount() == 2) {
            handleBottomDragViewTouchEvent(v, event);
        }
        return true;
    }

    /**
     * 处理左边拖动按钮事件
     *
     * @param event
     */
    private void handleLeftDragViewTouchEvent(View view, MotionEvent event) {
        mLeftDragViewWidth = view.getWidth();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = (int) event.getRawX();
                mDownX = (int) event.getX();
                mDownY = (int) event.getY();
                mMoveX = 0;
                mMoveY = 0;
                mMoveTime = System.currentTimeMillis();
                XLog.d(TAG + "按下 time=" + mMoveTime);
                break;
            case MotionEvent.ACTION_MOVE:
                int rawX = (int) event.getRawX();
                XLog.d(TAG + " rawX=" + rawX);
                int distance = rawX - mLastX;
                XLog.d(TAG + "getTop()=" + getTop() + " getBottom()=" + getBottom() + " distance=" + distance);
                mLeftWidth += distance;
                if (mEnableDrag) {
                    moveLeftView(mLeftWidth);
                }
                mLastX = rawX;
                mMoveX += Math.abs(event.getX() - mDownX);
                mMoveY += Math.abs(event.getY() - mDownY);
                mDownX = (int) event.getX();
                mDownY = (int) event.getY();
                break;
            case MotionEvent.ACTION_UP:
                long time = System.currentTimeMillis();
                XLog.d(TAG + "抬起 time=" + time);
                mMoveTime = time - mMoveTime;
                XLog.d(TAG + "抬起 mMoveTime=" + mMoveTime + " TAP_TIME_OUT=" + TAP_TIME_OUT + " mMoveX=" + mMoveX + " mMoveY=" + mMoveY);
                if (mMoveTime < TAP_TIME_OUT && mMoveX < MOVE_DISTANCE && mMoveY < MOVE_DISTANCE) {
                    // 点击事件
                    if (isNotFastClick()) {
                        mDragListener.onDragViewClick(view, mIsOpen);
                    }
                }
                break;
        }
    }

    /**
     * 处理右边拖动按钮事件
     *
     * @param event
     */
    private void handleRightDragViewTouchEvent(View view, MotionEvent event) {
        mRightDragViewWidth = view.getWidth();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = (int) event.getRawX();
                mDownX = (int) event.getX();
                mDownY = (int) event.getY();
                mMoveX = 0;
                mMoveY = 0;
                mMoveTime = System.currentTimeMillis();
                break;

            case MotionEvent.ACTION_MOVE:
                int rawX = (int) event.getRawX();
                XLog.d(TAG + " rawX=" + rawX);
                int distance = mLastX - rawX;
                XLog.d(TAG + "getTop()=" + getTop() + " getBottom()=" + getBottom() + " distance=" + distance);
                mRightWidth += distance;
                moveRightView(mRightWidth);
                mLastX = rawX;
                mMoveX += Math.abs(event.getX() - mDownX);
                mMoveY += Math.abs(event.getY() - mDownY);
                mDownX = (int) event.getX();
                mDownY = (int) event.getY();
                break;
            case MotionEvent.ACTION_UP:
                long time = System.currentTimeMillis();
                XLog.d(TAG + "抬起 time=" + time);
                mMoveTime = time - mMoveTime;
                XLog.d(TAG + "抬起 mMoveTime=" + mMoveTime + " TAP_TIME_OUT=" + TAP_TIME_OUT + " mMoveX=" + mMoveX + " mMoveY=" + mMoveY);
                if (mMoveTime < TAP_TIME_OUT && mMoveX < MOVE_DISTANCE && mMoveY < MOVE_DISTANCE) {
                    // 点击事件
                    if (isNotFastClick()) {
                        mDragListener.onDragViewClick(view, mIsOpen);
                    }
                }
                break;
        }
    }

    /**
     * 处理顶部拖动事件
     *
     * @param event
     */
    private void handleTopDragViewTouchEvent(View view, MotionEvent event) {
        mTopDragViewHeight = view.getHeight();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = (int) event.getRawY();
                mDownX = (int) event.getX();
                mDownY = (int) event.getY();
                mMoveX = 0;
                mMoveY = 0;
                mMoveTime = System.currentTimeMillis();
                break;

            case MotionEvent.ACTION_MOVE:
                int rawY = (int) event.getRawY();
                XLog.d(TAG + " rawY=" + rawY);
                int distance = rawY - mLastY;
                XLog.d(TAG + "getTop()=" + getTop() + " getBottom()=" + getBottom() + " distance=" + distance);
                mTopHeight += distance;
                moveTopView(mTopHeight);
                mLastY = rawY;
                mMoveX += Math.abs(event.getX() - mDownX);
                mMoveY += Math.abs(event.getY() - mDownY);
                mDownX = (int) event.getX();
                mDownY = (int) event.getY();
                break;
            case MotionEvent.ACTION_UP:
                long time = System.currentTimeMillis();
                XLog.d(TAG + "抬起 time=" + time);
                mMoveTime = time - mMoveTime;
                XLog.d(TAG + "抬起 mMoveTime=" + mMoveTime + " TAP_TIME_OUT=" + TAP_TIME_OUT + " mMoveX=" + mMoveX + " mMoveY=" + mMoveY);
                if (mMoveTime < TAP_TIME_OUT && mMoveX < MOVE_DISTANCE && mMoveY < MOVE_DISTANCE) {
                    // 点击事件
                    if (isNotFastClick()) {
                        mDragListener.onDragViewClick(view, mIsOpen);
                    }
                }
                break;
        }
    }

    /**
     * 处理底部按钮拖动事件
     *
     * @param event
     */
    private void handleBottomDragViewTouchEvent(View view, MotionEvent event) {
        mBottomDragViewHeight = view.getHeight();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = (int) event.getRawY();
                mDownX = (int) event.getX();
                mDownY = (int) event.getY();
                mMoveX = 0;
                mMoveY = 0;
                mMoveTime = System.currentTimeMillis();
                break;

            case MotionEvent.ACTION_MOVE:
                int rawY = (int) event.getRawY();
                XLog.d(TAG + " rawY=" + rawY);
                int distance = mLastY - rawY;
                XLog.d(TAG + "getTop()=" + getTop() + " getBottom()=" + getBottom() + " distance=" + distance);
                mBottomHeight += distance;
                moveBottomView(mBottomHeight);
                mLastY = rawY;
                mMoveX += Math.abs(event.getX() - mDownX);
                mMoveY += Math.abs(event.getY() - mDownY);
                mDownX = (int) event.getX();
                mDownY = (int) event.getY();
                break;
            case MotionEvent.ACTION_UP:
                long time = System.currentTimeMillis();
                XLog.d(TAG + "抬起 time=" + time);
                mMoveTime = time - mMoveTime;
                XLog.d(TAG + "抬起 mMoveTime=" + mMoveTime + " TAP_TIME_OUT=" + TAP_TIME_OUT + " mMoveX=" + mMoveX + " mMoveY=" + mMoveY);
                if (mMoveTime < TAP_TIME_OUT && mMoveX < MOVE_DISTANCE && mMoveY < MOVE_DISTANCE) {
                    // 点击事件
                    if (isNotFastClick()) {
                        mDragListener.onDragViewClick(view, mIsOpen);
                    }
                }
                break;
        }
    }

    /**
     * 根据比例打开面板
     *
     * @param ratio
     */
    public void openPanelWithRatio(final float ratio) {
        int width = getWidth();
        if (width == 0) {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    moveView(ratio);
                }
            }, 50);
        } else {
            moveView(ratio);
        }
    }

    private void moveView(final float ratio) {
        switch (mDragDirection) {
            case 0:
                moveLeftViewWithRatio(ratio);
                break;
            case 1:
                moveRightViewWithRatio(ratio);
                break;
            case 2:
                moveTopViewWithRatio(ratio);
                break;
            case 3:
                moveBottomViewWithRatio(ratio);
                break;
            default:
                break;
        }
    }

    /**
     * 关闭面板
     */
    public void closePanel() {
        switch (mDragDirection) {
            case 0:
                moveLeftViewWithRatio(0.0f);
                break;
            case 1:
                moveRightViewWithRatio(0.0f);
                break;
            case 2:
                moveTopViewWithRatio(0.0f);
                break;
            case 3:
                moveBottomViewWithRatio(0.0f);
                break;
            default:
                break;
        }
    }

    /**
     * 拖动左边面板到响应比例位置处
     */
    private void moveLeftViewWithRatio(float ratio) {
        mLeftWidth = (int) (getWidth() * ratio);
        moveLeftView(mLeftWidth);
    }

    /**
     * 拖动右边面板到响应比例位置处
     */
    private void moveRightViewWithRatio(float ratio) {
        mRightWidth = (int) (getWidth() * ratio);
        moveRightView(mRightWidth);
    }

    /**
     * 拖动顶部面板到响应比例位置处
     */
    private void moveTopViewWithRatio(float ratio) {
        mTopHeight = (int) (getHeight() * ratio);
        moveTopView(mTopHeight);
    }

    /**
     * 拖动顶部面板到响应比例位置处
     */
    private void moveBottomViewWithRatio(float ratio) {
        mBottomHeight = (int) (getHeight() * ratio);
        moveBottomView(mBottomHeight);
    }

    /**
     * 判断面板是否打开 默认为false
     *
     * @return
     */
    public boolean isOpen() {
        return mIsOpen;
    }

    /**
     * 拖拽事件监听器
     */
    public interface DragStateListener {
        /**
         * 当状态变更时监听
         *
         * @param dragState
         */
        void onDragStateChanged(DragState dragState);

        /**
         * 拖拽到边缘监听
         *
         * @param dragEdgeState
         */
        void onDragEdgeChanged(DragEdgeState dragEdgeState);

        /**
         * @param isOpen 面板是否打开
         * @param view   几点的view
         */
        void onDragViewClick(View view, boolean isOpen);
    }

    /**
     * 滑动状态
     */
    public enum DragEdgeState {
        // 滑动到边缘
        DRAG_STATE_TO_EDGE,
    }

    /**
     * 打开关闭状态
     */
    public enum DragState {
        // 左边面板打开
        DRAG_STATE_OPEN,
        // 左边面板关闭
        DRAG_STATE_CLOSE,
    }
}
