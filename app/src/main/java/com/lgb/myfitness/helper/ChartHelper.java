package com.lgb.myfitness.helper;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import com.lgb.myfitness.R;
import com.lgb.myfitness.been.BPM;
import com.lgb.myfitness.been.HistoryDay;
import com.lgb.myfitness.been.HistoryHour;
import com.lgb.myfitness.been.HistoryMonth;
import com.lgb.myfitness.been.Scale;
import com.lgb.myfitness.global.Global;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.util.DisplayMetrics;

public class ChartHelper {

	private static int TEXT_SIZE = 18;
	private static int TEXT_SIZE_SCALE_SMALL = 8;

	public static float POINT_SIZE = 8f;
	public static int PADDING_SIZE = 20;
	public static final int TOP = 20;
	public static int LEFT = 80;
	public static final int RIGHT = 20;
	public static final int BOTTOM = 20;
	public static int LINE_WIDTH = 5;
	public static final float SMOTHNESS = 0.0f;

	public static void setTextSize(int densityDPI) {
//		System.out.println("densityDPI = " + densityDPI);
//		System.out.println("TEXT SIZE = " + TEXT_SIZE);
//		System.out.println("POINT SIZE = " + POINT_SIZE);
//		System.out.println("PADDING SIZE = " + PADDING_SIZE);
		TEXT_SIZE = densityDPI * 1 / 40 + densityDPI * 1 / 40 + 2;
		TEXT_SIZE_SCALE_SMALL = densityDPI * 1 / 40 ;
		POINT_SIZE = densityDPI * 1 / 45;
		PADDING_SIZE = densityDPI * 1 / 40;
		LINE_WIDTH = densityDPI * 1 / 40 - densityDPI * 1 / 40 / 2;
		LEFT = densityDPI * 1 / 3;
//		System.out.println("TEXT SIZE 2 = " + TEXT_SIZE);
//		System.out.println("POINT SIZE 2 = " + POINT_SIZE);
//		System.out.println("PADDING SIZE 2 = " + PADDING_SIZE);
	}

	private static XYMultipleSeriesRenderer setNormalXYMultipleSeriesRenderer(
			XYMultipleSeriesRenderer mRenderer, Typeface face) {
		if (mRenderer == null) {
			mRenderer = new XYMultipleSeriesRenderer();
		}

//		mRenderer.setTextTypeface(face);

		mRenderer.setShowGridY(false);
		mRenderer.setShowGridX(false);
		// 是否支持图表移动
		mRenderer.setPanEnabled(true, false);
		// 坐标滑动上、下限
		mRenderer.setPanLimits(new double[] { 0, 0, 0, 0 });
		// 是否支持图表缩放
		mRenderer.setZoomEnabled(false, false);
		// mRenderer.setClickEnabled(true); //是否可以点击
		// mRenderer.setSelectableBuffer(50); //点击区域的大小

		// 边框的背景颜色
		mRenderer.setMarginsColor(Color.argb(0, 255, 255, 255));
		mRenderer.setBackgroundColor(Color.argb(0, 255, 255, 255));
		mRenderer.setApplyBackgroundColor(true);
		mRenderer.setGridColor(Color.BLACK);
		mRenderer.setLabelsColor(Color.BLACK);
		mRenderer.setXLabelsColor(Color.BLACK);
		mRenderer.setYLabelsColor(0, Color.BLACK);
		mRenderer.setAxesColor(Color.BLACK);

		mRenderer.setXAxisMin(0);

		mRenderer.setYLabelsAlign(Align.RIGHT);
		mRenderer.setYLabelsPadding(PADDING_SIZE);
		mRenderer.setLabelsTextSize(TEXT_SIZE);
		mRenderer.setLegendTextSize(TEXT_SIZE);
		mRenderer.setAxisTitleTextSize(TEXT_SIZE);
		mRenderer.setPointSize(POINT_SIZE);
		mRenderer.setLegendHeight(50);

		mRenderer.setMargins(new int[] { TOP, LEFT, BOTTOM, RIGHT });

		return mRenderer;
	}

	private static XYSeriesRenderer setNormalXYSeriesRenderer(XYSeriesRenderer r) {
		if (r == null) {
			r = new XYSeriesRenderer();
		}
		r.setLineWidth(LINE_WIDTH);
		r.setPointStyle(PointStyle.CIRCLE);
		r.setFillPoints(true);

		return r;
	}

	/**
	 * activity 中的chart样式
	 * 
	 * @param historyHourList
	 * @param face
	 * @param length
	 * @param type
	 * @return
	 */
	public static XYMultipleSeriesRenderer setRenderer_chart_activity(
			Context context, List<HistoryHour> historyHourList, Typeface face,
			int length, int type) {

		XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
		mRenderer = setNormalXYMultipleSeriesRenderer(mRenderer, face);

		mRenderer.setXAxisMax(length);
		mRenderer.setXLabels(length);
		mRenderer.setXTitle(context.getString(R.string.TIME));
		for (int i = 0; i < 24; i++) {
			mRenderer.addXTextLabel(i, String.valueOf(i));
			mRenderer.addXTextLabel(i + 1, "");
			
			i++;
		}
		mRenderer.setXLabels(0); // 设置只显示替换后的刻度
		mRenderer.setShowCustomTextGrid(true); // 显示自己定制的网格

		if (type == Global.TYPE_CALORIES) {
			int unitCalories = UnitHelper.getBurnUnit(context);
			if (unitCalories == Global.TYPE_CALORIES_KCAL) {
				mRenderer.setYTitle(context.getString(R.string.Calories_kcals));
				mRenderer.setYAxisMax(getMaxBurn(historyHourList) + 1);

			} else {
				mRenderer.setYTitle(context.getString(R.string.Calories_cals));
				mRenderer.setYAxisMax(getMaxBurn(historyHourList) * 1000 + 1);
			}

		} else if (type == Global.TYPE_STEP) {
			mRenderer.setYTitle(context.getString(R.string.Steps));
			mRenderer.setYAxisMax(getMaxStep(historyHourList) + 10);
		} else if (type == Global.TYPE_SLEEP) {
			mRenderer.setYTitle(context.getString(R.string.Sleep_Pattern));
			mRenderer.setYAxisMax(2.5);
			mRenderer.setYLabels(0);
			mRenderer.addYTextLabel(0, context.getString(R.string.Awake));
			mRenderer.addYTextLabel(1, context.getString(R.string.Light));
			mRenderer.addYTextLabel(2, context.getString(R.string.Deep));
		}

		XYSeriesRenderer r = new XYSeriesRenderer();
		r = setNormalXYSeriesRenderer(r);
		r.setColor(Color.RED);

		mRenderer.addSeriesRenderer(r);

		return mRenderer;
	}

