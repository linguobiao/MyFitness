package com.lgb.myfitness.database;

import java.util.ArrayList;
import java.util.List;
import com.lgb.myfitness.been.Alarm;
import com.lgb.myfitness.been.Band;
import com.lgb.myfitness.been.Goal;
import com.lgb.myfitness.been.Profile;
import com.lgb.myfitness.been.Reminder;
import android.content.Context;
import android.database.Cursor;


public class DatabaseProvider_public {
	/**
	 * query profile
	 * @param context
	 * @return
	 */
	public static List<Profile> queryProfile(Context context) {
		List<Profile> profileList = new ArrayList<Profile>();
		
		DatabaseAdapter_public databaseAdapter = new DatabaseAdapter_public(context);
		databaseAdapter.openDatabase();
		Cursor cursor = databaseAdapter.query_profile();
		if (cursor.moveToFirst()){
			do {
				String name = cursor.getString(0);
				int age = cursor.getInt(1);
				int gender = cursor.getInt(2);
				double height = cursor.getDouble(3);
				double weight = cursor.getDouble(4);
				int id = cursor.getInt(5);
				
				Profile profile = new Profile();
				profile.setName(name);
				profile.setAge(age);
				profile.setGender(gender);
				profile.setHeight(height);
				profile.setWeight(weight);
				profile.setID(id);
				
				profileList.add(profile);
			} while (cursor.moveToNext());
		}
		
		if (cursor != null) {
			cursor.close();
		}
		databaseAdapter.closeDatabase();
		
		return profileList;
	}
	
	
	/**
	 * query profile
	 * @param context
	 * @param nameQuery
	 * @return
	 */
	public static Profile queryProfile(Context context, String nameQuery) {
		Profile profile = null;
		
		DatabaseAdapter_public databaseAdapter = new DatabaseAdapter_public(context);
		databaseAdapter.openDatabase();
		Cursor cursor = databaseAdapter.query_profile(nameQuery);
		if (cursor.moveToFirst()){
			String name = cursor.getString(0);
			int age = cursor.getInt(1);
			int gender = cursor.getInt(2);
			Double height = cursor.getDouble(3);
			Double weight = cursor.getDouble(4);
			int id = cursor.getInt(5);
			
			profile = new Profile();
			profile.setName(name);
			profile.setAge(age);
			profile.setGender(gender);
			profile.setHeight(height);
			profile.setWeight(weight);
			profile.setID(id);
				
		}
		
		if (cursor != null) {
			cursor.close();
		}
		databaseAdapter.closeDatabase();
		
		return profile;
	}
	
	
	/**
	 * insert profile
	 * @param context
	 * @param profile
	 */
	public static void insertProfile(Context context, Profile profile) {
		if (profile != null) {
			DatabaseAdapter_public databaseAdapter = new DatabaseAdapter_public(context);
			databaseAdapter.openDatabase();

			databaseAdapter.insert_profile(
					profile.getName(), profile.getAge(), 
					profile.getGender(), 
					profile.getHeight(), profile.getWeight());

			databaseAdapter.closeDatabase();
		}
	}
	
	/**
	 * update profile
	 * @param context
	 * @param profile
	 */
	public static void updateProfile(Context context, String oldName, Profile profile) {
		if (oldName != null && profile != null) {
			DatabaseAdapter_public databaseAdapter = new DatabaseAdapter_public(context);
			databaseAdapter.openDatabase();

			databaseAdapter.update_profile(oldName, profile.getName(), profile.getAge(), profile.getGender(), 
					profile.getHeight(), profile.getWeight());

			databaseAdapter.closeDatabase();
		}
	}
	
