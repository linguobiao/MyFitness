package com.lgb.myfitness.database;

import java.util.Calendar;

import com.lgb.myfitness.helper.CalendarHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DatabaseAdapter_bpm {
	
	private static String TAG = "DatabaseAdapter_bpm";
	
	public static final String TABLE_BPM = "TABLE_BPM";
	
	///////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////
	
	public static final String KEY_BPM_SYSTOLIC = "KEY_BPM_SYSTOLIC";
	public static final String KEY_BPM_DIASTOLIC = "KEY_BPM_DIASTOLIC";
	public static final String KEY_BPM_PULSE_RATE = "KEY_BPM_PULSE_RATE";
	///////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////// 	

	public static final String CREATE_TABLE_BPM = "CREATE TABLE IF NOT EXISTS " + TABLE_BPM +
			"(" + 
			DatabaseAdapter_public.KEY_ROWID + " integer primary key autoincrement, " +
			DatabaseAdapter_public.KEY_DATE + " text not null, " + 
			DatabaseAdapter_wb013.KEY_DATETIME + " text not null, " + 
			DatabaseAdapter_public.KEY_PROFILE_ID + " int not null, " + 
			KEY_BPM_SYSTOLIC + " int not null, " +
			KEY_BPM_DIASTOLIC + " int not null, " +
			KEY_BPM_PULSE_RATE + " int not null" +
			"); "; 	
	
	//////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////
		
	private final Context context;
	private DatabaseOpenHelper databaseOpenHelper;
	private SQLiteDatabase db;
	
	public DatabaseAdapter_bpm(Context ctx){
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
	public DatabaseAdapter_bpm openDatabase(){
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
	 * 
	 * @param profileID
	 * @param date
	 * @param sys
	 * @param dia
	 * @param hr
	 * @return
	 */
	public long insert_bpm(int profileID, Calendar date, int sys, int dia, int hr) {
		String dateStr = CalendarHelper.getYyyy_MM_dd(date);
		String datetimeStr = CalendarHelper.getYyyy_MM_dd_HH_mm_ss(date);
		Log.d(TAG, "insert bpm " + datetimeStr + ", sys:" + sys + ", dia:" + dia + ", hr:" + hr + ", profile id:" + profileID);
		
		ContentValues initialValues = new ContentValues();
		initialValues.put(DatabaseAdapter_public.KEY_DATE, dateStr);
		initialValues.put(DatabaseAdapter_wb013.KEY_DATETIME, datetimeStr);
		initialValues.put(KEY_BPM_SYSTOLIC, sys);
		initialValues.put(KEY_BPM_DIASTOLIC, dia);
		initialValues.put(KEY_BPM_PULSE_RATE, hr);
		initialValues.put(DatabaseAdapter_public.KEY_PROFILE_ID, profileID);

		return db.insert(TABLE_BPM, null, initialValues);
	}
	
	
	/**
	 * 
	 * @param profileID
	 * @param date
	 * @param sys
	 * @param dia
	 * @param hr
	 * @return
	 */
	public int update_bpm(int profileID, Calendar date, int sys, int dia, int hr) {
		String dateStr = CalendarHelper.getYyyy_MM_dd(date);
		String datetimeStr = CalendarHelper.getYyyy_MM_dd_HH_mm_ss(date);
		Log.d(TAG, "update bpm " + datetimeStr + ", sys:" + sys + ", dia:" + dia + ", hr:" + hr + ", profile id:" + profileID);
		
		ContentValues initialValues = new ContentValues();
		initialValues.put(DatabaseAdapter_public.KEY_DATE, dateStr);
		initialValues.put(DatabaseAdapter_wb013.KEY_DATETIME, datetimeStr);
		initialValues.put(KEY_BPM_SYSTOLIC, sys);
		initialValues.put(KEY_BPM_DIASTOLIC, dia);
		initialValues.put(KEY_BPM_PULSE_RATE, hr);
		initialValues.put(DatabaseAdapter_public.KEY_PROFILE_ID, profileID);
		
		return db.update(TABLE_BPM, initialValues, DatabaseAdapter_wb013.KEY_DATETIME + "='" + datetimeStr + "' and " + DatabaseAdapter_public.KEY_PROFILE_ID + "=" + profileID, null);
	}
	
	
	public void delete_bpm(int profileID, Calendar date) {
		if (date != null) {
			String datetimeStr = CalendarHelper.getYyyy_MM_dd_HH_mm_ss(date);
			Log.d(TAG, "delete bpm " + datetimeStr + ", profile id:" + profileID);
			
			db.delete(TABLE_BPM, DatabaseAdapter_wb013.KEY_DATETIME + "='" + datetimeStr + "' and " + DatabaseAdapter_public.KEY_PROFILE_ID + "=" + profileID, null);
		}
		
	}
	
	
	/**
	 * 
	 * @param profileID
	 * @param limit
	 * @return
	 */
	public Cursor query_bpm_desc(int profileID, int limit) {
		
		Cursor mCursor = db.query(true, TABLE_BPM, 
				new String[]{DatabaseAdapter_wb013.KEY_DATETIME, KEY_BPM_SYSTOLIC, KEY_BPM_DIASTOLIC, KEY_BPM_PULSE_RATE},
				DatabaseAdapter_public.KEY_PROFILE_ID + "=" + profileID, 
				null, null, null, DatabaseAdapter_wb013.KEY_DATETIME + " desc", String.valueOf(limit));
		
		return mCursor;
	}
	
	
	/**
	 * 
	 * @param profileID
	 * @param limit
	 * @return
	 */
	public Cursor query_bpm_asc(int profileID, int limit) {
		
		Cursor mCursor = db.query(true, TABLE_BPM, 
				new String[]{DatabaseAdapter_wb013.KEY_DATETIME, KEY_BPM_SYSTOLIC, KEY_BPM_DIASTOLIC, KEY_BPM_PULSE_RATE},
				DatabaseAdapter_public.KEY_PROFILE_ID + "=" + profileID, 
				null, null, null, DatabaseAdapter_wb013.KEY_DATETIME + " asc", String.valueOf(limit));
		
		return mCursor;
	}
	
	/**
	 * 
	 * @param profileID
	 * @param limit
	 * @return
	 */
	public Cursor query_bpm_date_asc(int profileID, Calendar date, int limit) {
		String dateStr = CalendarHelper.getYyyy_MM_dd(date);
		Log.i("test", "query bpm  " + dateStr);
		Cursor mCursor = db.query(true, TABLE_BPM, 
				new String[]{DatabaseAdapter_wb013.KEY_DATETIME, KEY_BPM_SYSTOLIC, KEY_BPM_DIASTOLIC, KEY_BPM_PULSE_RATE},
				DatabaseAdapter_public.KEY_PROFILE_ID + "=" + profileID + " and " + DatabaseAdapter_public.KEY_DATE + "='" + dateStr + "'", 
				null, null, null, DatabaseAdapter_wb013.KEY_DATETIME + " desc", String.valueOf(limit));
		
		return mCursor;
	}
}
