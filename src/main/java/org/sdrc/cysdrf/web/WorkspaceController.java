package org.sdrc.cysdrf.web;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.RandomStringUtils;
import org.sdrc.core.Authorize;
import org.sdrc.cysdrf.model.CategoryDetailsModel;
import org.sdrc.cysdrf.model.IsActiveModel;
import org.sdrc.cysdrf.model.TemplateTXNModel;
import org.sdrc.cysdrf.service.AggregationService;
import org.sdrc.cysdrf.service.WorkspaceService;
import org.sdrc.cysdrf.util.ValueObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cysd")
public class WorkspaceController {

	@Autowired
	private WorkspaceService workspaceService;
	
	@Autowired
	private ResourceBundleMessageSource applicationMessageSource;
	
	@Autowired
	private AggregationService operationalService;
	
	@Autowired
	private ResourceBundleMessageSource messageSourceNotification;
	
	@ResponseBody
	@RequestMapping("/getCategories")
	public List<CategoryDetailsModel> getCategoryDetails(){
		return workspaceService.getAllCategoryList();
	}
	
	@ResponseBody
	@RequestMapping("/getRepoFiles")
	public List<ValueObject> getAllFiles(){
		return workspaceService.getFileNames();
	}
	
	@RequestMapping(value = "/downloadFile", method=RequestMethod.POST)
	public void downLoad(@RequestParam("fileName") String name, HttpServletResponse response)
			throws IOException {
		InputStream inputStream;
		String fileName = "";
		try {
			fileName=name.replaceAll("%3A", ":").replaceAll("%2F", "/").replaceAll("%5C", "/").replaceAll("%2C",",").replaceAll("\\+", " ");
			inputStream = new FileInputStream(fileName);
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"",
					new java.io.File(fileName).getName());
			response.setHeader(headerKey, headerValue);
			response.setContentType("application/octet-stream"); //for all file type
			ServletOutputStream outputStream = response.getOutputStream();
			FileCopyUtils.copy(inputStream, outputStream);
			outputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
//		finally{
//			new File(fileName).delete();
//		}
		
	}
	
	
	
	@Authorize(feature="approve", permission="Edit")
	@RequestMapping(value="/stateAdminTXN")
	public String getStateAdminTXN(){
		 return "stateAdminTransation";
	}
	
	@Authorize(feature="workspace", permission="Edit")
	@RequestMapping(value="/workspace")
	public String getWorkspace(){
		 return "workspace";
	}
	
	@Authorize(feature="workspace", permission="Edit")
	@RequestMapping(value="/transaction")
	public String getTransactions(){
		 return "transaction";
	}
	
	@Authorize(feature="approve", permission="Edit")
	@RequestMapping(value="/superAdmin")
	public String getSuperAdmin(){
		 return "superAdmin";
	}
	
	@RequestMapping(value="/getAllProjects")
	@ResponseBody
	public List<ValueObject> getAllProjects(@RequestParam("frameworkType")Integer frameworkType){
		return workspaceService.getProjects(frameworkType);
	}
	
	@RequestMapping(value="/getCurrentTimeperiod")
	@ResponseBody
	public List<ValueObject> getCurrentTimeperiod(){
		return workspaceService.getCurrentTimeperiod();
	}
	
	@RequestMapping(value="/uploadTarget", method=RequestMethod.POST)
	@ResponseBody
	public String uploadTargetFile(HttpServletRequest request,
			@RequestParam("timePeriodId") Integer timePeriodId,
			@RequestParam("timePeriod") String timePeriod,
			@RequestParam("frameworkType") Integer frameworkType,
			@RequestParam("uploadType") String uploadType,
			@RequestParam("file") MultipartFile file,
			@RequestParam("frameworkName") String frameworkName) throws Exception{
		return workspaceService.uploadTargetOrAchiveFile(timePeriodId, request.getRemoteAddr(), file, uploadType, frameworkType, frameworkName);
	}
	
	/*@RequestMapping("/saveDownloadedFileInfo")
	public void setDownloadFileInfo(HttpServletRequest request,Integer templateId){
		workspaceService.saveDownloadDetail(templateId, request.getRemoteAddr());
	}*/
	
	@RequestMapping(value="/createNewProject", method=RequestMethod.POST)
	@ResponseBody
	public boolean saveIndicatorClassificationsDetail(@RequestParam("icName") String icName, 
			@RequestParam("icType") String icType){
		Integer icParentNId =39; 
		String icGid = RandomStringUtils.randomAlphanumeric(40).toLowerCase(); 
		Byte icGlobal = 0; 
		Integer icOrder = (int) (Math.random()*100);
		workspaceService.saveIndicatorClassification(icParentNId, icGid, icName, icGlobal, icType, icOrder);
		return true;
	}

	@RequestMapping(value="/fileName", method=RequestMethod.POST)
	@ResponseBody
	public String getTemplateFile(@RequestParam("downloadType") String downloadType,
			@RequestParam("timePeriod") String timePeriod,
			@RequestParam("timePeriodId") Integer timePeriodId,
			@RequestParam("frameworkType") Integer frameworkType,
			@RequestParam("frameworkName") String frameworkName) throws Exception{
		return workspaceService.getFile(downloadType, timePeriodId, frameworkType, timePeriod, frameworkName);
	}
	
