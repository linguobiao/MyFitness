package com.lgb.myfitness.module.bpm.main;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.lgb.mvp.SimpleBaseFragment;
import com.lgb.myfitness.R;
import com.lgb.myfitness.been.BPM;
import com.lgb.myfitness.global.Global;
import com.lgb.myfitness.helper.CalculateHelper;
import com.lgb.myfitness.helper.ProfileHelper;
import com.lgb.myfitness.module.bpm.BPMFragmentManager;

import butterknife.BindView;

public class BPMStatisticsDetailFragment extends SimpleBaseFragment{

	private Animation leftInAnimation;
	private Animation leftOutAnimation;
	private Animation rightInAnimation;
	private Animation rightOutAnimation;

	@BindView(R.id.button_back) ImageView button_back;
	@BindView(R.id.text_name) TextView text_name;
	@BindView(R.id.text_sys) TextView text_sys;
	@BindView(R.id.text_dia) TextView text_dia;
	@BindView(R.id.text_hr) TextView text_hr;
	@BindView(R.id.text_date) TextView text_date;
	@BindView(R.id.text_time) TextView text_time;
	@BindView(R.id.text_type) TextView text_type;
	@BindView(R.id.text_result) TextView text_result;
	@BindView(R.id.image_point_low) ImageView image_point_low;
	@BindView(R.id.image_point_opti) ImageView image_point_opti;
	@BindView(R.id.image_point_norm) ImageView image_point_norm;
	@BindView(R.id.image_point_mild) ImageView image_point_mild;
	@BindView(R.id.image_point_midd) ImageView image_point_midd;
	@BindView(R.id.image_point_high) ImageView image_point_high;
	@BindView(R.id.flipper_result) ViewFlipper viewFlipper;

	private BPM bpm;

	@Override public void initView() {
		button_back.setOnClickListener(view -> {
			BPMFragmentManager.getInstance().showFragment(BPMStatisticsFragment.class);
			BPMFragmentManager.getInstance().removeFragment(BPMStatisticsDetailFragment.class);
		});
		if (savedInstanceState == null) {
			bpm = (BPM) getArguments().getSerializable(Global.KEY_BPM);

		} else {
			bpm = (BPM) savedInstanceState.getSerializable(Global.KEY_BPM);
		}

		initProfile();
		setValue(bpm);
		setGraphState(bpm);
	}

	@Override protected int getLayoutId() { return R.layout.fragment_bpm_home_statistics_detail; }


	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putSerializable(Global.KEY_BPM, bpm);
		
		super.onSaveInstanceState(outState);
	}

	public void showNextPage() {
		 viewFlipper.setInAnimation(leftInAnimation);
         viewFlipper.setOutAnimation(leftOutAnimation);
         viewFlipper.showNext();//向右滑动
	}
	
	
	public void showPreviousPage() {
		viewFlipper.setInAnimation(rightInAnimation);
        viewFlipper.setOutAnimation(rightOutAnimation);
        viewFlipper.showPrevious();//向左滑动
	}
	
	
	private void initProfile() {
		String name = ProfileHelper.getCurrentUseProfileName(getActivity());
		
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
			type = CalculateHelper.getBPMType(bpm.getSystolic());
		}
		
		typeCenter = CalculateHelper.getBPMTypeCenter(systolic, diatolic);
		if (typeCenter == Global.TYPE_BPM_TYPE_LOW) {
			text_type.setText(getString(R.string.Low));
		} else if (typeCenter == Global.TYPE_BPM_TYPE_HIGH)  {
			text_type.setText(getString(R.string.High));
		} else {
			text_type.setText(getString(R.string.Norm));
		}
		
		if (type == Global.TYPE_BPM_TYPE_LOW) {
			image_point_low.setVisibility(View.VISIBLE);
			image_point_opti.setVisibility(View.INVISIBLE);
			image_point_norm.setVisibility(View.INVISIBLE);
			image_point_mild.setVisibility(View.INVISIBLE);
			image_point_midd.setVisibility(View.INVISIBLE);
			image_point_high.setVisibility(View.INVISIBLE);
			
			text_result.setText(getString(R.string.result_low));
			
		} else if (type == Global.TYPE_BPM_TYPE_OPTI) {
			image_point_low.setVisibility(View.INVISIBLE);
			image_point_opti.setVisibility(View.VISIBLE);
			image_point_norm.setVisibility(View.INVISIBLE);
			image_point_mild.setVisibility(View.INVISIBLE);
			image_point_midd.setVisibility(View.INVISIBLE);
			image_point_high.setVisibility(View.INVISIBLE);
			
			text_result.setText(getString(R.string.result_opti));
			
		} else if (type == Global.TYPE_BPM_TYPE_NORM) {
			image_point_low.setVisibility(View.INVISIBLE);
			image_point_opti.setVisibility(View.INVISIBLE);
			image_point_norm.setVisibility(View.VISIBLE);
			image_point_mild.setVisibility(View.INVISIBLE);
			image_point_midd.setVisibility(View.INVISIBLE);
			image_point_high.setVisibility(View.INVISIBLE);
			
			text_result.setText(getString(R.string.result_norm));
			
		} else if (type == Global.TYPE_BPM_TYPE_MILD) {
			image_point_low.setVisibility(View.INVISIBLE);
			image_point_opti.setVisibility(View.INVISIBLE);
			image_point_norm.setVisibility(View.INVISIBLE);
			image_point_mild.setVisibility(View.VISIBLE);
			image_point_midd.setVisibility(View.INVISIBLE);
			image_point_high.setVisibility(View.INVISIBLE);
			
			text_result.setText(getString(R.string.result_mild));
			
		} else if (type == Global.TYPE_BPM_TYPE_MIDD) {
			image_point_low.setVisibility(View.INVISIBLE);
			image_point_opti.setVisibility(View.INVISIBLE);
			image_point_norm.setVisibility(View.INVISIBLE);
			image_point_mild.setVisibility(View.INVISIBLE);
			image_point_midd.setVisibility(View.VISIBLE);
			image_point_high.setVisibility(View.INVISIBLE);
			
			text_result.setText(getString(R.string.result_midd));
			
		} else if (type == Global.TYPE_BPM_TYPE_HIGH) {
			image_point_low.setVisibility(View.INVISIBLE);
			image_point_opti.setVisibility(View.INVISIBLE);
			image_point_norm.setVisibility(View.INVISIBLE);
			image_point_mild.setVisibility(View.INVISIBLE);
			image_point_midd.setVisibility(View.INVISIBLE);
			image_point_high.setVisibility(View.VISIBLE);
			
			text_result.setText(getString(R.string.result_high));
			
		}
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

}