	/**
	 * query alarm
	 * @param context
	 * @return
	 */
	public static Alarm queryAlarm(Context context, int profileIDQuery) {
		Alarm alarm = null;
		
		DatabaseAdapter_public databaseAdapter = new DatabaseAdapter_public(context);
		databaseAdapter.openDatabase();
		Cursor cursor = databaseAdapter.query_alarm(profileIDQuery);
		if (cursor.moveToFirst()){
			int state = cursor.getInt(0);
			int hour = cursor.getInt(1);
			int minute = cursor.getInt(2);
			int duration = cursor.getInt(3);
			int monday = cursor.getInt(4);
			int tuesday = cursor.getInt(5);
			int wednesday = cursor.getInt(6);
			int thursday = cursor.getInt(7);
			int friday = cursor.getInt(8);
			int saturday = cursor.getInt(9);
			int sunday = cursor.getInt(10);
			
			alarm = new Alarm();
			alarm.setState(state);
			alarm.setHour(hour);
			alarm.setMinute(minute);
			alarm.setDuration(duration);
			alarm.setMonday(monday);
			alarm.setTuesday(tuesday);
			alarm.setWednesday(wednesday);
			alarm.setThursday(thursday);
			alarm.setFriday(friday);
			alarm.setSaturday(saturday);
			alarm.setSunday(sunday);
		}
		
		if (cursor != null) {
			cursor.close();
		}
		databaseAdapter.closeDatabase();
		
		return alarm;
	}
	
	/**
	 * insert alarm
	 * @param context
	 * @param alarm
	 */
	public static void insertAlarm(Context context, int profileID, Alarm alarm) {
		if (alarm != null) {
			DatabaseAdapter_public databaseAdapter = new DatabaseAdapter_public(context);
			databaseAdapter.openDatabase();

			databaseAdapter.insert_alarm(profileID, alarm.getState(), alarm.getHour(), alarm.getMinute(), alarm.getDuration(),
					alarm.getMonday(), alarm.getTuesday(), alarm.getWednesday(), alarm.getThursday(), alarm.getFriday(), 
					alarm.getSaturday(), alarm.getSunday());

			databaseAdapter.closeDatabase();
		}
	}
	
	
	/**
	 * update alarm
	 * @param context
	 * @param alarm
	 */
	public static void updateAlarm(Context context, int profileIDUpdate, Alarm alarm) {
		if (alarm != null) {
			DatabaseAdapter_public databaseAdapter = new DatabaseAdapter_public(context);
			databaseAdapter.openDatabase();

			databaseAdapter.update_alarm(profileIDUpdate, alarm.getState(), alarm.getHour(), alarm.getMinute(), alarm.getDuration(),
					alarm.getMonday(), alarm.getTuesday(), alarm.getWednesday(), alarm.getThursday(), alarm.getFriday(), 
					alarm.getSaturday(), alarm.getSunday());

			databaseAdapter.closeDatabase();
		}
	}
	
	
	/**
	 * query reminder
	 * @param context
	 * @param profileIDQuery
	 * @return
	 */
	public static Reminder queryReminder(Context context, int profileIDQuery) {
		Reminder reminder = null;
		
		DatabaseAdapter_public databaseAdapter = new DatabaseAdapter_public(context);
		databaseAdapter.openDatabase();
		Cursor cursor = databaseAdapter.query_reminder(profileIDQuery);
		if (cursor.moveToFirst()){
			int state = cursor.getInt(0);
			int hour_begin = cursor.getInt(1);
			int hour_end = cursor.getInt(2);
			int interval = cursor.getInt(3);
			int monday = cursor.getInt(4);
			int tuesday = cursor.getInt(5);
			int wednesday = cursor.getInt(6);
			int thursday = cursor.getInt(7);
			int friday = cursor.getInt(8);
			int saturday = cursor.getInt(9);
			int sunday = cursor.getInt(10);
			
			reminder = new Reminder();
			reminder.setState(state);
			reminder.setBegin_hour(hour_begin);
			reminder.setEnd_hour(hour_end);
			reminder.setInterval(interval);
			reminder.setMonday(monday);
			reminder.setTuesday(tuesday);
			reminder.setWednesday(wednesday);
			reminder.setThursday(thursday);
			reminder.setFriday(friday);
			reminder.setSaturday(saturday);
			reminder.setSunday(sunday);
		}
		
		if (cursor != null) {
			cursor.close();
		}
		databaseAdapter.closeDatabase();
		
		return reminder;
	}
	
	
	/**
	 * insert reminder
	 * @param context
	 * @param profileID
	 * @param reminder
	 */
	public static void insertReminder(Context context, int profileID, Reminder reminder) {
		if (reminder != null) {
			DatabaseAdapter_public databaseAdapter = new DatabaseAdapter_public(context);
			databaseAdapter.openDatabase();

			databaseAdapter.insert_reminder(profileID, reminder.getState(), reminder.getBegin_hour(), reminder.getEnd_hour(), 
					reminder.getInterval(), 
					reminder.getMonday(), reminder.getTuesday(), reminder.getWednesday(), reminder.getThursday(), reminder.getFriday(), 
					reminder.getSaturday(), reminder.getSunday());

			databaseAdapter.closeDatabase();
		}
	}
	
	
	/**
	 * update reminder
	 * @param context
	 * @param profileIDUpdate
	 * @param reminder
	 */
	public static void updateReminder(Context context, int profileIDUpdate, Reminder reminder) {
		if (reminder != null) {
			DatabaseAdapter_public databaseAdapter = new DatabaseAdapter_public(context);
			databaseAdapter.openDatabase();

			databaseAdapter.update_reminder(profileIDUpdate, reminder.getState(), reminder.getBegin_hour(), reminder.getEnd_hour(), 
					reminder.getInterval(), 
					reminder.getMonday(), reminder.getTuesday(), reminder.getWednesday(), reminder.getThursday(), reminder.getFriday(), 
					reminder.getSaturday(), reminder.getSunday());

			databaseAdapter.closeDatabase();
		}
	}
	
	
	/**
	 * query goal
	 * @param context
	 * @param profileIDQuery
	 * @return
	 */
	public static Goal queryGoal(Context context, int profileIDQuery) {
		Goal goal = null;
		
		DatabaseAdapter_public databaseAdapter = new DatabaseAdapter_public(context);
		databaseAdapter.openDatabase();
		
		Cursor cursor = databaseAdapter.query_goal(profileIDQuery);
		if (cursor.moveToFirst()){
			goal = new Goal();
			goal.setStep(cursor.getInt(0));
			goal.setBurn(cursor.getInt(1));
			goal.setSleep(cursor.getDouble(2));
		}
		
		if (cursor != null) {
			cursor.close();
		}
		databaseAdapter.closeDatabase();
		
		return goal;
	}
	
	
	/**
	 * insert goal
	 * @param context
	 * @param profileID
	 * @param goal
	 */
	public static void insertGoal(Context context, int profileID, Goal goal) {
		if (goal != null) {
			DatabaseAdapter_public databaseAdapter = new DatabaseAdapter_public(context);
			databaseAdapter.openDatabase();

			databaseAdapter.insert_goal(goal.getStep(), goal.getBurn(), goal.getSleep(), profileID);

			databaseAdapter.closeDatabase();
		}
	}
	
