package org.sdrc.cysdrf.reposiotry;

import java.util.List;

import org.sdrc.cysdrf.domain.TemplateDetail;

public interface TemplateDetailRepository {

	TemplateDetail save(TemplateDetail templateDetail);
	
	TemplateDetail findByTimePeriodIdAndFrameworkTypeIdAndUserIdAndIsActive(Integer timePeriodId,
			Integer frameworkTypeId, Integer userId);
	
	List<TemplateDetail> findAll();
}
