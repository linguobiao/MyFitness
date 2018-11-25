package com.lgb.myfitness.helper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.lgb.myfitness.R;
import com.lgb.myfitness.been.HistoryDay;
import com.lgb.myfitness.been.HistoryHour;
import com.lgb.myfitness.been.HistoryMonth;
import com.lgb.myfitness.been.Profile;
import com.lgb.myfitness.been.Scale;
import com.lgb.myfitness.database.DatabaseProvider_public;
import com.lgb.myfitness.global.Global;

public class CalculateHelper {
	
	/**
	 * 根据步数计算出距离
	 * km
	 * @param steps
	 * @return
	 */
	public static double calculateDistance(Context context, int steps) {
		int stepDistance = 0;
		
		SharedPreferences mPrefs = context.getSharedPreferences(Global.KEY_PREF, Context.MODE_PRIVATE);
		String name = mPrefs.getString(Global.KEY_PROFILE_NAME, "");
		
		Profile profile = DatabaseProvider_public.queryProfile(context, name);
		if (profile != null) {
//			Log.i("DistanceHelper", "profile is not null");
			double height = profile.getHeight();
//			Log.i("DistanceHelper", "hightStr:"  + heightStr + "!!!");
			if (height != 0) {
				stepDistance = calculateStepDistance(height);
//				Log.i("DistanceHelper", "hight:"  + height);
			} else {
				stepDistance = calculateStepDistance(Global.DEFAULT_HEIGHT);
			}
			
		} else {
			stepDistance = calculateStepDistance(Global.DEFAULT_HEIGHT);
		}
//		Log.i("DisanceHelper", "step Distance:" + stepDistance);
		double distance_cm = (double)steps * (double)stepDistance;
		
		double distance_km = distance_cm / 100 / 1000;
		
		Log.i("calculateDistance", "step:" + steps + ", stepDistance:" + stepDistance + ", km:" + distance_km);
		
		return distance_km;
	}
	
	
	/**
	 * 计算目标距离
	 * 130，相当于13.0km
	 * @param steps
	 * @param stepDistance
	 * @return
	 */
	public static String calculateGoalDistance(int steps, int stepDistance) {
		int distance_cm = steps * stepDistance;
		
		double distance_km = (double)distance_cm / 100 / 1000;
		
		String valueStr = Global.df_double_1.format(distance_km * 10);
		valueStr = valueStr.replace(",", ".");
		
		return valueStr;
	}
	
	
	/**
	 * step distance(cm)
	 * @param height(cm)
	 * @return
	 */
	public static int calculateStepDistance(double height) {
		if (height < 145) {
			return 45;
		} else if (height >= 145 && height < 165) {
			return 55;
		} else if (height >= 165 && height < 180) {
			return 65;
		} else if (height >= 180 && height < 195) {
			return 75;
		} else if (height >= 195) {
			return 85;
		}
		
		return 65;
	}
	
	
	/**
	 * 计算睡眠质量
	 * @param deepSleep
	 * @param sleep
	 * @param sleepMove
	 * @return
	 */
	public static int calculateSleepQuality(int deepSleep, int sleep, int sleepMove) {
		if (sleepMove > 800) {
			sleepMove = 800;
		}
		if (sleep != 0) {
			double sleepQuality = 100 * (0.7 * 3 * deepSleep / sleep + 0.3 * (800 - sleepMove) / 800);
			
			if (sleepQuality > 100) {
				sleepQuality = 100;
			}
			
			return (int) sleepQuality;
		}
		
		return 0;
		
	}
	
	
	public static double calculateGoalPercent(double value, double goal) {
		double goalPercent = 0;
		if (goal != 0) {
			goalPercent = (double)value / (double)goal * 100;
			
//			if (goalPercent > 100) {
//				goalPercent = 100;
//			}
		}
		
		return goalPercent;
	}
	

	/**
	 * cm 转换成 feet
	 * @param cm
	 * @return
	 */
	public static double cmToFeet(double cm) {
		return cm / 100 / 0.3048;
	}
	
	/**
	 * feet 转换成 cm
	 * @param feet
	 * @return
	 */
	public static double feetToCm(double feet) {
		return feet * 100 * 0.3048;
	}
	
	
	/**
	 * kg 转换成 lbs
	 * @param kg
	 * @return
	 */
	public static double kgToLbs(double kg) {
		return kg / 0.4536;
	}
	
