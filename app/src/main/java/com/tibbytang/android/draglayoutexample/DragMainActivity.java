package com.tibbytang.android.draglayoutexample;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;

public class DragMainActivity extends AppCompatActivity implements DargItemViewBinder.DragViewClickListener {
    private RecyclerView mRecyclerView;
    private MultiTypeAdapter mMultiTypeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_main);
        initView();
        initData();
    }

    private void initView() {
        mRecyclerView = this.findViewById(R.id.main_drag_recycler_view);
        mMultiTypeAdapter = new MultiTypeAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mMultiTypeAdapter.register(String.class, new DargItemViewBinder(this));
    }

    private void initData() {
        List<String> list = new ArrayList<String>();
        list.add("左边拖动");
        list.add("右边拖动");
        list.add("顶部拖动");
        list.add("底部拖动");
        mMultiTypeAdapter.setItems(list);
        mRecyclerView.setAdapter(mMultiTypeAdapter);
    }

    @Override
    public void onDragViewClick(int position) {
        if (position == 0) {
            startActivity(new Intent(this, DragLeftActivity.class));
        }
        if (position == 1) {
            startActivity(new Intent(this, DragRightActivity.class));
        }
        if (position == 2) {
            startActivity(new Intent(this, DragTopActivity.class));
        }
        if (position == 3) {
            startActivity(new Intent(this, DragBottomActivity.class));
        }
    }
}
