package com.lgb.myfitness.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseAdapter_public {
	
	private static String TAG = "DatabaseAdapter_public";
	
	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "myfitness";
	////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////

	public static final String TABLE_PROFLIE = "TABLE_PROFILE";
	public static final String TABLE_ALARM = "TABLE_ALARM";
	public static final String TABLE_REMINDER = "TABLE_REMINDER";
	public static final String TABLE_GOAL = "TABLE_GOAL";
	public static final String TABLE_DEVICE = "TABLE_DEVICE";
	
	///////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////
	public static final String KEY_ROWID = "_id";
	
	public static final String KEY_DATE = "KEY_DATE";
	public static final String KEY_NAME = "KEY_NAME";
	public static final String KEY_AGE = "KEY_AGE";
	public static final String KEY_GENDER = "KEY_GENDER";
	public static final String KEY_HEIGHT = "KEY_HEIGHT";
	public static final String KEY_WEIGHT = "KEY_WEIGHT";
	public static final String KEY_PROFILE_ID = "KEY_PROFILE_ID";
	
	public static final String KEY_STATE = "KEY_STATE";
	public static final String KEY_HOUR = "KEY_HOUR";
	public static final String KEY_MINUTE = "KEY_MINUTE";
	public static final String KEY_DURATION = "KEY_DURATION";
	public static final String KEY_MONDAY = "KEY_MONDAY";
	public static final String KEY_TUESDAY = "KEY_TUESDAY";
	public static final String KEY_WEDNESDAY = "KEY_WEDNESDAY";
	public static final String KEY_THURSDAY = "KEY_THURSDAY";
	public static final String KEY_FRIDAY = "KEY_FRIDAY";
	public static final String KEY_SATURDAY = "KEY_SATURDAY";
	public static final String KEY_SUNDAY = "KEY_SUNDAY";
	
	public static final String KEY_HOUR_BEGIN = "KEY_HOUR_BEGIN";
	public static final String KEY_HOUR_END = "KEY_HOUR_END";
	public static final String KEY_INTERVAL = "INTERVAL";
	
	public static final String KEY_GOAL_STEP = "KEY_GOAL_STEP";
	public static final String KEY_GOAL_BURN = "KEY_GOAL_BURN";
	public static final String KEY_GOAL_SLEEP = "KEY_GOAL_SLEEP";
	
	public static final String KEY_DEVICE_ADDRESS = "KEY_DEVICE_ADDRESS";
	public static final String KEY_DEVICE_NAME= "KEY_DEVICE_NAME";
	public static final String KEY_DEVICE_ID = "KEY_DEVICE_ID";
	
	public static final String KEY_SCALE_WEIGHT = "KEY_SCALE_WEIGHT";
	public static final String KEY_SCALE_BMI = "KEY_SCALE_BMI";
	
	public static final String KEY_GOAL_WEIGHT = "KEY_GOAL_WEIGHT";
	///////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////
	
	public static final String CREATE_TABLE_PROFILE = "CREATE TABLE IF NOT EXISTS " + TABLE_PROFLIE +
			"(" + KEY_ROWID + " integer primary key autoincrement, " +
			KEY_NAME + " text not null, " +
			KEY_AGE + " int not null, " +
			KEY_GENDER + " int not null, " + 
			KEY_HEIGHT + " double not null, " +
			KEY_WEIGHT + " double not null" + 
			"); ";
	public static final String CREATE_TABLE_ALARM = "CREATE TABLE IF NOT EXISTS " + TABLE_ALARM +
			"(" + KEY_ROWID + " integer primary key autoincrement, " +
			KEY_STATE + " int not null, " + 
			KEY_HOUR + " int not null, " +
			KEY_MINUTE + " int not null, " + 
			KEY_DURATION + " int not null, " +
			KEY_MONDAY + " int not null, " + 
			KEY_TUESDAY + " int not null, " +
			KEY_WEDNESDAY + " int not null, " +
			KEY_THURSDAY + " int not null, " +
			KEY_FRIDAY + " int not null, " +
			KEY_SATURDAY + " int not null, " +
			KEY_SUNDAY + " int not null, " +
			KEY_PROFILE_ID + " int not null" +
			"); ";
	
	public static final String CREATE_TABLE_REMINDER = "CREATE TABLE IF NOT EXISTS " + TABLE_REMINDER +
			"(" + KEY_ROWID + " integer primary key autoincrement, " +
			KEY_STATE + " int not null, " + 
			KEY_HOUR_BEGIN + " int not null, " +
			KEY_HOUR_END + " int not null, " + 
			KEY_INTERVAL + " int not null, " +
			KEY_MONDAY + " int not null, " + 
			KEY_TUESDAY + " int not null, " +
			KEY_WEDNESDAY + " int not null, " +
			KEY_THURSDAY + " int not null, " +
			KEY_FRIDAY + " int not null, " +
			KEY_SATURDAY + " int not null, " +
			KEY_SUNDAY + " int not null, " +
			KEY_PROFILE_ID + " int not null" +
			"); ";	
	
	public static final String CREATE_TABLE_GOAL = "CREATE TABLE IF NOT EXISTS " + TABLE_GOAL +
			"(" + KEY_ROWID + " integer primary key autoincrement, " +
			KEY_GOAL_STEP + " int not null, " + 
			KEY_GOAL_BURN + " int not null, " +
			KEY_GOAL_SLEEP + " double not null, " + 
			KEY_PROFILE_ID + " int not null" +
			"); "; 
	public static final String CREATE_TABLE_DEVICE = "CREATE TABLE IF NOT EXISTS " + TABLE_DEVICE +
			"(" + KEY_ROWID + " integer primary key autoincrement, " +
			KEY_DEVICE_ADDRESS + " text not null, " +
			KEY_DEVICE_NAME + " text not null" +
			"); "; 	
			
	//////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////
		
	private final Context context;
	private DatabaseOpenHelper databaseOpenHelper;
	private SQLiteDatabase db;
	
	public DatabaseAdapter_public(Context ctx){
		this.context = ctx;
		databaseOpenHelper = new DatabaseOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	/**
	 * open database
	 * @return
	 */
	public DatabaseAdapter_public openDatabase(){
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
	 * insert profile
	 * @param gendar
	 * @param height
	 * @param weight
	 * @param datetimeBegin
	 * @param datetimeEnd
	 * @return
	 */
	public long insert_profile(String name, int age, int gendar, double height, double weight){
		
		ContentValues initialValues = new ContentValues();
		
		initialValues.put(KEY_NAME, name);
		initialValues.put(KEY_AGE, age);
		initialValues.put(KEY_GENDER, gendar);
		initialValues.put(KEY_HEIGHT, height);
		initialValues.put(KEY_WEIGHT, weight);

		return db.insert(TABLE_PROFLIE, null, initialValues);
	}
	
	/**
	 * update profile
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @param weight
	 * @param measure
	 * @return the number of rows affected 
	 */
	public int update_profile(String oldName, String name, int age, int gendar, double height, double weight){

		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_NAME, name);
		initialValues.put(KEY_AGE, age);
		initialValues.put(KEY_GENDER, gendar);
		initialValues.put(KEY_HEIGHT, height);
		initialValues.put(KEY_WEIGHT, weight);
		
		return db.update(TABLE_PROFLIE, initialValues, KEY_NAME + "=" + "\'" + oldName + "\'" , null);
	}
	
	/**
	 * query profile
	 * @return
	 */
	public Cursor query_profile(){

		Cursor mCursor = db.query(true, TABLE_PROFLIE, 
				new String[]{KEY_NAME, KEY_AGE, KEY_GENDER, KEY_HEIGHT, KEY_WEIGHT, KEY_ROWID}, 
				null, null, null, null, null, null);
		
		return mCursor;
	}
	
	
	public Cursor query_profile(String name) {
		if (name != null) {
			Cursor mCursor = db.query(true, TABLE_PROFLIE, 
					new String[]{KEY_NAME, KEY_AGE, KEY_GENDER, KEY_HEIGHT, KEY_WEIGHT, KEY_ROWID}, 
					KEY_NAME + "=" + "\'" + name + "\'", 
					null, null, null, null, null);
			
			return mCursor;
		}
		
		return null;
	}
	
	
	public Cursor query_profile(int profileID) {
		Cursor mCursor = db.query(true, TABLE_PROFLIE, 
				new String[]{KEY_NAME, KEY_AGE, KEY_GENDER, KEY_HEIGHT, KEY_WEIGHT, KEY_ROWID}, 
					KEY_ROWID + "=" + profileID , 
				null, null, null, null, null);
		
		return mCursor;
	}
	
	
	/**
	 * insert alarm
	 * @param state
	 * @param hour
	 * @param minute
	 * @param duration
	 * @param monday
	 * @param tuesday
	 * @param wednesday
	 * @param thursday
	 * @param friday
	 * @param saturday
	 * @param sunday
	 * @return
	 */
	public long insert_alarm(int profileID, int state, int hour, int minute, int duration, 
			int monday, int tuesday, int wednesday, int thursday, int friday, int saturday, int sunday ) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_STATE, state);
		initialValues.put(KEY_HOUR, hour);
		initialValues.put(KEY_MINUTE, minute);
		initialValues.put(KEY_DURATION, duration);
		initialValues.put(KEY_MONDAY, monday);
		initialValues.put(KEY_TUESDAY, tuesday);
		initialValues.put(KEY_WEDNESDAY, wednesday);
		initialValues.put(KEY_THURSDAY, thursday);
		initialValues.put(KEY_FRIDAY, friday);
		initialValues.put(KEY_SATURDAY, saturday);
		initialValues.put(KEY_SUNDAY, sunday);
		initialValues.put(KEY_PROFILE_ID, profileID);
		
		return db.insert(TABLE_ALARM, null, initialValues);
	}
	
	/**
	 * update alarm
	 * @return
	 */
	public int update_alarm(int profileIDUpdate, int state, int hour, int minute, int duration, 
			int monday, int tuesday, int wednesday, int thursday, int friday, int saturday, int sunday) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_STATE, state);
		initialValues.put(KEY_HOUR, hour);
		initialValues.put(KEY_MINUTE, minute);
		initialValues.put(KEY_DURATION, duration);
		initialValues.put(KEY_MONDAY, monday);
		initialValues.put(KEY_TUESDAY, tuesday);
		initialValues.put(KEY_WEDNESDAY, wednesday);
		initialValues.put(KEY_THURSDAY, thursday);
		initialValues.put(KEY_FRIDAY, friday);
		initialValues.put(KEY_SATURDAY, saturday);
		initialValues.put(KEY_SUNDAY, sunday);
		
		return db.update(TABLE_ALARM, initialValues, KEY_PROFILE_ID + "=" + profileIDUpdate , null);
	}
	
	/**
	 * query alarm
	 * @return
	 */
	public Cursor query_alarm(int profileIDQuery){
		Cursor mCursor = db.query(true, TABLE_ALARM, 
				new String[]{KEY_STATE, KEY_HOUR, KEY_MINUTE, KEY_DURATION, KEY_MONDAY, KEY_TUESDAY, 
					KEY_WEDNESDAY, KEY_THURSDAY, KEY_FRIDAY, KEY_SATURDAY, KEY_SUNDAY}, 
					KEY_PROFILE_ID + "=" + profileIDQuery, 
					null, null, null, null, null);
		
		return mCursor;
	}
	
	
	/**
	 * insert reminder
	 * @param state
	 * @param hour_begin
	 * @param hour_end
	 * @param interval
	 * @param type
	 * @param monday
	 * @param tuesday
	 * @param wednesday
	 * @param thursday
	 * @param friday
	 * @param saturday
	 * @param sunday
	 * @return
	 */
	public long insert_reminder(int profileID, int state, int hour_begin, int hour_end, int interval,
			int monday, int tuesday, int wednesday, int thursday, int friday, int saturday, int sunday ) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_STATE, state);
		initialValues.put(KEY_HOUR_BEGIN, hour_begin);
		initialValues.put(KEY_HOUR_END, hour_end);
		initialValues.put(KEY_INTERVAL, interval);
		initialValues.put(KEY_MONDAY, monday);
		initialValues.put(KEY_TUESDAY, tuesday);
		initialValues.put(KEY_WEDNESDAY, wednesday);
		initialValues.put(KEY_THURSDAY, thursday);
		initialValues.put(KEY_FRIDAY, friday);
		initialValues.put(KEY_SATURDAY, saturday);
		initialValues.put(KEY_SUNDAY, sunday);
		initialValues.put(KEY_PROFILE_ID, profileID);
		
		return db.insert(TABLE_REMINDER, null, initialValues);
	}
	
	/**
	 * update reminder
	 * @param state
	 * @param hour_begin
	 * @param hour_end
	 * @param interval
	 * @param type
	 * @param monday
	 * @param tuesday
	 * @param wednesday
	 * @param thursday
	 * @param friday
	 * @param saturday
	 * @param sunday
	 * @return
	 */
	public int update_reminder(int profileIDUpdate, int state, int hour_begin, int hour_end, int interval, 
			int monday, int tuesday, int wednesday, int thursday, int friday, int saturday, int sunday) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_STATE, state);
		initialValues.put(KEY_HOUR_BEGIN, hour_begin);
		initialValues.put(KEY_HOUR_END, hour_end);
		initialValues.put(KEY_INTERVAL, interval);
		initialValues.put(KEY_MONDAY, monday);
		initialValues.put(KEY_TUESDAY, tuesday);
		initialValues.put(KEY_WEDNESDAY, wednesday);
		initialValues.put(KEY_THURSDAY, thursday);
		initialValues.put(KEY_FRIDAY, friday);
		initialValues.put(KEY_SATURDAY, saturday);
		initialValues.put(KEY_SUNDAY, sunday);
		
		return db.update(TABLE_REMINDER, initialValues, KEY_PROFILE_ID + "=" + profileIDUpdate , null);
	}
	
	/**
	 * query reminder
	 * @return
	 */
	public Cursor query_reminder(int profileIDQuery){
		Cursor mCursor = db.query(true, TABLE_REMINDER, 
				new String[]{KEY_STATE, KEY_HOUR_BEGIN, KEY_HOUR_END, KEY_INTERVAL, KEY_MONDAY, KEY_TUESDAY, 
					KEY_WEDNESDAY, KEY_THURSDAY, KEY_FRIDAY, KEY_SATURDAY, KEY_SUNDAY}, 
				KEY_PROFILE_ID + "=" + profileIDQuery, 
				null, null, null, null, null);
		
		return mCursor;
	}
	

	/**
	 * 插入目标数据
	 * @param step
	 * @param burn
	 * @param sleep
	 * @return 
	 */
	public long insert_goal(int step, double burn, double sleep, int profileID) {
		ContentValues initialValues = new ContentValues();
		
		initialValues.put(KEY_GOAL_STEP, step);
		initialValues.put(KEY_GOAL_BURN, burn);
		initialValues.put(KEY_GOAL_SLEEP, sleep);
		initialValues.put(KEY_PROFILE_ID, profileID);

		return db.insert(TABLE_GOAL, null, initialValues);
	}
	
	
	/**
	 * 更新目标数据
	 * @param profileIDUpdate
	 * @param step
	 * @param burn
	 * @param sleep
	 * @return
	 */
	public int update_goal(int profileIDUpdate, int step, double burn, double sleep) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_GOAL_STEP, step);
		initialValues.put(KEY_GOAL_BURN, burn);
		initialValues.put(KEY_GOAL_SLEEP, sleep);
		
		return db.update(TABLE_GOAL, initialValues, KEY_PROFILE_ID + "=" + profileIDUpdate , null);
	}
	
	/**
	 * 查询目标数据
	 * @param profileIDQuery
	 * @return
	 */
	public Cursor query_goal(int profileIDQuery) {
		Cursor mCursor = db.query(true, TABLE_GOAL, 
				new String[]{KEY_GOAL_STEP, KEY_GOAL_BURN, KEY_GOAL_SLEEP}, 
					KEY_PROFILE_ID + "=" + profileIDQuery, 
				null, null, null, null, null);
		
		return mCursor;
	}
	
	
	/**
	 * 插入设备
	 * @param deviceAddress
	 * @return
	 */
	public long insert_device(String deviceAddress, String deviceName) {
		ContentValues initialValues = new ContentValues();
		
		initialValues.put(KEY_DEVICE_ADDRESS, deviceAddress);
		initialValues.put(KEY_DEVICE_NAME, deviceName);

		return db.insert(TABLE_DEVICE, null, initialValues);
	}
	
	
	/**
	 * 查询设备
	 * @param deviceAddress
	 * @return
	 */
	public Cursor query_device(String deviceAddress) {
		Cursor mCursor = db.query(true, TABLE_DEVICE, 
				new String[]{KEY_ROWID, KEY_DEVICE_ADDRESS, KEY_DEVICE_NAME}, 
					KEY_DEVICE_ADDRESS + "='" + deviceAddress + "'", 
				null, null, null, null, null);
		
		return mCursor;
	}
	
	
	/**
	 * 删除所有数据
	 */
	public void deleteAllData() {
		db.execSQL("DROP TABLE IF EXISTS " + DatabaseAdapter_public.TABLE_PROFLIE);
		db.execSQL("DROP TABLE IF EXISTS " + DatabaseAdapter_public.TABLE_ALARM);
		db.execSQL("DROP TABLE IF EXISTS " + DatabaseAdapter_public.TABLE_GOAL);
		db.execSQL("DROP TABLE IF EXISTS " + DatabaseAdapter_public.TABLE_DEVICE);
		
		db.execSQL("DROP TABLE IF EXISTS " + DatabaseAdapter_scale.TABLE_SCALE);
		db.execSQL("DROP TABLE IF EXISTS " + DatabaseAdapter_scale.TABLE_GOAL_WEIGHT);
		
		db.execSQL("DROP TABLE IF EXISTS " + DatabaseAdapter_wb013.TABLE_HISTORY_DAY_S);
		db.execSQL("DROP TABLE IF EXISTS " + DatabaseAdapter_wb013.TABLE_HISTORY_HOUR_S);
		db.execSQL("DROP TABLE IF EXISTS " + DatabaseAdapter_wb013.TABLE_SCREEN_S);

		db.execSQL("DROP TABLE IF EXISTS " + DatabaseAdapter_wristband.TABLE_HISTORY_DAY_L);
		db.execSQL("DROP TABLE IF EXISTS " + DatabaseAdapter_wristband.TABLE_HISTORY_HOUR_L);
		
		
		db.execSQL(DatabaseAdapter_public.CREATE_TABLE_PROFILE);
		db.execSQL(DatabaseAdapter_public.CREATE_TABLE_ALARM);
		db.execSQL(DatabaseAdapter_public.CREATE_TABLE_GOAL);
		db.execSQL(DatabaseAdapter_public.CREATE_TABLE_DEVICE);
		
		db.execSQL(DatabaseAdapter_scale.CREATE_TABLE_SCALE);
		db.execSQL(DatabaseAdapter_scale.CREATE_TABLE_GOAL_WEIGHT);
		
		db.execSQL(DatabaseAdapter_wb013.CREATE_TABLE_HISTORY_DAY_S);
		db.execSQL(DatabaseAdapter_wb013.CREATE_TABLE_HISTORY_HOUR_S);
		db.execSQL(DatabaseAdapter_wb013.CREATE_TABLE_SCREEN);
		
		db.execSQL(DatabaseAdapter_wristband.CREATE_TABLE_HISTORY_DAY_L);
		db.execSQL(DatabaseAdapter_wristband.CREATE_TABLE_HISTORY_HOUR_L);
		
		
	}
}
