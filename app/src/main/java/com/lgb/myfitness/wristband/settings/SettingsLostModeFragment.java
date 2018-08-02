package com.lgb.myfitness.wristband.settings;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import com.lgb.myfitness.R;
import com.lgb.myfitness.global.Global;
import com.lgb.myfitness.helper.BindHelper;
import com.lgb.myfitness.helper.FragmentHelper;
import com.lgb.myfitness.helper.ParserHelper;
import com.lgb.myfitness.helper.SyncHelper;
import com.lgb.myfitness.service.MyFitnessService;
import com.lgb.myfitness.wristband.main.ActivityFragment;

import android.app.Activity;
import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class SettingsLostModeFragment extends Fragment{
	
	private MyFitnessService mService = null;
	private BluetoothAdapter mBtAdapter = null;
	
	private String TAG = getTag();
	
	private Timer timer_read_rssi;
	private Timer timer_scan;
	
	private int lostRSSI = -95;
	private List<Integer> currentRSSIList = new ArrayList<Integer>();
	
	private LostModeListener mCallback;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_band_settings_lost_mode, container, false);
		
		initUI(view);
		initServiceConnection();
		initBroadcastReceiver();
		
		return view;
	}
	
	
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		if (timer_read_rssi != null) {
			timer_read_rssi.cancel();
		}
		if (timer_scan != null) {
			timer_scan.cancel();
		}
		
		if (mService != null)
		{
			mService.writeCharacteristic(
					Global.UUID_SERVICE, 
					Global.UUID_CHARACTERISTIC_COMMUNICATION, 
					ParserHelper.getLostModeEndData());
			
			mService.scan(false);
			
//			new Handler().postDelayed(new Runnable() {
//				
//				@Override
//				public void run() {
					mService.disconnect();
//				}
//			}, 100);
			
		}

