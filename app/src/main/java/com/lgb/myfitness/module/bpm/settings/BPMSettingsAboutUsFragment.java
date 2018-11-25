package com.lgb.myfitness.module.bpm.settings;

import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.lgb.mvp.SimpleBaseFragment;
import com.lgb.myfitness.R;
import com.lgb.myfitness.module.bpm.settings.about.BPMDeclarationFragment;
import com.lgb.myfitness.module.bpm.settings.about.BPMImpressumFragment;
import com.lgb.myfitness.module.bpm.BPMFragmentManager;
import com.lgb.myfitness.module.bpm.BPMSettingsFragment;
import com.lgb.xpro.utils.AppUtils;
import com.lgb.xpro.utils.NoDoubleClickListener;

import butterknife.BindView;

public class BPMSettingsAboutUsFragment extends SimpleBaseFragment {

	@BindView(R.id.tv_version) TextView tv_version;
	@BindView(R.id.declaration) TextView tv_declaration;
	@BindView(R.id.impressum) TextView tv_impressum;
	@BindView(R.id.button_back) ImageView button_back;
	@BindView(R.id.wv_about)
	WebView wv_about;

	@Override
	public void initView() {
		button_back.setOnClickListener(clickListener);
		tv_declaration.setOnClickListener(clickListener);
		tv_impressum.setOnClickListener(clickListener);
		tv_version.setText(getString(R.string.version_code, AppUtils.getVersionName(getActivity())));//版本号
//		wv_about.loadUrl("file:///android_asset/about_us.webarchive");
		wv_about.loadUrl("file:///android_asset/about.html");
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
					BPMFragmentManager.getInstance().returnMainFragment(BPMSettingsFragment.class);
					break;
				case R.id.declaration:
					BPMFragmentManager.getInstance().showFragment(BPMDeclarationFragment.class);
					break;
				case R.id.impressum:
					BPMFragmentManager.getInstance().showFragment(BPMImpressumFragment.class);
					break;
				default:
					break;
			}
		}
	};
	
}
