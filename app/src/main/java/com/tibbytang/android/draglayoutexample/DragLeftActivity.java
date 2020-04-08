package com.tibbytang.android.draglayoutexample;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elvishew.xlog.XLog;
import com.tibbytang.android.drag.DragLayout;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;

public class DragLeftActivity extends AppCompatActivity implements View.OnClickListener, DargItemViewBinder.DragViewClickListener {
    private DragLayout mDragLayout;
    private AppCompatButton mLeftView;
    private AppCompatButton mCenterView;
    private AppCompatButton mRightView;
    private AppCompatButton mThirdView;
    private AppCompatButton mHideView;
    private AppCompatButton mShowView;
    private AppCompatButton mGetStateView;
    private FrameLayout mDragContainerView;

    private RecyclerView mRecyclerView;
    private MultiTypeAdapter mMultiTypeAdapter;
    private List<String> mList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_left);
        initView();
        initData();
    }


    private void initView() {
        mDragLayout = this.findViewById(R.id.main_drag_layout_view);
        mLeftView = this.findViewById(R.id.move_left_view);
        mCenterView = this.findViewById(R.id.move_center_view);
        mRightView = this.findViewById(R.id.move_right_view);
        mThirdView = this.findViewById(R.id.move_third_view);
        mRecyclerView = this.findViewById(R.id.left_drag_recycler_view);
        mHideView = this.findViewById(R.id.hide_layout_view);
        mShowView = this.findViewById(R.id.show_layout_view);
        mGetStateView = this.findViewById(R.id.get_layout_state_view);
        mDragContainerView = this.findViewById(R.id.drag_container_view);
        mLeftView.setOnClickListener(this);
        mCenterView.setOnClickListener(this);
        mRightView.setOnClickListener(this);
        mThirdView.setOnClickListener(this);
        mShowView.setOnClickListener(this);
        this.mGetStateView.setOnClickListener(this);
        mHideView.setOnClickListener(this);
        mDragLayout.setEnableDrag(true);
        mDragLayout.addDragStateListener(new DragLayout.DragStateListener() {
            @Override
            public void onDragStateChanged(DragLayout.DragState dragState) {
                if (dragState == DragLayout.DragState.DRAG_STATE_OPEN) {
                    Toast.makeText(DragLeftActivity.this, "面板打开", Toast.LENGTH_SHORT).show();
                }
                if (dragState == DragLayout.DragState.DRAG_STATE_CLOSE) {
                    Toast.makeText(DragLeftActivity.this, "面板关闭", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onDragEdgeChanged(DragLayout.DragEdgeState dragEdgeState) {
                if (dragEdgeState == DragLayout.DragEdgeState.DRAG_STATE_TO_EDGE) {
                    Toast.makeText(DragLeftActivity.this, "滑动到边缘", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onDragViewClick(View view, boolean isOpen) {
                    if (isOpen) {
                        mDragLayout.openPanelWithRatio(0.0f);
                    } else {
                        mDragLayout.openPanelWithRatio(1.0f);
                }
            }
        });
    }

    private void initData() {
        mMultiTypeAdapter = new MultiTypeAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mMultiTypeAdapter.register(String.class, new DargItemViewBinder(this));

        mList.add("Java开发");
        mList.add("android开发");
        mList.add("kotlin开发");
        mList.add("go 开发");
        mList.add("rust 开发");
        mList.add("php 开发");
        mList.add("python 开发");
        mList.add("javascript 开发");
        mList.add("html 开发");
        mList.add("css 开发");
        mList.add("typescript 开发");
        mList.add("ruby 开发");
        mList.add("spring 开发");
        mList.add("ktor 开发");
        mMultiTypeAdapter.setItems(mList);
        mRecyclerView.setAdapter(mMultiTypeAdapter);
    }

    @Override
    public void onClick(View v) {

        if (v == mLeftView) {
//            mDragLayout.openPanelWithRatio(0.0f);
            mDragLayout.closePanel();
        }
        if (v == mRightView) {
            mDragLayout.openPanelWithRatio(1.0f);
        }
        if (v == mCenterView) {
            mDragLayout.openPanelWithRatio(1.0f / 2.0f);
        }
        if (v == mThirdView) {
            mDragLayout.openPanelWithRatio(1.0f / 3.0f);
        }
        if (v == mShowView) {
            mDragContainerView.setVisibility(View.VISIBLE);
        }
        if (v == mHideView) {
            mDragContainerView.setVisibility(View.GONE);
        }

        if (v==mGetStateView){
            XLog.d("dragLayout open state :"+mDragLayout.isOpen());
        }
    }

    @Override
    public void onDragViewClick(int position) {
        Toast.makeText(this, mList.get(position), Toast.LENGTH_SHORT).show();
    }
}