//		getActivity().stopService(new Intent(getActivity(), MyFitnessService.class));
		getActivity().unbindService(myServiceConnection);
		
		getActivity().unregisterReceiver(myBLEBroadcastReceiver);
	}



	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}
	
	
	public interface LostModeListener {
		public void endLostMode();
	}
	
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		try {
            mCallback = (LostModeListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement LostModeListener");
        }
	}
	
	
	/**
	 * my OnClickListener
	 */
	private OnClickListener myOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.image_title_logo:
				ActivityFragment.actionClickLogo(getActivity());
				
				break;
			case R.id.button_back:
				mCallback.endLostMode();
				FragmentHelper.actionBack(getActivity(), SettingsLostModeFragment.this);
				
				break;
			default:
				break;
			}
		}
	};
	
	
	/**
	 * my ServiceConnection
	 */
	private ServiceConnection myServiceConnection = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {
			mService = null;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mService = ((MyFitnessService.LocalBinder) service).getService();
			
			int connectState = mService.getConnectionState();
			if (connectState == MyFitnessService.STATE_CONNECTED) {
				
			} else if (connectState == MyFitnessService.STATE_DISCONNECTED) {
				mBtAdapter = SyncHelper.initBluetooth_auto(getActivity());
				
				if (mBtAdapter.isEnabled()) {
					mService.scan(true);
				} else {
					mBtAdapter.enable();
				}
			}
		}
	};
	
	/**
	 * my BLE BroadcastReceiver
	 */
	private BroadcastReceiver myBLEBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent){
			String action = intent.getAction();
			// 连上设备
			if (action.equals(MyFitnessService.ACTION_GATT_CONNECTED)) {
			} 
			// 断开设备
			else if (action.equals(MyFitnessService.ACTION_GATT_DISCONNECTED)) {
				
				setDisconnecState();
				
				mService.disconnect();
				disconnectReScan();
			} 
			// 找到设备
			else if (action.equals(MyFitnessService.ACTION_DEVICE_FOUND)){
				Bundle data = intent.getExtras();
				BluetoothDevice device = data.getParcelable(BluetoothDevice.EXTRA_DEVICE);
				
				connectToBLEDevice(device, mService);
			} 
			// 找到服务
			else if (action.equals(MyFitnessService.ACTION_GATT_SERVICES_DISCOVERED)) {
				mService.setCharactoristicNotifyAndWriteDescriptor(
						Global.UUID_SERVICE, 
						Global.UUID_CHARACTERISTIC_COMMUNICATION, 
						Global.UUID_DESCRIPTOR_CONFIGURATION);
			} 
			else if (action.equals(MyFitnessService.ACTION_WRITE_DESCRIPTOR_SUCCESS)) {
				mService.writeCharacteristic(
						Global.UUID_SERVICE, 
						Global.UUID_CHARACTERISTIC_COMMUNICATION, 
						ParserHelper.getLostModeBeginData());
			} 
			else if (action.equals(MyFitnessService.ACTION_RETURN_SPECIAL_KEY)) {
				readRSSI();
			}
			else if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
				Log.i(TAG, "bluetooth state change");
				int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.STATE_OFF);
				
				if (state == BluetoothAdapter.STATE_ON) {
					Log.i(TAG, "bluetooth enable");
					disconnectReScan();
				} else if (state == BluetoothAdapter.STATE_OFF) {
					Log.i(TAG, "bluetooth disable");
				}
			}
			else if (action.equals(MyFitnessService.ACTION_READ_REMOVE_RSSI)) {
				int currentRSSI = intent.getIntExtra(MyFitnessService.KEY_RSSI_VALUE, 0);
				
				addCurrentRSSI(currentRSSIList, currentRSSI);
				int averageRSSI = getAverageRSSI(currentRSSIList);
				Log.i(TAG, "average rssi:" + averageRSSI);
				
				updateUI(averageRSSI);
				if (mService != null) {
					mService.setLostModeRSSI(averageRSSI, lostRSSI);
				}
			}
		}
	};
	
	
	private void disconnectReScan() {
		if (timer_scan != null) {
			timer_scan.cancel();
		}
		
		timer_scan = new Timer();
		timer_scan.schedule(new TimerTask() {
			
			@Override
			public void run() {
				mService.scan(true);
				
			}
		}, 1000);
	}
	
	
	/**
	 * 连接BLE设备
	 * @param device
	 */
	private void connectToBLEDevice(BluetoothDevice device, MyFitnessService mService) {
		
		if (mService != null 
				&& (mService.getConnectionState() != MyFitnessService.STATE_CONNECTED || mService.getConnectionState() != MyFitnessService.STATE_CONNECTING)){
			if (device != null && device.getName() != null) {
				if (device.getName().startsWith(Global.DEVICE_NAME_WRISTBAND)) {
		
					String lastSyncAddress = BindHelper.getLastSyncDeviceAddress(getActivity());
					if (lastSyncAddress != null) {
						String address = device.getAddress();
						if (address.equals(lastSyncAddress)) {
							// 停止扫描
							mService.scan(false);
							
							mService.disconnect();
							mService.connect(device.getAddress(), false);
						} 
					} else {
						// 停止扫描
						mService.scan(false);
						
						mService.disconnect();
						
						mService.connect(device.getAddress(), false);
					}	
				}
			}
		}
	}
	
	
	/**
	 * 根据RSSI值跟新界面
	 * @param rssi
	 */
	private void updateUI(int rssi) {
		if (rssi > - 80) {
			// 0 to 1
			image_signal.setImageResource(R.drawable.image_lost_mode_signal_high);
			text_status.setText("");
			text_status.setText(getString(R.string.Found_Within));
			text_distance.setVisibility(View.VISIBLE);
			text_distance.setText(getString(R.string.distance_1));
		} else if (rssi > -85 && rssi <= -80) {
			// 1 to 2
			image_signal.setImageResource(R.drawable.image_lost_mode_siganl_midum);
			text_status.setText("");
			text_status.setText(getString(R.string.Found_Within));
			text_distance.setVisibility(View.VISIBLE);
			text_distance.setText(getString(R.string.distance_2));
		} else if (rssi > -95 && rssi <= -85) {
			// 2 to 3
			image_signal.setImageResource(R.drawable.image_lost_mode_signal_low);
			text_status.setText("");
			text_status.setText(getString(R.string.Found_Within));
			text_distance.setVisibility(View.VISIBLE);
			text_distance.setText(getString(R.string.distance_3));
		} else if (rssi <= -95) {
			// > 3
			image_signal.setImageResource(R.drawable.image_lost_mode_signal_null);
			text_status.setText("");
			text_status.setText(getString(R.string.Found_Within));
			text_distance.setVisibility(View.VISIBLE);
			text_distance.setText(getString(R.string.distance_4));
		}
	}
	
	
	private void setDisconnecState() {
		image_signal.setImageResource(R.drawable.image_lost_mode_signal_null);
		text_status.setText(getString(R.string.Searching));
		text_distance.setVisibility(View.GONE);
		text_distance.setText(getString(R.string.distance_1));
	}
	
	
	/**
	 * 每500ms读取一次RSSI值
	 */
	private void readRSSI() {
		timer_read_rssi = new Timer();
		timer_read_rssi.schedule(new TimerTask() {
			
			@Override
			public void run() {
				if (mService != null && mService.getConnectionState() == MyFitnessService.STATE_CONNECTED) {
					mService.getRssiVal();
				}
			}
		}, 0, 500);
	}
	
	
	private void addCurrentRSSI(List<Integer> currentRSSIList, int currentRSSI) {
		if (currentRSSIList != null && currentRSSIList.size() > 0) {
			// 移除第一个
			currentRSSIList.remove(0);
			// 添加到最后一个
			currentRSSIList.add(currentRSSI);
		}
	}
	
	
	private int getAverageRSSI(List<Integer> currentRSSIList) {
		if (currentRSSIList != null && currentRSSIList.size() > 0) {
			int sum = 0;
			for (Integer rssi : currentRSSIList) {
				sum += rssi;
			}
			
			return sum / currentRSSIList.size();
		}
		
		return -50;
	}
	
	
	private void initCurrentRSSIList() {
		currentRSSIList.add(-50);
		currentRSSIList.add(-50);
		currentRSSIList.add(-50);
		currentRSSIList.add(-50);
		currentRSSIList.add(-50);
	}
	
	
	/**
	 * 初始化 serviceConnection
	 */
	private void initServiceConnection()
	{
		Intent bindIntent = new Intent(getActivity(), MyFitnessService.class);
//		getActivity().startService(bindIntent);
		getActivity().bindService(bindIntent, myServiceConnection, Context.BIND_AUTO_CREATE);
	}
	
	/**
	 * 初始化 BroadcastReceiver
	 */
	private void initBroadcastReceiver() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(MyFitnessService.ACTION_GATT_CONNECTED);
		intentFilter.addAction(MyFitnessService.ACTION_GATT_DISCONNECTED);
		intentFilter.addAction(MyFitnessService.ACTION_DEVICE_FOUND);
		intentFilter.addAction(MyFitnessService.ACTION_GATT_SERVICES_DISCOVERED);
		intentFilter.addAction(MyFitnessService.ACTION_WRITE_DESCRIPTOR_SUCCESS);
		intentFilter.addAction(MyFitnessService.ACTION_RETURN_SPECIAL_KEY);
		intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
		intentFilter.addAction(MyFitnessService.ACTION_READ_REMOVE_RSSI);

		getActivity().registerReceiver(myBLEBroadcastReceiver, intentFilter);
		
	}
	
	
	/**
	 * 初始化UI
	 * @param view
	 */
	private void initUI(View view) {
		initCurrentRSSIList();
		
		text_status = (TextView) view.findViewById(R.id.text_status);
		text_distance = (TextView) view.findViewById(R.id.text_distance);

		image_signal = (ImageView) view.findViewById(R.id.image_signal);
		
		ImageView image_logo = (ImageView) view.findViewById(R.id.image_title_logo);
		image_logo.setOnClickListener(myOnClickListener);
		
		ImageView button_back = (ImageView) view.findViewById(R.id.button_back);
		button_back.setOnClickListener(myOnClickListener);
	}
	private ImageView image_signal;
	private TextView text_status;
	private TextView text_distance;
}
