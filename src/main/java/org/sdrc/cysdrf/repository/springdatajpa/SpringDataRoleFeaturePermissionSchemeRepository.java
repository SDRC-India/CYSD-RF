package org.sdrc.cysdrf.repository.springdatajpa;

import java.util.List;

import org.sdrc.cysdrf.domain.RoleFeaturePermissionScheme;
import org.sdrc.cysdrf.reposiotry.RoleFeaturePermissionSchemeRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

public interface SpringDataRoleFeaturePermissionSchemeRepository extends RoleFeaturePermissionSchemeRepository, Repository<RoleFeaturePermissionScheme, Integer>{

	@Override
	@Query("SELECT rf FROM RoleFeaturePermissionScheme rf WHERE rf.roleFeaturePermissionSchemeId IN "
			+ "(SELECT MAX(rf.roleFeaturePermissionSchemeId) FROM RoleFeaturePermissionScheme rf GROUP BY rf.schemeName)")
	List<RoleFeaturePermissionScheme> findByMaxRoleFeaturePermissionSchemeId();
}
