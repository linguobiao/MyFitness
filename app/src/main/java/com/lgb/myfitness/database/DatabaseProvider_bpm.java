package com.lgb.myfitness.database;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.lgb.myfitness.been.BPM;
import com.lgb.myfitness.global.Global;
import com.lgb.myfitness.helper.CalendarHelper;


public class DatabaseProvider_bpm {
	
	/**
	 * 
	 * @param context
	 * @param profileID
	 * @param limit
	 * @return
	 */
	public static List<BPM> queryBPM_desc(Context context, int profileID, int limit) {
		
		if (context != null) {
			
			if (limit < 0) {
				limit = 0;
			}

			DatabaseAdapter_bpm databaseAdapter = new DatabaseAdapter_bpm(context);
			databaseAdapter.openDatabase();
			
			List<BPM> bpmList = new ArrayList<BPM>();
			Cursor cursor = databaseAdapter.query_bpm_desc(profileID, limit);
			if (cursor.moveToFirst()) {

				do {
					BPM bpm = new BPM();
					Date _date = new Date();
					try {
						_date = Global.sdf_4.parse(cursor.getString(cursor.getColumnIndex(DatabaseAdapter_wb013.KEY_DATETIME)));
					} catch (ParseException e) {
						e.printStackTrace();
					}
					Calendar _cal = Calendar.getInstance();
					_cal.setTime(_date);
					
					bpm.setDatetime(_cal);
					bpm.setSystolic(cursor.getInt(cursor.getColumnIndex(DatabaseAdapter_bpm.KEY_BPM_SYSTOLIC)));
					bpm.setDiatolic(cursor.getInt(cursor.getColumnIndex(DatabaseAdapter_bpm.KEY_BPM_DIASTOLIC)));	
					bpm.setHeartRate(cursor.getInt(cursor.getColumnIndex(DatabaseAdapter_bpm.KEY_BPM_PULSE_RATE)));
					
					bpmList.add(bpm);
					
				} while (cursor.moveToNext());
			}
			
			if (cursor != null) {
				cursor.close();
			}
		
			databaseAdapter.closeDatabase();
			
			return bpmList;
		}

		return null;
		
	}
	
	
	public static List<BPM> queryBPM_asc(Context context, int profileID, int limit) {
		
		if (context != null) {
			
			if (limit < 0) {
				limit = 0;
			}

			DatabaseAdapter_bpm databaseAdapter = new DatabaseAdapter_bpm(context);
			databaseAdapter.openDatabase();
			
			List<BPM> bpmList = new ArrayList<BPM>();
			Cursor cursor = databaseAdapter.query_bpm_asc(profileID, limit);
			if (cursor.moveToFirst()) {

				do {
					BPM bpm = new BPM();
					Date _date = new Date();
					try {
						_date = Global.sdf_4.parse(cursor.getString(cursor.getColumnIndex(DatabaseAdapter_wb013.KEY_DATETIME)));
					} catch (ParseException e) {
						e.printStackTrace();
					}
					Calendar _cal = Calendar.getInstance();
					_cal.setTime(_date);
					
					bpm.setDatetime(_cal);
					bpm.setSystolic(cursor.getInt(cursor.getColumnIndex(DatabaseAdapter_bpm.KEY_BPM_SYSTOLIC)));
					bpm.setDiatolic(cursor.getInt(cursor.getColumnIndex(DatabaseAdapter_bpm.KEY_BPM_DIASTOLIC)));	
					bpm.setHeartRate(cursor.getInt(cursor.getColumnIndex(DatabaseAdapter_bpm.KEY_BPM_PULSE_RATE)));
					
					bpmList.add(bpm);
					
				} while (cursor.moveToNext());
			}
			
			if (cursor != null) {
				cursor.close();
			}
		
			databaseAdapter.closeDatabase();
			
			return bpmList;
		}

		return null;
		
	}
	
public static List<BPM> queryBPM_30Day_asc(Context context, int profileID) {
		
		if (context != null) {
			
			Calendar today = CalendarHelper.getToday();
			Calendar lastMonth = CalendarHelper.getLastMonth(today);

			DatabaseAdapter_bpm databaseAdapter = new DatabaseAdapter_bpm(context);
			databaseAdapter.openDatabase();
			
			List<BPM> bpmList = new ArrayList<BPM>();
			
			while(!lastMonth.after(today)) {
				Log.i("test", "" + Global.sdf_2.format(lastMonth.getTime()));
				Cursor cursor = databaseAdapter.query_bpm_date_asc(profileID, lastMonth, 1);
				if (cursor.moveToFirst()) {

					do {
						BPM bpm = new BPM();
						Date _date = new Date();
						try {
							_date = Global.sdf_4.parse(cursor.getString(cursor.getColumnIndex(DatabaseAdapter_wb013.KEY_DATETIME)));
						} catch (ParseException e) {
							e.printStackTrace();
						}
						Calendar _cal = Calendar.getInstance();
						_cal.setTime(_date);
						
						bpm.setDatetime(_cal);
						bpm.setSystolic(cursor.getInt(cursor.getColumnIndex(DatabaseAdapter_bpm.KEY_BPM_SYSTOLIC)));
						bpm.setDiatolic(cursor.getInt(cursor.getColumnIndex(DatabaseAdapter_bpm.KEY_BPM_DIASTOLIC)));	
						bpm.setHeartRate(cursor.getInt(cursor.getColumnIndex(DatabaseAdapter_bpm.KEY_BPM_PULSE_RATE)));
						
						bpmList.add(bpm);
						
					} while (cursor.moveToNext());
				} else {
					BPM bpm = new BPM();
					
					Calendar cal = Calendar.getInstance();
					cal.setTimeInMillis(lastMonth.getTimeInMillis());
					bpm.setDatetime(cal);
					bpm.setSystolic(-1);
					bpm.setDiatolic(-1);	
					bpm.setHeartRate(-1);
					bpmList.add(bpm);
				}
				
				if (cursor != null) {
					cursor.close();
				}
				lastMonth.set(Calendar.DATE, lastMonth.get(Calendar.DATE) + 1);
			}
			
			databaseAdapter.closeDatabase();
			Log.i("test", "bpmList size = " + bpmList.size());
			String value = "";
			for (int i = 0; i < bpmList.size(); i ++) {
				value = bpmList.get(i).getSystolic() + "";
				Log.i("test", Global.sdf_2.format(bpmList.get(i).getDatetime().getTime()) + ",  " +value);
				
			}
			return bpmList;
		}

		return null;
		
	}
	
	
	public static void insertBPM(Context context, int profileID, BPM bpm) {
		if (context != null && bpm != null) {
			
			DatabaseAdapter_bpm db = new DatabaseAdapter_bpm(context);
			db.openDatabase();
			
			db.insert_bpm(
					profileID, 
					bpm.getDatetime(), 
					bpm.getSystolic(), 
					bpm.getDiatolic(), 
					bpm.getHeartRate());
			
			db.closeDatabase();
		}
	}
	
	
	public static void updateBPM(Context context, int profileID, BPM bpm) {
		if (context != null && bpm != null) {
			
			DatabaseAdapter_bpm db = new DatabaseAdapter_bpm(context);
			db.openDatabase();
			
			db.update_bpm(
					profileID, 
					bpm.getDatetime(), 
					bpm.getSystolic(), 
					bpm.getDiatolic(), 
					bpm.getHeartRate());
			
			db.closeDatabase();
		}
	}
	
	
	public static void deleteBPM(Context context, int profileID, Calendar cal) {
		if (context != null && cal != null) {
			DatabaseAdapter_bpm db = new DatabaseAdapter_bpm(context);
			db.openDatabase();
			
			db.delete_bpm(profileID, cal);
			
			db.closeDatabase();
			
		}
	}
}
