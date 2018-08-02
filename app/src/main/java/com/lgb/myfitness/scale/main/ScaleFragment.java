package com.lgb.myfitness.scale.main;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import android.app.Fragment;
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
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.lgb.myfitness.R;
import com.lgb.myfitness.been.Profile;
import com.lgb.myfitness.been.Scale;
import com.lgb.myfitness.database.DatabaseProvider_public;
import com.lgb.myfitness.database.DatabaseProvider_scale;
import com.lgb.myfitness.global.Global;
import com.lgb.myfitness.helper.CalculateHelper;
import com.lgb.myfitness.helper.CalendarHelper;
import com.lgb.myfitness.helper.ChartHelper;
import com.lgb.myfitness.helper.DialogHelper;
import com.lgb.myfitness.helper.ParserHelper;
import com.lgb.myfitness.helper.ProfileHelper;
import com.lgb.myfitness.helper.SyncHelper;
import com.lgb.myfitness.service.MyFitnessService;

public class ScaleFragment extends Fragment {

	private String TAG = getTag();

	/**
	 * 起始点坐标值
	 */
//	private float[] xArray;
	private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
	private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
	private GraphicalView mChartView;
	private XYMultipleSeriesRenderer mRenderer_small = new XYMultipleSeriesRenderer();
	private GraphicalView mChartView_small;

	private Calendar date_query;
	private List<Scale> scaleList_31day = new ArrayList<Scale>();
	
	private int scaleViewCheckType = Global.TYPE_SCALE_VIEW_MAIN;
	
	private double height = Global.DEFAULT_HEIGHT;
	
	private Timer timer_stop_scan;
	
	private MyFitnessService mService = null;
	private BluetoothAdapter mBtAdapter = null;
	private ProgressDialog dialog_connecting;
	
	private int profileID = -1;
	
	private Handler myHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Global.HANDLER_SCAN_TIME_OUT: // 连接超时
				if (timer_stop_scan != null) {
					timer_stop_scan.cancel();
				}
				DialogHelper.hideDialog(dialog_connecting);
				disconnectDevice(mService);

				image_bluetooth.setImageResource(R.drawable.button_bluetooth_white);
				
				Toast.makeText(getActivity(), getString(R.string.connect_timeout),
						Toast.LENGTH_SHORT).show();
				
				break;
			case Global.HANDLER_DISCONNECTED: // 断开连接
				if (timer_stop_scan != null) {
					timer_stop_scan.cancel();
				}
				
				disconnectDevice(mService);
				
				image_bluetooth.setImageResource(R.drawable.button_bluetooth_white);
				
				Toast.makeText(getActivity(),
						getString(R.string.disconnected), Toast.LENGTH_SHORT)
						.show();
			default:
				break;
			}
		}

	};

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_scale, container, false);

		initUI(view);
		
		int densityDPI = ChartHelper.getDensityDpi(getActivity());
		ChartHelper.setTextSize(densityDPI);
		
		profileID = ProfileHelper.initProfileID(getActivity());
		
		initServiceConnection();
		initBroadcastReceiver();

		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();
		updateDate();
		updateUserInfo();
		updateChart();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		if (mService != null) {
			mService.scan(false);
			mService.disconnect();
		}

