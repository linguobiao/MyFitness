package com.lgb.myfitness.wristband.main;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import com.lgb.myfitness.R;
import com.lgb.myfitness.been.Band;
import com.lgb.myfitness.been.Goal;
import com.lgb.myfitness.been.HistoryDay;
import com.lgb.myfitness.been.HistoryHour;
import com.lgb.myfitness.been.ScreenData;
import com.lgb.myfitness.database.DatabaseProvider_public;
import com.lgb.myfitness.database.DatabaseProvider_wb013;
import com.lgb.myfitness.global.Global;
import com.lgb.myfitness.helper.BindHelper;
import com.lgb.myfitness.helper.CalculateHelper;
import com.lgb.myfitness.helper.CalendarHelper;
import com.lgb.myfitness.helper.ChartHelper;
import com.lgb.myfitness.helper.FormatHelper;
import com.lgb.myfitness.helper.ProfileHelper;
import com.lgb.myfitness.helper.ShowValueHelper;
import com.lgb.myfitness.helper.UnitHelper;
import com.lgb.myfitness.view.LineValue;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ActivityFragment extends Fragment{
	
	private String TAG = "ActivityFragment";
	
	private Calendar date_query;
	private Calendar date_today;
	
	private List<HistoryHour> historyList_24hour;
	private HistoryDay history_date;
	private ScreenData screen;
	
	/** The main dataset that includes all the series that go into a chart. */
	private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
	/** The main renderer that includes all the renderers customizing a chart. */
	private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
	private GraphicalView mChartView;
	
	private int profileID = -1;
	private int deviceID = -1;
//	private Band band;
	
	private int goalStep;
	
	private Bitmap bmp;
	private boolean isClickShareItem = false;
	
	private int unit = Global.TYPE_UNIT_METRIC;
	
	private String currentLocale = "en";
	private int progressCheckType = Global.TYPE_CALORIES;
	
	/**
	 * 起始点坐标值
	 */
	private float[] xArray ;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_band_activity, container, false);
		initUI(view);
		
		profileID = ProfileHelper.initProfileID(getActivity());
		currentLocale = getResources().getConfiguration().locale.getLanguage();
		initBand();
		initGoalValue();
		unit = UnitHelper.getUnit(getActivity());
			
		if (getArguments() != null) {
			progressCheckType = getArguments().getInt(Global.KEY_TAB, Global.TYPE_CALORIES);	
		}
		
		date_today = CalendarHelper.getToday();
		date_query = CalendarHelper.getToday();
		CalendarHelper.setDateInformation(date_query, text_date, currentLocale);
		queryDataAndUpdateUI_WB013(date_query, date_today);
		
		return view;
	}
	

	@Override
	public void onResume() {
		super.onResume();
	}

	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
	}
	

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		try {
            mCallback = (OnShareListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnShareListener");
        }
	}
	private OnShareListener mCallback;
	public interface OnShareListener {
		public void onShare(Bitmap bmp, int shareTo);
	}
	
	
	/**
	 * my OnDismissListener
	 */
	private OnDismissListener myOnDismissListener = new OnDismissListener() {

		@Override
		public void onDismiss(DialogInterface dialog) {
			if (!isClickShareItem) {
				if (bmp != null) {
					bmp.recycle();
					decorView.destroyDrawingCache();
					System.gc();
					Log.i(TAG, "recycle success");
				}
			}
		}
	};
	
	/**
	 * my OnClickListener
	 */
	private OnClickListener myOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.button_date_left:
				actionClickDateLeft();
				
				break;
			case R.id.button_date_right:
				if (date_query.before(date_today)) {
					actionClickDateRight();
				}

				break;
			case R.id.image_title_logo:
				actionClickLogo(getActivity());
				
				break;
			case R.id.button_left:
				actionClickTypeLeft();
				
				break;
			case R.id.button_right:
				actionClickTypeRight();
				
				break;
			case R.id.image_share:
				actionClickShare();
				
				break;
			default:
				break;
			}
			
		}
	};
	
	
	/**
	 * my onTouchListener
	 */
	private OnTouchListener myOnTouchListener = new OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				
				break;
			case MotionEvent.ACTION_MOVE:
				int xEvent = (int) event.getX();
				float x = ShowValueHelper.getLineXValue(xEvent, xArray);
				int index = ShowValueHelper.getXValueIndex(xEvent, xArray);
				
				if (progressCheckType == Global.TYPE_CALORIES) {
					String value = Global.df_int_1.format(ShowValueHelper.getBurnValue_hour(historyList_24hour, index));
					String time = ShowValueHelper.getTime_hour(historyList_24hour, index);
					line_value.setDrawData(x, value, time);
					
				} else if (progressCheckType == Global.TYPE_STEP) {
					String value = String.valueOf(ShowValueHelper.getStepValue_hour(historyList_24hour, index));
					String time = ShowValueHelper.getTime_hour(historyList_24hour, index);
					line_value.setDrawData(x, value, time);
					
				} else if (progressCheckType == Global.TYPE_SLEEP) {
					String value = String.valueOf(ShowValueHelper.getSleepValue_hour(getActivity(), historyList_24hour, index));
					String time = ShowValueHelper.getTime_hour(historyList_24hour, index);
					line_value.setDrawData(x, value, time);
					
				}
				break;
			case MotionEvent.ACTION_UP:
				line_value.setDrawData(0, "", "");
				break;	
			}
			return false;
		}
	};
	
	
	/**
	 * 点击类型左
	 */
	private void actionClickTypeLeft() {
		switch (progressCheckType) {
		case Global.TYPE_CALORIES:
			progressCheckType = Global.TYPE_SLEEP;
			setSleepInformation_wb013(historyList_24hour, history_date);
			showChart_wb013(historyList_24hour, Global.TYPE_SLEEP);
			
			break;
		case Global.TYPE_STEP:
			progressCheckType = Global.TYPE_CALORIES;
			setCaloriesInformation_wb013(historyList_24hour, history_date, screen);
			showChart_wb013(historyList_24hour, Global.TYPE_CALORIES);
			
			break;
		case Global.TYPE_SLEEP:
			progressCheckType = Global.TYPE_STEP;
			setStepInformation_wb013(historyList_24hour, history_date, screen);
			showChart_wb013(historyList_24hour, Global.TYPE_STEP);
			
			break;
		default:
			break;
		}
	}
	
	
	/**
	 * 点击类型右
	 */
	private void actionClickTypeRight() {
		switch (progressCheckType) {
		case Global.TYPE_CALORIES:
			progressCheckType = Global.TYPE_STEP;
			setStepInformation_wb013(historyList_24hour, history_date, screen);
			showChart_wb013(historyList_24hour, Global.TYPE_STEP);
			
			break;
		case Global.TYPE_STEP:
			progressCheckType = Global.TYPE_SLEEP;
			setSleepInformation_wb013(historyList_24hour, history_date);
			showChart_wb013(historyList_24hour, Global.TYPE_SLEEP);
			
			break;
		case Global.TYPE_SLEEP:
			progressCheckType = Global.TYPE_CALORIES;
			setCaloriesInformation_wb013(historyList_24hour, history_date, screen);
			showChart_wb013(historyList_24hour, Global.TYPE_CALORIES);
			
			break;
		default:
			break;
		}
	}
		
	
	/** 
     * 点击分享按键
     * 
     */  
	private void actionClickShare() {
		GetandSaveCurrentImage();
		showShareSelectDialog();	
	}
	
	
	/** 
     * 显示分享选择对话框
     * 
     */  
	private void showShareSelectDialog() {
		isClickShareItem = false;
		final String[] items = getResources().getStringArray(R.array.item);
		AlertDialog dd = new AlertDialog.Builder(getActivity())
				.setTitle(R.string.Share_via)
				.setItems(items, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						if (bmp != null) {
							mCallback.onShare(bmp, which);
						}
						isClickShareItem = true;
					}
				}).show();
		dd.setCanceledOnTouchOutside(true);
		dd.setOnDismissListener(myOnDismissListener);

	}
	 
	View decorView = null;
	
	/** 
     * 获取当前屏幕的截图 
     */  
    private void GetandSaveCurrentImage() {  
		// 构建Bitmap
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		int w = dm.widthPixels;
		int h = dm.heightPixels;
		Bitmap Bmp = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		
		// 获取屏幕
		if (decorView != null) {
			decorView.destroyDrawingCache();
		}
		View decorview = getActivity().getWindow().getDecorView();
		decorview.setDrawingCacheEnabled(true);
		Bmp = decorview.getDrawingCache();
		bmp = Bmp;
		decorView = decorview;
    }
	
	
	/**
	 * 点击日期减按钮
	 */
	private void actionClickDateLeft() {
		// 减一天
		date_query = CalendarHelper.minADay(date_query);
		// 设置日期数据
		CalendarHelper.setDateInformation(date_query, text_date, currentLocale);
		
		queryDataAndUpdateUI_WB013(date_query, date_today);
		
	}
	
	
	
	/**
	 * 点击日期加按钮
	 */
	private void actionClickDateRight() {
		// 加一天
		date_query = CalendarHelper.addADay(date_query);
		// 设置日期数据
		CalendarHelper.setDateInformation(date_query, text_date, currentLocale);
		
		queryDataAndUpdateUI_WB013(date_query, date_today);
	}
	
	
	/**
	 * 发送点解了logo的广播
	 * @param activity
	 */
	public static void actionClickLogo(Activity activity) {
		Intent intent = new Intent(Global.ACTION_SHOW_PROGRESS);
		activity.sendBroadcast(intent);
	}
	
	
	public void actionProfileUpdate() {
		currentLocale = getResources().getConfiguration().locale.getLanguage();
		profileID = ProfileHelper.initProfileID(getActivity());
		unit = UnitHelper.getUnit(getActivity());
		
		queryDataAndUpdateUI_WB013(date_query, date_today);
	}
	
	
	public void receiveDataUpdate() {
		initBand();
		
		date_query = CalendarHelper.getToday();
		CalendarHelper.setDateInformation(date_query, text_date, currentLocale);
		
		if (progressCheckType == Global.TYPE_CALORIES) {
			queryDataAndUpdateUI_WB013(date_query, date_today);
			
		} else {
			progressCheckType = Global.TYPE_CALORIES;
			
			queryDataAndUpdateUI_WB013(date_query, date_today);
		}
	}
	
	
	public void clickProgressStep() {
		progressCheckType = Global.TYPE_STEP;
		
		queryDataAndUpdateUI_WB013(date_query, date_today);
	}
	
	
	public void clickProgressCalories() {
		progressCheckType = Global.TYPE_CALORIES;
		
		queryDataAndUpdateUI_WB013(date_query, date_today);
	}
	
	
	public void clickProgressSleep() {
		progressCheckType = Global.TYPE_SLEEP;
		
		queryDataAndUpdateUI_WB013(date_query, date_today);
	}
	

	private void queryDataAndUpdateUI_WB013(Calendar date_query, Calendar date_today) {
		Calendar[] calArray = CalendarHelper.getTodayToTomorrow(date_query);
		// 蓝牙小时数据
		List<HistoryHour> historyList_ble = DatabaseProvider_wb013.queryHistoryHour(getActivity(), profileID, deviceID, calArray[0], calArray[1]);
//		for (HistoryHour historyHour : historyList_ble) {
//			System.out.println(historyHour.getDate().getTime() + ", " + historyHour.getStep());
//		}
		// 音频口日数据
		history_date = DatabaseProvider_wb013.queryHistoryDate(getActivity(), profileID, date_query);
//		System.out.println(history_date.getDate().getTime() + ", " + history_date.getActiveHour());
		// 屏幕数据
		if (CalendarHelper.isSameDate(date_query, date_today)) {
			screen = DatabaseProvider_wb013.queryScreen(getActivity(), date_query, profileID, deviceID);
		} else {
			screen = null;
		}
		// 补全24小时的蓝牙小时数据
		Map<Calendar, HistoryHour> map = CalendarHelper.get24HourMap_wb013(date_query);
		historyList_24hour = CalculateHelper.handleToComplete24Hour(map, historyList_ble);
		
		
		if (progressCheckType == Global.TYPE_CALORIES) {
			setCaloriesInformation_wb013(historyList_24hour, history_date, screen);
			showChart_wb013(historyList_24hour, Global.TYPE_CALORIES);
			
		} else if (progressCheckType == Global.TYPE_STEP){
			setStepInformation_wb013(historyList_24hour, history_date, screen);
			showChart_wb013(historyList_24hour, Global.TYPE_STEP);
			
		} else if (progressCheckType == Global.TYPE_SLEEP) {
			setSleepInformation_wb013(historyList_24hour, history_date);
			showChart_wb013(historyList_24hour, Global.TYPE_SLEEP);
		}
	}
	

	
	/**
	 * 设置calories界面数据
	 * @param burn
	 * @param distance
	 * @param step
	 */
	private void setCaloriesInformation_wb013(List<HistoryHour> history_hour_list, HistoryDay history_date, ScreenData screen) {
		
		double burn = 0;
		int step = 0;
		double distance = 0;
		double goalPercent = 0;
		
		if (screen != null) {
			burn = screen.getBurn();
			step = screen.getStep();
			distance = screen.getDistance();
			goalPercent = screen.getGoal();
			
		} else {
			// 用蓝牙数据统计
			if (history_hour_list != null && history_hour_list.size() > 0) {
				for (HistoryHour historyHour : history_hour_list) {
					if (historyHour != null) {
						burn += historyHour.getBurn();
						step += historyHour.getStep();
					}
				}
			}
			// 用音频口数据比较，显示步数大的数据
			if (history_date != null) {
				int dayStep = history_date.getStep();
				if (dayStep >= step) {
					step = dayStep;
					burn = history_date.getBurn();	
				}
			}
			
			goalPercent = CalculateHelper.calculateGoalPercent(step, goalStep);
			distance = CalculateHelper.calculateDistance(getActivity(), step);		
		}
		
		showCaloriesMeasureText();
		setCaloriesValues(goalPercent, burn, distance, step);
	}

	
	/**
	 * 设置step界面数据
	 * @param step
	 * @param active
	 * @param inActive
	 * @param average
	 */
	private void setStepInformation_wb013(List<HistoryHour> history_hour_list, HistoryDay history_date, ScreenData screen) {
		
		int active = 0;
		int step = 0;
		double goalPercent = 0;
		
		// 用蓝牙数据统计
		if (history_hour_list != null && history_hour_list.size() > 0) {
			for (HistoryHour historyHour : history_hour_list) {
				if (historyHour != null) {
					int stepHour = historyHour.getStep();
					if (stepHour > 0) {
						active ++;
						step += historyHour.getStep();
					}
				}
			}
		}
		
		// 用音频口数据比较，显示步数大的数据
		if (history_date != null) {
			int dayStep = history_date.getStep();
			if (dayStep >= step) {
				step = dayStep;
				active = history_date.getActiveHour();
			}
		}
		
		if (screen != null) {
			step = screen.getStep();
			goalPercent = screen.getGoal();
			
		} else {
			goalPercent = CalculateHelper.calculateGoalPercent(step, goalStep);	
		}
				
		
		showStepsMeasureText();
		setStepsValues(step, active, goalPercent);
	}

	
	/**
	 * 设置Sleep界面数据
	 * @param deep
	 * @param light
	 * @param awake
	 */
	private void setSleepInformation_wb013(List<HistoryHour> history_hour_list, HistoryDay history_date) {
		
		int deep = 0;
		int light = 0;
		int step = 0;
		if (history_hour_list != null && history_hour_list.size() > 0) {
			for (HistoryHour historyHour : history_hour_list) {
				if (historyHour != null) {
					step += historyHour.getStep();
					
					int sleepMove = historyHour.getSleepMove();
					int sleepPattern = CalculateHelper.getSleepPattern_accordingSleepMove(sleepMove);
					if (sleepPattern == Global.TYPE_SLEEP_DEEP) {
						deep ++;
					} else if (sleepPattern == Global.TYPE_SLEEP_LIGHT) {
						light ++;
					}
				}
			}
		} 
		if (history_date != null) {
			int dayStep = history_date.getStep();
			if (dayStep >= step) {
				deep = history_date.getDeepSleepHour();
				light = history_date.getLightSleepHour();	
			}
		}
		
		showSleepMeasureText();
		setSleepValues(deep, light);
		// 进度条
//		ProgressHelper.setProgress(deep + light, goalSleep, progress_goal);
	}

	
	/**
	 * 设置Calories页面显示的单位
	 */
	private void showCaloriesMeasureText() {
		// 大圆
		text_big_circle_label.setText(getString(R.string.GOAL));
		text_big_circle_label.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.text_size_activity_calories_goal));
		// 小圆 上
		text_small_circle_1_lable.setText(getString(R.string.Burned));
		text_small_circle_1_lable.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.text_size_activity_calories_burned));
		text_small_circle_1_measure.setText(getString(R.string.kcals));
		text_small_circle_1_measure.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.text_size_activity_calories_kcals));
		// 小圆 中
		text_small_circle_2_lable.setText(getString(R.string.Distance));
		text_small_circle_2_lable.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.text_size_activity_calories_distance));
		if (unit == Global.TYPE_UNIT_METRIC) {
			text_small_circle_2_measure.setText(getString(R.string.km));
		} else {
			text_small_circle_2_measure.setText(getString(R.string.mile));
		}
		text_small_circle_2_measure.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.text_size_activity_calories_km));
		// 小圆 下
		text_small_circle_3_lable.setText(getString(R.string.Steps));
		text_small_circle_3_lable.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.text_size_activity_calories_Steps));
		text_small_circle_3_measure.setText(getString(R.string.steps));
		text_small_circle_3_measure.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.text_size_activity_calories_steps));		
	
		image_activity.setBackgroundResource(R.drawable.image_activity_calor);
		activity_text.setText(R.string.CALORIES);
	}
	
	
	/**
	 * 设置Calories页面显示的数据值
	 * @param goalPercent
	 * @param burn
	 * @param distance
	 * @param step
	 */
	private void setCaloriesValues(double goalPercent, double burn, double distance, int step) {
		
		String goalPercentStr = FormatHelper.goalFormat(goalPercent);
		String distanceStr = "0.00";
		if (unit == Global.TYPE_UNIT_METRIC) {
			distanceStr = FormatHelper.distanceFormat(distance);
			
		} else {
			distance = CalculateHelper.kmToMile(distance);
			distanceStr = FormatHelper.distanceFormat(distance);
		
		}
		text_small_circle_1_value.setText(String.valueOf(burn));
		
		text_big_circle_value.setText(goalPercentStr + "%");
		text_big_circle_value.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.value_size_activity_calories_goal));
		// 小圆 上
		text_small_circle_1_value.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.value_size_activity_calories_burned));
		// 小圆 中
		text_small_circle_2_value.setText(distanceStr);
		text_small_circle_2_value.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.value_size_activity_calories_distance));
		//　小圆 下
		text_small_circle_3_value.setText(String.valueOf(step));
		text_small_circle_3_value.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.value_size_activity_calories_steps));
	}
	
	
	/**
	 * 设置STEPS页面显示的单位
	 */
	private void showStepsMeasureText() {
		// 大圆
		text_big_circle_label.setText(getString(R.string.STEPS));
		text_big_circle_label.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.text_size_activity_steps_steps));
		// 小圆 上
		text_small_circle_1_lable.setText(getString(R.string.Active));
		text_small_circle_1_lable.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.text_size_activity_steps_active));
		text_small_circle_1_measure.setText(getString(R.string.hours));
		text_small_circle_1_measure.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.text_size_activity_steps_hours));
		// 小圆 中
		text_small_circle_2_lable.setText(getString(R.string.In_active));
		text_small_circle_2_lable.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.text_size_activity_steps_inactive));
		text_small_circle_2_measure.setText(getString(R.string.hours));
		text_small_circle_2_measure.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.text_size_activity_steps_hours));
		// 小圆 下
		text_small_circle_3_lable.setText(getString(R.string.percent_of_Average));
		text_small_circle_3_lable.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.text_size_activity_steps_average));
		text_small_circle_3_measure.setText("");	
		
		image_activity.setBackgroundResource(R.drawable.image_activity_step);
		activity_text.setText(R.string.STEPS);
	}
	
	
	/**
	 * 设置STEPS页面显示的数据值
	 * @param step
	 * @param active
	 * @param goalPercent
	 */
	private void setStepsValues(int step, int active, double goalPercent) {
		
		String goalPercentStr = FormatHelper.goalFormat(goalPercent);
		
		// 大圆
		text_big_circle_value.setText(String.valueOf(step));
		text_big_circle_value.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.value_size_activity_steps_steps));
		// 小圆 上
		text_small_circle_1_value.setText(Global.df_double_1.format(active));
		text_small_circle_1_value.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.value_size_activity_steps_active));
		// 小圆 中
		text_small_circle_2_value.setText(Global.df_double_1.format(24 - active));
		text_small_circle_2_value.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.value_size_activity_steps_inactive));
		//　小圆 下
		text_small_circle_3_value.setText(goalPercentStr);	
		text_small_circle_3_value.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.value_size_activity_steps_average));		
	}
	
	
	/**
	 * 设置SLEEP页面显示的单位
	 */
	private void showSleepMeasureText() {
		// 大圆
		text_big_circle_label.setText(getString(R.string.SLEEP));
		text_big_circle_label.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.text_size_activity_sleep_sleep));
		// 小圆 上
		text_small_circle_1_lable.setText(getString(R.string.Deep));
		text_small_circle_1_lable.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.text_size_activity_sleep_deep));
		text_small_circle_1_measure.setText(getString(R.string.hours));
		text_small_circle_1_measure.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.text_size_activity_sleep_hours));
		// 小圆 中
		text_small_circle_2_lable.setText(getString(R.string.Light));
		text_small_circle_2_lable.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.text_size_activity_sleep_light));
		text_small_circle_2_measure.setText(getString(R.string.hours));
		text_small_circle_2_measure.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.text_size_activity_sleep_hours));
		// 小圆 下
		text_small_circle_3_lable.setText(getString(R.string.Awake));
		text_small_circle_3_lable.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.text_size_activity_sleep_awake));
		text_small_circle_3_measure.setText(getString(R.string.hours));
		text_small_circle_3_measure.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.text_size_activity_sleep_hours));		
	
		image_activity.setBackgroundResource(R.drawable.image_activity_sleep);
		activity_text.setText(R.string.SLEEP);
	}
	
	
	/**
	 * 设置SLEEP页面显示的数据值
	 * @param deep
	 * @param light
	 */
	private void setSleepValues(int deep, int light) {
		// 大圆
		text_big_circle_value.setText(String.valueOf(deep + light) + " " + getString(R.string.H));
		text_big_circle_value.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.value_size_activity_sleep_sleep));
		// 小圆 上
		text_small_circle_1_value.setText(Global.df_double_1.format(deep));
		text_small_circle_1_value.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.value_size_activity_sleep_deep));
		// 小圆 中
		text_small_circle_2_value.setText(Global.df_double_1.format(light));
		text_small_circle_2_value.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.value_size_activity_sleep_light));
		//　小圆 下
		text_small_circle_3_value.setText(Global.df_double_1.format(24 - (deep + light)));
		text_small_circle_3_value.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.value_size_activity_sleep_awake));		
	}
	
	
	/**
	 * 绘图
	 * @param historyHourList
	 * @param type
	 */
	private void showChart_wb013(List<HistoryHour> historyHourList, int type) {
		layout_chart.removeAllViews();
		
		mDataset = ChartHelper.setDataset_chart_activity(getActivity(), historyHourList, type);
		mRenderer = ChartHelper.setRenderer_chart_activity(getActivity(), historyHourList, face, 24, type);
		
		mChartView = ChartFactory.getCubeLineChartView(getActivity(), mDataset, mRenderer, ChartHelper.SMOTHNESS);
		mChartView.setOnTouchListener(myOnTouchListener);
		
		xArray = ShowValueHelper.calculateXArray(getActivity(), 24);

		layout_chart.addView(mChartView, new LayoutParams(LayoutParams.MATCH_PARENT,
		          LayoutParams.MATCH_PARENT));
	}
	
	
	private void initBand() {
		Band band = BindHelper.getLastSyncDevice(getActivity());
		if (band != null) {
			
			deviceID = band.getDeviceID();
			Log.i(TAG, "init band : " + deviceID);
		} else {
			deviceID = 1;
		}
	}
	
	
	/**
	 * 获取目标值
	 */
	private void initGoalValue() {
		goalStep = Global.DEFAULT_GOAL_STEP;
		
		Goal goal = DatabaseProvider_public.queryGoal(getActivity(), profileID);
		if (goal != null) {
			goalStep = goal.getStep();
		}
	}
	
	
	/**
	 * 初始化UI
	 * @param view
	 */
	private void initUI(View view) {
		face = Typeface.createFromAsset (getActivity().getAssets() , "fonts/EurostileExtended-Roman-DTC.ttf");
		
		TextView button_date_left = (TextView) view.findViewById(R.id.button_date_left);
		button_date_left.setOnClickListener(myOnClickListener);
		TextView button_date_right = (TextView) view.findViewById(R.id.button_date_right);
		button_date_right.setOnClickListener(myOnClickListener);
		ImageView image_logo = (ImageView) view.findViewById(R.id.image_title_logo);
		image_logo.setOnClickListener(myOnClickListener);
		
		image_activity = (ImageView) view.findViewById(R.id.image_activity);
		image_activity.setBackgroundResource(R.drawable.image_activity_calor);
		activity_text = (TextView) view.findViewById(R.id.activity_text);
		activity_text.setText(R.string.CALORIES);
		
		button_left = (ImageView) view.findViewById(R.id.button_left);
		button_left.setOnClickListener(myOnClickListener);
		button_right = (ImageView) view.findViewById(R.id.button_right);
		button_right.setOnClickListener(myOnClickListener);
		image_share = (ImageView) view.findViewById(R.id.image_share);
		image_share.setOnClickListener(myOnClickListener);
		text_date = (TextView) view.findViewById(R.id.text_date);
		text_date.setOnClickListener(myOnClickListener);
		
		text_big_circle_label = (TextView) view.findViewById(R.id.text_big_circle_label);
		text_big_circle_value = (TextView) view.findViewById(R.id.text_big_circle_value);
		
		text_small_circle_1_lable = (TextView) view.findViewById(R.id.text_small_circle_1_lable);
		text_small_circle_1_value = (TextView) view.findViewById(R.id.text_small_circle_1_value);
		text_small_circle_1_measure = (TextView) view.findViewById(R.id.text_small_circle_1_measure);
		
		text_small_circle_2_lable = (TextView) view.findViewById(R.id.text_small_circle_2_lable);
		text_small_circle_2_value = (TextView) view.findViewById(R.id.text_small_circle_2_value);
		text_small_circle_2_measure = (TextView) view.findViewById(R.id.text_small_circle_2_measure);
		
		text_small_circle_3_lable = (TextView) view.findViewById(R.id.text_small_circle_3_lable);
		text_small_circle_3_value = (TextView) view.findViewById(R.id.text_small_circle_3_value);
		text_small_circle_3_measure = (TextView) view.findViewById(R.id.text_small_circle_3_measure);
		
		line_value = (LineValue) view.findViewById(R.id.linevalue);
		layout_chart = (RelativeLayout) view.findViewById(R.id.layout_chart);
		
		int densityDPI = ChartHelper.getDensityDpi(getActivity());
		ChartHelper.setTextSize(densityDPI);
	}
	private Typeface face;
	private TextView text_date;
	private TextView text_big_circle_label, text_big_circle_value, activity_text;
	private TextView text_small_circle_1_lable, text_small_circle_1_value, text_small_circle_1_measure;
	private TextView text_small_circle_2_lable, text_small_circle_2_value, text_small_circle_2_measure;
	private TextView text_small_circle_3_lable, text_small_circle_3_value, text_small_circle_3_measure;
	private RelativeLayout layout_chart;
	private LineValue line_value;
	private ImageView button_left, button_right, image_activity, image_share;
}
