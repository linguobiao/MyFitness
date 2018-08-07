package com.lgb.myfitness.module.bpm.settings.about;

import android.view.View;

import com.lgb.mvp.SimpleBaseFragment;
import com.lgb.myfitness.R;
import com.lgb.myfitness.module.bpm.BPMFragmentManager;
import com.lgb.myfitness.module.bpm.settings.BPMSettingsAboutUsFragment;

import butterknife.BindView;

public class BPMImpressumFragment extends SimpleBaseFragment {

	@BindView(R.id.button_back) View button_back;

	@Override
	public void initView() {
		button_back.setOnClickListener(view -> BPMFragmentManager.getInstance().showFragment(BPMSettingsAboutUsFragment.class));
	}

	@Override protected int getLayoutId() { return R.layout.fragment_impressum; }
}