	/**
	 * update goal
	 * @param context
	 * @param profileIDUpdate
	 * @param goal
	 */
	public static void updateGoal(Context context, int profileIDUpdate, Goal goal) {
		if (goal != null) {
			DatabaseAdapter_public databaseAdapter = new DatabaseAdapter_public(context);
			databaseAdapter.openDatabase();

			databaseAdapter.update_goal(profileIDUpdate, goal.getStep(), goal.getBurn(), goal.getSleep());

			databaseAdapter.closeDatabase();
		}
	}
	
	
	/**
	 * insert device
	 * @param context
	 * @param band
	 */
	public static void insertDevice(Context context, Band band) {
		if (context != null && band != null) {
			DatabaseAdapter_public databaseAdapter = new DatabaseAdapter_public(context);
			databaseAdapter.openDatabase();

			databaseAdapter.insert_device(band.getAddress(), band.getName());

			databaseAdapter.closeDatabase();
		}
	}
	
	
	/**
	 * query device
	 * @param context
	 * @param deviceAddress
	 * @return
	 */
	public static Band queryDevice(Context context, String deviceAddress) {
		
		Band band = null;
		
		DatabaseAdapter_public databaseAdapter = new DatabaseAdapter_public(context);
		databaseAdapter.openDatabase();
		
		Cursor cursor = databaseAdapter.query_device(deviceAddress);
		if (cursor.moveToFirst()){
			band = new Band();
			band.setDeviceID(cursor.getInt(0));
			band.setAddress(cursor.getString(1));
			band.setName(cursor.getString(2));
		}
		
		if (cursor != null) {
			cursor.close();
		}
		databaseAdapter.closeDatabase();
		
		return band;
	}
	
	
	/**
	 * 删除所有数据表
	 * @param context
	 */
	public static void deleteAllTable(Context context) {
		DatabaseAdapter_public databaseAdapter = new DatabaseAdapter_public(context);
		databaseAdapter.openDatabase();

		databaseAdapter.deleteAllData();

		databaseAdapter.closeDatabase();
	}
}
