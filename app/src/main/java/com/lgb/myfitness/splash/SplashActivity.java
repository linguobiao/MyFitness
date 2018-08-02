package com.lgb.myfitness.splash;

import java.util.Timer;
import java.util.TimerTask;
import com.lgb.myfitness.R;
import com.lgb.myfitness.bpm.main.BPMMainActivity;
import com.lgb.myfitness.global.Global;
import com.lgb.myfitness.scale.main.ScaleMainActivity;
import com.lgb.myfitness.wristband.main.MainActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class SplashActivity extends Activity{
	
	private Timer timer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		timer = new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				
				SharedPreferences mPref = getSharedPreferences(Global.KEY_PREF, Context.MODE_PRIVATE);
				
				int defaultDevice = mPref.getInt(Global.KEY_DEFALUT_DEVICE, Global.TYPE_DEVICE_NULL);
				if (defaultDevice == Global.TYPE_DEVICE_NULL) {
					Intent intent = new Intent(SplashActivity.this, TypeActivity.class);
					startActivity(intent);
					
				} else if (defaultDevice == Global.TYPE_DEVICE_SCALE) {
					boolean isNewStartup = mPref.getBoolean(Global.KEY_IS_NEW_START_UP_SCALE, true);
					
					Intent intent = new Intent(SplashActivity.this, ScaleMainActivity.class);
					intent.putExtra(Global.KEY_IS_NEW_START_UP_SCALE, isNewStartup);
					startActivity(intent);
					
				} else if (defaultDevice == Global.TYPE_DEVICE_WRISTBAND) {
					boolean isNewStartup = mPref.getBoolean(Global.KEY_IS_NEW_START_UP_WRISTBAND, true);
					
					Intent intent = new Intent(SplashActivity.this, MainActivity.class);
					intent.putExtra(Global.KEY_IS_NEW_START_UP_WRISTBAND, isNewStartup);
					startActivity(intent);
					
				} else if (defaultDevice == Global.TYPE_DEVICE_BPM) {
					boolean isNewStartup = mPref.getBoolean(Global.KEY_IS_NEW_START_UP_BPM, true);
					
					Intent intent = new Intent(SplashActivity.this, BPMMainActivity.class);
					intent.putExtra(Global.KEY_IS_NEW_START_UP_BPM, isNewStartup);
					startActivity(intent);
				}

				SplashActivity.this.finish();
				
			}
		}, 500);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		if (timer != null) {
			timer.cancel();
		}
	}
	
}
