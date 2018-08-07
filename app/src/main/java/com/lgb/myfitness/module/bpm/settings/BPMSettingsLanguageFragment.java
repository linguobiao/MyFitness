package com.lgb.myfitness.module.bpm.settings;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.lgb.mvp.SimpleBaseFragment;
import com.lgb.myfitness.R;
import com.lgb.myfitness.helper.LanguageHelper;
import com.lgb.myfitness.helper.WheelHelper;
import com.lgb.myfitness.module.bpm.BPMFragmentManager;
import com.lgb.myfitness.module.bpm.BPMSettingsFragment;

import butterknife.BindView;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;

public class BPMSettingsLanguageFragment extends SimpleBaseFragment{

	@BindView(R.id.button_back) ImageView button_back;
	@BindView(R.id.button_save) ImageView button_save;
	@BindView(R.id.layout_language) View layout_language;
	@BindView(R.id.layout_label_language) View view_language_label;
	@BindView(R.id.text_language) TextView text_language;

	private WheelView wheel_language;
	private ArrayWheelAdapter<String> adapter_language;
	private String[] array_language;
	private PopupWindow mPopupWindow_language;

	@Override
	public void initView() {
		array_language = new String[]{getString(R.string.English), getString(R.string.German), getString(R.string.French)};
		button_back.setOnClickListener(myOnClickListener);
		button_save.setOnClickListener(myOnClickListener);
		layout_language.setOnClickListener(myOnClickListener);
		initLanguage();
	}

	@Override protected int getLayoutId() { return R.layout.fragment_bpm_settings_language; }
	
	/**
	 * my OnClickListener
	 */
	private OnClickListener myOnClickListener = v -> {
        switch (v.getId()) {
        case R.id.button_back:
            BPMFragmentManager.getInstance().returnMainFragment(BPMSettingsFragment.class);
            break;
        case R.id.layout_language:
            showLanguagePicker();
            break;
        case R.id.button_save:
            actionClickSave();
            break;
        default: break;
        }
    };
	
	
	private void showLanguagePicker() {
		if (mPopupWindow_language == null) {
			View popupView = getActivity().getLayoutInflater().inflate(R.layout.dialog_wheel_one, null);

			mPopupWindow_language = new PopupWindow(popupView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
			mPopupWindow_language.setTouchable(true);
			mPopupWindow_language.setOutsideTouchable(true);
			mPopupWindow_language.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap)null));
	        
			wheel_language = (WheelView) popupView.findViewById(R.id.wheel_wheel);
			adapter_language = new ArrayWheelAdapter<String>(getActivity(), array_language);
			wheel_language.setViewAdapter(adapter_language);
			
			Button button_commit = (Button) popupView.findViewById(R.id.button_one_wheel_commit);
			button_commit.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					String language = array_language[wheel_language.getCurrentItem()];
					
					view_language_label.setVisibility(View.GONE);
					text_language.setVisibility(View.VISIBLE);
					text_language.setText(language);
					
					mPopupWindow_language.dismiss();
				}
			});
			
			mPopupWindow_language.showAtLocation(getActivity().findViewById(R.id.layout_main), Gravity.BOTTOM, 0, 0);
	        
		} else {
			mPopupWindow_language.showAtLocation(getActivity().findViewById(R.id.layout_main), Gravity.BOTTOM, 0, 0);
		}
		
		String language = text_language.getText().toString();
		if (language != null) {

			WheelHelper.setWheelCurrentItem(language, array_language, wheel_language);
		}
	}
	
	
	private void actionClickSave() {
		String language = text_language.getText().toString();
		
		if (language.equals("")) {
			Toast.makeText(getActivity(), getString(R.string.Please_select_a_language), Toast.LENGTH_LONG).show();
			
		} else {
			LanguageHelper.saveLanguage(getActivity(), language);
			
			Toast.makeText(getActivity(), getString(R.string.Save_succeeded), Toast.LENGTH_SHORT).show();
		}
	}
	
	
	private void initLanguage() {
		String language = LanguageHelper.getBPLanguage(getActivity());
		if (language.equals("")) {
			view_language_label.setVisibility(View.VISIBLE);
			text_language.setText("");
			
		} else {
			view_language_label.setVisibility(View.GONE);
			text_language.setVisibility(View.VISIBLE);
			
			if (language.equals(getString(R.string.English))) {
				text_language.setText(getString(R.string.English));
				
			} else if (language.equals(getString(R.string.German))) {
				text_language.setText(getString(R.string.German));
				
			} else if (language.equals(getString(R.string.French))) {
				text_language.setText(getString(R.string.French));
				
			}
			
		}
	}
}
