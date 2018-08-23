package com.lgb.myfitness.module.bpm;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.guoou.sdk.api.BleListener;
import com.guoou.sdk.bean.BpmResultBean;
import com.guoou.sdk.global.SdkManager;
import com.inuker.bluetooth.library.Constants;
import com.inuker.bluetooth.library.connect.listener.BleConnectStatusListener;
import com.lgb.mvp.SimpleBaseActivity;
import com.lgb.myfitness.R;
import com.lgb.myfitness.been.BPM;
import com.lgb.myfitness.database.DatabaseProvider_bpm;
import com.lgb.myfitness.global.Global;
import com.lgb.myfitness.helper.ChartHelper;
import com.lgb.myfitness.helper.DialogHelper;
import com.lgb.myfitness.helper.KeyBoardHelper;
import com.lgb.myfitness.helper.ProfileHelper;
import com.lgb.myfitness.module.bpm.main.BPMStatisticsDetailFragment;
import com.lgb.myfitness.module.bpm.main.BPMTestFragment;
import com.lgb.myfitness.module.bpm.main.BPMTestFragment.OnTestListener;
import com.lgb.myfitness.module.bpm.main.TestResultActivity;
import com.lgb.myfitness.module.bpm.settings.BPMSettingsProfileFragment;
import com.lgb.myfitness.module.bpm.settings.BPMSettingsProfileFragment.OnProfileUpdateListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Calendar;

import butterknife.BindView;

public class BPMMainActivity extends SimpleBaseActivity implements OnProfileUpdateListener, OnTestListener{

	@BindView(R.id.radio_bpm) RadioButton radio_bpm;
	@BindView(R.id.radio_settings) RadioButton radio_settings;
	@BindView(R.id.image_bpm) ImageView image_bpm;
	@BindView(R.id.image_settings) ImageView image_settings;
	@BindView(R.id.text_bpm) TextView text_bpm;
	@BindView(R.id.text_settings) TextView text_settings;
	private GestureDetector detector; //手势检测

	private ProgressDialog dialog_connecting;
	private AlertDialog dialog_result_unusual;
	
	private int profileID;
	
	private static boolean isNewStartup;
	public static boolean getIsNewStartup() {
		return isNewStartup;
	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_main_bpm;
	}

