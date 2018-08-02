package com.lgb.myfitness.helper;

import java.util.Arrays;
import java.util.Calendar;
import com.lgb.myfitness.been.Alarm;
import com.lgb.myfitness.been.BPM;
import com.lgb.myfitness.been.DataRead;
import com.lgb.myfitness.been.Goal;
import com.lgb.myfitness.been.HistoryDay;
import com.lgb.myfitness.been.HistoryHour;
import com.lgb.myfitness.been.Profile;
import com.lgb.myfitness.been.Reminder;
import com.lgb.myfitness.been.Scale;
import com.lgb.myfitness.been.ScreenData;
import com.lgb.myfitness.database.DatabaseProvider_public;
import com.lgb.myfitness.global.Global;
import android.content.Context;
import android.util.Log;


public class ParserHelper {
	
	private static String TAG = "ParserHelper";
	
	/**
	 * 解析从3.5mm接口获取的数据
	 * @param bytedat
	 * @return
	 */
	public static DataRead parser35mmValue(byte[] bytedat) {

		int[] dat = new int[20];
		
		System.out.println("Start data Process...");
		int sum = 0;
		for (int i = 0; i < 20; i++) {
			if (bytedat[i] >= 0)
				dat[i] = (int) bytedat[i];
			else {
				dat[i] = (int) (256 + bytedat[i]);
			}
			if(i < 19)
				sum = sum + dat[i];
		}
		if(sum%256 == dat[19])
		{
			dat[0] = dat[0];// 帧头不作处理
			dat[13] = dat[13];// 保留字节不作处理
			dat[14] = dat[14];// 保留字节不作处理
			dat[15] = dat[15];// 保留字节不作处理
			dat[16] = dat[16];// 保留字节不作处理
			dat[17] = dat[17];// 保留字节不作处理
			dat[18] = dat[18];// 保留字节不作处理
			dat[19] = dat[19];// 检验和
			
	
			int FirstDayStep = dat[1] + dat[2] * 256;
			int FirstDayCal = dat[3] + dat[4] * 256;
			int SecondDayStep = dat[5] + dat[6] * 256;
			int SecondDayCal = dat[7] + dat[8] * 256;
			
//			int ThirdDayStep = dat[9] + dat[10] * 256;
//			int ThirdDayCal = dat[11] + dat[12] * 256;
			
			String firstDaySleepStr = byteToBinaryString(dat[9]);
//			System.out.println("firstDaySleepStr " + firstDaySleepStr);
			int firstDayDeepSleepHour = intForHigh4Bit(firstDaySleepStr);
			int firstDayLightSleepHour = intForLow4Bit(firstDaySleepStr);
			int firstDayActiveHour = dat[10];
			
			String secondDaySleepStr = byteToBinaryString(dat[11]);
//			System.out.println("secondDaySleepStr " + secondDaySleepStr);
			int secondDayDeepSleepHour = intForHigh4Bit(secondDaySleepStr);
			int secondDayLightSleepHour = intForLow4Bit(secondDaySleepStr);
			int secondDayActiveHour = dat[12];
			
			
			int firstDaySleepQuality = dat[13];
			int secondDaySleepQuality = dat[14];
//			int thirdDaySleepQuality = dat[15];
			int battaryLevel = dat[16];
			
			Log.i("parser35mmValue", 
					"当天的步数：            " + FirstDayStep + "\n" +
					"当天的卡路里消耗：" + FirstDayCal + "\n" +
					"当天的睡眠质量：    " + firstDaySleepQuality + "%" + "\n" +
					"当天的深睡时间： " + firstDayDeepSleepHour + "\n" +
					"当天的浅睡时间： " + firstDayLightSleepHour + "\n" +
					"当天的运动时间： " + firstDayActiveHour + "\n" +
					"昨天的步数：            " + SecondDayStep + "\n" +
					"昨天的卡路里消耗：" + SecondDayCal + "\n" +
					"昨天的睡眠质量：    " + secondDaySleepQuality + "%" + "\n" +
					"昨天的深睡时间： " + secondDayDeepSleepHour + "\n" +
					"昨天的浅睡时间： " + secondDayLightSleepHour + "\n" +
					"昨天的运动时间： " + secondDayActiveHour + "\n" +
					"电池电量：                " + battaryLevel + "%" + "\n"
					);
		
			Calendar today = CalendarHelper.getToday();
			HistoryDay todayHistory = new HistoryDay();
			todayHistory.setDate(today);
			todayHistory.setStep(FirstDayStep);
			todayHistory.setSleepQuality(firstDaySleepQuality);
			todayHistory.setBurn(FirstDayCal);
			todayHistory.setDeepSleepHour(firstDayDeepSleepHour);
			todayHistory.setLightSleepHour(firstDayLightSleepHour);
			todayHistory.setActiveHour(firstDayActiveHour);
			
			Calendar yesterday = CalendarHelper.getYesterday(today);
			HistoryDay yesterdayHistory = new HistoryDay();
			yesterdayHistory.setDate(yesterday);
			yesterdayHistory.setStep(SecondDayStep);
			yesterdayHistory.setSleepQuality(secondDaySleepQuality);
			yesterdayHistory.setBurn(SecondDayCal);
			yesterdayHistory.setDeepSleepHour(secondDayDeepSleepHour);
			yesterdayHistory.setLightSleepHour(secondDayLightSleepHour);
			yesterdayHistory.setActiveHour(secondDayActiveHour);
			
			
			DataRead read = new DataRead();
			read.setTodayHistory(todayHistory);
			read.setYesterdayHistory(yesterdayHistory);
			read.setBattaryLevel(battaryLevel);
			
			return read;
		}
		else{
			Log.i("parser35mmValue", "检验和错误\n");
		}
		return null;
	}
	
	
	/**
	 * 解析从蓝牙获取的数据
	 * @param bytedat
	 * @return
	 */
	public static HistoryHour parserBLEValue(byte[] bytedat) {
		
		int[] dat = new int[20];

		for (int i = 0; i < 20; i++) {
			if (bytedat[i] >= 0)
				dat[i] = (int) bytedat[i];
			else {
				dat[i] = (int) (256 + bytedat[i]);
			}
		}

//		int hour = dat[1];
		int step = dat[2] + dat[3] * 256;
		int burn = dat[4] + dat[5] * 256;
		int sleep = dat[6];
		int battaryLevel = dat[7];
			
		String time1 = byteToBinaryString(dat[8]);
		String time2 = byteToBinaryString(dat[9]);
		String time3 = byteToBinaryString(dat[10]);
		String time4 = byteToBinaryString(dat[11]);
		
		// 2000-01-01 00:00:00 后的秒数
		Long datetime = Long.parseLong(time4 + time3 + time2 + time1, 2);
		Calendar cal = CalendarHelper.get20010101Datetime();
		cal.setTimeInMillis(datetime * 1000 + cal.getTimeInMillis());
			
			
		HistoryHour history = new HistoryHour();
		history.setBurn(burn);
		history.setStep(step);
		history.setSleepMove(sleep);
		history.setDate(cal);
		
		Log.i("parserBLEValue", 
				Global.sdf_1.format(history.getDate().getTime()) + ", " +
						"步数：" + step  + ", " +
						"卡路里 ：" + burn + ", " +
						"动作次数：" + sleep + ", " +
						"电池电量：" + battaryLevel + "%\n\n"
						);

		
		return history;
	}
	