	/**
	 * 设置 progress day awake 图表的样式
	 * 
	 * @param face
	 * @param length
	 * @return
	 */
	public static XYMultipleSeriesRenderer setRenderer_chart_progress_day_awake(
			Context context, Typeface face, int length) {

		XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
		mRenderer = setNormalXYMultipleSeriesRenderer(mRenderer, face);

		mRenderer.setXAxisMax(length);
		mRenderer.setXLabels(length);
		mRenderer.setXTitle(context.getString(R.string.TIME));

		mRenderer.setYTitle(context.getString(R.string.Calories_Steps_Trend));
		mRenderer.setYAxisMax(1.1);

		for (int i = 0; i < 24; i++) {
			mRenderer.addXTextLabel(i, String.valueOf(i));
			mRenderer.addXTextLabel(i + 1, "");
			
			i++;
		}
		mRenderer.setXLabels(0); // 设置只显示替换后的刻度
		mRenderer.setShowCustomTextGrid(true); // 显示自己定制的网格
		XYSeriesRenderer r = new XYSeriesRenderer();
		r = setNormalXYSeriesRenderer(r);
		r.setColor(Color.RED);

		XYSeriesRenderer r2 = new XYSeriesRenderer();
		r2 = setNormalXYSeriesRenderer(r2);
		r2.setColor(Color.BLUE);

		mRenderer.addSeriesRenderer(r);
		mRenderer.addSeriesRenderer(r2);

		return mRenderer;
	}

	/**
	 * 设置 progress day asleep 图表样式
	 * 
	 * @param face
	 * @param length
	 * @return
	 */
	public static XYMultipleSeriesRenderer setRenderer_chart_progress_day_asleep(
			Context context, Typeface face, int length) {

		XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
		mRenderer = setNormalXYMultipleSeriesRenderer(mRenderer, face);

		// x轴
		mRenderer.setXAxisMax(length);
		mRenderer.setXLabels(length);
		mRenderer.setXTitle(context.getString(R.string.TIME));
		for (int i = 0; i < 24; i++) {
			mRenderer.addXTextLabel(i, String.valueOf(i));
			mRenderer.addXTextLabel(i + 1, "");
			
			i++;
		}
		mRenderer.setXLabels(0); // 设置只显示替换后的刻度
		mRenderer.setShowCustomTextGrid(true); // 显示自己定制的网格

		// y轴
		mRenderer.setYTitle(context.getString(R.string.Sleep_Pattern));
		mRenderer.setYAxisMax(2.5);
		mRenderer.setYLabels(0);
		mRenderer.addYTextLabel(0, context.getString(R.string.Awake));
		mRenderer.addYTextLabel(1, context.getString(R.string.Light));
		mRenderer.addYTextLabel(2, context.getString(R.string.Deep));

		XYSeriesRenderer r = new XYSeriesRenderer();
		r = setNormalXYSeriesRenderer(r);
		r.setColor(Color.BLUE);

		mRenderer.addSeriesRenderer(r);

		return mRenderer;
	}

	/**
	 * 设置 progress week awake 图表样式
	 * 
	 * @param face
	 * @param length
	 * @param today
	 * @return
	 */
	public static XYMultipleSeriesRenderer setRenderer_chart_progress_week_awake(
			Context context, Typeface face, int length, Calendar today) {
		Calendar[] calArray = CalendarHelper.getLastWeekToTomorrow(today);
		Map<Calendar, HistoryDay> map = CalendarHelper
				.getDayMap_wb013(calArray);

		XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
		mRenderer = setNormalXYMultipleSeriesRenderer(mRenderer, face);
//		mRenderer.setShowGridX(true);
		mRenderer.setShowCustomTextGrid(true); // 显示自己定制的网格

		// x轴
		mRenderer.setXAxisMax(length);
		mRenderer.setXLabels(length);
		mRenderer.setXTitle(context.getString(R.string.DATE));
		mRenderer.setXLabels(0);
		Set<Calendar> keySet = map.keySet();
		int index = 0;
		for (Calendar calendar : keySet) {
			mRenderer.addXTextLabel(index,
					CalendarHelper.getYyyy_MM_dd(calendar).substring(5));
			index++;
		}

		// y轴
		mRenderer.setYTitle(context.getString(R.string.Calories_Steps_Trend));
		mRenderer.setYAxisMax(1.1);

		XYSeriesRenderer r = new XYSeriesRenderer();
		r = setNormalXYSeriesRenderer(r);
		r.setColor(Color.RED);

		XYSeriesRenderer r2 = new XYSeriesRenderer();
		r2 = setNormalXYSeriesRenderer(r2);
		r2.setColor(Color.BLUE);

		mRenderer.addSeriesRenderer(r);
		mRenderer.addSeriesRenderer(r2);

		return mRenderer;
	}

