package com.lgb.myfitness.database;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import com.lgb.myfitness.been.HistoryDay;
import com.lgb.myfitness.been.HistoryHour;
import com.lgb.myfitness.been.ScreenData;
import com.lgb.myfitness.global.Global;
import com.lgb.myfitness.helper.CalendarHelper;

import android.content.Context;
import android.database.Cursor;

public class DatabaseProvider_wb013 {
	
	public static void insertScreen(Context context, ScreenData screen, int profileID, int deviceID) {
		if (context != null && screen != null) {
			DatabaseAdapter_wb013 databaseAdapter = new DatabaseAdapter_wb013(context);
			databaseAdapter.openDatabase();

			databaseAdapter.insert_screen(
					screen.getDate(), 
					screen.getStep(), 
					screen.getDistance(), 
					screen.getBurn(), 
					screen.getGoal(), 
					screen.getBattaryLevel(), 
					screen.getUint(),
					profileID, deviceID);

			databaseAdapter.closeDatabase();
		}
	}
	
	
	public static void updateScreen(Context context, ScreenData screen, int profileID, int deviceID) {
		if (context != null && screen != null) {
			DatabaseAdapter_wb013 databaseAdapter = new DatabaseAdapter_wb013(context);
			databaseAdapter.openDatabase();

			databaseAdapter.update_screen(
					screen.getDate(), 
					screen.getStep(), 
					screen.getDistance(), 
					screen.getBurn(), 
					screen.getGoal(), 
					screen.getBattaryLevel(), 
					screen.getUint(),
					profileID, deviceID);

			databaseAdapter.closeDatabase();
		}
	}
	
	
	public static ScreenData queryScreen(Context context, Calendar date, int profileID, int deviceID) {
		ScreenData screen = null;
		
		DatabaseAdapter_wb013 databaseAdapter = new DatabaseAdapter_wb013(context);
		databaseAdapter.openDatabase();
		Cursor cursor = databaseAdapter.query_screen(date, profileID, deviceID);
		if (cursor.moveToFirst()){
			screen = new ScreenData();
			Date _date = new Date();
			try {
				_date = Global.sdf_2.parse(cursor.getString(0));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			Calendar _cal = Calendar.getInstance();
			_cal.setTime(_date);
			screen.setDate(_cal);
			screen.setStep(cursor.getInt(1));
			screen.setDistance(cursor.getDouble(2));
			screen.setBurn(cursor.getInt(3));
			screen.setGoal(cursor.getInt(4));
			screen.setBattaryLevel(cursor.getInt(5));
			screen.setUint(cursor.getInt(6));
			
			if (cursor != null) {
				cursor.close();
			}
			databaseAdapter.closeDatabase();
			
			return screen;
		}
		
		if (cursor != null) {
			cursor.close();
		}
		databaseAdapter.closeDatabase();
		
		return screen;
	}
	
	
	public static void deleteScreen(Context context, Calendar date, int profileID, int deviceID) {
		if (date != null) {
			DatabaseAdapter_wb013 databaseAdapter = new DatabaseAdapter_wb013(context);
			databaseAdapter.openDatabase();
			
			databaseAdapter.delete_screen(date, profileID, deviceID);
			
			databaseAdapter.closeDatabase();
		}
	}
	
	/**
	 * 插入日历史数据
	 * @param context
	 * @param history
	 */
	public static void insertHistoryDate(Context context, int profileID, HistoryDay history) {
		if (history != null) {
			DatabaseAdapter_wb013 databaseAdapter = new DatabaseAdapter_wb013(context);
			databaseAdapter.openDatabase();

			databaseAdapter.insert_history_day(
					profileID, 
					history.getDate(), 
					history.getStep(), 
					history.getBurn(), 
					history.getSleepQuality(), 
					history.getDeepSleepHour(), 
					history.getLightSleepHour(), 
					history.getActiveHour());
			
			databaseAdapter.closeDatabase();
		}
	}
	
	/**
	 * 更新历史日数据
	 * @param context
	 * @param date
	 * @param step
	 * @param cal
	 * @param sleep
	 */
	public static void updateHistoryDate(Context context, int profileID, HistoryDay history) {
		if (history != null) {
			DatabaseAdapter_wb013 databaseAdapter = new DatabaseAdapter_wb013(context);
			databaseAdapter.openDatabase();
			
			databaseAdapter.update_history_day(
					profileID, 
					history.getDate(), 
					history.getStep(), 
					history.getBurn(), 
					history.getSleepQuality(), 
					history.getDeepSleepHour(), 
					history.getLightSleepHour(), 
					history.getActiveHour());

			databaseAdapter.closeDatabase();
		}
	}
	
	
	/**
	 * 删除一天的历史日数据
	 * @param context
	 * @param cal
	 */
	public static void deleteHistoryDate(Context context, int profileID, Calendar cal) {
		if (cal != null) {

			DatabaseAdapter_wb013 databaseAdapter = new DatabaseAdapter_wb013(context);
			databaseAdapter.openDatabase();
			
			databaseAdapter.delete_history_day(profileID, cal);
			
			databaseAdapter.closeDatabase();
			
		}
	}
	
	
	/**
	 * 查询一个日期的历史日数据
	 * @param context
	 * @param date
	 * @return
	 */
	public static HistoryDay queryHistoryDate(Context context,int profileID,  Calendar date) {
		
		if (context != null && date != null) {
			
			DatabaseAdapter_wb013 databaseAdapter = new DatabaseAdapter_wb013(context);
			// 打开数据库
			databaseAdapter.openDatabase();
			
			Cursor cursor = databaseAdapter.query_history_day(profileID, date);
			if (cursor.moveToFirst()){
				
				HistoryDay history = new HistoryDay();
				Date _date = new Date();
				try {
					_date = Global.sdf_2.parse(cursor.getString(0));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				Calendar _cal = Calendar.getInstance();
				_cal.setTime(_date);
				
				history.setDate(_cal);
				history.setStep(cursor.getInt(1));
				history.setBurn(cursor.getDouble(2));
				history.setSleepQuality(cursor.getInt(3));		
				history.setDeepSleepHour(cursor.getInt(4));
				history.setLightSleepHour(cursor.getInt(5));
				history.setActiveHour(cursor.getInt(6));
					
				if (cursor != null) {
					cursor.close();
				}
				databaseAdapter.closeDatabase();
				return history;		
			}
			
			if (cursor != null) {
				cursor.close();
			}
			databaseAdapter.closeDatabase();
		}
		
		
		return null;
	}
	
	/**
	 * 查询一个时间段的历史日数据
	 * @param context
	 * @param begin
	 * @param end
	 * @return
	 */
	public static List<HistoryDay> queryHistoryDate(Context context, int profileID, Calendar begin, Calendar end) {
		
		List<HistoryDay> historyList = new ArrayList<HistoryDay>();
		
		if (context != null && begin != null && end != null) {
			
			DatabaseAdapter_wb013 databaseAdapter = new DatabaseAdapter_wb013(context);
			// 打开数据库
			databaseAdapter.openDatabase();
			Cursor cursor = databaseAdapter.query_history_day(profileID, begin, end);
			if (cursor.moveToFirst()){
				do {
					HistoryDay history = new HistoryDay();
					Date _date = new Date();
					try {
						_date = Global.sdf_2.parse(cursor.getString(0));
					} catch (ParseException e) {
						e.printStackTrace();
					}
					Calendar _cal = Calendar.getInstance();
					_cal.setTime(_date);
					
					history.setDate(_cal);
					history.setStep(cursor.getInt(1));
					history.setBurn(cursor.getDouble(2));
					history.setSleepQuality(cursor.getInt(3));	
					history.setDeepSleepHour(cursor.getInt(4));
					history.setLightSleepHour(cursor.getInt(5));
					history.setActiveHour(cursor.getInt(6));
					
					historyList.add(history);
				} while (cursor.moveToNext());
			}
			
			if (cursor != null) {
				cursor.close();
			}
			databaseAdapter.closeDatabase();
		}
		
		return historyList;
	}
	
	
	/**
	 * 插入小时历史数据
	 * @param context
	 * @param history
	 */
	public static void insertHistoryHour(Context context, int profileID, int deviceID, HistoryHour history) {
		if (history != null) {
			DatabaseAdapter_wb013 databaseAdapter = new DatabaseAdapter_wb013(context);
			databaseAdapter.openDatabase();

			databaseAdapter.insert_history_hour(
					profileID, 
					deviceID, 
					history.getDate(), 
					history.getStep(), 
					history.getBurn(), 
					history.getSleepMove());
			
			databaseAdapter.closeDatabase();
		}
	}
	
	/**
	 * 更新小时历史数据
	 * @param context
	 * @param history
	 */
	public static void updateHistoryHour(Context context,int profileID, int deviceID, HistoryHour history) {
		if (history != null) {
			DatabaseAdapter_wb013 databaseAdapter = new DatabaseAdapter_wb013(context);
			databaseAdapter.openDatabase();
			
			databaseAdapter.update_history_hour(
					profileID, 
					deviceID, 
					history.getDate(), 
					history.getStep(), 
					history.getBurn(), 
					history.getSleepMove());

			databaseAdapter.closeDatabase();
		}
	}
	
	
	/**
	 * 查询一个日期的历史小时数据
	 * @param context
	 * @param date
	 * @return
	 */
	public static HistoryHour queryHistoryHour(Context context, int profileID, int deviceID, Calendar date) {
		
		if (context != null && date != null) {
			
			DatabaseAdapter_wb013 databaseAdapter = new DatabaseAdapter_wb013(context);
			// 打开数据库
			databaseAdapter.openDatabase();
			
			Cursor cursor = databaseAdapter.query_history_hour(profileID, deviceID, date);
			if (cursor.moveToFirst()){
				
				HistoryHour history = new HistoryHour();
				Date _date = new Date();
				try {
					_date = Global.sdf_3.parse(cursor.getString(cursor.getColumnIndex(DatabaseAdapter_wb013.KEY_DATETIME)));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				Calendar _cal = Calendar.getInstance();
				_cal.setTime(_date);
				
				history.setDate(_cal);
				history.setStep(cursor.getInt(cursor.getColumnIndex(DatabaseAdapter_wb013.KEY_STEP)));
				history.setBurn(cursor.getDouble(cursor.getColumnIndex(DatabaseAdapter_wb013.KEY_BURN)));
				history.setSleepMove(cursor.getInt(cursor.getColumnIndex(DatabaseAdapter_wb013.KEY_SLEEP_MOVE)));		
					
				if (cursor != null) {
					cursor.close();
				}
				databaseAdapter.closeDatabase();
				return history;		
			}
			
			if (cursor != null) {
				cursor.close();
			}
			
			databaseAdapter.closeDatabase();
		}
		
		
		return null;
	}
	
	
	/**
	 * 查询最新一个小时的HistoryHour数据
	 * @param context
	 * @param profileID
	 * @param deviceID
	 * @return
	 */
	public static HistoryHour queryHistoryHour(Context context, int profileID, int deviceID) {
		
		if (context != null) {
		
			DatabaseAdapter_wb013 databaseAdapter = new DatabaseAdapter_wb013(context); 
			databaseAdapter.openDatabase();
			
			Cursor cursor = databaseAdapter.query_history_hour_newest(profileID, deviceID);
			if (cursor.moveToFirst()){
				
				HistoryHour history = new HistoryHour();
				Date _date = new Date();
				try {
					_date = Global.sdf_3.parse(cursor.getString(cursor.getColumnIndex(DatabaseAdapter_wb013.KEY_DATETIME)));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				Calendar _cal = Calendar.getInstance();
				_cal.setTime(_date);
				
				history.setDate(_cal);
				history.setStep(cursor.getInt(cursor.getColumnIndex(DatabaseAdapter_wb013.KEY_STEP)));
				history.setBurn(cursor.getInt(cursor.getColumnIndex(DatabaseAdapter_wb013.KEY_BURN)));
				history.setSleepMove(cursor.getInt(cursor.getColumnIndex(DatabaseAdapter_wb013.KEY_SLEEP_MOVE)));		
					
				if (cursor != null) {
					cursor.close();
				}
				databaseAdapter.closeDatabase();
				return history;		
			}
			
			if (cursor != null) {
				cursor.close();
			}
			databaseAdapter.closeDatabase();
		}
		
		
		return null;
	}
	
	
	/**
	 * 查询一个时间段的历史日数据
	 * @param context
	 * @param begin
	 * @param end
	 * @return
	 */
	public static List<HistoryHour> queryHistoryHour( Context context, int profileID, int deviceID, Calendar begin, Calendar end) {
		
		List<HistoryHour> historyList = new ArrayList<HistoryHour>();
		
		if (context != null && begin != null && end != null) {
			
//			begin = CalendarHelper.setDayFormat(begin);
//			end = CalendarHelper.setDayFormat(end);
			
			DatabaseAdapter_wb013 databaseAdapter = new DatabaseAdapter_wb013(context);
			// 打开数据库
			databaseAdapter.openDatabase();
			Cursor cursor = databaseAdapter.query_history_hour(profileID, deviceID, begin, end);
			if (cursor.moveToFirst()){
				do {
					HistoryHour history = new HistoryHour();
					Date _date = new Date();
					try {
						_date = Global.sdf_3.parse(cursor.getString(cursor.getColumnIndex(DatabaseAdapter_wb013.KEY_DATETIME)));
					} catch (ParseException e) {
						e.printStackTrace();
					}
					Calendar _cal = Calendar.getInstance();
					_cal.setTime(_date);
					
					history.setDate(_cal);
					history.setStep(cursor.getInt(cursor.getColumnIndex(DatabaseAdapter_wb013.KEY_STEP)));
					history.setBurn(cursor.getDouble(cursor.getColumnIndex(DatabaseAdapter_wb013.KEY_BURN)));
					history.setSleepMove(cursor.getInt(cursor.getColumnIndex(DatabaseAdapter_wb013.KEY_SLEEP_MOVE)));	
					
					historyList.add(history);
				} while (cursor.moveToNext());
			}
			
			if (cursor != null) {
				cursor.close();
			}
			databaseAdapter.closeDatabase();
		}
		
		return historyList;
	}

	
	public static void insertResetTime(Context context, Calendar date, int profileID, int deviceID ) {
		if (context != null && date != null) {
			DatabaseAdapter_wb013 databaseAdapter = new DatabaseAdapter_wb013(context);
			databaseAdapter.openDatabase();

			databaseAdapter.insert_reset_time(date, profileID, deviceID);

			databaseAdapter.closeDatabase();
		}
	}
	
	
	public static void updateResetTime(Context context, Calendar date, int profileID, int deviceID) {
		if (context != null && date != null) {
			DatabaseAdapter_wb013 databaseAdapter = new DatabaseAdapter_wb013(context);
			databaseAdapter.openDatabase();

			databaseAdapter.update_reset_time(date, profileID, deviceID);

			databaseAdapter.closeDatabase();
		}
	}
	
	
	public static void deleteResetTime(Context context, Calendar date, int profileID, int deviceID) {
		if (context != null && date != null) {
			DatabaseAdapter_wb013 databaseAdapter = new DatabaseAdapter_wb013(context);
			databaseAdapter.openDatabase();

			databaseAdapter.delete_reset_time(date, profileID, deviceID);

			databaseAdapter.closeDatabase();
		}
	}
	
	
	public static boolean isHaveResetTime(Context context, Calendar cal, int profileID, int deviceID) {
		boolean isHave = false;
		
		if (cal != null) {
			DatabaseAdapter_wb013 databaseAdapter = new DatabaseAdapter_wb013(context);
			databaseAdapter.openDatabase();

			Cursor cursor = databaseAdapter.query_reset_time(cal, profileID, deviceID);
			if (cursor != null && cursor.getCount() != 0) {
				isHave = true;
			}

			databaseAdapter.closeDatabase();
		}
		
		return isHave;
	}
	
	
	/**
	 * 删除一天的history hour数据
	 * @param context
	 * @param date
	 */
	public static void deleteADayHistoryHour(Context context, int profileID, int deviceID, Calendar date) {
		if (date != null) {
			DatabaseAdapter_wb013 databaseAdapter = new DatabaseAdapter_wb013(context);
			databaseAdapter.openDatabase();
			
			databaseAdapter.delete_history_hour_aday(profileID, deviceID, date);
			
			databaseAdapter.closeDatabase();
		}
	}
	
	
	public static void deleteHistoryAfterNow(Context context) {
		
		Calendar today = CalendarHelper.getToday();
		Calendar tommorrow = CalendarHelper.addADay(today);
		Calendar now = Calendar.getInstance();
		
		DatabaseAdapter_wb013 databaseAdapter = new DatabaseAdapter_wb013(context);
		databaseAdapter.openDatabase();

		databaseAdapter.delete_history_day_after_tommorrow(tommorrow);
		databaseAdapter.delete_history_hour_after_now(now);

		databaseAdapter.closeDatabase();
	}
	
	
	public static void insertResetHistoryHour(Context context, int profileID, int deviceID, HistoryHour history) {
		if (history != null) {
			DatabaseAdapter_wb013 databaseAdapter = new DatabaseAdapter_wb013(context);
			databaseAdapter.openDatabase();

			databaseAdapter.insert_reset_history_hour(
					profileID, 
					deviceID, 
					history.getDate(), 
					history.getStep(), 
					history.getBurn(), 
					history.getSleepMove());
			
			databaseAdapter.closeDatabase();
		}
	}
	
	/**
	 * 更新小时历史数据
	 * @param context
	 * @param history
	 */
	public static void updateResetHistoryHour(Context context,int profileID, int deviceID, HistoryHour history) {
		if (history != null) {
			DatabaseAdapter_wb013 databaseAdapter = new DatabaseAdapter_wb013(context);
			databaseAdapter.openDatabase();
			
			databaseAdapter.update_reset_history_hour(
					profileID, 
					deviceID, 
					history.getDate(), 
					history.getStep(), 
					history.getBurn(), 
					history.getSleepMove());

			databaseAdapter.closeDatabase();
		}
	}
	
	
	/**
	 * 查询一个日期的历史小时数据
	 * @param context
	 * @param date
	 * @return
	 */
	public static HistoryHour queryResetHistoryHour(Context context, int profileID, int deviceID, Calendar date) {
		
		if (context != null && date != null) {
			
			DatabaseAdapter_wb013 databaseAdapter = new DatabaseAdapter_wb013(context);
			// 打开数据库
			databaseAdapter.openDatabase();
			
			Cursor cursor = databaseAdapter.query_reset_history_hour(profileID, deviceID, date);
			if (cursor.moveToFirst()){
				
				HistoryHour history = new HistoryHour();
				Date _date = new Date();
				try {
					_date = Global.sdf_3.parse(cursor.getString(cursor.getColumnIndex(DatabaseAdapter_wb013.KEY_DATETIME)));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				Calendar _cal = Calendar.getInstance();
				_cal.setTime(_date);
				
				history.setDate(_cal);
				history.setStep(cursor.getInt(cursor.getColumnIndex(DatabaseAdapter_wb013.KEY_STEP)));
				history.setBurn(cursor.getDouble(cursor.getColumnIndex(DatabaseAdapter_wb013.KEY_BURN)));
				history.setSleepMove(cursor.getInt(cursor.getColumnIndex(DatabaseAdapter_wb013.KEY_SLEEP_MOVE)));		
					
				if (cursor != null) {
					cursor.close();
				}
				databaseAdapter.closeDatabase();
				return history;		
			}
			
			if (cursor != null) {
				cursor.close();
			}
			databaseAdapter.closeDatabase();
		}
		
		
		return null;
	}
	
	
	public static void deleteResetHistoryHour(Context context, int profileID, int deviceID, Calendar date) {
		if (context != null && date != null) {
			
			DatabaseAdapter_wb013 databaseAdapter = new DatabaseAdapter_wb013(context);
			// 打开数据库
			databaseAdapter.openDatabase();
			
			databaseAdapter.delete_reset_history_hour(profileID, deviceID, date);
			
			databaseAdapter.closeDatabase();
		}
	}
	
	
	public static void deleteResetHistoryHourADay(Context context, int profileID, int deviceID, Calendar date) {
		if (context != null && date != null) {
			
			DatabaseAdapter_wb013 databaseAdapter = new DatabaseAdapter_wb013(context);
			// 打开数据库
			databaseAdapter.openDatabase();
			
			databaseAdapter.delete_reset_history_hour_aday(profileID, deviceID, date);
			
			databaseAdapter.closeDatabase();
		}
	}
}
