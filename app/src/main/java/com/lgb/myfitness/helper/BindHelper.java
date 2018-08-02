package com.lgb.myfitness.helper;

import java.util.Map;
import java.util.Set;

import com.lgb.myfitness.been.Band;
import com.lgb.myfitness.database.DatabaseProvider_public;
import com.lgb.myfitness.global.Global;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class BindHelper {
	
	/**
	 * 获取上次同步的设备
	 * @param context
	 * @return
	 */
	public static Band getLastSyncDevice(Context context) {
		if (context != null) {
			SharedPreferences mPrefs = context.getSharedPreferences(Global.KEY_PREF, Context.MODE_PRIVATE);
			// 上一次同步的地址
			String deviceAddress = mPrefs.getString(Global.KEY_LAST_SYNC_DEVICE_ADDRESS, null);
			
			// 如果有保存，查询数据库,获取deviceID
			if (deviceAddress != null) {
				Band band = DatabaseProvider_public.queryDevice(context, deviceAddress);
				return band;
			}
		}
		
		return null;
	}
	
	
	/**
	 * 获取上次同步的设备地址
	 * @param context
	 * @return
	 */
	public static String getLastSyncDeviceAddress(Context context) {
		
		if (context != null) {
			SharedPreferences mPrefs = context.getSharedPreferences(Global.KEY_PREF, Context.MODE_PRIVATE);
			// 上一次同步的地址
			String deviceAddress = mPrefs.getString(Global.KEY_LAST_SYNC_DEVICE_ADDRESS, null);
			
			return deviceAddress;
		}
		
		return null;
	}
	
	
	/**
	 * 保存上次同步的设备地址
	 * @param context
	 * @param deviceAddress
	 */
	public static void saveLastSyncDeviceAddress(Context context, String deviceAddress) {
		if (context != null) {
			SharedPreferences mPrefs = context.getSharedPreferences(Global.KEY_PREF, Context.MODE_PRIVATE);
			
			Editor editor = mPrefs.edit();
			editor.putString(Global.KEY_LAST_SYNC_DEVICE_ADDRESS, deviceAddress);
			editor.commit();
		}
	}
	
	
	/**
	 * save last sync device address
	 * @param context
	 * @param deviceAddress
	 */
	public static void saveLastSyncDevice(Context context, Band band) {
		
		if (context != null && band != null) {
			String deviceAddress = band.getAddress();
			if (deviceAddress != null) {
				SharedPreferences mPrefs = context.getSharedPreferences(Global.KEY_PREF, Context.MODE_PRIVATE);
				
				Editor editor = mPrefs.edit();
				editor.putString(Global.KEY_LAST_SYNC_DEVICE_ADDRESS, deviceAddress);
				editor.commit();
				
				// 保存到数据库
				Band temp = DatabaseProvider_public.queryDevice(context, deviceAddress);
				if (temp == null) {
					DatabaseProvider_public.insertDevice(context, band);
				} 
			}
		}
	}
	
	
	/**
	 * 获取距离最近的设备
	 * @param deviceMap
	 * @return
	 */
	public static Band getNearestDevice(Map<Band, Integer> deviceMap) {
		if (deviceMap != null) {
			Set<Band> keySet = deviceMap.keySet();
			
			int rssiMax = -9999;
			Band nearestDevice = null;
			
			for (Band band : keySet) {
				Integer rssiValue = deviceMap.get(band);
				
				if (rssiValue != null) {
					if (rssiValue > rssiMax) {
						rssiMax = rssiValue;
						nearestDevice = band;
					}
				}
			}
			if (nearestDevice != null) {
				Log.i("getNearestDeviceAddress", "nearest address: " + nearestDevice.getAddress());
			} else {
				Log.i("getNearestDeviceAddress", "nearest band is null");
			}
			
			return nearestDevice;
		}
		
		return null;
	}
	
	
	/**
	 * 判断是否包含该设备
	 * @param deviceMap
	 * @param lastSyncDeviceAddress
	 * @return
	 */
	public static boolean isContainBoundDevice(Map<Band, Integer> deviceMap, String lastSyncDeviceAddress) {
		if (deviceMap != null && lastSyncDeviceAddress != null) {
			Set<Band> keySet = deviceMap.keySet();
			for (Band band : keySet) {
				String address = band.getAddress();
				if (address != null && address.equals(lastSyncDeviceAddress)) {
					return true;
				}
			}
		}
		
		return false;
	}
}
