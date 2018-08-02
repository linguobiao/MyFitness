package com.lgb.myfitness.bpm.main;
import com.lgb.myfitness.global.Global;
import com.lgb.myfitness.helper.FragmentHelper;
import com.lgb.myfitness.wristband.settings.SettingsAboutUsFragment;
import com.lgb.myfitness.R;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class DecalrartionFragment extends Fragment {
	
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.fragment_declaration);
//		setTitle(R.string.declaration);
//	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_declaration, container, false);
		view.findViewById(R.id.button_back).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						FragmentManager fMgr = getActivity().getFragmentManager();
						FragmentHelper.removeFragment(fMgr, DecalrartionFragment.this);
						
						Fragment fragment_aboutUs = fMgr.findFragmentByTag(Global.FRAGMENT_BAND_SETTINGS_ABOUT_US);
						if (fragment_aboutUs != null) {
							FragmentHelper.hideAllFragment(fMgr);
							
							// 显示settings fragment
							FragmentHelper.showFragment(fMgr, fragment_aboutUs);
						} else {
							FragmentHelper.hideAllFragment(fMgr);
							
							fragment_aboutUs = new SettingsAboutUsFragment();
							FragmentHelper.addFragment(fMgr, fragment_aboutUs, Global.FRAGMENT_BAND_SETTINGS_ABOUT_US);
						}
					}
				});
		return view;
	}
}
