package com.lgb.myfitness.module.bpm.settings;

import com.lgb.mvp.SimpleBaseFragment;
import com.lgb.myfitness.bpm.main.DecalrartionFragment;
import com.lgb.myfitness.bpm.main.ImpressumFragment;
import com.lgb.myfitness.global.Global;
import com.lgb.myfitness.helper.FragmentHelper;
import com.lgb.myfitness.wristband.settings.SettingsAboutUsFragment;
import com.lgb.myfitness.R;
import com.lgb.xpro.utils.NoDoubleClickListener;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;

public class BPMSettingsAboutUsFragment extends SimpleBaseFragment {

	@BindView(R.id.declaration) TextView tv_declaration;
	@BindView(R.id.impressum) TextView tv_impressum;
	@BindView(R.id.button_back) ImageView button_back;

	private FragmentManager fMgr;

	@Override
	public void initView() {

		button_back.setOnClickListener(clickListener);
		tv_declaration.setOnClickListener(clickListener);
		tv_impressum.setOnClickListener(clickListener);
	}

	@Override protected int getLayoutId() { return R.layout.fragment_band_settings_about_us; }

	/**
	 * my OnClickListener
	 */

	private NoDoubleClickListener clickListener = new NoDoubleClickListener() {
		@Override
		protected void onNoDoubleClick(View v) {
			switch (v.getId()) {
				case R.id.button_back:
					FragmentHelper.actionBack_bpm_setting(getActivity(), BPMSettingsAboutUsFragment.this);

					break;
				case R.id.declaration:
					hideAboutUsFragment();

					Fragment fragment_reminder = new DecalrartionFragment();
					FragmentHelper.addFragment(fMgr, fragment_reminder, Global.FRAGMENT_IMPRESSUM);
					break;
				case R.id.impressum:
					hideAboutUsFragment();

					Fragment fragment = new ImpressumFragment();
					FragmentHelper.addFragment(fMgr, fragment, Global.FRAGMENT_DECALRARTION);
					break;
				default:
					break;
			}
		}
	};

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
