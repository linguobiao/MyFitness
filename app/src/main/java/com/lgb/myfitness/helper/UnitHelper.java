package com.lgb.myfitness.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.lgb.myfitness.global.Global;

public class UnitHelper {
	/**
	 * 公制或者英制
	 * @param context
	 * @return
	 */
	public static int getUnit(Context context) {
		
		if (context != null) {
			SharedPreferences mPrefs = context.getSharedPreferences(Global.KEY_PREF, Context.MODE_PRIVATE);
			
			int unit = mPrefs.getInt(Global.KEY_UNIT, Global.TYPE_UNIT_METRIC);
			
			return unit;
		}
		
		return Global.TYPE_UNIT_METRIC;
	}
	
	
	public static int getBurnUnit(Context context) {
		
		if (context != null) {
			SharedPreferences mPrefs = context.getSharedPreferences(Global.KEY_PREF, Context.MODE_PRIVATE);
			
			int unit = mPrefs.getInt(Global.KEY_UNIT_CALORIES, Global.TYPE_CALORIES_KCAL);
			
			return unit;
		}
	
		return Global.TYPE_CALORIES_KCAL;
	}
	
	
	public static int getTimeDisplay(Context context) {
		
		if (context != null) {
			SharedPreferences mPrefs = context.getSharedPreferences(Global.KEY_PREF, Context.MODE_PRIVATE);
			
			int time_display = mPrefs.getInt(Global.KEY_TIME_DISPLAY, Global.TYPE_TIME_DISPLAY_24);
			
			return time_display;
		}
		
		return Global.TYPE_TIME_DISPLAY_24;
	}
}	
