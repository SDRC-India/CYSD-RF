package org.sdrc.cysdrf.model;

import java.util.List;

public class UserDetailModel {

	private Integer userId;
	
	private String userName;
	
	private String email;
	
	private String contactNum;
	
	private String password;
	
	private String address;
	
	private String updatedBy;
	
	private String updatedDate;
	
	private boolean isLive;
	
	private long loginMetaId;
	
	private String createdBy;
	
	private String createdDate;
	
	private List<UserRoleFeaturePermissionMappingModel> userRoleFeaturePermissionMappings;

	public Integer getUserId() {
		return userId;
	}

	public String getUserName() {
		return userName;
	}

	public String getEmail() {
		return email;
	}

	public String getContactNum() {
		return contactNum;
	}

	public String getPassword() {
		return password;
	}

	public String getAddress() {
		return address;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public boolean isLive() {
		return isLive;
	}

	public List<UserRoleFeaturePermissionMappingModel> getUserRoleFeaturePermissionMappings() {
		return userRoleFeaturePermissionMappings;
	}
	
	public void setUserRoleFeaturePermissionMappings(
			List<UserRoleFeaturePermissionMappingModel> userRoleFeaturePermissionMappings) {
		this.userRoleFeaturePermissionMappings = userRoleFeaturePermissionMappings;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setContactNum(String contactNum) {
		this.contactNum = contactNum;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public void setLive(boolean isLive) {
		this.isLive = isLive;
	}

	public long getLoginMetaId() {
		return loginMetaId;
	}

	public void setLoginMetaId(long loginMetaId) {
		this.loginMetaId = loginMetaId;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

}
