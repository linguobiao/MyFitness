package com.lgb.myfitness.scale.main;

import com.lgb.myfitness.R;
import com.facebook.Session;
import com.lgb.myfitness.global.Global;
import com.lgb.myfitness.helper.FragmentHelper;
import com.lgb.myfitness.helper.KeyBoardHelper;
import com.lgb.myfitness.scale.settings.ScaleSettingProfileFragment;
import com.lgb.myfitness.scale.settings.ScaleSettingProfileFragment.OnProfileUpdateListener;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class ScaleMainActivity extends Activity implements OnProfileUpdateListener{

	private FragmentManager fMgr = getFragmentManager();
	
	private static boolean isNewStartup;
	public static boolean getIsNewStartup() {
		return isNewStartup;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_scale);
		
		isNewStartup = getIntent().getBooleanExtra(Global.KEY_IS_NEW_START_UP_SCALE, false);
		System.out.println("is new start up :" + isNewStartup);
		
		initUI();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			showQuitAPPDialog(this, keyCode);
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
		
		if (requestCode == Global.REQUEST_ENABLE_BLUETOOTH) {
			if (resultCode == Activity.RESULT_OK) {
				Intent intent = new Intent(Global.ACTION_BLUETOOTH_ENABLE_CONFORM);
				sendBroadcast(intent);
			}
		}

	}
	
	
	/**
	 * 显示退出提示框
	 */
	private void  showQuitAPPDialog(final Context context, final int keyCode) {
		
		new AlertDialog.Builder(context)
		.setTitle(R.string.Notice)
		.setMessage(getString(R.string.exit_app))
		.setPositiveButton(R.string.Exit, new DialogInterface.OnClickListener() {		
			@Override
			public void onClick(DialogInterface dialog, int which) {
				ScaleMainActivity.this.finish();
				System.exit(0);  
				
				ActivityManager am = (ActivityManager)getSystemService (Context.ACTIVITY_SERVICE);
				am.killBackgroundProcesses(getPackageName());
			}
		})
		.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {	
			@Override
			public void onClick(DialogInterface dialog, int which) {	
				
			}
		})
		.show();
	}
	
	
	/**
	 * my OnCheckedChangeListener
	 */
	private OnCheckedChangeListener myOnCheckedChangeListener = new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			switch (buttonView.getId()) {
			case R.id.radio_scale:
				if (isChecked) {
					KeyBoardHelper.hideKeyboard(ScaleMainActivity.this);
					isNewStartup = false;

					setRadioCheckState(radio_scale);
					showFragmentScale();
				} else {
					setRadioUnCheckState(radio_scale);
				}
				break;
			case R.id.radio_settings:
				if (isChecked) {
					KeyBoardHelper.hideKeyboard(ScaleMainActivity.this);
					
					setRadioCheckState(radio_settings);
					text_settings.setVisibility(View.VISIBLE);
					
					if (isNewStartup) {
						Fragment fragment_profile = fMgr.findFragmentByTag(Global.FRAGMENT_SCALES_SETTINGS_PROFILE);
						
						if (fragment_profile == null) {
							FragmentHelper.hideAllFragment_scale(fMgr);
							
							fragment_profile = new ScaleSettingProfileFragment();	
							FragmentHelper.addFragment(fMgr, fragment_profile, Global.FRAGMENT_SCALES_SETTINGS_PROFILE);
						} else {
							FragmentHelper.hideAllFragment_scale(fMgr);
							
							FragmentHelper.showFragment(fMgr, fragment_profile);
						}
					} else {
						showFragmentSettings();	
					}
					
				} else {
					setRadioUnCheckState(radio_settings);
					text_settings.setVisibility(View.GONE);
				}
				break;
			default:
				break;
			}
			
		}
	};
	
	
	/**
	 * my OnClickListener
	 */
	private OnClickListener myOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.text_settings:
				removeSettingSonFragmentAndShowSettingsFragment();
				break;

			default:
				break;
			}
		}
	};
	
	
	/**
	 * 设置radio为选中状态
	 * @param radio
	 */
	private void setRadioCheckState(RadioButton radio) {
		// scale
		if (radio.getId() == R.id.radio_scale) {
			image_scale.setVisibility(View.VISIBLE);
			
			radio_settings.setChecked(false);
		} 
		// settings
		else if (radio.getId() == R.id.radio_settings) {
			image_settings.setVisibility(View.VISIBLE);
			
			radio_scale.setChecked(false);
		} 		
	}
	
	
	
	/**
	 * 设置radio为未选中状态
	 * @param radio
	 */
	private void setRadioUnCheckState(RadioButton radio) {
		
		// scale
		if (radio.getId() == R.id.radio_scale) {
			image_scale.setVisibility(View.INVISIBLE);
		} 
		// settings
		else if (radio.getId() == R.id.radio_settings) {
			image_settings.setVisibility(View.INVISIBLE);
		} 

	}
	
	
	/**
	 * 顯示scale页面
	 */
	private void showFragmentScale() {
		if (fMgr == null) {
			fMgr = getFragmentManager();
		}
		
		Fragment fragment_progress = fMgr.findFragmentByTag(Global.FRAGMENT_SCALES);
		
		if (fragment_progress == null) {
			FragmentHelper.hideAllFragment_scale(fMgr);
			
			fragment_progress = new ScaleFragment();	
			FragmentHelper.addFragment(fMgr, fragment_progress, Global.FRAGMENT_SCALES);			
		} else {
			FragmentHelper.hideAllFragment_scale(fMgr);
			
			FragmentHelper.showFragment(fMgr, fragment_progress);
		}
	}
	
	
	/**
	 * 显示scale settings页面
	 */
	private void showFragmentSettings() {
		if (fMgr == null) {
			fMgr = getFragmentManager();
		}
		
		Fragment fragment_profile = fMgr.findFragmentByTag(Global.FRAGMENT_SCALES_SETTINGS_PROFILE);
		if (fragment_profile != null) {
			FragmentHelper.hideAllFragment_scale(fMgr);
			
			FragmentHelper.showFragment(fMgr, fragment_profile);
		}
		Fragment fragment_about_us = fMgr.findFragmentByTag(Global.FRAGMENT_SCALES_SETTINGS_ABOUT_US);
		if (fragment_about_us != null) {
			FragmentHelper.hideAllFragment_scale(fMgr);
			
			FragmentHelper.showFragment(fMgr, fragment_about_us);
		}
				
		if (fragment_profile == null 
				&& fragment_about_us == null ) {
			Fragment fragment_settings = fMgr.findFragmentByTag(Global.FRAGMENT_SCALES_SETTINGS);
			
			if (fragment_settings == null) {
				FragmentHelper.hideAllFragment_scale(fMgr);
				
				fragment_settings = new ScaleSettingFragment();	
				FragmentHelper.addFragment(fMgr, fragment_settings, Global.FRAGMENT_SCALES_SETTINGS);
			} else {
				FragmentHelper.hideAllFragment_scale(fMgr);
				
				FragmentHelper.showFragment(fMgr, fragment_settings);
			}
		}
	}
	
	
	/**
	 * 移除所有settings子界面，显示settings界面
	 */
	private void removeSettingSonFragmentAndShowSettingsFragment() {
		if (fMgr == null) {
			fMgr = getFragmentManager();
		}
		
		Fragment fragment_profile = fMgr.findFragmentByTag(Global.FRAGMENT_SCALES_SETTINGS_PROFILE);
		if (fragment_profile != null) {
			FragmentHelper.removeFragment(fMgr, fragment_profile);
		}
		Fragment fragment_about_us = fMgr.findFragmentByTag(Global.FRAGMENT_SCALES_SETTINGS_ABOUT_US);
		if (fragment_about_us != null) {
			FragmentHelper.removeFragment(fMgr, fragment_about_us);
		}
		
		Fragment fragment_settings = fMgr.findFragmentByTag(Global.FRAGMENT_SCALES_SETTINGS);
		if (fragment_settings != null) {
			FragmentHelper.hideAllFragment_scale(fMgr);
			
			FragmentHelper.showFragment(fMgr, fragment_settings);
		} else {
			FragmentHelper.hideAllFragment_scale(fMgr);
			
			fragment_settings = new ScaleSettingFragment();
			FragmentHelper.addFragment(fMgr, fragment_settings, Global.FRAGMENT_SCALES_SETTINGS);
		}
	}
	
	
	/**
	 * 初始化UI
	 */
	private void initUI() {
		radio_scale = (RadioButton) findViewById(R.id.radio_scale);
		radio_scale.setOnCheckedChangeListener(myOnCheckedChangeListener);
		
		radio_settings = (RadioButton) findViewById(R.id.radio_settings);
		radio_settings.setOnCheckedChangeListener(myOnCheckedChangeListener);
		
		image_scale = (ImageView) findViewById(R.id.image_scale);
		image_settings = (ImageView) findViewById(R.id.image_settings);
		
		text_settings = (TextView) findViewById(R.id.text_settings);
		text_settings.setOnClickListener(myOnClickListener);
		text_settings.setVisibility(View.GONE);
		
		if (isNewStartup) {
			radio_settings.setChecked(true);
		} else {
			radio_scale.setChecked(true);
		}
	}
	
	private RadioButton radio_scale, radio_settings;
	private ImageView image_scale, image_settings;
	private TextView text_settings;

	@Override
	public void onProfileUpdate() {
		if (fMgr == null) {
			fMgr = getFragmentManager();
		}
		
		ScaleFragment fragment_scale = (ScaleFragment) fMgr.findFragmentByTag(Global.FRAGMENT_SCALES);
		if (fragment_scale != null) {
			fragment_scale.actionProfileUpdate();
		}
		
	}

	@Override
	public void endNewStartUp() {
		// 标记完成 new start up
		SharedPreferences mPrefs = getSharedPreferences(Global.KEY_PREF, Context.MODE_PRIVATE);
		Editor editor = mPrefs.edit();
		editor.putBoolean(Global.KEY_IS_NEW_START_UP_SCALE, false);
		editor.commit();
		
	
		isNewStartup = false;
		radio_scale.setChecked(true);
		
	}
}
