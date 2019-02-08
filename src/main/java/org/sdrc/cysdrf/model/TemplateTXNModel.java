package org.sdrc.cysdrf.model;

public class TemplateTXNModel {

	private String userName;
	private Integer userId;
	private String frameworkType;
	private String areaName;
	private String timePeriod;
	private String targetStatus;
	private String achieveStatus;
	private String targetFileName;
	private String achieveFileName;
	private Integer txnTemplateId;
	private String areaCode;
	private Integer timePeriodId;
	private Integer frameworkId;
	private String disabled;
	private String isTargetAvailable;
	private String isAchieveAvailable;
	private String targetLastStatusDate;
	private String achieveLastStatusDate;
	private String isEnableChooseFile;
	
	public String getUserName() {
		return userName;
	}
	public Integer getUserId() {
		return userId;
	}
	public String getFrameworkType() {
		return frameworkType;
	}
	public String getAreaName() {
		return areaName;
	}
	public String getTimePeriod() {
		return timePeriod;
	}
	public String getTargetFileName() {
		return targetFileName;
	}
	public String getAchieveFileName() {
		return achieveFileName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public void setFrameworkType(String frameworkType) {
		this.frameworkType = frameworkType;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public void setTimePeriod(String timePeriod) {
		this.timePeriod = timePeriod;
	}
	public void setTargetFileName(String targetFileName) {
		this.targetFileName = targetFileName;
	}
	public void setAchieveFileName(String achieveFileName) {
		this.achieveFileName = achieveFileName;
	}
	public Integer getTxnTemplateId() {
		return txnTemplateId;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setTxnTemplateId(Integer txnTemplateId) {
		this.txnTemplateId = txnTemplateId;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public Integer getTimePeriodId() {
		return timePeriodId;
	}
	public void setTimePeriodId(Integer timePeriodId) {
		this.timePeriodId = timePeriodId;
	}
	public Integer getFrameworkId() {
		return frameworkId;
	}
	public void setFrameworkId(Integer frameworkId) {
		this.frameworkId = frameworkId;
	}
	public String getDisabled() {
		return disabled;
	}
	public void setDisabled(String disabled) {
		this.disabled = disabled;
	}
	public String getTargetStatus() {
		return targetStatus;
	}
	public String getAchieveStatus() {
		return achieveStatus;
	}
	public String getIsTargetAvailable() {
		return isTargetAvailable;
	}
	public String getIsAchieveAvailable() {
		return isAchieveAvailable;
	}
	public void setTargetStatus(String targetStatus) {
		this.targetStatus = targetStatus;
	}
	public void setAchieveStatus(String achieveStatus) {
		this.achieveStatus = achieveStatus;
	}
	public void setIsTargetAvailable(String isTargetAvailable) {
		this.isTargetAvailable = isTargetAvailable;
	}
	public void setIsAchieveAvailable(String isAchieveAvailable) {
		this.isAchieveAvailable = isAchieveAvailable;
	}
	public String getTargetLastStatusDate() {
		return targetLastStatusDate;
	}
	public String getAchieveLastStatusDate() {
		return achieveLastStatusDate;
	}
	public void setTargetLastStatusDate(String targetLastStatusDate) {
		this.targetLastStatusDate = targetLastStatusDate;
	}
	public void setAchieveLastStatusDate(String achieveLastStatusDate) {
		this.achieveLastStatusDate = achieveLastStatusDate;
	}
	public String getIsEnableChooseFile() {
		return isEnableChooseFile;
	}
	public void setIsEnableChooseFile(String isEnableChooseFile) {
		this.isEnableChooseFile = isEnableChooseFile;
	}
}
