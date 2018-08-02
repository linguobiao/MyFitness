package com.lgb.myfitness.helper;

import java.util.Calendar;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import com.lgb.myfitness.been.DataRead;
import com.lgb.myfitness.been.HistoryDay;
import com.lgb.myfitness.been.HistoryHour;
import com.lgb.myfitness.been.ScreenData;
import com.lgb.myfitness.database.DatabaseProvider_wb013;
import com.lgb.myfitness.global.Global;

public class SyncHelper {
	
	
	
	/**
	 * 删除当天全部小时数据
	 * @param context
	 * @param profileID
	 * @param today
	 */
	public static void deleteAllHistoryToday(Context context, int profileID, int deviceID, Calendar today) {
		// 删除当天小时数据
		DatabaseProvider_wb013.deleteADayHistoryHour(context, profileID, deviceID, today);
		// 删除当天日数据
		DatabaseProvider_wb013.deleteHistoryDate(context, profileID, today);
		// 删除当天屏幕数据
		DatabaseProvider_wb013.deleteScreen(context, today, profileID, deviceID);
		// 
		DatabaseProvider_wb013.deleteResetTime(context, today, profileID, deviceID);
		
		DatabaseProvider_wb013.deleteResetHistoryHour(context, profileID, deviceID, today);
	}

	
	
