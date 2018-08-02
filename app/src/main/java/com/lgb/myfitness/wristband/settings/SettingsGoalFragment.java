package com.lgb.myfitness.wristband.settings;

import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.lgb.myfitness.R;
import com.lgb.myfitness.been.Goal;
import com.lgb.myfitness.database.DatabaseProvider_public;
import com.lgb.myfitness.global.Global;
import com.lgb.myfitness.helper.FragmentHelper;
import com.lgb.myfitness.helper.ProfileHelper;
import com.lgb.myfitness.helper.UnitHelper;
import com.lgb.myfitness.helper.WheelHelper;
import com.lgb.myfitness.wristband.main.ActivityFragment;
import com.lgb.myfitness.wristband.main.MainActivity;

public class SettingsGoalFragment extends Fragment{
	
	private int clickButton = 0;
	private int profileID = -1;
	private int unitBurn = Global.TYPE_CALORIES_KCAL;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_band_settings_goal, container, false);
		
		profileID = ProfileHelper.initProfileID(getActivity());
		unitBurn = UnitHelper.getBurnUnit(getActivity());
		
		initArray();
		initUI(view);
		initGoal();
		
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
            mCallback = (OnGoalUpdateListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnUpdateListener");
        }
	}

	private OnGoalUpdateListener mCallback;
	public interface OnGoalUpdateListener {
		public void onGoalUpdate();
	}
	
	
	/**
	 * my OnClickListener
	 */
	private OnClickListener myOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.button_back:
				actionBack();
				break;
			case R.id.button_save:
				actionSave();
				break;
			case R.id.button_step:
				clickButton = Global.TYPE_STEP;
				actionClickGoalSelect();
				break;
			case R.id.button_burn:
				clickButton = Global.TYPE_CALORIES;
				actionClickGoalSelect();
				break;
			case R.id.button_sleep:
				clickButton = Global.TYPE_SLEEP;
				actionClickGoalSelect();
				break;
			case R.id.button_one_wheel_commit:
				actionSelectCommit();
				break;
			case R.id.image_title_logo:
				ActivityFragment.actionClickLogo(getActivity());
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
		boolean isNewsStartup = MainActivity.getIsNewStartup();
