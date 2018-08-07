package com.lgb.myfitness.splash;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import com.lgb.myfitness.R;
import com.lgb.myfitness.module.bpm.BPMMainActivity;
import com.lgb.myfitness.global.Global;
import com.lgb.myfitness.helper.DialogHelper;
import com.lgb.myfitness.helper.TimerHelper;
import com.lgb.myfitness.scale.main.ScaleMainActivity;
import com.lgb.myfitness.wristband.main.MainActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class TypeActivity extends Activity{

	private SharedPreferences mPrefs = null;
	private Timer timer;
	
	private ProgressDialog dialog_loading;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_type);
		initUI();
		
		initImageLanguage();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		TimerHelper.cancelTimer(timer);
		
		DialogHelper.cancelDialog(dialog_loading);
	}

	private OnClickListener myOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.layout_scale:		
				showLoadingDialog();
				saveDefaultDevice(Global.TYPE_DEVICE_SCALE);
				actionClickScale();
				
				break;
			case R.id.layout_wristband:
				showLoadingDialog();
				saveDefaultDevice(Global.TYPE_DEVICE_WRISTBAND);
				actionClickWristband();
				
				break;
			case R.id.layout_bpm:
				showLoadingDialog();
				saveDefaultDevice(Global.TYPE_DEVICE_BPM);
				actionClickBPM();
				
				break;
			default:
				break;
			}
			
		}
	};
	
	
	private void saveDefaultDevice(int defalutDevice) {
		if (mPrefs == null) {
			mPrefs = getSharedPreferences(Global.KEY_PREF, Context.MODE_PRIVATE);
		}
		
		
		Editor editor = mPrefs.edit();
		editor.putInt(Global.KEY_DEFALUT_DEVICE, defalutDevice);
		editor.commit();
	}
	
	
	private void actionClickScale() {
		if (mPrefs == null) {
			mPrefs = getSharedPreferences(Global.KEY_PREF, Context.MODE_PRIVATE);
		}
		final boolean isNewStartup = mPrefs.getBoolean(Global.KEY_IS_NEW_START_UP_SCALE, true);
		
		timer = new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				Intent intent = new Intent(TypeActivity.this, ScaleMainActivity.class);
				intent.putExtra(Global.KEY_IS_NEW_START_UP_SCALE, isNewStartup);
				startActivity(intent);
				
				myHandler.sendEmptyMessage(1);
				
				TypeActivity.this.finish();
				
			}
		}, 0);
	}
	
	
	private void actionClickWristband() {
		if (mPrefs == null) {
			mPrefs = getSharedPreferences(Global.KEY_PREF, Context.MODE_PRIVATE);
		}
		final boolean isNewStartup = mPrefs.getBoolean(Global.KEY_IS_NEW_START_UP_WRISTBAND, true);

		timer = new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				Intent intent = new Intent(TypeActivity.this, MainActivity.class);
				intent.putExtra(Global.KEY_IS_NEW_START_UP_WRISTBAND, isNewStartup);
				startActivity(intent);
				
				myHandler.sendEmptyMessage(1);
				
				TypeActivity.this.finish();
				
			}
		}, 0);
	}
	
	
	private void actionClickBPM() {
		if (mPrefs == null) {
			mPrefs = getSharedPreferences(Global.KEY_PREF, Context.MODE_PRIVATE);
		}
		final boolean isNewStartup = mPrefs.getBoolean(Global.KEY_IS_NEW_START_UP_BPM, true);

		timer = new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				Intent intent = new Intent(TypeActivity.this, BPMMainActivity.class);
				intent.putExtra(Global.KEY_IS_NEW_START_UP_BPM, isNewStartup);
				startActivity(intent);
				
				myHandler.sendEmptyMessage(1);
				
				TypeActivity.this.finish();
				
			}
		}, 0);
	}
	
	
	private Handler myHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				DialogHelper.hideDialog(dialog_loading);
				break;

			default:
				break;
			}
		}
		
	};
	
	
	private void showLoadingDialog() {
		if (dialog_loading == null) {
			dialog_loading = DialogHelper.showProgressDialog(TypeActivity.this, getString(R.string.Loading));
			dialog_loading.setCanceledOnTouchOutside(false);
			dialog_loading.show();
		} else {
			dialog_loading.show();
		}
	}
	
	
	private void initImageLanguage() {
		Resources resources = getResources();//获得res资源对象  
		Configuration config = resources.getConfiguration();//获得设置对象  
		Locale locale = config.locale;
//		System.out.println(locale);
		
		if (locale.equals(Locale.GERMAN) || locale.equals(Locale.GERMANY)) {
			image_scale.setImageResource(R.drawable.image_type_scale_de);
			image_wristband.setImageResource(R.drawable.image_type_wristband_de);
			
		} else {
			image_scale.setImageResource(R.drawable.image_type_scale_en);
			image_wristband.setImageResource(R.drawable.image_type_wristband_en);
		}
	}
	
	
	private void initUI() {
		View view_scale = findViewById(R.id.layout_scale);
		view_scale.setOnClickListener(myOnClickListener);
		
		View view_wristband = findViewById(R.id.layout_wristband);
		view_wristband.setOnClickListener(myOnClickListener);
		
		View view_bpm = findViewById(R.id.layout_bpm);
		view_bpm.setOnClickListener(myOnClickListener);
		
		image_scale = (ImageView) findViewById(R.id.image_scale);
		image_wristband = (ImageView) findViewById(R.id.image_wristband);
	}
	private ImageView image_scale, image_wristband;
}
