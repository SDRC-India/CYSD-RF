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
@Table
public class TXNTemplateDetail implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="TXNTemplateId")
	private int tXNTemplateId;
	
	@ManyToOne
	@JoinColumn(name="TemplateId")
	private TemplateDetail templateDetail;
	
	@Column(name="CreatedDate")
	private Timestamp createdDate;
	
	@Column(name="CreatedBy")
	private String createdBy;
	
	@Column(name="Status")
	private String status;
	
	@Column(name="IsAlive")
	private boolean isAlive;
	
	@ManyToOne
	@JoinColumn(name="UserId")
	private UserDetail userDetail;	
	
	@Column(name="UpdatedDate")
	private Timestamp updatedDate;
	
	@Column(name="UpdatedBy")
	private String updatedBy;
	
	@Column(name="FilePath")
	private String filePath;
	
	@Column(name="UploadType")
	private String uploadType;
	
	//added on 05-01-2019 by Sarita
	@Column(name="areaCode")
	private String areaCode;
	
	public int gettXNTemplateId() {
		return tXNTemplateId;
	}

	public TemplateDetail getTemplateDetail() {
		return templateDetail;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public Timestamp getUpdatedDate() {
		return updatedDate;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public String getFilePath() {
		return filePath;
	}

	public void settXNTemplateId(int tXNTemplateId) {
		this.tXNTemplateId = tXNTemplateId;
	}

	public void setTemplateDetail(TemplateDetail templateDetail) {
		this.templateDetail = templateDetail;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getUploadType() {
		return uploadType;
	}

	public void setUploadType(String uploadType) {
		this.uploadType = uploadType;
	}

	public UserDetail getUserDetail() {
		return userDetail;
	}

	public void setUserDetail(UserDetail userDetail) {
		this.userDetail = userDetail;
	}

	public String getStatus() {
		return status;
	}

	public boolean isAlive() {
		return isAlive;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

}