//		Log.i(getTag(), "is new start up: " + isNewsStartup);
		// 如果是new start up, 返回到fragment_profile
		if (isNewsStartup) {
			FragmentManager fMgr = getActivity().getFragmentManager();
			FragmentHelper.removeFragment(fMgr, this);
			FragmentHelper.addFragment(fMgr, new SettingsProfileFragment(), Global.FRAGMENT_BAND_SETTINGS_PROFILE);
			
		} else {
			FragmentHelper.actionBack(getActivity(), this);
		}
		
	}
	
	/**
	 * 保存事件
	 */
	private void actionSave() {
		
		saveGoalToDatabase();
		
		boolean isNewsStartup = MainActivity.getIsNewStartup();
//		Log.i(getTag(), "is new start up: " + isNewsStartup);
		// 如果是new start up 跳转到 fragment_goal
		if (isNewsStartup) {
			FragmentManager fMgr = getActivity().getFragmentManager();
			FragmentHelper.removeFragment(fMgr, this);
			FragmentHelper.addFragment(fMgr, new SettingsAlarmFragment(), Global.FRAGMENT_BAND_SETTINGS_ALARM);
		} else {
			Toast.makeText(getActivity(), getString(R.string.Save_succeeded), Toast.LENGTH_SHORT).show();
		}	
		
		mCallback.onGoalUpdate();
	}
	
	
	private void saveGoalToDatabase() {
		String goalStr = getString(R.string.GOAL) + " = ";
		
		String stepStr = button_step.getText().toString();
		stepStr = stepStr.replace(goalStr , "");
		
		String burnStr = button_burn.getText().toString();
		burnStr = burnStr.replace(goalStr , "");
		
		String sleepStr = button_sleep.getText().toString();
		sleepStr = sleepStr.replace(goalStr , "");
		sleepStr = sleepStr.replace(" " + getString(R.string.H), "");
		sleepStr = sleepStr.replace(",", ".");
		
		int step = Integer.parseInt(stepStr);
		double burn = Double.parseDouble(burnStr);
		// 以kcal形式保存
		if (unitBurn == Global.TYPE_CALORIES_KCAL) {
			
		} else {
			burn = burn / 1000;
		}
		double sleep = Double.parseDouble(sleepStr);
		Goal goal = new Goal();
		goal.setStep(step);
		goal.setBurn(burn);
		goal.setSleep(sleep);
		
		Goal temp = DatabaseProvider_public.queryGoal(getActivity(), profileID);
		if (temp == null) {
			DatabaseProvider_public.insertGoal(getActivity(), profileID, goal);
		} else {
			DatabaseProvider_public.updateGoal(getActivity(), profileID, goal);
		}
	}
	
	
	private void actionClickGoalSelect() {
		WheelHelper.hideWheel(view_goal);
		String goalStr = getString(R.string.GOAL) + " = ";
		
		if (clickButton == Global.TYPE_STEP) {
			wheel_goal.setViewAdapter(adapter_goal_step);
			
			String step = button_step.getText().toString().replaceAll(goalStr, "") + " " + getString(R.string.steps_day);
			WheelHelper.setWheelCurrentItem(step, ARRAY_GOAL_STEP, wheel_goal);
			
		} else if (clickButton == Global.TYPE_CALORIES) {
			wheel_goal.setViewAdapter(adapter_goal_burn);
			
			if (unitBurn == Global.TYPE_CALORIES_KCAL) {
				String burn = button_burn.getText().toString().replaceAll(goalStr, "") + " " + getString(R.string.kcals_day);
				WheelHelper.setWheelCurrentItem(burn, ARRAY_GOAL_BURN, wheel_goal);
			} else {
				String burn = button_burn.getText().toString().replaceAll(goalStr, "") + " " + getString(R.string.cals_day);
				WheelHelper.setWheelCurrentItem(burn, ARRAY_GOAL_BURN, wheel_goal);
			}
			
			
		} else if (clickButton == Global.TYPE_SLEEP) {
			wheel_goal.setViewAdapter(adapter_goal_sleep);
			
			String sleep = button_sleep.getText().toString().replaceAll(goalStr, "");
			sleep = sleep.replaceAll(" " + getString(R.string.H), "");
			sleep = sleep + " " + getString(R.string.hours_day);
			WheelHelper.setWheelCurrentItem(sleep, ARRAY_GOAL_SLEEP, wheel_goal);
		}
		
		WheelHelper.showWheel(view_goal);
	}
	
	
	/**
	 * 确认事件
	 */
	private void actionSelectCommit() {
		WheelHelper.hideWheel(view_goal);
		String goalStr = getString(R.string.GOAL);
		
		if (clickButton == Global.TYPE_STEP) {
			String steps =ARRAY_GOAL_STEP[wheel_goal.getCurrentItem()];
			steps = steps.replace(" " + getString(R.string.steps_day), "");
			button_step.setText(goalStr + " = " + steps);
		} 
		else if (clickButton == Global.TYPE_SLEEP) {
			String sleep = ARRAY_GOAL_SLEEP[wheel_goal.getCurrentItem()];
			sleep = sleep.replace(" " + getString(R.string.hours_day), "");
			button_sleep.setText(goalStr + " = " + sleep + " " + getString(R.string.H));
		} 
		else if (clickButton == Global.TYPE_CALORIES) {
			String burn = ARRAY_GOAL_BURN[wheel_goal.getCurrentItem()];
			
			if (unitBurn == Global.TYPE_CALORIES_KCAL) {
				burn = burn.replace(" " + getString(R.string.kcals_day), "");
				button_burn.setText(goalStr + " = " + burn);
			} else {
				burn = burn.replace(" " + getString(R.string.cals_day), "");
				button_burn.setText(goalStr + " = " + burn);
			}
			
		}
		
	}
	
	
	/**
	 * 初始化目标值
	 */
	private void initGoal() {
	
		int step = Global.DEFAULT_GOAL_STEP;
		double burn = Global.DEFAULT_GOAL_BURN;
		double sleep = Global.DEFAULT_GOAL_SLEEP;
		
		Goal goal = DatabaseProvider_public.queryGoal(getActivity(), profileID);
		if (goal != null) {
			step = goal.getStep();
			burn = goal.getBurn();
			sleep = goal.getSleep();
		}
		
		if (unitBurn == Global.TYPE_CALORIES_KCAL) {
			
		} else {
			burn = burn * 1000;
		}
		
		String goalStr = getString(R.string.GOAL) + " = ";
		
		button_step.setText(goalStr + step);
		button_burn.setText(goalStr + String.valueOf((int)burn));
		button_sleep.setText(goalStr + sleep + " " + getString(R.string.H));
	}
	
	/**
	 * 初始化wheel
	 */
	private void initArray() {
		ARRAY_GOAL_STEP = new String[48]; 
		int step = 1500;
		for (int i = 0; i < 48; i++) {
			step += 500;
			ARRAY_GOAL_STEP[i] = step + " " + getString(R.string.steps_day);
		}
		
		ARRAY_GOAL_BURN = new String[48];
		int kcal = 75;
		for (int i = 0; i < 48; i++){
			kcal += 25;
			if (unitBurn == Global.TYPE_CALORIES_KCAL) {
				ARRAY_GOAL_BURN[i] = kcal + " " + getString(R.string.kcals_day);
			} else {
				ARRAY_GOAL_BURN[i] = kcal * 1000 + " " + getString(R.string.cals_day);
			}
		}
		
		ARRAY_GOAL_SLEEP = new String[48];
		double hour = 0;
		for (int i = 0; i < 48; i++) {
			hour = hour + 0.5;
			ARRAY_GOAL_SLEEP[i] = hour + " " + getString(R.string.hours_day);
		}
	
	}
	
	/**
	 * 初始化UI
	 * @param view
	 */
	private void initUI(View view) {
		
		ImageView button_back = (ImageView) view.findViewById(R.id.button_back);
		button_back.setOnClickListener(myOnClickListener);
		ImageView button_save = (ImageView) view.findViewById(R.id.button_save);
		button_save.setOnClickListener(myOnClickListener);
		
		view_goal = view.findViewById(R.id.layout_wheel_goal);
		wheel_goal = (WheelView) view.findViewById(R.id.wheel_wheel);

		adapter_goal_step = new ArrayWheelAdapter<String>(getActivity(), ARRAY_GOAL_STEP);
		adapter_goal_burn = new ArrayWheelAdapter<String>(getActivity(), ARRAY_GOAL_BURN);
		adapter_goal_sleep = new ArrayWheelAdapter<String>(getActivity(), ARRAY_GOAL_SLEEP);
		
		Button button_commit = (Button) view.findViewById(R.id.button_one_wheel_commit);
		button_commit.setOnClickListener(myOnClickListener);

		button_step = (Button) view.findViewById(R.id.button_step);
		button_step.setOnClickListener(myOnClickListener);
		
		button_burn = (Button) view.findViewById(R.id.button_burn);
		button_burn.setOnClickListener(myOnClickListener);
		
		button_sleep = (Button) view.findViewById(R.id.button_sleep);
		button_sleep.setOnClickListener(myOnClickListener);
		
		ImageView image_logo = (ImageView) view.findViewById(R.id.image_title_logo);
		image_logo.setOnClickListener(myOnClickListener);

	}
	private View view_goal;
	private WheelView wheel_goal;
	private ArrayWheelAdapter<String> adapter_goal_step, adapter_goal_burn, adapter_goal_sleep;
	private Button button_step, button_sleep, button_burn;
	private String[] ARRAY_GOAL_STEP = null;
	private String[] ARRAY_GOAL_BURN = null;
	private String[] ARRAY_GOAL_SLEEP = null;
	
}