	/**
	 * 解析电子称数据
	 * @param value
	 * @return
	 */
	public static Scale parseScales(byte[] value, Calendar date) {	
		int[] dat = new int[16];
		double weight = 0;
		
		Scale scale = new Scale();
		for (int i = 0; i < 16; i++) {
//			System.out.println("***value " + i + " = " + value[i]);
			if (value[i] >= 0)
				dat[i] = (int) value[i];
			else {
				dat[i] = (int) (256 + value[i]);
			}
//			System.out.println("***" + i + " = " + dat[i]);
		}
		weight = (double)(dat[4] * 256 + dat[5]) / 10;
//		weight = 80.5;
//		String weight_1 = String.valueOf(dat[5]).substring(0, String.valueOf(dat[5]).length() - 1);
//		String weight_2 = String.valueOf(dat[5]).substring(String.valueOf(dat[5]).length() - 1);
//		String weight_12 = weight_1 + "." + weight_2;
//		weight = Double.parseDouble(weight_12);
		
		scale.setDate(date);
		scale.setWeight(weight);
		System.out.println("get weight = " + weight);
		return scale;
	}
	
	
	/**
	 * 解析获取到的屏幕数据
	 * @param bytedat
	 * @return
	 */
	public static ScreenData parserScreenValue(Context context, byte[] bytedat) {
		int[] dat = new int[20];

		for (int i = 0; i < 20; i++) {
			if (bytedat[i] >= 0)
				dat[i] = (int) bytedat[i];
			else {
				dat[i] = (int) (256 + bytedat[i]);
			}
		}
		
		int step = dat[1] + dat[2] * 256;
//		System.out.println("distance:" + dat[3] + dat[4] * 256 );
		double distance_km = (dat[3] + dat[4] * 256) / (double)100;
		int burn = dat[5] + dat[6] * 256;
		int goal = dat[7] + dat[8] * 256;
		int battaryLevel = dat[9];
		
		Log.i("parserScreenValue", "step:" + step + ", " 
				+ "distance:" + distance_km + "km, "
				+ "burn:" + burn + ", "
				+ "goal:" + goal + ", "
				+ "battaryLevel:" + battaryLevel + "\n");
		
		ScreenData screen = new ScreenData();
		Calendar cal = Calendar.getInstance();
		cal = CalendarHelper.setDayFormat(cal);
		screen.setDate(cal);
		screen.setStep(step);
		screen.setDistance(distance_km);
		screen.setBurn(burn);
		screen.setGoal(goal);
		screen.setBattaryLevel(battaryLevel);
		
		int unit = UnitHelper.getUnit(context);
		screen.setUint(unit);
		
		return screen;
	}
	
	
	/**
	 * 解析血压计数据
	 * @param context
	 * @param value
	 * @return
	 */
	public static BPM parserBPMValue(Context context, byte[] value) {
		if (context != null && value != null) {
			int size = value.length;
			
			int[] dat = new int[size];
			// 超范围处理
			for (int i = 0; i < size; i++) {
				if (value[i] >= 0)
					dat[i] = (int) value[i];
				else {
					dat[i] = (int) (256 + value[i]);
				}
			}
			
			// 找出起始坐标
			int index = -1;
			for (int i = 0; i + 3 < size; i++) {
				if (dat[i] == 0x02
						&& dat[i + 1] == 0x40
							&& dat[i + 2] == 0xdd) {
					index = i;
					break;
				}
			}
			
			// 对应的数据
			if (index + 12 < size) {
				
				int mark = dat[index + 4];
				
				// 0为测试成功标记
//				if (mark == 0) {
					int sys = dat[index + 5] * 256 + dat[index + 6];
					int dia = dat[index + 7] * 256 + dat[index + 8];
					int hr = dat[index + 11] * 256 + dat[index + 12];
					
					BPM bpm = new BPM();
					bpm.setDatetime(Calendar.getInstance());
					bpm.setSystolic(sys);
					bpm.setDiatolic(dia);
					bpm.setHeartRate(hr);
					
					return bpm;
					
//				} else {
//					return null;
//					
//				}
				
			}
			
		}
		
		
		return null;
	}
	
	
	public static byte[] requestAllData(Context context, int type) {
		byte[] value = new byte[20];
		// 帧头
		value[0] = (byte) 0xaa;
		// 数据处理方式
//		if (type == Global.TYPE_COMMUNICATE) {
//			value[1] = 0x00;
//		} else if (type == Global.TYPE_CLEAR) {
//			value[1] = 0x01;
//		}
		value[1] = 0x00;
		
		// 时间
		Calendar c = Calendar.getInstance();
		// year
		value[2] = (byte)( c.get(Calendar.YEAR) - 2000);
		// month
		value[3] = (byte)( c.get(Calendar.MONTH) + 1);
		// day
		value[4] = (byte)( c.get(Calendar.DAY_OF_MONTH));
		// hour
		value[5] = (byte)( c.get(Calendar.HOUR_OF_DAY));
		// minute
		value[6] = (byte)(( c.get(Calendar.MINUTE)));
		
		// height
		value[7] = (byte) Global.DEFAULT_HEIGHT;
		// weight
		value[8] = (byte) Global.DEFAULT_WEIGHT;
		// step distance
		value[9] = (byte) CalculateHelper.calculateStepDistance(Global.DEFAULT_HEIGHT);
		// alarm repeat day
		value[10] = (byte) 0x00;
		// alarm hour
		value[11] = 6;
		// alarm minute
		value[12] = 0;
		// alarm monitor period
		value[13] = 30;
		// reminder repeat day
		value[14] = (byte) 0x00;
		// reminder start hour
		value[15] = 8;
		// reminder end hour
		value[16] = 20;
		// reminder interval
		value[17] = 30;
		// goal
		value[18] = 0;
		
		int profileID = ProfileHelper.initProfileID(context);
		String name = ProfileHelper.getCurrentUseProfileName(context);
		Profile profile = DatabaseProvider_public.queryProfile(context, name);
		
		if (profile != null) {
			// 用户信息
			double height = profile.getHeight();
			if (height != 0) {
				value[7] = (byte) height;
				value[9] = (byte) CalculateHelper.calculateStepDistance(height);
			}
			
			double weight = profile.getWeight();
			if (weight != 0) {
				value[8] = (byte) weight;
			}
			
			profileID = profile.getID();
		}
		
	
		// 闹钟信息
		Alarm alarm = DatabaseProvider_public.queryAlarm(context, profileID);
		if (alarm != null) {
			if (alarm.getState() == Global.ALARM_ON) {
				value[10] = (byte) getRepeatDate(alarm);
			}
			int hour = alarm.getHour();
			value[11] = (byte) hour;
			
			int minute = alarm.getMinute();
			value[12] = (byte) minute;
			
			int duration = alarm.getDuration();
			value[13] = (byte) duration;
		}
	
		// 运动提醒
		Reminder reminder = DatabaseProvider_public.queryReminder(context, profileID);
		if (reminder != null) {
			if (reminder.getState() == Global.ALARM_ON) {
				value[14] = (byte) getRepeatDate(reminder);
			}
			
			int hour_start = reminder.getBegin_hour();
			value[15] = (byte) hour_start;
			
			int hour_end = reminder.getEnd_hour();
			value[16] = (byte) hour_end;
			
			int interval = reminder.getInterval();
			value[17] = (byte) interval;
		}
		
		// 目标信息
		Goal goal = DatabaseProvider_public.queryGoal(context, profileID);
		if (goal != null) {
			String goalkm = CalculateHelper.calculateGoalDistance(goal.getStep(), value[9]);
			value[18] = (byte) Double.parseDouble(goalkm);
		} else {
			String goalkm = CalculateHelper.calculateGoalDistance(Global.DEFAULT_GOAL_STEP, value[9]);
			value[18] = (byte) Double.parseDouble(goalkm);
		}	
		
		
		Log.i(TAG, "height:" + value[7] 
				+ " weight:" + value[8]
				+ " step distance:" + value[9]
				+ " alarm repeat:" + value[10]
				+ " alarm hour:" + value[11]
				+ " alarm minute:" + value[12]
				+ " alarm duration:" + value[13]
				+ " reminder repeat:" + value[14]
				+ " reminder begin hour:" + value[15]
				+ " reminder end hour:" + value[16]
				+ " reminder interval:" + value[17]
				+ " goal:" + value[18]);
		
		int sum = 0;
		for(int iii = 0; iii < 19; iii++){
			if(value[iii] >= 0){
				sum = sum + value[iii];
			}else {
				sum = sum + 256 + value[iii];
			}
		}
		value[19] = (byte)(sum%256);
//		System.out.println("------------ send data -------------");
//		for (byte b : value) {
//			System.out.println(b);
//		}
//		System.out.println("--------------------------------------");
		
		Log.i(TAG, "request all write data:" + Arrays.toString(value));
		return value;
	}
	
	
	
