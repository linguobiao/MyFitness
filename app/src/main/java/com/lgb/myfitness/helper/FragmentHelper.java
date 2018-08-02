package com.lgb.myfitness.helper;

import com.lgb.myfitness.R;
import com.lgb.myfitness.bpm.main.BPMSettingsFragment;
import com.lgb.myfitness.global.Global;
import com.lgb.myfitness.scale.main.ScaleSettingFragment;
import com.lgb.myfitness.wristband.main.SettingsFragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

public class FragmentHelper {
	/**
	 * 添加fragment
	 * @param fragment
	 * @param fragmentTag
	 */
	public static void addFragment (FragmentManager fMgr, Fragment fragment, String fragmentTag) {
		
		if (fMgr != null) {
			FragmentTransaction ft = fMgr.beginTransaction();
			if (fragment != null) {
				ft.add(R.id.layout_content, fragment, fragmentTag);
				ft.commit();
			}
		}
	}
	
	/**
	 * 显示fragment
	 * @param fragment
	 */
	public static void showFragment (FragmentManager fMgr, Fragment fragment) {
		
		FragmentTransaction ft = fMgr.beginTransaction();
		if (fragment != null) {
			ft.show(fragment);
			ft.commit();
		}
	}
	
	
	/**
	 * 移除一个fragment
	 * @param fMgr
	 * @param fragment
	 */
	public static void removeFragment(FragmentManager fMgr, Fragment fragment) {
		
		if (fragment != null) {
			FragmentTransaction ft = fMgr.beginTransaction();
			
			ft.remove(fragment);
			ft.commit();
		}
	}
	
	

