package com.lgb.myfitness.bpm.settings;

import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;

import com.lgb.myfitness.helper.FragmentHelper;
import com.lgb.myfitness.helper.LanguageHelper;
import com.lgb.myfitness.helper.WheelHelper;
import com.lgb.myfitness.R;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class BPMSettingsLanguageFragment extends Fragment{

	private PopupWindow mPopupWindow_language;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_bpm_settings_language, container, false);
		initUI(view);
		
		initLanguage();
		
		return view;
	}
	

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	
	/**
	 * my OnClickListener
	 */
	private OnClickListener myOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.button_back:
				FragmentHelper.actionBack_bpm_setting(getActivity(), BPMSettingsLanguageFragment.this);
				
				break;
			case R.id.layout_language:
				showLanguagePicker();
				
				break;
			case R.id.button_save:
				actionClickSave();
				
				break;
			default:
				break;
			}
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
	
	
	private void initLanguageArray() {
		array_language = new String[]{getString(R.string.English), getString(R.string.German), getString(R.string.French)};
	}
	
	
	/**
	 * 初始化UI
	 * @param view
	 */
	private void initUI(View view) {
		initLanguageArray();
		
		ImageView button_back = (ImageView) view.findViewById(R.id.button_back);
		button_back.setOnClickListener(myOnClickListener);
		
		ImageView button_save = (ImageView) view.findViewById(R.id.button_save);
		button_save.setOnClickListener(myOnClickListener);
		
		View view_language = view.findViewById(R.id.layout_language);
		view_language.setOnClickListener(myOnClickListener);
		
		view_language_label = view.findViewById(R.id.layout_label_language);
		
		text_language = (TextView) view.findViewById(R.id.text_language);
	}
	private View view_language_label;
	private TextView text_language;
	
	private WheelView wheel_language;
	private ArrayWheelAdapter<String> adapter_language;
	private String[] array_language;
}
