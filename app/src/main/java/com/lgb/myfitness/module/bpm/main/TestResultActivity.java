package com.lgb.myfitness.module.bpm.main;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.lgb.myfitness.R;
import com.lgb.myfitness.been.BPM;
import com.lgb.myfitness.global.Global;
import com.lgb.myfitness.helper.CalculateHelper;
import com.lgb.myfitness.helper.ProfileHelper;

public class TestResultActivity extends Activity{
	
	private BPM bpm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bpm_test_result);
		initUI();
		
		if (savedInstanceState == null) {
			bpm = (BPM) getIntent().getSerializableExtra(Global.KEY_BPM);
			
		} else {
			bpm = (BPM) savedInstanceState.getSerializable(Global.KEY_BPM);
		}
		
		initProfile();
		setValue(bpm);
		setGraphState(bpm);
	}

	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putSerializable(Global.KEY_BPM, bpm);
		
		super.onSaveInstanceState(outState);
	}


	private OnClickListener myOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.button_back:
				
				TestResultActivity.this.finish();
				
				break;
			default:
				break;
			}
			
		}
	};
	
	private void initProfile() {
		String name = ProfileHelper.getCurrentUseProfileName(TestResultActivity.this);
		
		if (name != null) {
			text_name.setText(name);
		} else {
			text_name.setText("");
		}
	}
	
	
	private void setGraphState(BPM bpm) {
		
		int type = Global.TYPE_BPM_TYPE_LOW;
		int typeCenter = Global.TYPE_BPM_TYPE_NORM;
		int diatolic = 0;
		int systolic = 0;
		if (bpm != null) {
			
			diatolic = bpm.getDiatolic();
			systolic = bpm.getSystolic();
			type = CalculateHelper.getBPMType(systolic);
			
		}
		typeCenter = CalculateHelper.getBPMTypeCenter(systolic, diatolic);
		if (typeCenter == Global.TYPE_BPM_TYPE_LOW) {
			text_type.setText(getString(R.string.Low));
		} else if (typeCenter == Global.TYPE_BPM_TYPE_HIGH)  {
			text_type.setText(getString(R.string.High));
		} else {
			text_type.setText(getString(R.string.Norm));
		}

		image_point_low.setVisibility(type == Global.TYPE_BPM_TYPE_LOW ? View.VISIBLE : View.INVISIBLE);
		image_point_opti.setVisibility(type == Global.TYPE_BPM_TYPE_OPTI ? View.VISIBLE : View.INVISIBLE);
		image_point_norm.setVisibility(type == Global.TYPE_BPM_TYPE_NORM ? View.VISIBLE : View.INVISIBLE);
		image_point_mild.setVisibility(type == Global.TYPE_BPM_TYPE_MILD ? View.VISIBLE : View.INVISIBLE);
		image_point_midd.setVisibility(type == Global.TYPE_BPM_TYPE_MIDD ? View.VISIBLE : View.INVISIBLE);
		image_point_high.setVisibility(type == Global.TYPE_BPM_TYPE_HIGH ? View.VISIBLE : View.INVISIBLE);
	}
	
	
	private void setValue(BPM bpm) {
		if (bpm != null) {
			text_sys.setText(String.valueOf(bpm.getSystolic()));
			text_dia.setText(String.valueOf(bpm.getDiatolic()));
			text_hr.setText(String.valueOf(bpm.getHeartRate()));
			
			String datetime = Global.sdf_5.format(bpm.getDatetime().getTime());
			String[] array_datetime = datetime.split(" ");
			text_date.setText(array_datetime[0]);
			text_time.setText(array_datetime[1]);
			
		} else {
			text_sys.setText("0");
			text_dia.setText("0");
			text_hr.setText("0");
			
			text_date.setText("--");
			text_time.setText("--");
		}
	}
	
	
	private void initUI() {
		ImageView button_back = (ImageView) findViewById(R.id.button_back);
		button_back.setOnClickListener(myOnClickListener);
		
		text_name = (TextView) findViewById(R.id.text_name);
		
		text_sys = (TextView) findViewById(R.id.text_sys);
		text_dia = (TextView) findViewById(R.id.text_dia);
		text_hr = (TextView) findViewById(R.id.text_hr);
		
		text_date = (TextView) findViewById(R.id.text_date);
		text_time = (TextView) findViewById(R.id.text_time);
		
		image_point_low = (ImageView) findViewById(R.id.image_point_low);
		image_point_opti = (ImageView) findViewById(R.id.image_point_opti);
		image_point_norm = (ImageView) findViewById(R.id.image_point_norm);
		image_point_mild = (ImageView) findViewById(R.id.image_point_mild);
		image_point_midd = (ImageView) findViewById(R.id.image_point_midd);
		image_point_high = (ImageView) findViewById(R.id.image_point_high);
		
		text_type = (TextView) findViewById(R.id.text_type);

	}
	private TextView text_name;
	private TextView text_sys, text_dia, text_hr;
	private TextView text_date, text_time;
	
	private ImageView image_point_low, image_point_opti, image_point_norm, image_point_mild, image_point_midd, image_point_high;
	private TextView text_type;

}
