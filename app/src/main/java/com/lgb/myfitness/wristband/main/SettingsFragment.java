package com.lgb.myfitness.wristband.main;

import com.lgb.myfitness.R;
import com.lgb.myfitness.global.Global;
import com.lgb.myfitness.helper.BindHelper;
import com.lgb.myfitness.helper.FragmentHelper;
import com.lgb.myfitness.splash.TypeActivity;
import com.lgb.myfitness.wristband.settings.SettingsAboutUsFragment;
import com.lgb.myfitness.wristband.settings.SettingsAlarmFragment;
import com.lgb.myfitness.wristband.settings.SettingsGoalFragment;
import com.lgb.myfitness.wristband.settings.SettingsLostModeFragment;
import com.lgb.myfitness.wristband.settings.SettingsProfileFragment;
import com.lgb.myfitness.wristband.settings.SettingsReminderFragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class SettingsFragment extends Fragment{
	
	private String TAG = getTag();
	private FragmentManager fMgr;
	private SettingsClickListener mCallback;
		
	
	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		
	}
	
	
	public interface SettingsClickListener {
		public void clickSync();
		public void startLostMode();
	}
	
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		try {
            mCallback = (SettingsClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement SettingsCheckListener");
        }
	}

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_band_settings, container, false);
		initUI(view);
		
		initBindState();
		
		return view;
	}
	
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
	
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}



	/**
	 * my OnClickListener 
	 */
	private OnClickListener myOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.layout_profile:
				actionClickProfile();
				
				break;
			case R.id.layout_goal:
				actionClickGoal();
				
				break;
			case R.id.layout_reminder:
				actionClickReminder();
				
				break;
			case R.id.layout_alarm:
				actionClickAlarm();
				
				break;
			case R.id.layout_about_us:
				actionClickAboutUs();
				
				break;
			case R.id.image_title_logo:
				ActivityFragment.actionClickLogo(getActivity());
				
				break;
			case R.id.button_sync:
				actionClickSync();
				
				break;
			case R.id.button_lost_mode:
				mCallback.startLostMode();
				actionClickLostMode();
				
				break;
			case R.id.layout_choice_device:
				actionClickChoiceDevice();
				
				break;
			case R.id.image_unbound:
				String lastSyncDeivceAddress = BindHelper.getLastSyncDeviceAddress(getActivity());
				if (lastSyncDeivceAddress != null) {
					showUnboundDialog(getActivity());
				}
				break;
			default:
				break;
			}
		}
	};
	
	
	/**
	 * 显示reset notice提示框
	 */
	private void showUnboundDialog(final Context context) {
		
		new AlertDialog.Builder(context)
		.setTitle(R.string.Notice)
		.setMessage(getString(R.string.notice_unbound))
		.setPositiveButton(R.string.Commit, new DialogInterface.OnClickListener() {		
			@Override
			public void onClick(DialogInterface dialog, int which) {
				BindHelper.saveLastSyncDeviceAddress(getActivity(), null);
				image_state.setImageResource(R.drawable.image_state_unbind);
				
				Toast.makeText(context, context.getString(R.string.Unbound_succeeded), Toast.LENGTH_SHORT).show();
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
	 * 隐藏settings fragment
	 */
	private void hideSettingFragment() {
		if (fMgr == null) {
			fMgr = getActivity().getFragmentManager();
		}
		
		SettingsFragment fragment_settings = (SettingsFragment) fMgr.findFragmentByTag(Global.FRAGMENT_BAND_SETTINGS);
		if (fragment_settings != null) {
			FragmentTransaction ft = fMgr.beginTransaction();
			ft.hide(fragment_settings);
			ft.commit();
		}
		
	}
	
	
	/**
	 * 点击profile
	 */
	private void actionClickProfile() {	
		hideSettingFragment();
		
		Fragment fragment_profile = new SettingsProfileFragment();
		FragmentHelper.addFragment(fMgr, fragment_profile, Global.FRAGMENT_BAND_SETTINGS_PROFILE);
		
	}
	
	
	/**
	 * 点击goal
	 */
	private void actionClickGoal() {
		hideSettingFragment();
		
		Fragment fragment_goal = new SettingsGoalFragment();
		FragmentHelper.addFragment(fMgr, fragment_goal, Global.FRAGMENT_BAND_SETTINGS_GOAL);
		
	}
	
	
	/**
	 * 点击reminder
	 */
	private void actionClickReminder() {
		hideSettingFragment();
		
		Fragment fragment_reminder = new SettingsReminderFragment();
		FragmentHelper.addFragment(fMgr, fragment_reminder, Global.FRAGMENT_BAND_SETTINGS_REMINDER);
		
	}
	
	
	
	/**
	 * 点击alarm
	 */
	private void actionClickAlarm() {
		hideSettingFragment();
		
		Fragment fragment_alarm = new SettingsAlarmFragment();
		FragmentHelper.addFragment(fMgr, fragment_alarm, Global.FRAGMENT_BAND_SETTINGS_ALARM);
		
	}
	
	/**
	 * 点击about us
	 */
	private void actionClickAboutUs() {
		hideSettingFragment();
		
		Fragment fragment_about_us = new SettingsAboutUsFragment();
		FragmentHelper.addFragment(fMgr, fragment_about_us, Global.FRAGMENT_BAND_SETTINGS_ABOUT_US);
		
	}
	
	
	private void actionClickSync() {
		int sdk = Build.VERSION.SDK_INT;
		Log.i(TAG, "sdk int : " + sdk);
		if (sdk >= 18) {
//			beginScanDevice();
			mCallback.clickSync();
			
		} else {
			Toast.makeText(getActivity(), getString(R.string.can_not_use_BLE), Toast.LENGTH_SHORT).show();
		}	
	}
	
	
	private void actionClickLostMode() {
		hideSettingFragment();
		
		Fragment fragment_lost_mode = new SettingsLostModeFragment();
		FragmentHelper.addFragment(fMgr, fragment_lost_mode, Global.FRAGMENT_BAND_SETTINGS_LOST_MODE);
	}
	
	
	private void actionClickChoiceDevice() {
		getActivity().finish();
		
		Intent intent = new Intent(getActivity(), TypeActivity.class);
		getActivity().startActivity(intent);
	}
	
	
	public void initBindState() {
		String lastSyncDeivceAddress = BindHelper.getLastSyncDeviceAddress(getActivity());
		// 没有绑定
		if (lastSyncDeivceAddress == null) {
			if (image_state != null) {
				image_state.setImageResource(R.drawable.image_state_unbind);
			}
			
		} else {
			if (image_state != null) {
				image_state.setImageResource(R.drawable.image_state_bind);
			}
			
		}
	}
	

	/**
	 * init UI
	 * @param view
	 */
	private void initUI(View view) {
		
		ImageView image_logo = (ImageView) view.findViewById(R.id.image_title_logo);
		image_logo.setOnClickListener(myOnClickListener);
		image_state = (ImageView) view.findViewById(R.id.image_unbound);
		image_state.setOnClickListener(myOnClickListener);
		
		View view_profile = view.findViewById(R.id.layout_profile);
		view_profile.setOnClickListener(myOnClickListener);
		
		View view_goal = view.findViewById(R.id.layout_goal);
		view_goal.setOnClickListener(myOnClickListener);
		
		View view_reminder = view.findViewById(R.id.layout_reminder);
		view_reminder.setOnClickListener(myOnClickListener);
		
		View view_alarm = view.findViewById(R.id.layout_alarm);
		view_alarm.setOnClickListener(myOnClickListener);
		
		View view_about_us = view.findViewById(R.id.layout_about_us);
		view_about_us.setOnClickListener(myOnClickListener);
		
		View view_choice_device = view.findViewById(R.id.layout_choice_device);
		view_choice_device.setOnClickListener(myOnClickListener);
	
		Button button_sycn = (Button) view.findViewById(R.id.button_sync);
		button_sycn.setOnClickListener(myOnClickListener);
		
		Button button_lost_mode = (Button) view.findViewById(R.id.button_lost_mode);
		button_lost_mode.setOnClickListener(myOnClickListener);
	}
	
	private ImageView image_state;
}