	/**
	 * 设置progress week asleep 图表样式
	 * 
	 * @param face
	 * @param length
	 * @param today
	 * @return
	 */
	public static XYMultipleSeriesRenderer setRenderer_chart_progress_week_asleep(
			Context context, Typeface face, int length, Calendar today) {

		Calendar[] calArray = CalendarHelper.getLastWeekToTomorrow(today);
		Map<Calendar, HistoryDay> map = CalendarHelper
				.getDayMap_wb013(calArray);

		XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
		mRenderer = setNormalXYMultipleSeriesRenderer(mRenderer, face);
//		mRenderer.setShowGridX(true);
		mRenderer.setShowCustomTextGrid(true); // 显示自己定制的网格

		// x轴
		mRenderer.setXAxisMax(length);
		mRenderer.setXLabels(length);
		mRenderer.setXTitle(context.getString(R.string.DATE));
		mRenderer.setXLabels(0);
		Set<Calendar> keySet = map.keySet();
		int index = 0;
		for (Calendar calendar : keySet) {
			mRenderer.addXTextLabel(index,
					CalendarHelper.getYyyy_MM_dd(calendar).substring(5));
			index++;
		}

		// y轴
		mRenderer.setYTitle(context.getString(R.string.Sleep_Rating));
		mRenderer.setYAxisMax(105);

		XYSeriesRenderer r = new XYSeriesRenderer();
		r = setNormalXYSeriesRenderer(r);
		r.setColor(Color.BLUE);

		mRenderer.addSeriesRenderer(r);

		return mRenderer;
	}

	/**
	 * 设置 progress month awake 图表样式
	 * 
	 * @param face
	 * @param length
	 * @param today
	 * @return
	 */
	public static XYMultipleSeriesRenderer setRenderer_chart_progress_month_awake(
			Context context, Typeface face, int length, Calendar today) {

		Calendar[] calArray = CalendarHelper.getLastMonthToTomorrow(today);
		Map<Calendar, HistoryDay> map = CalendarHelper
				.getDayMap_wb013(calArray);

		XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
		mRenderer = setNormalXYMultipleSeriesRenderer(mRenderer, face);
		// mRenderer.setShowGridX(true);

		// x轴
		mRenderer.setXAxisMax(length);
		mRenderer.setXLabels(length);
		mRenderer.setXTitle(context.getString(R.string.DATE));
		mRenderer.setXLabels(0);
		mRenderer.setShowCustomTextGrid(true);
		Set<Calendar> keySet = map.keySet();
		int index = 0;
		// System.out.println(keySet.size());
		for (Calendar calendar : keySet) {
			// System.out.println(Global.sdf_2.format(calendar.getTime()));
			if (index == 0 || index == 5 || index == 10 || index == 15
					|| index == 20 || index == 25 || index == 30) {
				mRenderer.addXTextLabel(index,
						CalendarHelper.getYyyy_MM_dd(calendar).substring(5));
			} else {
				mRenderer.addXTextLabel(index, "");
			}

			index++;
		}

		// y轴
		mRenderer.setYTitle(context.getString(R.string.Calories_Steps_Trend));
		mRenderer.setYAxisMax(1.1);

		XYSeriesRenderer r = new XYSeriesRenderer();
		r = setNormalXYSeriesRenderer(r);
		r.setColor(Color.RED);

		XYSeriesRenderer r2 = new XYSeriesRenderer();
		r2 = setNormalXYSeriesRenderer(r2);
		r2.setColor(Color.BLUE);

		mRenderer.addSeriesRenderer(r);
		mRenderer.addSeriesRenderer(r2);

		return mRenderer;
	}

	/**
	 * 设置 progress month asleep 图表样式
	 * 
	 * @param face
	 * @param length
	 * @param today
	 * @return
	 */
	public static XYMultipleSeriesRenderer setRenderer_chart_progress_month_asleep(
			Context context, Typeface face, int length, Calendar today) {

		Calendar[] calArray = CalendarHelper.getLastMonthToTomorrow(today);
		Map<Calendar, HistoryDay> map = CalendarHelper
				.getDayMap_wb013(calArray);

		XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
		mRenderer = setNormalXYMultipleSeriesRenderer(mRenderer, face);
//		mRenderer.setShowGridX(true);
		mRenderer.setShowCustomTextGrid(true); // 显示自己定制的网格

		// x轴
		mRenderer.setXAxisMax(length);
		mRenderer.setXLabels(length);
		mRenderer.setXTitle(context.getString(R.string.DATE));
		mRenderer.setXLabels(0);
		Set<Calendar> keySet = map.keySet();
		int index = 0;
		for (Calendar calendar : keySet) {
			if (index == 0 || index == 5 || index == 10 || index == 15
					|| index == 20 || index == 25 || index == 30) {
				mRenderer.addXTextLabel(index,
						CalendarHelper.getYyyy_MM_dd(calendar).substring(5));
			} else {
				mRenderer.addXTextLabel(index, "");
			}
			index++;
		}

		// y轴
		mRenderer.setYTitle(context.getString(R.string.Sleep_Rating));
		mRenderer.setYAxisMax(105);

		XYSeriesRenderer r = new XYSeriesRenderer();
		r = setNormalXYSeriesRenderer(r);
		r.setColor(Color.BLUE);

		mRenderer.addSeriesRenderer(r);

		return mRenderer;
	}

