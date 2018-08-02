package com.lgb.myfitness.bpm.main;

import com.lgb.myfitness.global.Global;
import com.lgb.myfitness.helper.FragmentHelper;
import com.lgb.myfitness.helper.ProfileHelper;
import com.lgb.myfitness.R;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class BPMTestFragment extends Fragment{

	private OnTestListener mCallback;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_bpm_home_test, container, false);
		initUI(view);
		
		initProfile();
		
		mCallback.onTestConnect();
		return view;
	}
	
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		try {
			mCallback = (OnTestListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnTestListener");
		}
	}

	
	public interface OnTestListener {
		public void onTestConnect();
		public void onTestDisconnect();
		public void onTestStart();
		public void onTestStop();
	}
	
	
	private OnClickListener myOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.button_back:
				mCallback.onTestDisconnect();
				
				FragmentManager fgmr = getFragmentManager();
				FragmentHelper.removeFragment(fgmr, BPMTestFragment.this);
				
				BPMFragment fragment_bpm = (BPMFragment) fgmr.findFragmentByTag(Global.FRAGMENT_BPM);
				if (fragment_bpm != null) {
					FragmentHelper.hideAllFragment_bpm(fgmr);
					
					fragment_bpm.initBPM();
					FragmentHelper.showFragment(fgmr, fragment_bpm);
					
				} else {
					fragment_bpm = new BPMFragment();
					
					FragmentHelper.addFragment(fgmr, fragment_bpm, Global.FRAGMENT_BPM);
				}
				
				break;
			case R.id.button_state:
				String state = button_state.getText().toString();
				
				if (state.equals(getString(R.string.START))) {
					mCallback.onTestStart();
					
				} else if (state.equals(getString(R.string.STOP))) {
					mCallback.onTestStop();
					
				} else if (state.equals(getString(R.string.RECONNECT))) {
					mCallback.onTestConnect();
					
				}
				
				break;
			default:
				break;
			}
			
		}
	};
	
	
	public void stateConnected() {
		image_connection.setBackgroundResource(R.drawable.background_bpm_pressure_check);
		image_connection.setImageResource(R.drawable.image_bpm_plug_check);
		
		text_connection.setText(getString(R.string.Connected));
		
		text_state.setText(getString(R.string.state_connected));
		
		button_state.setText(getString(R.string.START));
	}
	
	
	public void stateDisconnected() {
		image_connection.setBackgroundResource(R.drawable.background_bpm_pressure_uncheck);
		image_connection.setImageResource(R.drawable.image_bpm_plug_uncheck);
		
		text_connection.setText(getString(R.string.Unconnected));
		
		image_battery.setBackgroundResource(R.drawable.background_bpm_pressure_uncheck);
		image_battery.setImageResource(R.drawable.image_bpm_battery_0_uncheck);
		
		text_battery.setText("0%");
		text_pressure.setText("0");
		
		text_state.setText(getString(R.string.state_disconnected));
		
		button_state.setText(getString(R.string.RECONNECT));
	}
	
	
	public void stateStart() {
		image_connection.setBackgroundResource(R.drawable.background_bpm_pressure_check);
		image_connection.setImageResource(R.drawable.image_bpm_plug_check);
		
		text_connection.setText(getString(R.string.Connected));
		
		text_state.setText(getString(R.string.state_start));
		
		button_state.setText(getString(R.string.STOP));
	}
	
	
	public void stateStop() {
		text_pressure.setText("0");
		
		text_state.setText(getString(R.string.state_connected));
		
		button_state.setText(getString(R.string.START));
	}
	
	
	public void setBattery(int batteryValue) {
		image_battery.setBackgroundResource(R.drawable.background_bpm_pressure_check);
		
		if (batteryValue >= 0 && batteryValue <= 25) {
			image_battery.setImageResource(R.drawable.image_bpm_battery_0_check);
		} else if (batteryValue > 25 && batteryValue <= 50) {
			image_battery.setImageResource(R.drawable.image_bpm_battery_1_check);
		} else if (batteryValue > 50 && batteryValue <= 75) {
			image_battery.setImageResource(R.drawable.image_bpm_battery_2_check);
		} else if (batteryValue > 75 && batteryValue <= 100) {
			image_battery.setImageResource(R.drawable.image_bpm_battery_3_check);
		}
		
		
		text_battery.setText(String.valueOf(batteryValue) + "%");
	}
	
	
	public void setPressure(int pressure) {
		text_pressure.setText(String.valueOf(pressure));
	}
	
	
	private void initProfile() {
		String name = ProfileHelper.getCurrentUseProfileName(getActivity());
		if (name != null) {
			text_name.setText(name);
		}
	}
	
	
	private void initUI(View view) {
		text_name = (TextView) view.findViewById(R.id.text_name);
		
		text_pressure = (TextView) view.findViewById(R.id.text_pressure);
		text_pressure.setText("0");
		
		image_connection = (ImageView) view.findViewById(R.id.image_connection);
		image_connection.setBackgroundResource(R.drawable.background_bpm_pressure_uncheck);
		image_connection.setImageResource(R.drawable.image_bpm_plug_uncheck);
		
		image_battery = (ImageView) view.findViewById(R.id.image_battery);
		image_battery.setBackgroundResource(R.drawable.background_bpm_pressure_uncheck);
		image_battery.setImageResource(R.drawable.image_bpm_battery_0_uncheck);
		
		text_connection = (TextView) view.findViewById(R.id.text_connection);
		text_connection.setText(getString(R.string.Unconnected));
		
		text_battery = (TextView) view.findViewById(R.id.text_battery);
		text_battery.setText("0%");
		
		text_state = (TextView) view.findViewById(R.id.text_state);
		text_state.setText(getString(R.string.state_disconnected));
		
		button_state = (Button) view.findViewById(R.id.button_state);
		button_state.setOnClickListener(myOnClickListener);
		button_state.setText(getString(R.string.SEARCHING_));
		
		ImageView button_back = (ImageView) view.findViewById(R.id.button_back);
		button_back.setOnClickListener(myOnClickListener);
	}
	private TextView text_name;
	private TextView text_pressure;
	private ImageView image_connection, image_battery;
	private TextView text_connection, text_battery;
	private TextView text_state;
	private Button button_state;
}
