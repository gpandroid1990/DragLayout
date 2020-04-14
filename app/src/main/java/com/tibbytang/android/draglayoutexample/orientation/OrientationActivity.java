package com.tibbytang.android.draglayoutexample.orientation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;

import com.elvishew.xlog.XLog;
import com.tibbytang.android.draglayoutexample.R;

public class OrientationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orientation);
        XLog.d("onCreate");
        findViewById(R.id.orientation_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OrientationDialogFragment.newInstance().show(getSupportFragmentManager(), "OrientationDialogFragment");
//                CustomDialog customDialog = new CustomDialog(OrientationActivity.this);
//                customDialog.show();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        XLog.d("onSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        XLog.d("onRestoreInstanceState");
    }

    @Override
    protected void onResume() {
        super.onResume();
        XLog.d("onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        XLog.d("onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        XLog.d("onDestroy");
    }
}
