package com.lgb.myfitness.helper;

import com.lgb.myfitness.global.Global;

public class FormatHelper {

	/**
	 * goal format
	 * 去尾，整数
	 * @param goalPercent
	 * @return
	 */
	public static String goalFormat(double goalPercent) {
		String goalStr = String.valueOf(goalPercent);
		goalStr = goalStr.replaceAll(",", ".");
		
		int index = goalStr.indexOf(".");
		if (index != -1) {
			int end = index + 3;
			int length = goalStr.length();
			
			if (end <= length) {
				return goalStr.substring(0, index);
			} 
		}
		
		return Global.df_int_1.format(goalPercent);
	}
	
	
	/**
	 * distance format
	 * 去尾，保留两位小数
	 * @param distance
	 * @return
	 */
	public static String distanceFormat(double distance) {
		String distanceStr = String.valueOf(distance);
		distanceStr = distanceStr.replaceAll(",", ".");
		
		int indexDistance = distanceStr.indexOf(".");
		if (indexDistance != -1 ){
			int end = indexDistance + 3;
			int length = distanceStr.length();
			
			if (end <= length) {
				return distanceStr.substring(0, indexDistance + 3);
			}
		}
		
		return Global.df_double_2.format(distance);
	}
}
