package com.lgb.myfitness.wristband.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
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
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.lgb.myfitness.R;
import com.lgb.myfitness.audio.AudioTrackManager;
import com.lgb.myfitness.audio.RecordTask;
import com.lgb.myfitness.been.Band;
import com.lgb.myfitness.been.DataRead;
import com.lgb.myfitness.been.HistoryHour;
import com.lgb.myfitness.been.ScreenData;
import com.lgb.myfitness.database.DatabaseProvider_wb013;
import com.lgb.myfitness.global.Global;
import com.lgb.myfitness.helper.BindHelper;
import com.lgb.myfitness.helper.CalculateHelper;
import com.lgb.myfitness.helper.CalendarHelper;
import com.lgb.myfitness.helper.DialogHelper;
import com.lgb.myfitness.helper.FragmentHelper;
import com.lgb.myfitness.helper.KeyBoardHelper;
import com.lgb.myfitness.helper.ParserHelper;
import com.lgb.myfitness.helper.ProfileHelper;
import com.lgb.myfitness.helper.ShareHelper;
import com.lgb.myfitness.helper.SyncHelper;
import com.lgb.myfitness.helper.TimerHelper;
import com.lgb.myfitness.service.MyFitnessService;
import com.lgb.myfitness.wristband.main.ActivityFragment.OnShareListener;
import com.lgb.myfitness.wristband.main.ProgressFragment.ProgressCheckListener;
import com.lgb.myfitness.wristband.main.SettingsFragment.SettingsClickListener;
import com.lgb.myfitness.wristband.settings.SettingsAlarmFragment.OnAlarmUpdateListener;
import com.lgb.myfitness.wristband.settings.SettingsGoalFragment.OnGoalUpdateListener;
import com.lgb.myfitness.wristband.settings.SettingsLostModeFragment.LostModeListener;
import com.lgb.myfitness.wristband.settings.SettingsProfileFragment;
import com.lgb.myfitness.wristband.settings.SettingsProfileFragment.OnProfileUpdateListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity implements OnProfileUpdateListener, OnGoalUpdateListener, OnAlarmUpdateListener, ProgressCheckListener, OnShareListener, SettingsClickListener, LostModeListener{

	private String TAG = "MainActivity";
	
	private FragmentManager fMgr = getFragmentManager();
	/**
	 * 要显示的activity fragment 子tab
	 */
	private int tab_activity;
	
	private AudioTrackManager mAudioTrack;
	private AudioManager audioManager; // 声明AudioManager类
	private int currentVolume; // 标记下当前的音量，用于播放声音命令后及时恢复现场
	private int maxVolume; // 存储可设置的最大音量
	
	private static boolean isNewStartup;
	public static boolean getIsNewStartup() {
		return isNewStartup;
	}
	
	private MyFitnessService mService = null;
	private BluetoothAdapter mBtAdapter = null;
	
	private int profileID = -1;
	private int deviceID = -1;
	
	private Timer timer_finish_sync ;
	private Timer timer_get_screen;
	private Timer timer_set_unit;
	private Timer timer_check_data;
//	private Timer timer_stop_scan;
	private Timer timer_connect;
	
	private ProgressDialog dialog_sharing = null;
	private ProgressDialog dialog_connecting;
	private ProgressDialog dialog_ble_sync;
	private ProgressDialog dialog_audio_sync;
	
	private Map<Band, Integer> deviceMap = new HashMap<Band, Integer>();
	private List<Integer> hourLabelList = new ArrayList<Integer>();
	private int requestHourLabelIndex = 0;
	private int receiveHourAmount = 0;
	
	private int TYPE_JACK = Global.TYPE_JACK_NULL;
	private int TYPE_BLE = Global.TYPE_BLE_NULL;
	
	private boolean isStartLostMode = false;
	private boolean isAlive = false;
	
	private Bitmap bmp;
//	private UiLifecycleHelper uiHelper;
	private boolean canPresentShareDialogWithPhotos;
	private static final String PERMISSION = "publish_actions";
//	private GraphUser user;
	private final String PENDING_ACTION_BUNDLE_KEY = "com.facebook.samples.hellofacebook:PendingAction";
	private static enum PendingAction {NONE, POST_PHOTO, POST_STATUS_UPDATE};
	private PendingAction pendingAction = PendingAction.NONE;
	
	private HistoryHour history_newest;
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	
		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			if (webview != null) {
//				if (webview.canGoBack()) {
//					webview.goBack();
//				} else {
//					System.out.println("webview = " + webview);
//					layout_main.removeView(webview);
//					view_bottom.setVisibility(View.VISIBLE);
//					view_content.setVisibility(View.VISIBLE);
//					webview = null;
//				}
//				return true;
//			} else {
				showQuitAPPDialog(this, keyCode);
//			}
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_wristband);
		initUI();
		
		isNewStartup = getIntent().getBooleanExtra(Global.KEY_IS_NEW_START_UP_WRISTBAND, false);
		Log.i(TAG, "is new start up:" + isNewStartup);
		
		profileID = ProfileHelper.initProfileID(this);
		initBand();

		initAudio();
		init35mmBroadcastReceiver();
		
		initServiceConnection();
		initBLEBroadcastReceiver();
		
//		initFacebook(savedInstanceState);
//		initTwitter();
		
		if (isNewStartup) {
			radio_settings.setChecked(true);
		} else {
			radio_progress.setChecked(true);
		}
	}
	

	@Override
	protected void onResume() {
		super.onResume();
		
		initReceiver();
//		uiHelper.onResume();

		isAlive = true;
	}


	@Override
	protected void onPause() {
		super.onPause();
		
		unregisterReceiver(myBroadcastReceiver);
//		uiHelper.onPause();
		
		isAlive = false;
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		cancelAllTimer();
		
		disconnectDevice(mService);
		
		unbindService(myServiceConnection);
		
		unregisterReceiver(my35mmBroadcastReceiver);
		if (!isStartLostMode) {
			unregisterReceiver(myBLEBroadcastReceiver);
		}
		
		DialogHelper.cancelDialog(dialog_audio_sync);
		DialogHelper.cancelDialog(dialog_connecting);
		DialogHelper.cancelDialog(dialog_ble_sync);
		DialogHelper.cancelDialog(dialog_sharing);

//		uiHelper.onDestroy();
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
//		Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
		
		if (requestCode == Global.REQUEST_ENABLE_BLUETOOTH) {
			if (resultCode == Activity.RESULT_OK) {
				Intent intent = new Intent(Global.ACTION_BLUETOOTH_ENABLE_CONFORM);
				sendBroadcast(intent);
			}
		}

	}
	
	
	/**
	 * myBroadcastReceiver
	 */
	private BroadcastReceiver myBroadcastReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			
			// 要显示 progress fragment
			if (action.equals(Global.ACTION_SHOW_PROGRESS)) {
				radio_progress.setChecked(true);
			} 
			
		}
	};
	
	
	private int timeoutCount = 0;
	/**
	 * 3.5mm 广播接收
	 */
	private BroadcastReceiver my35mmBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			// 成功返回数据，解析
			if (action.equals(Global.ACTION_DECODER_OK)) {
				Log.i(TAG, Global.ACTION_DECODER_OK);
				byte[] dat_bit = intent.getByteArrayExtra("result");
				
				if (TYPE_JACK == Global.TYPE_JACK_DATA) {
					if (dat_bit != null && dat_bit.length == 20) {
						if (dat_bit[18] == 0) {
							DataRead read = ParserHelper.parser35mmValue(dat_bit);
							DatabaseProvider_wb013.deleteHistoryAfterNow(MainActivity.this);
							SyncHelper.saveHistoryDate(MainActivity.this, read, profileID); 
							actionDataUpdate();
							
							Toast.makeText(MainActivity.this, getString(R.string.sync_finish), Toast.LENGTH_SHORT).show();
							setBackToOriginalVolume();
							
						} else if (dat_bit[18] == 1) {
							Log.i(TAG, "存在掉电标志" + dat_bit[18]);
//							SyncHelper.deleteAllHistoryToday(MainActivity.this, profileID, deviceID, Calendar.getInstance());
//							actionDataUpdate();
							
//							showResetDialog(MainActivity.this);
							DialogHelper.showAlertDialog(MainActivity.this, getString(R.string.Notice), getString(R.string.band_is_reset), false);
						}
					}
					
					TYPE_JACK = Global.TYPE_JACK_NULL;
					DialogHelper.cancelDialog(dialog_audio_sync);
					
				}
			}
			// 超时
			else if (action.equals(Global.ACTION_TIME_OUT)) {
				Log.i(TAG, "time out");
				timeoutCount ++;
				if (timeoutCount < 2) {
//					Toast.makeText(context, getString(R.string.mm35_time_out_request_sync_again), Toast.LENGTH_SHORT).show(); 
					
					if (TYPE_JACK == Global.TYPE_JACK_DATA) {
						requestDayData();
					}
				} else {
					Log.i(TAG, "time out count above 2, stop sycn");
					setBackToOriginalVolume();
					DialogHelper.cancelDialog(dialog_audio_sync);
					Toast.makeText(context, getString(R.string.mm35_sync_fail_please_light_up_your_device_and_try_again), Toast.LENGTH_SHORT).show(); 
				}
				
			}
			// 耳机状态
			else if (action.equals("android.intent.action.HEADSET_PLUG")) {
				Log.i(TAG, "headset plug state change");
				if (intent.hasExtra("state")){ 
					// 耳机断开
					if (intent.getIntExtra("state", 0) == 0){ 
						Log.i(TAG, "Audio jack disconnected");
						setBackToOriginalVolume();
						
					}
					// 耳机插入
					else if (intent.getIntExtra("state", 0) == 1){ 
						initBand();
						showAudioSyncDialog();
						
						Log.i(TAG, "Audio jack connected");
						timeoutCount = 0;
						
						requestDayData();
					} 
				}
			}
		}
	};
	
	
	private void requestDayData() {
		TYPE_JACK = Global.TYPE_JACK_DATA;
		
		byte[] value = ParserHelper.requestAllData(MainActivity.this, Global.TYPE_COMMUNICATE);
		sync35mm(value);
	}
	
	
	private void sync35mm(byte[] value) {
		// 播放之前将系统音量设置成最大值
		audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0);
		
		// 发送数据
		mAudioTrack.SendDataInModulation(value);// 测试用数据
		
		// 返回数据
		// 播放完成之后才开始录音
		new Handler().postDelayed(new Runnable() {
			public void run() {
				new RecordTask(MainActivity.this).execute();
			}
		}, 500);		
			
	}
	
	
	private void setBackToOriginalVolume() {
		audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0);
	}
	
	
	/**
	 * my BLE BroadcastReceiver
	 */
	private BroadcastReceiver myBLEBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent){
			String action = intent.getAction();
			// 找到设备
			///////////////////////////////////////////////////////////
			if (action.equals(MyFitnessService.ACTION_DEVICE_FOUND)){
				receiveFoundDevice(intent);
			} 
			// 连接上设备
			///////////////////////////////////////////////////////////
			else if (action.equals(MyFitnessService.ACTION_GATT_CONNECTED)) {
				receiveConnected(MainActivity.this);
			} 
			// 连接失败
			///////////////////////////////////////////////////////////
			else if (action.equals(MyFitnessService.ACTION_GATT_CONNECTED_FAIL)) {
				receiveConnectFail();
			}
			// 断开设备
			//////////////////////////////////////////////////////////
			else if (action.equals(MyFitnessService.ACTION_GATT_DISCONNECTED)) {
				receiveDisconnected();
			} 
			// 找到服务
			//////////////////////////////////////////////////////////
			else if (action.equals(MyFitnessService.ACTION_GATT_SERVICES_DISCOVERED)) {
				receiveServiceDiscovered();
			} 
			else if (action.equals(MyFitnessService.ACTION_GATT_SERVICES_DISCOVER_FAIL)) {
				receiveServiceDiscoverFail();
			}
			// 广播开通成功之后
			/////////////////////////////////////////////////////////
			else if (action.equals(MyFitnessService.ACTION_WRITE_DESCRIPTOR_SUCCESS)) {
				receiveWriteDescriptorSuccess();
			}
			else if (action.equals(MyFitnessService.ACTION_WRITE_DESCRIPTOR_FAIL)) {
				receiveWriteDescriptorFail();
			}
			else if (action.equals(MyFitnessService.ACTION_WRITE_CHARACTERISTIC_SUCCESS)) {
				receiveWriteCharacterSuccess();
			}
			else if (action.equals(MyFitnessService.ACTION_WRITE_CHARACTERISTIC_FAIL)) {
				receiveWriteCharacterFail();
			}
			// 收到广播数据
			else if (action.equals(MyFitnessService.ACTION_DATA_AVAILABLE)) {
				receiveData(intent);
			}
			else if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
				receiveBluetoothDisable(intent);
			}
			else if (action.equals(Global.ACTION_BLUETOOTH_ENABLE_CONFORM)) {
				receiveBluetoothEnable();
				
			}
		}	
	};
	
	
	private void receiveFoundDevice(Intent intent) {
		if (intent != null) {
			Bundle data = intent.getExtras();
			
			if (data != null) {
				BluetoothDevice device = data.getParcelable(BluetoothDevice.EXTRA_DEVICE);
				int rssi = data.getInt(BluetoothDevice.EXTRA_RSSI);
				
				// 连接设备
				matchDevice(deviceMap, device, rssi);
			}
		}
	}
	
	
	private void receiveConnected(Context context) {
		profileID = ProfileHelper.initProfileID(context);
		DialogHelper.hideDialog(dialog_connecting);
		
		// 数据库最新的数据
		history_newest = DatabaseProvider_wb013.queryHistoryHour(context, profileID, deviceID);
		
		showSyncDialog();
	}

	
	private void receiveConnectFail() {
		myHandler.sendEmptyMessage(Global.HANDLER_CONNECT_FAIL);
	}
	
	
	private void receiveDisconnected() {
		DatabaseProvider_wb013.deleteHistoryAfterNow(MainActivity.this);
		actionDataUpdate();
		
		myHandler.sendEmptyMessage(Global.HANDLER_DISCONNECTED);
		
		if (TYPE_BLE == Global.TYPE_BLE_NULL) {
			Log.i(TAG, "sync finish disconnected");
			
		} else {
			Log.i(TAG, "try to reconnect, ble type:" + TYPE_BLE);
//			Toast.makeText(MainActivity.this, getString(R.string.sync_fail), Toast.LENGTH_SHORT).show();
			
			myHandler.sendEmptyMessage(Global.HANDLER_TRY_TO_RECONNECT);
		}
	}
	
	
	private void receiveServiceDiscovered() {
		TYPE_BLE = Global.TYPE_BLE_SET_NOTIFY;
		mService.setBandNotifyTrue();
		
	}
	
	
	private void receiveServiceDiscoverFail() {
		TYPE_BLE = Global.TYPE_BLE_NULL;
		myHandler.sendEmptyMessage(Global.HANDLER_DISCONNECTED);
		
		myHandler.sendEmptyMessage(Global.HANDLER_TRY_TO_RECONNECT);
	}

	
	private void receiveWriteDescriptorSuccess() {
		Log.i(TAG, "receive write descriptor success");
		
		TYPE_BLE = Global.TYPE_BLE_ALL;
		mService.requestAllBLEData(MainActivity.this);
	}
	
	
	private void receiveWriteDescriptorFail() {
		TYPE_BLE = Global.TYPE_BLE_NULL;
		myHandler.sendEmptyMessage(Global.HANDLER_DISCONNECTED);
		
		myHandler.sendEmptyMessage(Global.HANDLER_TRY_TO_RECONNECT);
	}
	
	
	private void receiveWriteCharacterSuccess() {

		if (TYPE_BLE == Global.TYPE_BLE_ALL) {
			
		} else if (TYPE_BLE == Global.TYPE_BLE_1_HOUR) {
			requestHourLabelIndex ++;
			Log.i(TAG, "requestHourLabelIndex ++ ");
			
			if (requestHourLabelIndex < hourLabelList.size()) {
				mService.request1HourBLEData(hourLabelList.get(requestHourLabelIndex));
				
			} else {
//				mechanismFinishSync();
			}
			
		} else if (TYPE_BLE == Global.TYPE_BLE_UNIT) {
			// 获取屏幕数据
			mechanismGetScreenData(mService);
			
		} else if (TYPE_BLE == Global.TYPE_BLE_SCREEN) {
//			mechanismCheckData();
			
		}
	}
	
	
	private void receiveWriteCharacterFail() {
		Log.i(TAG, "write characteristic fail");
		
//		if (TYPE_BLE == Global.TYPE_BLE_ALL) {
//			mService.requestAllBLEData(MainActivity.this);
//			
//		} else if (TYPE_BLE == Global.TYPE_BLE_1_HOUR) {
//			if (requestHourLabelIndex < hourLabelList.size()) {
//				mService.request1HourBLEData(hourLabelList.get(requestHourLabelIndex));
//			} 
//			
//		} else if (TYPE_BLE == Global.TYPE_BLE_UNIT) {
//			mService.setUnitData(MainActivity.this);
//			
//		} else if (TYPE_BLE == Global.TYPE_BLE_SCREEN) {
//			mService.requestScreenData();
//			
//		}
		
		TYPE_BLE = Global.TYPE_BLE_NULL;
		myHandler.sendEmptyMessage(Global.HANDLER_DISCONNECTED);
		
		myHandler.sendEmptyMessage(Global.HANDLER_TRY_TO_RECONNECT);
	}
	
