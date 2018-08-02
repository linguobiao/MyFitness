package com.lgb.myfitness.global;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.UUID;

import android.annotation.SuppressLint;

public class Global {
	
	public static final long TIME_DELAY_CONNECT = 5 * 1000;
//	public static final long TIME_DELAY_RECONNECT = 2 * 1000;
	public static final long TIME_DELAY_STOP_SCAN = 10 * 1000;
	public static final long TIME_DELAY_CHECK_DATA = 2 * 1000;
	public static final long TIME_DELAY_FINISH_SYNC = 2 * 1000;
	public static final long TIME_DELAY_GET_SCREEN = 1 * 1500;
	public static final long TIME_DELAY_SET_UNIT = 1 * 1500;
	public static final long TIME_DELAY_STOP_SYNC = 10 * 1000;
	
	/////////////////////////////////////////////////

	public static final int DEFAULT_GOAL_STEP = 10000;
	public static final int DEFAULT_GOAL_BURN = 500;
	public static final double DEFAULT_GOAL_SLEEP = 8;
	public static final double DEFAULT_HEIGHT = 180;
	public static final double DEFAULT_WEIGHT = 70;
	public static final int DEFAULT_AGE = 18;
	public static final int DEFAULT_GENDER = Global.TYPE_GENDER_MALE;
	public static final double DEFAULT_STEP_DISTANCE = 0.45 * DEFAULT_HEIGHT;
	public static final int DEFAULT_REMINDER_START = 8;
	public static final int DEFAULT_REMINDER_END = 20;
	
	/////////////////////////////////////////////////
	public static final int HANDLER_SCAN_TIME_OUT = 11;
	public static final int HANDLER_DISCONNECTED = 12;
	public static final int HANDLER_SYNC_FINISH = 13;
//	public static final int HANDLER_STOP_CONNECT = 14;
//	public static final int HANDLER_STOP_SYNC = 15;
	public static final int HANDLER_TRY_TO_RECONNECT = 16;
//	public static final int HANDLER_SYNC_TIME_OUT = 17;
	public static final int HANDLER_BOUND_DEVICE = 18;
	public static final int HANDLER_FINISH_SYNC = 19;
	public static final int HANDLER_CONNECT_FAIL = 20;
	
	
	/////////////////////////////////////////////////

	public static final int REQUEST_ENABLE_BLUETOOTH = 11;

	// //////////////////////////////////////////////
	
	public static final String FRAGMENT_BAND_PROGRESS = "FRAGMENT_PROGRESS";
	public static final String FRAGMENT_BAND_ACTIVITY = "FRAGMENT_ACTIVITY";
	public static final String FRAGMENT_BAND_SETTINGS = "FRAGMENT_SETTINGS";
//	public static final String FRAGMENT_SETTINGS_SCALES = "FRAGMENT_SETTINGS_SCALES";
	public static final String FRAGMENT_BAND_SETTINGS_PROFILE = "FRAGMENT_SETTINGS_PROFILE";
	public static final String FRAGMENT_BAND_SETTINGS_GOAL = "FRAGMENT_SETTINGS_GOAL";
	public static final String FRAGMENT_BAND_SETTINGS_REMINDER = "FRAGMENT_SETTINGS_REMINDER";
	public static final String FRAGMENT_BAND_SETTINGS_ALARM = "FRAGMENT_SETTINGS_ALARM";
	public static final String FRAGMENT_BAND_SETTINGS_ABOUT_US = "FRAGMENT_SETTINGS_ABOUT_US";
	public static final String FRAGMENT_BAND_SETTINGS_USER_MANUAL = "FRAGMENT_SETTINGS_USER_MANUAL";
	public static final String FRAGMENT_BAND_SETTINGS_LOST_MODE = "FRAGMENT_SETTINGS_LOST_MODE";
	public static final String FRAGMENT_BAND_SETTINGS_REMIND_FUNCTION = "FRAGMENT_SETTINGS_REMIND_FUNCTION";
	public static final String FRAGMENT_IMPRESSUM = "FRAGMENT_IMPRESSUM";
	public static final String FRAGMENT_DECALRARTION = "FRAGMENT_DECALRARTION";
	
	public static final String FRAGMENT_SCALES = "FRAGMENT_SCALES";
	public static final String FRAGMENT_SCALES_SETTINGS = "FRAGMENT_SCALES_SETTINGS";
	public static final String FRAGMENT_SCALES_SETTINGS_PROFILE = "FRAGMENT_SCALES_SETTINGS_PROFILE";
	public static final String FRAGMENT_SCALES_SETTINGS_ABOUT_US = "FRAGMENT_SCALES_SETTINGS_ABOUT_US";
	
