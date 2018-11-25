package com.guoou.sdk.sdk;

import com.guoou.sdk.bean.BpmBatteryBean;
import com.guoou.sdk.bean.BpmPressureBean;
import com.guoou.sdk.bean.BpmResultBean;
import com.guoou.sdk.util.ByteUtils;
import com.guoou.sdk.util.LogHelper;

import org.greenrobot.eventbus.EventBus;

import java.util.Arrays;

/**
 * Created by LGB on 2018/5/8.
 */

public class SdkUtils {

    //单例
    private static class Instance {public static final SdkUtils instance = new SdkUtils();}
    public static SdkUtils getInstance() {return SdkUtils.Instance.instance;}


    public void parserNotify(byte[] value) {
        String valueArray = Arrays.toString(value);
        if (valueArray.startsWith("[2, 64, -35, 1,")) {

        } else if (valueArray.startsWith("[2, 64, -35, 2,")) {//收到 血压计压力值
            LogHelper.log("------>> 收到血压计压力值");
            int pressure = ByteUtils.byte2int_2(value, 4);
            EventBus.getDefault().post(new BpmPressureBean(pressure));
        } else if (valueArray.startsWith("[2, 64, -35, 3,")) {//收到 血压计电量
            LogHelper.log("------>> 收到血压计电量");
            int percent = value[6];
            EventBus.getDefault().post(new BpmBatteryBean(percent));
        } else if (valueArray.startsWith("[2, 64, -35, 12,")) {//收到 血压计结果
            LogHelper.log("------>> 收到血压计结果");
            int sys = ByteUtils.byte2int_2(value, 5);
            int dia = ByteUtils.byte2int_2(value, 7);
            int hr = ByteUtils.byte2int_2(value, 11);
            boolean isSuccess = value[4] == 28;
            BpmResultBean bean = new BpmResultBean(isSuccess);
            if (isSuccess) {
                bean.setSystolic(sys);
                bean.setDiatolic(dia);
                bean.setHeartRate(hr);
            } else {
                bean.setError(value[12]);
            }
            EventBus.getDefault().post(bean);
        }
    }
}
