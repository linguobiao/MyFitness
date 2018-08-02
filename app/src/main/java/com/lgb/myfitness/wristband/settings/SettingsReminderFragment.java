package com.lgb.myfitness.wristband.settings;

import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;

import com.lgb.myfitness.been.Reminder;
import com.lgb.myfitness.database.DatabaseProvider_public;
import com.lgb.myfitness.global.Global;
import com.lgb.myfitness.helper.FragmentHelper;
import com.lgb.myfitness.helper.ProfileHelper;
import com.lgb.myfitness.helper.WheelHelper;
import com.lgb.myfitness.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class SettingsReminderFragment extends Fragment{
	
	private int profileID = -1;
	
	private int clickTimeType = 0;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_band_settings_reminder, container, false);
		initUI(view);
		
		profileID = ProfileHelper.initProfileID(getActivity());
		
		initReminder();
		
		return view;
	}
	
	
	/**
	 * my OnClickListener
	 */
	private OnClickListener myOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.button_back:
				FragmentHelper.actionBack(getActivity(), SettingsReminderFragment.this);
				
				break;
			case R.id.button_save:
				actionSave();
				
				break;
			case R.id.text_start:
				clickTimeType = Global.TYPE_REMINDER_TIME_BEGIN;
				actionClickTimeSelect();
				
				break;
			case R.id.text_end:
				clickTimeType = Global.TYPE_REMINDER_TIME_END;
				actionClickTimeSelect();
				
				break;
			case R.id.text_interval:
				actionClickIntervalSelect();
				
				break;
			case R.id.button_one_wheel_commit:
				actionClickIntervalCommit();
				
				break;
			case R.id.button_two_wheel_commit:
				actionClickTimeCommit();
				
				break;
			default:
				break;
			}
		}
	};
	
	
	/**
	 * my onCheckedChangeListener
	 */
	private OnCheckedChangeListener myOnCheckListener = new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			if (isChecked) {
				reminderSwitchOnState();
			} else {
				reminderSwitchOffState();
			}
		}
	};
	
	
	private void actionClickTimeSelect() {
		
		if (clickTimeType == Global.TYPE_REMINDER_TIME_BEGIN) {
			wheel_hour.setViewAdapter(adapter_begin_hour);
			wheel_minute.setViewAdapter(adapter_minute);
			
			String time = null;
			time = text_time_begin.getText().toString();
			
			if (time != null) {
				int index = time.indexOf(":");
				String hourStr = time.substring(0, index);
				String minuteStr = time.substring(index + 1, time.length());
				
				WheelHelper.setWheelCurrentItem(hourStr, array_begin_hour, wheel_hour);
				WheelHelper.setWheelCurrentItem(minuteStr, array_minute, wheel_minute);
			}
			
		} else if (clickTimeType == Global.TYPE_REMINDER_TIME_END) {
			wheel_hour.setViewAdapter(adapter_end_hour);
			wheel_minute.setViewAdapter(adapter_minute);
			
			String time = null;
			time = text_time_end.getText().toString();
			
			if (time != null) {
				int index = time.indexOf(":");
				String hourStr = time.substring(0, index);
				String minuteStr = time.substring(index + 1, time.length());
				
				WheelHelper.setWheelCurrentItem(hourStr, array_end_hour, wheel_hour);
				WheelHelper.setWheelCurrentItem(minuteStr, array_minute, wheel_minute);
			}
		}
		
		WheelHelper.hideWheel(view_interval);
		WheelHelper.showWheel(view_time);
	}
	
	
	private void actionClickTimeCommit() {
		WheelHelper.hideWheel(view_time);
		
		if (clickTimeType == Global.TYPE_REMINDER_TIME_BEGIN) {
			String hour = array_begin_hour[wheel_hour.getCurrentItem()];
			String minute = Global.ARRAY_MINUTE_ONE[wheel_minute.getCurrentItem()];
			
			text_time_begin.setText(hour + ":" + minute);
			
		} else if (clickTimeType == Global.TYPE_REMINDER_TIME_END) {
			String hour = array_end_hour[wheel_hour.getCurrentItem()];
			String minute = Global.ARRAY_MINUTE_ONE[wheel_minute.getCurrentItem()];
			
			text_time_end.setText(hour + ":" + minute);
		}
	}
	
	
	private void actionClickIntervalSelect() {

		wheel_interval.setViewAdapter(adapter_interval);
		
		String intervalStr = text_interval.getText().toString();
		if (intervalStr != null) {
			WheelHelper.setWheelCurrentItem(intervalStr, array_interval, wheel_interval);
		}
		
		WheelHelper.hideWheel(view_time);
		WheelHelper.showWheel(view_interval);
	}

	
	private void actionClickIntervalCommit() {
		WheelHelper.hideWheel(view_interval);
		
		String interval = array_interval[wheel_interval.getCurrentItem()];
		
		text_interval.setText(interval);
	}
	
	
	private void actionSave() {
		Reminder reminder = new Reminder();
		
		//开关
		if (button_switch.isChecked()) {
			reminder.setState(Global.ALARM_ON);
		} else {
			reminder.setState(Global.ALARM_OFF);
		}
		
		// 时间
		String time_begin = text_time_begin.getText().toString();
		int index = time_begin.indexOf(":");
		String beginHourStr = time_begin.substring(0, index);
		int beginHour = Integer.parseInt(beginHourStr);
		reminder.setBegin_hour(beginHour);
		
		String time_end = text_time_end.getText().toString();
		int index2 = time_end.indexOf(":");
		String endHourStr = time_end.substring(0, index2);
		int endHour = Integer.parseInt(endHourStr);
		reminder.setEnd_hour(endHour);
		
		boolean checkDatetime = SettingsProfileFragment.checkDatatime(time_begin, time_end);
		if (!checkDatetime) {
			Toast.makeText(getActivity(), getString(R.string.Reminder_save_fail), Toast.LENGTH_LONG).show();
//			ToastHelper.showToast(getActivity(), getString(R.string.Reminder_save_fail), Toast.LENGTH_SHORT);
			return;
		}
		
		// interval
		String intervalStr = text_interval.getText().toString();
		intervalStr = intervalStr.replace(getString(R.string.mins), "");
		int interval = Integer.parseInt(intervalStr);
		reminder.setInterval(interval);
		
		// repeat
		if (button_mon.isChecked()) {
			reminder.setMonday(Global.ALARM_ON);
		} else {
			reminder.setMonday(Global.ALARM_OFF);
		}
		if (button_tue.isChecked()) {
			reminder.setTuesday(Global.ALARM_ON);
		} else {
			reminder.setTuesday(Global.ALARM_OFF);
		}
		if (button_wed.isChecked()) {
			reminder.setWednesday(Global.ALARM_ON);
		} else {
			reminder.setWednesday(Global.ALARM_OFF);
		}
		if (button_thu.isChecked()) {
			reminder.setThursday(Global.ALARM_ON);
		} else {
			reminder.setThursday(Global.ALARM_OFF);
		}
		if (button_fri.isChecked()) {
			reminder.setFriday(Global.ALARM_ON);
		} else {
			reminder.setFriday(Global.ALARM_OFF);
		}
		if (button_sat.isChecked()) {
			reminder.setSaturday(Global.ALARM_ON);
		} else {
			reminder.setSaturday(Global.ALARM_OFF);
		}
		if (button_sun.isChecked()) {
			reminder.setSunday(Global.ALARM_ON);
		} else {
			reminder.setSunday(Global.ALARM_OFF);
		}
		
		// 保存到数据
		if (profileID != -1) {
			Reminder temp = DatabaseProvider_public.queryReminder(getActivity(), profileID);
			if (temp == null) {
				DatabaseProvider_public.insertReminder(getActivity(), profileID, reminder);
			} else {
				DatabaseProvider_public.updateReminder(getActivity(), profileID, reminder);
			}
		}
		
		
		Toast.makeText(getActivity(), getString(R.string.Save_succeeded), Toast.LENGTH_LONG).show();
//		ToastHelper.showToast(getActivity(), getString(R.string.Save_succeeded), Toast.LENGTH_SHORT);
	}
	
		
	private void reminderSwitchOnState() {
		view_time.setVisibility(View.GONE);
		view_interval.setVisibility(View.GONE);
		
		view_week_button.setVisibility(View.VISIBLE);
		view_week_button_fake.setVisibility(View.GONE);
		
		text_time_begin.setOnClickListener(myOnClickListener);
		text_time_end.setOnClickListener(myOnClickListener);
		text_interval.setOnClickListener(myOnClickListener);
		
		button_mon.setClickable(true);
		button_tue.setClickable(true);
		button_wed.setClickable(true);
		button_thu.setClickable(true);
		button_fri.setClickable(true);
		button_sat.setClickable(true);
		button_sun.setClickable(true);
	}
	
	
	private void reminderSwitchOffState() {
		view_time.setVisibility(View.GONE);
		view_interval.setVisibility(View.GONE);
		
		view_week_button.setVisibility(View.GONE);
		view_week_button_fake.setVisibility(View.VISIBLE);
		
		text_time_begin.setOnClickListener(null);
		text_time_end.setOnClickListener(null);
		text_interval.setOnClickListener(null);
		
		button_mon.setClickable(false);
		button_tue.setClickable(false);
		button_wed.setClickable(false);
		button_thu.setClickable(false);
		button_fri.setClickable(false);
		button_sat.setClickable(false);
		button_sun.setClickable(false);
	}
	
	
	private void initReminder() {
		
		Reminder reminder = DatabaseProvider_public.queryReminder(getActivity(), profileID);
		
		if (reminder != null) {
			int state = reminder.getState();
			int beginHour = reminder.getBegin_hour();
			int endHour = reminder.getEnd_hour();
			int interval = reminder.getInterval();
			int monday = reminder.getMonday();
			int tuesday = reminder.getTuesday();
			int wednesday = reminder.getWednesday();
			int thursday = reminder.getThursday();
			int friday = reminder.getFriday();
			int saturday = reminder.getSaturday();
			int sunday = reminder.getSunday();
			
			if (state == Global.ALARM_ON) {
				button_switch.setChecked(true);
			} else {
				button_switch.setChecked(false);
			}
			
			text_time_begin.setText(Global.df_int_2.format(beginHour) + ":00");
			text_time_end.setText(Global.df_int_2.format(endHour) + ":00");
			text_interval.setText(interval + getString(R.string.mins));
			
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
			
			text_interval.setText(array_interval[0]);
			text_time_begin.setText(Global.df_int_2.format(Global.DEFAULT_REMINDER_START) + ":00");
			text_time_end.setText(Global.df_int_2.format(Global.DEFAULT_REMINDER_END) + ":00");
		}
		
		if (button_switch.isChecked()) {
			reminderSwitchOnState();
		} else {
			reminderSwitchOffState();
		}
	}
	
	
	private void initArray() {
		array_begin_hour = new String[24];
		for (int i = 0; i < 24; i++) {
			array_begin_hour[i] = Global.df_int_2.format(i);
		}
		
		array_end_hour = new String[24];
		for (int i = 1; i < 25; i++) {
			array_end_hour[i - 1] = Global.df_int_2.format(i);
		}
		
		array_minute = new String[]{"00"};
		
		array_interval = new String[61];
		for (int i = 0; i < 61; i++) {
			array_interval[i] = String.valueOf(i) + getString(R.string.mins);
		}
	}
	
	
	private void initUI(View view) {
		initArray();
		
		ImageView button_back = (ImageView) view.findViewById(R.id.button_back);
		button_back.setOnClickListener(myOnClickListener);
		ImageView button_save = (ImageView) view.findViewById(R.id.button_save);
		button_save.setOnClickListener(myOnClickListener);
		
		button_switch = (ToggleButton) view.findViewById(R.id.button_reminder_switch);
		button_switch.setOnCheckedChangeListener(myOnCheckListener);
		
		text_time_begin = (TextView) view.findViewById(R.id.text_start);
		text_time_end = (TextView) view.findViewById(R.id.text_end);
		text_interval = (TextView) view.findViewById(R.id.text_interval);
		
		button_mon = (ToggleButton) view.findViewById(R.id.button_mon);
		button_tue = (ToggleButton) view.findViewById(R.id.button_tue);
		button_wed = (ToggleButton) view.findViewById(R.id.button_wed);
		button_thu = (ToggleButton) view.findViewById(R.id.button_thu);
		button_fri = (ToggleButton) view.findViewById(R.id.button_fri);
		button_sat = (ToggleButton) view.findViewById(R.id.button_sat);
		button_sun = (ToggleButton) view.findViewById(R.id.button_sun);
		
		view_time = view.findViewById(R.id.layout_wheel_time);
		view_interval = view.findViewById(R.id.layout_wheel_interval);
		
		view_week_button = view.findViewById(R.id.layout_week_button);
		view_week_button_fake = view.findViewById(R.id.layout_week_button_fake);
		
		Button button_time_commit = (Button) view.findViewById(R.id.button_two_wheel_commit);
		button_time_commit.setOnClickListener(myOnClickListener);
		
		Button button_interval_commit = (Button) view.findViewById(R.id.button_one_wheel_commit);
		button_interval_commit.setOnClickListener(myOnClickListener);
		
		wheel_hour = (WheelView) view.findViewById(R.id.wheel_hour);
		wheel_minute = (WheelView) view.findViewById(R.id.wheel_minute);
		wheel_interval = (WheelView) view.findViewById(R.id.wheel_wheel);
		
		adapter_begin_hour = new ArrayWheelAdapter<String>(getActivity(), array_begin_hour);
		adapter_end_hour = new ArrayWheelAdapter<String>(getActivity(), array_end_hour);
		adapter_minute = new ArrayWheelAdapter<String>(getActivity(), array_minute);
		adapter_interval = new ArrayWheelAdapter<String>(getActivity(), array_interval);
	}
	private ToggleButton button_switch;
	private TextView text_time_begin, text_interval, text_time_end;
	private ToggleButton button_mon, button_tue, button_wed, button_thu, button_fri, button_sat, button_sun;
	private View view_time, view_interval;
	private View view_week_button, view_week_button_fake;
	private WheelView wheel_hour, wheel_minute, wheel_interval;
	private ArrayWheelAdapter<String> adapter_begin_hour, adapter_end_hour, adapter_minute, adapter_interval;
	private String[] array_begin_hour, array_end_hour, array_minute, array_interval;
}
