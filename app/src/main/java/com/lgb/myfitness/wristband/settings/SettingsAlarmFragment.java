package com.lgb.myfitness.wristband.settings;

import java.util.Calendar;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.lgb.myfitness.R;
import com.lgb.myfitness.been.Alarm;
import com.lgb.myfitness.database.DatabaseProvider_public;
import com.lgb.myfitness.global.Global;
import com.lgb.myfitness.helper.FragmentHelper;
import com.lgb.myfitness.helper.ProfileHelper;
import com.lgb.myfitness.helper.UnitHelper;
import com.lgb.myfitness.helper.WheelHelper;
import com.lgb.myfitness.wristband.main.ActivityFragment;
import com.lgb.myfitness.wristband.main.MainActivity;
import com.lgb.myfitness.wristband.main.SettingsFragment;

public class SettingsAlarmFragment extends Fragment{
	
	private int profileID = -1;
	private int type_time_display;
 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_band_settings_alarm, container, false);
		initUI(view);
		
		profileID = ProfileHelper.initProfileID(getActivity());
		type_time_display = UnitHelper.getTimeDisplay(getActivity());
		
		initAlarm();
		
		return view;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		try {
            mCallback = (OnAlarmUpdateListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnUpdateListener");
        }
	}

	private OnAlarmUpdateListener mCallback;
	public interface OnAlarmUpdateListener {
		public void endNewStartUp();
	}
	

	/**
	 * my OnClickListener
	 */
	private OnClickListener myOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.button_back:
				actionBack();
				break;
			case R.id.button_save:
				actionSave();
				break;
			case R.id.button_two_wheel_commit:
				actionTime24Commit();
				break;
			case R.id.button_three_wheel_commit:
				actionTime12Commit();
				break;
			case R.id.button_one_wheel_commit:
				actionDurationCommit();
				break;
			case R.id.text_time:
				actionClickTime();
				break;
			case R.id.text_duration:
				actionClickDuration();
				break;
			case R.id.image_title_logo:
				ActivityFragment.actionClickLogo(getActivity());
				break;
			default:
				break;
			}
		}
	};
	
	
	/**
	 * my OnCheckedChangeListener
	 */
	private OnCheckedChangeListener myOnCheckedChangeListener = new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			if (isChecked) {
				alarmSwitchOnState();
			} else {
				alarmSwitchOffState();
			}
			
		}
	};
	
	
	/**
	 * 返回事件
	 */
	private void actionBack() {
		boolean isNewsStartup = MainActivity.getIsNewStartup();
//		Log.i(getTag(), "is new start up: " + isNewsStartup);
		// 如果是new start up, 返回到fragment_profile
		if (isNewsStartup) {
			FragmentManager fMgr = getActivity().getFragmentManager();
			FragmentHelper.removeFragment(fMgr, this);
			FragmentHelper.addFragment(fMgr, new SettingsGoalFragment(), Global.FRAGMENT_BAND_SETTINGS_GOAL);
			
		} else {
			FragmentHelper.actionBack(getActivity(), this);
		}
		
	}
	
	/**
	 * 保存事件
	 */
	private void actionSave() {
		// 保存数据到数据库
		saveAlarmToDatabase();
		
		boolean isNewsStartup = MainActivity.getIsNewStartup();
		Log.i(getTag(), "is new start up: " + isNewsStartup);
		// 如果是new start up 跳转到 fragment_goal
		if (isNewsStartup) {
			FragmentManager fMgr = getActivity().getFragmentManager();
			FragmentHelper.removeFragment(fMgr, this);
			FragmentHelper.addFragment(fMgr, new SettingsFragment(), Global.FRAGMENT_BAND_SETTINGS);
		
			// 标记完成 new start up
			SharedPreferences mPrefs = getActivity().getSharedPreferences(Global.KEY_PREF, Context.MODE_PRIVATE);
			Editor editor = mPrefs.edit();
			editor.putBoolean(Global.KEY_IS_NEW_START_UP_WRISTBAND, false);
			editor.commit();

			mCallback.endNewStartUp();
		} else {
			Toast.makeText(getActivity(), getString(R.string.Save_succeeded), Toast.LENGTH_SHORT).show();		
		}	
		
	}
	
	/**
	 * 保存alarm到数据库
	 */
	private void saveAlarmToDatabase() {
		Alarm alarm = new Alarm();
		
		//开关
		if (button_switch.isChecked()) {
			alarm.setState(Global.ALARM_ON);
		} else {
			alarm.setState(Global.ALARM_OFF);
		}
		
		// 时间
		String time = text_time.getText().toString();
		int index = time.indexOf(":");
		// hour
		String hourStr = time.substring(0, index);
		// minute
		String minuteStr = time.substring(index + 1, index + 1 + 2);	
		int hour = Integer.parseInt(hourStr);
		if (time.contains(Global.ARRAY_AM_PM[1])) {
			hour += 12;
		}
		int minute = Integer.parseInt(minuteStr);
		alarm.setHour(hour);
		alarm.setMinute(minute);
		
		// duration
		String durationStr = text_duration.getText().toString();
		durationStr = durationStr.replaceAll(getString(R.string.mins), "");
		int duration = Integer.parseInt(durationStr);
		alarm.setDuration(duration);	
		
		// repeat
		if (button_mon.isChecked()) {
			alarm.setMonday(Global.ALARM_ON);
		} else {
			alarm.setMonday(Global.ALARM_OFF);
		}
		if (button_tue.isChecked()) {
			alarm.setTuesday(Global.ALARM_ON);
		} else {
			alarm.setTuesday(Global.ALARM_OFF);
		}
		if (button_wed.isChecked()) {
			alarm.setWednesday(Global.ALARM_ON);
		} else {
			alarm.setWednesday(Global.ALARM_OFF);
		}
		if (button_thu.isChecked()) {
			alarm.setThursday(Global.ALARM_ON);
		} else {
			alarm.setThursday(Global.ALARM_OFF);
		}
		if (button_fri.isChecked()) {
			alarm.setFriday(Global.ALARM_ON);
		} else {
			alarm.setFriday(Global.ALARM_OFF);
		}
		if (button_sat.isChecked()) {
			alarm.setSaturday(Global.ALARM_ON);
		} else {
			alarm.setSaturday(Global.ALARM_OFF);
		}
		if (button_sun.isChecked()) {
			alarm.setSunday(Global.ALARM_ON);
		} else {
			alarm.setSunday(Global.ALARM_OFF);
		}
		
		// 保存到数据
		Alarm temp = DatabaseProvider_public.queryAlarm(getActivity(), profileID);
		if (temp == null) {
			DatabaseProvider_public.insertAlarm(getActivity(), profileID, alarm);
		} else {
			DatabaseProvider_public.updateAlarm(getActivity(), profileID, alarm);
		}
	}
	
	
	/**
	 * 点击时间确认
	 */
	private void actionTime24Commit() {
		WheelHelper.hideWheel(view_time_24);
		
		String hour = Global.ARRAY_HOUR_23[wheel_2_left.getCurrentItem()];
		String minute = Global.ARRAY_MINUTE[wheel_2_right.getCurrentItem()];
		
		text_time.setText(hour + ":" + minute);
	}
	
	
	private void actionTime12Commit() {
		WheelHelper.hideWheel(view_time_12);
		
		adjustAmPm();
		String hour = Global.ARRAY_HOUR_11[wheel_3_left.getCurrentItem()];
		String minute = Global.ARRAY_MINUTE[wheel_3_middle.getCurrentItem()];
		String am_pm = Global.ARRAY_AM_PM[wheel_3_right.getCurrentItem()];
		
		
		text_time.setText(hour + ":" + minute + am_pm);
	}
	
	
	private void actionDurationCommit() {
		WheelHelper.hideWheel(view_duration);
		
		String duration = array_period[wheel_duration.getCurrentItem()];
		text_duration.setText(duration);
	}
	
	
	/**
	 * 点击时间
	 */
	private void actionClickTime() {
		WheelHelper.hideWheel(view_duration);
		
		if (type_time_display == Global.TYPE_TIME_DISPLAY_12) {
			WheelHelper.hideWheel(view_time_24);
				
			String time = text_time.getText().toString();
			int index = time.indexOf(":");
			String hourStr = time.substring(0, index);
			String minuteStr = time.substring(index + 1, index + 3);
			String amPmStr = time.substring(index + 3, time.length());
			
//			System.out.println(hourStr + ", " + minuteStr + ", " + amPmStr);
			
			WheelHelper.setWheelCurrentItem(hourStr, Global.ARRAY_HOUR_11, wheel_3_left);
			WheelHelper.setWheelCurrentItem(minuteStr, Global.ARRAY_MINUTE, wheel_3_middle);
			WheelHelper.setWheelCurrentItem(amPmStr, Global.ARRAY_AM_PM, wheel_3_right);
			
			WheelHelper.showWheel(view_time_12);
		} else {
			WheelHelper.hideWheel(view_time_12);
			
			String time = text_time.getText().toString();
			int index = time.indexOf(":");
			String hourStr = time.substring(0, index);
			String minuteStr = time.substring(index + 1, time.length());

			WheelHelper.setWheelCurrentItem(hourStr, Global.ARRAY_HOUR_23, wheel_2_left);
			WheelHelper.setWheelCurrentItem(minuteStr, Global.ARRAY_MINUTE, wheel_2_right);	
			
			WheelHelper.showWheel(view_time_24);
		}
	}
	
	
	private void actionClickDuration() {
		WheelHelper.hideWheel(view_time_24);
		
		String period = text_duration.getText().toString();
		WheelHelper.setWheelCurrentItem(period, array_period, wheel_duration);
		
		WheelHelper.showWheel(view_duration);
	}
	
	
	private void adjustAmPm() {
		int index = wheel_3_left.getCurrentItem();
		if (index > 12) {
			wheel_3_right.setCurrentItem(1);
		} else {
			wheel_3_right.setCurrentItem(0);
		}
	}
	
	
	/**
	 * alarm 开关打开状态，可点击
	 */
	private void alarmSwitchOnState() {
		view_time_24.setVisibility(View.GONE);
		view_time_12.setVisibility(View.GONE);
		view_week_button.setVisibility(View.VISIBLE);
		view_week_button_fake.setVisibility(View.GONE);
		
		text_time.setOnClickListener(myOnClickListener);
		text_duration.setOnClickListener(myOnClickListener);
		
		button_mon.setClickable(true);
		button_tue.setClickable(true);
		button_wed.setClickable(true);
		button_thu.setClickable(true);
		button_fri.setClickable(true);
		button_sat.setClickable(true);
		button_sun.setClickable(true);
	}
	
	
	/**
	 * alarm 开关关闭状态，不可点击
	 */
	private void alarmSwitchOffState() {
		view_time_24.setVisibility(View.GONE);
		view_time_12.setVisibility(View.GONE);
		view_week_button.setVisibility(View.GONE);
		view_week_button_fake.setVisibility(View.VISIBLE);
		
		text_time.setOnClickListener(null);
		text_duration.setOnClickListener(null);
		
		button_mon.setClickable(false);
		button_tue.setClickable(false);
		button_wed.setClickable(false);
		button_thu.setClickable(false);
		button_fri.setClickable(false);
		button_sat.setClickable(false);
		button_sun.setClickable(false);
	}
	
	
	/**
	 * 初始化alarm数据
	 */
	private void initAlarm() {
		
		Alarm alarm = DatabaseProvider_public.queryAlarm(getActivity(), profileID);
		if (alarm != null) {
			int state = alarm.getState();
			int hour = alarm.getHour();
			int minute = alarm.getMinute();
			int duration = alarm.getDuration();
			int monday = alarm.getMonday();
			int tuesday = alarm.getTuesday();
			int wednesday = alarm.getWednesday();
			int thursday = alarm.getThursday();
			int friday = alarm.getFriday();
			int saturday = alarm.getSaturday();
			int sunday = alarm.getSunday();
			
			if (state == Global.ALARM_ON) {
				button_switch.setChecked(true);
			} else {
				button_switch.setChecked(false);
			}
			
			if (type_time_display == Global.TYPE_TIME_DISPLAY_12) {
				if (hour > 12) {
					hour -= 12;
					text_time.setText(Global.df_int_2.format(hour) + ":" + Global.df_int_2.format(minute) + Global.ARRAY_AM_PM[1]);	
				} else {
					text_time.setText(Global.df_int_2.format(hour) + ":" + Global.df_int_2.format(minute) + Global.ARRAY_AM_PM[0]);
				}
			} else {
				text_time.setText(Global.df_int_2.format(hour) + ":" + Global.df_int_2.format(minute));
			}

			text_duration.setText(duration + getString(R.string.mins));
			
			if (monday == Global.ALARM_ON) {
				button_mon.setChecked(true);
			} else {
				button_mon.setChecked(false);
			}
			if (tuesday == Global.ALARM_ON) {
				button_tue.setChecked(true);
			} else {
				button_tue.setChecked(false);
			}
			if (wednesday == Global.ALARM_ON) {
				button_wed.setChecked(true);
			} else {
				button_wed.setChecked(false);
			}
			if (thursday == Global.ALARM_ON) {
				button_thu.setChecked(true);
			} else {
				button_thu.setChecked(false);
			}
			if (friday == Global.ALARM_ON) {
				button_fri.setChecked(true);
			} else {
				button_fri.setChecked(false);
			}
			if (saturday == Global.ALARM_ON) {
				button_sat.setChecked(true);
			} else {
				button_sat.setChecked(false);
			}
			if (sunday== Global.ALARM_ON) {
				button_sun.setChecked(true);
			} else {
				button_sun.setChecked(false);
			}
		} else {
			button_switch.setChecked(false);

			button_mon.setChecked(false);
			button_tue.setChecked(false);
			button_wed.setChecked(false);
			button_thu.setChecked(false);
			button_fri.setChecked(false);
			button_sat.setChecked(false);
			button_sun.setChecked(false);
			
			Calendar cal = Calendar.getInstance();
			int hour = cal.get(Calendar.HOUR_OF_DAY);
			int minute = cal.get(Calendar.MINUTE);
			if (type_time_display == Global.TYPE_TIME_DISPLAY_12) {
				if (hour > 12) {
					hour -= 12;
					text_time.setText(Global.df_int_2.format(hour) + ":" + Global.df_int_2.format(minute) + Global.ARRAY_AM_PM[1]);
				} else {
					text_time.setText(Global.df_int_2.format(hour) + ":" + Global.df_int_2.format(minute) + Global.ARRAY_AM_PM[0]);
				}
			} else {
				text_time.setText(Global.df_int_2.format(hour) + ":" + Global.df_int_2.format(minute));
			}
			text_duration.setText(array_period[0]);
		}
		
		if (button_switch.isChecked()) {
			alarmSwitchOnState();
		} else {
			alarmSwitchOffState();
		}
	}
	
	
	private void initArray() {
		array_period = new String[61];
		for (int i = 0; i < 61; i++) {
			array_period[i] = String.valueOf(i) + getString(R.string.mins);
		}
	}
	
	
	/**
	 * 初始化UI
	 * @param view
	 */
	private void initUI(View view) {
		initArray();
//		Typeface face = Typeface.createFromAsset (getActivity().getAssets() , "fonts/EurostileExtended-Roman-DTC.ttf");
		
		ImageView button_back = (ImageView) view.findViewById(R.id.button_back);
		button_back.setOnClickListener(myOnClickListener);
		ImageView button_save = (ImageView) view.findViewById(R.id.button_save);
		button_save.setOnClickListener(myOnClickListener);
		ImageView image_logo = (ImageView) view.findViewById(R.id.image_title_logo);
		image_logo.setOnClickListener(myOnClickListener);
		
		
		text_time = (TextView) view.findViewById(R.id.text_time);
		text_time.setOnClickListener(myOnClickListener);
		text_duration = (TextView) view.findViewById(R.id.text_duration);
		text_duration.setOnClickListener(myOnClickListener);
		
		button_mon = (ToggleButton) view.findViewById(R.id.button_mon);
		button_tue = (ToggleButton) view.findViewById(R.id.button_tue);
		button_wed = (ToggleButton) view.findViewById(R.id.button_wed);
		button_thu = (ToggleButton) view.findViewById(R.id.button_thu);
		button_fri = (ToggleButton) view.findViewById(R.id.button_fri);
		button_sat = (ToggleButton) view.findViewById(R.id.button_sat);
		button_sun = (ToggleButton) view.findViewById(R.id.button_sun);
		
		button_switch = (ToggleButton) view.findViewById(R.id.button_alarm_switch);
		button_switch.setOnCheckedChangeListener(myOnCheckedChangeListener);
		
		view_time_24 = view.findViewById(R.id.layout_wheel_time_24);
		view_time_12 = view.findViewById(R.id.layout_wheel_time_12);
		view_duration = view.findViewById(R.id.layout_wheel_duration);
		
		Button button_time_commit = (Button) view.findViewById(R.id.button_two_wheel_commit);
		button_time_commit.setOnClickListener(myOnClickListener);
		Button button_duration_commit = (Button) view.findViewById(R.id.button_one_wheel_commit);
		button_duration_commit.setOnClickListener(myOnClickListener);
		Button button_time_12_commit = (Button) view.findViewById(R.id.button_three_wheel_commit);
		button_time_12_commit.setOnClickListener(myOnClickListener);
		
		adapter_hour_23 = new ArrayWheelAdapter<String>(getActivity(), Global.ARRAY_HOUR_23);
		adapter_minute = new ArrayWheelAdapter<String>(getActivity(), Global.ARRAY_MINUTE);
		adapter_duration = new ArrayWheelAdapter<String>(getActivity(), array_period);
		adapter_hour_11 = new ArrayWheelAdapter<String>(getActivity(), Global.ARRAY_HOUR_11);
		adapter_am_pm = new ArrayWheelAdapter<String>(getActivity(), Global.ARRAY_AM_PM);
		
		wheel_2_left = (WheelView) view.findViewById(R.id.wheel_hour);
		wheel_2_left.setViewAdapter(adapter_hour_23);
		wheel_2_right = (WheelView) view.findViewById(R.id.wheel_minute);
		wheel_2_right.setViewAdapter(adapter_minute);
		wheel_duration = (WheelView) view.findViewById(R.id.wheel_wheel);
		wheel_duration.setViewAdapter(adapter_duration);
		wheel_3_left = (WheelView) view.findViewById(R.id.wheel_3_hour);
		wheel_3_left.setViewAdapter(adapter_hour_11);
		wheel_3_left.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_MOVE) {
					adjustAmPm();
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					adjustAmPm();
					new Handler().postDelayed(new Runnable() {
						
						@Override
						public void run() {
							adjustAmPm();
							
						}
					}, 500);
				}
				return false;
			}
		});

		wheel_3_middle = (WheelView) view.findViewById(R.id.wheel_3_minute);
		wheel_3_middle.setViewAdapter(adapter_minute);
		wheel_3_right = (WheelView) view.findViewById(R.id.wheel_am_pm);
		wheel_3_right.setViewAdapter(adapter_am_pm);
		
		view_week_button = view.findViewById(R.id.layout_week_button);
		view_week_button_fake = view.findViewById(R.id.layout_week_button_fake);
		
	}
	
	private ToggleButton button_switch;
	private TextView text_time;
	private ToggleButton button_mon, button_tue, button_wed, button_thu, button_fri, button_sat, button_sun;
	private View view_time_24, view_time_12;
	private WheelView wheel_2_left, wheel_2_right;
	private WheelView wheel_3_left, wheel_3_middle, wheel_3_right;
	private ArrayWheelAdapter<String> adapter_hour_23, adapter_minute, adapter_hour_11, adapter_am_pm;
	private TextView text_duration;
	private View view_duration;
	private WheelView wheel_duration;
	private ArrayWheelAdapter<String> adapter_duration;
	private String[] array_period;
	private View view_week_button, view_week_button_fake;
}
