package com.lgb.myfitness.been;

import java.util.Calendar;

public class HistoryMonth {
	/**
	 * 月份日期，每月一号
	 */
	private Calendar date;
	/**
	 * 步数总数
	 */
	private int step;
	/**
	 * 卡路里总数
	 */
	private double burn;
	/**
	 * 有数据的天数
	 */
	private int dayAmount;
	/**
	 * 总睡眠质量
	 */
	private int totoalSleepQuality;
	private int totalDeepSleep;
	private int totalLightSleep;
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
	public int getDayAmount() {
		return dayAmount;
	}
	public void setDayAmount(int dayAmount) {
		this.dayAmount = dayAmount;
	}
	public int getTotoalSleepQuality() {
		return totoalSleepQuality;
	}
	public void setTotoalSleepQuality(int totoalSleepQuality) {
		this.totoalSleepQuality = totoalSleepQuality;
	}
	public int getTotalDeepSleep() {
		return totalDeepSleep;
	}
	public void setTotalDeepSleep(int totalDeepSleep) {
		this.totalDeepSleep = totalDeepSleep;
	}
	public int getTotalLightSleep() {
		return totalLightSleep;
	}
	public void setTotalLightSleep(int totalLightSleep) {
		this.totalLightSleep = totalLightSleep;
	}
	
}