	/**
	 * 设置 progress year awake 图表样式
	 * 
	 * @param face
	 * @param length
	 * @param today
	 * @return
	 */
	public static XYMultipleSeriesRenderer setRenderer_chart_progress_year_awake(
			Context context, Typeface face, int length, Calendar today) {

		Map<Calendar, HistoryMonth> map = CalendarHelper.get12MonthMap(today);

		XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
		mRenderer = setNormalXYMultipleSeriesRenderer(mRenderer, face);
//		mRenderer.setShowGridX(true);
		mRenderer.setShowCustomTextGrid(true); // 显示自己定制的网格

		// x轴
		mRenderer.setXAxisMax(length);
		mRenderer.setXLabels(length);
		mRenderer.setXTitle(context.getString(R.string.MONTH));
		mRenderer.setXLabels(0);
		Set<Calendar> keySet = map.keySet();
		int index = 0;
		for (Calendar calendar : keySet) {
			int month = calendar.get(Calendar.MONTH);
			mRenderer.addXTextLabel(index, Global.df_int_2.format(month + 1));
			index++;
		}

		// y轴
		mRenderer.setYTitle(context.getString(R.string.Calories_Steps_Trend));
		mRenderer.setYAxisMax(1.1);

		XYSeriesRenderer r = new XYSeriesRenderer();
		r = setNormalXYSeriesRenderer(r);
		r.setColor(Color.RED);

		XYSeriesRenderer r2 = new XYSeriesRenderer();
		r2 = setNormalXYSeriesRenderer(r2);
		r2.setColor(Color.BLUE);

		mRenderer.addSeriesRenderer(r);
		mRenderer.addSeriesRenderer(r2);

		return mRenderer;
	}

	/**
	 * 设置 progress year asleep 图表样式
	 * 
	 * @param face
	 * @param length
	 * @param today
	 * @return
	 */
	public static XYMultipleSeriesRenderer setRenderer_chart_progress_year_asleep(
			Context context, Typeface face, int length, Calendar today) {

		Map<Calendar, HistoryMonth> map = CalendarHelper.get12MonthMap(today);

		XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
		mRenderer = setNormalXYMultipleSeriesRenderer(mRenderer, face);
//		mRenderer.setShowGridX(true);
		mRenderer.setShowCustomTextGrid(true); // 显示自己定制的网格

		// x轴
		mRenderer.setXAxisMax(length);
		mRenderer.setXLabels(length);
		mRenderer.setXTitle(context.getString(R.string.MONTH));
		mRenderer.setXLabels(0);
		Set<Calendar> keySet = map.keySet();
		int index = 0;
		for (Calendar calendar : keySet) {
			int month = calendar.get(Calendar.MONTH);
			mRenderer.addXTextLabel(index, Global.df_int_2.format(month + 1));
			index++;
		}

		// y轴
		mRenderer.setYTitle(context.getString(R.string.Sleep_Rating));
		mRenderer.setYAxisMax(105);

		XYSeriesRenderer r = new XYSeriesRenderer();
		r = setNormalXYSeriesRenderer(r);
		r.setColor(Color.BLUE);

		mRenderer.addSeriesRenderer(r);

		return mRenderer;
	}

	/**
	 * 设置 scale month weight small 图表样式
	 * 
	 * @param face
	 * @param length
	 * @param today
	 * @return
	 */

	public static XYMultipleSeriesRenderer setRenderer_chart_scale_month_weight_small(
			Context context, List<Scale> scaleList, Typeface face, int length,
			Calendar today) {

		Calendar[] calArray = CalendarHelper.getLastMonthToTomorrow(today);
		Map<Calendar, Scale> map = CalendarHelper.getDayMap_scale(calArray);

		XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
		mRenderer = setNormalXYMultipleSeriesRenderer(mRenderer, face);

		// X轴
		// /////////////////////////////////////////////////
		mRenderer.setXAxisMax(length);
		mRenderer.setXLabels(length);
		mRenderer.setXLabels(0);
		mRenderer.setShowCustomTextGrid(true); // 显示自己定制的网格
		Set<Calendar> keySet = map.keySet();
		int index = 0;
		for (Calendar calendar : keySet) {
			if (index == 0 || index == 5 || index == 10 || index == 15
					|| index == 20 || index == 25 || index == 30) {
				mRenderer.addXTextLabel(index,
						CalendarHelper.getYyyy_MM_dd(calendar).substring(5));
			} else {
				mRenderer.addXTextLabel(index, "");
			}
			index++;
		}

		// Y轴
		// /////////////////////////////////////////////////
		mRenderer.setYAxisMax(getMaxWeight(scaleList) + 5);

		// 全局
		// /////////////////////////////////////////////////////////////
		mRenderer.setBackgroundColor(Color.argb(0, 0, 0, 0));
		mRenderer.setApplyBackgroundColor(true);
		mRenderer.setGridColor(Color.WHITE);
		mRenderer.setLabelsColor(Color.WHITE);
		mRenderer.setAxesColor(Color.WHITE);
		mRenderer.setXLabelsColor(Color.WHITE);
		mRenderer.setYLabelsColor(0, Color.WHITE);
		mRenderer.setLabelsTextSize(TEXT_SIZE_SCALE_SMALL);
		mRenderer.setPointSize(POINT_SIZE / 2);
		mRenderer.setMargins(new int[] { 10, 25, 5, 5 });
		mRenderer.setYLabelsPadding(5);
		mRenderer.setXLabelsPadding(5);
		XYSeriesRenderer r = new XYSeriesRenderer();
		r = setNormalXYSeriesRenderer(r);
		r.setColor(Color.BLUE);
		r.setLineWidth(LINE_WIDTH / 2);
		mRenderer.addSeriesRenderer(r);
		return mRenderer;
	}

	/**
	 * 设置 scale month weight 图表样式
	 * 
	 * @param face
	 * @param length
	 * @param today
	 * @return
	 */

