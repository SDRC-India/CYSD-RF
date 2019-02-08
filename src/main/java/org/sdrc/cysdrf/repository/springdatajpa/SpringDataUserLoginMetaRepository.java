package org.sdrc.cysdrf.repository.springdatajpa;

import java.sql.Timestamp;

import org.sdrc.cysdrf.domain.UserLoginMeta;
import org.sdrc.cysdrf.reposiotry.UserLoginMetaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface SpringDataUserLoginMetaRepository extends UserLoginMetaRepository, Repository<UserLoginMeta, Integer> {

	@Override
	@Modifying 
	@Query("UPDATE UserLoginMeta logInMeta SET logInMeta.loggedOutDateTime = :loggedOutDateTime , logInMeta.isAlive =FALSE WHERE logInMeta.userLogInMetaId = :userLogInMetaId ")
	void updateStatus(@Param("loggedOutDateTime")Timestamp loggedOutDateTime, @Param("userLogInMetaId")long userLogInMetaId);
	
}
