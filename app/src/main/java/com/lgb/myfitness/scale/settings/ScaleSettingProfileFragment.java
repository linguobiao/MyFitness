package com.lgb.myfitness.scale.settings;

import java.util.List;
import com.lgb.myfitness.R;
import com.lgb.myfitness.been.Profile;
import com.lgb.myfitness.database.DatabaseProvider_public;
import com.lgb.myfitness.database.DatabaseProvider_scale;
import com.lgb.myfitness.global.Global;
import com.lgb.myfitness.helper.FragmentHelper;
import com.lgb.myfitness.helper.KeyBoardHelper;
import com.lgb.myfitness.helper.ProfileHelper;
import com.lgb.myfitness.scale.main.ScaleMainActivity;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

public class ScaleSettingProfileFragment extends Fragment {

	private SharedPreferences mPrefs = null;
//	private String profile_name = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_scale_settings_profile_,
				container, false);
		initUI(view);
		
		initProfileData();

		return view;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		try {
			mCallback = (OnProfileUpdateListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnUpdateListener");
		}
	}

	private OnProfileUpdateListener mCallback;

	public interface OnProfileUpdateListener {
		public void onProfileUpdate();
		public void endNewStartUp();
	}

	/**
	 * my OnClickListener
	 */
	private OnClickListener myOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.button_back:
				KeyBoardHelper.hideKeyboard(getActivity());
				actionBack();
				
				break;
			case R.id.button_save:
				KeyBoardHelper.hideKeyboard(getActivity());
				actionSave();
				
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 返回事件
	 */
	private void actionBack() {
		FragmentHelper.actionBack_scale(getActivity(), this);
	}

	/**
	 * 保存事件
	 */
	private void actionSave() {
		
		String nameStr = edit_name.getText().toString();
		String heightStr = edit_height.getText().toString();
		String weightStr = edit_weight.getText().toString();
		
		boolean isNameOK = false;
		boolean isHeightOk = false;
		boolean isWeihtOk = false;
		
		if (!TextUtils.isEmpty(nameStr)) {
			isNameOK = true;
		}
		if (!TextUtils.isEmpty(heightStr)) {
			double heightDouble = Double.parseDouble(heightStr);
			if (heightDouble > 300 || heightDouble < 0) {
				isHeightOk = false;
			} else {
				isHeightOk = true;
			}
		}
		if (!TextUtils.isEmpty(weightStr)) {
			double weightDouble = Double.parseDouble(weightStr);
			if (weightDouble > 300 || weightDouble < 0) {
				isWeihtOk = false;
			} else {
				isWeihtOk = true;
			}
		}

		if (isNameOK && isHeightOk && isWeihtOk) {
			saveProfileToDB();
			saveCurrentUseProfileName();
			saveGoalWeight();
			
			boolean isNewsStartup = ScaleMainActivity.getIsNewStartup();
			Log.i(getTag(), "is new start up: " + isNewsStartup);
			// 如果是new start up 跳转到 fragment_goal
			if (isNewsStartup) {
				FragmentManager fMgr = getActivity().getFragmentManager();
				FragmentHelper.removeFragment(fMgr, this);
			
				mCallback.endNewStartUp();
			} else {
				Toast.makeText(getActivity(),
						getString(R.string.Save_succeeded), Toast.LENGTH_SHORT)
						.show();
			}	

			mCallback.onProfileUpdate();
		} else if (!isNameOK) {
			Toast.makeText(getActivity(),
					getString(R.string.name_can_not_be_empty),
					Toast.LENGTH_SHORT).show();
		} else if (!isHeightOk) {
			Toast.makeText(getActivity(),
					getString(R.string.Profile_save_fail_height),
					Toast.LENGTH_SHORT).show();
		} else if (!isWeihtOk) {
			Toast.makeText(getActivity(),
					getString(R.string.Profile_save_fail_weight),
					Toast.LENGTH_SHORT).show();
		}
	}

	
	private void saveProfileToDB() {
		Profile profile = new Profile();
		// 性别
		if (radio_man.isChecked()) {
			profile.setGender(Global.TYPE_GENDER_MALE);
		} else if (radio_woman.isChecked()) {
			profile.setGender(Global.TYPE_GENDER_FEMALE);
		}
		// 名字
		String name = edit_name.getText().toString();
		if (name.equalsIgnoreCase(getString(R.string.Name))) {
			profile.setName(name);
		} else {
			profile.setName(name);
		}
		// 年龄
		String ageStr = edit_age.getText().toString();
		if (!TextUtils.isEmpty(ageStr)) {
			profile.setAge(Integer.parseInt(ageStr));
		}
		// 身高
		String heightStr = edit_height.getText().toString();
		if (!TextUtils.isEmpty(heightStr)) {
			double height = Double.parseDouble(heightStr);
			profile.setHeight(height);
		}
		// 体重
		String weightStr = edit_weight.getText().toString();
		if (!TextUtils.isEmpty(weightStr)) {
			double weight = Double.parseDouble(weightStr);
			profile.setWeight(weight);
		}

		Profile temp = DatabaseProvider_public.queryProfile(getActivity(), name);
		System.out.println("text name:" + edit_name.getText().toString());
		if (temp == null) {
			// 插入
			DatabaseProvider_public.insertProfile(getActivity(), profile);
		} else {
			// 更新
			DatabaseProvider_public.updateProfile(getActivity(), name, profile);
		}
	}

	private void saveCurrentUseProfileName() {
		if (mPrefs == null) {
			mPrefs = getActivity().getSharedPreferences(Global.KEY_PREF,
					Context.MODE_PRIVATE);
		}

		Editor editor = mPrefs.edit();

		editor.putString(Global.KEY_PROFILE_NAME, edit_name.getText()
				.toString());

		editor.commit();
	}
	
	
	private void saveGoalWeight() {
		String goalWeightStr = edit_goal_weight.getText().toString();
		if (!TextUtils.isEmpty(goalWeightStr)) {
			double goalWeight = Double.parseDouble(goalWeightStr);
			
			int profileID = ProfileHelper.initProfileID(getActivity());
			Double value = DatabaseProvider_scale.queryGoalWeight(getActivity(), profileID);
			if (value != null) {
				DatabaseProvider_scale.updateGoalWeight(getActivity(), profileID, goalWeight);
				
			} else {
				DatabaseProvider_scale.insertGoalWeight(getActivity(), profileID, goalWeight);
				
			}
		} else {
			double goalWeight = 0;
			
			int profileID = ProfileHelper.initProfileID(getActivity());
			Double value = DatabaseProvider_scale.queryGoalWeight(getActivity(), profileID);
			if (value != null) {
				DatabaseProvider_scale.updateGoalWeight(getActivity(), profileID, goalWeight);
				
			} else {
				DatabaseProvider_scale.insertGoalWeight(getActivity(), profileID, goalWeight);
				
			}
		}
	}

	
	/**
	 * 初始化profile数据
	 */
	private void initProfileData() {

		String nameQuery = ProfileHelper.getCurrentUseProfileName(getActivity());;
		Profile profile = DatabaseProvider_public.queryProfile(getActivity(),
				nameQuery);
		// Profile profile = DatabaseProvider.queryProfile(getActivity());
		if (profile != null) {
			// System.out.println(profile.getID() + "," + profile.getName() +
			// ", " + profile.getGender() + ", " + profile.getHeight() + ", " +
			// profile.getWeight());
			// 名字
			String name = profile.getName();
			edit_name.setText(name);

			// 性别
			int gender = profile.getGender();
			if (gender == Global.TYPE_GENDER_MALE) {
				radio_man.setChecked(true);
			} else {
				radio_woman.setChecked(true);
			}
			// 年龄
			int age = profile.getAge();
			if (age != 0) {
				edit_age.setText(String.valueOf(age));
			}
			// 身高
			double height = profile.getHeight();
			if (height != 0) {
				edit_height.setText(Global.df_double_1.format(height)
						.replaceAll(",", "."));
			}
			// 体重
			double weight = profile.getWeight();
			if (weight != 0) {
				edit_weight.setText(Global.df_double_1.format(weight)
						.replaceAll(",", "."));
			}
			// goal weight
			int profileID = profile.getID();
			Double goalWeight = DatabaseProvider_scale.queryGoalWeight(getActivity(), profileID);
			if (goalWeight != null && goalWeight != 0) {
				edit_goal_weight.setText(String.valueOf(goalWeight).replaceAll(",", "."));
			}

		} else {
			edit_name.setText("");
			edit_age.setText("");
			radio_man.setChecked(true);
			edit_height.setText("");
			edit_weight.setText("");
			edit_goal_weight.setText("");
		}

		List<Profile> profileList = DatabaseProvider_public
				.queryProfile(getActivity());
		Log.i(getTag(), "*****************************");
		for (Profile test : profileList) {
			Log.i(getTag(), test.getID() + ", " + test.getName());
		}
		Log.i(getTag(), "*****************************");
	}


	/**
	 * 初始化UI
	 * 
	 * @param view
	 */
	private void initUI(View view) {

		ImageView button_back = (ImageView) view.findViewById(R.id.button_back);
		button_back.setOnClickListener(myOnClickListener);
		ImageView button_save = (ImageView) view.findViewById(R.id.button_save);
		button_save.setOnClickListener(myOnClickListener);

		radio_man = (RadioButton) view.findViewById(R.id.radio_man);
		radio_man.setChecked(true);
		radio_woman = (RadioButton) view.findViewById(R.id.radio_woman);

		edit_name = (EditText) view.findViewById(R.id.edit_name);
		edit_age = (EditText) view.findViewById(R.id.edit_age);
		edit_height = (EditText) view.findViewById(R.id.edit_height);
		edit_weight = (EditText) view.findViewById(R.id.edit_weight);
		edit_goal_weight = (EditText) view.findViewById(R.id.edit_goal_weight);
	}

	private RadioButton radio_man, radio_woman;
	private EditText edit_name, edit_age, edit_height, edit_weight, edit_goal_weight;

}