	@Override
	public void initView() {
		EventBus.getDefault().register(this);
		radio_bpm.setOnCheckedChangeListener(myOnCheckedChangeListener);
		radio_settings.setOnCheckedChangeListener(myOnCheckedChangeListener);
		text_bpm.setOnClickListener(myOnClickListener);
		text_bpm.setVisibility(View.GONE);
		text_settings.setOnClickListener(myOnClickListener);
		text_settings.setVisibility(View.GONE);

		int densityDPI = ChartHelper.getDensityDpi(BPMMainActivity.this);
		ChartHelper.setTextSize(densityDPI);

		detector = new GestureDetector(BPMMainActivity.this, myOnGestureListener);
		BPMFragmentManager.getInstance().init(this, R.id.layout_content);

		profileID = ProfileHelper.initProfileID(BPMMainActivity.this);

		SharedPreferences mPref = getSharedPreferences(Global.KEY_PREF, Context.MODE_PRIVATE);
		isNewStartup = mPref.getBoolean(Global.KEY_IS_NEW_START_UP_BPM, true);

		if (isNewStartup) {
			radio_settings.setChecked(true);
		} else {
			radio_bpm.setChecked(true);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		SdkManager.getInstance().getClient().disconnect(boundMac);

		DialogHelper.cancelDialog(dialog_connecting);
		EventBus.getDefault().unregister(this);
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
		@Override public boolean onSingleTapUp(MotionEvent e) { return false; }
		@Override public void onShowPress(MotionEvent e) { }
		@Override public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) { return false; }
		@Override public void onLongPress(MotionEvent e) { }
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			BPMStatisticsDetailFragment fragment_detail = (BPMStatisticsDetailFragment) BPMFragmentManager.getInstance().findFragment(BPMStatisticsDetailFragment.class);
			if (fragment_detail != null) {
				if(e1.getX() - e2.getX() > 120){
					fragment_detail.showNextPage();
					return true;
				}else if(e1.getX() - e2.getY() < -120){
					fragment_detail.showPreviousPage();
					return true;
				}
			}
			return false;
		}
		@Override public boolean onDown(MotionEvent e) {
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

	private String boundMac;
	
	/**
	 * 开始扫描设备
	 */
	private void beginScanDevice() {
		showConnectingDialog();
		SdkManager.getInstance().connectDevice(null, new BleListener() {
			@Override
			public void onResult(boolean isSuccess, String mac) {
				if (isSuccess) {
					boundMac = mac;
					SdkManager.getInstance().getClient().registerConnectStatusListener(mac, bleConnectStatusListener);
					receiveConnected();
					new Handler().postDelayed(() -> {
                        SdkManager.getInstance().writeBpm(0xaa);//获取电量
                    }, 1000);
				} else {
					SdkManager.getInstance().getClient().disconnect(mac);
					receiveDisconnected();
					Toast.makeText(BPMMainActivity.this, getString(R.string.connect_timeout), Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

    private BleConnectStatusListener bleConnectStatusListener = new BleConnectStatusListener() {
        @Override
        public void onConnectStatusChanged(String mac, int status) {
            if (status == Constants.STATUS_CONNECTED) {

            } else if (status == Constants.STATUS_DISCONNECTED) {
				receiveDisconnected();
            }
        }
    };
	
	private void receiveConnected() {
		DialogHelper.hideDialog(dialog_connecting);
		
		BPMTestFragment fragment_test = (BPMTestFragment) BPMFragmentManager.getInstance().findFragment(BPMTestFragment.class);
		if (fragment_test != null) {
			fragment_test.stateConnected();
		}
	}
	
	
	private void receiveDisconnected() {
		DialogHelper.hideDialog(dialog_connecting);

		BPMTestFragment fragment_test = (BPMTestFragment) BPMFragmentManager.getInstance().findFragment(BPMTestFragment.class);
		if (fragment_test != null) {
			fragment_test.stateDisconnected();
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

								SdkManager.getInstance().getClient().stopSearch();
								SdkManager.getInstance().getClient().disconnect(boundMac);
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
		if (!SdkManager.getInstance().getClient().isBluetoothOpened()) {
			SdkManager.getInstance().getClient().openBluetooth();
			return;
		}
		checkBluetoothPermission();
	}
	
	
	@Override
	public void onTestDisconnect() {
		SdkManager.getInstance().getClient().disconnect(boundMac);
	}

	private void checkBluetoothPermission() {
		if (Build.VERSION.SDK_INT >= 23) {
			//校验是否已具有模糊定位权限
			if (ContextCompat.checkSelfPermission(BPMMainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
				ActivityCompat.requestPermissions(BPMMainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 10001);
			} else {
				//具有权限
				beginScanDevice();
			}
		} else {
			//系统不高于6.0直接执行
			beginScanDevice();
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == 10001) {
			if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				//同意权限
				beginScanDevice();
			} else {
				// 权限拒绝
				// 下面的方法最好写一个跳转，可以直接跳转到权限设置页面，方便用户
				Toast.makeText(this, "获取蓝牙权限失败！", Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Subscribe
	public void onEventResult(BpmResultBean event) {

		if (event.isSuccess()) {
			BPM bpm = new BPM();
			bpm.setDatetime(Calendar.getInstance());
			bpm.setSystolic(event.getSystolic());
			bpm.setDiatolic(event.getDiatolic());
			bpm.setHeartRate(event.getHeartRate());


			if (bpm.getSystolic() <= 300 ) {
				DatabaseProvider_bpm.insertBPM(BPMMainActivity.this, profileID, bpm);

				showTestCompleteDialog(bpm);

			} else {
				showResultUnusualDialog();
			}

		} else {
			DialogHelper.showAlertDialog(BPMMainActivity.this, getString(R.string.Notice), getString(R.string.Test_fail), null);

		}

		BPMTestFragment fragment_test = (BPMTestFragment) BPMFragmentManager.getInstance().findFragment(BPMTestFragment.class);
		if (fragment_test != null) {
			fragment_test.stateStop();
		}
	}

}
