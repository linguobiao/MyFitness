package com.lgb.myfitness.been;

import java.util.Calendar;

public class HistoryHour {
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
	 * 睡眠动作次数
	 */
	private int sleepMove;
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
	public int getSleepMove() {
		return sleepMove;
	}
	public void setSleepMove(int sleepMove) {
		this.sleepMove = sleepMove;
	}
}
