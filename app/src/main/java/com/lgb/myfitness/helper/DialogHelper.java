package com.lgb.myfitness.helper;


import com.lgb.myfitness.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;

public class DialogHelper {
	
	
	public static ProgressDialog showProgressDialog(Context context, String message) {
		ProgressDialog mpDialog = new ProgressDialog(context);  
        mpDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);//设置风格为圆形进度条  
        mpDialog.setMessage(message);  
        
        return mpDialog;
	}
	
	/**
	 * Function to display simple Alert Dialog
	 * @param context - application context
	 * @param title - alert dialog title
	 * @param message - alert message
	 * @param status - success/failure (used to set icon)
	 * 				 - pass null if you don't want icon
	 * */
	public static void showAlertDialog(Context context, String title, String message,
			Boolean status) {
		Builder alertDialog = new AlertDialog.Builder(context);
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		alertDialog.setPositiveButton(R.string.ok ,null);
		alertDialog.show();
		
	}
	
	public static void cancelDialog(Dialog dialog) {
		if (dialog != null) {
			dialog.dismiss();
		}
	}
	
	
	public static void hideDialog(Dialog dialog) {
		if (dialog != null) {
			dialog.hide();
		}
	}
}