//		getActivity().stopService(new Intent(getActivity(), MyFitnessService.class));
		getActivity().unbindService(myServiceConnection);
		
		getActivity().unregisterReceiver(myBLEBroadcastReceiver);
		if (timer_stop_scan != null) {
			timer_stop_scan.cancel();
		}

		DialogHelper.cancelDialog(dialog_connecting);
	}

	/**
	 * my OnClickListener
	 */
	private OnClickListener myOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.image_scale_ble:
				actionClickBluetooth();
				
				break;
			case R.id.button_left:
				actionClickTypeRight();
				
				break;
			case R.id.button_right:
				actionClickTypeRight();
				
				break;

			default:
				break;
			}
		}
	};

	/**
	 * my BLE BroadcastReceiver
	 */
	private BroadcastReceiver myBLEBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			// 找到设备
			////////////////////////////////////////////////////////
			if (action.equals(MyFitnessService.ACTION_DEVICE_FOUND)) {
				Bundle data = intent.getExtras();
				BluetoothDevice device = data.getParcelable(BluetoothDevice.EXTRA_DEVICE);

				if (device != null) {
					String deviceName = device.getName();
					String deviceAddress = device.getAddress();
					System.out.println("device name = " + deviceName);
					if ((deviceName != null) && (deviceAddress != null)) {
						if (deviceName.contains(Global.DEVICE_NAME_SCALE)) {
//							if (timer_stop_scan != null) {
//								timer_stop_scan.cancel();
//							}
							if (mService != null) {
								mService.scan(false);
								mService.connect(deviceAddress, false);
							}
						}
					}
				}
			}
			// 连接上设备
			// /////////////////////////////////////////////////////////
			else if (action.equals(MyFitnessService.ACTION_GATT_CONNECTED)) {
				DialogHelper.hideDialog(dialog_connecting);
				if (timer_stop_scan != null) {
					timer_stop_scan.cancel();
				}
				
				image_bluetooth.setImageResource(R.drawable.button_bluetooth_blue);
			}
			// 连接失败
			// /////////////////////////////////////////////////////////
			else if (action.equals(MyFitnessService.ACTION_GATT_CONNECTED_FAIL)) {
				myHandler.sendEmptyMessage(Global.HANDLER_SCAN_TIME_OUT);
			}
			// 断开设备
			// ////////////////////////////////////////////////////////
			else if (action.equals(MyFitnessService.ACTION_GATT_DISCONNECTED)) {
				myHandler.sendEmptyMessage(Global.HANDLER_DISCONNECTED);
			}
			// 找到服务
			// ////////////////////////////////////////////////////////
			else if (action.equals(MyFitnessService.ACTION_GATT_SERVICES_DISCOVERED)) {
				mService.setScaleNotifyTrue();
			}
			// 收到广播数据
			else if (action.equals(MyFitnessService.ACTION_DATA_AVAILABLE)) {
				byte[] value = intent.getByteArrayExtra(MyFitnessService.KEY_NOTIFY_DATA);
				receiveScales(value, date_query);	
			}
			// 手机蓝牙状态改变
			/////////////////////////////////////////////////////////////
			else if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
				Log.i(TAG, "bluetooth state change");
				int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
						BluetoothAdapter.STATE_OFF);
				if (state == BluetoothAdapter.STATE_ON) {
					
				} else if (state == BluetoothAdapter.STATE_OFF) {
					Log.i(TAG, "bluetooth disable");
					myHandler.sendEmptyMessage(Global.HANDLER_DISCONNECTED);
				}
			}
			else if (action.equals(Global.ACTION_BLUETOOTH_ENABLE_CONFORM)) {
				Log.i(TAG, "bluetooth enable");
				int sdk = Build.VERSION.SDK_INT;
				Log.i(TAG, "sdk int : " + sdk);
				if (sdk >= 18) {
					beginScanDevice();
				} else {
					Toast.makeText(getActivity(),getString(R.string.can_not_use_BLE),Toast.LENGTH_SHORT).show();
				}
			}
		}
	};

	/**
	 * 点击蓝牙图标事件
	 */
	private void actionClickBluetooth() {
		int sdk = Build.VERSION.SDK_INT;
		Log.i(TAG, "sdk int : " + sdk);
		if (sdk >= 18) {
			if (mService != null && mService.getConnectionState() != MyFitnessService.STATE_CONNECTED) {
				beginScanDevice();
			} else {
				System.out.println("had connected");
			}
		} else {
			Toast.makeText(getActivity(), getString(R.string.can_not_use_BLE),
					Toast.LENGTH_SHORT).show();
		}
	}
	
	
	/**
	 * 点击right事件
	 */
	private void actionClickTypeRight() {
		switch (scaleViewCheckType) {
		case Global.TYPE_SCALE_VIEW_MAIN:
			showViewChart();
			
			break;
		case Global.TYPE_SCALE_VIEW_CHART:
			showViewMain();
			
			break;
		default:
			break;
		}
	}
	
	/**
	 * 显示scale图表界面
	 */
	private void showViewChart() {
		scaleViewCheckType = Global.TYPE_SCALE_VIEW_CHART;
		
		image_scale.setBackgroundResource(R.drawable.image_progress_day);
		text_scale.setText(getString(R.string.MONTH));
		layout_view.setVisibility(View.GONE);
		layout_view_chart.setVisibility(View.VISIBLE);
	}
	
	/**
	 * 显示scale主界面
	 */
	private void showViewMain() {
		scaleViewCheckType = Global.TYPE_SCALE_VIEW_MAIN;
		
		image_scale.setBackgroundResource(R.drawable.image_scale);
		text_scale.setText(getString(R.string.SCALE));
		layout_view_chart.setVisibility(View.GONE);
		layout_view.setVisibility(View.VISIBLE);
	}
	
	/**
	 * 处理收到的数据
	 * 
	 * @param value
	 */
	private void receiveScales(byte[] value, Calendar date_query) {
		
		if (value != null && date_query != null) {
			Scale scale = ParserHelper.parseScales(value, date_query);
			if(scale != null) {
				// calculate bmi
				String bmiStr = Global.df_double_2.format(scale.getWeight() / ((height * height) / 10000));
				bmiStr = bmiStr.replaceAll(",", ".");
				double bmi = Double.parseDouble(bmiStr);
				scale.setBMI(bmi);
				
				saveScale(profileID, scale);
				
				text_weight.setText(String.valueOf(scale.getWeight()));
				text_bmi.setText(String.valueOf(bmi));
				
				updateChart();
			}
		}
		
	}

	/**
	 * 保存电子称数据
	 * 
	 * @param value
	 */
	public void saveScale(int profileID, Scale scale) {
		
		if (scale != null) {
			
			Scale temp = DatabaseProvider_scale.queryScale(getActivity(), profileID, date_query);
		
			if (temp == null) {
				DatabaseProvider_scale.insertScale(getActivity(), profileID, scale);
			} else {
				DatabaseProvider_scale.updateScale(getActivity(), profileID, scale);
			}
		}
	}

	/**
	 * 开始扫描设备
	 */
	private void beginScanDevice() {

		mBtAdapter = SyncHelper.initBluetooth_manual(getActivity());
		if (mBtAdapter != null) {
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
			Toast.makeText(getActivity(),
					getString(R.string.No_bluetooth_in_device),
					Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 五秒内找不到设备就停止扫描设备
	 */
	private void mechanismScanTimeOut() {
		timer_stop_scan = new Timer();
		timer_stop_scan.schedule(new TimerTask() {

			@Override
			public void run() {
				// 停止搜索
				mService.scan(false);
				myHandler.sendEmptyMessage(Global.HANDLER_SCAN_TIME_OUT);
			}
		}, 10 * 1000);
	}
	
	
	public void actionProfileUpdate() {
		profileID = ProfileHelper.initProfileID(getActivity());
		updateUserInfo();
		updateChart();
	}
	

	/**
	 * 显示正在连接对话框
	 */
	private void showConnectingDialog() {
		if (dialog_connecting == null) {
			dialog_connecting = DialogHelper.showProgressDialog(getActivity(),
					getString(R.string.bluetooth_connecting));
			dialog_connecting
					.setOnKeyListener(new DialogInterface.OnKeyListener() {

						@Override
						public boolean onKey(DialogInterface dialog,
								int keyCode, KeyEvent event) {
							if (keyCode == KeyEvent.KEYCODE_BACK) {
								if (timer_stop_scan != null) {
									timer_stop_scan.cancel();
								}
								disconnectDevice(mService);
								
								image_bluetooth.setImageResource(R.drawable.button_bluetooth_white);
								
								Toast.makeText(getActivity(),
										getString(R.string.stop_connecting), Toast.LENGTH_SHORT)
										.show();
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
	
	/**
	 * 更新用户信息
	 */
	private void updateUserInfo() {

		SharedPreferences mPrefs = getActivity().getSharedPreferences(Global.KEY_PREF, Context.MODE_PRIVATE);
		String name = mPrefs.getString(Global.KEY_PROFILE_NAME, "Name");
		Profile profile = DatabaseProvider_public.queryProfile(getActivity(), name);
		System.out.println("name = " + name);
		double weight = 0;
		double _height = 0;
		int gender = Global.TYPE_GENDER_NULL;
		double goalWeight = 0;
		if (profile != null) {
			name = profile.getName();
			weight = profile.getWeight();
			gender = profile.getGender();
			_height = profile.getHeight();
			height = _height;
			
			int profileID = profile.getID();
			Double value = DatabaseProvider_scale.queryGoalWeight(getActivity(), profileID);
			if (value != null) {
				goalWeight = value;
			}
			
		} else {
			name = "--";
		}
		text_user_name.setText(name);
		text_user_name_chart.setText(name);
		text_user_weight.setText(String.valueOf(weight) + "kg");
		text_user_height.setText(String.valueOf(_height) + "cm");
		text_goal_weight.setText(String.valueOf(goalWeight));
		if (gender == Global.TYPE_GENDER_MALE) {
			text_user_gender.setText(getString(R.string.Male));
			
		} else if (gender == Global.TYPE_GENDER_FEMALE) {
			text_user_gender.setText(getString(R.string.Female));
			
		} else if (gender == Global.TYPE_GENDER_NULL) {
			text_user_gender.setText("-");
			
		}
	}

	
	private void updateDate() {
		date_query = CalendarHelper.getToday();

		if (date_query != null) {
			text_date.setText(Global.sdf_2.format(date_query.getTime()));
		}
		
	}
	
	
	private void updateChart() {
		showWeightMonthChart();
		showBmiMonthChart();
	}
	
	
	/**
	 * 显示体重月视图（大图或小图）
	 */
	private void showWeightMonthChart() {

		Calendar[] calArray = CalendarHelper.getLastMonthToTomorrow(date_query);
		List<Scale> scaleList = DatabaseProvider_scale.queryScale(getActivity(), profileID, calArray[0], calArray[1]);
		Map<Calendar, Scale> day30Map = CalendarHelper.getDayMap_scale(calArray);
		scaleList_31day = CalculateHelper.handleToCompleteWeight(day30Map, scaleList);

		//设置小图和大图数据
		mDataset = ChartHelper.setDataset_chart_scale_month_weihgt(getActivity(), scaleList_31day);
		
		//设置小图样式
		layout_chart_small.removeAllViews();
		mRenderer_small = ChartHelper.setRenderer_chart_scale_month_weight_small(getActivity(), scaleList_31day, face, 31, date_query);
		mChartView_small = ChartFactory.getCubeLineChartView(getActivity(), mDataset, mRenderer_small, ChartHelper.SMOTHNESS);
		layout_chart_small.addView(mChartView_small, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		
		//设置大图样式	
		layout_chart_weight.removeAllViews();
		mRenderer = ChartHelper.setRenderer_chart_scale_month_weight(getActivity(), scaleList_31day, face, 31, date_query);
		mChartView = ChartFactory.getCubeLineChartView(getActivity(), mDataset, mRenderer, ChartHelper.SMOTHNESS);
//		mChartView.setOnTouchListener(myOnTouchListener);
//		xArray = ShowValueHelper.calculateXArray(getActivity(), 30);
		layout_chart_weight.addView(mChartView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

	}
	
	/**
	 * 显示BMI值月视图（大图） 
	 */
	private void showBmiMonthChart() {

		Calendar[] calArray = CalendarHelper.getLastMonthToTomorrow(date_query);
		List<Scale> scaleList = DatabaseProvider_scale.queryScale(getActivity(), profileID, calArray[0], calArray[1]);
		Map<Calendar, Scale> day30Map = CalendarHelper.getDayMap_scale(calArray);
		scaleList_31day = CalculateHelper.handleToCompleteWeight(day30Map, scaleList);

		//设置小图和大图数据
		mDataset = ChartHelper.setDataset_chart_scale_month_bmi(getActivity(), scaleList_31day);
	
		//设置大图样式	
		layout_chart_bmi.removeAllViews();
		mRenderer = ChartHelper.setRenderer_chart_scale_month_bmi(getActivity(), scaleList_31day, face, 31, date_query);
		mChartView = ChartFactory.getCubeLineChartView(getActivity(), mDataset,mRenderer, ChartHelper.SMOTHNESS);
//		mChartView.setOnTouchListener(myOnTouchListener);
//		xArray = ShowValueHelper.calculateXArray(getActivity(), 30);
		layout_chart_bmi.addView(mChartView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

	}

	/**
	 * 断开连接
	 * 
	 * @param mService
	 */
	private void disconnectDevice(MyFitnessService mService) {
		if (mService != null) {
			mService.scan(false);
			mService.disconnect();
		}
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
		Intent bindIntent = new Intent(getActivity(), MyFitnessService.class);
//		getActivity().startService(bindIntent);
		getActivity().bindService(bindIntent, myServiceConnection,
				Context.BIND_AUTO_CREATE);
	}

	
	
	/**
	 * 初始化 BroadcastReceiver
	 */
	private void initBroadcastReceiver() {
//		Log.i(TAG, "init***************************");
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(MyFitnessService.ACTION_GATT_CONNECTED);
		intentFilter.addAction(MyFitnessService.ACTION_GATT_DISCONNECTED);
		intentFilter.addAction(MyFitnessService.ACTION_DEVICE_FOUND);
		intentFilter.addAction(MyFitnessService.ACTION_GATT_SERVICES_DISCOVERED);
		intentFilter.addAction(MyFitnessService.ACTION_DATA_AVAILABLE);
		intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
		intentFilter.addAction(Global.ACTION_BLUETOOTH_ENABLE_CONFORM);

		getActivity().registerReceiver(myBLEBroadcastReceiver, intentFilter);

	}

	/**
	 * init UI
	 * 
	 * @param view
	 */
	private void initUI(View view) {
		face = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/EurostileExtended-Roman-DTC.ttf");

		layout_chart_small = (RelativeLayout) view.findViewById(R.id.layout_chart_small);
		layout_chart_weight = (RelativeLayout) view.findViewById(R.id.layout_chart_weight);
		layout_chart_bmi = (RelativeLayout) view.findViewById(R.id.layout_chart_bmi);
		layout_view = (LinearLayout) view.findViewById(R.id.layout_view);
		layout_view_chart = (LinearLayout) view.findViewById(R.id.layout_view_chart);

		image_bluetooth = (ImageView) view.findViewById(R.id.image_scale_ble);
		image_bluetooth.setOnClickListener(myOnClickListener);
		image_scale = (ImageView) view.findViewById(R.id.image_scale);
		image_scale.setBackgroundResource(R.drawable.image_scale);
		
		ImageView image_logo = (ImageView) view.findViewById(R.id.image_title_logo);
		image_logo.setOnClickListener(myOnClickListener);
		button_left = (ImageView) view.findViewById(R.id.button_left);
		button_left.setOnClickListener(myOnClickListener);
		button_right = (ImageView) view.findViewById(R.id.button_right);
		button_right.setOnClickListener(myOnClickListener);
		

		text_user_name = (TextView) view.findViewById(R.id.text_user_name);
		text_user_gender = (TextView) view.findViewById(R.id.text_user_gender);
		text_user_weight = (TextView) view.findViewById(R.id.text_user_weight);
		text_user_height = (TextView) view.findViewById(R.id.text_user_height);
		text_weight = (TextView) view.findViewById(R.id.text_weight_data);
		text_bmi = (TextView) view.findViewById(R.id.text_bmi);
		text_goal_weight = (TextView) view.findViewById(R.id.text_weight_below);
		text_date = (TextView) view.findViewById(R.id.text_date_scale);
		text_user_name_chart = (TextView) view.findViewById(R.id.text_user_name_chart);
		text_scale = (TextView) view.findViewById(R.id.text_scale);
		text_scale.setText(getString(R.string.SCALE));
		
	}

	private Typeface face;
	private RelativeLayout layout_chart_small, layout_chart_weight, layout_chart_bmi;
	private LinearLayout layout_view, layout_view_chart;
	private ImageView image_bluetooth, button_left, button_right, image_scale;
	private TextView text_user_name, text_user_gender, text_user_weight, text_user_height;
	private TextView text_weight, text_bmi, text_goal_weight, text_date, text_user_name_chart, text_scale;

}
