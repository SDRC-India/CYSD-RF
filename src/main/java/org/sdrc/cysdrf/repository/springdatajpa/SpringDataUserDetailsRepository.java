package org.sdrc.cysdrf.repository.springdatajpa;

import java.sql.Timestamp;
import java.util.List;

import org.sdrc.cysdrf.domain.UserDetail;
import org.sdrc.cysdrf.reposiotry.UserDetailsRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface SpringDataUserDetailsRepository extends UserDetailsRepository,Repository<UserDetail, Integer> {

	@Override
	@Query("SELECT ud FROM UserDetail ud WHERE ud.emailId = :emailId AND ud.isLive = TRUE")
	UserDetail findByEmailId(@Param("emailId") String emailId);
	
	@Override
	@Modifying
	@Query("UPDATE UserDetail ud SET ud.isLive = :isLive, "
			+ "ud.updatedBy = :updatedBy, "
			+ "ud.updatedDate = :updatedDate WHERE ud.userDetailId = :userId")
	void setIsLive(@Param("isLive") boolean isLive,@Param("updatedBy") String updatedBy, @Param("updatedDate") Timestamp updatedDate,
			@Param("userId") Integer userId);
	
	@Override
	@Query("SELECT ud FROM UserDetail ud WHERE ud.userDetailId NOT IN (SELECT urfpm.userDetail.userDetailId FROM UserRoleFeaturePermissionMapping urfpm) "
			+ "AND ud.isLive IS TRUE")
	List<UserDetail> findUserDetailNotInUserRoleFeaturePermissionMapping();
}