	public static final String FRAGMENT_BPM = "FRAGMENT_BPM";
	public static final String FRAGMENT_BPM_TEST = "FRAGMENT_BPM_TEST";
	public static final String FRAGMENT_BPM_STATISTICS = "FRAGMENT_BPM_STATISTICS";
	public static final String FRAGMENT_BPM_STATISTICS_DETAIL = "FRAGMENT_BPM_STATISTICS_DETAIL";
	public static final String FRAGMENT_BPM_SETTINGS = "FRAGMENT_BPM_SETTINGS";
	public static final String FRAGMENT_BPM_SETTINGS_PROFILE = "FRAGMENT_BPM_SETTINGS_PROFILE";
	public static final String FRAGMENT_BPM_SETTINGS_ABOUT_US = "FRAGMENT_BPM_SETTINGS_ABOUT_US";
	public static final String FRAGMENT_BPM_SETTINGS_LANGUAGE = "FRAGMENT_BPM_SETTINGS_LANGUAGE";
	// ///////////////////////////////////////////////

	public static final String ACTION_BLUETOOTH_ENABLE_CONFORM = "com.lgb.myfitness.ACTION_BLUETOOTH_ENABLE_CONFORM";

//	public static final String ACTION_SET_GOAL_SUCCESS = "ACTION_SET_GOAL_SUCCESS";
//	public static final String ACTION_HISTORY_DATA_CHANGE = "ACTION_RECEIVE_HISTORY_DATA";
//	public static final String ACTION_SET_PROFILE_SUCCESS = "ACTION_SET_PROFILE_SUCCESS";
//	public static final String ACTION_SET_REMIND_SUCCESS = "ACTION_SET_REMIND_SUCCESS";

	public static final String ACTION_DECODER_OK = "ACTION_DECODER_OK";
	public static final String ACTION_TIME_OUT = "ACTION_TIME_OUT";

//	public static final String ACTION_SHOW_STEP = "ACTION_SHOW_STEP";
//	public static final String ACTION_SHOW_SLEEP = "ACTION_SHOW_SLEEP";
//	public static final String ACTION_SHOW_BURN = "ACTION_SHOW_BURN";
	public static final String ACTION_SHOW_PROGRESS = "ACTION_SHOW_PROGRESS";
	
//	public static final String ACTION_SHOW_ACTIVITY = "ACTION_SHOW_ACTIVITY";
//	public static final String ACTION_SHOW_ACTIVITY_FINISH = "ACTION_SHOW_ACTIVITY_FINISH";

	// ///////////////////////////////////////////////
	public static final String KEY_FRAGMENT_NAME = "KEY_FRAGMENT_NAME";
	public static final String KEY_PREF = "KEY_MYFITNESS";
	
	public static final String KEY_IS_NEW_START_UP_WRISTBAND = "KEY_IS_NEW_START_UP_WRISTBAND";
	public static final String KEY_IS_NEW_START_UP_SCALE = "KEY_IS_NEW_START_UP_SCALE";
	public static final String KEY_IS_NEW_START_UP_BPM = "KEY_IS_NEW_START_UP_BPM";
	
	public static final String KEY_PROFILE_NAME = "KEY_PROFILE_NAME";
	public static final String KEY_DEVICE_ADDRESS = "KEY_DEVICE_ADDRESS";
	public static final String KEY_DEVICE_ID = "KEY_DEVICE_ID";
	public static final String KEY_UNIT = "KEY_UNIT";
	public static final String KEY_UNIT_CALORIES = "KEY_UNIT_CALORIES";
	public static final String KEY_TIME_DISPLAY = "KEY_TIME_DISPLAY";
	
	public static final String KEY_TAB = "KEY_TAB";
	public static final String KEY_LAST_SYNC_DEVICE_ADDRESS = "KEY_LAST_SYNC_DEVICE_ADDRESS";
	public static final String KEY_DEFALUT_DEVICE = "KEY_DEFALUT_DEVICE";
	
	public static final String KEY_BPM = "KEY_BPM";
	public static final String KEY_BPM_LANGUAGE = "KEY_BPM_LANGUAGE";
	
	// ///////////////////////////////////////////////
	public static final int ALARM_ON = 0;
	public static final int ALARM_OFF = 1;
	
	public static final int TYPE_REMINDER_TIME_BEGIN = 1;
	public static final int TYPE_REMINDER_TIME_END = 2;

	public static final int TYPE_SELECT_LANGUAGE = 1;
	public static final int TYPE_SELECT_UNIT = 2;
	public static final int TYPE_SELECT_TIME_DISPLAY = 3;
	public static final int TYPE_SELECT_UNIT_CALORIES = 4;

	public static final int TYPE_CALORIES = 1;
	public static final int TYPE_STEP = 2;
	public static final int TYPE_SLEEP = 3;
	
	public static final int TYPE_SCALE_VIEW_MAIN = 1;
	public static final int TYPE_SCALE_VIEW_CHART = 2;
	
