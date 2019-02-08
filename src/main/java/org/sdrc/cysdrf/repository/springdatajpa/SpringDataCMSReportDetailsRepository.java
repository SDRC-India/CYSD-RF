package org.sdrc.cysdrf.repository.springdatajpa;

import java.util.List;

import org.sdrc.cysdrf.domain.ReportDetails;
import org.sdrc.cysdrf.reposiotry.CMSReportDetailsRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface SpringDataCMSReportDetailsRepository extends
		CMSReportDetailsRepository, JpaRepository<ReportDetails, Integer> {

	@Override
	@Query("select rd from ReportDetails rd  where rd.isAlive IS TRUE")
	public List<ReportDetails> findAllReportByIsLiveTrue();

	@Override
	@Query("select rd from ReportDetails rd  where rd.reportId = :reportId and rd.isAlive=1")
	ReportDetails findReportDetailsById(@Param("reportId") Integer reportId);

	@Override
	@Modifying
	@Transactional
	@Query("UPDATE ReportDetails rd SET rd.isAlive = false where rd.reportId = :reportId")
	public void deleteRecord(@Param("reportId") Integer reportId);
	
	@Override
	@Query("select rd from ReportDetails rd  where rd.isAlive IS TRUE and rd.isConfidential IS FALSE ")
	public List<ReportDetails> findAllNonConfidentialReportByIsLiveTrue();

}
