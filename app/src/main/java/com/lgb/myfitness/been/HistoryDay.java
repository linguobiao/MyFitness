package com.lgb.myfitness.been;

import java.util.Calendar;

public class HistoryDay {
	/**
	 * 日期
	 * yyyy.mm.dd
	 */
	private Calendar date;
	/**
	 * 步数
	 */
	private int step;
	/**
	 * 卡路里
	 * 大卡
	 */
	private double burn;
	/**
	 * 睡眠质量
	 * 百分比
	 * 
	 */
	private int sleepQuality;
	/**
	 * 睡眠动作次数
	 */
	private int sleepMove;
	/**
	 * 深睡小时
	 */
	private int deepSleepHour;
	/**
	 * 浅睡小时
	 */
	private int lightSleepHour;
	/**
	 * 运动时间
	 */
	private int activeHour;
	public Calendar getDate() {
		return date;
	}
	public void setDate(Calendar date) {
		this.date = date;
	}
	public int getStep() {
		return step;
	}
	public void setStep(int step) {
		this.step = step;
	}
	public double getBurn() {
		return burn;
	}
	public void setBurn(double burn) {
		this.burn = burn;
	}
	public int getSleepQuality() {
		return sleepQuality;
	}
	public void setSleepQuality(int sleepQuality) {
		this.sleepQuality = sleepQuality;
	}
	public int getSleepMove() {
		return sleepMove;
	}
	public void setSleepMove(int sleepMove) {
		this.sleepMove = sleepMove;
	}
	public int getDeepSleepHour() {
		return deepSleepHour;
	}
	public void setDeepSleepHour(int deepSleepHour) {
		this.deepSleepHour = deepSleepHour;
	}
	public int getLightSleepHour() {
		return lightSleepHour;
	}
	public void setLightSleepHour(int lightSleepHour) {
		this.lightSleepHour = lightSleepHour;
	}
	public int getActiveHour() {
		return activeHour;
	}
	public void setActiveHour(int activeHour) {
		this.activeHour = activeHour;
	}
}