	@RequestMapping(value="/saveFile", method=RequestMethod.POST)
	public String saveFile(@RequestParam("timePeriodId") Integer timePeriodId,
			@RequestParam("timePeriod") String timePeriod,
			@RequestParam("project") String projects,
			@RequestParam("frameworkType") Integer frameworkType,
			@RequestParam("uploadType") String uploadType, 
			RedirectAttributes redirectAttributes) throws Exception{
		workspaceService.saveFile(timePeriodId, timePeriod, projects, frameworkType);
		return "workspace";
	}
	
	@RequestMapping("/getAllFramework")
	@ResponseBody
	public List<ValueObject> getAllFramework(){
		return workspaceService.getAllFrameworks();
	}
	
	@RequestMapping(value="/getUserTXNFiles")
	@ResponseBody
	public List<TemplateTXNModel> getUserTemplates(){
		return workspaceService.getUserSpecificTXNTemplates();
	}
	
	@RequestMapping("/getAllUserTxnForms")
	@ResponseBody
	public List<TemplateTXNModel> getAllUserTemplates(@RequestParam("timePeriodId") String timePeriodId){
		return workspaceService.getAllUserTxnTemplates(timePeriodId);
	}
	
	@RequestMapping("/getActiveClass")
	@ResponseBody
	public IsActiveModel getActiveClass(@RequestParam("timePeriodId") Integer timePeriodId,
			@RequestParam("frameworkTypeId") Integer frameworkTypeId,
			@RequestParam("frameworkTypeName") String frameworkTypeName){
		return workspaceService.setIsActiveClass(timePeriodId, frameworkTypeId, frameworkTypeName);
	}
	
	@RequestMapping(value="/adminUploadAchieve", method=RequestMethod.POST, produces = {"application/json"})
	@ResponseBody
	public String uploadAdminAchieveFile(MultipartHttpServletRequest request,
			@RequestParam("userId") Integer userId, @RequestParam("txnTemplateId") Integer txnTemplateId,
			@RequestParam("timePeriodId") Integer timePeriodId,
			@RequestParam("frameworkId") Integer frameworkId) throws Exception{
		MultipartFile multipartFile = request.getFile("file_"+userId);
		return workspaceService.saveAchieveByAdmin(userId, txnTemplateId, multipartFile, timePeriodId, frameworkId);
	}
	
	@RequestMapping(value="/adminApprove", method=RequestMethod.POST)
	@ResponseBody
	public boolean approveAchievedFile(@RequestParam("userId") Integer userId, 
			@RequestParam("txnTemplateId") Integer txnTemplateId,
			@RequestParam("timePeriodId") Integer timePeriodId,
			@RequestParam("frameworkId") Integer frameworkId) throws Exception{
		return workspaceService.approveAchieveFileByAdmin(userId, txnTemplateId, timePeriodId, frameworkId);
	}
	
	/*@RequestMapping(value="/doAggregateTargetAchieve", method=RequestMethod.POST)
	@ResponseBody
	public boolean doAggregateTarget(@RequestParam(value="userId",required=false) Integer userId,
			@RequestParam(value="areaCode",required=false) String areaCode,
			@RequestParam("timePeriodId") Integer timePeriodId,
			@RequestParam("frameworkType") Integer frameworkType,
			@RequestParam("uploadType") String uploadType) throws Exception{
		return workspaceService.persisitTargetIntoUtData(userId, areaCode, timePeriodId, frameworkType, uploadType);
	}*/
	
	@RequestMapping(value="/getTargetFile", method=RequestMethod.POST)
	@ResponseBody
	public String getTargetFile(@RequestParam("userName") String userName,
			@RequestParam("userId") Integer userId,
			@RequestParam("timePeriodId") Integer timePeriodId,
			@RequestParam("frameworkType") Integer frameworkType,
			@RequestParam("targetFileName") String targetFileName,
			@RequestParam("frameworkTypeName") String frameworkTypeName) throws Exception{
		return workspaceService.getAdminTargetFileName(userName, userId, timePeriodId, frameworkType, targetFileName, frameworkTypeName);
	}
	
	/*@RequestMapping("/uploadOperational")
	public String uploadOperational(RedirectAttributes redirectAttributes) throws IOException{
		int sourceNId=39;
		String areaCode="IND021";
		int timeperiodId=2;
		String fileName="D://CYSD_Operational//File Reading//CYSD_OP_RFW_270916_r2.xls";
		Boolean flag = false;
		String errmsg=operationalService.oprationalAggregate(sourceNId, areaCode, timeperiodId, fileName, flag);
		List<String> msgs = new ArrayList<String>();
		msgs.add(errmsg);
		redirectAttributes.addFlashAttribute("formError", msgs);
		redirectAttributes.addFlashAttribute("className",messageSourceNotification.getMessage("bootstrap.alert.success", null, null));
		return "home";
	}*/
	
	@RequestMapping("/getAllFactsheetFramework")
	@ResponseBody
	public List<ValueObject> getAllFactsheetFramework(){
		return workspaceService.getAllFrameworksForFactsheet();
	}
	
	/*@RequestMapping(value="/updateAreaCode")
	@ResponseBody
	public String updateAreaCode(){
		 return workspaceService.setAreaId();
	}*/
	
}
