package org.sdrc.cysdrf.service;

import java.util.List;

import org.sdrc.cysdrf.model.CategoryDetailsModel;
import org.sdrc.cysdrf.model.IsActiveModel;
import org.sdrc.cysdrf.model.TemplateTXNModel;
import org.sdrc.cysdrf.util.ValueObject;
import org.springframework.web.multipart.MultipartFile;

public interface WorkspaceService {

	List<CategoryDetailsModel> getAllCategoryList();

	List<ValueObject> getFileNames();
	
	List<ValueObject> getProjects(Integer frameworkType);
	
	List<ValueObject> getCurrentTimeperiod();

//	void saveDownloadDetail(Integer templateId, String ipAddress);
	
	void saveIndicatorClassification(Integer icParentNId, String icGid, String icName,	Byte icGlobal, 
			String icType, Integer icOrder);

	String getFile(String downloadType, Integer timePeriodId, Integer frameworkType,String timePeriod, String frameworkName) throws Exception;

	String uploadTargetOrAchiveFile(Integer timpePeriodNid, String ipAddress, MultipartFile file, String uploadType,
			Integer frameworkType, String frameworkName) throws Exception;

	void saveFile(Integer timpePeriodNid, String timePeriod, String projects, Integer frameworkType);

	List<ValueObject> getAllFrameworks();

	List<TemplateTXNModel> getUserSpecificTXNTemplates();
	
	List<TemplateTXNModel> getAllUserTxnTemplates(String timePeriodId);

	IsActiveModel setIsActiveClass(Integer timePeriodId, Integer frameworkTypeId, String frameworkTypeName);

	String saveAchieveByAdmin(Integer userId, Integer txnTemplateId, MultipartFile file, Integer timePeriodId, Integer frameworkId) throws Exception;

	boolean approveAchieveFileByAdmin(Integer userId, Integer txnTemplateId, Integer timePeriodId, Integer frameworkId) throws Exception;

	String getAdminTargetFileName(String userName, Integer userId, Integer timePeriodId, Integer frameworkType, String targetFileName, String frameworkTypeName)
			throws Exception;

	List<ValueObject> getAllFrameworksForFactsheet();

//	String setAreaId();

}
