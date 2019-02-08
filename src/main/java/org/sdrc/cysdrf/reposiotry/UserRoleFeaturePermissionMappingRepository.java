package org.sdrc.cysdrf.reposiotry;

import java.util.List;

import org.sdrc.cysdrf.domain.UserRoleFeaturePermissionMapping;
import org.springframework.transaction.annotation.Transactional;

public interface UserRoleFeaturePermissionMappingRepository {

	List<UserRoleFeaturePermissionMapping> findAll();
	
	UserRoleFeaturePermissionMapping save(UserRoleFeaturePermissionMapping userRoleFeaturePermissionMapping);

	@Transactional("webTransactionManager")
	void deleteByUserId(Integer userId);
}
