package org.sdrc.cysdrf.model;

public class IsActiveModel {
	
	private boolean isSaveActive;
	private boolean isDownloadActive;
	private boolean isTargetUploadActive;
	private boolean isAchieveUploadActive;
	private boolean isTargetActive;
	private boolean isAchieveActive;
	private boolean isProjectActive;
	private boolean isAchieveDownload;
	
	public boolean isSaveActive() {
		return isSaveActive;
	}
	public boolean isDownloadActive() {
		return isDownloadActive;
	}

	public boolean isTargetActive() {
		return isTargetActive;
	}
	public boolean isAchieveActive() {
		return isAchieveActive;
	}
	public boolean isProjectActive() {
		return isProjectActive;
	}
	public void setSaveActive(boolean isSaveActive) {
		this.isSaveActive = isSaveActive;
	}
	public void setDownloadActive(boolean isDownloadActive) {
		this.isDownloadActive = isDownloadActive;
	}
	public void setTargetActive(boolean isTargetActive) {
		this.isTargetActive = isTargetActive;
	}
	public void setAchieveActive(boolean isAchieveActive) {
		this.isAchieveActive = isAchieveActive;
	}
	public void setProjectActive(boolean isProjectActive) {
		this.isProjectActive = isProjectActive;
	}
	public boolean isTargetUploadActive() {
		return isTargetUploadActive;
	}
	public boolean isAchieveUploadActive() {
		return isAchieveUploadActive;
	}
	public void setTargetUploadActive(boolean isTargetUploadActive) {
		this.isTargetUploadActive = isTargetUploadActive;
	}
	public void setAchieveUploadActive(boolean isAchieveUploadActive) {
		this.isAchieveUploadActive = isAchieveUploadActive;
	}
	public boolean isAchieveDownload() {
		return isAchieveDownload;
	}
	public void setAchieveDownload(boolean isAchieveDownload) {
		this.isAchieveDownload = isAchieveDownload;
	}
}
