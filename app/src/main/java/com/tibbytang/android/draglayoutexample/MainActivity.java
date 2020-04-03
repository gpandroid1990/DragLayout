package com.tibbytang.android.draglayoutexample;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.elvishew.xlog.XLog;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private DragLayout mDragLayout;
    private AppCompatButton mLeftView;
    private AppCompatButton mCenterView;
    private AppCompatButton mRightView;
    private AppCompatButton mThirdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDragLayout = this.findViewById(R.id.main_drag_layout_view);
        mLeftView = this.findViewById(R.id.move_left_view);
        mCenterView = this.findViewById(R.id.move_center_view);
        mRightView = this.findViewById(R.id.move_right_view);
        mThirdView = this.findViewById(R.id.move_third_view);

        mLeftView.setOnClickListener(this);
        mCenterView.setOnClickListener(this);
        mRightView.setOnClickListener(this);
        mThirdView.setOnClickListener(this);
        mDragLayout.addDragStateListener(new DragLayout.DragStateListener() {
            @Override
            public void onDragStateChanged(DragLayout.DragState dragState) {
                if (dragState == DragLayout.DragState.DRAG_STATE_TO_LEFT) {
                    XLog.d("滑动到左边");
                }
                if (dragState == DragLayout.DragState.DRAG_STATE_TO_RIGHT) {
                    XLog.d("滑动到右边");
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == mLeftView) {
            mDragLayout.moveLeft();
        }
        if (v == mRightView) {
            mDragLayout.moveRight();
        }
        if (v == mCenterView) {
            mDragLayout.moveCenter();
        }
        if (v == mThirdView) {
            mDragLayout.moveThird();
        }
    }
}
