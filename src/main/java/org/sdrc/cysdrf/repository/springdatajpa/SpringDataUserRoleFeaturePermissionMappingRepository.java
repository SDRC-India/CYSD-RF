package org.sdrc.cysdrf.repository.springdatajpa;

import org.sdrc.cysdrf.domain.UserRoleFeaturePermissionMapping;
import org.sdrc.cysdrf.reposiotry.UserRoleFeaturePermissionMappingRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface SpringDataUserRoleFeaturePermissionMappingRepository extends 
				UserRoleFeaturePermissionMappingRepository, Repository<UserRoleFeaturePermissionMapping, Integer>{

	@Override
	@Modifying
	@Query("DELETE FROM UserRoleFeaturePermissionMapping urfpm WHERE urfpm.userDetail.userDetailId = :userId")
	void deleteByUserId(@Param("userId") Integer userId);
}
