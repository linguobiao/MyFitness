package com.lgb.myfitness.bpm.main;

import com.lgb.myfitness.been.BPM;
import com.lgb.myfitness.global.Global;
import com.lgb.myfitness.helper.CalculateHelper;
import com.lgb.myfitness.helper.FragmentHelper;
import com.lgb.myfitness.helper.ProfileHelper;
import com.lgb.myfitness.R;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class BPMStatisticsDetailFragment extends Fragment{

	private BPM bpm;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_bpm_home_statistics_detail, container, false);
		initUI(view);
		
		if (savedInstanceState == null) {
			bpm = (BPM) getArguments().getSerializable(Global.KEY_BPM);
			
		} else {
			bpm = (BPM) savedInstanceState.getSerializable(Global.KEY_BPM);
		}
		
		initProfile();
		setValue(bpm);
		setGraphState(bpm);
		
		return view;
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
				
				FragmentManager fgmr = getFragmentManager();
				FragmentHelper.removeFragment(fgmr, BPMStatisticsDetailFragment.this);
				
				Fragment fragment_bpm_statistics = fgmr.findFragmentByTag(Global.FRAGMENT_BPM_STATISTICS);
				if (fragment_bpm_statistics != null) {
					FragmentHelper.hideAllFragment_bpm(fgmr);
					
					FragmentHelper.showFragment(fgmr, fragment_bpm_statistics);
					
				} else {
					fragment_bpm_statistics = new BPMStatisticsFragment();
					
					FragmentHelper.addFragment(fgmr, fragment_bpm_statistics, Global.FRAGMENT_BPM_STATISTICS);
				}
				
				break;
			default:
				break;
			}
			
		}
	};
	
	
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
	
	
	private void initUI(View view) {
		ImageView button_back = (ImageView) view.findViewById(R.id.button_back);
		button_back.setOnClickListener(myOnClickListener);
		
		text_name = (TextView) view.findViewById(R.id.text_name);
		
		text_sys = (TextView) view.findViewById(R.id.text_sys);
		text_dia = (TextView) view.findViewById(R.id.text_dia);
		text_hr = (TextView) view.findViewById(R.id.text_hr);
		
		text_date = (TextView) view.findViewById(R.id.text_date);
		text_time = (TextView) view.findViewById(R.id.text_time);
		
		image_point_low = (ImageView) view.findViewById(R.id.image_point_low);
		image_point_opti = (ImageView) view.findViewById(R.id.image_point_opti);
		image_point_norm = (ImageView) view.findViewById(R.id.image_point_norm);
		image_point_mild = (ImageView) view.findViewById(R.id.image_point_mild);
		image_point_midd = (ImageView) view.findViewById(R.id.image_point_midd);
		image_point_high = (ImageView) view.findViewById(R.id.image_point_high);
		
		text_type = (TextView) view.findViewById(R.id.text_type);
		
		text_result = (TextView) view.findViewById(R.id.text_result);
		
		viewFlipper = (ViewFlipper)view.findViewById(R.id.flipper_result);
	}
	private TextView text_name;
	private TextView text_sys, text_dia, text_hr;
	private TextView text_date, text_time;
	
	private ImageView image_point_low, image_point_opti, image_point_norm, image_point_mild, image_point_midd, image_point_high;
	private TextView text_type;
	private TextView text_result;
	
	private ViewFlipper viewFlipper;
	
	
	private Animation leftInAnimation;
	private Animation leftOutAnimation;
	private Animation rightInAnimation;
	private Animation rightOutAnimation;
	
}