	public static XYMultipleSeriesRenderer setRenderer_chart_scale_month_weight(
			Context context, List<Scale> scaleList, Typeface face, int length,
			Calendar today) {

		Calendar[] calArray = CalendarHelper.getLastMonthToTomorrow(today);
		Map<Calendar, Scale> map = CalendarHelper.getDayMap_scale(calArray);

		XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();

		// X轴
		mRenderer.setXTitle(context.getString(R.string.DATE));
		mRenderer.setXAxisMax(length);
		mRenderer.setXLabels(length);
		mRenderer.setXLabels(0); // 设置只显示替换后的刻度
		mRenderer.setShowCustomTextGrid(true); // 显示自己定制的网格
		Set<Calendar> keySet = map.keySet();
		System.out.println("keyset length = " + keySet.size());
		int index = 0;
		for (Calendar calendar : keySet) {
			if (index == 0 || index == 5 || index == 10 || index == 15
					|| index == 20 || index == 25 || index == 30) {
				mRenderer.addXTextLabel(index,
						CalendarHelper.getYyyy_MM_dd(calendar).substring(5));
			} else {
				mRenderer.addXTextLabel(index, "");
			}
			index++;
		}

		// Y轴
		mRenderer.setYTitle(context.getString(R.string.Weight_Kg));
		mRenderer.setYAxisMax(getMaxWeight(scaleList) + 5);

		// 全局
		mRenderer = setNormalXYMultipleSeriesRenderer(mRenderer, face);
		mRenderer.setBackgroundColor(Color.argb(0, 0, 0, 0));
		mRenderer.setApplyBackgroundColor(true);
		mRenderer.setGridColor(Color.WHITE);
		mRenderer.setLabelsColor(Color.WHITE);
		mRenderer.setAxesColor(Color.WHITE);
		mRenderer.setXLabelsColor(Color.WHITE);
		mRenderer.setYLabelsColor(0, Color.WHITE);
		mRenderer.setLabelsTextSize(TEXT_SIZE);
		mRenderer.setAxisTitleTextSize(TEXT_SIZE + 3);
		XYSeriesRenderer r = new XYSeriesRenderer();
		r = setNormalXYSeriesRenderer(r);
		r.setColor(Color.BLUE);
		mRenderer.addSeriesRenderer(r);
		return mRenderer;
	}

	/**
	 * 设置 scale month bmi 图表样式
	 * 
	 * @param face
	 * @param length
	 * @param today
	 * @return
	 */

	public static XYMultipleSeriesRenderer setRenderer_chart_scale_month_bmi(
			Context context, List<Scale> scaleList, Typeface face, int length,
			Calendar today) {

		Calendar[] calArray = CalendarHelper.getLastMonthToTomorrow(today);
		Map<Calendar, Scale> map = CalendarHelper.getDayMap_scale(calArray);

		XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();

		// X轴
		mRenderer.setXTitle(context.getString(R.string.DATE));
		mRenderer.setXAxisMax(length);
		mRenderer.setXLabels(length);
		mRenderer.setXLabels(0); // 设置只显示替换后的刻度
		mRenderer.setShowCustomTextGrid(true); // 显示自己定制的网格
		Set<Calendar> keySet = map.keySet();
		System.out.println("keyset length = " + keySet.size());
		int index = 0;
		for (Calendar calendar : keySet) {
			if (index == 0 || index == 5 || index == 10 || index == 15
					|| index == 20 || index == 25 || index == 30) {
				mRenderer.addXTextLabel(index,
						CalendarHelper.getYyyy_MM_dd(calendar).substring(5));
			} else {
				mRenderer.addXTextLabel(index, "");
			}
			index++;
		}

		// Y轴
		mRenderer.setYTitle(context.getString(R.string.BMI));
		mRenderer.setYAxisMax(getMaxBMI(scaleList) + 2);

		// 全局
		mRenderer = setNormalXYMultipleSeriesRenderer(mRenderer, face);
		mRenderer.setBackgroundColor(Color.argb(0, 0, 0, 0));
		mRenderer.setApplyBackgroundColor(true);
		mRenderer.setGridColor(Color.WHITE);
		mRenderer.setLabelsColor(Color.WHITE);
		mRenderer.setAxesColor(Color.WHITE);
		mRenderer.setXLabelsColor(Color.WHITE);
		mRenderer.setYLabelsColor(0, Color.WHITE);
		mRenderer.setLabelsTextSize(TEXT_SIZE);
		mRenderer.setAxisTitleTextSize(TEXT_SIZE + 3);
		XYSeriesRenderer r = new XYSeriesRenderer();
		r = setNormalXYSeriesRenderer(r);
		r.setColor(Color.RED);
		mRenderer.addSeriesRenderer(r);
		return mRenderer;
	}
	
	
	public static XYMultipleSeriesRenderer setRenderer_chart_bpm(
			Context context, int length) {
		Calendar today = CalendarHelper.getToday();
		Calendar[] calArray = CalendarHelper.getLast30dayToTomorrow(today);
		Map<Calendar, HistoryDay> map = CalendarHelper
				.getDayMap_wb013(calArray);
		
		XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
		mRenderer = setNormalXYMultipleSeriesRenderer(mRenderer, null);

		mRenderer.setXAxisMax(length);
		mRenderer.setXLabels(length);
		mRenderer.setLabelsTextSize(TEXT_SIZE + TEXT_SIZE/3);
		Set<Calendar> keySet = map.keySet();
		int index = 0;
		// System.out.println(keySet.size());
		for (Calendar calendar : keySet) {
			// System.out.println(Global.sdf_2.format(calendar.getTime()));
			if (index == 0 || index == 5 || index == 10 || index == 15
					|| index == 20 || index == 25 || index == 29) {
				mRenderer.addXTextLabel(index,
						CalendarHelper.get_dd_MM(calendar));
			} else {
				mRenderer.addXTextLabel(index, "");
			}

			index++;
		}
		mRenderer.setXTitle(context.getString(R.string.DATE));
//		for (int i = 0; i < length; i++) {
//			mRenderer.addXTextLabel(i, "");
//		}
		mRenderer.setXLabels(0); // 设置只显示替换后的刻度
		mRenderer.setShowCustomTextGrid(true); // 显示自己定制的网格

		mRenderer.setYTitle("");
		mRenderer.setYAxisMax(6.5);
		mRenderer.setYLabels(0);
		mRenderer.addYTextLabel(0, context.getString(R.string.L1) + "  ");
		mRenderer.addYTextLabel(1, context.getString(R.string.L2) + "  ");
		mRenderer.addYTextLabel(2, context.getString(R.string.L3) + "  ");
		mRenderer.addYTextLabel(3, context.getString(R.string.L4) + "  ");
		mRenderer.addYTextLabel(4, context.getString(R.string.L5) + "  ");
		mRenderer.addYTextLabel(5, context.getString(R.string.L6) + "  ");
		mRenderer.addYTextLabel(6, context.getString(R.string.L7) + "  ");

		mRenderer.setShowLegend(false);
		
		XYSeriesRenderer r = new XYSeriesRenderer();
		r = setNormalXYSeriesRenderer(r);
		r.setColor(Color.RED);

		mRenderer.addSeriesRenderer(r);
		
		return mRenderer;
	}
	

