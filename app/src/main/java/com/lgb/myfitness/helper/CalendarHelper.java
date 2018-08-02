package com.lgb.myfitness.helper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import com.lgb.myfitness.been.HistoryDay;
import com.lgb.myfitness.been.HistoryHour;
import com.lgb.myfitness.been.HistoryMonth;
import com.lgb.myfitness.been.Scale;
import com.lgb.myfitness.global.Global;

import android.widget.TextView;

public class CalendarHelper {
	
	
	/**
	 * 判断是否同一天
	 * @param cal1
	 * @param cal2
	 * @return
	 */
	public static boolean isSameDate(Calendar cal1, Calendar cal2) {
		if (cal1 != null && cal2 != null) {
			String date1 = Global.sdf_2.format(cal1.getTime());
			String date2 = Global.sdf_2.format(cal2.getTime());
			
			if (date1.equals(date2)) {
				return true;
			}
		}
		
		return false;
	}
	
	
	/**
	 * 获取2001-01-01 00:00:00
	 * @return
	 */
	public static Calendar get20010101Datetime() {
		Calendar cal_ = Calendar.getInstance();
		cal_.set(Calendar.YEAR, 2000);
		cal_.set(Calendar.MONTH, Calendar.JANUARY);
		cal_.set(Calendar.DATE, 1);
		cal_.set(Calendar.HOUR_OF_DAY, 0);
		cal_.set(Calendar.MINUTE, 0);
		cal_.set(Calendar.SECOND, 0);
		cal_.set(Calendar.MILLISECOND, 0);
		
		return cal_;
	}
	
	
	
	
	/**
	 * 日期格式化 (yyyy.mm.dd)
	 * @param cal
	 * @return
	 */
	public static Calendar setDayFormat(Calendar cal) {
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		return cal;
	}
	
	/**
	 * 日期格式化(yyyy.mm.dd HH)
	 * @param cal
	 * @return
	 */
	public static Calendar setHourFormat(Calendar cal) {
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		return cal;
	}
	
