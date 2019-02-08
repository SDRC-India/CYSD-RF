package org.sdrc.cysdrf.repository.springdatajpa;

import java.util.List;

import org.sdrc.cysdrf.domain.ReportTypeDetails;
import org.sdrc.cysdrf.reposiotry.CMSReportTypeDetailsRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SpringDataCMSReportTypeDetailsRepository extends
		CMSReportTypeDetailsRepository, JpaRepository<ReportTypeDetails,Integer> {
	
	@Override
	@Query("Select rtd from ReportTypeDetails rtd")
	public List<ReportTypeDetails> findAllReportType();
	
	@Override
	@Query("Select rtd from ReportTypeDetails rtd where rtd.reportTypeId != :reportTypeId")
	public List<ReportTypeDetails> findAllReportTypeForNonConfendtialType(@Param("reportTypeId")Integer reportTypeId);

}
