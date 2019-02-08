package org.sdrc.cysdrf.model;

public class WorkspaceModel {

	private String userName;
	private String userEmail;
	private String userId;
	private String password;
	private String roleName;
	private String roleId;
	private Integer featurePermissionId;
	private String areaCode;
	private Integer RoleFeaturePermissionSchemeId;
	private Integer UserRoleFeaturePermissionId;
	
	public String getUserName() {
		return userName;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public String getUserId() {
		return userId;
	}
	public String getPassword() {
		return password;
	}
	public String getRoleName() {
		return roleName;
	}
	public String getRoleId() {
		return roleId;
	}
	public Integer getFeaturePermissionId() {
		return featurePermissionId;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public Integer getRoleFeaturePermissionSchemeId() {
		return RoleFeaturePermissionSchemeId;
	}
	public Integer getUserRoleFeaturePermissionId() {
		return UserRoleFeaturePermissionId;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public void setFeaturePermissionId(Integer featurePermissionId) {
		this.featurePermissionId = featurePermissionId;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public void setRoleFeaturePermissionSchemeId(Integer roleFeaturePermissionSchemeId) {
		RoleFeaturePermissionSchemeId = roleFeaturePermissionSchemeId;
	}
	public void setUserRoleFeaturePermissionId(Integer userRoleFeaturePermissionId) {
		UserRoleFeaturePermissionId = userRoleFeaturePermissionId;
	}
	
}