	/**
	 * 日期格式化(yyyy.mm.01)
	 * @param cal
	 * @return
	 */
	public static Calendar setMonthFormat(Calendar cal) {
		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		return cal;
	}
	
	
	/**
	 * 获取今天日期
	 * @return
	 */
	public static Calendar getToday() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		return cal;	
	}
	
	
	/**
	 * 获取昨天的日期
	 * @param cal
	 * @return
	 */
	public static Calendar getYesterday(Calendar cal) {
//		Calendar result = Calendar.getInstance();
//		result.setTimeInMillis(cal.getTimeInMillis());
		
		Calendar result = minADay(cal);
		
		return result;
	}
	
	/**
	 * 获取前天的日期
	 * @param cal
	 * @return
	 */
	public static Calendar getDateBeforeYesterday(Calendar cal) {
//		Calendar result = Calendar.getInstance();
//		result.setTimeInMillis(cal.getTimeInMillis());
		
		Calendar result = minADay(cal);
		result = minADay(result);
		return result;
	}
	
	
	/**
	 * 添加一天
	 * @param cal
	 * @return
	 */
	public static Calendar addADay(Calendar cal) {
		Calendar result = Calendar.getInstance();
		result.setTimeInMillis(cal.getTimeInMillis());
		
		result.set(Calendar.DATE, cal.get(Calendar.DATE) + 1);
		
		return result;
	}
	
	
	/**
	 * 添加X天
	 * @param cal
	 * @param dayAdd
	 * @return
	 */
	public static Calendar addXDay(Calendar cal, int dayAdd) {
		Calendar result = Calendar.getInstance();
		result.setTimeInMillis(cal.getTimeInMillis());
		
		result.set(Calendar.DATE, cal.get(Calendar.DATE) + dayAdd);
		
		return result;
	}
	
	
	/**
	 * 
	 * @param cal
	 * @return
	 */
	public static Calendar minADay(Calendar cal) {
		Calendar result = Calendar.getInstance();
		result.setTimeInMillis(cal.getTimeInMillis());
		
		result.set(Calendar.DATE, cal.get(Calendar.DATE) - 1);
		
		return result;
	}
	
	
	public static Calendar addAWeek(Calendar cal) {
		Calendar result = Calendar.getInstance();
		result.setTimeInMillis(cal.getTimeInMillis());
		
		result.set(Calendar.WEEK_OF_MONTH, cal.get(Calendar.WEEK_OF_MONTH) + 1);
		
		return result;
	}
	
	
	public static Calendar minAWeek(Calendar cal) {
		Calendar result = Calendar.getInstance();
		result.setTimeInMillis(cal.getTimeInMillis());
		
		result.set(Calendar.WEEK_OF_MONTH, cal.get(Calendar.WEEK_OF_MONTH) - 1);
		
		return result;
	}
	
	
	public static Calendar addAMonth(Calendar cal) {
		Calendar result = Calendar.getInstance();
		result.setTimeInMillis(cal.getTimeInMillis());
		
		result.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1);
		
		return result;
	}
	
	
	public static Calendar addXMonth(Calendar cal, int monthAdd) {
		Calendar result = Calendar.getInstance();
		result.setTimeInMillis(cal.getTimeInMillis());
		
		result.set(Calendar.MONTH, cal.get(Calendar.MONTH) + monthAdd);
		
		return result;
	}
	
	
	public static Calendar minAMonth(Calendar cal) {
		Calendar result = Calendar.getInstance();
		result.setTimeInMillis(cal.getTimeInMillis());
		
		result.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1);
		
		return result;
	}
	
	
	public static Calendar addAYear(Calendar cal) {
		Calendar result = Calendar.getInstance();
		result.setTimeInMillis(cal.getTimeInMillis());
		
		result.set(Calendar.YEAR, cal.get(Calendar.YEAR) + 1);
		
		return result;
	}
	
	
	public static Calendar minAYear(Calendar cal) {
		Calendar result = Calendar.getInstance();
		result.setTimeInMillis(cal.getTimeInMillis());
		
		result.set(Calendar.YEAR, cal.get(Calendar.YEAR) - 1);
		
		return result;
	}
	
	
	/**
	 * 
	 * @param cal
	 * @return
	 */
	public static Calendar addAnHour(Calendar cal) {
		Calendar result = Calendar.getInstance();
		result.setTimeInMillis(cal.getTimeInMillis());
		
		result.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY) + 1);
		
		return result;
	}
	
	
	public static Calendar addXHour(Calendar cal, int hourAdd) {
		Calendar result = Calendar.getInstance();
		result.setTimeInMillis(cal.getTimeInMillis());
		
		result.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY) + hourAdd);
		
		return result;
	}
	
	
	/**
	 * 
	 * @param cal
	 * @param xHourBefore
	 * @return
	 */
	public static Calendar minXHour(Calendar cal, int xHourBefore) {
		Calendar result = Calendar.getInstance();
		result.setTimeInMillis(cal.getTimeInMillis());
		
		result.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY) - xHourBefore);
		
		return result;
	}
	

	