	/**
	 * 隐藏所有fragment
	 */
	public static void hideAllFragment (FragmentManager fMgr) {
		
		FragmentTransaction ft = fMgr.beginTransaction();
		
		// fragment progress
		Fragment fragment_progress = fMgr.findFragmentByTag(Global.FRAGMENT_BAND_PROGRESS);
		if (fragment_progress != null) {
			ft.hide(fragment_progress);
		} 
		// fragment activity
		Fragment fragment_activity = fMgr.findFragmentByTag(Global.FRAGMENT_BAND_ACTIVITY);
		if (fragment_activity != null) {
			ft.hide(fragment_activity);
		}
		// fragment settings
		Fragment fragment_settings = fMgr.findFragmentByTag(Global.FRAGMENT_BAND_SETTINGS);
		if (fragment_settings != null) {
			ft.hide(fragment_settings);
		}
		// fragment settings profile
		Fragment fragment_profile = fMgr.findFragmentByTag(Global.FRAGMENT_BAND_SETTINGS_PROFILE);
		if (fragment_profile != null) {
			ft.hide(fragment_profile);
		}
		// fragment settings goal
		Fragment fragment_goal = fMgr.findFragmentByTag(Global.FRAGMENT_BAND_SETTINGS_GOAL);
		if (fragment_goal != null) {
			ft.hide(fragment_goal);
		}
		// fragment settings reminder
		Fragment fragment_reminder = fMgr.findFragmentByTag(Global.FRAGMENT_BAND_SETTINGS_REMINDER);
		if (fragment_reminder != null) {
			ft.hide(fragment_reminder);
		}
		// fragment settings alarm
		Fragment fragment_alarm = fMgr.findFragmentByTag(Global.FRAGMENT_BAND_SETTINGS_ALARM);
		if (fragment_alarm != null) {
			ft.hide(fragment_alarm);
		}
		// fragment setting about us
		Fragment fragment_about_us = fMgr.findFragmentByTag(Global.FRAGMENT_BAND_SETTINGS_ABOUT_US);
		if (fragment_about_us != null) {
			ft.hide(fragment_about_us);
		}
		// fragment settings user manual
		Fragment fragment_manual = fMgr.findFragmentByTag(Global.FRAGMENT_BAND_SETTINGS_USER_MANUAL);
		if (fragment_manual != null) {
			ft.hide(fragment_manual);
		}
		// fragment_settings_lost_mode
		Fragment fragment_lost_mode = fMgr.findFragmentByTag(Global.FRAGMENT_BAND_SETTINGS_LOST_MODE);
		if (fragment_lost_mode != null) {
			ft.hide(fragment_lost_mode);
		}
		
		ft.commit();
	}
	
	
	/**
	 * 隐藏所有fragment
	 */
	public static void hideAllFragment_scale (FragmentManager fMgr) {
		FragmentTransaction ft = fMgr.beginTransaction();
		
		// fragment scale
		Fragment fragment_scales = fMgr.findFragmentByTag(Global.FRAGMENT_SCALES);
		if (fragment_scales != null) {
			ft.hide(fragment_scales);
		} 
		// fragment settings
		Fragment fragment_settings = fMgr.findFragmentByTag(Global.FRAGMENT_SCALES_SETTINGS);
		if (fragment_settings != null) {
			ft.hide(fragment_settings);
		}
		// fragment settings profile
		Fragment fragment_profile = fMgr.findFragmentByTag(Global.FRAGMENT_SCALES_SETTINGS_PROFILE);
		if (fragment_profile != null) {
			ft.hide(fragment_profile);
		}
		// fragment setting about us
		Fragment fragment_about_us = fMgr.findFragmentByTag(Global.FRAGMENT_SCALES_SETTINGS_ABOUT_US);
		if (fragment_about_us != null) {
			ft.hide(fragment_about_us);
		}
		
		ft.commit();
	}
	
	
	/**
	 * 隐藏所有fragment
	 */
	public static void hideAllFragment_bpm(FragmentManager fMgr) {
		FragmentTransaction ft = fMgr.beginTransaction();
		
		// fragment bpm
		Fragment fragment_bpm = fMgr.findFragmentByTag(Global.FRAGMENT_BPM);
		if (fragment_bpm != null) {
			ft.hide(fragment_bpm);
		} 
		// fragment bpm test
		Fragment fragment_bpm_test = fMgr.findFragmentByTag(Global.FRAGMENT_BPM_TEST);
		if (fragment_bpm_test != null) {
			ft.hide(fragment_bpm_test);
		} 
		// fragment bpm statistics
		Fragment fragment_bpm_statistics = fMgr.findFragmentByTag(Global.FRAGMENT_BPM_STATISTICS);
		if (fragment_bpm_statistics != null) {
			ft.hide(fragment_bpm_statistics);
		}
		// fragment bpm statistics detail
		Fragment fragment_bpm_statistics_detail = fMgr.findFragmentByTag(Global.FRAGMENT_BPM_STATISTICS_DETAIL);
		if (fragment_bpm_statistics_detail != null) {
			ft.hide(fragment_bpm_statistics_detail);
		}
		// fragment settings
		Fragment fragment_settings = fMgr.findFragmentByTag(Global.FRAGMENT_BPM_SETTINGS);
		if (fragment_settings != null) {
			ft.hide(fragment_settings);
		}
		// fragment settings profile
		Fragment fragment_profile = fMgr.findFragmentByTag(Global.FRAGMENT_BPM_SETTINGS_PROFILE);
		if (fragment_profile != null) {
			ft.hide(fragment_profile);
		}
		// fragment setting about us
		Fragment fragment_about_us = fMgr.findFragmentByTag(Global.FRAGMENT_BPM_SETTINGS_ABOUT_US);
		if (fragment_about_us != null) {
			ft.hide(fragment_about_us);
		}
		// fragment setting language
		Fragment fragment_language = fMgr.findFragmentByTag(Global.FRAGMENT_BPM_SETTINGS_LANGUAGE);
		if (fragment_language != null) {
			ft.hide(fragment_language);
		}
		
		ft.commit();
	}
	
	
	/**
	 * 返回事件
	 * @param fm
	 * @param fragment_remove
	 */
	public static void actionBack(Activity activity, Fragment fragment_remove) {
		FragmentManager fMgr = activity.getFragmentManager();
		FragmentHelper.removeFragment(fMgr, fragment_remove);
		
		Fragment fragment_settings = fMgr.findFragmentByTag(Global.FRAGMENT_BAND_SETTINGS);
		if (fragment_settings != null) {
			FragmentHelper.hideAllFragment(fMgr);
			
			// 显示settings fragment
			FragmentHelper.showFragment(fMgr, fragment_settings);
		} else {
			FragmentHelper.hideAllFragment(fMgr);
			
			fragment_settings = new SettingsFragment();
			FragmentHelper.addFragment(fMgr, fragment_settings, Global.FRAGMENT_BAND_SETTINGS);
		}
	}
	
	
	public static void actionBack_scale(Activity activity, Fragment fragment_remove) {
		FragmentManager fMgr = activity.getFragmentManager();
		FragmentHelper.removeFragment(fMgr, fragment_remove);
		
		Fragment fragment_settings = fMgr.findFragmentByTag(Global.FRAGMENT_SCALES_SETTINGS);
		if (fragment_settings != null) {
			FragmentHelper.hideAllFragment_scale(fMgr);
			
			// 显示settings fragment
			FragmentHelper.showFragment(fMgr, fragment_settings);
		} else {
			FragmentHelper.hideAllFragment_scale(fMgr);
			
			fragment_settings = new ScaleSettingFragment();
			FragmentHelper.addFragment(fMgr, fragment_settings, Global.FRAGMENT_SCALES_SETTINGS);
		}
	}
	
	
	public static void actionBack_bpm_setting(Activity activity, Fragment fragment_remove) {
		FragmentManager fMgr = activity.getFragmentManager();
		FragmentHelper.removeFragment(fMgr, fragment_remove);
		
		Fragment fragment_settings = fMgr.findFragmentByTag(Global.FRAGMENT_BPM_SETTINGS);
		if (fragment_settings != null) {
			FragmentHelper.hideAllFragment_bpm(fMgr);
			
			// 显示settings fragment
			FragmentHelper.showFragment(fMgr, fragment_settings);
		} else {
			FragmentHelper.hideAllFragment_bpm(fMgr);
			
			fragment_settings = new BPMSettingsFragment();
			FragmentHelper.addFragment(fMgr, fragment_settings, Global.FRAGMENT_BPM_SETTINGS);
		}
	}
}
