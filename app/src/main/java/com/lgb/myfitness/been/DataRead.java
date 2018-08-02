package com.lgb.myfitness.been;

public class DataRead {
	/**
	 * 今天数据
	 */
	private HistoryDay todayHistory;
	/**
	 * 昨天数据
	 */
	private HistoryDay yesterdayHistory;
//	/**
//	 * 前天数据
//	 */
//	private HistoryDate beforeYesterdayHistory;
	
	/**
	 * 电池电量
	 */
	private int battaryLevel;

	public HistoryDay getTodayHistory() {
		return todayHistory;
	}

	public void setTodayHistory(HistoryDay todayHistory) {
		this.todayHistory = todayHistory;
	}

	public HistoryDay getYesterdayHistory() {
		return yesterdayHistory;
	}

	public void setYesterdayHistory(HistoryDay yesterdayHistory) {
		this.yesterdayHistory = yesterdayHistory;
	}

//	public HistoryDate getBeforeYesterdayHistory() {
//		return beforeYesterdayHistory;
//	}
//
//	public void setBeforeYesterdayHistory(HistoryDate beforeYesterdayHistory) {
//		this.beforeYesterdayHistory = beforeYesterdayHistory;
//	}

	public int getBattaryLevel() {
		return battaryLevel;
	}

	public void setBattaryLevel(int battaryLevel) {
		this.battaryLevel = battaryLevel;
	}

}
