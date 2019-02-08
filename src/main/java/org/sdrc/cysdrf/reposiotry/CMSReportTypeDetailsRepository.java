package org.sdrc.cysdrf.reposiotry;

import java.util.List;

import org.sdrc.cysdrf.domain.ReportTypeDetails;

public interface CMSReportTypeDetailsRepository {
	
	public List<ReportTypeDetails> findAllReportType();
	
	public List<ReportTypeDetails> findAllReportTypeForNonConfendtialType(Integer reportTypeId);

}
