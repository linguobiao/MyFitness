package com.lgb.myfitness.bpm.main;

import java.util.List;

import com.lgb.myfitness.been.BPM;
import com.lgb.myfitness.database.DatabaseProvider_bpm;
import com.lgb.myfitness.global.Global;
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
import android.widget.TextView;

public class BPMFragment extends Fragment{

	private FragmentManager fMgr;
	private int profileID;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_bpm_home, container, false);
		initUI(view);
		
		profileID = ProfileHelper.initProfileID(getActivity());
		initBPM();
		
		return view;
	}
	
	
	/**
	 * my OnClickListener
	 */
	private OnClickListener myOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.layout_value:
				actionClickValue();
				
				break;
			case R.id.layout_device:
				actionClickBluetoothDevice();
				
				break;
			case R.id.layout_history:
				actionClickValue();
				
				break;
			default:
				break;
			}
		}
	};
	
	
	private void actionClickValue() {
		if (fMgr == null) {
			fMgr = getFragmentManager();
		}
		
		FragmentHelper.hideAllFragment_bpm(fMgr);
		
		Fragment fragment_value = new BPMStatisticsFragment();
		FragmentHelper.addFragment(fMgr, fragment_value, Global.FRAGMENT_BPM_STATISTICS);
	}
	
	
	private void actionClickBluetoothDevice() {
		if (fMgr == null) {
			fMgr = getFragmentManager();
		}
		
		FragmentHelper.hideAllFragment_bpm(fMgr);
		
		Fragment fragment_test = new BPMTestFragment();
		FragmentHelper.addFragment(fMgr, fragment_test, Global.FRAGMENT_BPM_TEST);
	}
	
	
	public void initBPM() {
		List<BPM> bpmList = DatabaseProvider_bpm.queryBPM_desc(getActivity(), profileID, 1);
		
//		for (BPM bpm : bpmList) {
//			System.out.println(Global.sdf_4.format(bpm.getDatetime().getTime()) + ", " + bpm.getSystolic() + ", " + bpm.getDiatolic() + ", " + bpm.getHeartRate());
//		}
		
		if (bpmList != null && bpmList.size() > 0) {
			BPM bpm = bpmList.get(0);
			
			if (bpm != null) {
				text_sys.setText(String.valueOf(bpm.getSystolic()));
				text_dia.setText(String.valueOf(bpm.getDiatolic()));
				
			} else {
				text_sys.setText(String.valueOf(0));
				text_dia.setText(String.valueOf(0));
			}
			
		} else {
			text_sys.setText(String.valueOf(0));
			text_dia.setText(String.valueOf(0));
		}
		
	}
	
	
	private void initUI(View view) {
		text_sys = (TextView) view.findViewById(R.id.text_sys);
		text_dia = (TextView) view.findViewById(R.id.text_dia);
		
		View layout_value = view.findViewById(R.id.layout_value);
		layout_value.setOnClickListener(myOnClickListener);
		
		View layout_device = view.findViewById(R.id.layout_device);
		layout_device.setOnClickListener(myOnClickListener);
		
		View layout_history = view.findViewById(R.id.layout_history);
		layout_history.setOnClickListener(myOnClickListener);
	}
	private TextView text_sys, text_dia;
}
