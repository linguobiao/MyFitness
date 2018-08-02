package com.lgb.myfitness.database;

import java.util.Calendar;

import com.lgb.myfitness.helper.CalendarHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DatabaseAdapter_wristband {
	private static String TAG = "DatabaseAdapter";
	
	public static final String TABLE_HISTORY_DAY_L = "TABLE_HISTORY_DAY_L";
	public static final String TABLE_HISTORY_HOUR_L = "TABLE_HISTORY_HOUR_L";
	
	///////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////
	
	public static final String KEY_DATE = "KEY_DATE";
	public static final String KEY_DATE_LONG = "KEY_DATE_LONG";
	public static final String KEY_DATETIME = "KEY_DATETIME";
	public static final String KEY_DATETIME_LONG = "KEY_DATETIME_LONG";
	public static final String KEY_STEP = "KEY_STEP";
	public static final String KEY_BURN = "KEY_BURN";
	public static final String KEY_SLEEP_GRADE = "KEY_SLEEP_GRADE";
	
	public static final String KEY_SLEEP_QUALITY = "KEY_SLEEP_QUALITY";
	public static final String KEY_SLEEP_DEEP_HOUR = "KEY_SLEEP_DEEP_HOUR";
	public static final String KEY_SLEEP_LIGHT_HOUR = "KEY_SLEEP_LIGHT_HOUR";
	public static final String KEY_ACTIVE_HOUR = "KEY_ACTIVE_HOUR";
	
	///////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////
	
	public static final String CREATE_TABLE_HISTORY_DAY_L = "CREATE TABLE IF NOT EXISTS " + TABLE_HISTORY_DAY_L +
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
	public static final String CREATE_TABLE_HISTORY_HOUR_L = "CREATE TABLE IF NOT EXISTS " + TABLE_HISTORY_HOUR_L +
			"(" + 
			DatabaseAdapter_public.KEY_ROWID + " integer primary key autoincrement, " +
			KEY_DATE + " text not null, " + 
			KEY_DATETIME + " text not null, " + 
			KEY_DATETIME_LONG + " long not null, " +
			KEY_STEP + " int not null, " + 
			KEY_BURN + " double not null, " + 
			KEY_SLEEP_GRADE + " int not null, " +
			DatabaseAdapter_public.KEY_PROFILE_ID + " int not null, " +
			DatabaseAdapter_public.KEY_DEVICE_ID + " int not null " +
			"); ";

	//////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////
		
	private final Context context;
	private DatabaseOpenHelper databaseOpenHelper;
	private SQLiteDatabase db;
	
	public DatabaseAdapter_wristband(Context ctx){
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
	public DatabaseAdapter_wristband openDatabase(){
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

		return db.insert(TABLE_HISTORY_DAY_L, null, initialValues);
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
				TABLE_HISTORY_DAY_L, 
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
				TABLE_HISTORY_DAY_L, 
				KEY_DATE + "='" + dateDay + "' and " + DatabaseAdapter_public.KEY_PROFILE_ID + "=" + profileID, 
				null);
	}
	
	
	public void delete_history_day_after_tommorrow(Calendar now) {
		String dateDay = CalendarHelper.getYyyy_MM_dd(now);
		Log.d(TAG, "delete history day after " + dateDay);
		
		db.delete(
				TABLE_HISTORY_DAY_L, 
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
		
		Cursor mCursor = db.query(true, TABLE_HISTORY_DAY_L, 
				new String[]{KEY_DATE, KEY_STEP, KEY_BURN, KEY_SLEEP_QUALITY,
					KEY_SLEEP_DEEP_HOUR, KEY_SLEEP_LIGHT_HOUR, KEY_ACTIVE_HOUR}, 
					KEY_DATE + "='" + dateDay + "' and " + DatabaseAdapter_public.KEY_PROFILE_ID + "=" + profileID, 
				null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		
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
		
		Cursor mCursor = db.query(true, TABLE_HISTORY_DAY_L, 
				new String[]{KEY_DATE, KEY_STEP, KEY_BURN, KEY_SLEEP_QUALITY,
					KEY_SLEEP_DEEP_HOUR, KEY_SLEEP_LIGHT_HOUR, KEY_ACTIVE_HOUR}, 
				KEY_DATE + ">='" + date_begin + "' and " + KEY_DATE + "<'" + date_end + "' and " + DatabaseAdapter_public.KEY_PROFILE_ID + "=" + profileID, 
				null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		
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
	public long insert_history_hour(int profileID, int deviceID, Calendar datetime, int step, double burn, int sleepGrade){
		
		String date = CalendarHelper.getYyyy_MM_dd(datetime);
		String dateHour = CalendarHelper.getYyyy_MM_dd_HH(datetime);
		
		Log.d(TAG, "insert history hour " + dateHour + ", step:" + step + ", burn:" + burn + ", " + sleepGrade);
		
		ContentValues initialValues = new ContentValues();
		
		initialValues.put(KEY_DATE, date);
		initialValues.put(KEY_DATETIME, dateHour);
		initialValues.put(KEY_DATETIME_LONG, datetime.getTimeInMillis());
		initialValues.put(KEY_STEP, step);
		initialValues.put(KEY_BURN, burn);
		initialValues.put(KEY_SLEEP_GRADE, sleepGrade);
		initialValues.put(DatabaseAdapter_public.KEY_PROFILE_ID, profileID);
		initialValues.put(DatabaseAdapter_public.KEY_DEVICE_ID, deviceID);

		return db.insert(TABLE_HISTORY_HOUR_L, null, initialValues);
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
	public int update_history_hour(int profileID, int deviceID, Calendar datetime, int step, double burn, double sleepGrade){
		String dateDay = CalendarHelper.getYyyy_MM_dd(datetime);
		String dateHour = CalendarHelper.getYyyy_MM_dd_HH(datetime);
		Log.d(TAG, "update history hour " + dateHour);
		
		ContentValues initialValues = new ContentValues();
		
		initialValues.put(KEY_DATE, dateDay);
		initialValues.put(KEY_DATETIME, dateHour);
		initialValues.put(KEY_DATETIME_LONG, datetime.getTimeInMillis());
		initialValues.put(KEY_STEP, step);
		initialValues.put(KEY_BURN, burn);
		initialValues.put(KEY_SLEEP_GRADE, sleepGrade);
		
		return db.update(
				TABLE_HISTORY_HOUR_L, 
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
		
		Cursor mCursor = db.query(true, TABLE_HISTORY_HOUR_L, 
				new String[]{KEY_DATETIME, KEY_STEP, KEY_BURN, KEY_SLEEP_GRADE}, 
				KEY_DATETIME + "='" + dateHour + "' and " + DatabaseAdapter_public.KEY_PROFILE_ID + "=" + profileID
					+ " and " + DatabaseAdapter_public.KEY_DEVICE_ID + "=" + deviceID, 
				null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		
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
		
		Cursor mCursor = db.query(true, TABLE_HISTORY_HOUR_L, 
				new String[]{KEY_DATETIME, KEY_STEP, KEY_BURN, KEY_SLEEP_GRADE}, 
				KEY_DATETIME + ">='" + date_begin + "' and " + KEY_DATETIME + "<'" + date_end  + "' and " + DatabaseAdapter_public.KEY_PROFILE_ID + "=" + profileID
					+ " and " + DatabaseAdapter_public.KEY_DEVICE_ID + "=" + deviceID, 	
				null, null, null, KEY_DATETIME_LONG + " asc", null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		
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
					TABLE_HISTORY_HOUR_L, 
					KEY_DATE + "='" + _date + "'" + " and " + DatabaseAdapter_public.KEY_PROFILE_ID + "=" + profileID
						+ " and " + DatabaseAdapter_public.KEY_DEVICE_ID + "=" + deviceID,
					null);
			
		}
		
	}
	
	
	public void delete_history_hour_after_tommorrow(Calendar now) {
		if (now != null) {
			String _date = CalendarHelper.getYyyy_MM_dd_HH(now);
			Log.d(TAG, "delete history hour after now " + _date);
			
			int result = db.delete(
					TABLE_HISTORY_HOUR_L, 
					KEY_DATETIME + ">= '" + _date + "'" , 
					null);
			Log.i(TAG, "delete_history_hour_after_now result row: " + result);
		}
	}
	
}
