package com.lgb.myfitness.been;

import java.util.Calendar;

public class Scale {

	/**
	 * 体重
	 */
	private double weight;
	
	/**
	 * BMI
	 */
	private double bmi;
	
	private Calendar date;
	
	public Calendar getDate() {
		return date;
	}
	public void setDate(Calendar date) {
		this.date = date;
	}
	
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	
	public double getBMI() {
		return bmi;
	}
	public void setBMI(double bmi) {
		this.bmi = bmi;
	}
	
}
