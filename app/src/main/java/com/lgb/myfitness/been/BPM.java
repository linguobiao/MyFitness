package com.lgb.myfitness.been;

import java.io.Serializable;
import java.util.Calendar;

public class BPM implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Calendar datetime;
	private int systolic;
	private int diatolic;
	private int heartRate;
	public int getSystolic() {
		return systolic;
	}
	public void setSystolic(int systolic) {
		this.systolic = systolic;
	}
	public int getDiatolic() {
		return diatolic;
	}
	public void setDiatolic(int diatolic) {
		this.diatolic = diatolic;
	}
	public int getHeartRate() {
		return heartRate;
	}
	public void setHeartRate(int heartRate) {
		this.heartRate = heartRate;
	}
	public Calendar getDatetime() {
		return datetime;
	}
	public void setDatetime(Calendar datetime) {
		this.datetime = datetime;
	}
	
	
}
