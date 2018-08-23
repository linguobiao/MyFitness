package com.guoou.sdk.sdk;

import android.os.CountDownTimer;

import com.guoou.sdk.api.BleListener;
import com.guoou.sdk.api.SdkApi;
import com.guoou.sdk.bean.Cmd;
import com.guoou.sdk.global.SdkManager;
import com.guoou.sdk.util.LogHelper;
import com.inuker.bluetooth.library.Constants;
import com.inuker.bluetooth.library.connect.options.BleConnectOptions;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.inuker.bluetooth.library.search.SearchRequest;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.search.response.SearchResponse;

import java.util.Arrays;
import java.util.UUID;

import static com.inuker.bluetooth.library.Constants.REQUEST_SUCCESS;

/**
 * Created by LGB on 2017/7/21.
 */

public class BleUtils implements SdkApi{

    public static UUID UUID_CHARACTERISTIC_NOTIFY_BPM = UUID.fromString("00002AF0-0000-1000-8000-00805f9b34fb");

    private static final UUID UUID_SERVICE_ffe0 = UUID.fromString("000018f0-0000-1000-8000-00805f9b34fb");
    private static final UUID UUID_CHARACTER_ffe1 = UUID.fromString("00002AF1-0000-1000-8000-00805f9b34fb");

    private static final BleConnectOptions options = new BleConnectOptions.Builder()
            .setConnectRetry(0)
            .setConnectTimeout(8000)
            .setServiceDiscoverRetry(0)
            .setServiceDiscoverTimeout(5000)
            .build();

    //倒计时
    private CountDownTimer countDownTimer;
    private int syncTimeOut = 10;//同步超时时间，默认10秒

    private int connectTimes = 1;//搜索次数，默认5次，每次10秒

    private BleListener syncListener;

    private String boundMac;

    private BleListener connectListener;

    @Override
    public boolean isConnect(String mac) {
        return SdkManager.getInstance().getClient().getConnectStatus(mac) == Constants.STATUS_DEVICE_CONNECTED;
    }

    @Override
    public void connectDevice(String mac, BleListener bleListener) {
        SdkManager.getInstance().getClient().stopSearch();
        boundMac = mac;
        connectListener = bleListener;
        SearchRequest request = buildSearchRequest(connectTimes);
        SdkManager.getInstance().getClient().search(request, mSearchResponse);
    }

    private SearchRequest buildSearchRequest(int times){
        SearchRequest.Builder builder = new SearchRequest.Builder();
        for (int i = 0; i < times; i ++) {
            builder.searchBluetoothLeDevice(10000);
        }
        return builder.build();
    }

    private final SearchResponse mSearchResponse = new SearchResponse() {

        boolean isConnectSuccess = false;

        @Override public void onSearchStarted() {
            isConnectSuccess = false;
            LogHelper.log("onSearchStarted:");}

        @Override
        public void onDeviceFounded(SearchResult device) {
            LogHelper.log("onDeviceFounded:" + device.getName() + ",  " + device.device.getAddress());
//            String mac = ByteUtils.getDevice(device).getMac();
            if (!isConnectSuccess && "BPM-188".equals(device.getName())) {
                isConnectSuccess = true;
                SdkManager.getInstance().getClient().stopSearch();
                SdkManager.getInstance().getClient().refreshCache(device.device.getAddress());
                connect(device.device.getAddress());
            }
        }

        @Override public void onSearchStopped() {
            LogHelper.log("onSearchStopped:");
            if (connectListener != null && !isConnectSuccess) connectListener.onResult(false, null);
        }

        @Override public void onSearchCanceled() {
            LogHelper.log("onSearchCanceled:");
            if (connectListener != null && !isConnectSuccess) connectListener.onResult(false, null);
        }
    };

    private void connect(final String mac) {
        SdkManager.getInstance().getClient().connect(mac, BleUtils.options, new BleConnectResponse() {
            @Override
            public void onResponse(int code, BleGattProfile profile) {
                if (code == REQUEST_SUCCESS) {
                    LogHelper.log("connected success, open notify...");
                    openNotify(mac);
                } else {
                    LogHelper.log("connected fail");
                    if (connectListener != null) connectListener.onResult(false, mac);
                    SdkManager.getInstance().getClient().disconnect(mac);
                }
            }
        });
    }

    // 写BLE设备
    private void write(final String boundMac, final byte[] bytes) {
        LogHelper.log("write : " + Arrays.toString(bytes));
        SdkManager.getInstance().getClient().write(boundMac, BleUtils.UUID_SERVICE_ffe0, BleUtils.UUID_CHARACTER_ffe1, bytes, new BleWriteResponse() {
            @Override
            public void onResponse(int code) {
                if (code == REQUEST_SUCCESS) {
                    LogHelper.log("write success");
                    if ((byte)0xa6 == bytes[4]) {
                        LogHelper.log("write language success, start test");
                        writeBpm(0xa1);
                    }
                } else {
                    cancelTimer();
                    if (syncListener != null) syncListener.onResult(false, boundMac);
                }
            }
        });
    }

    // 打开设备通知
    private void openNotify(final String boundMac) {
        SdkManager.getInstance().getClient().notify(boundMac, BleUtils.UUID_SERVICE_ffe0, BleUtils.UUID_CHARACTERISTIC_NOTIFY_BPM, new BleNotifyResponse() {
            @Override
            public void onNotify(UUID service, UUID character, byte[] value) {
                LogHelper.log("notify : " + Arrays.toString(value));
                SdkUtils.getInstance().parserNotify(value);
            }

            @Override
            public void onResponse(int code) {
                if (code == REQUEST_SUCCESS) {
                    BleUtils.this.boundMac = boundMac;
                    if (connectListener != null) connectListener.onResult(true, boundMac);
                } else {
                    if (connectListener != null) connectListener.onResult(false, boundMac);
                    SdkManager.getInstance().getClient().disconnect(boundMac);
                }
            }
        });
    }

    @Override
    public void setSyncTimeOut(int seconds) {
        syncTimeOut = seconds;
    }

    @Override
    public void setConnectTimes(int times) {
        connectTimes = times;
    }

    @Override
    public void clean() {
        cancelTimer();
    }

    private void initTimer() {
        if (countDownTimer == null) {
            countDownTimer = new CountDownTimer(syncTimeOut * 1000, 1000) {
                public void onTick(long millisUntilFinished) {}
                public void onFinish() {
                    cancelTimer();
                    if (syncListener != null) syncListener.onResult(false, null);
                }
            };
        }
        countDownTimer.start();
    }

    @Override
    public void cancelTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Override
    public void writeBpm(int order) {
        write(boundMac, Cmd.getBpmCmd(order));
    }

    @Override
    public void writeBpmLanguage(int language) {
        write(boundMac, Cmd.getBpmLanguageCmd(language));
    }
}
