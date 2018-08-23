package com.guoou.sdk.util;

import android.util.Log;

/**
 * Created by LGB on 2018/4/24.
 */

public class LogHelper {
    private final static boolean SHOW_LOG = true;
    private final static String LGB_SDK = "LGB_SDK";
    public static void log(String msg) {
        if (!SHOW_LOG) return;
        Log.d(LGB_SDK, msg);
    }

}
