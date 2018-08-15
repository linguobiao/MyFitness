package com.lgb.myfitness.module.bpm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.lgb.mvp.SimpleBaseActivity;
import com.lgb.myfitness.been.BPM;
import com.lgb.myfitness.bpm.main.BPMStatisticsDetailFragment;
import com.lgb.myfitness.module.bpm.main.BPMTestFragment;
import com.lgb.myfitness.module.bpm.main.BPMTestFragment.OnTestListener;
import com.lgb.myfitness.bpm.main.TestResultActivity;
import com.lgb.myfitness.module.bpm.settings.BPMSettingsProfileFragment;
import com.lgb.myfitness.module.bpm.settings.BPMSettingsProfileFragment.OnProfileUpdateListener;
import com.lgb.myfitness.database.DatabaseProvider_bpm;
import com.lgb.myfitness.global.Global;
import com.lgb.myfitness.helper.ChartHelper;
import com.lgb.myfitness.helper.DialogHelper;
import com.lgb.myfitness.helper.KeyBoardHelper;
import com.lgb.myfitness.helper.LanguageHelper;
import com.lgb.myfitness.helper.ParserHelper;
import com.lgb.myfitness.helper.ProfileHelper;
import com.lgb.myfitness.helper.SyncHelper;
import com.lgb.myfitness.helper.TimerHelper;
import com.lgb.myfitness.service.MyFitnessService;
import com.lgb.myfitness.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class BPMMainActivity extends SimpleBaseActivity implements OnProfileUpdateListener, OnTestListener{

	private String TAG = "BPMMainActivity";
	
	private FragmentManager fMgr = getFragmentManager();
	
	private MyFitnessService mService = null;
	private BluetoothAdapter mBtAdapter = null;
	
	private Timer timer_scan_timeout;
	
	private ProgressDialog dialog_connecting;
	private ProgressDialog dialog_loading;
	private AlertDialog dialog_result_unusual;
	
	private int profileID;
	
	private static boolean isNewStartup;
	public static boolean getIsNewStartup() {
		return isNewStartup;
	}
	
	private int BLE_TYPE;
	
	private boolean isResult = false;

	@Override
	public int getLayoutId() {
		return R.layout.activity_main_bpm;
	}

	@Override
	public void initView() {
		initUI();
		BPMFragmentManager.getInstance().init(this, R.id.layout_content);

		profileID = ProfileHelper.initProfileID(BPMMainActivity.this);
		isNewStartup = getIntent().getBooleanExtra(Global.KEY_IS_NEW_START_UP_BPM, false);

		if (isNewStartup) {
			radio_settings.setChecked(true);
		} else {
			radio_bpm.setChecked(true);
		}

		initServiceConnection();
		initBLEBroadcastReceiver();
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		unbindService(myServiceConnection);
		unregisterReceiver(myBLEBroadcastReceiver);
		
		disconnectDevice(mService);
		
		DialogHelper.cancelDialog(dialog_connecting);
		DialogHelper.cancelDialog(dialog_loading);
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == Global.REQUEST_ENABLE_BLUETOOTH) {
			if (resultCode == Activity.RESULT_OK) {
				Intent intent = new Intent(Global.ACTION_BLUETOOTH_ENABLE_CONFORM);
				sendBroadcast(intent);
				
			} else {
				receiveDisconnected();
				
			}
		}

	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			showQuitAPPDialog(this, keyCode);
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
	/**
	 * my OnCheckedChangeListener
	 */
	private OnCheckedChangeListener myOnCheckedChangeListener = new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			switch (buttonView.getId()) {
			case R.id.radio_bpm:
				if (isChecked) {
					KeyBoardHelper.hideKeyboard(BPMMainActivity.this);
					isNewStartup = false;

					setRadioCheckState(radio_bpm);
					text_bpm.setVisibility(View.VISIBLE);

					BPMFragmentManager.getInstance().showMainFragment(BPMFragment.class);

				} else {
					setRadioUnCheckState(radio_bpm);
					text_bpm.setVisibility(View.GONE);
				}
				
				break;
			case R.id.radio_settings:
				if (isChecked) {
					KeyBoardHelper.hideKeyboard(BPMMainActivity.this);
					
					setRadioCheckState(radio_settings);
					text_settings.setVisibility(View.VISIBLE);
					
					if (isNewStartup) {
						BPMFragmentManager.getInstance().showFragment(BPMSettingsProfileFragment.class);
					} else {
						BPMFragmentManager.getInstance().showMainFragment(BPMSettingsFragment.class);
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
	private OnClickListener myOnClickListener = v -> {
        switch (v.getId()) {
        case R.id.text_settings:
            BPMFragmentManager.getInstance().returnMainFragment(BPMSettingsFragment.class);
            break;
        case R.id.text_bpm:
            BPMFragmentManager.getInstance().returnMainFragment(BPMFragment.class);
            break;
        default: break;
        }
    };
	
	
	private OnGestureListener myOnGestureListener = new OnGestureListener() {
		
		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			
			return false;
		}
		
		@Override
		public void onShowPress(MotionEvent e) {
			
			
		}
		
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
				float distanceY) {
			
			return false;
		}
		
		@Override
		public void onLongPress(MotionEvent e) {
			
			
		}
		
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			
			 if(e1.getX() - e2.getX() > 120){
				 if (fMgr == null) {
					 fMgr = getFragmentManager();
				 }
				 BPMStatisticsDetailFragment fragment_detail = (BPMStatisticsDetailFragment) fMgr.findFragmentByTag(Global.FRAGMENT_BPM_STATISTICS_DETAIL);
				 if (fragment_detail != null) {
					 fragment_detail.showNextPage();
				 }
                 
                 return true;
             
			 }else if(e1.getX() - e2.getY() < -120){
				 if (fMgr == null) {
					 fMgr = getFragmentManager();
				 }
				 BPMStatisticsDetailFragment fragment_detail = (BPMStatisticsDetailFragment) fMgr.findFragmentByTag(Global.FRAGMENT_BPM_STATISTICS_DETAIL);
				 if (fragment_detail != null) {
					 fragment_detail.showPreviousPage();
				 }
                 
                 return true;
			 }
			 
			return false;
		}
		
		@Override
		public boolean onDown(MotionEvent e) {
			
			return false;
		}
	};
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		return detector.onTouchEvent(event); //touch事件交给手势处理。
	}


	/**
	 * 设置radio为选中状态
	 * @param radio
	 */
	private void setRadioCheckState(RadioButton radio) {
		// scale
		if (radio.getId() == R.id.radio_bpm) {
			image_bpm.setVisibility(View.VISIBLE);
			
			radio_settings.setChecked(false);
		} 
		// settings
		else if (radio.getId() == R.id.radio_settings) {
			image_settings.setVisibility(View.VISIBLE);
			
			radio_bpm.setChecked(false);
		} 		
	}
	
	
	
	/**
	 * 设置radio为未选中状态
	 * @param radio
	 */
	private void setRadioUnCheckState(RadioButton radio) {
		
		// scale
		if (radio.getId() == R.id.radio_bpm) {
			image_bpm.setVisibility(View.INVISIBLE);
		} 
		// settings
		else if (radio.getId() == R.id.radio_settings) {
			image_settings.setVisibility(View.INVISIBLE);
		} 

	}
	
	private BroadcastReceiver myBLEBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			
			if (action.equals(MyFitnessService.ACTION_DEVICE_FOUND)) {
				receiveDeviceFound(intent);
				
			} else if (action.equals(MyFitnessService.ACTION_GATT_CONNECTED)) {
				receiveConnected();
				
			} else if (action.equals(MyFitnessService.ACTION_GATT_DISCONNECTED)) {
				receiveDisconnected();

			} else if (action.equals(MyFitnessService.ACTION_GATT_CONNECTED_FAIL)) {
				receiveDisconnected();
				
			} else if (action.equals(MyFitnessService.ACTION_GATT_SERVICES_DISCOVERED)) {
				receiveServiceDiscovered();
				
			} else if (action.equals(MyFitnessService.ACTION_GATT_SERVICES_DISCOVER_FAIL)) {
				receiveServiceDiscoverFail();
				
			} else if (action.equals(MyFitnessService.ACTION_WRITE_DESCRIPTOR_SUCCESS)) {
				receiveWriteDescriptorSuccess();
				
			} else if (action.equals(MyFitnessService.ACTION_WRITE_DESCRIPTOR_FAIL)) {
				receiveWriteDescriptorFail();
				
			} else if (action.equals(MyFitnessService.ACTION_WRITE_CHARACTERISTIC_SUCCESS)) {
				receiveWriteCharacteristicSuccess();
				
			} else if (action.equals(MyFitnessService.ACTION_WRITE_CHARACTERISTIC_FAIL)) {
				receiveWriteCharacteristicFail();
				
			} else if (action.equals(MyFitnessService.ACTION_DATA_AVAILABLE)) {
				receiveData(intent);
				
			} else if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
				disconnectDevice(mService);
				
			} else if (action.equals(Global.ACTION_BLUETOOTH_ENABLE_CONFORM)) {
				beginScanDevice();
			}
		}
	};

	
	/**
	 * 开始扫描设备
	 */
	private void beginScanDevice() {

		mBtAdapter = SyncHelper.initBluetooth_manual(BPMMainActivity.this);
		if (mBtAdapter != null && mService != null) {
			// 蓝牙是否打开
			if (mBtAdapter.isEnabled()) {
				Log.i(TAG, "connnectionState:" + mService.getConnectionState());

				if (mService.getConnectionState() != MyFitnessService.STATE_CONNECTED) {

					Log.i(TAG, "no connected device, begin to scan");
					showConnectingDialog();
					
					mService.scan(true);
					
					// 开启定时
					mechanismScanTimeOut();

				} else {
					mService.disconnect();

					Log.i(TAG, "no connected device, begin to scan");
					mService.scan(true);
					showConnectingDialog();
				}
			} else {
				
			}
			
		} else {
			Toast.makeText(BPMMainActivity.this, getString(R.string.No_bluetooth_in_device), Toast.LENGTH_SHORT).show();
		}
	}
	
	
	/**
	 * 断开连接
	 * @param mService
	 */
	private void disconnectDevice(MyFitnessService mService) {
		if (mService != null) {
			mService.scan(false);
			mService.disconnect();
		}
		
		DialogHelper.hideDialog(dialog_connecting);
		DialogHelper.hideDialog(dialog_loading);
	}
	
	
	private void receiveDeviceFound(Intent intent) {
		if (intent != null) {
			Bundle data = intent.getExtras();
			BluetoothDevice device = data.getParcelable(BluetoothDevice.EXTRA_DEVICE);

			if (device != null) {
				String deviceName = device.getName();
				String deviceAddress = device.getAddress();
				
				if ((deviceName != null) && (deviceAddress != null)) {
					if (deviceName.contains(Global.DEVICE_NAME_BPMONITOR)) {

						if (mService != null) {
							TimerHelper.cancelTimer(timer_scan_timeout);
							mService.disconnect();
							
							mService.scan(false);
							mService.connect(deviceAddress, false);
						}
					}
				}
			}
		}
	}
	
	
	private void receiveConnected() {
		DialogHelper.hideDialog(dialog_connecting);
		
		if (fMgr == null) {
			fMgr = getFragmentManager();
		}
		
		BPMTestFragment fragment_test = (BPMTestFragment) fMgr.findFragmentByTag(Global.FRAGMENT_BPM_TEST);
		if (fragment_test != null) {
			fragment_test.stateConnected();
		}
	
		showLoadingDialog();
	}
	
	
	private void receiveDisconnected() {
		if (fMgr == null) {
			fMgr = getFragmentManager();
		}
		
		BPMTestFragment fragment_test = (BPMTestFragment) fMgr.findFragmentByTag(Global.FRAGMENT_BPM_TEST);
		if (fragment_test != null) {
			fragment_test.stateDisconnected();
		}
	
		disconnectDevice(mService);
	}
	
	
	private void receiveServiceDiscovered() {
		
		if (mService != null && mService.getConnectionState() == MyFitnessService.STATE_CONNECTED) {
			mService.setBPMNotifyTrue();
		}

	}
	
	
	private void receiveServiceDiscoverFail() {
		DialogHelper.hideDialog(dialog_loading);
		
	}
	
	
	private void receiveWriteDescriptorSuccess() {
		DialogHelper.hideDialog(dialog_loading);
		
	}
	
	
	private void receiveWriteDescriptorFail() {
		disconnectDevice(mService);
	}
	
	
	private void receiveWriteCharacteristicSuccess() {
		Log.i(TAG, "receiveWriteCharacteristicSuccess");
		
		if (BLE_TYPE == Global.TYPE_BPM_LANGUAGE) {
			setBPMonitorStartTest();
			
		} else if (BLE_TYPE == Global.TYPE_BPM_START) {
			
			
		}
		
	}
	
	
	private void receiveWriteCharacteristicFail() {
		if (BLE_TYPE == Global.TYPE_BPM_LANGUAGE) {
			setBPMonitorLanguage();
			
		} else if (BLE_TYPE == Global.TYPE_BPM_START) {
			setBPMonitorStartTest();
			
		}
		
	}
	
	private List<Byte> resultList = new ArrayList<Byte>();
	private void receiveData(Intent intent) {
		if (intent != null) {
			byte[] value = intent.getByteArrayExtra(MyFitnessService.KEY_NOTIFY_DATA);
			
			if (value != null) {
				// 数据长度
				int size = value.length;
				// 转换
				int[] dat = new int[size];
				for (int i = 0; i < size; i++) {

					if (value[i] >= 0)
						dat[i] = (int) value[i];
					else {
						dat[i] = (int) (256 + value[i]);
					}
				}
				
				// 判定数据类型
				int type = 0;
				if (size > 3) {
					// 获取起始坐标
					int index = -1;
					for (int i = 0; i + 3 < size; i++) {
						if (dat[i] == 0x02
								&& dat[i + 1] == 0x40
									&& dat[i + 2] == 0xdd) {
							index = i;
							break;
						}
					}
					
					// 确定类型
					type = dat[index + 3];
					if (type == 0x02) { // pressure
						Log.i(TAG, "pressure: " + Arrays.toString(dat));
						if (index + 5 < size) {
							int pressure = dat[index + 4] * 256 + dat[index + 5] ;
							setTestPressure(pressure);
							
							return;
						}
						
					} else if (type == 0x03) { // battery
						Log.i(TAG, "battery: " + Arrays.toString(dat));
						if (index + 6 < size) {
							int battery = dat[index + 6];
							setTestBattery(battery);
							
							return;
						}
						
					} else if (type == 0x0c) { // result
						Log.i(TAG, "result: " + Arrays.toString(dat));
						
						if (size - index < 17) {
							// 只有数据不完整才设置标记
							isResult = true;
							resultList.clear();
							addToList(value, resultList);
							
						} else {
							parseBPMDataAndShowStopState(value);
						}
						
						return;
					}
				}  
				
				if (type != 0x02 && type != 0x03 && type != 0x0c && isResult) {
					Log.i(TAG, "result remain: " + Arrays.toString(dat));
					
					addToList(value, resultList);
					byte[] resultArray = new byte[resultList.size()];
					setArrayValue(resultArray, resultList);
					
					parseBPMDataAndShowStopState(resultArray);
					
					isResult = false;
				}
			}
		}
	}
	
	
	/**
	 * 五秒内找不到设备就停止扫描设备
	 */
	private void mechanismScanTimeOut() {
		timer_scan_timeout = new Timer();
		timer_scan_timeout.schedule(new TimerTask() {

			@Override
			public void run() {
				// 停止搜索
				mService.scan(false);
				myHandler.sendEmptyMessage(Global.HANDLER_SCAN_TIME_OUT);
			}
		}, Global.TIME_DELAY_STOP_SCAN);
	}
	
	
	private void parseBPMDataAndShowStopState(byte[] resultArray) {
		BPM bpm = ParserHelper.parserBPMValue(BPMMainActivity.this, resultArray);
		
		if (bpm != null) {
			
			if (bpm.getSystolic() <= 300 ) {
				DatabaseProvider_bpm.insertBPM(BPMMainActivity.this, profileID, bpm);
				
				showTestCompleteDialog(bpm);
				
			} else {
				showResultUnusualDialog();
			}
			
		} else {
			DialogHelper.showAlertDialog(BPMMainActivity.this, getString(R.string.Notice), getString(R.string.Test_fail), null);
		}
		
		if (fMgr == null) {
			fMgr = getFragmentManager();
		}
		
		BPMTestFragment fragment_test = (BPMTestFragment) fMgr.findFragmentByTag(Global.FRAGMENT_BPM_TEST);
		if (fragment_test != null) {
			fragment_test.stateStop();
		}
	}
	
	
	private void setTestBattery(int battery) {
		if (fMgr == null) {
			fMgr = getFragmentManager();
		}
		
		BPMTestFragment fragment_test = (BPMTestFragment) fMgr.findFragmentByTag(Global.FRAGMENT_BPM_TEST);
		if (fragment_test != null) {
			fragment_test.setBattery(battery);
		}
	}
	
	
	private void setTestPressure(int pressure) {
		if (fMgr == null) {
			fMgr = getFragmentManager();
		}
		
		BPMTestFragment fragment_test = (BPMTestFragment) fMgr.findFragmentByTag(Global.FRAGMENT_BPM_TEST);
		if (fragment_test != null) {
			fragment_test.setPressure(pressure);
		}
	}
	
	
	private void setBPMonitorLanguage() {
		if (mService != null && mService.getConnectionState() == MyFitnessService.STATE_CONNECTED) {
			
			String language = LanguageHelper.getBPLanguage(BPMMainActivity.this);
			int languageMark = -1;
			if (language.equals(getString(R.string.English))) {
				languageMark = Global.TYPE_BPM_LANGUAGE_ENGLISH;
				
			} else if (language.equals(getString(R.string.German))) {
				languageMark = Global.TYPE_BPM_LANGUAGE_GERMAN;
				
			} else if (language.equals(getString(R.string.French))) {
				languageMark = Global.TYPE_BPM_LANGUAGE_FRENCH;
				
			} else {
				languageMark = Global.TYPE_BPM_LANGUAGE_GERMAN;
			}
			
			BLE_TYPE = Global.TYPE_BPM_LANGUAGE;
			Log.i(TAG, "setBPMonitorLanguage, languageMark:" + languageMark);
			mService.setBPMLanguage(languageMark);
		}
		
	}
	
	
	private void setBPMonitorStartTest() {
		if (mService != null && mService.getConnectionState() == MyFitnessService.STATE_CONNECTED) {
			
			BLE_TYPE = Global.TYPE_BPM_START;
			Log.i(TAG, "setBPMonitorStartTest");
			mService.setBPMTestStart();
			
			if (fMgr == null) {
				fMgr = getFragmentManager();
			}
			
			BPMTestFragment fragment_test = (BPMTestFragment) fMgr.findFragmentByTag(Global.FRAGMENT_BPM_TEST);
			if (fragment_test != null) {
				fragment_test.stateStart();
			}
			
		} else {
			
		}
	}
	
	
	/**
	 * 显示正在连接对话框
	 */
	private void showConnectingDialog() {
		if (dialog_connecting == null) {
			dialog_connecting = DialogHelper.showProgressDialog(BPMMainActivity.this, getString(R.string.bluetooth_connecting));
			dialog_connecting
					.setOnKeyListener(new DialogInterface.OnKeyListener() {

						@Override
						public boolean onKey(DialogInterface dialog,
								int keyCode, KeyEvent event) {
							if (keyCode == KeyEvent.KEYCODE_BACK) {
								
								TimerHelper.cancelTimer(timer_scan_timeout);
								disconnectDevice(mService);
								
								receiveDisconnected();
							}

							return false;
						}
					});
			dialog_connecting.setCanceledOnTouchOutside(false);
			dialog_connecting.show();
		} else {
			dialog_connecting.show();
		}
	}
	
	
	private void showLoadingDialog() {
		if (dialog_loading == null) {
			dialog_loading = DialogHelper.showProgressDialog(BPMMainActivity.this, getString(R.string.Loading));
		
			dialog_loading.setCanceledOnTouchOutside(false);
			dialog_loading.show();
			
		} else {
			dialog_loading.show();
		}
	}
	
	
	private void showResultUnusualDialog() {
		if (dialog_result_unusual == null) {
			dialog_result_unusual = new AlertDialog.Builder(BPMMainActivity.this).create();
			dialog_result_unusual.setTitle(getString(R.string.Notice));
			dialog_result_unusual.setMessage(getString(R.string.BPM_unusual_data));
			dialog_result_unusual.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.ok), new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog_result_unusual.dismiss();
					
				}
			});
			
			dialog_result_unusual.show();
			
		} else {
			dialog_result_unusual.show();
		}
	}
	
	
	private void showTestCompleteDialog(final BPM bpm) {
		final AlertDialog dialog_result = new AlertDialog.Builder(BPMMainActivity.this).create();
		
		dialog_result.setTitle(getString(R.string.Measuring_Done));
		dialog_result.setMessage(getString(R.string.Test_complete));
		dialog_result.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.Cancel), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog_result.dismiss();
				
			}
		});
		dialog_result.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.Check), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (bpm != null) {
					Intent intent = new Intent(BPMMainActivity.this, TestResultActivity.class);
					intent.putExtra(Global.KEY_BPM, bpm);
					
					startActivity(intent);
				}
				
				
			}
		});
		
		dialog_result.show();
	}
	
	
	/**
	 * 显示退出提示框
	 */
	private void showQuitAPPDialog(final Context context, final int keyCode) {
		
		new AlertDialog.Builder(context)
		.setTitle(R.string.Notice)
		.setMessage(getString(R.string.exit_app))
		.setPositiveButton(R.string.Exit, new DialogInterface.OnClickListener() {		
			@Override
			public void onClick(DialogInterface dialog, int which) {
				BPMMainActivity.this.finish();
			}
		})
		.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {	
			@Override
			public void onClick(DialogInterface dialog, int which) {	
				
			}
		})
		.show();
	}
	
	
	private Handler myHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Global.HANDLER_SCAN_TIME_OUT: // 连接超时
				disconnectDevice(mService);
				
				receiveDisconnected();
				
				Toast.makeText(BPMMainActivity.this, getString(R.string.connect_timeout), Toast.LENGTH_SHORT).show();
				
				break;
			default:
				break;
			}
		}

	};
	
	
	private void addToList(byte[] resultArray, List<Byte> resultList) {
		for (int i = 0; i < resultArray.length; i++) {
			resultList.add(resultArray[i]);
		}
	}
	
	
	private void setArrayValue(byte[] resultArray, List<Byte> resultList) {
		for (int i = 0; i < resultList.size(); i++) {
			resultArray[i] = resultList.get(i);
		}
	}
	
	
	private void initBLEBroadcastReceiver() {
		IntentFilter intentFilter = new IntentFilter();
		
		intentFilter.addAction(MyFitnessService.ACTION_DEVICE_FOUND);
		
		intentFilter.addAction(MyFitnessService.ACTION_GATT_CONNECTED);
		intentFilter.addAction(MyFitnessService.ACTION_GATT_DISCONNECTED);
		intentFilter.addAction(MyFitnessService.ACTION_GATT_CONNECTED_FAIL);
		
		intentFilter.addAction(MyFitnessService.ACTION_GATT_SERVICES_DISCOVERED);
		intentFilter.addAction(MyFitnessService.ACTION_GATT_SERVICES_DISCOVER_FAIL);
		
		intentFilter.addAction(MyFitnessService.ACTION_WRITE_DESCRIPTOR_SUCCESS);
		intentFilter.addAction(MyFitnessService.ACTION_WRITE_DESCRIPTOR_FAIL);
		
		intentFilter.addAction(MyFitnessService.ACTION_WRITE_CHARACTERISTIC_SUCCESS);
		intentFilter.addAction(MyFitnessService.ACTION_WRITE_CHARACTERISTIC_FAIL);
		
		intentFilter.addAction(MyFitnessService.ACTION_DATA_AVAILABLE);
		
		intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
		intentFilter.addAction(Global.ACTION_BLUETOOTH_ENABLE_CONFORM);

		registerReceiver(myBLEBroadcastReceiver, intentFilter);
	}
	
	
	/**
	 * 初始化 serviceConnection
	 */
	private void initServiceConnection() {
		Intent bindIntent = new Intent(BPMMainActivity.this, MyFitnessService.class);

		bindService(bindIntent, myServiceConnection,
				Context.BIND_AUTO_CREATE);
	}
	
	
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
		}
	};
	
	
	private void initUI() {
		radio_bpm = (RadioButton) findViewById(R.id.radio_bpm);
		radio_bpm.setOnCheckedChangeListener(myOnCheckedChangeListener);
		
		radio_settings = (RadioButton) findViewById(R.id.radio_settings);
		radio_settings.setOnCheckedChangeListener(myOnCheckedChangeListener);
		
		image_bpm = (ImageView) findViewById(R.id.image_bpm);
		image_settings = (ImageView) findViewById(R.id.image_settings);
		
		text_bpm = (TextView) findViewById(R.id.text_bpm);
		text_bpm.setOnClickListener(myOnClickListener);
		text_bpm.setVisibility(View.GONE);
		
		text_settings = (TextView) findViewById(R.id.text_settings);
		text_settings.setOnClickListener(myOnClickListener);
		text_settings.setVisibility(View.GONE);
		
		int densityDPI = ChartHelper.getDensityDpi(BPMMainActivity.this);
		ChartHelper.setTextSize(densityDPI);
		
		detector = new GestureDetector(BPMMainActivity.this, myOnGestureListener);
		
	}
	private RadioButton radio_bpm, radio_settings;
	private ImageView image_bpm, image_settings;
	private TextView text_bpm, text_settings;
	private GestureDetector detector; //手势检测
	
	@Override
	public void onProfileUpdate() {
		profileID = ProfileHelper.initProfileID(BPMMainActivity.this);
		
	}


	@Override
	public void endNewStartUp() {
		SharedPreferences mPrefs = getSharedPreferences(Global.KEY_PREF, Context.MODE_PRIVATE);
		Editor editor = mPrefs.edit();
		editor.putBoolean(Global.KEY_IS_NEW_START_UP_BPM, false);
		editor.commit();
		
		isNewStartup = false;
		radio_bpm.setChecked(true);
		
	}


	@Override
	public void onTestConnect() {
		isResult = false;
		
		beginScanDevice();
		
	}
	
	
	@Override
	public void onTestDisconnect() {
		disconnectDevice(mService);
		
	}


	@Override
	public void onTestStart() {
//		if (mService != null && mService.getConnectionState() == MyFitnessService.STATE_CONNECTED) {
//			mService.setBPMTestStart();
//			
//			if (fMgr == null) {
//				fMgr = getFragmentManager();
//			}
//			
//			BPMTestFragment fragment_test = (BPMTestFragment) fMgr.findFragmentByTag(Global.FRAGMENT_BPM_TEST);
//			if (fragment_test != null) {
//				fragment_test.stateStart();
//			}
//			
//		} else {
//			
//		}
		
//		setBPMonitorStartTest();
		
		isResult = false;
		
		setBPMonitorLanguage();
	}


	@Override
	public void onTestStop() {
		if (mService != null && mService.getConnectionState() == MyFitnessService.STATE_CONNECTED) {
			mService.setBPMTestStop();
			
			if (fMgr == null) {
				fMgr = getFragmentManager();
			}
			
			BPMTestFragment fragment_test = (BPMTestFragment) fMgr.findFragmentByTag(Global.FRAGMENT_BPM_TEST);
			if (fragment_test != null) {
				fragment_test.stateStop();
			}
			
		} else {
			
		}
		
	}
}
