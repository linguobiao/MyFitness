package com.lgb.myfitness.helper;

import com.lgb.myfitness.been.Profile;
import com.lgb.myfitness.database.DatabaseProvider_public;
import com.lgb.myfitness.global.Global;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class ProfileHelper {
	/**
	 * 获取当前使用的用户ID
	 * @param context
	 * @return
	 */
	public static int initProfileID(Context context) {
		
		if (context != null) {
			SharedPreferences mPrefs = context.getSharedPreferences(Global.KEY_PREF, Context.MODE_PRIVATE);
			
			// 获取当前账号名称
			String name = mPrefs.getString(Global.KEY_PROFILE_NAME, "");
			Profile profileInUsed = DatabaseProvider_public.queryProfile(context, name);
			if (profileInUsed != null) {
				int profileID = profileInUsed.getID();
				return profileID;
			}
			
		}
		
		return 1;
	}
	
	
	/**
	 * 获取当前使用的用户名称
	 * @param context
	 * @return
	 */
	public static String getCurrentUseProfileName(Context context) {
		
		if (context != null) {
			SharedPreferences mPrefs = context.getSharedPreferences(Global.KEY_PREF, Context.MODE_PRIVATE);
			
			// 获取当前账号名称
			String name = mPrefs.getString(Global.KEY_PROFILE_NAME, "");
		
			return name;
		}
		
		return "";
	}
	
	
	/**
	 * 保存当前使用的用户名
	 * @param context
	 * @param name
	 */
	public static void saveCurrentUseProfileName(Context context, String name) {
		
		if (context != null && name != null) {
			SharedPreferences mPrefs = context.getSharedPreferences(Global.KEY_PREF, Context.MODE_PRIVATE);
			
			Editor editor = mPrefs.edit();
			editor.putString(Global.KEY_PROFILE_NAME, name);
			editor.commit();
		}
		
	}
}
