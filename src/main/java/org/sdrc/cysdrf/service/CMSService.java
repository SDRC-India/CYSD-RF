package org.sdrc.cysdrf.service;

import java.util.List;

import org.sdrc.cysdrf.model.ReportDetailsModel;
import org.sdrc.cysdrf.model.ReportTypeDetailsModel;
import org.sdrc.cysdrf.model.SectionDetailsModel;
import org.sdrc.cysdrf.model.TagDetailsModel;
import org.springframework.web.multipart.MultipartFile;

public interface CMSService {

	public List<SectionDetailsModel> getAllSections();

	public List<ReportDetailsModel> getAllReports();

	public String saveReport(String reportTitle, String desc, Integer parentId,
			String reportType, String url, Integer sectionId,
			String tags, MultipartFile[] files,MultipartFile file);

	public boolean editSection(String sectionName, Integer sectionId,
			List<String> tags, String description, Integer parentId);

	public boolean newSection(String sectionName, Integer parentId,
			List<String> tags, String description);

	public void updateRecord(Integer reportId, String reportName,
			String description, String tags);

	public List<TagDetailsModel> getAllTags();

	public List<ReportTypeDetailsModel> getAllReportType();
	
	public String getReportDocumentById(Integer reportId);
	
	public boolean deleteReport(Integer reportId);
}
