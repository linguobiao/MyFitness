package com.lgb.myfitness.helper;

import com.lgb.myfitness.global.Global;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class LanguageHelper {
	
	public static String getBPLanguage(Context context) {
		
		if (context != null) {
			SharedPreferences mPrefs = context.getSharedPreferences(Global.KEY_PREF, Context.MODE_PRIVATE);
			
			String language = mPrefs.getString(Global.KEY_BPM_LANGUAGE, "");
			
			return language;
		}
		
		return "";
	}
	
	
	public static void saveLanguage(Context context, String language) {
		if (context != null && language != null) {
			SharedPreferences mPrefs = context.getSharedPreferences(Global.KEY_PREF, Context.MODE_PRIVATE);
			
			Editor editor = mPrefs.edit();
			editor.putString(Global.KEY_BPM_LANGUAGE, language);
			editor.commit();
			
		}
	}
}
