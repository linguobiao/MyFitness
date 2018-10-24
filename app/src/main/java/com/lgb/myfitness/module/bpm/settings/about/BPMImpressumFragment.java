package com.lgb.myfitness.module.bpm.settings.about;

import android.view.View;
import android.widget.TextView;

import com.lgb.mvp.SimpleBaseFragment;
import com.lgb.myfitness.R;
import com.lgb.myfitness.module.bpm.BPMFragmentManager;
import com.lgb.myfitness.module.bpm.settings.BPMSettingsAboutUsFragment;
import com.lgb.xpro.utils.AppUtils;

import butterknife.BindView;

public class BPMImpressumFragment extends SimpleBaseFragment {

	@BindView(R.id.button_back) View button_back;
	@BindView(R.id.tv_version) TextView tv_version;

	@Override
	public void initView() {
		button_back.setOnClickListener(view -> BPMFragmentManager.getInstance().showFragment(BPMSettingsAboutUsFragment.class));
		tv_version.setText(getString(R.string.impressum_content_2, AppUtils.getVersionName(getActivity())));//版本号
	}

	@Override protected int getLayoutId() { return R.layout.fragment_impressum; }
}
