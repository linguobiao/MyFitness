package com.lgb.myfitness.helper;

import java.util.Calendar;
import java.util.List;
import com.lgb.myfitness.R;
import com.lgb.myfitness.been.HistoryHour;
import com.lgb.myfitness.global.Global;
import android.app.Activity;
import android.content.Context;
import android.view.Window;


public class ShowValueHelper {
	/**
	 * 获取步数
	 * @param historyList_24hour
	 * @param index
	 * @return
	 */
	public static int getStepValue_hour(List<?> historyList_24hour, int index) {
		if (historyList_24hour != null) {
			int size = historyList_24hour.size();
			if (index < size && index >= 0) {
				Object history = historyList_24hour.get(index);
				if (history != null) {
					if (history instanceof HistoryHour) {
						return ((HistoryHour) history).getStep();
						
					} else {
						return 0;
					}
				}	
			}
		}
		
		return 0;
	}
	
	
	/**
	 * 获取卡路里
	 * @param historyList_24hour
	 * @param index
	 * @return
	 */
	public static double getBurnValue_hour(List<?> historyList_24hour, int index) {
		if (historyList_24hour != null) {
			int size = historyList_24hour.size();
			if (index < size && index >= 0) {
				Object history = historyList_24hour.get(index);
				if (history != null) {
					if (history instanceof HistoryHour) {
						return ((HistoryHour) history).getBurn();
					} else {
						return 0;
					}
				}
			}
		}
		
		return 0;
	}
	
	
	public static String getSleepValue_hour(Context context, List<?> historyList_24hour, int index) {
		if (historyList_24hour != null) {
			int size = historyList_24hour.size();
			
			if (index < size && index >= 0) {
				Object history = historyList_24hour.get(index);
				if (history != null) {
					if (history instanceof HistoryHour) {
						int sleepMove = ((HistoryHour) history).getSleepMove();
						int sleepPattern = CalculateHelper.getSleepPattern_accordingSleepMove(sleepMove);
						if (sleepPattern == Global.TYPE_SLEEP_DEEP) {
							return context.getString(R.string.Deep);
						} else if (sleepPattern == Global.TYPE_SLEEP_LIGHT) {
							return context.getString(R.string.Light);
						} else if (sleepPattern == Global.TYPE_SLEEP_AWAKE) {
							return context.getString(R.string.Awake);
						}
					} else {
						return context.getString(R.string.Awake);
					}
					
				}
			}
		}
		
		return context.getString(R.string.Awake);
	}
	
	public static String getTime_hour(List<?> historyList_24hour, int index) {
		if (historyList_24hour != null) {
			int size = historyList_24hour.size();
			if (index < size && index >= 0) {
				Object history = historyList_24hour.get(index);
				if (history != null) {
					if (history instanceof HistoryHour) {
						Calendar cal = ((HistoryHour) history).getDate();
						int hour = cal.get(Calendar.HOUR_OF_DAY);
						return Global.df_int_2.format(hour) + ":00";
						
					} else {
						return "00:00";
						
					}
					
				}
			}
		}
		
		return "00:00";
	}
	
	
	/**
	 * 计算所有竖线的X值
	 * @param context
	 * @param amount
	 * @return
	 */
	public static float[] calculateXArray(Activity context, int amount) {
		// 保存所有竖线的X值
		float[] xArray = new float[amount + 1];
		
		// 屏幕宽度
		int width = context.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getRight();
		// 显示数值view的宽度
		float length = width - ChartHelper.LEFT - ChartHelper.RIGHT;
		// 间距
		float interval = length / amount;
		
		int mark = ChartHelper.LEFT;
		for (int i = 0; i < amount + 1; i++) {
			xArray[i] = mark + interval * i;
		}
		
		return xArray;
	}
	
	
	/**
	 * 根据滑动坐标X，获取划线的X坐标值
	 * @param xEvent
	 * @param xArray
	 * @return
	 */
	public static float getLineXValue(int xEvent, float[] xArray) {
		for (int i = 0; i + 1 < xArray.length; i++) {
			// 在两条竖线之间，值为左边竖线的值
			if (xEvent >= xArray[i] && xEvent < xArray[i + 1]) {
				
				return xArray[i];
			}
		}
		
		return 0;
	}
	
	
	/**
	 * 根据滑动坐标X，获取下标
	 * @param xEvent
	 * @param xArray
	 * @return
	 */
	public static int getXValueIndex(int xEvent, float[] xArray) {
		for (int i = 0; i + 1 < xArray.length; i++) {
			// 在两条竖线之间，值为左边竖线的值
			if (xEvent >= xArray[i] && xEvent < xArray[i + 1]) {

				return i;
			}
		}
		
		return 0;
	}
}
