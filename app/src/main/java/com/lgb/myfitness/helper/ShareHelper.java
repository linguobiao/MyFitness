package com.lgb.myfitness.helper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.facebook.FacebookRequestError;
import com.facebook.model.GraphObject;
import com.lgb.myfitness.R;
import com.lgb.myfitness.global.Global;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

public class ShareHelper {
	
	private static String TAG = "ShareHelper";
	/**
	 * Checking for all possible internet providers
	 * **/
	public static boolean isConnectingToInternet(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}

		}
		return false;
	}
	
	/**
	 * Show publish result by a dialog, success or fail.
	 * **/
	public static void showPublishResult(Context context, GraphObject result, FacebookRequestError error) {
        String title = null;
        String message = null;
        if (error == null) {
            title = context.getString(R.string.success);
            String id = result.cast(GraphObjectWithId.class).getId();
            message = context.getString(R.string.successfully_posted_post, id);
        } else {
            title = context.getString(R.string.error);
            message = error.getErrorMessage();
        }
        DialogHelper.showAlertDialog(context, title, message, false);
    }
	
	private interface GraphObjectWithId extends GraphObject {
        String getId();
    }
	
	public static Map<String, String> setImage(Activity activity){
		Map<String, String> mapPackageName = new LinkedHashMap<String, String>();
		
		PackageManager pManager = activity.getPackageManager();
		ArrayList<ResolveInfo> resolveInfos = ShareHelper.getShareApp(activity);
		for (ResolveInfo resolveInfo : resolveInfos) {
			String packageName = resolveInfo.activityInfo.packageName;
			if (packageName.startsWith(Global.PACKAGE_FACEBOOK)){
				System.out.println("**************" + packageName);
//				image_facebook.setImageDrawable(resolveInfo.loadIcon(pManager));
				mapPackageName.put(Global.PACKAGE_NAME_FACEBOOK, packageName);
			}
		}
		
		return mapPackageName;
	}
	
	public static ArrayList<ResolveInfo> getShareApp(Context context){
		ArrayList<ResolveInfo> SMS_EMAIL_FACEBOOK = new ArrayList<ResolveInfo>();
		 
         Intent intent = new Intent(Intent.ACTION_SEND, null);
         intent.addCategory(Intent.CATEGORY_DEFAULT);
         intent.setType("image/png");
         PackageManager pManager = context.getPackageManager();
         List<ResolveInfo> mApps = pManager.queryIntentActivities(intent, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
	
         
         for (ResolveInfo resolveInfo : mApps) {
        	 String packageName = resolveInfo.activityInfo.packageName;
        	 Log.i(TAG, resolveInfo.activityInfo.packageName + ", " + resolveInfo.loadLabel(pManager).toString());
        	 if (packageName.startsWith(Global.PACKAGE_FACEBOOK)){
        		 
        		 SMS_EMAIL_FACEBOOK.add(resolveInfo);
        	 }	
		}
         
        return SMS_EMAIL_FACEBOOK;
	}
	public static void actionShare_facebook(String packageName, Activity activity, Bitmap bmp, String shareText ){

		Intent intent = new Intent(Intent.ACTION_SEND);   
		intent.setType("image/jpg"); 
//		intent.setType("text/plain"); // 纯文本 
		
		// 标题
		intent.putExtra(Intent.EXTRA_SUBJECT, "MyFitness Share");   
		// 文字
		intent.putExtra(Intent.EXTRA_TEXT, shareText);  
//		intent.putExtra(Intent.EXTRA_HTML_TEXT, shareText);
		// 图片
		intent.putExtra(Intent.EXTRA_STREAM, getImageUri(activity, bmp));
		
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
		intent.setPackage(packageName);
		
		activity.startActivity(intent);	
		System.out.println("****3");
	}
	
	public static Uri getImageUri(Context inContext, Bitmap bmp) {
		System.out.println("bmp = " + bmp);
		Uri uri = null;
//		String fileName = Calendar.getInstance().getTimeInMillis() + "";
//		File sd=Environment.getExternalStorageDirectory(); 
		File f = new File(Environment.getExternalStorageDirectory().getPath(), "My Fitness.jpg"); 
		if (f.exists()) { 
			f.delete(); 
		}
		System.out.println("kdkdkdk0000");
		try { 
			FileOutputStream out = new FileOutputStream(f); 
			bmp.compress(Bitmap.CompressFormat.JPEG, 90, out); 
			System.out.println("kdkdkdk00002");
			out.flush(); 
			out.close(); 
			Log.i(TAG, "已经保存"); 
			} catch (FileNotFoundException e) { 
				// TODO Auto-generated catch block 
				e.printStackTrace(); 
			} catch (IOException e) { 
				// TODO Auto-generated catch block 
				e.printStackTrace(); 
			} 
			
//		  ContentResolver cr = inContext.getContentResolver();
//			String url = Images.Media.insertImage(cr, bmp, fileName, "");
//			
//			Uri uri = Uri.parse(url);
		  uri = Uri.fromFile(f);
		
		return uri;
	}
}
