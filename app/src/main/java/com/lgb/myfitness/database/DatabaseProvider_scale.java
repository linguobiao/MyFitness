package com.lgb.myfitness.database;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.database.Cursor;

import com.lgb.myfitness.been.Scale;
import com.lgb.myfitness.global.Global;

public class DatabaseProvider_scale {
	/**
	 * query scale
	 * @param context
	 * @return
	 */
	public static Scale queryScale(Context context, int profileID, Calendar date) {
		
		if (context != null && date != null) {

			DatabaseAdapter_scale databaseAdapter = new DatabaseAdapter_scale(
					context);
			databaseAdapter.openDatabase();
			Cursor cursor = databaseAdapter.query_scale(profileID, date);
			if (cursor.moveToFirst()) {

				Scale scale = new Scale();
				Date _date = new Date();
				try {
					_date = Global.sdf_2.parse(cursor.getString(0));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				Calendar _cal = Calendar.getInstance();
				_cal.setTime(_date);

				scale.setDate(_cal);
				scale.setWeight(cursor.getDouble(1));
				scale.setBMI(cursor.getDouble(2));

				databaseAdapter.closeDatabase();
				return scale;
			}

			if (cursor != null) {
				cursor.close();
			}
			databaseAdapter.closeDatabase();
		}

		return null;
		
	}
	
	/**
	 * 查询一个时间段的电子称数据
	 * @param context
	 * @param begin
	 * @param end
	 * @return
	 */
	public static List<Scale> queryScale(Context context, int profileID, Calendar begin, Calendar end) {
		
		List<Scale> scaleList = new ArrayList<Scale>();
		
		if (context != null && begin != null && end != null) {
			
			DatabaseAdapter_scale databaseAdapter = new DatabaseAdapter_scale(context);
			// 打开数据库
			databaseAdapter.openDatabase();
			Cursor cursor = databaseAdapter.query_scale(profileID, begin, end);
			if (cursor.moveToFirst()){
				do {
					
					Scale scale = new Scale();
					Date _date = new Date();
					try {
						_date = Global.sdf_2.parse(cursor.getString(0));
					} catch (ParseException e) {
						e.printStackTrace();
					}
					Calendar _cal = Calendar.getInstance();
					_cal.setTime(_date);
					
					scale.setDate(_cal);
					scale.setWeight(cursor.getDouble(1));
					scale.setBMI(cursor.getDouble(2));
					
					scaleList.add(scale);
					
					} while (cursor.moveToNext());
			}
			
			cursor.close();
			databaseAdapter.closeDatabase();
		}
		
		return scaleList;
	}
	
	
	/**
	 * insert scale
	 * @param context
	 * @param scale
	 */
	public static void insertScale(Context context,int profileID, Scale scale) {
		if (scale != null) {
			DatabaseAdapter_scale databaseAdapter = new DatabaseAdapter_scale(context);
			databaseAdapter.openDatabase();
			
			databaseAdapter.insert_scale(profileID, scale.getDate(), scale.getWeight(), scale.getBMI());
			
			databaseAdapter.closeDatabase();
		}
	}
	
	/**
	 * update scale
	 * @param context
	 * @param scale
	 */
	public static void updateScale(Context context,int profileID, Scale scale) {
		if (scale != null) {
			DatabaseAdapter_scale databaseAdapter = new DatabaseAdapter_scale(context);
			databaseAdapter.openDatabase();
			
			databaseAdapter.update_scale(profileID, scale.getDate(), scale.getWeight(), scale.getBMI());
			
			databaseAdapter.closeDatabase();
		}
	}
	
	
	public static void insertGoalWeight(Context context, int profileID, double goalWeight) {
		if (context != null) {
			DatabaseAdapter_scale databaseAdapter = new DatabaseAdapter_scale(context);
			databaseAdapter.openDatabase();
			
			databaseAdapter.insert_goal_weight(profileID, goalWeight);
			
			databaseAdapter.closeDatabase();
		}
	}
	
	
	public static void updateGoalWeight(Context context, int profileID, double goalWeight) {
		if (context != null) {
			DatabaseAdapter_scale databaseAdapter = new DatabaseAdapter_scale(context);
			databaseAdapter.openDatabase();
			
			databaseAdapter.update_goal_weight(profileID, goalWeight);
			
			databaseAdapter.closeDatabase();
		}
	}
	
	
	
	public static Double queryGoalWeight(Context context, int profileID) {
		Double value = null;
		
		if (context != null) {
			DatabaseAdapter_scale databaseAdapter = new DatabaseAdapter_scale(context);
			databaseAdapter.openDatabase();
			Cursor cursor = databaseAdapter.query_goal_weight(profileID);
			if (cursor.moveToFirst()){
				
				double goalWeight = cursor.getDouble(0);
				
				cursor.close();
				return goalWeight;
			}
			databaseAdapter.closeDatabase();
		}
		
		return value;
	}
}
