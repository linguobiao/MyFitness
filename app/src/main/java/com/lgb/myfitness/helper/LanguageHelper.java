package com.lgb.myfitness.helper;

import com.lgb.myfitness.R;
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

	public static int getLanguageOrder(Context context) {
		String language = LanguageHelper.getBPLanguage(context);
		int languageMark = Global.TYPE_BPM_LANGUAGE_GERMAN;
		if (language.equals(context.getString(R.string.English))) {
			languageMark = Global.TYPE_BPM_LANGUAGE_ENGLISH;
		} else if (language.equals(context.getString(R.string.French))) {
			languageMark = Global.TYPE_BPM_LANGUAGE_FRENCH;
		}
		return languageMark;
	}
}
