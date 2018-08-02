package com.lgb.myfitness.helper;

import com.lgb.myfitness.view.GoalProgressbar;

public class ProgressHelper {
	/**
	 * 设置进度
	 * @param value
	 * @param goalValue
	 * @param progress_goal
	 */
	public static void setProgress(double value, double goalValue, GoalProgressbar progress_goal) {
		int goalPercent = 0;
//		System.out.println("goal step: " + goalStep);
		if (goalValue != 0) {
			goalPercent = (int) ((double)value / (double)goalValue * 100);
			
			if (goalPercent > 100) {
				goalPercent = 100;
			}
		}
		
		progress_goal.setProgress(goalPercent);
	}
	
	
	public static void setProgress(double goalPercent, GoalProgressbar progress_goal) {
		if (goalPercent > 100) {
			goalPercent = 100;
		}
		
		progress_goal.setProgress((int)goalPercent);
	}
}