	public static byte[] getLostModeBeginData() {
		byte[] value = new byte[20];
		value[0] = (byte) 0xaa;
		value[1] = 0x02;
		
		// 校验和
		int sum = 0;
		for(int iii = 0; iii < 19; iii++){
			if(value[iii] >= 0){
				sum = sum + value[iii];
			}else {
				sum = sum + 256 + value[iii];
			}
		}
		value[19] = (byte)(sum%256);
		
		return value;
	}
	
	
	public static byte[] getLostModeEndData() {
		byte[] value = new byte[20];
		value[0] = (byte) 0xaa;
		value[1] = 0x03;
		
		// 校验和
		int sum = 0;
		for(int iii = 0; iii < 19; iii++){
			if(value[iii] >= 0){
				sum = sum + value[iii];
			}else {
				sum = sum + 256 + value[iii];
			}
		}
		value[19] = (byte)(sum%256);
		
		return value;
	}
	
	
	/**
	 * 获取一小时数据接口
	 * @param hourLabel
	 * @return
	 */
	public static byte[] request1HourBleData(int hourLabel) {
		byte[] value = new byte[20];
		value[0] = (byte) 0xAA;
		value[1] = (byte) 0xA1;
		value[2] = (byte) hourLabel;
		
		int sum = 0;
		for(int iii = 0; iii < 19;iii++){
			if(value[iii] >= 0){
				sum = sum + value[iii];
			}else {
				sum = sum + 256 + value[iii];
			}
		}
		value[19] = (byte)(sum%256);
		
		Log.i(TAG, "request 1hour write data:" + Arrays.toString(value));
		return value;
	}
	
	
	/**
	 * 获取屏幕数据
	 * @return
	 */
	public static byte[] requestScreenData() {
		byte[] value = new byte[20];
		value[0] = (byte) 0xAA;
		value[1] = (byte) 0xB1;
		
		int sum = 0;
		for(int iii = 0; iii < 19;iii++){
			if(value[iii] >= 0){
				sum = sum + value[iii];
			}else {
				sum = sum + 256 + value[iii];
			}
		}
//		Log.i(TAG, "sum:" + sum);
		value[19] = (byte)(sum%256);
		
		Log.i(TAG, "request screen write data:" + Arrays.toString(value));
		
		return value;
	}
		
	
	public static byte[] setUnitData(Context context) {
		byte[] value = new byte[20];
		value[0] = (byte) 0xaa;
		value[1] = (byte) 0xd1;
		
		int time_display = UnitHelper.getTimeDisplay(context);
		if (time_display == Global.TYPE_TIME_DISPLAY_24) {
			value[2] = 0x00;
		} else {
			value[2] = 0x01;
		}
		
		int unit = UnitHelper.getUnit(context);
		if (unit == Global.TYPE_UNIT_METRIC) {
			value[3] = 0x00;
		} else {
			value[3] = 0x01;
		}
		
		int sum = 0;
		for(int iii = 0; iii < 19;iii++){
			if(value[iii] >= 0){
				sum = sum + value[iii];
			}else {
				sum = sum + 256 + value[iii];
			}
		}
//		Log.i(TAG, "sum:" + sum);
		value[19] = (byte)(sum%256);
		
		Log.i(TAG, "set unit write data:" + Arrays.toString(value));
		
		return value;
	}
	
	
	public static byte[] setLostModeData(int averageRSSI, int lostRSSI) {
		byte[] value = new byte[2];
		value[0] = (byte) averageRSSI;
		value[1] = (byte) lostRSSI;
		
		Log.i(TAG, "set lost mode write data:" + Arrays.toString(value));
		
		return value;
	}
	
	
	public static byte[] setBpmWriteData(int order, int xor) {
		byte[] value = new byte[6];
		value[0] = (byte) 0x02;
		value[1] = (byte) 0x40;
		value[2] = (byte) 0xdc;
		value[3] = (byte) 0x01;
		value[4] = (byte) order;
		value[5] = (byte) xor;
		
		Log.i(TAG, "set bpm write data:" + Arrays.toString(value));
		
		return value;
	}
	
	
	/**
	 * 二进制字符串，将高4位转化为int
	 * @param binaryStr
	 * @return
	 */
	private static int intForHigh4Bit(String binaryStr) {
		if (binaryStr != null && binaryStr.length() == 8) {
			String high4BitStr = binaryStr.substring(0, 4);
			
			if (high4BitStr != null) {
				return Integer.parseInt(high4BitStr, 2);
			}
		}
		
		return 0;
	}
	
	
	/**
	 * 二进制字符串，将低四位转为为int
	 * @param binaryStr
	 * @return
	 */
	private static int intForLow4Bit(String binaryStr) {
		if (binaryStr != null && binaryStr.length() == 8) {
			String low4BitStr = binaryStr.substring(4, 8);
			
			if (low4BitStr != null) {
				return Integer.parseInt(low4BitStr, 2);
			}
		}
		
		return 0;
	}
	
	

