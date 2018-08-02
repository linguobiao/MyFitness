package com.lgb.myfitness.wristband.main;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.lgb.myfitness.R;
import com.lgb.myfitness.been.Band;
import com.lgb.myfitness.been.Goal;
import com.lgb.myfitness.been.HistoryDay;
import com.lgb.myfitness.been.HistoryHour;
import com.lgb.myfitness.been.HistoryMonth;
import com.lgb.myfitness.been.ScreenData;
import com.lgb.myfitness.database.DatabaseProvider_public;
import com.lgb.myfitness.database.DatabaseProvider_wb013;
import com.lgb.myfitness.global.Global;
import com.lgb.myfitness.global.Global.ProgressType;
import com.lgb.myfitness.helper.BindHelper;
import com.lgb.myfitness.helper.CalculateHelper;
import com.lgb.myfitness.helper.CalendarHelper;
import com.lgb.myfitness.helper.ChartHelper;
import com.lgb.myfitness.helper.ProfileHelper;
import com.lgb.myfitness.helper.ProgressHelper;
import com.lgb.myfitness.view.GoalProgressbar;

public class ProgressFragment extends Fragment{
	
//	private String TAG = "ProgressFragment";
	
	/** The main dataset that includes all the series that go into a chart. */
	private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
	/** The main renderer that includes all the renderers customizing a chart. */
	private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
	private GraphicalView mChartView;
	
	
	private Calendar date_query;
	private List<HistoryHour> historyList_24hour = new ArrayList<HistoryHour>();
	private List<HistoryDay> historyList_7day = new ArrayList<HistoryDay>();
	private List<HistoryDay> historyList_30day = new ArrayList<HistoryDay>();
	private List<HistoryMonth> historyList_12month = new ArrayList<HistoryMonth>();
	
	private int profileID = -1;
	private int deviceID = -1;
	
	private int goalStep;
//	private double goalBurn;
	private double goalSleep;
	
