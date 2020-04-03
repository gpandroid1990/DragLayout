package com.tibbytang.android.draglayoutexample;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
        mLeftView.setOnClickListener(this);
        mCenterView.setOnClickListener(this);
        mRightView.setOnClickListener(this);
        mThirdView.setOnClickListener(this);
        mDragLayout.addDragStateListener(new DragLayout.DragStateListener() {
            @Override
            public void onDragStateChanged(DragLayout.DragState dragState) {
                if (dragState == DragLayout.DragState.DRAG_STATE_LEFT_OPEN) {
                    Toast.makeText(DragLeftActivity.this, "左边面板打开", Toast.LENGTH_SHORT).show();
                }
                if (dragState == DragLayout.DragState.DRAG_STATE_LEFT_CLOSE) {
                    Toast.makeText(DragLeftActivity.this, "左边面板关闭", Toast.LENGTH_SHORT).show();
                }

                if (dragState == DragLayout.DragState.DRAG_STATE_RIGHT_OPEN) {
                    Toast.makeText(DragLeftActivity.this, "右边面板打开", Toast.LENGTH_SHORT).show();
                }
                if (dragState == DragLayout.DragState.DRAG_STATE_RIGHT_CLOSE) {
                    Toast.makeText(DragLeftActivity.this, "右边面板关闭", Toast.LENGTH_SHORT).show();
                }

                if (dragState == DragLayout.DragState.DRAG_STATE_TOP_OPEN) {
                    Toast.makeText(DragLeftActivity.this, "顶部面板打开", Toast.LENGTH_SHORT).show();
                }
                if (dragState == DragLayout.DragState.DRAG_STATE_TOP_CLOSE) {
                    Toast.makeText(DragLeftActivity.this, "顶部面板关闭", Toast.LENGTH_SHORT).show();
                }

                if (dragState == DragLayout.DragState.DRAG_STATE_BOTTOM_OPEN) {
                    Toast.makeText(DragLeftActivity.this, "底部面板打开", Toast.LENGTH_SHORT).show();
                }
                if (dragState == DragLayout.DragState.DRAG_STATE_BOTTOM_CLOSE) {
                    Toast.makeText(DragLeftActivity.this, "底部面板关闭", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onDragEdgeChanged(DragLayout.DragEdgeState dragEdgeState) {
                if (dragEdgeState == DragLayout.DragEdgeState.DRAG_STATE_BOTTOM_TO_TOP_EDGE) {
                    Toast.makeText(DragLeftActivity.this, "滑动到顶部", Toast.LENGTH_SHORT).show();
                }
                if (dragEdgeState == DragLayout.DragEdgeState.DRAG_STATE_TOP_TO_BOTTOM_EDGE) {
                    Toast.makeText(DragLeftActivity.this, "滑动到底部", Toast.LENGTH_SHORT).show();
                }
                if (dragEdgeState == DragLayout.DragEdgeState.DRAG_STATE_LEFT_TO_RIGHT_EDGE) {
                    Toast.makeText(DragLeftActivity.this, "滑动到右边", Toast.LENGTH_SHORT).show();
                }
                if (dragEdgeState == DragLayout.DragEdgeState.DRAG_STATE_RIGHT_TO_LEFT_EDGE) {
                    Toast.makeText(DragLeftActivity.this, "滑动到左边", Toast.LENGTH_SHORT).show();
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
            mDragLayout.moveLeftViewToLeftEdge();
        }
        if (v == mRightView) {
            mDragLayout.moveLeftViewToRightEdge();
        }
        if (v == mCenterView) {
            mDragLayout.moveLeftViewWithRatio(1.0f/2.0f);
        }
        if (v==mThirdView){
            mDragLayout.moveLeftViewWithRatio(1.0f/3.0f);
        }
    }

    @Override
    public void onDragViewClick(int position) {
        Toast.makeText(this, mList.get(position), Toast.LENGTH_SHORT).show();
    }
}
