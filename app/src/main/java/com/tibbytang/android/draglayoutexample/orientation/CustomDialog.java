package com.tibbytang.android.draglayoutexample.orientation;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.tibbytang.android.draglayoutexample.R;

/**
 * 作者:tibbytang
 * 微信:tibbytang19900607
 * 有问题加微信
 * 创建于:2020-04-13 17:47
 */
public class CustomDialog extends Dialog {

    private View mLayoutView;
    public CustomDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
        setContentView(R.layout.test_dialog);
        getWindow().setLayout(300, WindowManager.LayoutParams.MATCH_PARENT);
    }

}
