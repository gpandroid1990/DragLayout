package com.tibbytang.android.draglayoutexample.orientation;

/**
 * 作者:tibbytang
 * 微信:tibbytang19900607
 * 有问题加微信
 * 创建于:2020-04-13 18:06
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.tibbytang.android.draglayoutexample.R;

import java.io.Serializable;

/**
 * 自定义容器View使用自定义属性控制宽高比例 可以选择根据宽或者高为标准进行大小控制
 * <br>最终可实现任意比例的布局容器：很好用的 <br>
 *         app:demoHeight="1"<br>
 *         app:demoWidth="3"<br>
 *         app:standard="w"
 * @Author StoneJxn
 * @Date 2017年1月13日 上午1:24:48
 */
public class StoneFiexedLayout extends RelativeLayout implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private int mDemoHeight = -1;
    private int mDemoWidth = -1;
    private String mStandard = "w";

    public StoneFiexedLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.StoneFiexedLayout, defStyle, 0);
        if (a != null) {
            mDemoHeight = a.getInteger(R.styleable.StoneFiexedLayout_demoHeight, -1);
            mDemoWidth = a.getInteger(R.styleable.StoneFiexedLayout_demoWidth, -1);
            mStandard = a.getString(R.styleable.StoneFiexedLayout_standard);
        }
    }

    public StoneFiexedLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StoneFiexedLayout(Context context) {
        this(context, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));

        // Children are just made to fill our space.
        if (mStandard.length() > 0 && mDemoWidth != -1 && mDemoHeight != -1) {
            if (mStandard.equals("w")) {// 以宽为标准
                int childWidthSize = getMeasuredWidth();
                widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize * mDemoHeight / mDemoWidth, MeasureSpec.EXACTLY);
            } else if (mStandard.equals("h")) {// 以高为标准
                int childheightSize = getMeasuredHeight();
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(childheightSize, MeasureSpec.EXACTLY);
                widthMeasureSpec = MeasureSpec.makeMeasureSpec(childheightSize * mDemoWidth / mDemoHeight, MeasureSpec.EXACTLY);
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
