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
public class FrameworkType implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="FrameworkTypeId")
	private int frameworkTypeId;
	
	@Column(name="FrameworkName")
	private String frameworkName;
	
	@Column(name="CreatedDate")
	private Timestamp createdDate;
	
	@Column(name="CreatedBy")
	private String createdBy;
	
	@Column(name="IsActive")
	private boolean isActive;
	
	@ManyToOne
	@JoinColumn(name="CoordinatorId")
	private UserDetail userDetail;
	
	@OneToMany(mappedBy="frameworkType")
	private List<TemplateDetail> templateDetails;

	public int getFrameworkTypeId() {
		return frameworkTypeId;
	}

	public String getFrameworkName() {
		return frameworkName;
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

	public UserDetail getUserDetail() {
		return userDetail;
	}

	public List<TemplateDetail> getTemplateDetails() {
		return templateDetails;
	}

	public void setFrameworkTypeId(int frameworkTypeId) {
		this.frameworkTypeId = frameworkTypeId;
	}

	public void setFrameworkName(String frameworkName) {
		this.frameworkName = frameworkName;
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

	public void setUserDetail(UserDetail userDetail) {
		this.userDetail = userDetail;
	}

	public void setTemplateDetails(List<TemplateDetail> templateDetails) {
		this.templateDetails = templateDetails;
	}

	public FrameworkType(int frameworkTypeId) {
		super();
		this.frameworkTypeId = frameworkTypeId;
	}

	public FrameworkType() {
		super();
		// TODO Auto-generated constructor stub
	}

}
