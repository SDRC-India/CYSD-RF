package org.sdrc.cysdrf.reposiotry;

import java.sql.Timestamp;
import java.util.List;

import org.sdrc.cysdrf.domain.UserDetail;
import org.springframework.transaction.annotation.Transactional;

public interface UserDetailsRepository {

	UserDetail findByEmailId(String emailId);

	UserDetail save(UserDetail userDetail);

	@Transactional("webTransactionManager")
	void setIsLive(boolean isLive, String updatedBy, Timestamp updatedDate, Integer userId);

	List<UserDetail> findUserDetailNotInUserRoleFeaturePermissionMapping();
	
	List<UserDetail> findAll();
	
	UserDetail findByUserDetailId(Integer userDetailId);
}
