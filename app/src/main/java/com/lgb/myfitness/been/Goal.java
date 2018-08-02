package com.lgb.myfitness.been;

public class Goal {
	/**
	 * 
	 */
	private int step;
	/**
	 * 单位kcal
	 */
	private double burn;
	/**
	 * 单位hour
	 */
	private double sleep;
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
	public double getSleep() {
		return sleep;
	}
	public void setSleep(double sleep) {
		this.sleep = sleep;
	}
}
