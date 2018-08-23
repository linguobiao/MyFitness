package com.guoou.sdk.global;

import android.content.Context;
import android.text.TextUtils;

import com.guoou.sdk.api.BleListener;
import com.guoou.sdk.api.SdkApi;
import com.guoou.sdk.sdk.BleUtils;
import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.search.SearchResult;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class SdkManager {

    private final String SDK_VERSION = "1.0.003";
    private List<SearchResult> availableDevices;

    private static class Instance {private static final SdkManager instance = new SdkManager();}
    public static SdkManager getInstance() {return SdkManager.Instance.instance;}

    private BluetoothClient mClient;
    private Context mContext;

    public void init(Context context) {
        mContext = context;
    }

    public BluetoothClient getClient() {
        if (mClient == null) {
            synchronized (SdkManager.class) {
                if (mClient == null) {
                    mClient = new BluetoothClient(mContext);
                }
            }
        }
        return mClient;
    }

    public void finish() {
        mClient = null;
    }

    SdkApi sdkApi = new BleUtils();

    public boolean isConnect(String mac) {
        return sdkApi.isConnect(mac);
    }

    public void connectDevice(String mac, BleListener bleListener){
        sdkApi.connectDevice(mac, bleListener);
    }

    public void connectDevice(String mac){
        sdkApi.connectDevice(mac, null);
    }

    public void setSyncTimeOut(int seconds) {
        sdkApi.setSyncTimeOut(seconds);
    }

    public void setConnectTimes(int times) {
        sdkApi.setConnectTimes(times);
    }

    /**
     * 获取可用设备列表
     */
    public List<SearchResult> getAvailableDevices() {
        return this.availableDevices;
    }

    /**
     * @param devices
     */
    public void setAvailableDevices(List<SearchResult> devices) {
        if (availableDevices == null) availableDevices = new ArrayList<>();
        availableDevices.clear();
        if (devices == null) return;
        availableDevices.addAll(devices);
    }

    /**
     * 获取蓝牙设备名称
     */
    public String getBluetoothName(String mac) {
        if (TextUtils.isEmpty(mac)) return null;
        if (availableDevices == null || availableDevices.size() == 0) return null;
        for (SearchResult device : availableDevices) {
            if (mac.equals(device.getAddress())) return device.getName();
        }
        return null;
    }

    public void cancelTimer(){
        sdkApi.cancelTimer();
    }

    public void clean(){
        sdkApi.clean();
    }

    public void writeBpm(int order) {
        sdkApi.writeBpm(order);
    }

    public void writeBpmLanguage(int language) {
        sdkApi.writeBpmLanguage(language);
    }
}
