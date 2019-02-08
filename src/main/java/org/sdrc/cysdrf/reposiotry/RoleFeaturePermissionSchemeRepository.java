package org.sdrc.cysdrf.reposiotry;

import java.util.List;

import org.sdrc.cysdrf.domain.RoleFeaturePermissionScheme;

public interface RoleFeaturePermissionSchemeRepository {

	List<RoleFeaturePermissionScheme> findAll();
	
	List<RoleFeaturePermissionScheme> findByMaxRoleFeaturePermissionSchemeId();
	
	List<RoleFeaturePermissionScheme> findBySchemeName(String schemeName);
	
	RoleFeaturePermissionScheme findByRoleFeaturePermissionSchemeId(Integer roleFeaturePermissionSchemeId);
}