	private ProgressType progress_type = ProgressType.Daily;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_band_progress, container, false);
		initUI(view);
		
		profileID = ProfileHelper.initProfileID(getActivity());
		initBand();
		initGoal();
		
		date_query = CalendarHelper.getToday();
		progress_type = ProgressType.Daily;
		radio_awake.setChecked(true);
		refreshUI();
		
		return view;
	}
	

	@Override
	public void onStart() {
		super.onStart();
	}


	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	
	private ProgressCheckListener mCallback;
	public interface ProgressCheckListener {
		public void onProgressCheckSteps();
		public void onProgressCheckBurn();
		public void onProgressCheckSleep();
	}
	
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		try {
            mCallback = (ProgressCheckListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ProgressCheckListener");
        }
	}
	
	
	private void refreshUI() {
		
		if (radio_awake.isChecked()) {
			if (progress_type.equals(ProgressType.Daily)) {
				setDailyReportInformation(date_query);
				showAwakeDayChart();
			} else if (progress_type.equals(ProgressType.Weekly)) {
				setWeeklyReportInformation(date_query);
				showAwakeWeekChart();
			} else if (progress_type.equals(ProgressType.Monthly)) {
				setMonthlyReportInformation(date_query);
				showAwakeMonthChart();
			} else if (progress_type.equals(ProgressType.Yearly)) {
				setYearlyReportInformation(date_query);
				showAwakeYearChart();
			}
			
		} else if (radio_asleep.isChecked()) {
			if (progress_type.equals(ProgressType.Daily)) {
				setDailyReportInformation(date_query);
				showAsleepDayChart();
			} else if (progress_type.equals(ProgressType.Weekly)) {
				setWeeklyReportInformation(date_query);
				showAsleepWeekChart();
			} else if (progress_type.equals(ProgressType.Monthly)) {
				setMonthlyReportInformation(date_query);				
				showAsleepMonthChart();
			} else if (progress_type.equals(ProgressType.Yearly)) {
				setYearlyReportInformation(date_query);				
				showAsleepYearChart();
			}
		}
	}
	
	
	/**
	 * my OnClickListener
	 */
	private OnClickListener myOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.image_circle_burn:
				actionClickCircleBurn();
				break;
			case R.id.image_circle_step:
				actionClickCircleStep();
				break;
			case R.id.image_circle_sleep:
				actionClickCircleSleep();
				break;
			case R.id.button_left:
				actionClickTypeLeft();
				break;
			case R.id.button_right:
				actionClickTypeRight();
				break;
			default:
				break;
			}
		}
	};

	
	private void actionClickTypeLeft() {
		switch (progress_type) {
		case Daily:
			progress_type = ProgressType.Yearly;

			setYearlyReportInformation(date_query);
			if (radio_awake.isChecked()) {
				showAwakeYearChart();
			} else if (radio_asleep.isChecked()){
				showAsleepYearChart();
			}
			
			break;
		case Weekly:
			progress_type = ProgressType.Daily;

			setDailyReportInformation(date_query);
			if (radio_awake.isChecked()) {
				showAwakeDayChart();
			} else if (radio_asleep.isChecked()){
				showAsleepDayChart();
			}
			
			break;
		case Monthly:
			progress_type = ProgressType.Weekly;

			setWeeklyReportInformation(date_query);
			if (radio_awake.isChecked()) {
				showAwakeWeekChart();
			} else if (radio_asleep.isChecked()){
				showAsleepWeekChart();
			}
			
			break;
		case Yearly:
			progress_type = ProgressType.Monthly;

			setMonthlyReportInformation(date_query);
			if (radio_awake.isChecked()) {
				showAwakeMonthChart();
			} else if (radio_asleep.isChecked()){
				showAsleepMonthChart();
			}
			
			break;
		default:
			break;
		}
	}
	
	
	private void actionClickTypeRight() {
		switch (progress_type) {
		case Daily:
			progress_type = ProgressType.Weekly;

			setWeeklyReportInformation(date_query);
			if (radio_awake.isChecked()) {
				showAwakeWeekChart();
			} else if (radio_asleep.isChecked()){
				showAsleepWeekChart();
			}
			
			break;
		case Weekly:
			progress_type = ProgressType.Monthly;
			
			setMonthlyReportInformation(date_query);
			if (radio_awake.isChecked()) {
				showAwakeMonthChart();
			} else if (radio_asleep.isChecked()){
				showAsleepMonthChart();
			}
			
			break;
		case Monthly:
			progress_type = ProgressType.Yearly;

			setYearlyReportInformation(date_query);
			if (radio_awake.isChecked()) {
				showAwakeYearChart();
			} else if (radio_asleep.isChecked()){
				showAsleepYearChart();
			}
			
			break;
		case Yearly:
			progress_type = ProgressType.Daily;

			setDailyReportInformation(date_query);
			if (radio_awake.isChecked()) {
				showAwakeDayChart();
			} else if (radio_asleep.isChecked()){
				showAsleepDayChart();
			}
			
			break;
		default:
			break;
		}
	}
	
	
	
	/**
	 * my OnCheckedChangeListener
	 */
	private OnCheckedChangeListener myOnCheckedChangeListener = new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			switch (buttonView.getId()) {
			case R.id.radio_awake:
				if (isChecked) {
					if (progress_type.equals(ProgressType.Daily)) {
						showAwakeDayChart();
					} else if (progress_type.equals(ProgressType.Weekly)) {
						showAwakeWeekChart();
					} else if (progress_type.equals(ProgressType.Monthly)) {
						showAwakeMonthChart();
					} else if (progress_type.equals(ProgressType.Yearly)) {
						showAwakeYearChart();
					}
				}
				break;
			case R.id.radio_asleep:
				if (isChecked) {
					if (progress_type.equals(ProgressType.Daily)) {
						showAsleepDayChart();
					} else if (progress_type.equals(ProgressType.Weekly)) {
						showAsleepWeekChart();
					} else if (progress_type.equals(ProgressType.Monthly)) {
						showAsleepMonthChart();
					} else if (progress_type.equals(ProgressType.Yearly)) {
						showAsleepYearChart();
					}
				}
				break;
			default:
				break;
			}
			
		}
	};
	
	
	
	private void actionClickCircleStep() {
		mCallback.onProgressCheckSteps();
	}
	
	
	private void actionClickCircleSleep() {
		mCallback.onProgressCheckSleep();
	}
	
	
	private void actionClickCircleBurn() {
		mCallback.onProgressCheckBurn();
	}
	
	
    public void actionGoalUpdate() {
    	initGoal();
    	
    	refreshUI();
    }

    public void actionProfileUpdate() {
    	profileID = ProfileHelper.initProfileID(getActivity());
    	initBand();
		
		refreshUI();
    }
    
    
    public void receiveDataUpdate() {
    	initBand();
		
    	date_query = CalendarHelper.getToday();
    	
    	if (radio_awake.isChecked() && progress_type.equals(ProgressType.Daily)) {
    		refreshUI();
    		
    	} else if (!radio_awake.isChecked() && !progress_type.equals(ProgressType.Daily)){
    		radio_awake.setChecked(true);
    		progress_type = ProgressType.Daily;
    		
    		refreshUI();
    		
    	} else if (radio_awake.isChecked() && !progress_type.equals(ProgressType.Daily)) {
    		progress_type = ProgressType.Daily;
    		
    		refreshUI();
    		
    	} else if (!radio_awake.isChecked() && progress_type.equals(ProgressType.Daily)) {
    		radio_awake.setChecked(true);
    		
    		refreshUI();
    	}
    }

	
    /**
	 * 设置daily数据
	 */
	private void setDailyReportInformation(Calendar date_query) {
		
		if (date_query != null) {
			Calendar[] calArray = CalendarHelper.getTodayToTomorrow(date_query);
			List<HistoryHour> historyList_hour = DatabaseProvider_wb013.queryHistoryHour(getActivity(), profileID, deviceID, calArray[0], calArray[1]);
		
			Map<Calendar, HistoryHour> hour24Map = CalendarHelper.get24HourMap_wb013(date_query);
			historyList_24hour = CalculateHelper.handleToComplete24Hour(hour24Map, historyList_hour);
			
			ScreenData screen = DatabaseProvider_wb013.queryScreen(getActivity(), date_query, profileID, deviceID);
		
			HistoryDay history_day = DatabaseProvider_wb013.queryHistoryDate(getActivity(), profileID, date_query);
		
			setDailyTextValue_wb013(historyList_hour, history_day, screen);
			setDialyText();
		}
	
	}
	
	/**
	 * 设置weekly数据
	 */
	private void setWeeklyReportInformation(Calendar date_query) {
		if (date_query != null) {
			Calendar[] calArray = CalendarHelper.getLastWeekToTomorrow(date_query);
			List<HistoryHour> historyList_hour = DatabaseProvider_wb013.queryHistoryHour(getActivity(), profileID, deviceID, calArray[0], calArray[1]);
			List<HistoryDay> historyList_audio = DatabaseProvider_wb013.queryHistoryDate(getActivity(), profileID, calArray[0], calArray[1]);
			
			Map<Calendar, HistoryDay> day7Map = CalendarHelper.getDayMap_wb013(calArray);
			historyList_7day = CalculateHelper.handleToCompleteWeekOrMonthData(day7Map, historyList_hour);
			CalculateHelper.calculateSleepQuality(historyList_7day);
			
			CalculateHelper.replaceByAudioData(historyList_7day, historyList_audio);
			
			setWeeklyTextValue_wb013(historyList_7day);
			setWeeklyText();
		}
	}
	
	
	/**
	 * 设置monthly数据
	 */
	private void setMonthlyReportInformation(Calendar date_query) {
		
		if (date_query != null) {
			Calendar[] calArray = CalendarHelper.getLastMonthToTomorrow(date_query);
			List<HistoryHour> historyList_hour = DatabaseProvider_wb013.queryHistoryHour(getActivity(), profileID, deviceID, calArray[0], calArray[1]);
			List<HistoryDay> historyList_audio = DatabaseProvider_wb013.queryHistoryDate(getActivity(), profileID, calArray[0], calArray[1]);
			
			Map<Calendar, HistoryDay> day30Map = CalendarHelper.getDayMap_wb013(calArray);
			historyList_30day = CalculateHelper.handleToCompleteWeekOrMonthData(day30Map, historyList_hour);
			CalculateHelper.calculateSleepQuality(historyList_30day);
			
			CalculateHelper.replaceByAudioData(historyList_30day, historyList_audio);
			
			setMonthlyTextValue_wb013(historyList_30day);
			setMonthlyText();
		}
	}
	
	/**
	 * 设置yearly数据
	 */
	private void setYearlyReportInformation(Calendar date_query) {
		
		if (date_query != null) {
			Calendar[] calArray = CalendarHelper.getLastYearToTomorrow(date_query);
			
			// 小时数据
			List<HistoryHour> historyList_hour = DatabaseProvider_wb013.queryHistoryHour(getActivity(), profileID, deviceID, calArray[0], calArray[1]);
		
			List<HistoryDay> historyList_audio = DatabaseProvider_wb013.queryHistoryDate(getActivity(), profileID, calArray[0], calArray[1]);
			
			// 日数据
			List<HistoryDay> historyList_day = CalculateHelper.handleToDay(historyList_hour);
			CalculateHelper.calculateSleepQuality(historyList_day);
			
			CalculateHelper.replaceByAudioData_year(historyList_day, historyList_audio);
//			for (HistoryDay historyDay : historyList_day) {
//				System.out.println(historyDay.getDate().getTime() + ", " + historyDay.getStep() + ", " + historyDay.getSleepQuality());
//			}
			
			// 月数据
			Map<Calendar, HistoryMonth> month12Map = CalendarHelper.get12MonthMap(date_query);
			historyList_12month = CalculateHelper.handleToComplete12Month(month12Map, historyList_day);
		
			setYearlyTextValue_wb013(historyList_12month);
			setYearlyText();
		}
	}
	
	
	private void setDailyTextValue_wb013(List<HistoryHour> historyList_hour, HistoryDay history_day, ScreenData screen) {

		int step = 0;
		int sleep = 0;
		double burn = 0;
		double goalPercent = 0;
		
		for (HistoryHour historyHour : historyList_hour) {
			if (historyHour != null) {
				step += historyHour.getStep();
				burn += historyHour.getBurn();
				
				int sleepPattern = ChartHelper.getSleepPattern(historyHour.getSleepMove());
				if (sleepPattern == Global.TYPE_SLEEP_DEEP || sleepPattern == Global.TYPE_SLEEP_LIGHT) {
					sleep ++;
				}
			}
		}
		
		if (history_day != null) {
			int dayStep = history_day.getStep();
			if (dayStep > step) {
				step = dayStep;
				sleep = history_day.getDeepSleepHour() + history_day.getLightSleepHour();
				burn = history_day.getBurn();
			}
		}
		
		if (screen != null) {
			step = screen.getStep();
			burn = screen.getBurn();
			goalPercent = screen.getGoal();
			
		} else {
			goalPercent = CalculateHelper.calculateGoalPercent(step, goalStep);
		}
		

		text_value_step.setText(String.valueOf(step));
		text_value_sleep.setText(Global.df_double_1.format(sleep) + " " + getString(R.string.H));
		text_value_burn.setText(Global.df_double_1.format(burn));
		
		ProgressHelper.setProgress(goalPercent, progressbar_step);
		ProgressHelper.setProgress(sleep, goalSleep, progressbar_sleep);
	}
			
	
	private void setWeeklyTextValue_wb013(List<HistoryDay> historyList_day) {
		
		int step = 0;
		int sleep = 0;
		int burn = 0;
		
		for (HistoryDay history : historyList_day) {
			if (history != null) {
				step += history.getStep();
				burn += history.getBurn();
				sleep += (history.getDeepSleepHour() + history.getLightSleepHour());
			}
		}
		
		int size = historyList_day.size();
		if (size != 0) {
			step = step / size;
			sleep = sleep / size;
			burn = burn / size;
		}
		
		
		text_value_step.setText(String.valueOf(step));
		text_value_sleep.setText(Global.df_double_1.format(sleep) + " "  + getString(R.string.H));
		text_value_burn.setText(Global.df_double_1.format(burn));
		
		ProgressHelper.setProgress(step, goalStep, progressbar_step);
		ProgressHelper.setProgress(sleep, goalSleep, progressbar_sleep);
	}
	
	
	private void setMonthlyTextValue_wb013(List<HistoryDay> historyList_day) {

		int step = 0;
		int sleep = 0;
		int burn = 0;
		
		for (HistoryDay history : historyList_day) {
			if (history != null) {
				step += history.getStep();
				burn += history.getBurn();
				sleep += (history.getDeepSleepHour() + history.getLightSleepHour());
			}
		}
		
		int size = historyList_day.size();
		if (size != 0) {
			step = step / size;
			sleep = sleep / size;
			burn = burn / size;
		}
		
		
		text_value_step.setText(String.valueOf(step));
		text_value_sleep.setText(Global.df_double_1.format(sleep) + " " + getString(R.string.H));
		text_value_burn.setText(Global.df_double_1.format(burn));
		
		ProgressHelper.setProgress(step, goalStep, progressbar_step);
		ProgressHelper.setProgress(sleep, goalSleep, progressbar_sleep);
	}
	
	
	private void setYearlyTextValue_wb013(List<HistoryMonth> historyList_yearly) {

		// 日平均数据
		int step = 0;
		int sleep = 0;
		int burn = 0;
		for (HistoryMonth history : historyList_yearly) {
			if (history != null) {
				step += history.getStep();
				burn += history.getBurn();
				sleep += (history.getTotalDeepSleep() + history.getTotalLightSleep());
			}
		}

		int size = 365;
		if (size != 0) {
			step = step / size;
			sleep = sleep / size;
			burn = burn / size;
		}
		
		
		text_value_step.setText(String.valueOf(step));
		text_value_sleep.setText(Global.df_double_1.format(sleep) + " " + getString(R.string.H));
		text_value_burn.setText(Global.df_double_1.format(burn));
		
		ProgressHelper.setProgress(step, goalStep, progressbar_step);
		ProgressHelper.setProgress(sleep, goalSleep, progressbar_sleep);
	}
	

	private void setDialyText() {
		text_progress.setText(R.string.DAY);
		text_title.setText(getString(R.string.DAILY_REPORT));
		
		text_label_cal.setText(getString(R.string.KCALS));
	}
	
	
	private void setWeeklyText() {
		text_progress.setText(R.string.WEEK);
		text_title.setText(getString(R.string.WEEKLY_REPORT));
		
		text_label_cal.setText(getString(R.string.KCALS));
	}
	
	
	private void setMonthlyText() {
		text_progress.setText(R.string.MONTH);
		text_title.setText(getString(R.string.MONTHLY_REPORT));
		
		text_label_cal.setText(getString(R.string.KCALS));
	}
	
	
	private void setYearlyText() {
		text_progress.setText(R.string.YEAR);
		text_title.setText(getString(R.string.YEARLY_REPORT));
		
		text_label_cal.setText(getString(R.string.KCALS));
	}

	
	private void showAwakeDayChart() {
		layout_chart.removeAllViews();
		
		mDataset = ChartHelper.setDataset_chart_progress_day_awake(getActivity(), historyList_24hour);
		mRenderer = ChartHelper.setRenderer_chart_progress_day_awake(getActivity(), face, 24);
		
		mChartView = ChartFactory.getCubeLineChartView(getActivity(), mDataset, mRenderer, ChartHelper.SMOTHNESS);
		
		layout_chart.addView(mChartView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}
	
	
	private void showAwakeWeekChart() {
		layout_chart.removeAllViews();

		mDataset = ChartHelper.setDataset_chart_progress_week_awake(getActivity(), historyList_7day);
		mRenderer = ChartHelper.setRenderer_chart_progress_week_awake(getActivity(), face, 7, date_query);
		
		mChartView = ChartFactory.getCubeLineChartView(getActivity(), mDataset, mRenderer, ChartHelper.SMOTHNESS);
		
		layout_chart.addView(mChartView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}
	
	
	private void showAwakeMonthChart() {
		
		layout_chart.removeAllViews();
		
		mDataset = ChartHelper.setDataset_chart_progress_week_awake(getActivity(), historyList_30day);
		mRenderer = ChartHelper.setRenderer_chart_progress_month_awake(getActivity(), face, 31, date_query);
		
		mChartView = ChartFactory.getCubeLineChartView(getActivity(), mDataset, mRenderer, ChartHelper.SMOTHNESS);
		
		layout_chart.addView(mChartView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}
	
	
	private void showAwakeYearChart() {

		layout_chart.removeAllViews();
		
		mDataset = ChartHelper.setDataset_chart_progress_year_awake(getActivity(), historyList_12month);
		mRenderer = ChartHelper.setRenderer_chart_progress_year_awake(getActivity(), face, 12, date_query);
		
		mChartView = ChartFactory.getCubeLineChartView(getActivity(), mDataset, mRenderer, ChartHelper.SMOTHNESS);
		
		layout_chart.addView(mChartView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}
	

	private void showAsleepDayChart() {

		layout_chart.removeAllViews();
		
		mDataset = ChartHelper.setDataset_chart_progress_day_asleep(getActivity(), historyList_24hour);
		mRenderer = ChartHelper.setRenderer_chart_progress_day_asleep(getActivity(), face, 24);
		
		mChartView = ChartFactory.getCubeLineChartView(getActivity(), mDataset, mRenderer, ChartHelper.SMOTHNESS);
		
		layout_chart.addView(mChartView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}
	
	
	private void showAsleepWeekChart() {

		layout_chart.removeAllViews();
		
		mDataset = ChartHelper.setDataset_chart_progress_week_asleep(getActivity(), historyList_7day);
		mRenderer = ChartHelper.setRenderer_chart_progress_week_asleep(getActivity(), face, 7, date_query);
		
		mChartView = ChartFactory.getCubeLineChartView(getActivity(), mDataset, mRenderer, ChartHelper.SMOTHNESS);
		
		layout_chart.addView(mChartView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}
	
	
	private void showAsleepMonthChart() {

		layout_chart.removeAllViews();
		
		mDataset = ChartHelper.setDataset_chart_progress_week_asleep(getActivity(), historyList_30day);
		mRenderer = ChartHelper.setRenderer_chart_progress_month_asleep(getActivity(), face, 31, date_query);
		
		mChartView = ChartFactory.getCubeLineChartView(getActivity(), mDataset, mRenderer, ChartHelper.SMOTHNESS);
		
		layout_chart.addView(mChartView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}
	
	
	private void showAsleepYearChart() {

		layout_chart.removeAllViews();
		
		mDataset = ChartHelper.setDataset_chart_progress_year_asleep(getActivity(), historyList_12month);
		mRenderer = ChartHelper.setRenderer_chart_progress_year_asleep(getActivity(), face, 12, date_query);
		
		mChartView = ChartFactory.getCubeLineChartView(getActivity(), mDataset, mRenderer, ChartHelper.SMOTHNESS);
		
		layout_chart.addView(mChartView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}
	
	
	private void initBand() {
		Band band = BindHelper.getLastSyncDevice(getActivity());
		if (band != null) {
			deviceID = band.getDeviceID();
		} else {
			deviceID = 1;
		}
	}
	
	
	private void initGoal() {
		Goal goal = DatabaseProvider_public.queryGoal(getActivity(), profileID);
		if (goal != null) {
			goalStep = goal.getStep();
//			goalBurn = goal.getBurn();
			goalSleep = goal.getSleep();
			
		} else {
			goalStep = Global.DEFAULT_GOAL_STEP;
//			goalBurn = Global.DEFAULT_GOAL_BURN;
			goalSleep = Global.DEFAULT_GOAL_SLEEP;
		}
	}
	
	
	/**
	 * init UI
	 * @param view
	 */
	private void initUI(View view) {
		face = Typeface.createFromAsset (getActivity().getAssets() , "fonts/EurostileExtended-Roman-DTC.ttf");
		
		text_title = (TextView) view.findViewById(R.id.text_title);
		text_value_step = (TextView) view.findViewById(R.id.text_value_steps);
		text_value_sleep = (TextView) view.findViewById(R.id.text_value_sleep);
		text_value_burn = (TextView) view.findViewById(R.id.text_value_burn);
		text_label_cal = (TextView) view.findViewById(R.id.text_label_cal);
		
		button_left = (ImageView) view.findViewById(R.id.button_left);
		button_left.setOnClickListener(myOnClickListener);
		button_right = (ImageView) view.findViewById(R.id.button_right);
		button_right.setOnClickListener(myOnClickListener);
		
		
		radio_awake = (RadioButton) view.findViewById(R.id.radio_awake);
		radio_awake.setOnCheckedChangeListener(myOnCheckedChangeListener);
		radio_asleep = (RadioButton) view.findViewById(R.id.radio_asleep);
		radio_asleep.setOnCheckedChangeListener(myOnCheckedChangeListener);
		
		progressbar_step = (GoalProgressbar) view.findViewById(R.id.progress_step);
		progressbar_sleep = (GoalProgressbar) view.findViewById(R.id.progress_sleep);
		
		layout_chart = (RelativeLayout) view.findViewById(R.id.layout_chart);
		
		ImageView image_circle_step = (ImageView) view.findViewById(R.id.image_circle_step);
		image_circle_step.setOnClickListener(myOnClickListener);
		ImageView image_circle_sleep = (ImageView) view.findViewById(R.id.image_circle_sleep);
		image_circle_sleep.setOnClickListener(myOnClickListener);
		ImageView image_circle_burn = (ImageView) view.findViewById(R.id.image_circle_burn);
		image_circle_burn.setOnClickListener(myOnClickListener);
		
		image_progress_date = (ImageView) view.findViewById(R.id.progress_date);
		image_progress_date.setBackgroundResource(R.drawable.image_progress_day);
		text_progress = (TextView) view.findViewById(R.id.progress_text);
		text_progress.setText(R.string.DAY);	
		
		int densityDPI = ChartHelper.getDensityDpi(getActivity());
		ChartHelper.setTextSize(densityDPI);
	}
	private Typeface face ;
	private TextView text_title, text_progress;
	private TextView text_value_step, text_value_sleep, text_value_burn;
	private TextView text_label_cal;
	private RelativeLayout layout_chart;
	private RadioButton radio_awake, radio_asleep;
	private ImageView button_left, button_right, image_progress_date;
	private GoalProgressbar progressbar_step, progressbar_sleep;
}
