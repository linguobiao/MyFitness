package com.lgb.myfitness.scale.main;

import com.lgb.myfitness.R;
import com.lgb.myfitness.global.Global;
import com.lgb.myfitness.helper.FragmentHelper;
import com.lgb.myfitness.scale.settings.ScaleSettingAboutUsFragment;
import com.lgb.myfitness.scale.settings.ScaleSettingProfileFragment;
import com.lgb.myfitness.splash.TypeActivity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

public class ScaleSettingFragment extends Fragment{
	
	private FragmentManager fMgr;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_scale_settings, container, false);
		initUI(view);
		
		return view;
	}
	
	private OnClickListener myOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.layout_profile:
				actionClickProfile();
				
				break;
			case R.id.layout_about_us:
				actionClickAboutUs();
				
				break;
			case R.id.layout_choice_device:
				actionClickChoiceDevice();
				
				break;
			default:
				break;
			}
		}
	};
	
	
	private void actionClickProfile() {
		if (fMgr == null) {
			fMgr = getFragmentManager();
		}
		
		FragmentHelper.hideAllFragment_scale(fMgr);
		
		Fragment fragment_profile = new ScaleSettingProfileFragment();
		FragmentHelper.addFragment(fMgr, fragment_profile, Global.FRAGMENT_SCALES_SETTINGS_PROFILE);
	}
	
	
	private void actionClickAboutUs() {
		if (fMgr == null) {
			fMgr = getFragmentManager();
		}
		
		FragmentHelper.hideAllFragment_scale(fMgr);
		
		Fragment fragment_about_us = new ScaleSettingAboutUsFragment();
		FragmentHelper.addFragment(fMgr, fragment_about_us, Global.FRAGMENT_SCALES_SETTINGS_ABOUT_US);
		
	}
	
	
	private void actionClickChoiceDevice() {
		getActivity().finish();
		
		Intent intent = new Intent(getActivity(), TypeActivity.class);
		getActivity().startActivity(intent);
	}
	
	
	private void initUI(View view) {
		View view_profile = view.findViewById(R.id.layout_profile);
		view_profile.setOnClickListener(myOnClickListener);
		
		View view_about_us = view.findViewById(R.id.layout_about_us);
		view_about_us.setOnClickListener(myOnClickListener);
		
		View view_choice_device = view.findViewById(R.id.layout_choice_device);
		view_choice_device.setOnClickListener(myOnClickListener);
	}
}
