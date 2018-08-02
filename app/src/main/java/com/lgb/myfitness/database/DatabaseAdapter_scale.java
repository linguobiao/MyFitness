package com.lgb.myfitness.database;

import java.util.Calendar;

import com.lgb.myfitness.helper.CalendarHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DatabaseAdapter_scale {
	
	private static String TAG = "DatabaseAdapter_scale";
	
	public static final String TABLE_SCALE = "TABLE_SCALE";
	public static final String TABLE_GOAL_WEIGHT = "TABLE_GOAL_WEIGHT";
	
	///////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////
	
	public static final String KEY_SCALE_WEIGHT = "KEY_SCALE_WEIGHT";
	public static final String KEY_SCALE_BMI = "KEY_SCALE_BMI";
	
	public static final String KEY_GOAL_WEIGHT = "KEY_GOAL_WEIGHT";
	///////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////// 	

	public static final String CREATE_TABLE_SCALE = "CREATE TABLE IF NOT EXISTS " + TABLE_SCALE +
			"(" + 
			DatabaseAdapter_public.KEY_ROWID + " integer primary key autoincrement, " +
			DatabaseAdapter_public.KEY_DATE + " text not null, " + 
			DatabaseAdapter_public.KEY_PROFILE_ID + " int not null, " + 
			KEY_SCALE_WEIGHT + " double not null, " +
			KEY_SCALE_BMI + " double not null" +
			"); "; 	
	public static final String CREATE_TABLE_GOAL_WEIGHT = "CREATE TABLE IF NOT EXISTS " + TABLE_GOAL_WEIGHT +
			"(" + 
			DatabaseAdapter_public.KEY_ROWID + " integer primary key autoincrement, " +
			DatabaseAdapter_public.KEY_PROFILE_ID + " int not null, " + 
			KEY_GOAL_WEIGHT + " double not null" +
			"); "; 	
			
			
	//////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////
		
	private final Context context;
	private DatabaseOpenHelper databaseOpenHelper;
	private SQLiteDatabase db;
	
	public DatabaseAdapter_scale(Context ctx){
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
	public DatabaseAdapter_scale openDatabase(){
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
	 * 插入电子称数据
	 * @param weight
	 * @return 
	 */
	public long insert_scale(int profileID, Calendar date, double weight, double bmi) {
		String dateDay = CalendarHelper.getYyyy_MM_dd(date);
		Log.d(TAG, "insert scale " + dateDay + ", weight:" + weight + ", bmi:" + bmi + ", profile id:" + profileID);
		
		ContentValues initialValues = new ContentValues();
		
		initialValues.put(DatabaseAdapter_public.KEY_DATE, dateDay);
		initialValues.put(KEY_SCALE_WEIGHT, weight);
		initialValues.put(KEY_SCALE_BMI, bmi);
		initialValues.put(DatabaseAdapter_public.KEY_PROFILE_ID, profileID);

		return db.insert(TABLE_SCALE, null, initialValues);
	}
	
	/**
	 * 更新电子称数据
	 * @param weight
	 * @return
	 */
	public int update_scale(int profileID, Calendar date, double weight, double bmi) {
		String dateDay = CalendarHelper.getYyyy_MM_dd(date);
		Log.d(TAG, "update scale " + dateDay + ", weight:" + weight + ", bmi:" + bmi);
		ContentValues initialValues = new ContentValues();
		
		initialValues.put(DatabaseAdapter_public.KEY_DATE, dateDay);
		initialValues.put(KEY_SCALE_WEIGHT, weight);
		initialValues.put(KEY_SCALE_BMI, bmi);
		initialValues.put(DatabaseAdapter_public.KEY_PROFILE_ID, profileID);
		
		return db.update(TABLE_SCALE, initialValues, DatabaseAdapter_public.KEY_DATE + "='" + dateDay + "' and " + DatabaseAdapter_public.KEY_PROFILE_ID + "=" + profileID, null);
	}
	
	/**
	 * 查询电子称数据
	 */
	public Cursor query_scale(int profileID, Calendar date) {
		
		String dateDay = CalendarHelper.getYyyy_MM_dd(date);
		Log.d(TAG, "query scale " + dateDay);
		
		Cursor mCursor = db.query(true, TABLE_SCALE, 
				new String[]{DatabaseAdapter_public.KEY_DATE, KEY_SCALE_WEIGHT, KEY_SCALE_BMI},
				DatabaseAdapter_public.KEY_DATE + "='" + dateDay + "' and " + DatabaseAdapter_public.KEY_PROFILE_ID + "=" + profileID, 
				null, null, null, null, null);
		
		return mCursor;
	}
	
	/**
	 * query scale
	 * @param dateBegin
	 * @param dateEnd
	 * @return
	 */
	public Cursor query_scale(int profileID, Calendar dateBegin, Calendar dateEnd) {
		String date_begin = CalendarHelper.getYyyy_MM_dd(dateBegin);
		String date_end  = CalendarHelper.getYyyy_MM_dd(dateEnd);
		Log.d(TAG, "query scale from " + date_begin + " to " + date_end);
		
		Cursor mCursor = db.query(true, TABLE_SCALE, 
				new String[]{DatabaseAdapter_public.KEY_DATE, KEY_SCALE_WEIGHT, KEY_SCALE_BMI}, 
				DatabaseAdapter_public.KEY_DATE + ">='" + date_begin + "' and " + DatabaseAdapter_public.KEY_DATE + "<'" + date_end + "' and " + DatabaseAdapter_public.KEY_PROFILE_ID + "=" + profileID, 
				null, null, null, null, null);
		
		return mCursor;
	}
	
	
	/**
	 * 插入目标体重
	 * @param profileID
	 * @param goalWeight
	 * @return
	 */
	public long insert_goal_weight(int profileID, double goalWeight) {

		Log.d(TAG, "insert goal weight " + profileID + ", goal weight:" + goalWeight);
		ContentValues initialValues = new ContentValues();
		
		initialValues.put(KEY_GOAL_WEIGHT, goalWeight);
		initialValues.put(DatabaseAdapter_public.KEY_PROFILE_ID, profileID);

		return db.insert(TABLE_GOAL_WEIGHT, null, initialValues);
	}
	
	
	
	public int update_goal_weight(int profileID, double goalWeight) {
		
		Log.d(TAG, "update goal weight " + profileID + ", goal weight:" + goalWeight);
		ContentValues initialValues = new ContentValues();
		
		initialValues.put(KEY_GOAL_WEIGHT, goalWeight);
		initialValues.put(DatabaseAdapter_public.KEY_PROFILE_ID, profileID);
		
		return db.update(TABLE_GOAL_WEIGHT, initialValues, DatabaseAdapter_public.KEY_PROFILE_ID + "=" + profileID, null);
	}
	
	
	public Cursor query_goal_weight(int profileID) {
		
		Log.d(TAG, "query goal weight " + profileID);
		
		Cursor mCursor = db.query(true, TABLE_GOAL_WEIGHT, 
				new String[]{KEY_GOAL_WEIGHT}, 
				DatabaseAdapter_public.KEY_PROFILE_ID + "=" + profileID, 
				null, null, null, null, null);
		
		return mCursor;
	}
	
	
	
}
