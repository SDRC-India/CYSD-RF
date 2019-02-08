package org.sdrc.cysdrf.repository.springdatajpa;

import org.sdrc.cysdrf.domain.TemplateDetail;
import org.sdrc.cysdrf.reposiotry.TemplateDetailRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface SpringDataTemplateDetailRepository extends TemplateDetailRepository, Repository<TemplateDetail, Integer> {

	@Override
	@Query("SELECT td FROM TemplateDetail td WHERE td.timePeriodId = :timePeriodId "
			+ "AND td.frameworkType.frameworkTypeId = :frameworkTypeId "
			+ "AND td.userDetail.userDetailId = :userId "
			+ "AND td.isActive IS TRUE")
	TemplateDetail findByTimePeriodIdAndFrameworkTypeIdAndUserIdAndIsActive(@Param("timePeriodId") Integer timePeriodId, 
			@Param("frameworkTypeId") Integer frameworkTypeId, @Param("userId") Integer userId);
}

