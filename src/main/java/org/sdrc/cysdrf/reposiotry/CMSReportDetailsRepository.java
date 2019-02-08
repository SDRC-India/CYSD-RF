package org.sdrc.cysdrf.reposiotry;

import java.util.List;

import org.sdrc.cysdrf.domain.ReportDetails;
import org.springframework.transaction.annotation.Transactional;

public interface CMSReportDetailsRepository {

	List<ReportDetails> findAllReportByIsLiveTrue();

	@Transactional
	void save(ReportDetails reportDetails);

	ReportDetails findReportDetailsById(Integer reportId);
	
	List<ReportDetails> findAllNonConfidentialReportByIsLiveTrue();
	
	void deleteRecord(Integer reportId);

}
