package com.lgb.xpro.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.List;

/**
 * 跟App相关的辅助类
 * 
 */
public class AppUtils {

	private AppUtils() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");

	}

	/**
	 * 获取应用程序名称
	 */
	public static String getAppName(Context context) {
		try{
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			int labelRes = packageInfo.applicationInfo.labelRes;
			return context.getResources().getString(labelRes);
		} catch (NameNotFoundException e){
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取软件版本号
	 */
	private static int CURR_VERSION = -1;
	public static int getVerCode(Context context) {
		if(CURR_VERSION > 0) return CURR_VERSION;

		int verCode = -1;
		try {
			verCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
			CURR_VERSION = verCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return verCode;
	}

	/**
	 * [获取应用程序版本名称信息]
	 * 
	 * @param context
	 * @return 当前应用的版本名称
	 */
	public static String getVersionName(Context context) {
		try
		{
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			return packageInfo.versionName;

		} catch (NameNotFoundException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取设备号
	 * @param context
	 * @return
	 */
	@Deprecated
	private static String ImeiNo = null;
	@Deprecated
	public static String getImieNo(Context context){
		if(!TextUtils.isEmpty(ImeiNo) && ImeiNo.length() > 0) return ImeiNo;
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String imie = tm.getDeviceId();
		return imie;
	}

	/**
	 * application应用<meta-data>元素 获取
	 * @param context
	 * @param key
	 * @return
	 */
	public static String getMetaDataByKey(Context context, String key){
		try{
			ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
			return appInfo.metaData.getString(key);
		}catch (NameNotFoundException e){
			e.printStackTrace();
		}
		return null;
	}

	public static String getWxAppId(Context context, String runEnvironment){
		//签名必须和证书匹配起来。
		String key = "";
		String packageName = context.getPackageName();
		if(TextUtils.isEmpty(packageName)){
			key = "WX_APPID_RELEASE";
		}else{
			if("com.crm.wdsoft1".equals(packageName.trim())){
				key = "WX_APPID_UNIT";
			}else{
				key = "WX_APPID_RELEASE";
			}
		}
		if("local".equals(runEnvironment)){// applicationId = com.crm.wdsoft
			key = "WX_APPID_DEBUG";
		}
		if(TextUtils.isEmpty(key)){
			key = "WX_APPID_RELEASE";
		}
		String wxAppId = getMetaDataByKey(context,key);
		//Logger.e("wxAppId==============" + wxAppId + "======key=" + key+"=====packageName==="+packageName);
		return wxAppId;
	}

	/**
	 * 通过包名获取相应的MD5校验码
	 * @param packagePath
	 * @return
     */
	public static String getApkMd5ByPath(String packagePath) {
		String sha = "";
		try {
			MessageDigest msgDigest = MessageDigest.getInstance("SHA-1");
			byte[] bytes = new byte[1024];
			int byteCount;
			FileInputStream fis = new FileInputStream(new File(packagePath));
			while ((byteCount = fis.read(bytes)) > 0) {
				msgDigest.update(bytes, 0, byteCount);
			}
			BigInteger bi = new BigInteger(1, msgDigest.digest());
			sha = bi.toString(16);
			fis.close();
			// 这里添加从服务器中获取哈希值然后进行对比校验
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sha;
	}

	/**
	 * 判断APP是否在后台运行
	 * @param context
	 * @return
	 */
	public static boolean isRunInBackground(Context context) {
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		if (appProcesses != null) {
			for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
				if (appProcess.processName.equals(context.getPackageName())) {
                    return appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
				}
			}
		}
		return false;
	}

	/**
	 * 得到APP签名信息
	 * @param context
	 * @return
     */
	public static String getAppSignInfo(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager().getPackageInfo(
					context.getPackageName(), PackageManager.GET_SIGNATURES);
			Signature[] signs = packageInfo.signatures;
			Signature sign = signs[0];
			return sign.toCharsString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 判断当前是否处于锁屏状态
	 * @param context
	 * @return
	 */
	public static boolean isApplicationLocked(Context context) {
		KeyguardManager keyguardManager = (KeyguardManager) context .getSystemService(Context.KEYGUARD_SERVICE);
		boolean isLockedState = keyguardManager.inKeyguardRestrictedInputMode();// 用于判断当前是否处于锁屏状态。
		return isLockedState;
	}

	/**
	 * 判断是否显示在前端
	 * @param context
	 * @return
	 */
	public static boolean isAppOnForeground(Context context) {
		return true;
		/*if(isApplicationLocked(context)) return true;
		boolean ishomBack = SpManager.getInstance().readBoolean("lockpattern","ishomback",false);
		if(ishomBack) return true;
		ActivityManager mActivityManager = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE));
		ProcessHelper processHelper = new ProcessHelper(mActivityManager);
		String proccessName = processHelper.getForegroundApp();
		if(TextUtils.isEmpty(proccessName)) return true;
		//Logger.e("========proccessName===="+ proccessName);
		return proccessName.startsWith(context.getPackageName());*/
	}

	private static int count = 0;
	public static void initActivityLifecycleCallbacks(Application application, final OnAppStateListener onAppStateListener) {
		count = 0;
		application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
			@Override
            public void onActivityStopped(Activity activity) {
				count--;
				if (count == 0) {onAppStateListener.onBackgroundState();}
			}

			@Override
            public void onActivityStarted(Activity activity) {
				if (count == 0) {onAppStateListener.onForegroundState();}
				count++;
			}

			@Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {}

			@Override
            public void onActivityResumed(Activity activity) {}

			@Override
            public void onActivityPaused(Activity activity) {}

			@Override
            public void onActivityDestroyed(Activity activity) {}

			@Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {}
		});
	}

	public interface OnAppStateListener {
		void onBackgroundState();
		void onForegroundState();
	}
}