	/**
	 * 保存小时数据
	 * @param value
	 */
	public static void saveHistoryHour(Context context, HistoryHour history_receive, int profileID, int deviceID, Calendar cal_current_hour) {
//		Log.i(TAG, "profileID " + profileID);
		if (history_receive != null) {
//			HistoryHour history = ParserHelper.parserBLEValue(value);
			
			// 查询数据库有没有获取到的数据
			HistoryHour temp = DatabaseProvider_wb013.queryHistoryHour(context, profileID, deviceID, history_receive.getDate());
			if (temp == null) {
				// 如果为空直接插入
				DatabaseProvider_wb013.insertHistoryHour(context, profileID, deviceID, history_receive);
				
			} else {
				if (cal_current_hour != null) {
					// 当前时间
					String current_hour = CalendarHelper.getYyyy_MM_dd_HH(cal_current_hour);
					// 返回到的数据的时间
					String return_data = CalendarHelper.getYyyy_MM_dd_HH(history_receive.getDate());
					
					// 判断是否当前时间
					if (current_hour.equals(return_data)) {
						
						// 判断是否有断电标记
						boolean isHaveReset = DatabaseProvider_wb013.isHaveResetTime(context, cal_current_hour, profileID, deviceID);
						if (isHaveReset) {
							double reset_burn = 0;
							int reset_step = 0;
							int reset_sleepMove = 0;
							
							// 数据库中保存的断电时刻小时数据
							HistoryHour historyHour_reset = DatabaseProvider_wb013.queryResetHistoryHour(context, profileID, deviceID, cal_current_hour);
							if (historyHour_reset != null) {
								reset_burn = historyHour_reset.getBurn();
								reset_step = historyHour_reset.getStep();
								reset_sleepMove = historyHour_reset.getSleepMove();
							}
							
							// 要保存的新数据
							HistoryHour newData = new HistoryHour();
							newData.setDate(cal_current_hour);
							newData.setBurn(history_receive.getBurn() - reset_burn + temp.getBurn());
							newData.setStep(history_receive.getStep() - reset_step + temp.getStep());
							newData.setSleepMove(history_receive.getSleepMove() - reset_sleepMove + temp.getSleepMove());
							
							// 更新数据库小时数据
							DatabaseProvider_wb013.updateHistoryHour(context, profileID, deviceID, newData);
							// 更新重置小时数据
							if (historyHour_reset == null) {
								DatabaseProvider_wb013.insertResetHistoryHour(context, profileID, deviceID, history_receive);
							} else {
								DatabaseProvider_wb013.updateResetHistoryHour(context, profileID, deviceID, history_receive);
							}
							
							return;
						}
					}
				}
				
				DatabaseProvider_wb013.updateHistoryHour(context, profileID, deviceID, history_receive);
			}
		}	
	}
	
	
	/**
	 * 保存小时数据
	 * @param value
	 */
	public static void saveHistoryHour(Context context, HistoryHour history, int profileID, int deviceID) {
//		Log.i(TAG, "profileID " + profileID);
		if (profileID != -1 && history != null) {
//			HistoryHour history = ParserHelper.parserBLEValue(value);
			
			HistoryHour temp = DatabaseProvider_wb013.queryHistoryHour(context, profileID, deviceID, history.getDate());
			if (temp == null) {
				DatabaseProvider_wb013.insertHistoryHour(context, profileID, deviceID, history);
			} else {
				DatabaseProvider_wb013.updateHistoryHour(context, profileID, deviceID, history);
			}
		}	
	}
	
	
	public static void saveScreenData(Context context, ScreenData screen, int profileID, int deviceID) {
		if (context != null && screen != null) {
			Calendar date = screen.getDate();
			
			ScreenData screenSave = DatabaseProvider_wb013.queryScreen(context, date, profileID, deviceID);
			if (screenSave == null) {
//				Log.i("saveScreenData", "screenSave is null");
				DatabaseProvider_wb013.insertScreen(context, screen, profileID, deviceID);
			} else {
//				Log.i("saveScreenData", "screenSave is null");
				DatabaseProvider_wb013.updateScreen(context, screen, profileID, deviceID);
			}
		}
	}
	
	
	/**
	 * 保存3.5mm口返回的日数据
	 * @param read
	 * @param profileID
	 */
	public static void saveHistoryDate(Context context, DataRead read, int profileID) {
		if (read != null) {
			// 今天的数据
			HistoryDay today = read.getTodayHistory();
			if (today != null) {
				
				HistoryDay temp = DatabaseProvider_wb013.queryHistoryDate(context, profileID, today.getDate());
				if (temp == null) {
					DatabaseProvider_wb013.insertHistoryDate(context, profileID, today);
				} else {
					DatabaseProvider_wb013.updateHistoryDate(context, profileID, today);
				}
			}
			
			// 昨天数据
			HistoryDay yesterday = read.getYesterdayHistory();
			if (yesterday != null) {
				HistoryDay temp = DatabaseProvider_wb013.queryHistoryDate(context, profileID, yesterday.getDate());
				if (temp == null) {
					DatabaseProvider_wb013.insertHistoryDate(context, profileID, yesterday);
				} else {
					DatabaseProvider_wb013.updateHistoryDate(context, profileID, yesterday);
				}
			}
		}
	}
	
	
	/**
	 * 初始化蓝牙
	 * @param context
	 */
	public static BluetoothAdapter initBluetooth_manual(Activity context){
		BluetoothManager mBluetoothManager = null;

		if (mBluetoothManager == null){
			mBluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
			
			// 没有蓝牙模块
			if (mBluetoothManager == null){
				return null;
			}
		}
		BluetoothAdapter mBtAdapter = mBluetoothManager.getAdapter();

		// 判断机器是否有蓝牙
		if (!mBtAdapter.isEnabled()){
			Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			context.startActivityForResult(enableIntent, Global.REQUEST_ENABLE_BLUETOOTH);
		}
		return mBtAdapter;
	}
	
	
	/**
	 * 初始化蓝牙
	 * @param context
	 */
	public static BluetoothAdapter initBluetooth_auto(Activity context){
		BluetoothManager mBluetoothManager = null;

		if (mBluetoothManager == null){
			mBluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
			
			// 没有蓝牙模块
			if (mBluetoothManager == null){
				return null;
			}
		}
		BluetoothAdapter mBtAdapter = mBluetoothManager.getAdapter();

		return mBtAdapter;
	}
	
}
