package org.sdrc.cysdrf.model;

public class ReportTypeDetailsModel {

	private Integer reportTypeId;

	private String reportTypeName;

	public Integer getReportTypeId() {
		return reportTypeId;
	}

	public void setReportTypeId(Integer reportTypeId) {
		this.reportTypeId = reportTypeId;
	}

	public String getReportTypeName() {
		return reportTypeName;
	}

	public void setReportTypeName(String reportTypeName) {
		this.reportTypeName = reportTypeName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	private String description;

	private String fileType;
}
