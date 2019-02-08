package org.sdrc.cysdrf.service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;

import org.sdrc.cysdrf.model.UserDetailModel;
import org.sdrc.cysdrf.model.UserRoleSchemeModel;

public interface UserService {

	UserDetailModel setUserDetail(String emailId);

	long saveUserLoginMeta(String ipAddress, Integer userId, String userAgent);

	void addNewUser(String userName, String emailId, String contactNum, String password);

	boolean activeDeactiveUser(Integer userId, boolean isLive);

	List<UserRoleSchemeModel> findAll();

	List<UserDetailModel> findAllUser();

	List<UserDetailModel> getUsersNotAttachedToRole();

	boolean attachRole(Integer roleFeaturePermissionSchemeId, String schemeName, Integer userId);

	void detachRole(Integer userId);

	void updateLoggedOutStatus(long userLoginMetaId, Timestamp loggedOutDateTime);

	void createCurrentTimeperiod() throws ParseException;

}