//	private Calendar cal_current_hour;
	
	private void receiveData(Intent intent) {
		if (intent != null) {
			
			byte[] value = intent.getByteArrayExtra(MyFitnessService.KEY_NOTIFY_DATA);
			
			if (TYPE_BLE == Global.TYPE_BLE_ALL) {
				Log.i(TAG, "全部蓝牙数据返回");
				int mark = value[19];
				if (mark == 1) {
					Log.i(TAG, "存在掉电标记");
					// 断开蓝牙连接
					cancelAllTimer();
					TYPE_BLE = Global.TYPE_BLE_NULL;
					disconnectDevice(mService);
					
					// 显示重置选择对话框
//					showResetDialog(MainActivity.this);
					DialogHelper.showAlertDialog(MainActivity.this, getString(R.string.Notice), getString(R.string.band_is_reset), false);
					
				} else {
					Log.i(TAG, "不存在掉电标记");
					// 解析数据
					HistoryHour historyHour = ParserHelper.parserBLEValue(value);
					// 保存数据
//					cal_current_hour = Calendar.getInstance();
//					SyncHelper.saveHistoryHour(MainActivity.this, historyHour, profileID, deviceID, cal_current_hour);
					SyncHelper.saveHistoryHour(MainActivity.this, historyHour, profileID, deviceID);
					
//					// 检查小时数据
//					mechanismCheckData();
					
					// 设置单位
					mechanismSetUnit(mService);
				}
				
			} else if (TYPE_BLE == Global.TYPE_BLE_1_HOUR) {
				Log.i(TAG, "1小时蓝牙数据返回");
				HistoryHour historyHour = ParserHelper.parserBLEValue(value);
				
//				cal_current_hour = Calendar.getInstance();
//				SyncHelper.saveHistoryHour(MainActivity.this, historyHour, profileID, deviceID, cal_current_hour);
				SyncHelper.saveHistoryHour(MainActivity.this, historyHour, profileID, deviceID);
				
				receiveHourAmount ++;
				Log.i(TAG, "receive hour amount:" + receiveHourAmount);
				
				mechanismFinishSync();
			
			}  else if (TYPE_BLE == Global.TYPE_BLE_SCREEN) {
				Log.i(TAG, "screen数据返回");
				
				ScreenData screen = ParserHelper.parserScreenValue(MainActivity.this, value);
				SyncHelper.saveScreenData(MainActivity.this, screen, profileID, deviceID);
				
				mechanismCheckData();
			}
		}
		
	}
	
	
	private void receiveBluetoothEnable() {
		Log.i(TAG, "bluetooth enable");
		int sdk = Build.VERSION.SDK_INT;
		Log.i(TAG, "sdk int : " + sdk);
		if (sdk >= 18) {
			beginScanDevice();
		} else {
			Toast.makeText(MainActivity.this, getString(R.string.can_not_use_BLE), Toast.LENGTH_SHORT).show();
		}	
	}
	
	
	private void receiveBluetoothDisable(Intent intent) {
		Log.i(TAG, "bluetooth state change");
		if (intent != null) {
			int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.STATE_OFF);
			if (state == BluetoothAdapter.STATE_ON) {
				
			} else if (state == BluetoothAdapter.STATE_OFF) {
				TYPE_BLE = Global.TYPE_BLE_NULL;
				Log.i(TAG, "bluetooth disable");
				
				myHandler.sendEmptyMessage(Global.HANDLER_DISCONNECTED);
			}
		}
	}
	
	
	/**
	 * 开始扫描设备
	 */
	private void beginScanDevice() {
		mBtAdapter = SyncHelper.initBluetooth_manual(MainActivity.this);
		if (mBtAdapter != null) {
			// 蓝牙是否打开
			if (mBtAdapter.isEnabled()) {
				Log.i(TAG, "connnectionState:" + mService.getConnectionState());
				
				if (mService.getConnectionState() != MyFitnessService.STATE_CONNECTED) {
					// 清空map
					deviceMap.clear();
					
					Log.i(TAG, "no connected device, begin to scan");
					mService.scan(true);
					showConnectingDialog();
								
					mechanismConnect(mService, deviceMap);
					
				} else {
//					mService.disconnect();
					cancelAllTimer();
					TYPE_BLE = Global.TYPE_BLE_NULL;
					disconnectDevice(mService);
					
					Toast.makeText(MainActivity.this, getString(R.string.sync_fail), Toast.LENGTH_SHORT).show();
				}
			} else {
				
			}
		} else {
			Toast.makeText(MainActivity.this, getString(R.string.No_bluetooth_in_device), Toast.LENGTH_SHORT).show();
		}
	}
	
	
	/**
	 * 寻找合适设备
	 * @param device
	 */
	private void matchDevice(Map<Band, Integer> deviceMap, BluetoothDevice device, int rssi) {
		
		String name = device.getName();
		String address = device.getAddress();
		
		if (device != null && name != null && address != null) {
			if (name.startsWith(Global.DEVICE_NAME_WRISTBAND)) {
				Log.i("find device", address + ", " + rssi);
				
				Band band = new Band();
				band.setName(name);
				band.setAddress(address);
				
				deviceMap.put(band, rssi);
			}
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
		
		DialogHelper.hideDialog(dialog_ble_sync);
		DialogHelper.hideDialog(dialog_connecting);
	}
	
	
	/**
	 * 停止所有定时器
	 */
	private void cancelAllTimer() {

		TimerHelper.cancelTimer(timer_connect);
		TimerHelper.cancelTimer(timer_get_screen);
		TimerHelper.cancelTimer(timer_set_unit);
		TimerHelper.cancelTimer(timer_check_data);
		TimerHelper.cancelTimer(timer_finish_sync);
	}
	
	
	/**
	 * mechanism of connect
	 */
	private void mechanismConnect(final MyFitnessService mService, final Map<Band, Integer> deviceMap) {
		if (timer_connect != null) {
			timer_connect.cancel();
		}
		
		timer_connect = new Timer();
		timer_connect.schedule(new TimerTask() {
			
			@Override
			public void run() {
				// 上次同步的地址
				String lastSyncAddress = BindHelper.getLastSyncDeviceAddress(MainActivity.this);
				if (lastSyncAddress != null) {
					boolean isContain = BindHelper.isContainBoundDevice(deviceMap, lastSyncAddress);
					if (isContain) {
						if (mService != null) {
							TYPE_BLE = Global.TYPE_BLE_CONNECTING;
							
							mService.disconnect();
							mService.scan(false);
							
							initBand();
							
							mService.connect(lastSyncAddress, false);
						}
					} else {
						myHandler.sendEmptyMessage(Global.HANDLER_SCAN_TIME_OUT);
					}
					
				} else {
					// 最近的设备
					Band nearestDevice = BindHelper.getNearestDevice(deviceMap);
					if (nearestDevice != null) {
						if (mService != null) {
							TYPE_BLE = Global.TYPE_BLE_CONNECTING;
							
							BindHelper.saveLastSyncDevice(MainActivity.this, nearestDevice);
							initBand();
							myHandler.sendEmptyMessage(Global.HANDLER_BOUND_DEVICE);
							
							mService.disconnect();
							mService.scan(false);
							
							mService.connect(nearestDevice.getAddress(), false);
						}
						
					} else {
						myHandler.sendEmptyMessage(Global.HANDLER_SCAN_TIME_OUT);
					}
				}
				
			}
		}, Global.TIME_DELAY_CONNECT);
	}
	
	
	/**
	 * 获取屏幕数据的机制
	 * @param mService
	 */
	private void mechanismGetScreenData(final MyFitnessService mService) {
		if (timer_get_screen != null) {
			timer_get_screen.cancel();
		}
		
		timer_get_screen = new Timer();
		timer_get_screen.schedule(new TimerTask() {
			
			@Override
			public void run() {
				TYPE_BLE = Global.TYPE_BLE_SCREEN;
				if (mService != null) {
					mService.requestScreenData();
				}
			}
		}, Global.TIME_DELAY_GET_SCREEN);
	}
	
	
	private void mechanismSetUnit(final MyFitnessService mService) {
		if (timer_set_unit != null) {
			timer_set_unit.cancel();
		}
		
		timer_set_unit = new Timer();
		timer_set_unit.schedule(new TimerTask() {
			
			@Override
			public void run() {
				TYPE_BLE = Global.TYPE_BLE_UNIT;
				if (mService != null) {
					mService.setUnitData(MainActivity.this);
				}
				
			}
		}, Global.TIME_DELAY_SET_UNIT);
	}
	
	
	/**
	 * 检验数据机制
	 */
	private void mechanismCheckData() {
		if (timer_check_data != null) {
			timer_check_data.cancel();
		}
		
		timer_check_data = new Timer();
		timer_check_data.schedule(new TimerTask() {
			
			@Override
			public void run() {
				TYPE_BLE = Global.TYPE_BLE_1_HOUR;
				
				hourLabelList.clear();
				requestHourLabelIndex = 0;
				receiveHourAmount = 0;
				
				// 72小时的map
				Map<Integer, Calendar> hourMap = CalendarHelper.get72HourMap();
				Set<Integer> keySet = hourMap.keySet();
				for (Integer hourLabel : keySet) {
					Calendar cal = hourMap.get(hourLabel);
					
					// 数据库最新的hour label强制加入
					if (history_newest != null && history_newest.getDate() != null && cal.equals(history_newest.getDate())) {
						Log.i(TAG, "database hour newest label is " + hourLabel + " , add it " + cal.getTime());
						
						hourLabelList.add(hourLabel);
						continue;
					}
					
					// 查询数据库看是否存在数据
					HistoryHour temp = DatabaseProvider_wb013.queryHistoryHour(MainActivity.this, profileID, deviceID, cal);
					if (temp == null) {
						System.out.println("数据库没有数据， " + hourLabel + ", " + Global.sdf_1.format(cal.getTime()));
//						mService.request1HourBLEData(hourLabel);
						hourLabelList.add(hourLabel);
					} else {
						System.out.println(hourLabel + ", " + Global.sdf_1.format(cal.getTime()) + ", " + temp.getStep());
					}
				}
				
				hourLabelList = CalculateHelper.getASCsort(hourLabelList);
				Log.i(TAG, "request hour label list size: " + hourLabelList.size());
//				Log.i(TAG, "request hour label list size: " + hourLabelList.size());
				for (int i = 0; i < hourLabelList.size(); i++) {
					Log.i(TAG, i + " --- hour label:" + hourLabelList.get(i));
				}
				Log.i(TAG, "****************************");
				
				if (hourLabelList.size() == 0) {
					// 如果不需要补数据，结束同步
					mechanismFinishSync();
					
				} else {
					requestHourLabelIndex = 0;
					receiveHourAmount = 0;
					
					if (mService != null 
							&& mService.getConnectionState() == MyFitnessService.STATE_CONNECTED
								&& requestHourLabelIndex < hourLabelList.size()) {
						mService.request1HourBLEData(hourLabelList.get(requestHourLabelIndex));
					}
				}
				
			}
		}, Global.TIME_DELAY_CHECK_DATA);
	}
	
	
	private void mechanismFinishSync() {
		if (timer_finish_sync != null) {
			timer_finish_sync.cancel();
		}
		
		timer_finish_sync = new Timer();
		timer_finish_sync.schedule(new TimerTask() {
			
			@Override
			public void run() {
				myHandler.sendEmptyMessage(Global.HANDLER_SYNC_FINISH);
			}
		}, Global.TIME_DELAY_FINISH_SYNC);
	}
	
	
	private void actionDataUpdate() {
		if (fMgr == null) {
			fMgr = getFragmentManager();
		}
		
		ProgressFragment fragment_progress = (ProgressFragment) fMgr.findFragmentByTag(Global.FRAGMENT_BAND_PROGRESS);
		if (fragment_progress != null) {
			fragment_progress.receiveDataUpdate();
		}
		
		ActivityFragment fragment_activity = (ActivityFragment) fMgr.findFragmentByTag(Global.FRAGMENT_BAND_ACTIVITY);
		if (fragment_activity != null) {
			fragment_activity.receiveDataUpdate();
		}
	}
	
	
	private Handler myHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Global.HANDLER_SCAN_TIME_OUT:
				cancelAllTimer();
				TYPE_BLE = Global.TYPE_BLE_NULL;
				disconnectDevice(mService);
				
				Toast.makeText(MainActivity.this, getString(R.string.connect_timeout), Toast.LENGTH_SHORT).show();
				break;
			case Global.HANDLER_DISCONNECTED:
				cancelAllTimer();
				disconnectDevice(mService);
				
				break;
			case Global.HANDLER_CONNECT_FAIL:
				cancelAllTimer();
				TYPE_BLE = Global.TYPE_BLE_NULL;
				disconnectDevice(mService);
				
				Toast.makeText(MainActivity.this, getString(R.string.connect_fail), Toast.LENGTH_SHORT).show();
				
				break;
			case Global.HANDLER_SYNC_FINISH:
				cancelAllTimer();
				TYPE_BLE = Global.TYPE_BLE_NULL;
				disconnectDevice(mService);
				
				Toast.makeText(MainActivity.this, getString(R.string.sync_finish), Toast.LENGTH_SHORT).show();
				
				break;
			case Global.HANDLER_TRY_TO_RECONNECT:
				cancelAllTimer();
				disconnectDevice(mService);
				beginScanDevice();
				
				break;
			case Global.HANDLER_BOUND_DEVICE:
				if (fMgr == null) {
					fMgr = getFragmentManager();
				}
				SettingsFragment fragment_settings = (SettingsFragment) fMgr.findFragmentByTag(Global.FRAGMENT_BAND_SETTINGS);
				if (fragment_settings != null) {
					fragment_settings.initBindState();
				}
				
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
			switch (buttonView.getId()) {
			case R.id.radio_progress:
				if (isChecked) {
					KeyBoardHelper.hideKeyboard(MainActivity.this);
					isNewStartup = false;
					setRadioCheckState(radio_progress);
					showFragmentProgress();
				} else {
					setRadioUnCheckState(radio_progress);
				}
				break;
			case R.id.radio_activity:
				if (isChecked) {
					KeyBoardHelper.hideKeyboard(MainActivity.this);
					isNewStartup = false;
					setRadioCheckState(radio_activity);
					if (tab_activity == 0) {
						showFragmentActivity();
					} else {
						tab_activity = 0;
					}

				} else {
					setRadioUnCheckState(radio_activity);
				}
				break;
			case R.id.radio_settings:
				if (isChecked) {
					KeyBoardHelper.hideKeyboard(MainActivity.this);
					setRadioCheckState(radio_settings);
					text_settings.setVisibility(View.VISIBLE);
					
					if (isNewStartup) {
						Fragment fragment_profile = fMgr.findFragmentByTag(Global.FRAGMENT_BAND_SETTINGS_PROFILE);
						
						if (fragment_profile == null) {
							FragmentHelper.hideAllFragment(fMgr);
							
							fragment_profile = new SettingsProfileFragment();	
							FragmentHelper.addFragment(fMgr, fragment_profile, Global.FRAGMENT_BAND_SETTINGS_PROFILE);
						} else {
							FragmentHelper.hideAllFragment(fMgr);
							
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
				isNewStartup = false;
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
		// progress
		if (radio.getId() == R.id.radio_progress) {
			image_progress.setVisibility(View.VISIBLE);
			
			radio_activity.setChecked(false);
			radio_settings.setChecked(false);
		} 
		// activity
		else if (radio.getId() == R.id.radio_activity) {
			image_activity.setVisibility(View.VISIBLE);
			
			radio_progress.setChecked(false);
			radio_settings.setChecked(false);
		} 
		// settings
		else if (radio.getId() == R.id.radio_settings) {
			image_settings.setVisibility(View.VISIBLE);
			
			radio_progress.setChecked(false);
			radio_activity.setChecked(false);
		} 		
	}
	
	
	
	/**
	 * 设置radio为未选中状态
	 * @param radio
	 */
	private void setRadioUnCheckState(RadioButton radio) {
		
		// progress
		if (radio.getId() == R.id.radio_progress) {
			image_progress.setVisibility(View.INVISIBLE);
		} 
		// activity
		else if (radio.getId() == R.id.radio_activity) {
			image_activity.setVisibility(View.INVISIBLE);
		} 
		// settings
		else if (radio.getId() == R.id.radio_settings) {
			image_settings.setVisibility(View.INVISIBLE);
		} 

	}
	
	
	
	/**
	 * 顯示progress页面
	 */
	private void showFragmentProgress() {
		if (fMgr == null) {
			fMgr = getFragmentManager();
		}
		
		Fragment fragment_progress = fMgr.findFragmentByTag(Global.FRAGMENT_BAND_PROGRESS);
		
		if (fragment_progress == null) {
			FragmentHelper.hideAllFragment(fMgr);
			
			fragment_progress = new ProgressFragment();	
			FragmentHelper.addFragment(fMgr, fragment_progress, Global.FRAGMENT_BAND_PROGRESS);			
		} else {
			FragmentHelper.hideAllFragment(fMgr);
			
			FragmentHelper.showFragment(fMgr, fragment_progress);
		}
	}

	
	
	/**
	 * 显示activity页面
	 */
	private void showFragmentActivity() {
		if (fMgr == null) {
			fMgr = getFragmentManager();
		}
		
		Fragment fragment_activity = fMgr.findFragmentByTag(Global.FRAGMENT_BAND_ACTIVITY);
		
		if (fragment_activity == null) {
			FragmentHelper.hideAllFragment(fMgr);
			
			fragment_activity = new ActivityFragment();	
			FragmentHelper.addFragment(fMgr, fragment_activity, Global.FRAGMENT_BAND_ACTIVITY);
		} else {
			FragmentHelper.hideAllFragment(fMgr);
			
			FragmentHelper.showFragment(fMgr, fragment_activity);
		}
	}
	
	
	/**
	 * 显示settings页面
	 */
	private void showFragmentSettings() {
		if (fMgr == null) {
			fMgr = getFragmentManager();
		}
		
		Fragment fragment_profile = fMgr.findFragmentByTag(Global.FRAGMENT_BAND_SETTINGS_PROFILE);
		if (fragment_profile != null) {
			FragmentHelper.hideAllFragment(fMgr);
			
			FragmentHelper.showFragment(fMgr, fragment_profile);
		}
		Fragment fragment_goal = fMgr.findFragmentByTag(Global.FRAGMENT_BAND_SETTINGS_GOAL);
		if (fragment_goal != null) {
			FragmentHelper.hideAllFragment(fMgr);
			
			FragmentHelper.showFragment(fMgr, fragment_goal);
		}
		Fragment fragment_alarm = fMgr.findFragmentByTag(Global.FRAGMENT_BAND_SETTINGS_ALARM);
		if (fragment_alarm != null) {
			FragmentHelper.hideAllFragment(fMgr);
			
			FragmentHelper.showFragment(fMgr, fragment_alarm);
		}
		Fragment fragment_reminder = fMgr.findFragmentByTag(Global.FRAGMENT_BAND_SETTINGS_REMINDER);
		if (fragment_reminder != null) {
			FragmentHelper.hideAllFragment(fMgr);
			
			FragmentHelper.showFragment(fMgr, fragment_reminder);
		}
		Fragment fragment_about_us = fMgr.findFragmentByTag(Global.FRAGMENT_BAND_SETTINGS_ABOUT_US);
		if (fragment_about_us != null) {
			FragmentHelper.hideAllFragment(fMgr);
			
			FragmentHelper.showFragment(fMgr, fragment_about_us);
		}
		Fragment fragment_lost_mode = fMgr.findFragmentByTag(Global.FRAGMENT_BAND_SETTINGS_LOST_MODE);
		if (fragment_lost_mode != null) {
			FragmentHelper.hideAllFragment(fMgr);
			
			FragmentHelper.showFragment(fMgr, fragment_lost_mode);
		}
				
		
		if (fragment_profile == null 
					&& fragment_goal == null			
						&& fragment_alarm == null
							&& fragment_reminder == null
								&& fragment_about_us == null 
									&& fragment_lost_mode == null) {
			Fragment fragment_settings = fMgr.findFragmentByTag(Global.FRAGMENT_BAND_SETTINGS);
			
			if (fragment_settings == null) {
				FragmentHelper.hideAllFragment(fMgr);
				
				fragment_settings = new SettingsFragment();	
				FragmentHelper.addFragment(fMgr, fragment_settings, Global.FRAGMENT_BAND_SETTINGS);
			} else {
				FragmentHelper.hideAllFragment(fMgr);
				
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
		
		Fragment fragment_profile = fMgr.findFragmentByTag(Global.FRAGMENT_BAND_SETTINGS_PROFILE);
		if (fragment_profile != null) {
			FragmentHelper.removeFragment(fMgr, fragment_profile);
		}
		Fragment fragment_goal = fMgr.findFragmentByTag(Global.FRAGMENT_BAND_SETTINGS_GOAL);
		if (fragment_goal != null) {
			FragmentHelper.removeFragment(fMgr, fragment_goal);
		}
		Fragment fragment_reminder = fMgr.findFragmentByTag(Global.FRAGMENT_BAND_SETTINGS_REMINDER);
		if (fragment_reminder != null) {
			FragmentHelper.removeFragment(fMgr, fragment_reminder);
		}
		Fragment fragment_alarm = fMgr.findFragmentByTag(Global.FRAGMENT_BAND_SETTINGS_ALARM);
		if (fragment_alarm != null) {
			FragmentHelper.removeFragment(fMgr, fragment_alarm);
		}
		Fragment fragment_about_us = fMgr.findFragmentByTag(Global.FRAGMENT_BAND_SETTINGS_ABOUT_US);
		if (fragment_about_us != null) {
			FragmentHelper.removeFragment(fMgr, fragment_about_us);
		}
		Fragment fragment_lost_mode = fMgr.findFragmentByTag(Global.FRAGMENT_BAND_SETTINGS_LOST_MODE);
		if (fragment_lost_mode != null) {
			FragmentHelper.removeFragment(fMgr, fragment_lost_mode);
			
			endLostMode();
		}
		
		Fragment fragment_settings = fMgr.findFragmentByTag(Global.FRAGMENT_BAND_SETTINGS);
		if (fragment_settings != null) {
			FragmentHelper.hideAllFragment(fMgr);
			
			FragmentHelper.showFragment(fMgr, fragment_settings);
		} else {
			FragmentHelper.hideAllFragment(fMgr);
			
			fragment_settings = new SettingsFragment();
			FragmentHelper.addFragment(fMgr, fragment_settings, Global.FRAGMENT_BAND_SETTINGS);
		}
	}
	
	
	
	
//	**********************************************************************
	/**
	 * 选择分享路径
	 */
	private void sendImg(int shareTo) {
		if (shareTo == Global.TYPE_SHARE_TO_FACEBOOK) {
//			shareToFacebook();
			
		} else if (shareTo == Global.TYPE_SHARE_TO_TWITTER) {
//			shareToTwitter();
			
		} else {

		}
	}

	
	private Map<String, String> mapPackageName;
//	/**
//	 * share to Facebook
//	 */
//	public void shareToFacebook() {
//		if (mapPackageName != null){
//			String packageName = mapPackageName.get(Global.PACKAGE_NAME_FACEBOOK);
//			if (packageName != null){
//				System.out.println("********************facebook app");
//
//				//create a file to write bitmap data
//				ShareHelper.actionShare_facebook(packageName, MainActivity.this, bmp, "www.technaxx.de/My Fitness");
//
//			} else {
////				Toast.makeText(MainActivity.this, getString(R.string.no_facebook_in_your_phone), Toast.LENGTH_SHORT).show();
//				System.out.println("****************************no facebook app");
//				Session session = Session.getActiveSession();
//				System.out.println("session.isOpened() = " + session.isOpened());
//				if (session.isOpened()) {
//					performPublish(PendingAction.POST_PHOTO,
//							canPresentShareDialogWithPhotos);
//
//				} else {
//					loginButton.callOnClick();
//					performPublish(PendingAction.POST_PHOTO,
//							canPresentShareDialogWithPhotos);
//				}
//			}
//		}
//
//
//	}
//
//
//	private void performPublish(PendingAction action, boolean allowNoSession) {
//		Session session = Session.getActiveSession();
//		if (session != null) {
//			pendingAction = action;
//			if (session != null
//					&& session.getPermissions().contains("publish_actions")) {
//				handlePendingAction();
//				return;
//			} else if (session.isOpened()) {
//				session.requestNewPublishPermissions(new Session.NewPermissionsRequest(
//						this, PERMISSION));
//				return;
//			}
//		}
//
//		if (allowNoSession) {
//			pendingAction = action;
//			handlePendingAction();
//		}
//	}
//
//
//	private void handlePendingAction() {
//		PendingAction previouslyPendingAction = pendingAction;
//		pendingAction = PendingAction.NONE;
//
//		switch (previouslyPendingAction) {
//		case POST_PHOTO:
//			postPhoto();
//			break;
//		// case POST_STATUS_UPDATE:
//		// postStatusUpdate();
//		// break;
//		default:
//			break;
//		}
//	}
//
//	/**
//	 * 发送图片
//	 */
//	private void postPhoto() {
//		Session session = Session.getActiveSession();
//		if (bmp != null) {
//			if (canPresentShareDialogWithPhotos) {
//				FacebookDialog shareDialog = createShareDialogBuilderForPhoto(
//						bmp).build();
//				uiHelper.trackPendingDialogCall(shareDialog.present());
//
//			} else if (session != null
//					&& session.getPermissions().contains("publish_actions")) {
//
//				Request request = Request.newUploadPhotoRequest(
//						Session.getActiveSession(), bmp,
//						new Request.Callback() {
//							@Override
//							public void onCompleted(Response response) {
//									DialogHelper.cancelDialog(dialog_sharing);
//								showPublishResult(response.getGraphObject(),
//										response.getError());
//							}
//						});
//				request.executeAsync();
//				dialog_sharing = DialogHelper.showProgressDialog(MainActivity.this, getString(R.string.Sharing_to_facebook));
//				dialog_sharing.setCanceledOnTouchOutside(false);
//				dialog_sharing.show();
//
//			} else {
//				pendingAction = PendingAction.POST_PHOTO;
//			}
//		}
//	}
//
//
//	private FacebookDialog.PhotoShareDialogBuilder createShareDialogBuilderForPhoto(
//			Bitmap... photos) {
//		return new FacebookDialog.PhotoShareDialogBuilder(this)
//				.addPhotos(Arrays.asList(photos));
//	}
//
//
//	/**
//	 * 显示分享结果
//	 */
//	private void showPublishResult(GraphObject result,
//			FacebookRequestError error) {
//		String title = null;
//		String message = null;
//		if (error == null) {
//			title = getString(R.string.success);
//
//			//获取发送成功的ID
////			String id = result.cast(GraphObjectWithId.class).getId();
//			message = getString(R.string.Successfully_shared_to_facebook);
//		} else {
//			title = getString(R.string.error);
//			message = error.getErrorMessage();
//		}
//		DialogHelper.showAlertDialog(MainActivity.this, title, message, false);
//	}
//
//
//	private Session.StatusCallback callback = new Session.StatusCallback() {
//		@Override
//		public void call(Session session, SessionState state,
//				Exception exception) {
//			onSessionStateChange(session, state, exception);
//		}
//	};
//
//
//	private void onSessionStateChange(Session session, SessionState state,
//			Exception exception) {
//		System.out.println("state = " + state);
//		if (pendingAction != PendingAction.NONE
//				&& (exception instanceof FacebookOperationCanceledException || exception instanceof FacebookAuthorizationException)) {
//			new AlertDialog.Builder(MainActivity.this)
//					.setTitle(R.string.cancelled)
//					.setMessage(R.string.permission_not_granted)
//					.setPositiveButton(R.string.ok, null).show();
//			pendingAction = PendingAction.NONE;
//
//		} else if (state == SessionState.OPENED_TOKEN_UPDATED) {
//			System.out.println("OPENED_TOKEN_UPDATED");
//			handlePendingAction();
//
//		} else if (state == SessionState.OPENED) {
//			System.out.println("OPENED");
////			shareToFacebook();
//			String packageName = mapPackageName.get(Global.PACKAGE_NAME_FACEBOOK);
//			if (packageName == null){
//			System.out.println("****************************no facebook app");
//				Session session1 = Session.getActiveSession();
//				System.out.println("session.isOpened() = " + session1.isOpened());
//				if (session1.isOpened()) {
//					performPublish(PendingAction.POST_PHOTO,
//							canPresentShareDialogWithPhotos);
//				} else {
//					loginButton.callOnClick();
//					performPublish(PendingAction.POST_PHOTO,
//							canPresentShareDialogWithPhotos);
//				}
//			}
//
//		} else if (state == SessionState.CLOSED_LOGIN_FAILED) {
//			System.out.println("CLOSED_LOGIN_FAILED");
//		}
//	}

	/***********************************************************************************
	************************************************************************************
		Share to Twitter 		
	*/
//		private WebView webview = null;
//		private String Url;
//
//		private static Twitter twitter;
//		private static RequestToken requestToken;
//		private static SharedPreferences mSharedPreferences;
//		private static Status responeStatus = null;
//
//		private void shareToTwitter() {
//			System.out.println("is twitter login " + isTwitterLoggedInAlready());
//			loginToTwitter();
//			if (isTwitterLoggedInAlready()) {
//				updateStatus();
//			}
//		}
//
//		/**
//		 * Function to login twitter
//		 * */
//		private void loginToTwitter() {
//			if (!isTwitterLoggedInAlready()) {
//				ConfigurationBuilder builder = new ConfigurationBuilder();
//				builder.setOAuthConsumerKey(Global.TWITTER_CONSUMER_KEY);
//				builder.setOAuthConsumerSecret(Global.TWITTER_CONSUMER_SECRET);
//				Configuration configuration = builder.build();
//				TwitterFactory factory = new TwitterFactory(configuration);
//
//				twitter = factory.getInstance();
//
//				webview = new WebView(this);
//				// 设置WebView属性，能够执行Javascript脚本
//				webview.getSettings().setJavaScriptEnabled(true);
//
//				webview.setWebViewClient(new WebViewClient() {
//					@Override
//					public boolean shouldOverrideUrlLoading(WebView view, String url) {
//						System.out.println("url = " + url);
//						if (url.contains(Global.TWITTER_CALLBACK_URL)) {
//							Url = url;
//							new GetAndSaveToken().execute(url);
//
//						} else {
//							view.loadUrl(url);
//						}
//						return true;
//					}
//				});
//
//				new TwitterGetAccessTokenTask().execute();
//			} else {
//				System.out.println("Already Logged into twitter");
//			}
//		}
//
//
//		class TwitterGetAccessTokenTask extends AsyncTask<String, RequestToken, String> {
//			ProgressDialog dialog_loading;
//			@Override
//			protected void onPreExecute() {
//				// TODO Auto-generated method stub
//				super.onPreExecute();
//				view_bottom.setVisibility(View.INVISIBLE);
//				view_content.setVisibility(View.INVISIBLE);
//				webview.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
//						LayoutParams.MATCH_PARENT));
//				layout_main.addView(webview);
//				dialog_loading = DialogHelper.showProgressDialog(MainActivity.this, getString(R.string.Loading));
//				dialog_loading.show();
//			}
//
//			@Override
//			protected String doInBackground(String... params) {
//				try {
//					System.out.println("request token start");
//					requestToken = twitter
//							.getOAuthRequestToken(Global.TWITTER_CALLBACK_URL);
//					// startActivity(new Intent(Intent.ACTION_VIEW, Uri
//					// .parse(requestToken.getAuthenticationURL())));
//				} catch (TwitterException e) {
//					e.printStackTrace();
//				}
//				return null;
//			}
//
//			protected void onPostExecute(String file_url) {
//				// 加载需要显示的网页
//				if (requestToken != null) {
//					webview.loadUrl(requestToken.getAuthenticationURL());
//					DialogHelper.cancelDialog(dialog_loading);
//				}
//			}
//		}
//
//
//		class GetAndSaveToken extends AsyncTask<String, String, String> {
//
//			protected String doInBackground(String... args) {
//				// Uri uri = getIntent().getData();
//				if (Url != null) {
//					final Uri uri = Uri.parse(Url);
//					if (uri != null && uri.toString().startsWith(Global.TWITTER_CALLBACK_URL)) {
//						String verifier = uri.getQueryParameter(Global.URL_TWITTER_OAUTH_VERIFIER);
//
//						try {
//							AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, verifier);
//
//							Editor e = mSharedPreferences.edit();
//							e.putString(Global.PREF_KEY_OAUTH_TOKEN, accessToken.getToken());
//							e.putString(Global.PREF_KEY_OAUTH_SECRET, accessToken.getTokenSecret());
//							e.putBoolean(Global.PREF_KEY_TWITTER_LOGIN, true);
//							e.commit(); // save changes
//							Log.e("Twitter OAuth Token", "> " + accessToken.getToken());
//						} catch (Exception e) {
//							// Check log for login errors
//							Log.e("Twitter Login Error", "> " + e.getMessage());
//							e.printStackTrace();
//						}
//					}
//				}
//				return null;
//			}
//
//			@Override
//			protected void onPostExecute(String result) {
//				// TODO Auto-generated method stub
//				super.onPostExecute(result);
//				layout_main.removeView(webview);
//				view_bottom.setVisibility(View.VISIBLE);
//				view_content.setVisibility(View.VISIBLE);
//				webview = null;
//				updateStatus();
//			}
//
//		}
//
//		/**
//		 * Function to update status
//		 * */
//		class updateTwitterStatus extends AsyncTask<String, String, String> {
//
//			@Override
//			protected void onPreExecute() {
//				super.onPreExecute();
////				pDialog = new ProgressDialog(MainActivity.this);
////				pDialog.setMessage("Updating to twitter...");
////				pDialog.setIndeterminate(false);
////				pDialog.setCancelable(false);
////				pDialog.show();
//
//				if (MainActivity.this != null && isAlive) {
//					dialog_sharing = DialogHelper.showProgressDialog(MainActivity.this, getString(R.string.Sharing_to_twitter));
//					dialog_sharing.setCanceledOnTouchOutside(false);
//					dialog_sharing.show();
//				}
//			}
//
//			protected String doInBackground(String... args) {
//				Log.d("Tweet Text", "> " + args[0]);
//				String status = args[0];
//
//				ConfigurationBuilder builder = new ConfigurationBuilder();
//				builder.setOAuthConsumerKey(Global.TWITTER_CONSUMER_KEY);
//				builder.setOAuthConsumerSecret(Global.TWITTER_CONSUMER_SECRET);
//
//				String access_token = mSharedPreferences.getString(
//						Global.PREF_KEY_OAUTH_TOKEN, "");
//				String access_token_secret = mSharedPreferences.getString(
//						Global.PREF_KEY_OAUTH_SECRET, "");
//
//				AccessToken accessToken = new AccessToken(access_token,
//						access_token_secret);
//				Twitter twitter = new TwitterFactory(builder.build())
//						.getInstance(accessToken);
//
//				try {
//					uploadPic(status, twitter);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				return null;
//			}
//
//			protected void onPostExecute(String file_url) {
//				DialogHelper.cancelDialog(dialog_sharing);
//				if (responeStatus != null) {
//					runOnUiThread(new Runnable() {
//						@Override
//						public void run() {
//							DialogHelper.showAlertDialog(MainActivity.this, getString(R.string.success), getString(R.string.Successfully_shared_to_twitter), false);
//
//						}
//					});
//				}
//			}
//		}
//
//		/**
//		 * To upload a picture with some piece of text.
//		 * @param message Message to display with picture
//		 * @param twitter Instance of authorized Twitter class
//		 * @throws Exception exception if any
//		 */
//		public void uploadPic(String message, Twitter twitter) throws Exception {
//			responeStatus = null;
//			try {
//				StatusUpdate status = new StatusUpdate(message);
//				if (bmp != null) {
//					ByteArrayOutputStream baos = new ByteArrayOutputStream();
//					bmp.compress(CompressFormat.PNG, 0 /* ignored for PNG */, baos);
//					byte[] bitmapdata = baos.toByteArray();
//					ByteArrayInputStream bais = new ByteArrayInputStream(bitmapdata);
//					status.setMedia("a", bais);
//					responeStatus = twitter.updateStatus(status);
//					System.out.println("responeStatus = " +responeStatus);
//				} else {
//					System.out.println("bmp = null");
//				}
//			} catch (TwitterException e) {
//				Log.d("TAG", "Pic Upload error" + e.getErrorMessage());
//				throw e;
//			}
//		}
//
//		/**
//		 * Check user already logged in your application using twitter Login flag is
//		 * fetched from Shared Preferences
//		 * */
//		private boolean isTwitterLoggedInAlready() {
//			return mSharedPreferences.getBoolean(Global.PREF_KEY_TWITTER_LOGIN,
//					false);
//		}
//
//
//		private void updateStatus() {
//			String status = "www.technaxx.de/My_Fitness";
//			if (status.trim().length() > 0) {
//				new updateTwitterStatus().execute(status);
//			} else {
//				Toast.makeText(getApplicationContext(),
//						"Please enter status message", Toast.LENGTH_SHORT).show();
//			}
//		}

//		***********************************************************************************
//		***********************************************************************************

	
	private void showAudioSyncDialog() {
		if (dialog_audio_sync == null) {
			dialog_audio_sync = DialogHelper.showProgressDialog(MainActivity.this, getString(R.string.synchronizing));
			dialog_audio_sync.setCanceledOnTouchOutside(false);
			dialog_audio_sync.show();
		} else {
			dialog_audio_sync.show();
		}
	}
	
	
	private void showConnectingDialog() {
		if (dialog_connecting == null) {
			dialog_connecting = DialogHelper.showProgressDialog(MainActivity.this, getString(R.string.bluetooth_connecting));
			dialog_connecting.setOnKeyListener(new DialogInterface.OnKeyListener() {
				
				@Override
				public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
					if (keyCode == KeyEvent.KEYCODE_BACK) {  
						cancelAllTimer();
						TYPE_BLE = Global.TYPE_BLE_NULL;
						disconnectDevice(mService);
						
						Toast.makeText(MainActivity.this, getString(R.string.stop_connecting), Toast.LENGTH_SHORT).show();
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
	
	
	private void showSyncDialog() {
		if (dialog_ble_sync == null) {
			dialog_ble_sync = DialogHelper.showProgressDialog(MainActivity.this, getString(R.string.synchronizing));
//			dialog_sync.setCancelable(false);
			dialog_ble_sync.setOnKeyListener(new DialogInterface.OnKeyListener() {
				
				@Override
				public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
					if (keyCode == KeyEvent.KEYCODE_BACK) {  
						cancelAllTimer();
						TYPE_BLE = Global.TYPE_BLE_NULL;
						disconnectDevice(mService);
						
						Toast.makeText(MainActivity.this, getString(R.string.stop_sync), Toast.LENGTH_SHORT).show();
					}

					return false;
				}
			});
			dialog_ble_sync.setCanceledOnTouchOutside(false);
			dialog_ble_sync.show();
		} else {
			dialog_ble_sync.show();
		}
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
				MainActivity.this.finish();
			}
		})
		.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {	
			@Override
			public void onClick(DialogInterface dialog, int which) {	
				
			}
		})
		.show();
	}
	
	
	private void showResetDialog(Context context) {
		new AlertDialog.Builder(context)
		.setTitle(R.string.Notice)
		.setMessage(getString(R.string.notice_reset))
		.setPositiveButton(R.string.Retain, new DialogInterface.OnClickListener() {		
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 插入断电标记
				Calendar cal = Calendar.getInstance();
				boolean isHaveResetTime = DatabaseProvider_wb013.isHaveResetTime(MainActivity.this, cal, profileID, deviceID);
				if (!isHaveResetTime) {
					DatabaseProvider_wb013.insertResetTime(MainActivity.this, cal, profileID, deviceID);
				}
				
				DatabaseProvider_wb013.deleteResetHistoryHour(MainActivity.this, profileID, deviceID, cal);
			}
		})
		.setNegativeButton(R.string.Clear, new DialogInterface.OnClickListener() {	
			@Override
			public void onClick(DialogInterface dialog, int which) {	
				SyncHelper.deleteAllHistoryToday(MainActivity.this, profileID, deviceID, Calendar.getInstance());
				
				actionDataUpdate();
				
			}
		})
		.show();
	}
	
	
	private void initBand() {
		Band band = BindHelper.getLastSyncDevice(MainActivity.this);
		if (band != null) {
			deviceID = band.getDeviceID();
		} else {
			deviceID = 1;
		}
	}
	
	
	/**
	 * 初始化广播接收器
	 */
	private void initReceiver() {
		IntentFilter f = new IntentFilter();
		f.addAction(Global.ACTION_SHOW_PROGRESS);
		
		registerReceiver(myBroadcastReceiver, f);
	}

	
	/**
	 * 初始化3.5mm口
	 */
	private void init35mmBroadcastReceiver() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Global.ACTION_DECODER_OK);
		intentFilter.addAction(Global.ACTION_TIME_OUT);
		intentFilter.addAction("android.intent.action.HEADSET_PLUG");
		
		registerReceiver(my35mmBroadcastReceiver, intentFilter);
	}
	
	
	/**
	 * 初始化音频接口
	 */
	private void initAudio() {
		/* 设置该Activity中音量控制键控制的音频流为多媒体音量 */
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		/* 实例化控制声音系统的对象 */
		audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		/* app启动即获取系统当前的媒体音量 */
		currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		/* 获取媒体音量可设置的最大值 */
		maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		mAudioTrack = new AudioTrackManager();
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
	
	
	/**
	 * 初始化 serviceConnection
	 */
	private void initServiceConnection() {
		Intent bindIntent = new Intent(MainActivity.this, MyFitnessService.class);
//		MainActivity.this.startService(bindIntent);
		bindService(bindIntent, myServiceConnection, Context.BIND_AUTO_CREATE);
	}
	
	
	/**
	 * 初始化 BroadcastReceiver
	 */
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
		
		intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
		intentFilter.addAction(Global.ACTION_BLUETOOTH_ENABLE_CONFORM);
		
		intentFilter.addAction(MyFitnessService.ACTION_DATA_AVAILABLE);

		registerReceiver(myBLEBroadcastReceiver, intentFilter);
		
	}
	
	
//	/**
//	 * 初始化facebook
//	 */
//	private void initFacebook(Bundle savedInstanceState) {
//		loginButton = (LoginButton) findViewById(R.id.loginbutton);
//		loginButton.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
//					@Override
//					public void onUserInfoFetched(GraphUser user) {
//						System.out.println("onUserInfoFetched");
//						MainActivity.this.user = user;
//					}
//				});
//		uiHelper = new UiLifecycleHelper(this, callback);
//		uiHelper.onCreate(savedInstanceState);
//
//		if (savedInstanceState != null) {
//			String name = savedInstanceState
//					.getString(PENDING_ACTION_BUNDLE_KEY);
//
//			if (name != null) {
//				pendingAction = PendingAction.valueOf(name);
//			}
//		}
//		canPresentShareDialogWithPhotos = FacebookDialog.canPresentShareDialog(
//				this, FacebookDialog.ShareDialogFeature.PHOTOS);
//		mapPackageName = ShareHelper.setImage(this);
//	}

//	/**
//	 * 初始化twitter
//	 */
//	private void initTwitter() {
//		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//
//		mSharedPreferences = getApplicationContext().getSharedPreferences(
//				"MyPref", 0);
//
//		if (!isTwitterLoggedInAlready()) {
//			new GetAndSaveToken().execute();
//		}
//	}
	
	/**
	 * 初始化UI
	 */
	private void initUI() {
		radio_progress = (RadioButton) findViewById(R.id.radio_progress);
		radio_progress.setOnCheckedChangeListener(myOnCheckedChangeListener);
		
		radio_activity = (RadioButton) findViewById(R.id.radio_activity);
		radio_activity.setOnCheckedChangeListener(myOnCheckedChangeListener);
		
		radio_settings = (RadioButton) findViewById(R.id.radio_settings);
		radio_settings.setOnCheckedChangeListener(myOnCheckedChangeListener);
		
		image_progress = (ImageView) findViewById(R.id.image_progress);
		image_activity = (ImageView) findViewById(R.id.image_activity);
		image_settings = (ImageView) findViewById(R.id.image_settings);
		
		text_settings = (TextView) findViewById(R.id.text_settings);
		text_settings.setOnClickListener(myOnClickListener);
		text_settings.setVisibility(View.GONE);
		
		
		layout_main = (FrameLayout) findViewById(R.id.layout_main);
		view_bottom = findViewById(R.id.layout_bottom);
		view_content = findViewById(R.id.layout_content);
	
	}
	
	private RadioButton radio_progress, radio_activity, radio_settings;
	private ImageView image_progress, image_activity, image_settings;
	private TextView text_settings;
//	private LoginButton loginButton;
	private FrameLayout layout_main;
	private View view_bottom, view_content;
	
	@Override
	public void onProfileUpdate() {
		profileID = ProfileHelper.initProfileID(MainActivity.this);
		
		ProgressFragment fragment_progress = (ProgressFragment) fMgr.findFragmentByTag(Global.FRAGMENT_BAND_PROGRESS);
		if (fragment_progress != null) {
			fragment_progress.actionProfileUpdate();
		}
		
		ActivityFragment fragment_activity = (ActivityFragment) fMgr.findFragmentByTag(Global.FRAGMENT_BAND_ACTIVITY);
		if (fragment_activity != null) {
			fragment_activity.actionProfileUpdate();
		}
	}


	@Override
	public void onGoalUpdate() {
		if (fMgr == null) {
			fMgr = getFragmentManager();
		}
		
		ProgressFragment fragment_progress = (ProgressFragment) fMgr.findFragmentByTag(Global.FRAGMENT_BAND_PROGRESS);
		if (fragment_progress != null) {
			fragment_progress.actionGoalUpdate();
		}
	}

	
	@Override
	public void endNewStartUp() {
		isNewStartup = false;
		radio_progress.setChecked(true);
	}
	
	
	@Override
	public void onProgressCheckSteps() {
		tab_activity = Global.TYPE_STEP;
		
		if (fMgr == null) {
			fMgr = getFragmentManager();
		}
		ActivityFragment fragment_activity = (ActivityFragment) fMgr.findFragmentByTag(Global.FRAGMENT_BAND_ACTIVITY);
		if (fragment_activity == null) {
			fragment_activity = new ActivityFragment();
			Bundle args = new Bundle();
			args.putInt(Global.KEY_TAB, tab_activity);
			fragment_activity.setArguments(args);
			
			FragmentHelper.hideAllFragment(fMgr);
			
			FragmentHelper.addFragment(fMgr, fragment_activity, Global.FRAGMENT_BAND_ACTIVITY);
		} else {
			FragmentHelper.hideAllFragment(fMgr);
			
			FragmentHelper.showFragment(fMgr, fragment_activity);
			fragment_activity.clickProgressStep();
		}
		radio_activity.setChecked(true);
		
	}
	
	
	@Override
	public void onProgressCheckBurn() {
		tab_activity = Global.TYPE_CALORIES;
		
		if (fMgr == null) {
			fMgr = getFragmentManager();
		}
		ActivityFragment fragment_activity = (ActivityFragment) fMgr.findFragmentByTag(Global.FRAGMENT_BAND_ACTIVITY);
		if (fragment_activity == null) {
			fragment_activity = new ActivityFragment();
			Bundle args = new Bundle();
			args.putInt(Global.KEY_TAB, tab_activity);
			fragment_activity.setArguments(args);
			
			FragmentHelper.hideAllFragment(fMgr);
			
			FragmentHelper.addFragment(fMgr, fragment_activity, Global.FRAGMENT_BAND_ACTIVITY);
		} else {
			FragmentHelper.hideAllFragment(fMgr);
			
			FragmentHelper.showFragment(fMgr, fragment_activity);
			fragment_activity.clickProgressCalories();
		}
		radio_activity.setChecked(true);
		
	}
	
	
	@Override
	public void onProgressCheckSleep() {
		tab_activity = Global.TYPE_SLEEP;
		
		if (fMgr == null) {
			fMgr = getFragmentManager();
		}
		ActivityFragment fragment_activity = (ActivityFragment) fMgr.findFragmentByTag(Global.FRAGMENT_BAND_ACTIVITY);
		if (fragment_activity == null) {
			fragment_activity = new ActivityFragment();
			Bundle args = new Bundle();
			args.putInt(Global.KEY_TAB, tab_activity);
			fragment_activity.setArguments(args);
			
			FragmentHelper.hideAllFragment(fMgr);
			
			FragmentHelper.addFragment(fMgr, fragment_activity, Global.FRAGMENT_BAND_ACTIVITY);
		} else {
			FragmentHelper.hideAllFragment(fMgr);
			
			FragmentHelper.showFragment(fMgr, fragment_activity);
			fragment_activity.clickProgressSleep();
		}
		radio_activity.setChecked(true);
		
	}
	
	
	@Override
	public void onShare(Bitmap bmp, int shareTo) {
		this.bmp = bmp;
		if (ShareHelper.isConnectingToInternet(MainActivity.this)){
			sendImg(shareTo);
		} else {
			Toast.makeText(MainActivity.this, getString(R.string.no_internet_connect), Toast.LENGTH_SHORT).show();
		}
		
	}


	@Override
	public void clickSync() {
		int sdk = Build.VERSION.SDK_INT;
		Log.i(TAG, "sdk int : " + sdk);
		if (sdk >= 18) {
			beginScanDevice();
			
		} else {
			Toast.makeText(MainActivity.this, getString(R.string.can_not_use_BLE), Toast.LENGTH_SHORT).show();
		}	
		
	}


	@Override
	public void startLostMode() {
		isStartLostMode = true;
		unregisterReceiver(myBLEBroadcastReceiver);
		
	}


	@Override
	public void endLostMode() {
		isStartLostMode = false;
		initBLEBroadcastReceiver();
		
	}

}
