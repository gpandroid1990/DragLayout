package com.tibbytang.android.drag;

import com.elvishew.xlog.XLog;

/**
 * created by tibbytang 2019/4/17 3:28 PM
 * Wechat:ITnan562980080
 * QQ:562980080
 * website:tibbytang.com
 */
public class FastClickUtil {
    private static final int TIME = 500;
    private static long currentTime = 0L;
    private static int count = 0;

    public static boolean isNotFastClick() {
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

    public static boolean isNotFastClick(int time) {
        if (Math.abs((System.currentTimeMillis() - currentTime)) > time) {
            currentTime = System.currentTimeMillis();
            return true;
        }
        currentTime = System.currentTimeMillis();
        return false;
    }
}