	public static final int TYPE_COMMUNICATE = 0;
	public static final int TYPE_CLEAR = 1;
	
	public static final int TYPE_DATA_COMMUNICATE = 0;
	public static final int TYPE_DATA_CLEAR = 1;
	
	public static final int TYPE_CAMERA_START = 0;
	public static final int TYPE_CAMERA_END = 1;
	
	public static final int TYPE_LOST_MODE_START = 0;
	public static final int TYPE_LOST_MODE_END = 1;
	
	public static final int TYPE_GENDER_MALE = 1;
	public static final int TYPE_GENDER_FEMALE = 0;
	public static final int TYPE_GENDER_NULL = 2;
	
	public static final int TYPE_SLEEP_AWAKE = 0;
	public static final int TYPE_SLEEP_LIGHT = 1;
	public static final int TYPE_SLEEP_DEEP = 2;
	
	public static final int TYPE_NULL = 100;
	public static final int TYPE_UNIT_METRIC = 0;
	public static final int TYPE_UNIT_IMPERIAL = 1;
	
	public static final int TYPE_CALORIES_KCAL = 0;
	public static final int TYPE_CALORIES_CAL = 1;
	
	public static final int TYPE_TIME_DISPLAY_12 = 1;
	public static final int TYPE_TIME_DISPLAY_24 = 2;

	public static final int TYPE_BLE_1_HOUR = 1;
	public static final int TYPE_BLE_ALL = 2;
	public static final int TYPE_BLE_SCREEN = 3;
	public static final int TYPE_BLE_UNIT = 4;
	public static final int TYPE_BLE_SET_NOTIFY = 5;
	public static final int TYPE_BLE_CONNECTING = 6;
	public static final int TYPE_BLE_NULL = 10;
	
	public static final int TYPE_BPM_START = 1;
	public static final int TYPE_BPM_LANGUAGE = 2;
	
//	public static final int TYPE_JACK_SCREEN = 1;
	public static final int TYPE_JACK_DATA = 2;
//	public static final int TYPE_JACK_UNIT = 4;
	public static final int TYPE_JACK_NULL = 3;
	
	public static final int TYPE_BPM_TYPE_LOW = 0;
	public static final int TYPE_BPM_TYPE_OPTI = 1;
	public static final int TYPE_BPM_TYPE_NORM = 2;
	public static final int TYPE_BPM_TYPE_MILD = 3;
	public static final int TYPE_BPM_TYPE_MIDD = 4;
	public static final int TYPE_BPM_TYPE_HIGH = 5;
	
	public static final int TYPE_BPM_LANGUAGE_GERMAN = 1;
	public static final int TYPE_BPM_LANGUAGE_FRENCH = 2;
	public static final int TYPE_BPM_LANGUAGE_ENGLISH = 3;
	public static final int TYPE_BPM_LANGUAGE_SPAIN = 4;
	
	public static final int TYPE_DEVICE_SCALE = 1;
	public static final int TYPE_DEVICE_WRISTBAND = 2;
	public static final int TYPE_DEVICE_BPM = 3;
	public static final int TYPE_DEVICE_NULL = 0;
	
	public static final int TYPE_SHARE_TO_FACEBOOK = 0;
	public static final int TYPE_SHARE_TO_TWITTER = 1;
	public static final String PACKAGE_FACEBOOK = "com.facebook";
	public static final String PACKAGE_NAME_FACEBOOK = "PACKAGE_NAME_FACEBOOK";
	///////////////////////////////////////////////////
	public static final String TWITTER_CONSUMER_KEY = "RMZU00JQx0BXGjsvPXjT1RruO"; // place your cosumer key here
	public static final String TWITTER_CONSUMER_SECRET = "03ndzpj7OAi44yS9pdUI6d4iqaHewhWGQsjBR09SW0M1DUIw12"; // place your consumer secret here
	
	public static final String PREFERENCE_NAME = "twitter_oauth";
	public static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
	public static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
	public static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLogedIn";

	public static final String TWITTER_CALLBACK_URL = "oauth://t4jsample";
	public static final String URL_TWITTER_AUTH = "auth_url";
    public static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
    public static final String URL_TWITTER_OAUTH_TOKEN = "oauth_token";
    
	// ///////////////////////////////////////////////
	
	
	public static enum ProgressType{Daily, Weekly, Monthly, Yearly};
	
	// ////////////////////////////////////////////////
	/**
	 * 两位整数
	 */
	public static final DecimalFormat df_int_1 = new DecimalFormat("0");
	/**
	 * 两位整数
	 */
	public static final DecimalFormat df_int_2 = new DecimalFormat("00");
	/**
	 * 保留一位小数
	 */
	public static final DecimalFormat df_double_1 = new DecimalFormat("0.0");
	/**
	 * 保留两位小数
	 */
	public static final DecimalFormat df_double_2 = new DecimalFormat("0.00");
	
