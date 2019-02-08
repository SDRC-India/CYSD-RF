package org.sdrc.cysdrf.model;


public class ReportDetailsModel {

	private Integer reportId;

	private String reportName;

	private String description;

	private Integer parentReportId;

	private String type;

	private String fileType;

	private boolean isAlive;

	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	private String uploadedBy;

	private String uploadedDate;

	private String updatedBy;

	private String updatedDate;

	private String tags;
	
	private boolean isConfidential;

	public boolean isConfidential() {
		return isConfidential;
	}

	public void setConfidential(boolean isConfidential) {
		this.isConfidential = isConfidential;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	private Integer sectionId;

	private Integer themeId;

	public Integer getThemeId() {
		return themeId;
	}

	public void setThemeId(Integer themeId) {
		this.themeId = themeId;
	}

	private Integer userId;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getReportId() {
		return reportId;
	}

	public void setReportId(Integer reportId) {
		this.reportId = reportId;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getParentReportId() {
		return parentReportId;
	}

	public void setParentReportId(Integer parentReportId) {
		this.parentReportId = parentReportId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public boolean isAlive() {
		return isAlive;
	}

	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}

	public String getUploadedBy() {
		return uploadedBy;
	}

	public void setUploadedBy(String uploadedBy) {
		this.uploadedBy = uploadedBy;
	}

	public String getUploadedDate() {
		return uploadedDate;
	}

	public void setUploadedDate(String uploadedDate) {
		this.uploadedDate = uploadedDate;
	}

	public Integer getSectionId() {
		return sectionId;
	}

	public void setSectionId(Integer sectionId) {
		this.sectionId = sectionId;
	}

}
