package com.tibbytang.android.draglayoutexample;

import android.app.Application;

import com.elvishew.xlog.LogConfiguration;
import com.elvishew.xlog.LogLevel;
import com.elvishew.xlog.XLog;
import com.elvishew.xlog.printer.AndroidPrinter;

/**
 * 作者:tibbytang
 * 微信:tibbytang19900607
 * 有问题加微信
 * 创建于:2020-04-02 10:32
 */
public class DragApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initLog();
    }

    private void initLog() {
        LogConfiguration logConfiguration = new LogConfiguration.Builder()
                .tag("tibbytang-android")
                .logLevel(LogLevel.ALL)
                .build();
        XLog.init(logConfiguration, new AndroidPrinter());
    }
}