	/**
	 * 将一个byte转化为2进制字符串
	 * @param b
	 * @return
	 */
	private static String byteToBinaryString(int b) {
		String binary = Integer.toBinaryString(b);
		
		if (binary != null) {
			int size = binary.length();
			// 补位，足8位
			if (size < 8) {
				for (int i = 0; i < 8 - size; i++) {
					binary = "0" + binary;
				}
			}
		}
		
		return binary;
	}
	
	
	/**
	 * 获取重复日期的二进制字符串
	 * @param mon
	 * @param tue
	 * @param wed
	 * @param thu
	 * @param fri
	 * @param sat
	 * @param sun
	 * @return
	 */
	public static String getRepeatDateBinary(int mon, int tue, int wed, int thu, int fri, int sat, int sun) {
		StringBuffer sb = new StringBuffer();
		// 0关，1开
		sb.append("0");
		if (sun == Global.ALARM_ON) {
			sb.append(1);
		} else {
			sb.append(0);
		}
		if (sat == Global.ALARM_ON) {
			sb.append(1);
		} else {
			sb.append(0);
		}
		if (fri == Global.ALARM_ON) {
			sb.append(1);
		} else {
			sb.append(0);
		}
		if (thu == Global.ALARM_ON) {
			sb.append(1);
		} else {
			sb.append(0);
		}
		if (wed == Global.ALARM_ON) {
			sb.append(1);
		} else {
			sb.append(0);
		}
		if (tue == Global.ALARM_ON) {
			sb.append(1);
		} else {
			sb.append(0);
		}
		if (mon == Global.ALARM_ON) {
			sb.append(1);
		} else {
			sb.append(0);
		}
		
		return sb.toString();
	}
	
	
	/**
	 * 获取alarm重复日期的值
	 * @param alarm
	 * @return
	 */
	public static int getRepeatDate(Alarm alarm) {
		if (alarm != null) {
			String dateBinaryStr = getRepeatDateBinary(
					alarm.getMonday(), alarm.getTuesday(), 
					alarm.getWednesday(), alarm.getThursday(), 
					alarm.getFriday(), alarm.getSaturday(), alarm.getSunday());
			int intValue = Integer.parseInt(dateBinaryStr, 2);
			return intValue;
		}
		
		return 0;
	}
	
	/**
	 * 获取reminder重复日期的值
	 * @param reminder
	 * @return
	 */
	public static int getRepeatDate(Reminder reminder) {
		if (reminder != null) {
			String dateBinaryStr = getRepeatDateBinary(
					reminder.getMonday(), reminder.getTuesday(), 
					reminder.getWednesday(), reminder.getThursday(), 
					reminder.getFriday(), reminder.getSaturday(), reminder.getSunday());
			int intValue = Integer.parseInt(dateBinaryStr, 2);
			return intValue;
		}
		
		return 0;
	}
	
	
	public static boolean isSpecialKey(byte[] value) {
		StringBuffer sb = new StringBuffer();
		for (byte b : value) {
			int x = 0;
			if (b >= 0)
				x = (int) b;
			else {
				x = (int) (256 + b);
			}
			
			sb.append(Integer.toHexString(x));
		}

//		System.out.println(sb.toString());
		if (sb.toString().equals("aa111111111111111111bc")) {
			return true;
		}
		
		return false;
	}
}
