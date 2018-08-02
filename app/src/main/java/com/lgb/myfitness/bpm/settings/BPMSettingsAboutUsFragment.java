package com.lgb.myfitness.bpm.settings;

import com.lgb.myfitness.bpm.main.DecalrartionFragment;
import com.lgb.myfitness.bpm.main.ImpressumFragment;
import com.lgb.myfitness.global.Global;
import com.lgb.myfitness.helper.FragmentHelper;
import com.lgb.myfitness.wristband.settings.SettingsAboutUsFragment;
import com.lgb.myfitness.R;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class BPMSettingsAboutUsFragment extends Fragment {

	private FragmentManager fMgr;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_band_settings_about_us,
				container, false);
		initUI(view);

		view.findViewById(R.id.declaration).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						hideAboutUsFragment();

						Fragment fragment_reminder = new DecalrartionFragment();
						FragmentHelper.addFragment(fMgr, fragment_reminder,
								Global.FRAGMENT_IMPRESSUM);
					}
				});

		view.findViewById(R.id.impressum).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						hideAboutUsFragment();

						Fragment fragment_reminder = new ImpressumFragment();
						FragmentHelper.addFragment(fMgr, fragment_reminder,
								Global.FRAGMENT_DECALRARTION);
					}
				});

		return view;
	}

	/**
	 * my OnClickListener
	 */
	private OnClickListener myOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.button_back:
				FragmentHelper.actionBack_bpm_setting(getActivity(),
						BPMSettingsAboutUsFragment.this);

				break;
			default:
				break;
			}
		}
	};

	/**
	 * 初始化UI
	 * 
	 * @param view
	 */
	private void initUI(View view) {

		ImageView button_back = (ImageView) view.findViewById(R.id.button_back);
		button_back.setOnClickListener(myOnClickListener);

	}

	/**
	 * 隐藏settings fragment
	 */
	private void hideAboutUsFragment() {
		if (fMgr == null) {
			fMgr = getActivity().getFragmentManager();
		}

		SettingsAboutUsFragment fragment_aboutUs = (SettingsAboutUsFragment) fMgr
				.findFragmentByTag(Global.FRAGMENT_BAND_SETTINGS_ABOUT_US);
		if (fragment_aboutUs != null) {
			FragmentTransaction ft = fMgr.beginTransaction();
			ft.hide(fragment_aboutUs);
			ft.commit();
		}

	}
	
}
