package com.lgb.myfitness.module.bpm;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import com.lgb.mvp.SimpleBaseFragment;
import com.lgb.myfitness.R;
import com.lgb.myfitness.module.bpm.settings.BPMSettingsLanguageFragment;
import com.lgb.myfitness.module.bpm.settings.BPMSettingsProfileFragment;
import com.lgb.myfitness.module.bpm.settings.BPMSettingsAboutUsFragment;
import com.lgb.myfitness.splash.TypeActivity;

import butterknife.BindView;

public class BPMSettingsFragment extends SimpleBaseFragment{
	@BindView(R.id.layout_profile) View view_profile;
	@BindView(R.id.layout_about_us) View layout_about_us;
	@BindView(R.id.layout_language) View layout_language;
	@BindView(R.id.layout_choice_device) View layout_choice_device;

	@Override
	public void initView() {
		view_profile.setOnClickListener(myOnClickListener);
		layout_about_us.setOnClickListener(myOnClickListener);
		layout_language.setOnClickListener(myOnClickListener);
		layout_choice_device.setOnClickListener(myOnClickListener);
	}

	@Override protected int getLayoutId() { return R.layout.fragment_bpm_settings; }

	private OnClickListener myOnClickListener = v -> {
        switch (v.getId()) {
        case R.id.layout_profile:
            BPMFragmentManager.getInstance().showFragment(BPMSettingsProfileFragment.class);
            break;
        case R.id.layout_about_us:
            BPMFragmentManager.getInstance().showFragment(BPMSettingsAboutUsFragment.class);
            break;
        case R.id.layout_language:
            BPMFragmentManager.getInstance().showFragment(BPMSettingsLanguageFragment.class);
            break;
        case R.id.layout_choice_device:
            Intent intent = new Intent(getActivity(), TypeActivity.class);
            getActivity().startActivity(intent);
            getActivity().finish();
            break;
        default: break;
        }
    };

}
