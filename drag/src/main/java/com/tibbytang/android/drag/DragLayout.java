package com.tibbytang.android.drag;

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
    private boolean mIsScrollToLeft = false;
    private boolean mIsScrollToRight = false;
    private boolean mIsScrollToTop = false;
    private boolean mIsScrollToBottom = false;
    private boolean mIsLeftOpen = false;
    private boolean mIsRightOpen = false;
    private boolean mIsTopOpen = false;
    private boolean mIsBottomOpen = false;
    // 最小阈值 拖拽到右边触发边缘检测距离
    private static final int MIN_DISTANCE = 0;
    // 最小移动距离 判断是否打开
    private static final int MIN_OPEN_DISTANCE = 2;
    private DragStateListener mDragListener;

    public DragLayout(@NonNull Context context) {
        super(context);
    }

    public DragLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DragLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void addDragStateListener(DragStateListener dragListener) {
        mDragListener = dragListener;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mLeftDragContentLayout = findViewById(R.id.drag_left_content_view);
        mRightDragContentLayout = findViewById(R.id.drag_right_content_view);
        mTopDragContentLayout = findViewById(R.id.drag_top_content_view);
        mBottomDragContentLayout = findViewById(R.id.drag_bottom_content_view);

        mLeftDragView = findViewById(R.id.drag_left_view);
        mRightDragView = findViewById(R.id.drag_right_view);
        mTopDragView = findViewById(R.id.drag_top_view);
        mBottomDragView = findViewById(R.id.drag_bottom_view);
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
            // 判断是否拖拽到边缘
            if (mLeftWidth >= (getWidth() - MIN_DISTANCE)) {
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
        if (null != mRightDragView && mRightDragContentLayout != null) {
            LayoutParams layoutParams = (LayoutParams) mRightDragContentLayout.getLayoutParams();
            if (width <= 0) {
                layoutParams.width = 0;
            } else if (width > getWidth()) {
                layoutParams.width = getWidth();
                if (null != mDragListener && !mIsScrollToLeft) {
                    mIsScrollToLeft = true;
                    mDragListener.onDragEdgeChanged(DragEdgeState.DRAG_STATE_RIGHT_TO_LEFT_EDGE);
                }
            } else {
                layoutParams.width = width;
            }
            mRightDragContentLayout.setLayoutParams(layoutParams);

            // 判断是否拖拽到边缘
            if (mRightWidth >= (getWidth() - MIN_DISTANCE)) {
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


    public void moveTopView(int height) {
        XLog.d(TAG + " moveTopView height=" + height);
        if (null != mTopDragView && mTopDragContentLayout != null) {
            LayoutParams layoutParams = (LayoutParams) mTopDragContentLayout.getLayoutParams();
            if (height <= 0) {
                layoutParams.height = 0;
            } else if (height > getHeight()) {
                layoutParams.height = getHeight();
                if (null != mDragListener && !mIsScrollToBottom) {
                    mIsScrollToBottom = true;
                    mDragListener.onDragEdgeChanged(DragEdgeState.DRAG_STATE_TOP_TO_BOTTOM_EDGE);
                }
            } else {
                layoutParams.height = height;
            }
            mTopDragContentLayout.setLayoutParams(layoutParams);
            // 判断是否拖拽到边缘
            if (mTopHeight >= (getHeight() - MIN_DISTANCE)) {
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

    public void moveBottomView(int height) {
        XLog.d(TAG + " moveBottomView height=" + height + " getHeight()=" + getHeight());
        if (null != mBottomDragView && mBottomDragContentLayout != null) {
            LayoutParams layoutParams = (LayoutParams) mBottomDragContentLayout.getLayoutParams();
            if (height <= 0) {
                layoutParams.height = 0;
            } else if (height > getHeight()) {
                layoutParams.height = getHeight();
                if (null != mDragListener && !mIsScrollToTop) {
                    mIsScrollToTop = true;
                    mDragListener.onDragEdgeChanged(DragEdgeState.DRAG_STATE_BOTTOM_TO_TOP_EDGE);
                }
            } else {
                layoutParams.height = height;
            }
            mBottomDragContentLayout.setLayoutParams(layoutParams);
            // 判断是否拖拽到边缘
            if (mBottomHeight >= (getHeight() - MIN_DISTANCE)) {
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
     * 拖动左边面板到左边缘
     */
    public void moveLeftViewToLeftEdge() {
        mLeftWidth = 0;
        moveLeftView(mLeftWidth);
    }

    /**
     * 拖动左边面板到右边缘
     */
    public void moveLeftViewToRightEdge() {
        mLeftWidth = getWidth();
        moveLeftView(getWidth());
    }

    /**
     * 拖动左边面板到响应比例位置处
     */
    public void moveLeftViewWithRatio(float ratio) {
        mLeftWidth = (int) (getWidth() * ratio);
        moveLeftView(mLeftWidth);
    }

    /**
     * 拖动右边面板到左边缘
     */
    public void moveRightViewToLeftEdge() {
        mRightWidth = getWidth();
        moveRightView(getWidth());
    }

    /**
     * 拖动右边面板到右边缘
     */
    public void moveRightViewToRightEdge() {
        mRightWidth = 0;
        moveRightView(mRightWidth);
    }

    /**
     * 拖动右边面板到响应比例位置处
     */
    public void moveRightViewWithRatio(float ratio) {
        mRightWidth = (int) (getWidth() * ratio);
        moveRightView(mRightWidth);
    }

    /**
     * 拖动顶部面板到顶部
     */
    public void moveTopViewToTopEdge() {
        mTopHeight = 0;
        moveTopView(mTopHeight);
    }

    /**
     * 拖动顶部面板到底部
     */
    public void moveTopViewToBottomEdge() {
        mTopHeight = getHeight();
        moveTopView(mTopHeight);
    }

    /**
     * 拖动顶部面板到响应比例位置处
     */
    public void moveTopViewWithRatio(float ratio) {
        mTopHeight = (int) (getHeight() * ratio);
        moveTopView(mTopHeight);
    }

    /**
     * 拖动底部面板到顶部
     */
    public void moveBottomViewToTopEdge() {
        mBottomHeight = getHeight();
        moveBottomView(mBottomHeight);
    }

    /**
     * 拖动顶部面板到底部
     */
    public void moveBottomViewToBottomEdge() {
        mBottomHeight = 0;
        moveBottomView(mBottomHeight);
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
        // 当状态变更时监听
        void onDragStateChanged(DragState dragState);

        // 拖拽到边缘监听
        void onDragEdgeChanged(DragEdgeState dragEdgeState);
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