	/**
	 * 获取最大的卡路里值
	 * 
	 * @param historyHourList
	 * @return
	 */
	private static double getMaxBurn(List<HistoryHour> historyHourList) {
		if (historyHourList != null && historyHourList.size() > 0) {
			double maxBurn = 0;
			for (HistoryHour historyHour : historyHourList) {
				if (historyHour != null) {
					double burn = historyHour.getBurn();
					if (burn > maxBurn) {
						maxBurn = burn;
					}
				}
			}

			return maxBurn;
		}

		return 0;
	}

	/**
	 * 获取最大的卡路里值
	 * 
	 * @param historyDayList
	 * @return
	 */
	private static double getMaxBurn_day(List<HistoryDay> historyDayList) {
		if (historyDayList != null && historyDayList.size() > 0) {
			double maxBurn = 0;
			for (HistoryDay historyDay : historyDayList) {
				if (historyDay != null) {
					double burn = historyDay.getBurn();
					if (burn > maxBurn) {
						maxBurn = burn;
					}
				}
			}

			return maxBurn;
		}

		return 0;
	}

	/**
	 * 获取最大的卡路里值
	 * 
	 * @param historyMonthList
	 * @return
	 */
	private static double getMaxBurn_month(List<HistoryMonth> historyMonthList) {
		if (historyMonthList != null && historyMonthList.size() > 0) {
			double maxBurn = 0;
			for (HistoryMonth historyMonth : historyMonthList) {
				if (historyMonth != null) {
					double burn = historyMonth.getBurn();
					if (burn > maxBurn) {
						maxBurn = burn;
					}
				}
			}

			return maxBurn;
		}

		return 0;
	}

	/**
	 * 获取最大的步数
	 * 
	 * @param historyHourList
	 * @return
	 */
	private static double getMaxStep(List<HistoryHour> historyHourList) {
		if (historyHourList != null && historyHourList.size() > 0) {
			int max = 0;
			for (HistoryHour historyHour : historyHourList) {
				if (historyHour != null) {
					int step = historyHour.getStep();
					if (step > max) {
						max = step;
					}
				}
			}

			return max;
		}

		return 0;
	}

	/**
	 * 获取最大的步数
	 * 
	 * @param historyDayList
	 * @return
	 */
	private static double getMaxStep_day(List<HistoryDay> historyDayList) {
		if (historyDayList != null && historyDayList.size() > 0) {
			int max = 0;
			for (HistoryDay historyDay : historyDayList) {
				if (historyDay != null) {
					int step = historyDay.getStep();
					if (step > max) {
						max = step;
					}
				}
			}

			return max;
		}

		return 0;
	}

	/**
	 * 获取最大的步数
	 * 
	 * @param historyMonthList
	 * @return
	 */
	private static double getMaxStep_month(List<HistoryMonth> historyMonthList) {
		if (historyMonthList != null && historyMonthList.size() > 0) {
			int max = 0;
			for (HistoryMonth historyMonth : historyMonthList) {
				if (historyMonth != null) {
					int step = historyMonth.getStep();
					if (step > max) {
						max = step;
					}
				}
			}

			return max;
		}

		return 0;
	}

	/**
	 * 获取最大的体重值
	 * 
	 * @param historyHourList
	 * @return
	 */
	private static double getMaxWeight(List<Scale> scaleList) {
		if (scaleList != null && scaleList.size() > 0) {
			double maxWeight = 0;
			for (Scale scale : scaleList) {
				if (scale != null) {
					double weight = scale.getWeight();
					if (weight > maxWeight) {
						maxWeight = weight;
					}
				}
			}

			return maxWeight;
		}

		return 0;
	}

	/**
	 * 获取最大的BMI值
	 * 
	 * @param historyHourList
	 * @return
	 */
	private static double getMaxBMI(List<Scale> scaleList) {
		if (scaleList != null && scaleList.size() > 0) {
			double maxBMI = 0;
			for (Scale scale : scaleList) {
				if (scale != null) {
					double bmi = scale.getBMI();
					if (bmi > maxBMI) {
						maxBMI = bmi;
					}
				}
			}

			return maxBMI;
		}

		return 0;
	}

