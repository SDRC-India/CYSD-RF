package org.sdrc.cysdrf.reposiotry;

import java.util.List;

import org.sdrc.cysdrf.domain.TXNTemplateDetail;

public interface TXNTemplateDetailRepository {

	TXNTemplateDetail save(TXNTemplateDetail txnTemplateDetail);

	List<TXNTemplateDetail> findByUserIdAndIsAlive(Integer userId);

	List<TXNTemplateDetail> findByTimeperiodIdAndIsAlive(Integer userDetailId, Integer timePeriodId);

	List<TXNTemplateDetail> findByUserIdAndTimePeriodIdAndFrameworkAndIsAlive(
			Integer userId, Integer timePeriodId, Integer frameworkTypeId); 
	
	TXNTemplateDetail findByTXNTemplateId(Integer tXNTemplateId);
	
	List<TXNTemplateDetail> findAll();
}
