package com.lgb.myfitness.module.bpm;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.lgb.mvp.SimpleBaseFragment;
import com.lgb.myfitness.R;
import com.lgb.myfitness.been.BPM;
import com.lgb.myfitness.module.bpm.main.BPMTestFragment;
import com.lgb.myfitness.database.DatabaseProvider_bpm;
import com.lgb.myfitness.helper.ProfileHelper;
import com.lgb.myfitness.module.bpm.main.BPMStatisticsFragment;

import java.util.List;

import butterknife.BindView;

public class BPMFragment extends SimpleBaseFragment{

	@BindView(R.id.text_sys) TextView text_sys;
	@BindView(R.id.text_dia) TextView text_dia;
	@BindView(R.id.layout_value) View layout_value;
	@BindView(R.id.layout_device) View layout_device;
	@BindView(R.id.layout_history) View layout_history;

	private int profileID;

	@Override
	public void initView() {
		layout_value.setOnClickListener(myOnClickListener);
		layout_device.setOnClickListener(myOnClickListener);
		layout_history.setOnClickListener(myOnClickListener);

		profileID = ProfileHelper.initProfileID(getActivity());
		initBPM();
	}

	@Override protected int getLayoutId() { return R.layout.fragment_bpm_home; }

	/**
	 * my OnClickListener
	 */
	private OnClickListener myOnClickListener = v -> {
        switch (v.getId()) {
        case R.id.layout_value:
			BPMFragmentManager.getInstance().showFragment(BPMStatisticsFragment.class);
            break;
        case R.id.layout_device:
			BPMFragmentManager.getInstance().showFragment(BPMTestFragment.class);
            break;
        case R.id.layout_history:
			BPMFragmentManager.getInstance().showFragment(BPMStatisticsFragment.class);
            break;
        default:
            break;
        }
    };


	public void initBPM() {
		List<BPM> bpmList = DatabaseProvider_bpm.queryBPM_desc(getActivity(), profileID, 1);
		
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
}