	/**
	 * 根据睡眠动作次数确定睡眠类型
	 * 
	 * @param sleepMove
	 * @return
	 */
	public static int getSleepPattern(int sleepMove) {
		if (sleepMove == 255) {
			return Global.TYPE_SLEEP_AWAKE;
		} else if (sleepMove < 10) {
			return Global.TYPE_SLEEP_DEEP;
		} else if (sleepMove >= 10 && sleepMove < 255) {
			return Global.TYPE_SLEEP_LIGHT;
		}

		return Global.TYPE_SLEEP_AWAKE;
	}

	/**
	 * 设置activity图表数据
	 * 
	 * @param historyList
	 * @param type
	 * @return
	 */
	public static XYMultipleSeriesDataset setDataset_chart_activity(
			Context context, List<HistoryHour> historyList, int type) {

		XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
		XYSeries series = new XYSeries("");
		if (historyList != null) {

			if (type == Global.TYPE_CALORIES) {
				int unitCalories = UnitHelper.getBurnUnit(context);
				if (unitCalories == Global.TYPE_CALORIES_KCAL) {
					series = new XYSeries(
							context.getString(R.string.Calories_kcals));
				} else {
					series = new XYSeries(
							context.getString(R.string.Calories_cals));
				}

				for (int i = 0; i < historyList.size(); i++) {
					HistoryHour history = historyList.get(i);
					if (history != null) {
						if (unitCalories == Global.TYPE_CALORIES_KCAL) {
							series.add(i, history.getBurn());
						} else {
							series.add(i, history.getBurn() * 1000);
						}

					} else {
						series.add(i, 0);
					}
				}
			} else if (type == Global.TYPE_STEP) {
				series = new XYSeries(context.getString(R.string.Steps));
				for (int i = 0; i < historyList.size(); i++) {
					HistoryHour history = historyList.get(i);
					if (history != null) {
						series.add(i, history.getStep());
					} else {
						series.add(i, 0);
					}
				}
			} else if (type == Global.TYPE_SLEEP) {
				series = new XYSeries(context.getString(R.string.Sleep_Pattern));
				for (int i = 0; i < historyList.size(); i++) {
					HistoryHour history = historyList.get(i);
					if (history != null) {

						int sleepMove = history.getSleepMove();
						int sleepPattern = getSleepPattern(sleepMove);
						series.add(i, sleepPattern);
					} else {
						series.add(i, 0);
					}
				}
			}
		}
		mDataset.addSeries(series);

		return mDataset;
	}

	/**
	 * 设置 progress day awake图表的数据
	 * 
	 * @param historyList
	 * @return
	 */
	public static XYMultipleSeriesDataset setDataset_chart_progress_day_awake(
			Context context, List<HistoryHour> historyList) {

		XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();

		XYSeries series_burn = new XYSeries(
				context.getString(R.string.Calories));
		XYSeries series_step = new XYSeries(context.getString(R.string.Steps));

		if (historyList != null) {
			double maxBurn = getMaxBurn(historyList);
			double maxStep = getMaxStep(historyList);

			for (int i = 0; i < historyList.size(); i++) {
				HistoryHour history = historyList.get(i);
				if (history != null) {
					// System.out.println("max burn:" + maxBurn + ", burn:" +
					// history.getBurn());
					if (maxBurn == 0) {
						series_burn.add(i, 0);
					} else {
						series_burn
								.add(i, (double) history.getBurn() / maxBurn);
					}
					if (maxStep == 0) {
						series_step.add(i, 0);
					} else {
						series_step
								.add(i, (double) history.getStep() / maxStep);
					}

				} else {
					series_burn.add(i, 0);
					series_step.add(i, 0);
				}
			}
		}

		mDataset.addSeries(series_burn);
		mDataset.addSeries(series_step);

		return mDataset;
	}

	/**
	 * 设置 progress week awake图表的数据
	 * 
	 * @param historyList
	 * @return
	 */
	public static XYMultipleSeriesDataset setDataset_chart_progress_week_awake(
			Context context, List<HistoryDay> historyList) {

		XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();

		XYSeries series_burn = new XYSeries(
				context.getString(R.string.Calories));
		XYSeries series_step = new XYSeries(context.getString(R.string.Steps));

		if (historyList != null) {
			double maxBurn = getMaxBurn_day(historyList);
			double maxStep = getMaxStep_day(historyList);

			for (int i = 0; i < historyList.size(); i++) {
				HistoryDay history = historyList.get(i);
				if (history != null) {
					if (maxBurn == 0) {
						series_burn.add(i, 0);
					} else {
						series_burn
								.add(i, (double) history.getBurn() / maxBurn);
					}
					if (maxStep == 0) {
						series_step.add(i, 0);
					} else {
						series_step
								.add(i, (double) history.getStep() / maxStep);
					}
				} else {
					series_burn.add(i, 0);
					series_step.add(i, 0);
				}
			}
		}

		mDataset.addSeries(series_burn);
		mDataset.addSeries(series_step);

		return mDataset;
	}

