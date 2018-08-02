package com.lgb.myfitness.been;

import java.util.Calendar;

public class ScreenData {
	private Calendar date;
	private int step;
	/**
	 * 距离，单位km
	 */
	private double distance;
	/**
	 * 卡路里， 单位kcal
	 */
	private int burn;
	/**
	 * 完成目标百分比，用步数计算
	 */
	private int goal;
	/**
	 * 英制还是公制
	 */
	private int uint;
	/**
	 * 电池电量百分比
	 */
	private int battaryLevel;
	public int getStep() {
		return step;
	}
	public void setStep(int step) {
		this.step = step;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	public int getBurn() {
		return burn;
	}
	public void setBurn(int burn) {
		this.burn = burn;
	}
	public int getGoal() {
		return goal;
	}
	public void setGoal(int goal) {
		this.goal = goal;
	}
	public int getBattaryLevel() {
		return battaryLevel;
	}
	public void setBattaryLevel(int battaryLevel) {
		this.battaryLevel = battaryLevel;
	}
	public Calendar getDate() {
		return date;
	}
	public void setDate(Calendar date) {
		this.date = date;
	}
	public int getUint() {
		return uint;
	}
	public void setUint(int uint) {
		this.uint = uint;
	}
	
}
