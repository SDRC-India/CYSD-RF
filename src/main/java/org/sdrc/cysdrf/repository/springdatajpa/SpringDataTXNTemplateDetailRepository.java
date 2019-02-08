package org.sdrc.cysdrf.repository.springdatajpa;

import java.util.List;

import org.sdrc.cysdrf.domain.TXNTemplateDetail;
import org.sdrc.cysdrf.reposiotry.TXNTemplateDetailRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface SpringDataTXNTemplateDetailRepository extends TXNTemplateDetailRepository, Repository<TXNTemplateDetail, Integer> {

	@Override
	@Query("SELECT txntd FROM TXNTemplateDetail txntd WHERE txntd.templateDetail.userDetail.userDetailId = :userId "
			+ "AND txntd.isAlive IS TRUE")
	List<TXNTemplateDetail> findByUserIdAndIsAlive(@Param("userId") Integer userId);
	
//	@Override
//	@Query("select txntd "
//			+ "from FrameworkType ft, UserDetail ud,TemplateDetail td, TXNTemplateDetail txntd "
//			+ "where ud.userDetailId = ft.userDetail.userDetailId "
//			+ "and ud.userDetailId = :userDetailId "
//			+ "and td.timePeriodId = :timePeriodId "
//			+ "and txntd.isAlive IS TRUE "
//			+ "and td.templateId = txntd.templateDetail.templateId")
//	List<TXNTemplateDetail> findByTimeperiodIdAndIsAlive(@Param("userDetailId") Integer userDetailId,
//														 @Param("timePeriodId") Integer timePeriodId);
	
	@Override
	@Query("select txntd "
			+ "from UserDetail ud,TemplateDetail td, TXNTemplateDetail txntd "
			+ "where ud.userDetailId = :userDetailId "
			+ "and td.timePeriodId = :timePeriodId "
			+ "and txntd.isAlive IS TRUE "
			+ "and td.templateId = txntd.templateDetail.templateId")
	List<TXNTemplateDetail> findByTimeperiodIdAndIsAlive(@Param("userDetailId") Integer userDetailId,
														 @Param("timePeriodId") Integer timePeriodId);
	
	@Override
	@Query("SELECT txntd FROM TXNTemplateDetail txntd WHERE txntd.templateDetail.userDetail.userDetailId = :userId "
			+ "AND txntd.templateDetail.timePeriodId = :timePeriodId "
			+ "AND txntd.templateDetail.frameworkType.frameworkTypeId = :frameworkTypeId "
			+ "AND txntd.isAlive IS TRUE")
	List<TXNTemplateDetail> findByUserIdAndTimePeriodIdAndFrameworkAndIsAlive(@Param("userId") Integer userId,
			 @Param("timePeriodId") Integer timePeriodId,
			 @Param("frameworkTypeId") Integer frameworkTypeId);
}
