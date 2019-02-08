package org.sdrc.cysdrf.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table
public class TemplateDetail implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="TemplateId")
	private int templateId;
	
	@Column(name="TimePeriodId")
	private int timePeriodId;
	
	@Column(name="TimePeriod")
	private String timePeriod;
	
	@ManyToOne
	@JoinColumn(name="FrameworkTypeId")
	private FrameworkType frameworkType;
	
	@Column(name="Projects")
	private String projects;
	
	@Column(name="FilePath")
	private String filePath;
	
	@Column(name="CreatedDate")
	private Timestamp createdDate;
	
	@Column(name="CreatedBy")
	private String createdBy;
	
	@ManyToOne
	@JoinColumn(name="UserId")
	private UserDetail userDetail;
	
	@Column(name="IsActive")
	private boolean isActive;
	
	@Column(name="UpdatedBy")
	private String updatedBy;
	
	@Column(name="UpdatedDate")
	private Timestamp updatedDate;
	
	@OneToMany(mappedBy="templateDetail")
	private List<TXNTemplateDetail> txnTemplateDetails;

	
	//added on 05-01-2019 by Sarita
	@Column(name="areaCode")
	private String areaCode;
	
	
	public int getTemplateId() {
		return templateId;
	}

	public int getTimePeriodId() {
		return timePeriodId;
	}

	public String getTimePeriod() {
		return timePeriod;
	}

	public FrameworkType getFrameworkType() {
		return frameworkType;
	}

	public String getProjects() {
		return projects;
	}

	public String getFilePath() {
		return filePath;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public boolean isActive() {
		return isActive;
	}

	public List<TXNTemplateDetail> getTxnTemplateDetails() {
		return txnTemplateDetails;
	}

	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}

	public void setTimePeriodId(int timePeriodId) {
		this.timePeriodId = timePeriodId;
	}

	public void setTimePeriod(String timePeriod) {
		this.timePeriod = timePeriod;
	}

	public void setFrameworkType(FrameworkType frameworkType) {
		this.frameworkType = frameworkType;
	}

	public void setProjects(String projects) {
		this.projects = projects;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public void setTxnTemplateDetails(List<TXNTemplateDetail> txnTemplateDetails) {
		this.txnTemplateDetails = txnTemplateDetails;
	}

	public UserDetail getUserDetail() {
		return userDetail;
	}

	public void setUserDetail(UserDetail userDetail) {
		this.userDetail = userDetail;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public Timestamp getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

}
