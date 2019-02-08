package org.sdrc.cysdrf.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="ReportDetails")
public class ReportDetails implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="reportId")
	private Integer reportId;
	
	@Column(name="reportName")
	private String reportName;
	
	@Column(name="description",length=320)
	private String description;
	
	@Column(name="parentReportId")
	private Integer parentReportId;
	
	@Column(name="type")
	private String type;
	
	@Column(name="fileType")
	private String fileType;
	
	@Column(name="isAlive")
	private boolean isAlive;
	
	@Column(name="uploadedBy")
	private String uploadedBy;
	
	@Column(name="uploadedDate")
	private Timestamp uploadedDate;
	
	@Column(name="tags")
	private String tags;
	
	@ManyToOne
	@JoinColumn(name="UserId")
	private UserDetail userDetail;
	
	@Column(name="url", columnDefinition = "VARCHAR(MAX)")
	private String url;
	
	@Column(name="updatedBy")
	private String updatedBy;
	
	@Column(name="updatedDate")
	private Timestamp updatedDate;
	
	public boolean isConfidential() {
		return isConfidential;
	}

	public void setConfidential(boolean isConfidential) {
		this.isConfidential = isConfidential;
	}

	@Column(name="IsConfidential")
	private boolean isConfidential;
	
	@ManyToOne
	@JoinColumn(name="SectionId")
	private SectionDetails sectionDetails;

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

	public Timestamp getUploadedDate() {
		return uploadedDate;
	}

	public void setUploadedDate(Timestamp uploadedDate) {
		this.uploadedDate = uploadedDate;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public UserDetail getUserDetail() {
		return userDetail;
	}

	public void setUserDetail(UserDetail userDetail) {
		this.userDetail = userDetail;
	}

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

	public Timestamp getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}

	public SectionDetails getSectionDetails() {
		return sectionDetails;
	}

	public void setSectionDetails(SectionDetails sectionDetails) {
		this.sectionDetails = sectionDetails;
	}
}