	/**
	 * 保留一位小数
	 */
	public static final DecimalFormat df_int_2_double_1 = new DecimalFormat("00.0");

	// ////////////////////////////////////////////////

	/**
	 * yyyy-MM-dd HH:mm
	 */
	@SuppressLint("SimpleDateFormat")
	public static final SimpleDateFormat sdf_1 = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm");

	/**
	 * yyy-MM-dd
	 */
	@SuppressLint("SimpleDateFormat")
	public static final SimpleDateFormat sdf_2 = new SimpleDateFormat(
			"yyyy-MM-dd");
	
	/**
	 * dd-MM
	 */
	@SuppressLint("SimpleDateFormat")
	public static final SimpleDateFormat sdf_2_1 = new SimpleDateFormat(
			"dd-MM");

	/**
	 * yyy-MM-dd HH
	 */
	@SuppressLint("SimpleDateFormat")
	public static final SimpleDateFormat sdf_3 = new SimpleDateFormat(
			"yyyy-MM-dd HH");
	/**
	 * yyy-MM-dd HH:mm:ss
	 */
	@SuppressLint("SimpleDateFormat")
	public static final SimpleDateFormat sdf_4 = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	/**
	 * dd-MM HH:mm:ss
	 */
	@SuppressLint("SimpleDateFormat")
	public static final SimpleDateFormat sdf_4_1 = new SimpleDateFormat(
			"dd-MM HH:mm:ss");
	
	public static final SimpleDateFormat sdf_5 = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.GERMAN);
	// ////////////////////////////////////////////////
	public static String DEVICE_NAME_WRISTBAND = "wb013";
	public static String DEVICE_NAME_SCALE = "ElecScalesBH";
	public static String DEVICE_NAME_BPMONITOR = "Technaxx BP";
//	public static String DEVICE_NAME_BPMONITOR = "RG-BLE-12";
	
	// ////////////////////////////////////////////////
	public static UUID UUID_SERVICE = UUID.fromString("0000fff0-0000-1000-8000-00805f9b34fb");
	public static UUID UUID_CHARACTERISTIC_BOUND_DEVICE = UUID.fromString("0000fff0-0000-1000-8000-00805f9b34fb");
	public static UUID UUID_CHARACTERISTIC_COMMUNICATION = UUID.fromString("0000fff1-0000-1000-8000-00805f9b34fb");
	public static UUID UUID_CHARACTERISTIC_COMMUNICATION_SCALE = UUID.fromString("0000fff4-0000-1000-8000-00805f9b34fb");
	public static UUID UUID_CHARACTERISTIC_LOST_MODE = UUID.fromString("0000fff3-0000-1000-8000-00805f9b34fb");
	public static final UUID UUID_DESCRIPTOR_CONFIGURATION = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
	
	public static UUID UUID_SERVICE_BPM = UUID.fromString("000018f0-0000-1000-8000-00805f9b34fb");
	public static UUID UUID_CHARACTERISTIC_WRITE_BPM = UUID.fromString("00002AF1-0000-1000-8000-00805f9b34fb");
	public static UUID UUID_CHARACTERISTIC_NOTIFY_BPM = UUID.fromString("00002AF0-0000-1000-8000-00805f9b34fb");
	
	// /////////////////////////////////////////////////
	public static final String[] ARRAY_DATETIME = new String[] { "00:00",
			"01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00",
			"08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00",
			"15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00",
			"22:00", "23:00", "24:00" };
	
	public static final String[] ARRAY_HOUR_23 = new String[] { "00", "01", "02", "03",
			"04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14",
			"15", "16", "17", "18", "19", "20", "21", "22", "23" };
	public static final String[] ARRAY_HOUR_24 = new String[] { "01", "02", "03",
		"04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14",
		"15", "16", "17", "18", "19", "20", "21", "22", "23", "24" };
	public static final String[] ARRAY_MINUTE = new String[] { "00", "01", "02",
			"03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13",
			"14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24",
			"25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35",
			"36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46",
			"47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57",
			"58", "59" };
	public static final String[] ARRAY_HOUR_12 = new String[] { "00", "01", "02", "03",
		"04", "05", "06", "07", "08", "09", "10", "11", "12" };
	public static final String[] ARRAY_HOUR_11 = new String[] { "00", "01", "02", "03",
		"04", "05", "06", "07", "08", "09", "10", "11", "12", "01", "02", "03",
		"04", "05", "06", "07", "08", "09", "10", "11"};
	public static final String[] ARRAY_AM_PM = new String[] {"AM", "PM"};
	public static final String[] ARRAY_MINUTE_ONE = new String[] { "00" };

	public static final String[] ARRAY_LANGUAGE = new String[] {"English"};

	
	/////////////////////////////////////////
	
}
