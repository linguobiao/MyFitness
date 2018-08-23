package com.guoou.sdk.api;


/**
 * Created by LGB on 2018/4/24.
 */

public interface SdkApi {

    /**
     * 判断设备是否连接
     * @param mac 设备的mac地址
     * @return true=已经连接, false=未连接
     */
    boolean isConnect(String mac);

    /**
     * 连接设备
     * @param mac 设备的mac地址
     * @param bleListener 监听连接结果,可传null
     */
    void connectDevice(String mac, BleListener bleListener);

    /**
     * 设置同步超时时间，默认10秒
     * @param seconds 秒
     */
    void setSyncTimeOut(int seconds);


    /**
     * 设置搜索连接次数，默认5次
     * @param times 次数
     */
    void setConnectTimes(int times);

    void cancelTimer();

    void clean();

    void writeBpm(int order);

    void writeBpmLanguage(int language);
}