//	/**
//	 * 昨天与明天
//	 * @param today
//	 */
//	public static Calendar[] getYesterdayToTomorrow(Calendar today) {
//		
//		// 昨天日期
//		Calendar yesterday = minADay(today);
//		yesterday = setDayFormat(yesterday);
//		
//		// 明天日期
//		Calendar tomorrow  = addADay(today);
//		tomorrow = setDayFormat(tomorrow);		
//		
//		Calendar[] calArray = new Calendar[2];
//		calArray[0] = yesterday;
//		calArray[1] = tomorrow;
//		
//		return calArray;
//	}
	
	/**
	 * 当天到明天
	 * @param today
	 * @return
	 */
	public static Calendar[] getTodayToTomorrow(Calendar today) {
		Calendar date_first = Calendar.getInstance();
		date_first.setTimeInMillis(today.getTimeInMillis());
		date_first = setDayFormat(date_first);
		
		Calendar date_second  = addADay(today);
		date_second = setDayFormat(date_second);	
		
		Calendar[] calArray = new Calendar[2];
		calArray[0] = date_first;
		calArray[1] = date_second;
		
		return calArray;
	}
	
	/**
	 * 上周到明天
	 * @param today
	 * @return
	 */
	public static Calendar[] getLastWeekToTomorrow(Calendar today) {
	
		Calendar lastWeek = Calendar.getInstance();
		lastWeek.setTimeInMillis(today.getTimeInMillis());
		lastWeek.set(Calendar.DATE, lastWeek.get(Calendar.DATE) - 6);
		lastWeek = setDayFormat(lastWeek);
		
		Calendar tomorrow = addADay(today);
		tomorrow = setDayFormat(tomorrow);		
		
		Calendar[] calArray = new Calendar[2];
		calArray[0] = lastWeek;
		calArray[1] = tomorrow;
		
		return calArray;
	}
	
	
	/**
	 * 上个月到明天
	 * @param today
	 * @return
	 */
	public static Calendar[] getLastMonthToTomorrow(Calendar today) {
		
		Calendar lastMonth = Calendar.getInstance();
		lastMonth.setTimeInMillis(today.getTimeInMillis());
		lastMonth.set(Calendar.DATE, lastMonth.get(Calendar.DATE) - 30);
		lastMonth = setDayFormat(lastMonth);
		
		Calendar tomorrow = addADay(today);
		tomorrow = setDayFormat(tomorrow);		
		
		Calendar[] calArray = new Calendar[2];
		calArray[0] = lastMonth;
		calArray[1] = tomorrow;
		
		return calArray;		
	}
	
	/**
	 * 30天
	 * @param today
	 * @return
	 */
	public static Calendar[] getLast30dayToTomorrow(Calendar today) {
		
		Calendar lastMonth = Calendar.getInstance();
		lastMonth.setTimeInMillis(today.getTimeInMillis());
		lastMonth.set(Calendar.DATE, lastMonth.get(Calendar.DATE) - 29);
		lastMonth = setDayFormat(lastMonth);
		
		Calendar tomorrow = addADay(today);
		tomorrow = setDayFormat(tomorrow);		
		
		Calendar[] calArray = new Calendar[2];
		calArray[0] = lastMonth;
		calArray[1] = tomorrow;
		
		return calArray;		
	}
	
	
	/**
	 * 上一年到明天
	 * @param today
	 * @return
	 */
	public static Calendar[] getLastYearToTomorrow(Calendar today) {
		
		Calendar lastYear = Calendar.getInstance();
		lastYear.setTimeInMillis(today.getTimeInMillis());
		lastYear.set(Calendar.DATE, lastYear.get(Calendar.DATE) - 364);
		lastYear = setDayFormat(lastYear);
		
		Calendar tomorrow  = addADay(today);
		tomorrow = setDayFormat(tomorrow);		
		
		Calendar[] calArray = new Calendar[2];
		calArray[0] = lastYear;
		calArray[1] = tomorrow;
		
		return calArray;	
	}
	
	/**
	 * 30天前
	 * @param today
	 * @return
	 */
	public static Calendar getLastMonth(Calendar today) {
		
		Calendar lastMonth = Calendar.getInstance();
		lastMonth.setTimeInMillis(today.getTimeInMillis());
		lastMonth.set(Calendar.DATE, lastMonth.get(Calendar.DATE) - 29);
		lastMonth = setDayFormat(lastMonth);
		return lastMonth;		
	}
	
	
	/**
	 * 
	 * @param today
	 * @return
	 */
	public static Map<Calendar, HistoryMonth> get12MonthMap(Calendar today) {
		Map<Calendar, HistoryMonth> map = new LinkedHashMap<Calendar, HistoryMonth>();
		
		if (today != null) {
			for (int i = 11; i > -1; i--) {
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(today.getTimeInMillis());
				cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - i);
				cal = setMonthFormat(cal);
				
				map.put(cal, null);
			}
		}
		
		
		return map;
	}
	
	
	/**
	 * 
	 * @param calArray
	 * @return
	 */
	public static Map<Calendar, HistoryDay> getDayMap_wb013(Calendar[] calArray) {
		
		Map<Calendar, HistoryDay> map = new LinkedHashMap<Calendar, HistoryDay>();
		
		if (calArray != null && calArray.length == 2) {
			Calendar begin = calArray[0];
			Calendar end = calArray[1];

			Calendar start = Calendar.getInstance();
			start.setTimeInMillis(begin.getTimeInMillis());
			start = setDayFormat(start);
			
			for (int i = 0; ; i++) {
				Calendar cal = addXDay(start, i);
				
				if (cal.equals(end)) {
					break;
				} else {
					map.put(cal, null);
				}
			}
			
			
		}
		
		return map;
	}
	
	/**
	 * 
	 * @param calArray
	 * @return
	 */
	public static Map<Calendar, Scale> getDayMap_scale(Calendar[] calArray) {
		
		Map<Calendar, Scale> map = new LinkedHashMap<Calendar, Scale>();
		
		if (calArray != null && calArray.length == 2) {
			Calendar begin = calArray[0];
			Calendar end = calArray[1];

			Calendar start = Calendar.getInstance();
			start.setTimeInMillis(begin.getTimeInMillis());
			start = setDayFormat(start);
			
			for (int i = 0; ; i++) {
				Calendar cal = addXDay(start, i);
				
				if (cal.equals(end)) {
					break;
				} else {
					map.put(cal, null);
				}
			}
			
			
		}
		
		return map;
	}
	
	
	/**
	 * 
	 * 当天00:00到23:00
	 * @param today
	 * @return
	 */
	public static Map<Calendar, HistoryHour> get24HourMap_wb013(Calendar today) {
		Map<Calendar, HistoryHour> map = new LinkedHashMap<Calendar, HistoryHour>();
		
		if (today != null) {
			// 起始点为00:00
			Calendar begin = Calendar.getInstance();
			begin.setTimeInMillis(today.getTimeInMillis());
			begin = setDayFormat(begin);
			
			for (int i = 0; i < 24; i++) {
				Calendar cal = addXHour(begin, i);
				map.put(cal, null);
			}
		}
		
		return map;
	}
	
	
	/**
	 * 当前小时往前数72个小时
	 * @return
	 */
	public static Map<Integer, Calendar> get72HourMap() {
		Map<Integer, Calendar> map = new LinkedHashMap<Integer, Calendar>();
		
		Calendar now = Calendar.getInstance();
		now = setHourFormat(now);
		
		for (int i = 0; i < 72; i++) {
			map.put(i, minXHour(now, i));
		}
		return map;
	}
	
	
	public static String getYyyy_MM_dd(Calendar cal) {
		if (cal != null) {
//			int year = cal.get(Calendar.YEAR);
//			int month = cal.get(Calendar.MONTH) + 1;
//			int day = cal.get(Calendar.DATE);
//			
//			return Global.df_int_4.format(year) + "-" + Global.df_1.format(month) + "-" + Global.df_1.format(day);
		
			return Global.sdf_2.format(cal.getTime());
		}
		
		return "2000-01-01";
	}
	
	public static String get_dd_MM(Calendar cal) {
		if (cal != null) {
//			int year = cal.get(Calendar.YEAR);
//			int month = cal.get(Calendar.MONTH) + 1;
//			int day = cal.get(Calendar.DATE);
//			
//			return Global.df_int_4.format(year) + "-" + Global.df_1.format(month) + "-" + Global.df_1.format(day);
		
			return Global.sdf_2_1.format(cal.getTime());
		}
		
		return "01-01";
	}
	
	
	public static String getYyyy_MM_dd_HH(Calendar cal) {
		if (cal != null) {
//			int year = cal.get(Calendar.YEAR);
//			int month = cal.get(Calendar.MONTH) + 1;
//			int day = cal.get(Calendar.DATE);
//			int hour = cal.get(Calendar.HOUR_OF_DAY);
//			
//			return Global.df_int_4.format(year) + "-" + Global.df_1.format(month) + "-" + Global.df_1.format(day)
//					+ " " + Global.df_1.format(hour);
			
			return Global.sdf_3.format(cal.getTime());
		}
		
		return "2000-01-01 00";
	}
	
	
	public static String getYyyy_MM_dd_HH_mm_ss(Calendar cal) {
		if (cal != null) {

			return Global.sdf_4.format(cal.getTime());
		}
		
		return "2000-01-01 00:00:00";
	}
	
	
	public static void setDateInformation(Calendar cal, TextView text_date, String locale) {
		SimpleDateFormat sdf = null;
		if (locale.equals("fr")) {
			sdf = new SimpleDateFormat("EEE dd MMM", Locale.FRENCH);
		} else if (locale.equals("da")) {
			sdf = new SimpleDateFormat("EEE dd MMM", new Locale("da"));
		} else if (locale.equals("nl")) {
			sdf = new SimpleDateFormat("EEE dd MMM", new Locale("nl"));
		} else if (locale.equals("de")) {
			sdf = new SimpleDateFormat("EEE dd MMM", Locale.GERMANY);
		} else if (locale.equals("el")) {
			sdf = new SimpleDateFormat("EEE dd MMM", new Locale("el"));
		} else if (locale.equals("it")) {
			sdf = new SimpleDateFormat("EEE dd MMM", Locale.ITALIAN);
		} else if (locale.equals("ru")) {
			sdf = new SimpleDateFormat("EEE dd MMM", new Locale("ru"));
		} else if (locale.equals("es")) {
			sdf = new SimpleDateFormat("EEE dd MMM", new Locale("es"));
		}  else if (locale.equals("sv")) {
			sdf = new SimpleDateFormat("EEE dd MMM", new Locale("sv"));
		}
		else {
			sdf = new SimpleDateFormat("EEE dd MMM", Locale.ENGLISH);
		}
		if (sdf != null) {
			text_date.setText(sdf.format(cal.getTime()));
		}
	}
}
