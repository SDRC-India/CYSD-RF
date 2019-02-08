package org.sdrc.cysdrf.model;

public class TimePeriodModel {

	private String timePeriod;
	private String startDate;
	private String endDate;
	private String periodicity;

	public String getTimePeriod() {
		return timePeriod;
	}

	public String getStartDate() {
		return startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public String getPeriodicity() {
		return periodicity;
	}

	public void setTimePeriod(String timePeriod) {
		this.timePeriod = timePeriod;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public void setPeriodicity(String periodicity) {
		this.periodicity = periodicity;
	}

}
