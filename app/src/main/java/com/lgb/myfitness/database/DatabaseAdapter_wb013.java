package com.lgb.myfitness.database;

import java.util.Calendar;

import com.lgb.myfitness.helper.CalendarHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DatabaseAdapter_wb013 {
	private static String TAG = "DatabaseAdapter_wb013";
	
	public static final String TABLE_HISTORY_DAY_S = "TABLE_HISTORY_DAY_S";
	public static final String TABLE_HISTORY_HOUR_S = "TABLE_HISTORY_HOUR_S";
	public static final String TABLE_SCREEN_S = "TABLE_SCREEN_S";
	public static final String TABLE_RESET_TIME = "TABLE_RESET_TIME";
	public static final String TABLE_RESET_HISTORY_HOUR = "TABLE_RESET_HISTORY_HOUR";
	
	///////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////
	
	public static final String KEY_DATE = "KEY_DATE";
	public static final String KEY_DATE_LONG = "KEY_DATE_LONG";
	public static final String KEY_DATETIME = "KEY_DATETIME";
	public static final String KEY_DATETIME_LONG = "KEY_DATETIME_LONG";
	public static final String KEY_STEP = "KEY_STEP";
	public static final String KEY_BURN = "KEY_BURN";
	public static final String KEY_SLEEP_MOVE = "KEY_SLEEP_MOVE";
	
	public static final String KEY_SLEEP_QUALITY = "KEY_SLEEP_QUALITY";
	public static final String KEY_SLEEP_DEEP_HOUR = "KEY_SLEEP_DEEP_HOUR";
	public static final String KEY_SLEEP_LIGHT_HOUR = "KEY_SLEEP_LIGHT_HOUR";
	public static final String KEY_ACTIVE_HOUR = "KEY_ACTIVE_HOUR";
	
	public static final String KEY_DISTANCE = "KEY_DISTANCE";
	public static final String KEY_GOAL = "KEY_GOAL";
	public static final String KEY_UNIT = "KEY_UNIT";
	public static final String KEY_BATTARY = "KEY_BATTARY";
	
	///////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////
	
	public static final String CREATE_TABLE_HISTORY_DAY_S = "CREATE TABLE IF NOT EXISTS " + TABLE_HISTORY_DAY_S +
			"(" + 
			DatabaseAdapter_public.KEY_ROWID + " integer primary key autoincrement, " +
			KEY_DATE + " text not null, " + 
			KEY_DATE_LONG + " long not null, " +
			KEY_STEP + " int not null, " + 
			KEY_BURN + " double not null, " + 
			KEY_SLEEP_QUALITY + " int not null, " + 
			KEY_SLEEP_DEEP_HOUR + " int not null, " +
			KEY_SLEEP_LIGHT_HOUR + " int not null, " +
			KEY_ACTIVE_HOUR + " int not null, " +
			DatabaseAdapter_public.KEY_PROFILE_ID + " int not null" +
			"); ";
	public static final String CREATE_TABLE_HISTORY_HOUR_S = "CREATE TABLE IF NOT EXISTS " + TABLE_HISTORY_HOUR_S +
			"(" + 
			DatabaseAdapter_public.KEY_ROWID + " integer primary key autoincrement, " +
			KEY_DATE + " text not null, " + 
			KEY_DATETIME + " text not null, " + 
			KEY_DATETIME_LONG + " long not null, " +
			KEY_STEP + " int not null, " + 
			KEY_BURN + " double not null, " + 
			KEY_SLEEP_MOVE + " int not null, " +
			DatabaseAdapter_public.KEY_PROFILE_ID + " int not null, " +
			DatabaseAdapter_public.KEY_DEVICE_ID + " int not null " +
			"); ";
	public static final String CREATE_TABLE_SCREEN = "CREATE TABLE IF NOT EXISTS " + TABLE_SCREEN_S +
			"(" + DatabaseAdapter_public.KEY_ROWID + " integer primary key autoincrement, " +
			KEY_DATE + " string not null, " +
			KEY_STEP + " int not null, " +
			KEY_DISTANCE + " double not null, " +
			KEY_BURN + " int not null, " +
			KEY_GOAL + " int not null, " +
			KEY_BATTARY + " int not null, " +
			KEY_UNIT  + " int not null, " +
			DatabaseAdapter_public.KEY_PROFILE_ID + " int not null, " +
			DatabaseAdapter_public.KEY_DEVICE_ID + " int not null " +
			"); "; 	
	public static final String CREATE_TABLE_RESET_TIME = "CREATE TABLE IF NOT EXISTS " + TABLE_RESET_TIME +
			"(" + DatabaseAdapter_public.KEY_ROWID + " integer primary key autoincrement, " +
			KEY_DATE + " string not null, " +
			KEY_DATETIME + " string not null, " +
			DatabaseAdapter_public.KEY_PROFILE_ID + " int not null, " +
			DatabaseAdapter_public.KEY_DEVICE_ID + " int not null " +
			"); ";
	public static final String CREATE_TABLE_RESET_HISTORY_HOUR = "CREATE TABLE IF NOT EXISTS " + TABLE_RESET_HISTORY_HOUR +
			"(" + 
			DatabaseAdapter_public.KEY_ROWID + " integer primary key autoincrement, " +
			KEY_DATE + " text not null, " + 
			KEY_DATETIME + " text not null, " + 
			KEY_STEP + " int not null, " + 
			KEY_BURN + " double not null, " + 
			KEY_SLEEP_MOVE + " int not null, " +
			DatabaseAdapter_public.KEY_PROFILE_ID + " int not null, " +
			DatabaseAdapter_public.KEY_DEVICE_ID + " int not null " +
			"); ";
	//////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////
		
	private final Context context;
	private DatabaseOpenHelper databaseOpenHelper;
	private SQLiteDatabase db;
	
	public DatabaseAdapter_wb013(Context ctx){
		this.context = ctx;
		databaseOpenHelper = new DatabaseOpenHelper(
				context, 
				DatabaseAdapter_public.DATABASE_NAME, 
				null, 
				DatabaseAdapter_public.DATABASE_VERSION);
	}
	
	/**
	 * open database
	 * @return
	 */
	public DatabaseAdapter_wb013 openDatabase(){
		db = databaseOpenHelper.getWritableDatabase();
		return this;
	}
	
	
	/**
	 * close database
	 */
	public void closeDatabase(){
		databaseOpenHelper.close();
	}
	

	
	
	/**
	 * insert history day
	 * @param date
	 * @param datetime
	 * @param step
	 * @param burn
	 * @param sleepQuality
	 * @return
	 */
	public long insert_history_day(int profileID, Calendar date, int step, double burn, int sleepQuality, int deepSleep, int lightSleep, int active){
		
		String dateDay = CalendarHelper.getYyyy_MM_dd(date);
		Log.d(TAG, "insert history day " + dateDay + ", step:" + step + ", burn:" + burn + ", " + sleepQuality);
		
		ContentValues initialValues = new ContentValues();
		
		initialValues.put(KEY_DATE, dateDay);
		initialValues.put(KEY_DATE_LONG, date.getTimeInMillis());
		initialValues.put(KEY_STEP, step);
		initialValues.put(KEY_BURN, burn);
		initialValues.put(KEY_SLEEP_QUALITY, sleepQuality);
		initialValues.put(KEY_SLEEP_DEEP_HOUR, deepSleep);
		initialValues.put(KEY_SLEEP_LIGHT_HOUR, lightSleep);
		initialValues.put(KEY_ACTIVE_HOUR, active);
		initialValues.put(DatabaseAdapter_public.KEY_PROFILE_ID, profileID);

		return db.insert(TABLE_HISTORY_DAY_S, null, initialValues);
	}
	
	/**
	 * update history date
	 * @param date
	 * @param datetime
	 * @param step
	 * @param burn
	 * @param sleepQuality
	 * @return
	 */
	public int update_history_day(int profileID, Calendar date, int step, double burn, int sleepQuality, int deepSleep, int lightSleep, int active){
		
		String dateDay = CalendarHelper.getYyyy_MM_dd(date);
		Log.d(TAG, "update history day " + dateDay + ", step:" + step + ", burn:" + burn + ", " + sleepQuality);
		
		ContentValues initialValues = new ContentValues();
		
		initialValues.put(KEY_DATE, dateDay);
		initialValues.put(KEY_DATE_LONG, date.getTimeInMillis());
		initialValues.put(KEY_STEP, step);
		initialValues.put(KEY_BURN, burn);
		initialValues.put(KEY_SLEEP_QUALITY, sleepQuality);
		initialValues.put(KEY_SLEEP_DEEP_HOUR, deepSleep);
		initialValues.put(KEY_SLEEP_LIGHT_HOUR, lightSleep);
		initialValues.put(KEY_ACTIVE_HOUR, active);
		
		return db.update(
				TABLE_HISTORY_DAY_S, 
				initialValues, 
				KEY_DATE + "='" + dateDay + "' and " + DatabaseAdapter_public. KEY_PROFILE_ID + "=" + profileID, 
				null);
	}
	
	
	/**
	 * delete a history day
	 * @param date
	 */
	public void delete_history_day(int profileID, Calendar date) {
		
		String dateDay = CalendarHelper.getYyyy_MM_dd(date);
		Log.d(TAG, "delete history day " + dateDay);
		
		db.delete(
				TABLE_HISTORY_DAY_S, 
				KEY_DATE + "='" + dateDay + "' and " + DatabaseAdapter_public.KEY_PROFILE_ID + "=" + profileID, 
				null);
	}
	
	
	public void delete_history_day_after_tommorrow(Calendar now) {
		String dateDay = CalendarHelper.getYyyy_MM_dd(now);
		Log.d(TAG, "delete history day after " + dateDay);
		
		db.delete(
				TABLE_HISTORY_DAY_S, 
				KEY_DATE + ">='" + dateDay + "'", 
				null);
	}
	
	

	/**
	 * query history day
	 * @return
	 */
	public Cursor query_history_day(int profileID, Calendar date){
		String dateDay = CalendarHelper.getYyyy_MM_dd(date);
		Log.d(TAG, "query history day " + dateDay);
		
		Cursor mCursor = db.query(true, TABLE_HISTORY_DAY_S, 
				new String[]{KEY_DATE, KEY_STEP, KEY_BURN, KEY_SLEEP_QUALITY,
					KEY_SLEEP_DEEP_HOUR, KEY_SLEEP_LIGHT_HOUR, KEY_ACTIVE_HOUR}, 
					KEY_DATE + "='" + dateDay + "' and " + DatabaseAdapter_public.KEY_PROFILE_ID + "=" + profileID, 
				null, null, null, null, null);
//		if (mCursor != null) {
//			mCursor.moveToFirst();
//		}
		
		return mCursor;
	}
	
	/**
	 * query history day
	 * @param dateBegin
	 * @param dateEnd
	 * @return
	 */
	public Cursor query_history_day(int profileID, Calendar dateBegin, Calendar dateEnd) {
		String date_begin = CalendarHelper.getYyyy_MM_dd(dateBegin);
		String date_end  = CalendarHelper.getYyyy_MM_dd(dateEnd);
		Log.d(TAG, "query history day from " + date_begin + " to " + date_end);
		
		Cursor mCursor = db.query(true, TABLE_HISTORY_DAY_S, 
				new String[]{KEY_DATE, KEY_STEP, KEY_BURN, KEY_SLEEP_QUALITY,
					KEY_SLEEP_DEEP_HOUR, KEY_SLEEP_LIGHT_HOUR, KEY_ACTIVE_HOUR}, 
				KEY_DATE + ">='" + date_begin + "' and " + KEY_DATE + "<'" + date_end + "' and " + DatabaseAdapter_public.KEY_PROFILE_ID + "=" + profileID, 
				null, null, null, null, null);
//		if (mCursor != null) {
//			mCursor.moveToFirst();
//		}
		
		return mCursor;
	}
	
	
	/**
	 * insert history hour
	 * @param datetime
	 * @param datetime
	 * @param step
	 * @param burn
	 * @param sleepQuality
	 * @return
	 */
	public long insert_history_hour(int profileID, int deviceID, Calendar datetime, int step, double burn, int sleepMove){
		
		String date = CalendarHelper.getYyyy_MM_dd(datetime);
		String dateHour = CalendarHelper.getYyyy_MM_dd_HH(datetime);
		
		Log.d(TAG, "insert history hour " + dateHour + ", step:" + step + ", burn:" + burn + ", " + sleepMove);
		
		ContentValues initialValues = new ContentValues();
		
		initialValues.put(KEY_DATE, date);
		initialValues.put(KEY_DATETIME, dateHour);
		initialValues.put(KEY_DATETIME_LONG, datetime.getTimeInMillis());
		initialValues.put(KEY_STEP, step);
		initialValues.put(KEY_BURN, burn);
		initialValues.put(KEY_SLEEP_MOVE, sleepMove);
		initialValues.put(DatabaseAdapter_public.KEY_PROFILE_ID, profileID);
		initialValues.put(DatabaseAdapter_public.KEY_DEVICE_ID, deviceID);

		return db.insert(TABLE_HISTORY_HOUR_S, null, initialValues);
	}
	
	
	/**
	 * update history hour
	 * @param date
	 * @param datetime
	 * @param step
	 * @param burn
	 * @param sleep
	 * @return
	 */
	public int update_history_hour(int profileID, int deviceID, Calendar datetime, int step, double burn, double sleepMove){
		String dateDay = CalendarHelper.getYyyy_MM_dd(datetime);
		String dateHour = CalendarHelper.getYyyy_MM_dd_HH(datetime);
		Log.d(TAG, "update history hour " + dateHour);
		
		ContentValues initialValues = new ContentValues();
		
		initialValues.put(KEY_DATE, dateDay);
		initialValues.put(KEY_DATETIME, dateHour);
		initialValues.put(KEY_DATETIME_LONG, datetime.getTimeInMillis());
		initialValues.put(KEY_STEP, step);
		initialValues.put(KEY_BURN, burn);
		initialValues.put(KEY_SLEEP_MOVE, sleepMove);
		
		return db.update(
				TABLE_HISTORY_HOUR_S, 
				initialValues, 
				KEY_DATETIME + "='" + dateHour + "' and " + DatabaseAdapter_public.KEY_PROFILE_ID + "=" + profileID
					+ " and " + DatabaseAdapter_public.KEY_DEVICE_ID + "=" + deviceID,
				null);
	}
	
	/**
	 * query history hour
	 * @return
	 */
	public Cursor query_history_hour(int profileID, int deviceID, Calendar datetime){
		String dateHour = CalendarHelper.getYyyy_MM_dd_HH(datetime);
		Log.d(TAG, "query history hour " + dateHour);
		
		Cursor mCursor = db.query(true, TABLE_HISTORY_HOUR_S, 
				new String[]{KEY_DATETIME, KEY_STEP, KEY_BURN, KEY_SLEEP_MOVE}, 
				KEY_DATETIME + "='" + dateHour + "' and " + DatabaseAdapter_public.KEY_PROFILE_ID + "=" + profileID
					+ " and " + DatabaseAdapter_public.KEY_DEVICE_ID + "=" + deviceID, 
				null, null, null, null, null);
//		if (mCursor != null) {
//			mCursor.moveToFirst();
//		}
		
		return mCursor;
	}
	
	
	/**
	 * query newest history hour
	 * @param profileID
	 * @param deviceID
	 * @return
	 */
	public Cursor query_history_hour_newest(int profileID, int deviceID) {
		Cursor mCursor = db.query(true, TABLE_HISTORY_HOUR_S, 
				new String[]{KEY_DATETIME, KEY_STEP, KEY_BURN, KEY_SLEEP_MOVE}, 
				DatabaseAdapter_public.KEY_PROFILE_ID + "=" + profileID
					+ " and " + DatabaseAdapter_public.KEY_DEVICE_ID + "=" + deviceID, 
				null, null, null, KEY_DATETIME + " desc", "1");
		
		return mCursor;
	}
	
	
	/**
	 * query history hour
	 * @param dateBegin
	 * @param dateEnd
	 * @return
	 */
	public Cursor query_history_hour(int profileID, int deviceID, Calendar dateBegin, Calendar dateEnd) {
		
		String date_begin = CalendarHelper.getYyyy_MM_dd_HH(dateBegin);
		String date_end = CalendarHelper.getYyyy_MM_dd_HH(dateEnd);
		
		Log.d(TAG, "query history hour from " + date_begin + " to " + date_end);
		
		Cursor mCursor = db.query(true, TABLE_HISTORY_HOUR_S, 
				new String[]{KEY_DATETIME, KEY_STEP, KEY_BURN, KEY_SLEEP_MOVE}, 
				KEY_DATETIME + ">='" + date_begin + "' and " + KEY_DATETIME + "<'" + date_end  + "' and " + DatabaseAdapter_public.KEY_PROFILE_ID + "=" + profileID
					+ " and " + DatabaseAdapter_public.KEY_DEVICE_ID + "=" + deviceID, 	
				null, null, null, KEY_DATETIME_LONG + " asc", null);
//		if (mCursor != null) {
//			mCursor.moveToFirst();
//		}
		
		return mCursor;
	}
	
	/**
	 * delete a day's history hour data
	 * @param date
	 * @return
	 */
	public void delete_history_hour_aday(int profileID, int deviceID, Calendar date) {
		if (date != null) {
			String _date = CalendarHelper.getYyyy_MM_dd(date);
			
			Log.d(TAG, "delete history hour " + _date);
			db.delete(
					TABLE_HISTORY_HOUR_S, 
					KEY_DATE + "='" + _date + "'" + " and " + DatabaseAdapter_public.KEY_PROFILE_ID + "=" + profileID
						+ " and " + DatabaseAdapter_public.KEY_DEVICE_ID + "=" + deviceID,
					null);
			
		}
		
	}
	
	
	public void delete_history_hour_after_now(Calendar now) {
		if (now != null) {
			String _date = CalendarHelper.getYyyy_MM_dd_HH(now);
			Log.d(TAG, "delete history hour after now " + _date);
			
			int result = db.delete(
					TABLE_HISTORY_HOUR_S, 
					KEY_DATETIME + "> '" + _date + "'" , 
					null);
			Log.i(TAG, "delete_history_hour_after_now result row: " + result);
		}
	}
	
	
	/**
	 * 插入屏幕数据
	 * @param step
	 * @param distance
	 * @param burn
	 * @param goal
	 * @param battery
	 * @param profileID
	 * @param deviceID
	 * @return
	 */
	public long insert_screen(Calendar date, int step, double distance, int burn, int goal, int battery, int unit, int profileID, int deviceID ) {
		
		String dateStr = CalendarHelper.getYyyy_MM_dd(date);
		Log.d(TAG, "insert screen " + dateStr + " step:" + step + ", distance:" + distance + ", burn:" + burn + ", goal:" + goal + ", unit:" + unit + ", profileID:" + profileID + ", deviceID:" + deviceID);
		
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_DATE, dateStr);
		initialValues.put(KEY_STEP, step);
		initialValues.put(KEY_DISTANCE, distance);
		initialValues.put(KEY_BURN, burn);
		initialValues.put(KEY_GOAL, goal);
		initialValues.put(KEY_BATTARY, battery);
		initialValues.put(KEY_UNIT, unit);
		initialValues.put(DatabaseAdapter_public.KEY_PROFILE_ID, profileID);
		initialValues.put(DatabaseAdapter_public.KEY_DEVICE_ID, deviceID);

		return db.insert(TABLE_SCREEN_S, null, initialValues);
	}
	
	
	/**
	 * 更新屏幕数据
	 * @param date
	 * @param step
	 * @param distance
	 * @param burn
	 * @param goal
	 * @param battery
	 * @param profileID
	 * @param deviceID
	 * @return
	 */
	public int update_screen(Calendar date, int step, double distance, int burn, int goal, int battery, int unit, int profileID, int deviceID ) {
		String dateStr = CalendarHelper.getYyyy_MM_dd(date);
		Log.d(TAG, "update screen " + dateStr + " step:" + step + ", distance:" + distance + ", burn:" + burn + ", goal:" + goal + ", unit:" + unit + ", profileID:" + profileID + ", deviceID:" + deviceID);
		
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_DATE, dateStr);
		initialValues.put(KEY_STEP, step);
		initialValues.put(KEY_DISTANCE, distance);
		initialValues.put(KEY_BURN, burn);
		initialValues.put(KEY_GOAL, goal);
		initialValues.put(KEY_BATTARY, battery);
		initialValues.put(KEY_UNIT, unit);
		initialValues.put(DatabaseAdapter_public.KEY_PROFILE_ID, profileID);
		initialValues.put(DatabaseAdapter_public.KEY_DEVICE_ID, deviceID);
		
		return db.update(
				TABLE_SCREEN_S, 
				initialValues, 
				DatabaseAdapter_public.KEY_PROFILE_ID + "=" + profileID + " and " + DatabaseAdapter_public.KEY_DEVICE_ID + "=" + deviceID + " and " + KEY_DATE + "='" + dateStr + "'", 
				null);
	}
	
	
	public Cursor query_screen(Calendar date, int profileID, int deviceID) {
		
		String dateStr = CalendarHelper.getYyyy_MM_dd(date);
		Log.d(TAG, "query screen " + dateStr + ", profileID:" + profileID + ", deviceID:" + deviceID);
		
		Cursor mCursor = db.query(true, TABLE_SCREEN_S, 
				new String[]{KEY_DATE, KEY_STEP, KEY_DISTANCE, KEY_BURN, KEY_GOAL, KEY_BATTARY, KEY_UNIT}, 
				DatabaseAdapter_public.KEY_PROFILE_ID + "=" + profileID + " and " + DatabaseAdapter_public.KEY_DEVICE_ID + "=" + deviceID + " and " + KEY_DATE + "='" + dateStr + "'", 
				null, null, null, null, null);
//		if (mCursor != null) {
//			mCursor.moveToFirst();
//		}
		
		return mCursor;
	}
	
	
	public void delete_screen(Calendar date, int profileID, int deviceID) {
		String dateStr = CalendarHelper.getYyyy_MM_dd(date);
		Log.d(TAG, "delete screen " + dateStr + ", profileID:" + profileID + ", deviceID:" + deviceID);
	
		db.delete(
				TABLE_SCREEN_S, 
				KEY_DATE + "='" + dateStr + "'" + " and " + DatabaseAdapter_public.KEY_PROFILE_ID + "=" + profileID
				+ " and " + DatabaseAdapter_public.KEY_DEVICE_ID + "=" + deviceID,
				null);
		
	}
	
	
	public long insert_reset_time(Calendar date, int profileID, int deviceID) {
		if (date != null) {
			String dateStr = CalendarHelper.getYyyy_MM_dd(date);
			String datetimeStr = CalendarHelper.getYyyy_MM_dd_HH(date);
			
			Log.d(TAG, "insert reset time " + datetimeStr + ", profileID:" + profileID + ", deviceID:" + deviceID);
			
			ContentValues initialValues = new ContentValues();
			initialValues.put(KEY_DATE, dateStr);
			initialValues.put(KEY_DATETIME, datetimeStr);
			initialValues.put(DatabaseAdapter_public.KEY_PROFILE_ID, profileID);
			initialValues.put(DatabaseAdapter_public.KEY_DEVICE_ID, deviceID);

			return db.insert(TABLE_RESET_TIME, null, initialValues);
		}
		
		return -1;
	}
	
	
	public int update_reset_time(Calendar date, int profileID, int deviceID) {
		if (date != null) {
			String dateStr = CalendarHelper.getYyyy_MM_dd(date);
			String datetimeStr = CalendarHelper.getYyyy_MM_dd_HH(date);
			
			Log.d(TAG, "update reset time " + datetimeStr + ", profileID:" + profileID + ", deviceID:" + deviceID);
			
			ContentValues initialValues = new ContentValues();
			initialValues.put(KEY_DATE, dateStr);
			initialValues.put(KEY_DATETIME, datetimeStr);
			initialValues.put(DatabaseAdapter_public.KEY_PROFILE_ID, profileID);
			initialValues.put(DatabaseAdapter_public.KEY_DEVICE_ID, deviceID);
			
			return db.update(
					TABLE_RESET_TIME, 
					initialValues, 
					DatabaseAdapter_public.KEY_PROFILE_ID + "=" + profileID + " and " + DatabaseAdapter_public.KEY_DEVICE_ID + "=" + deviceID + " and " + KEY_DATETIME + "='" + datetimeStr + "'", 
					null);
		}
		
		return -1;
	}
	
	
	public Cursor query_reset_time(Calendar date, int profileID, int deviceID) {
		if (date != null) {
			String datetimeStr = CalendarHelper.getYyyy_MM_dd_HH(date);
			Log.d(TAG, "query reset time " + datetimeStr + ", profileID:" + profileID + ", deviceID:" + deviceID);
			
			Cursor mCursor = db.query(true, TABLE_RESET_TIME, 
					new String[]{KEY_DATE, KEY_DATETIME}, 
					DatabaseAdapter_public.KEY_PROFILE_ID + "=" + profileID + " and " + DatabaseAdapter_public.KEY_DEVICE_ID + "=" + deviceID + " and " + KEY_DATETIME + "='" + datetimeStr + "'", 
					null, null, null, null, null);
//			if (mCursor != null) {
//				mCursor.moveToFirst();
//			}
			
			return mCursor;
			
		}
		
		return null;
	}

	
	public void delete_reset_time(Calendar date, int profileID, int deviceID) {
		if (date != null) {
			String dateStr = CalendarHelper.getYyyy_MM_dd(date);
			Log.d(TAG, "delete reset time " + dateStr + ", profileID:" + profileID + ", deviceID:" + deviceID);
		
			db.delete(
					TABLE_RESET_TIME, 
					KEY_DATE + "='" + dateStr + "'" + " and " + DatabaseAdapter_public.KEY_PROFILE_ID + "=" + profileID
					+ " and " + DatabaseAdapter_public.KEY_DEVICE_ID + "=" + deviceID,
					null);
		}
	}
	
	
	public long insert_reset_history_hour(int profileID, int deviceID, Calendar datetime, int step, double burn, int sleepMove){
		
		String date = CalendarHelper.getYyyy_MM_dd(datetime);
		String dateHour = CalendarHelper.getYyyy_MM_dd_HH(datetime);
		
		Log.d(TAG, "insert reset history hour " + dateHour + ", step:" + step + ", burn:" + burn + ", " + sleepMove);
		
		ContentValues initialValues = new ContentValues();
		
		initialValues.put(KEY_DATE, date);
		initialValues.put(KEY_DATETIME, dateHour);
		initialValues.put(KEY_STEP, step);
		initialValues.put(KEY_BURN, burn);
		initialValues.put(KEY_SLEEP_MOVE, sleepMove);
		initialValues.put(DatabaseAdapter_public.KEY_PROFILE_ID, profileID);
		initialValues.put(DatabaseAdapter_public.KEY_DEVICE_ID, deviceID);

		return db.insert(TABLE_RESET_HISTORY_HOUR, null, initialValues);
	}
	
	
	/**
	 * update history hour
	 * @param date
	 * @param datetime
	 * @param step
	 * @param burn
	 * @param sleep
	 * @return
	 */
	public int update_reset_history_hour(int profileID, int deviceID, Calendar datetime, int step, double burn, double sleepMove){
		String dateDay = CalendarHelper.getYyyy_MM_dd(datetime);
		String dateHour = CalendarHelper.getYyyy_MM_dd_HH(datetime);
		Log.d(TAG, "update history hour " + dateHour);
		
		ContentValues initialValues = new ContentValues();
		
		initialValues.put(KEY_DATE, dateDay);
		initialValues.put(KEY_DATETIME, dateHour);
		initialValues.put(KEY_STEP, step);
		initialValues.put(KEY_BURN, burn);
		initialValues.put(KEY_SLEEP_MOVE, sleepMove);
		
		return db.update(
				TABLE_RESET_HISTORY_HOUR, 
				initialValues, 
				KEY_DATETIME + "='" + dateHour + "' and " + DatabaseAdapter_public.KEY_PROFILE_ID + "=" + profileID
					+ " and " + DatabaseAdapter_public.KEY_DEVICE_ID + "=" + deviceID,
				null);
	}
	
	
	/**
	 * query history hour
	 * @return
	 */
	public Cursor query_reset_history_hour(int profileID, int deviceID, Calendar datetime){
		String dateHour = CalendarHelper.getYyyy_MM_dd_HH(datetime);
		Log.d(TAG, "query reset history hour " + dateHour);
		
		Cursor mCursor = db.query(true, TABLE_RESET_HISTORY_HOUR, 
				new String[]{KEY_DATETIME, KEY_STEP, KEY_BURN, KEY_SLEEP_MOVE}, 
				KEY_DATETIME + "='" + dateHour + "' and " + DatabaseAdapter_public.KEY_PROFILE_ID + "=" + profileID
					+ " and " + DatabaseAdapter_public.KEY_DEVICE_ID + "=" + deviceID, 
				null, null, null, null, null);
//		if (mCursor != null) {
//			mCursor.moveToFirst();
//		}
		
		return mCursor;
	}
	
	
	/**
	 * delete a day's history hour data
	 * @param date
	 * @return
	 */
	public void delete_reset_history_hour_aday(int profileID, int deviceID, Calendar date) {
		if (date != null) {
			String _date = CalendarHelper.getYyyy_MM_dd(date);
			
			Log.d(TAG, "delete reset history hour " + _date);
			db.delete(
					TABLE_RESET_HISTORY_HOUR, 
					KEY_DATE + "='" + _date + "'" + " and " + DatabaseAdapter_public.KEY_PROFILE_ID + "=" + profileID
						+ " and " + DatabaseAdapter_public.KEY_DEVICE_ID + "=" + deviceID,
					null);
			
		}
		
	}
	
	
	/**
	 * delete a day's history hour data
	 * @param date
	 * @return
	 */
	public void delete_reset_history_hour(int profileID, int deviceID, Calendar date) {
		if (date != null) {
			String datetime = CalendarHelper.getYyyy_MM_dd_HH(date);
			
			Log.d(TAG, "delete reset history hour " + datetime);
			db.delete(
					TABLE_RESET_HISTORY_HOUR, 
					KEY_DATETIME + "='" + datetime + "'" + " and " + DatabaseAdapter_public.KEY_PROFILE_ID + "=" + profileID
						+ " and " + DatabaseAdapter_public.KEY_DEVICE_ID + "=" + deviceID,
					null);
			
		}
		
	}
}
