package com.guoou.sdk.api;

/**
 * Created by LGB on 2018/4/24.
 */

public interface BleListener {
    void onResult(boolean isSuccess, String mac);
}