	/**
	 * 设置 progress year awake图表的数据
	 * 
	 * @param historyList
	 * @return
	 */
	public static XYMultipleSeriesDataset setDataset_chart_progress_year_awake(
			Context context, List<HistoryMonth> historyList) {

		XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();

		XYSeries series_burn = new XYSeries(
				context.getString(R.string.Calories));
		XYSeries series_step = new XYSeries(context.getString(R.string.Steps));

		if (historyList != null) {
			double maxBurn = getMaxBurn_month(historyList);
			double maxStep = getMaxStep_month(historyList);

			for (int i = 0; i < historyList.size(); i++) {
				HistoryMonth history = historyList.get(i);
				if (history != null) {
					if (maxBurn == 0) {
						series_burn.add(i, 0);
					} else {
						series_burn
								.add(i, (double) history.getBurn() / maxBurn);
					}
					if (maxStep == 0) {
						series_step.add(i, 0);
					} else {
						series_step
								.add(i, (double) history.getStep() / maxStep);
					}
				} else {
					series_burn.add(i, 0);
					series_step.add(i, 0);
				}
			}
		}

		mDataset.addSeries(series_burn);
		mDataset.addSeries(series_step);

		return mDataset;
	}

	/**
	 * 设置progress day asleep图表的数据
	 * 
	 * @param historyList
	 * @return
	 */
	public static XYMultipleSeriesDataset setDataset_chart_progress_day_asleep(
			Context context, List<HistoryHour> historyList) {
		XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();

		XYSeries series_sleep = new XYSeries(
				context.getString(R.string.Sleep_Pattern));

		if (historyList != null) {
			for (int i = 0; i < historyList.size(); i++) {
				HistoryHour history = historyList.get(i);
				if (history != null) {
					int sleepMove = history.getSleepMove();
					int sleepPattern = getSleepPattern(sleepMove);
					series_sleep.add(i, sleepPattern);
				} else {
					series_sleep.add(i, 0);
				}
			}
		}

		mDataset.addSeries(series_sleep);

		return mDataset;
	}

	/**
	 * 设置progress week asleep图表的数据
	 * 
	 * @param historyList
	 * @return
	 */
	public static XYMultipleSeriesDataset setDataset_chart_progress_week_asleep(
			Context context, List<HistoryDay> historyList) {
		XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();

		XYSeries series_sleep = new XYSeries(
				context.getString(R.string.Sleep_Rating));

		if (historyList != null) {
			for (int i = 0; i < historyList.size(); i++) {
				HistoryDay history = historyList.get(i);
				if (history != null) {
					// int sleepQuality = DistanceHelper.calculateSleepQuality(
					// history.getDeepSleepHour(),
					// history.getDeepSleepHour() + history.getLightSleepHour(),
					// history.getSleepMove());
					int sleepQuality = history.getSleepQuality();
					series_sleep.add(i, sleepQuality);
				} else {
					series_sleep.add(i, 0);
				}
			}
		}

		mDataset.addSeries(series_sleep);

		return mDataset;
	}

	/**
	 * 设置progress week asleep图表的数据
	 * 
	 * @param historyList
	 * @return
	 */
	public static XYMultipleSeriesDataset setDataset_chart_progress_year_asleep(
			Context context, List<HistoryMonth> historyList) {
		XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();

		XYSeries series_sleep = new XYSeries(
				context.getString(R.string.Sleep_Rating));

		if (historyList != null) {
			for (int i = 0; i < historyList.size(); i++) {
				HistoryMonth history = historyList.get(i);
				if (history != null) {
					int dayAmount = history.getDayAmount();
					if (dayAmount != 0) {
						series_sleep.add(i, history.getTotoalSleepQuality()
								/ dayAmount);
					} else {
						series_sleep.add(i, 0);
					}

				} else {
					series_sleep.add(i, 0);
				}
			}
		}

		mDataset.addSeries(series_sleep);

		return mDataset;
	}

	/**
	 * 设置scale month weight图表的数据
	 * 
	 * @param historyList
	 * @return
	 */
	public static XYMultipleSeriesDataset setDataset_chart_scale_month_weihgt(
			Context context, List<Scale> scaleList) {
		XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
		XYSeries series = new XYSeries("");
		if (scaleList != null) {

			series = new XYSeries("");
			for (int i = 0; i < scaleList.size(); i++) {
				Scale scale = scaleList.get(i);
				if (scale != null) {
					series.add(i, (int)scale.getWeight());
				} else {
					series.add(i, 0);
				}
			}
		}
		mDataset.addSeries(series);

		return mDataset;
	}

	/**
	 * 设置scale month bmi图表的数据
	 * 
	 * @param historyList
	 * @return
	 */
	public static XYMultipleSeriesDataset setDataset_chart_scale_month_bmi(
			Context context, List<Scale> scaleList) {
		XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
		XYSeries series = new XYSeries("");
		if (scaleList != null) {

			series = new XYSeries("");
			for (int i = 0; i < scaleList.size(); i++) {
				Scale scale = scaleList.get(i);
				if (scale != null) {
					series.add(i, scale.getBMI());
				} else {
					series.add(i, 0);
				}
			}
		}
		mDataset.addSeries(series);

		return mDataset;
	}
	
	
	public static XYMultipleSeriesDataset setDataset_chart_bpm(
			Context context, List<BPM> bpmList, int length) {

		XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
		XYSeries series = new XYSeries("");
		if (bpmList != null) {

			int size = bpmList.size();
		
			for (int i = 0; i < length; i++) {
				
				if (i < size) {
					BPM bpm = bpmList.get(i);
					
					if (bpm != null) {
						series.add(i, CalculateHelper.getBPMLevel(bpm.getSystolic(), bpm.getDiatolic()));

					} else {
						series.add(i, Global.TYPE_BPM_LEVEL_1);
					}
					
				} else {
					series.add(i, Global.TYPE_BPM_LEVEL_1);
				}
				
			}
		}
		mDataset.addSeries(series);

		return mDataset;
	}
	

	public static int getDensityDpi(Activity context) {
		DisplayMetrics metric = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(metric);
		int densityDpi = metric.densityDpi; // 屏幕密度DPI（120 / 160 / 240）

		return densityDpi;
	}

	

	
}
