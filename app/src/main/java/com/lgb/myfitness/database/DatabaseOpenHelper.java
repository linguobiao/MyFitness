package com.lgb.myfitness.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseOpenHelper extends SQLiteOpenHelper{
	private static String TAG = "DatabaseOpenHelper";
	
	public DatabaseOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DatabaseAdapter_public.CREATE_TABLE_PROFILE);
		db.execSQL(DatabaseAdapter_public.CREATE_TABLE_ALARM);
		db.execSQL(DatabaseAdapter_public.CREATE_TABLE_REMINDER);
		db.execSQL(DatabaseAdapter_public.CREATE_TABLE_GOAL);
		db.execSQL(DatabaseAdapter_public.CREATE_TABLE_DEVICE);
		
		db.execSQL(DatabaseAdapter_scale.CREATE_TABLE_SCALE);
		db.execSQL(DatabaseAdapter_scale.CREATE_TABLE_GOAL_WEIGHT);
		
		db.execSQL(DatabaseAdapter_wb013.CREATE_TABLE_HISTORY_DAY_S);
		db.execSQL(DatabaseAdapter_wb013.CREATE_TABLE_HISTORY_HOUR_S);
		db.execSQL(DatabaseAdapter_wb013.CREATE_TABLE_SCREEN);
		db.execSQL(DatabaseAdapter_wb013.CREATE_TABLE_RESET_TIME);
		db.execSQL(DatabaseAdapter_wb013.CREATE_TABLE_RESET_HISTORY_HOUR);
		
		db.execSQL(DatabaseAdapter_bpm.CREATE_TABLE_BPM);
		
		db.execSQL(DatabaseAdapter_wristband.CREATE_TABLE_HISTORY_DAY_L);
		db.execSQL(DatabaseAdapter_wristband.CREATE_TABLE_HISTORY_HOUR_L);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
				+ newVersion + ", which will destroy all old data");
//		db.execSQL("DROP TABLE IF EXISTS " + DatabaseAdapter_public.TABLE_PROFLIE);
//		db.execSQL("DROP TABLE IF EXISTS " + DatabaseAdapter_public.TABLE_ALARM);
//		db.execSQL("DROP TABLE IF EXISTS " + DatabaseAdapter_public.TABLE_GOAL);
//		db.execSQL("DROP TABLE IF EXISTS " + DatabaseAdapter_public.TABLE_DEVICE);
//		db.execSQL("DROP TABLE IF EXISTS " + DatabaseAdapter_public.TABLE_SCALE);
//		db.execSQL("DROP TABLE IF EXISTS " + DatabaseAdapter_public.TABLE_GOAL_WEIGHT);
//		
//		db.execSQL("DROP TABLE IF EXISTS " + DatabaseAdapter_wb013.TABLE_HISTORY_DAY_S);
//		db.execSQL("DROP TABLE IF EXISTS " + DatabaseAdapter_wb013.TABLE_HISTORY_HOUR_S);
//		db.execSQL("DROP TABLE IF EXISTS " + DatabaseAdapter_wb013.TABLE_SCREEN_S);
//
//		db.execSQL("DROP TABLE IF EXISTS " + DatabaseAdapter_wristband.TABLE_HISTORY_DAY_L);
//		db.execSQL("DROP TABLE IF EXISTS " + DatabaseAdapter_wristband.TABLE_HISTORY_HOUR_L);
//		
//		onCreate(db);
		
		db.execSQL("DROP TABLE IF EXISTS " + DatabaseAdapter_public.TABLE_REMINDER);
		db.execSQL("DROP TABLE IF EXISTS " + DatabaseAdapter_wb013.TABLE_RESET_TIME);
		db.execSQL("DROP TABLE IF EXISTS " + DatabaseAdapter_wb013.TABLE_RESET_HISTORY_HOUR);
		
		db.execSQL("DROP TABLE IF EXISTS " + DatabaseAdapter_bpm.TABLE_BPM);
		
		db.execSQL(DatabaseAdapter_public.CREATE_TABLE_REMINDER);
		db.execSQL(DatabaseAdapter_wb013.CREATE_TABLE_RESET_TIME);
		db.execSQL(DatabaseAdapter_wb013.CREATE_TABLE_RESET_HISTORY_HOUR);
		
		db.execSQL(DatabaseAdapter_bpm.CREATE_TABLE_BPM);
		
	}

}