	/**
	 * lbs 转换成 kg
	 * @param lbs
	 * @return
	 */
	public static double LbsToKg(double lbs) {
		return lbs * 0.4536;
	}
	
	
	public static double kmToMile(double km) {
		return km / 1.609;
	}
	
	
	public static double mileToKm(double mile) {
		return mile * 1.609;
	}
	
	
	/**
	 * 12小时转化为24小时
	 * @param hour12Str
	 * @return
	 */
	public static String hour12To24(String hour12Str) {
		
		if (hour12Str != null && !hour12Str.equals("")) {
			int index = hour12Str.indexOf(":");
			String hourStr = hour12Str.substring(0, index);
			boolean isDigital = isDigital(hourStr);
			if (isDigital) {
				int hour = Integer.parseInt(hourStr);
				// 如果显示为pm， 时间加12小时
				if (hour12Str.contains(Global.ARRAY_AM_PM[1])) {
					hour += 12;
				}
				
				return Global.df_int_2.format(hour) + ":" + Global.ARRAY_MINUTE_ONE[0];
			}
		}
		
		return "00:00";
	}
	
	
	public static String hour24To12(String hour24Str) {
		if (hour24Str != null && !hour24Str.equals("")) {
			int index = hour24Str.indexOf(":");
			String hourStr = hour24Str.substring(0, index);
			boolean isDigital = isDigital(hourStr);
			
			if (isDigital) {
				int hour = Integer.parseInt(hourStr);
				if (hour > 12) {
					hour -= 12;
					return Global.df_int_2.format(hour) + ":" + Global.ARRAY_MINUTE_ONE[0] + Global.ARRAY_AM_PM[1];
				} else {
					return Global.df_int_2.format(hour) + ":" + Global.ARRAY_MINUTE_ONE[0] + Global.ARRAY_AM_PM[0];
				}
			}
		}
		
		return "00:00AM";
	}
	
	
	public static boolean isDigital(String str) {
		if (str != null) {
			char[] charArray = str.toCharArray();
			for (char c : charArray) {
				if (!Character.isDigit(c)) {
					  return false;
				}  
			}
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * 将小时历史数据放入到map中
	 * @param map
	 * @param historyHourList
	 */
	public static List<Scale> handleToCompleteWeight(Map<Calendar, Scale> map, List<Scale> scaleList) {
		
		List<Scale> ScaleList = new ArrayList<Scale>();
		System.out.println("scaleList = " + scaleList);
		if (scaleList != null && map != null) {
			// 循环数据库中的历史小时数据
			for (Scale scale : scaleList) {
				Calendar scale_datetime = scale.getDate();
				
				if (map.containsKey(scale_datetime)) {
					map.put(scale_datetime, scale);
				}
			}
			
			// 将完整的数据添加到新 list
			Set<Calendar> keySet = map.keySet();
			for (Calendar key : keySet) {
//				System.out.println(key.getTime());
				Scale scale = map.get(key);
				if (scale == null) {
					scale = new Scale();
					scale.setDate(key);
					scale.setWeight(0);
				}
				ScaleList.add(scale);
			}
		}
		
		return ScaleList;
	}
	
	/**
	 * 将小时历史数据放入到map中
	 * @param map
	 * @param historyHourList
	 */
	public static List<HistoryHour> handleToComplete24Hour(Map<Calendar, HistoryHour> map, List<HistoryHour> historyList) {
		
		List<HistoryHour> historyHourList = new ArrayList<HistoryHour>();
		
		if (historyList != null && map != null) {
			// 循环数据库中的历史小时数据
			for (HistoryHour history_hour : historyList) {
				Calendar history_datetime = history_hour.getDate();
				
				if (map.containsKey(history_datetime)) {
					map.put(history_datetime, history_hour);
				}
			}
			
			// 将完整的数据添加到新 list
			Set<Calendar> keySet = map.keySet();
			for (Calendar key : keySet) {
//				System.out.println(key.getTime());
				HistoryHour history = map.get(key);
				if (history == null) {
					history = new HistoryHour();
					history.setDate(key);
					history.setSleepMove(255);
				}
				historyHourList.add(history);
			}
		}
		
		return historyHourList;
	}
	
	
	/**
	 * 周或者月份
	 * @param map
	 * @param historyList_hour
	 * @return
	 */
	public static List<HistoryDay> handleToCompleteWeekOrMonthData(Map<Calendar, HistoryDay> map, List<HistoryHour> historyList_hour) {
		
		List<HistoryDay> historyDateList = new ArrayList<HistoryDay>();
		
		if (historyList_hour != null && map != null) {
			// 循环数据库中的历史小时数据
			for (HistoryHour history_hour : historyList_hour) {
				
				Calendar history_datetime = Calendar.getInstance();
				history_datetime.setTimeInMillis(history_hour.getDate().getTimeInMillis());
				// 日期格式yyyy.mm.dd
				history_datetime = CalendarHelper.setDayFormat(history_datetime);
				
				if (map.containsKey(history_datetime)) {
					HistoryDay historyDate = map.get(history_datetime);
					// 如果没有数据则新建
					if (historyDate == null) {
						historyDate = new HistoryDay();
						historyDate.setDate(history_datetime);
						historyDate.setBurn(history_hour.getBurn());
						historyDate.setStep(history_hour.getStep());
						int sleepMove = history_hour.getSleepMove();
						if (sleepMove != 255) {
							historyDate.setSleepMove(sleepMove);
						}
						int sleepPattern = getSleepPattern_accordingSleepMove(sleepMove);
						if (sleepPattern == Global.TYPE_SLEEP_DEEP) {
							historyDate.setDeepSleepHour(1);
						} else if (sleepPattern == Global.TYPE_SLEEP_LIGHT) {
							historyDate.setLightSleepHour(1);
						}	
					}
					// 如果有数据则累加
					else {
						historyDate.setBurn(historyDate.getBurn() + history_hour.getBurn());
						historyDate.setStep(historyDate.getStep() + history_hour.getStep());
						int sleepMove = history_hour.getSleepMove();
						if (sleepMove != 255) {
							historyDate.setSleepMove(historyDate.getSleepMove() + sleepMove);
						}
						int sleepPattern = getSleepPattern_accordingSleepMove(sleepMove);
						if (sleepPattern == Global.TYPE_SLEEP_DEEP) {
							historyDate.setDeepSleepHour(historyDate.getDeepSleepHour() + 1);
						} else if (sleepPattern == Global.TYPE_SLEEP_LIGHT) {
							historyDate.setLightSleepHour(historyDate.getLightSleepHour() + 1);
						}	
					}
					
					map.put(history_datetime, historyDate);
				}
			}
			
			// 将完整的数据添加到新 list
			Set<Calendar> keySet = map.keySet();
			for (Calendar key : keySet) {
//				System.out.println(key.getTime());
				HistoryDay history = map.get(key);
				if (history == null) {
					history = new HistoryDay();
					history.setDate(key);
				} 
				
				historyDateList.add(history);
			}
		}
		
		return historyDateList;
	}
	
	
	public static List<HistoryDay> handleToDay(List<HistoryHour> historyList) {
		Map<Calendar, HistoryDay> map = new LinkedHashMap<Calendar, HistoryDay>();
		
		for (HistoryHour historyHour : historyList) {
			if (historyHour != null) {
				Calendar cal_date = Calendar.getInstance();
				cal_date.setTimeInMillis(historyHour.getDate().getTimeInMillis());
				cal_date = CalendarHelper.setDayFormat(cal_date);
				
				HistoryDay historyDay = map.get(cal_date);
				if (historyDay == null) {
					historyDay = new HistoryDay();
					historyDay.setDate(cal_date);
					historyDay.setBurn(historyHour.getBurn());
					historyDay.setStep(historyHour.getStep());
					int sleepMove = historyHour.getSleepMove();
					if (sleepMove != 255) {
						historyDay.setSleepMove(sleepMove);
					}
					int sleepPattern = getSleepPattern_accordingSleepMove(sleepMove);
					if (sleepPattern == Global.TYPE_SLEEP_DEEP) {
						historyDay.setDeepSleepHour(1);
					} else if (sleepPattern == Global.TYPE_SLEEP_LIGHT) {
						historyDay.setLightSleepHour(1);
					}	
					
					map.put(cal_date, historyDay);
				} else {
					historyDay.setBurn(historyDay.getBurn() + historyHour.getBurn());
					historyDay.setStep(historyDay.getStep() + historyHour.getStep());
					int sleepMove = historyHour.getSleepMove();
					if (sleepMove != 255) {
						historyDay.setSleepMove(historyDay.getSleepMove() + sleepMove);
					}
					int sleepPattern = getSleepPattern_accordingSleepMove(sleepMove);
					if (sleepPattern == Global.TYPE_SLEEP_DEEP) {
						historyDay.setDeepSleepHour(historyDay.getDeepSleepHour() + 1);
					} else if (sleepPattern == Global.TYPE_SLEEP_LIGHT) {
						historyDay.setLightSleepHour(historyDay.getLightSleepHour() + 1);
					}	
					
					map.put(cal_date, historyDay);
				}
			}
		}
		
		List<HistoryDay> historyDateList = new ArrayList<HistoryDay>();
		Set<Calendar> keySet = map.keySet();
		for (Calendar key : keySet) {
			HistoryDay history = map.get(key);
			historyDateList.add(history);
		}
		
		return historyDateList;
	}
	
	
	
	public static List<HistoryMonth> handleToComplete12Month(Map<Calendar, HistoryMonth> month12Map, List<HistoryDay> historyList_day) {
		
		for (HistoryDay historyDate : historyList_day) {
			if (historyDate != null) {
				Calendar date_month = Calendar.getInstance();
				date_month.setTimeInMillis(historyDate.getDate().getTimeInMillis());
				date_month = CalendarHelper.setMonthFormat(date_month);
				
				// 同一个月份
				if (month12Map.containsKey(date_month)) {

					int sleepQuality = historyDate.getSleepQuality();
					
					HistoryMonth month = month12Map.get(date_month);
					if (month == null) {
						month = new HistoryMonth();
						month.setDate(date_month);
						month.setBurn(historyDate.getBurn());
						month.setStep(historyDate.getStep());
						month.setTotoalSleepQuality(sleepQuality);
						if (sleepQuality != 0) {
							month.setDayAmount(1);
						} else {
							month.setDayAmount(0);
						}
						month.setTotalDeepSleep(historyDate.getDeepSleepHour());
						month.setTotalLightSleep(historyDate.getLightSleepHour());
					} else {
						month.setBurn(month.getBurn() + historyDate.getBurn());
						month.setStep(month.getStep() + historyDate.getStep());
						month.setTotoalSleepQuality(month.getTotoalSleepQuality() + sleepQuality);
						if (sleepQuality != 0) {
							month.setDayAmount(month.getDayAmount() + 1);
						} else {
							month.setDayAmount(month.getDayAmount());
						}
						
						month.setTotalDeepSleep(month.getTotalDeepSleep() + historyDate.getDeepSleepHour());
						month.setTotalLightSleep(month.getTotalLightSleep() + historyDate.getLightSleepHour());
					}
					
					month12Map.put(date_month, month);
				}	
			}
		}
		
		List<HistoryMonth> historyMonthList = new ArrayList<HistoryMonth>();
		// 将完整的数据添加到新 list
		Set<Calendar> keySet = month12Map.keySet();
		for (Calendar key : keySet) {
//			System.out.println(key.getTime());
			HistoryMonth history = month12Map.get(key);
			if (history == null) {
				history = new HistoryMonth();
				history.setDate(key);
				history.setDayAmount(1);
			}
			historyMonthList.add(history);
		}
		
		return historyMonthList;
	}
	
	
	public static HistoryDay queryHistoryDay(Calendar cal, List<HistoryDay> historydayList) {
		if (cal != null && historydayList != null) {
			String queryDate = CalendarHelper.getYyyy_MM_dd(cal);
			
			for (HistoryDay historyDay : historydayList) {
				String date = CalendarHelper.getYyyy_MM_dd(historyDay.getDate());
				if (date.equals(queryDate)) {
					return historyDay;
				}
			}
		}
		
		return null;
	}
	
	
	public static void replaceByAudioData(List<HistoryDay> historyList_day, List<HistoryDay> historyList_audio) {
		for (HistoryDay historyDay : historyList_day) {
			Calendar date = historyDay.getDate();
			int step = historyDay.getStep();
			
			HistoryDay historyDay_audio = queryHistoryDay(date, historyList_audio);
			if (historyDay_audio != null) {
				int step_audio = historyDay_audio.getStep();
				if (step_audio >= step) {
					historyDay.setStep(step_audio);
					historyDay.setBurn(historyDay_audio.getBurn());
					historyDay.setDeepSleepHour(historyDay_audio.getDeepSleepHour());
					historyDay.setLightSleepHour(historyDay_audio.getLightSleepHour());
					historyDay.setSleepQuality(historyDay_audio.getSleepQuality());
				}
			}
		}
	}
	
	
	public static void replaceByAudioData_year(List<HistoryDay> historyList_day, List<HistoryDay> historyList_audio) {
		for (HistoryDay historyDay_audio : historyList_audio) {
			Calendar date = historyDay_audio.getDate();
			int step_audio = historyDay_audio.getStep();
			
			HistoryDay historyDay = queryHistoryDay(date, historyList_day);
			if (historyDay != null) {
				int step = historyDay.getStep();
				if (step_audio >= step) {
					historyDay.setStep(step_audio);
					historyDay.setBurn(historyDay_audio.getBurn());
					historyDay.setDeepSleepHour(historyDay_audio.getDeepSleepHour());
					historyDay.setLightSleepHour(historyDay_audio.getLightSleepHour());
					historyDay.setSleepQuality(historyDay_audio.getSleepQuality());
				}
			} else {
				historyList_day.add(historyDay_audio);
			}
		}
	}
	
	
	public static List<Integer> getASCsort(List<Integer> hourLabelList) {
		if (hourLabelList != null && hourLabelList.size() != 0) {
			Collections.sort(hourLabelList);
			
			List<Integer> list = new ArrayList<Integer>();
			for (int i = hourLabelList.size() - 1; i > -1; i--) {
				list.add(hourLabelList.get(i));
			}
			
			return list;
		}
		
		return new ArrayList<Integer>();
	}
	
	/**
	 * 根据小时数据整合成的日数据，计算睡眠质量
	 * @param historyList_weekly
	 */
	public static void calculateSleepQuality(List<HistoryDay> historyList_weekly) {
		if (historyList_weekly != null) {
			for (HistoryDay historyDate : historyList_weekly) {
				if (historyDate != null) {
					int sleepQuality = calculateSleepQuality(
							historyDate.getDeepSleepHour(), 
							historyDate.getDeepSleepHour() + historyDate.getLightSleepHour(),
							historyDate.getSleepMove());
					historyDate.setSleepQuality(sleepQuality);
				}
			}
		}
	}
	
	
	
	/**
	 * 根据睡眠动作次数确定睡眠类型
	 * 
	 * @param sleepMove
	 * @return
	 */
	public static int getSleepPattern_accordingSleepMove(int sleepMove) {
		if (sleepMove == 255) {
			// awake
			return Global.TYPE_SLEEP_AWAKE;
		} else if (sleepMove < 10) {
			// deep
			return Global.TYPE_SLEEP_DEEP;
		} else if (sleepMove >= 10 && sleepMove < 255) {
			// light
			return Global.TYPE_SLEEP_LIGHT;
		}

		return Global.TYPE_SLEEP_AWAKE;
	}
	
	
//	public static int getBPMType(int sys) {
////		if (sys <= 90 ) {
////			return Global.TYPE_BPM_TYPE_LOW;
////
////		} else if (sys <= 120) {
////			return Global.TYPE_BPM_TYPE_OPTI;
////
////		} else if (sys <= 130) {
////			return Global.TYPE_BPM_TYPE_NORM;
////
////		} else if (sys <= 160) {
////			return Global.TYPE_BPM_TYPE_MILD;
////
////		} else if (sys <= 180) {
////			return Global.TYPE_BPM_TYPE_MIDD;
////
////		} else {
////			return Global.TYPE_BPM_TYPE_HIGH;
////		}
//
//		if (sys < 60 ) {
//			return Global.TYPE_BPM_TYPE_LOW;
//
//		} else if (sys < 90) {
//			return Global.TYPE_BPM_TYPE_OPTI;
//
//		} else if (sys < 120) {
//			return Global.TYPE_BPM_TYPE_NORM;
//
//		} else if (sys < 140) {
//			return Global.TYPE_BPM_TYPE_MILD;
//
//		} else if (sys < 180) {
//			return Global.TYPE_BPM_TYPE_MIDD;
//
//		} else {
//			return Global.TYPE_BPM_TYPE_HIGH;
//		}
//	}
//
//	/**
//	 * 计算圆圈中间的等级
//	 * @param sys
//	 * @param dia
//	 * @return
//	 */
//	public static int getBPMTypeCenter(int sys, int dia) {
//
//		if (dia < 60 && sys < 90){
//			return Global.TYPE_BPM_TYPE_LOW;
//		} else if (dia > 90 || sys > 140) {
//			return Global.TYPE_BPM_TYPE_HIGH;
//		}
//
//		return Global.TYPE_BPM_TYPE_NORM;
//
//	}

	/**
	 * 计算等级（7级）
	 * @param sys
	 * @param dia
	 * @return
	 */
	public static int getBPMLevel(int sys, int dia) {

		int levelDia;
		int levelSys;

		if (dia < 60){
			levelDia = Global.TYPE_BPM_LEVEL_1;//Low  蓝色
		} else if (dia < 80) {
			levelDia = Global.TYPE_BPM_LEVEL_2;//Opti  蓝色
		} else if (dia < 85) {
			levelDia = Global.TYPE_BPM_LEVEL_3;//Norm  蓝色
		} else if (dia < 90) {
			levelDia = Global.TYPE_BPM_LEVEL_4;//High-Normal  蓝色
		} else if (dia < 100) {
			levelDia = Global.TYPE_BPM_LEVEL_5;//Mild Hypertension  橙色
		} else if (dia < 110) {
			levelDia = Global.TYPE_BPM_LEVEL_6;//Moderate Hypertension  橙色
		} else {
			levelDia = Global.TYPE_BPM_LEVEL_7;//Severe Hypertension  红色
		}

		if (sys < 90){
			levelSys = Global.TYPE_BPM_LEVEL_1;//Low  蓝色
		} else if (sys < 120) {
			levelSys = Global.TYPE_BPM_LEVEL_2;//Opti  蓝色
		} else if (sys < 130) {
			levelSys = Global.TYPE_BPM_LEVEL_3;//Norm  蓝色
		} else if (sys < 140) {
			levelSys = Global.TYPE_BPM_LEVEL_4;//High-Normal  蓝色
		} else if (sys < 160) {
			levelSys = Global.TYPE_BPM_LEVEL_5;//Mild Hypertension  橙色
		} else if (sys < 180) {
			levelSys = Global.TYPE_BPM_LEVEL_6;//Moderate Hypertension  橙色
		} else {
			levelSys = Global.TYPE_BPM_LEVEL_7;//Severe Hypertension  红色
		}

//		if (dia < 60 && sys < 90){
//			return Global.TYPE_BPM_LEVEL_1;//Low  蓝色
//		} else if (dia >= 60 && dia < 80 && sys >= 90 && sys < 120) {
//			return Global.TYPE_BPM_LEVEL_2;//Opti  蓝色
//		} else if (dia >= 80 && dia < 85 && sys >= 120 && sys < 130) {
//			return Global.TYPE_BPM_LEVEL_3;//Norm  蓝色
//		} else if (dia >= 85 && dia < 90 && sys >= 130 && sys < 140) {
//			return Global.TYPE_BPM_LEVEL_4;//High-Normal  蓝色
//		} else if (dia >= 90 && dia < 100 && sys >= 140 && sys < 160) {
//			return Global.TYPE_BPM_LEVEL_5;//Mild Hypertension  橙色
//		} else if (dia >= 100 && dia < 110 && sys >= 160 && sys < 180) {
//			return Global.TYPE_BPM_LEVEL_6;//Moderate Hypertension  橙色
//		} else if (dia >= 110 && sys >= 180) {
//			return Global.TYPE_BPM_LEVEL_7;//Severe Hypertension  红色
//		}
		return levelDia > levelSys ? levelDia : levelSys;
	}

	/**
	 * 计算等级（3级）
	 * @return
	 */
	public static int getBPMLevelColor(int level) {
		switch (level) {
			case Global.TYPE_BPM_LEVEL_1:
			case Global.TYPE_BPM_LEVEL_2:
			case Global.TYPE_BPM_LEVEL_3:
			case Global.TYPE_BPM_LEVEL_4:return Global.TYPE_BPM_LEVEL_BLUE;
			case Global.TYPE_BPM_LEVEL_5:
			case Global.TYPE_BPM_LEVEL_6:return Global.TYPE_BPM_LEVEL_ORANGE;
			case Global.TYPE_BPM_LEVEL_7:return Global.TYPE_BPM_LEVEL_RED;
			default:return -1;
		}
	}

	public static String buildBPMLevelText(int level) {
		switch (level) {
			case Global.TYPE_BPM_LEVEL_1:return ContextHelper.getInstance().getString(R.string.L1);
			case Global.TYPE_BPM_LEVEL_2:return ContextHelper.getInstance().getString(R.string.L2);
			case Global.TYPE_BPM_LEVEL_3:return ContextHelper.getInstance().getString(R.string.L3);
			case Global.TYPE_BPM_LEVEL_4:return ContextHelper.getInstance().getString(R.string.L4);
			case Global.TYPE_BPM_LEVEL_5:return ContextHelper.getInstance().getString(R.string.L5);
			case Global.TYPE_BPM_LEVEL_6:return ContextHelper.getInstance().getString(R.string.L6);
			case Global.TYPE_BPM_LEVEL_7:return ContextHelper.getInstance().getString(R.string.L7);
			default:return "ERROR";
		}
	}
}
