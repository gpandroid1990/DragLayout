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
    private boolean mIsScrollToLeft = false;
    private boolean mIsScrollToRight = false;
    private boolean mIsScrollToTop = false;
    private boolean mIsScrollToBottom = false;
    private boolean mIsLeftOpen = false;
    private boolean mIsRightOpen = false;
    private boolean mIsTopOpen = false;
    private boolean mIsBottomOpen = false;
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
        mContentViewGrop = getChildAt(0);
        mDragView = getChildAt(1);
        initDragView();
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
                if (null != mDragListener && !mIsScrollToRight) {
                    mIsScrollToRight = true;
                    mDragListener.onDragEdgeChanged(DragEdgeState.DRAG_STATE_LEFT_TO_RIGHT_EDGE);
                }
            } else {
                mIsScrollToRight = false;
            }
            // 判断是否打开
            if (mLeftWidth > MIN_OPEN_DISTANCE && !mIsLeftOpen && null != mDragListener) {
                mIsLeftOpen = true;
                mDragListener.onDragStateChanged(DragState.DRAG_STATE_LEFT_OPEN);
            }
            // 判断是否关闭
            if (mLeftWidth <= 0 && mIsLeftOpen && null != mDragListener) {
                mIsLeftOpen = false;
                mDragListener.onDragStateChanged(DragState.DRAG_STATE_LEFT_CLOSE);
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
                if (null != mDragListener && !mIsScrollToLeft) {
                    mIsScrollToLeft = true;
                    mDragListener.onDragEdgeChanged(DragEdgeState.DRAG_STATE_RIGHT_TO_LEFT_EDGE);
                }
            } else {
                layoutParams.width = width;
            }
            mContentViewGrop.setLayoutParams(layoutParams);

            // 判断是否拖拽到边缘
            if (mRightWidth >= (getWidth() - mRightDragViewWidth)) {
                if (null != mDragListener && !mIsScrollToLeft) {
                    mIsScrollToLeft = true;
                    mDragListener.onDragEdgeChanged(DragEdgeState.DRAG_STATE_RIGHT_TO_LEFT_EDGE);
                }
            } else {
                mIsScrollToLeft = false;
            }
            // 判断是否打开
            if (mRightWidth > MIN_OPEN_DISTANCE && !mIsRightOpen && null != mDragListener) {
                mIsRightOpen = true;
                mDragListener.onDragStateChanged(DragState.DRAG_STATE_RIGHT_OPEN);
            }
            // 判断是否关闭
            if (mRightWidth <= 0 && mIsRightOpen && null != mDragListener) {
                mIsRightOpen = false;
                mDragListener.onDragStateChanged(DragState.DRAG_STATE_RIGHT_CLOSE);
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
                if (null != mDragListener && !mIsScrollToBottom) {
                    mIsScrollToBottom = true;
                    mDragListener.onDragEdgeChanged(DragEdgeState.DRAG_STATE_TOP_TO_BOTTOM_EDGE);
                }
            } else {
                layoutParams.height = height;
            }
            mContentViewGrop.setLayoutParams(layoutParams);
            // 判断是否拖拽到边缘
            if (mTopHeight >= (getHeight() - mTopDragViewHeight)) {
                if (null != mDragListener && !mIsScrollToTop) {
                    mIsScrollToTop = true;
                    mDragListener.onDragEdgeChanged(DragEdgeState.DRAG_STATE_TOP_TO_BOTTOM_EDGE);
                }
            } else {
                mIsScrollToTop = false;
            }
            // 判断是否打开
            if (mTopHeight > MIN_OPEN_DISTANCE && !mIsTopOpen && null != mDragListener) {
                mIsTopOpen = true;
                mDragListener.onDragStateChanged(DragState.DRAG_STATE_TOP_OPEN);
            }
            // 判断是否关闭
            if (mTopHeight <= 0 && mIsTopOpen && null != mDragListener) {
                mIsTopOpen = false;
                mDragListener.onDragStateChanged(DragState.DRAG_STATE_TOP_CLOSE);
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
                if (null != mDragListener && !mIsScrollToTop) {
                    mIsScrollToTop = true;
                    mDragListener.onDragEdgeChanged(DragEdgeState.DRAG_STATE_BOTTOM_TO_TOP_EDGE);
                }
            } else {
                layoutParams.height = height;
            }
            mContentViewGrop.setLayoutParams(layoutParams);
            // 判断是否拖拽到边缘
            if (mBottomHeight >= (getHeight() - mBottomDragViewHeight)) {
                if (null != mDragListener && !mIsScrollToBottom) {
                    mIsScrollToBottom = true;
                    mDragListener.onDragEdgeChanged(DragEdgeState.DRAG_STATE_BOTTOM_TO_TOP_EDGE);
                }
            } else {
                mIsScrollToBottom = false;
            }
            // 判断是否打开
            if (mBottomHeight > MIN_OPEN_DISTANCE && !mIsBottomOpen && null != mDragListener) {
                mIsBottomOpen = true;
                mDragListener.onDragStateChanged(DragState.DRAG_STATE_BOTTOM_OPEN);
            }
            // 判断是否关闭
            if (mBottomHeight <= 0 && mIsBottomOpen && null != mDragListener) {
                mIsBottomOpen = false;
                mDragListener.onDragStateChanged(DragState.DRAG_STATE_BOTTOM_CLOSE);
            }
        }
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
        if (childCount == 2) {
            for (int i = 0; i < childCount; i++) {
                View childView = getChildAt(i);
                int measureWidth = childView.getMeasuredWidth();
                int measureHeight = childView.getMeasuredHeight();
                if (i == 1) {
                    if (mDragDirection == 0) {
                        if (mLeftWidth <= 0) {
                            childView.layout(0, 0, measureWidth, measureHeight);
                        } else if (mLeftWidth >= getWidth() - measureWidth) {
                            childView.layout(getWidth() - measureWidth, 0, getWidth(), measureHeight);
                        } else {
                            childView.layout(mLeftWidth, 0, measureWidth + mLeftWidth, measureHeight);
                        }
                    }
                    if (mDragDirection == 1) {
                        if (mRightWidth <= 0) {
                            childView.layout(getWidth() - measureWidth, 0, getWidth(), measureHeight);
                        } else if (mRightWidth >= getWidth() - mRightDragViewWidth) {
                            childView.layout(0, 0, measureWidth, measureHeight);
                        } else {
                            childView.layout(getWidth() - mRightWidth - measureWidth, 0, getWidth() - mRightWidth, measureHeight);
                        }
                    }
                    if (mDragDirection == 2) {
                        if (mTopHeight <= 0) {
                            childView.layout(0, 0, getWidth(), measureHeight);
                        } else if (mTopHeight >= getHeight() - mTopDragViewHeight) {
                            childView.layout(0, getHeight() - measureHeight, getWidth(), getHeight());
                        } else {
                            childView.layout(0, mTopHeight, getWidth(), mTopHeight + measureHeight);
                        }
                    }
                    if (mDragDirection == 3) {
                        if (mBottomHeight <= 0) {
                            childView.layout(0, getHeight() - measureHeight, getWidth(), getHeight());
                        } else if (mBottomHeight >= getHeight() - mBottomDragViewHeight) {
                            childView.layout(0, 0, getWidth(), measureHeight);
                        } else {
                            childView.layout(0, getHeight() - measureHeight - mBottomHeight, getWidth(), getHeight() - mBottomHeight);
                        }
                    }
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
                        mDragListener.onDragViewClick(view, 0, mIsLeftOpen);
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
                        mDragListener.onDragViewClick(view, 1, mIsRightOpen);
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
                        mDragListener.onDragViewClick(view, 2, mIsTopOpen);
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
                        mDragListener.onDragViewClick(view, 3, mIsBottomOpen);
                    }
                }
                break;
        }
    }

    /**
     * 拖动左边面板到响应比例位置处
     */
    public void moveLeftViewWithRatio(float ratio) {
        mLeftWidth = (int) (getWidth() * ratio);
        moveLeftView(mLeftWidth);
    }

    /**
     * 拖动右边面板到响应比例位置处
     */
    public void moveRightViewWithRatio(float ratio) {
        mRightWidth = (int) (getWidth() * ratio);
        moveRightView(mRightWidth);
    }

    /**
     * 拖动顶部面板到响应比例位置处
     */
    public void moveTopViewWithRatio(float ratio) {
        mTopHeight = (int) (getHeight() * ratio);
        moveTopView(mTopHeight);
    }

    /**
     * 拖动顶部面板到响应比例位置处
     */
    public void moveBottomViewWithRatio(float ratio) {
        mBottomHeight = (int) (getHeight() * ratio);
        moveBottomView(mBottomHeight);
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
         * @param position 对应拖拽按钮方向 0为left 1为right 2为top 3 为bottom
         * @param isOpen   面板是否打开
         * @param view     几点的view
         */
        void onDragViewClick(View view, int position, boolean isOpen);
    }

    /**
     * 滑动状态
     */
    public enum DragEdgeState {
        // 左边滑动到右边
        DRAG_STATE_LEFT_TO_RIGHT_EDGE,
        // 右边滑动到左边
        DRAG_STATE_RIGHT_TO_LEFT_EDGE,
        // 顶部滑动到顶部
        DRAG_STATE_TOP_TO_BOTTOM_EDGE,
        // 底部滑动到顶部
        DRAG_STATE_BOTTOM_TO_TOP_EDGE,
    }

    /**
     * 打开关闭状态
     */
    public enum DragState {
        // 左边面板打开
        DRAG_STATE_LEFT_OPEN,
        // 左边面板关闭
        DRAG_STATE_LEFT_CLOSE,
        // 右边面板打开
        DRAG_STATE_RIGHT_OPEN,
        // 右边面板关闭
        DRAG_STATE_RIGHT_CLOSE,
        // 顶部面板打开
        DRAG_STATE_TOP_OPEN,
        // 顶部面板关闭
        DRAG_STATE_TOP_CLOSE,
        // 底部面板打开
        DRAG_STATE_BOTTOM_OPEN,
        // 底部面板关闭
        DRAG_STATE_BOTTOM_CLOSE,
    }
}
