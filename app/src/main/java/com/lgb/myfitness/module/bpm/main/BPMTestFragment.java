package com.lgb.myfitness.module.bpm.main;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.guoou.sdk.bean.BpmBatteryBean;
import com.guoou.sdk.bean.BpmPressureBean;
import com.guoou.sdk.global.SdkManager;
import com.lgb.mvp.SimpleBaseFragment;
import com.lgb.myfitness.R;
import com.lgb.myfitness.helper.LanguageHelper;
import com.lgb.myfitness.helper.ProfileHelper;
import com.lgb.myfitness.module.bpm.BPMFragment;
import com.lgb.myfitness.module.bpm.BPMFragmentManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;

public class BPMTestFragment extends SimpleBaseFragment{

	@BindView(R.id.button_back) View button_back;
	@BindView(R.id.text_name) TextView text_name;
	@BindView(R.id.text_pressure) TextView text_pressure;
	@BindView(R.id.text_connection) TextView text_connection;
	@BindView(R.id.text_battery) TextView text_battery;
	@BindView(R.id.text_state) TextView text_state;
	@BindView(R.id.image_connection) ImageView image_connection;
	@BindView(R.id.image_battery) ImageView image_battery;
	@BindView(R.id.button_state) Button button_state;

	private OnTestListener mCallback;

	@Override
	public void initView() {

		EventBus.getDefault().register(this);

		stateDisconnected();

		button_state.setOnClickListener(myOnClickListener);
		button_state.setText(getString(R.string.SEARCHING_));

		button_back.setOnClickListener(myOnClickListener);

		initProfile();

		mCallback.onTestConnect();
	}

	@Override protected int getLayoutId() { return R.layout.fragment_bpm_home_test; }


	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallback = (OnTestListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement OnTestListener");
		}
	}

	public interface OnTestListener {
		public void onTestConnect();
		public void onTestDisconnect();
	}

	private OnClickListener myOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.button_back:
				BPMFragmentManager.getInstance().returnMainFragment(BPMFragment.class);
				break;
			case R.id.button_state:
				String state = button_state.getText().toString();
				
				if (state.equals(getString(R.string.START))) {
//					mCallback.onTestStart();
//					SdkManager.getInstance().writeBpmLanguage(LanguageHelper.getLanguageOrder(getActivity()));
					SdkManager.getInstance().writeBpm(0xa1);
					stateStart();
				} else if (state.equals(getString(R.string.STOP))) {
//					mCallback.onTestStop();
					SdkManager.getInstance().writeBpm(0xa2);
					stateStop();
					
				} else if (state.equals(getString(R.string.RECONNECT))) {
					mCallback.onTestConnect();
				}
				break;
			default: break;
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
	
	private void stateStart() {
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

	@Subscribe
	public void onEventPressure(BpmPressureBean event) {
		text_pressure.setText(String.valueOf(event.getPressure()));
	}

	@Subscribe
	public void onEventBattery(BpmBatteryBean event) {
		setBattery(event.getBatteryPercent());
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mCallback.onTestDisconnect();
		EventBus.getDefault().unregister(this);
	}
}
