package com.lgb.myfitness.scale.settings;

import com.lgb.myfitness.R;
import com.lgb.myfitness.helper.FragmentHelper;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class ScaleSettingAboutUsFragment extends Fragment{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_band_settings_about_us, container, false);
		
		initUI(view);
		
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
				FragmentHelper.actionBack_scale(getActivity(), ScaleSettingAboutUsFragment.this);
				break;
			default:
				break;
			}
		}
	};
	
	
	/**
	 * 初始化UI
	 * @param view
	 */
	private void initUI(View view) {
		
		ImageView button_back = (ImageView) view.findViewById(R.id.button_back);
		button_back.setOnClickListener(myOnClickListener);
		
	}
	
}
