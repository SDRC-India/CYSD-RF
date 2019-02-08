package org.sdrc.cysdrf.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table
public class UserDetail implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="UserDetailId")
	private int userDetailId;
	
	@Column(name="UserName")
	private String userName;
	
	@Column(name="IsLive")
	private boolean isLive;
	
	@Column(name="UpdatedDate")
	private Timestamp updatedDate;
	
	@Column(name="UpdatedBy")
	private String updatedBy;
	
	@Column(name="ContactNo")
	private String contactNo;
	
	@Column(name="EmailId")
	private String emailId;
	
	@Column(name="CreatedBy")
	private String createdBy;
	
	@Column(name="CreatedDate")
	private Timestamp createdDate;
	
	public UserDetail() {
		super();
	}

	public UserDetail(int userDetailId) {
		super();
		this.userDetailId = userDetailId;
	}

	@Column(name="Password")
	private String password;
	
	@OneToMany(mappedBy="userDetail", fetch=FetchType.EAGER)
	private List<UserRoleFeaturePermissionMapping> userRoleFeaturePermissionMappings;
	
	@OneToMany(mappedBy="userDetail")
	private List<FrameworkType> frameworkTypes;
	
	@OneToMany(mappedBy="userDetail")
	private List<UserLoginMeta> userLoginMetas;
	
	@OneToMany(mappedBy="userDetail", fetch=FetchType.EAGER)
	private List<TemplateDetail> templateDetails;
	
	@OneToMany(mappedBy="userDetail", fetch=FetchType.EAGER)
	private List<TXNTemplateDetail> txnTemplateDetails;
 
	public int getUserDetailId() {
		return userDetailId;
	}

	public void setUserDetailId(int userDetailId) {
		this.userDetailId = userDetailId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public boolean isLive() {
		return isLive;
	}

	public void setLive(boolean isLive) {
		this.isLive = isLive;
	}

	public Timestamp getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public List<UserRoleFeaturePermissionMapping> getUserRoleFeaturePermissionMappings() {
		return userRoleFeaturePermissionMappings;
	}

	public void setUserRoleFeaturePermissionMappings(
			List<UserRoleFeaturePermissionMapping> userRoleFeaturePermissionMappings) {
		this.userRoleFeaturePermissionMappings = userRoleFeaturePermissionMappings;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<FrameworkType> getFrameworkTypes() {
		return frameworkTypes;
	}

	public List<UserLoginMeta> getUserLoginMetas() {
		return userLoginMetas;
	}

	public List<TemplateDetail> getTemplateDetails() {
		return templateDetails;
	}

	public List<TXNTemplateDetail> getTxnTemplateDetails() {
		return txnTemplateDetails;
	}

	public void setFrameworkTypes(List<FrameworkType> frameworkTypes) {
		this.frameworkTypes = frameworkTypes;
	}

	public void setUserLoginMetas(List<UserLoginMeta> userLoginMetas) {
		this.userLoginMetas = userLoginMetas;
	}

	public void setTemplateDetails(List<TemplateDetail> templateDetails) {
		this.templateDetails = templateDetails;
	}

	public void setTxnTemplateDetails(List<TXNTemplateDetail> txnTemplateDetails) {
		this.txnTemplateDetails = txnTemplateDetails;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

}
