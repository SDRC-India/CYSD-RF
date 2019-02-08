package org.sdrc.cysdrf.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.ServletContext;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellReference;
import org.sdrc.cysdrf.domain.CategoryDetail;
import org.sdrc.cysdrf.domain.FrameworkType;
import org.sdrc.cysdrf.domain.TXNTemplateDetail;
import org.sdrc.cysdrf.domain.TemplateDetail;
import org.sdrc.cysdrf.domain.UserDetail;
import org.sdrc.cysdrf.model.CategoryDetailsModel;
import org.sdrc.cysdrf.model.IsActiveModel;
import org.sdrc.cysdrf.model.TemplateTXNModel;
import org.sdrc.cysdrf.model.UserDetailModel;
import org.sdrc.cysdrf.reposiotry.CategoryDetailsRepository;
import org.sdrc.cysdrf.reposiotry.FrameworkTypeRepository;
import org.sdrc.cysdrf.reposiotry.TXNTemplateDetailRepository;
import org.sdrc.cysdrf.reposiotry.TemplateDetailRepository;
import org.sdrc.cysdrf.reposiotry.UserDetailsRepository;
import org.sdrc.cysdrf.util.Constants;
import org.sdrc.cysdrf.util.StateManager;
import org.sdrc.cysdrf.util.ValueObject;
import org.sdrc.devinfo.domain.UtAreaEn;
import org.sdrc.devinfo.domain.UtIndicatorClassificationsEn;
import org.sdrc.devinfo.domain.UtTimeperiod;
import org.sdrc.devinfo.repository.AreaRepository;
import org.sdrc.devinfo.repository.IndicatorClassificationsRepository;
import org.sdrc.devinfo.repository.TimeperiodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class WorkspaceServiceImpl implements WorkspaceService{

	@Autowired
	private CategoryDetailsRepository categoryDetailsRepository;
	
//	@Autowired
//	private DownloadDetailRepository downloadDetailRepository;
	
	@Autowired
	private StateManager stateManager;
	
	@Autowired
	private IndicatorClassificationsRepository indicatorClassificationsRepository;
	
	@Autowired
	private TimeperiodRepository timeperiodRepository;
	
	@Autowired
	private TemplateDetailRepository templateDetailRepository;
	
	@Autowired
	private TXNTemplateDetailRepository tXNTemplateDetailRepository;
	
	@Autowired
	private FrameworkTypeRepository frameworkTypeRepository;
	
	@Autowired
	private ServletContext context;
	
	@Autowired
	private ResourceBundleMessageSource messageSourceNotification;
	
	@Autowired
	private ResourceBundleMessageSource messages;
	
	@Autowired
	private AreaRepository areaRepository;
	
	@Autowired
	private AggregationService aggregationService;
	
	@Autowired
	private UserDetailsRepository userDetailsRepository;
	
	private SimpleDateFormat sdf = new SimpleDateFormat(ResourceBundle.getBundle("spring/app").getString("date.format.createUpdateDate"));
	private SimpleDateFormat sdf1 = new SimpleDateFormat(ResourceBundle.getBundle("spring/app").getString("datetime.format"));
	private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(ResourceBundle.getBundle("spring/app").getString("datetime.format"));
	
	@Override
	public List<CategoryDetailsModel> getAllCategoryList(){
		List<CategoryDetail> categoryDetails = categoryDetailsRepository.findAll();
		List<CategoryDetailsModel> categoryDetailsModels = new ArrayList<>();
		for (CategoryDetail categoryDetail : categoryDetails) {
			CategoryDetailsModel categoryDetailsModel = new CategoryDetailsModel();
			categoryDetailsModel.setCategoryId(categoryDetail.getCategoryId());
			categoryDetailsModel.setCategoryName(categoryDetail.getCategoryName());
			categoryDetailsModel.setParentCategoryId(categoryDetail.getParentCategoryId());
			categoryDetailsModel.setType(categoryDetail.getType());
			categoryDetailsModels.add(categoryDetailsModel);
		}
		return categoryDetailsModels;
	}
	
	@Override
	public List<ValueObject> getFileNames(){
		List<String> filePaths = Arrays.asList("D://CRV	S_NEW.xlsx", "D://sdrc.pptx", "D://submission.xml","D://CRVS_NEW.xlsx",
				"D://sdrc.pptx", "D://submission.xml","D://CRVS_NEW.xlsx", "D://sdrc.pptx", "D://submission.xml",
				"D://CRVS_NEW.xlsx", "D://sdrc.pptx", "D://submission.xml");
		List<ValueObject> fileNameImageList = new ArrayList<>();
		for (String filePath : filePaths) {
			ValueObject valueObject = new ValueObject();
			String extension = filePath.split("\\.")[filePath.split("\\.").length-1];
			valueObject.setKey(filePath);
			valueObject.setValue((extension.equals("xlsx") ? "resources\\images\\xlsx_Img.png" : extension.equals("pptx") ? 
					"resources\\images\\pptx_Img.png" : "resources\\images\\txt_Img.jpg"));
			fileNameImageList.add(valueObject);
		}
		
		return fileNameImageList;
	}
	
	//save the current selection in TemplateDetail when the user clicks on Create Template button in worksapce.jsp
	@Override
	@Transactional(value="webTransactionManager", rollbackFor=Exception.class)
	public void saveFile(Integer timpePeriodNid, String timePeriod, String projects,
			Integer frameworkType){
		UserDetailModel userDetailModel = (UserDetailModel) stateManager.getValue(Constants.USER_PRINCIPAL);
		if(null!=userDetailModel){
			TemplateDetail detail = templateDetailRepository.
					findByTimePeriodIdAndFrameworkTypeIdAndUserIdAndIsActive(timpePeriodNid, 1, userDetailModel.getUserId());
			if(null!=detail){
				detail.setActive(false);
				templateDetailRepository.save(detail);	
			}
			TemplateDetail templateDetail = new TemplateDetail();
			templateDetail.setActive(true);
//			templateDetail.setFilePath(filePath);
			templateDetail.setFrameworkType(new FrameworkType(frameworkType));
			templateDetail.setProjects(projects.substring(1, projects.lastIndexOf(']')).replaceAll("\"", ""));
			templateDetail.setTimePeriod(timePeriod);
			templateDetail.setTimePeriodId(timpePeriodNid);
			templateDetail.setUserDetail(new UserDetail(userDetailModel.getUserId()));
			templateDetail.setAreaCode(userDetailModel.getUserRoleFeaturePermissionMappings().get(0).getRoleFeaturePermissionSchemeModel().getAreaCode());
			templateDetail.setCreatedBy(userDetailModel.getUserName());
			templateDetail.setCreatedDate(new Timestamp(new Date().getTime()));
			templateDetailRepository.save(templateDetail);	
		}
		
	}
	
	/*@Override
	@Transactional("webTransactionManager")
	public void saveDownloadDetail(Integer templateId, String ipAddress){
//		DownloadDetail downloadDetail = new DownloadDetail();
//		downloadDetail.setDownloadDate(new Timestamp(new Date().getTime()));
//		downloadDetail.setIpAddress(ipAddress);
//		downloadDetail.setTemplateDetail(templateDetailRepository.f);
//		downloadDetailRepository.save(downloadDetail);
	}*/
	
	@Override
	public List<ValueObject> getProjects(Integer frameworkType) {
		List<UtIndicatorClassificationsEn> allProjects=new ArrayList<UtIndicatorClassificationsEn>();
		if(frameworkType==0){
			allProjects = indicatorClassificationsRepository.findAllProject();
		}
		else{
			allProjects=indicatorClassificationsRepository.getOperationalProjects();
		}
			List<ValueObject> projects = new ArrayList<>();
			for(UtIndicatorClassificationsEn allProject : allProjects){
				ValueObject valueObject = new ValueObject();
				valueObject.setKey(Integer.toString(allProject.getIC_NId()));
				valueObject.setValue(allProject.getIC_Name()+"?"+allProject.getIC_Type());
				projects.add(valueObject);
			}
		return projects;
	}

	//create xlsx templates based on projects selected
	//when the user clicks on download in workspace.jsp
	@Override
	@Transactional("webTransactionManager")
	public String getFile(String downloadType,Integer timePeriodId, Integer frameworkType,String timePeriod, String frameworkName) throws Exception{
		UserDetailModel user = ((UserDetailModel) stateManager.getValue(Constants.USER_PRINCIPAL));
		
		//get the template object from TemplateDetail table( that has been created before) for the logged in user, selected timeperiod
		TemplateDetail templateDetail =templateDetailRepository.
				findByTimePeriodIdAndFrameworkTypeIdAndUserIdAndIsActive(timePeriodId, frameworkType, user.getUserId());
		
		//get the txn list for the above selection
		List<TXNTemplateDetail> txnTemplateDetails = tXNTemplateDetailRepository.
				findByUserIdAndTimePeriodIdAndFrameworkAndIsAlive(user.getUserId(), timePeriodId, frameworkType);
		
//		programme RF---------------------
		if(frameworkName.equalsIgnoreCase(Constants.FRAMEWORK_TYPE_PROGRAMME)){
			String [] projectArrs = templateDetail.getProjects().split(","); //get the projects 
			Integer[] intarray=new Integer[projectArrs.length];
		   
			for(int i=0; i<projectArrs.length; i++){
		        intarray[i]=Integer.parseInt(projectArrs[i].trim());
		    }
			List<Integer> projectList = new ArrayList<Integer>(Arrays.asList(intarray));
			
			//for all projects get the indicatorClassificationsEns
			List<UtIndicatorClassificationsEn> indicatorClassificationsEns = indicatorClassificationsRepository.findByIC_NIdIn(projectList);
			
			String fileName= messageSourceNotification.getMessage(Constants.EXCEL_STORE_PATH, null, null)+"/" +
					"CYSDRF_"+templateDetail.getUserDetail().getUserName()+"_"+templateDetail.getTimePeriod()+".xls";
			
			boolean flag = false; //taregt--achieved column lock--unlock 
			
			//if achieved is already given return the uploaded achieve file
			if(downloadType.equalsIgnoreCase(messageSourceNotification.getMessage(Constants.UPLOAD_TYPE_ACHIEVED, null, null)) && txnTemplateDetails.size()>1){
				return txnTemplateDetails.get(txnTemplateDetails.size()-1).getFilePath();
			}
			//while downloading the blank template for the first time i.e txn  template list is emplty
			else if(txnTemplateDetails.size()==0 && null!=templateDetail && null == templateDetail.getFilePath()){
				FileInputStream inputStream = new FileInputStream(context.getRealPath(
						messageSourceNotification.getMessage(Constants.EXCEL_READ_PATH, null, null)));
//				FileInputStream inputStream = new FileInputStream("D:/RF_Final_RPP_30072016_r6.xls");
				HSSFWorkbook xssfWorkbook = new HSSFWorkbook(inputStream);
				HSSFSheet xssfSheet = xssfWorkbook.getSheet(messageSourceNotification.getMessage(Constants.EXCEL_CREATE_PROJECT_SHEET, null, null)); 
				int size=1; //project row
				int columnNo = 9; //target start column
				
				//for every project create planned--achieved ******
				for(UtIndicatorClassificationsEn indicatorClassificationsEn : indicatorClassificationsEns){
					 CellReference ref = new CellReference(messages.getMessage(Integer.toString(size), null, null));
					 Row r = xssfSheet.getRow(ref.getRow());
					 if (r != null) {
					    Cell c = r.getCell(ref.getCol());
					    c.setCellValue(indicatorClassificationsEn.getIC_Name());
					 }
					 Row targetAchieveRow = xssfSheet.getRow(1);
					 Cell targetCmn = targetAchieveRow.getCell(columnNo);
					 targetCmn.setCellValue(messageSourceNotification.getMessage(Constants.UPLOAD_TYPE_PLANNED, null, null));
					 
					 lockTargetAchiveColumn(xssfWorkbook, columnNo, flag, frameworkType, frameworkName); //****** unlock the target column
					 columnNo++;
					 
					 Cell achieveCmn = targetAchieveRow.getCell(columnNo);
					 achieveCmn.setCellValue(messageSourceNotification.getMessage(Constants.UPLOAD_TYPE_ACHIEVED, null, null));
					 columnNo++;
					 size++;
				}
				//security purpose
				HSSFSheet metaDataSheet = xssfWorkbook.getSheet(messageSourceNotification.getMessage(Constants.EXCEL_UNIQUE_KEY_SHEET, null, null)); 
				if(null!=metaDataSheet){
					Row currentTimeRow = metaDataSheet.createRow(59);
					Cell uniqueCell = currentTimeRow.createCell(101);
					uniqueCell.setCellValue(templateDetail.getTemplateId()+"_"+templateDetail.getUserDetail().getUserName());
					xssfWorkbook.setSheetHidden(1, 2); //hide Per cent sheet
					FileOutputStream outputStream = new FileOutputStream(
							new File(fileName));
					xssfWorkbook.write(outputStream);
					outputStream.close();
					templateDetail.setFilePath(fileName);
					TemplateDetail templateDetail2 = templateDetailRepository.save(templateDetail);	
					return templateDetail2.getFilePath();
				}else
					return null;
				//once target file is created and present in the db, return the filepath
			}else if(downloadType.equalsIgnoreCase(messageSourceNotification.getMessage(Constants.UPLOAD_TYPE_TARGET, null, null)) && txnTemplateDetails.size()>0 
					&& templateDetail.getUpdatedBy()!=null && templateDetail.getUpdatedDate()!=null){
					return templateDetail.getFilePath();
					
				//give the file with unlocked achieve column to fill achieve
			}else if(downloadType.equalsIgnoreCase(messageSourceNotification.getMessage(Constants.UPLOAD_TYPE_TARGET, null, null)) && txnTemplateDetails.size()>0){
				
				String uploadFileName=txnTemplateDetails.get(0).getFilePath();
				FileInputStream inputStream = new FileInputStream(uploadFileName);
				HSSFWorkbook xssfWorkbook = new HSSFWorkbook(inputStream);
				int columnNo = 9; //target start column
				
				//set the locking/unlocking style to planned/achieve columns
				for(int projectSize=0 ; projectSize<indicatorClassificationsEns.size();projectSize++){
					flag = true;
					lockTargetAchiveColumn(xssfWorkbook, columnNo, flag, frameworkType, frameworkName);//lock target column where flag true
					columnNo++;
						 
					flag = false;
					lockTargetAchiveColumn(xssfWorkbook, columnNo, flag, frameworkType, frameworkName);//unlock achieve column where flag false
					columnNo++;
				}
					//security purpose
				HSSFSheet metaDataSheet = xssfWorkbook.getSheet(messageSourceNotification.getMessage(Constants.EXCEL_UNIQUE_KEY_SHEET, null, null)); 
				Row currentTimeRow = metaDataSheet.createRow(59);
				Cell uniqueCell = currentTimeRow.createCell(101);
				uniqueCell.setCellValue(txnTemplateDetails.get(0).gettXNTemplateId()+"_"+templateDetail.getUserDetail().getUserName());
				xssfWorkbook.setSheetHidden(1, 2); //hide Per cent sheet
				FileOutputStream outputStream = new FileOutputStream(
						new File(fileName));
				xssfWorkbook.write(outputStream);
				outputStream.close();
				
				templateDetail.setFilePath(fileName);
				templateDetail.setUpdatedBy(user.getUserName());
				templateDetail.setUpdatedDate(new Timestamp(new Date().getTime()));
				templateDetailRepository.save(templateDetail);	
				return fileName;
			}
			//download the blank template every time it downloads before going to the txn table
			else if(txnTemplateDetails.size()==0 && null!=templateDetail && null!=templateDetail.getFilePath()){
				return templateDetail.getFilePath();
			}else
				return null;
		}
		
		//OPERATIONAL FRAMEWORKTYPE========
		else{
			boolean flag = false; //target--achieved column lock--unlock
			String fileName= messageSourceNotification.getMessage(Constants.EXCEL_STORE_PATH, null, null)+"/" +
					"CYSDRF_OP_"+timePeriod+".xls";
			
			if(downloadType.equalsIgnoreCase(messageSourceNotification.getMessage(Constants.UPLOAD_TYPE_TARGET, null, null))
					&& txnTemplateDetails.size()==0 && null==templateDetail){
				
				TemplateDetail detail = new TemplateDetail();
				
				FileInputStream inputStream = new FileInputStream(context.getRealPath(
						messageSourceNotification.getMessage(Constants.OPERATIONAL_EXCEL_READ_PATH, null, null)));
				
//				FileInputStream inputStream = new FileInputStream("F:/CYSD_OP_RFW_280916_r2.xls");
				HSSFWorkbook xssfWorkbook = new HSSFWorkbook(inputStream);
				int columnNo = 8; // target start column

				lockTargetAchiveColumn(xssfWorkbook, columnNo, flag, frameworkType, frameworkName); // ****** unlock  the target column
					 
				//security purpose
				HSSFSheet metaDataSheet = xssfWorkbook.getSheet(messageSourceNotification.getMessage(Constants.EXCEL_UNIQUE_KEY_SHEET, null, null)); 
				
				if(null!=metaDataSheet){
					
					detail.setFilePath(fileName);
					detail.setActive(true);
					detail.setFrameworkType(new FrameworkType(frameworkType));
					detail.setProjects("17");
					detail.setTimePeriod(timePeriod);
					detail.setTimePeriodId(timePeriodId);
					detail.setUserDetail(userDetailsRepository.findByUserDetailId(user.getUserId()));
					detail.setAreaCode(userDetailsRepository.findByUserDetailId(user.getUserId()).getUserRoleFeaturePermissionMappings().get(0).getRoleFeaturePermissionScheme().getAreaCode());
					detail.setCreatedBy(user.getUserName());
					detail.setCreatedDate(new Timestamp(new Date().getTime()));
					TemplateDetail detail2 = templateDetailRepository.save(detail);	
					
					Row currentTimeRow = metaDataSheet.createRow(59);
					Cell uniqueCell = currentTimeRow.createCell(101);
					uniqueCell.setCellValue(detail2.getTemplateId()+"_"+detail2.getUserDetail().getUserName());
					xssfWorkbook.setSheetHidden(1, 2); //hide Per cent sheet
					xssfWorkbook.setSheetHidden(2, 2); //hide sheet3
					FileOutputStream outputStream = new FileOutputStream(
							new File(fileName));
					xssfWorkbook.write(outputStream);
					outputStream.close();
					return fileName;
				}else
					return null;
			}else if(downloadType.equalsIgnoreCase(messageSourceNotification.getMessage(Constants.UPLOAD_TYPE_TARGET, null, null))
					&& null!=templateDetail && txnTemplateDetails.size() == 0){
				return templateDetail.getFilePath();
			}else if(downloadType.equalsIgnoreCase(messageSourceNotification.getMessage(Constants.UPLOAD_TYPE_TARGET, null, null))
					&& txnTemplateDetails.size() > 0 && templateDetail.getUpdatedBy()!=null && templateDetail.getUpdatedDate()!=null){
				return templateDetail.getFilePath();
			}else if (downloadType.equalsIgnoreCase(messageSourceNotification
					.getMessage(Constants.UPLOAD_TYPE_TARGET, null, null))
					&& txnTemplateDetails.size() > 0 && null != templateDetail) {

//				String fileName = messageSourceNotification.getMessage(
//						Constants.EXCEL_STORE_PATH, null, null) + "/" + "CYSDRF_OP.xls";

				// FileInputStream inputStream = new FileInputStream(context.getRealPath(
				// messageSourceNotification.getMessage(Constants.EXCEL_READ_PATH, null, null)));
				String uploadFileName=txnTemplateDetails.get(0).getFilePath();
				FileInputStream inputStream = new FileInputStream(uploadFileName);
				HSSFWorkbook xssfWorkbook = new HSSFWorkbook(inputStream);
				
				int columnNo = 8; //target start column
				
				flag = true;
				lockTargetAchiveColumn(xssfWorkbook, columnNo, flag, frameworkType, frameworkName);//lock target column where flag true
				columnNo++;
					 
				flag = false;
				lockTargetAchiveColumn(xssfWorkbook, columnNo, flag, frameworkType, frameworkName);//unlock achieve column where flag false
				columnNo++;

				// security purpose
				HSSFSheet metaDataSheet = xssfWorkbook
						.getSheet(messageSourceNotification.getMessage(
								Constants.EXCEL_UNIQUE_KEY_SHEET, null, null));

				if (null != metaDataSheet) {

					Row currentTimeRow = metaDataSheet.createRow(59);
					Cell uniqueCell = currentTimeRow.createCell(101);
					uniqueCell.setCellValue(txnTemplateDetails.get(0).gettXNTemplateId()
							+ "_"
							+ templateDetail.getUserDetail().getUserName());
//					xssfWorkbook.setSheetOrder(metaDataSheet.getSheetName(), 1);
//					xssfWorkbook.setSheetOrder("Sheet3", 2);
					
					xssfWorkbook.setSheetHidden(1, 2); // hide Per cent sheet
					xssfWorkbook.setSheetHidden(2, 2); //hide sheet3
					
//					System.out.println(xssfWorkbook.getSheetIndex("Per cent")+"==>>>>HIDDEN STATUS==>>> "+xssfWorkbook.isSheetVeryHidden(1));
//					System.out.println(xssfWorkbook.getSheetIndex("sheet3")+"HIDDEN STATUS==>>> "+xssfWorkbook.isSheetVeryHidden(2));
					
					FileOutputStream outputStream = new FileOutputStream(
							new File(fileName));
					xssfWorkbook.write(outputStream);
					outputStream.close();
					templateDetail.setUpdatedBy(user.getUserName());
					templateDetail.setUpdatedDate(new Timestamp(new Date().getTime()));
					templateDetail.setFilePath(fileName);
					templateDetailRepository.save(templateDetail);
					return fileName;
				}else
					return null;
			}else if(downloadType.equalsIgnoreCase(messageSourceNotification
					.getMessage(Constants.UPLOAD_TYPE_ACHIEVED, null, null))
					&& txnTemplateDetails.size() > 1){
				return txnTemplateDetails.get(1).getFilePath();
			}else
				return null;
			
		}
		
	}
	
	
	//first time with unlocked achieved columns and locked target columns ******** when admin clicks on download uploaded target file to fill achieve
	@Override
	@Transactional("webTransactionManager")
	public String getAdminTargetFileName(String userName, Integer userId,
			Integer timePeriodId, Integer frameworkType, String targetFileName, String frameworkTypeName) throws Exception{
		
		if(frameworkTypeName.equals(Constants.FRAMEWORK_TYPE_OPERATION))
			return targetFileName;
		
		
		UserDetailModel userDetailModel = (UserDetailModel) stateManager.getValue(Constants.USER_PRINCIPAL);
		List<TXNTemplateDetail> txnTemplateDetails = tXNTemplateDetailRepository.
				findByUserIdAndTimePeriodIdAndFrameworkAndIsAlive(userId, timePeriodId, frameworkType);
		
		String fileName= messageSourceNotification.getMessage(Constants.EXCEL_STORE_PATH, null, null)+"/"
				+"CYSDRF_"+txnTemplateDetails.get(0).getTemplateDetail().getUserDetail().getUserName()+"_"
				+txnTemplateDetails.get(0).getTemplateDetail().getTimePeriod()
				+".xls";
		
		//when file already present
		if(txnTemplateDetails.size()>0 && txnTemplateDetails.get(0).getTemplateDetail().getUpdatedBy()!=null &&
						 txnTemplateDetails.get(0).getTemplateDetail().getUpdatedDate()!=null){
				return  txnTemplateDetails.get(0).getTemplateDetail().getFilePath();
		}else {
			
			String uploadFileName=txnTemplateDetails.get(0).getFilePath();
			FileInputStream inputStream = new FileInputStream(uploadFileName);
			HSSFWorkbook xssfWorkbook = new HSSFWorkbook(inputStream);
			int columnNo = 9; //target start column
			
			//set the locking/unlocking style to planned/achieve columns
			for(int projectSize=0 ; projectSize<txnTemplateDetails.get(0).getTemplateDetail().getProjects().split(",").length;projectSize++){
				lockTargetAchiveColumn(xssfWorkbook, columnNo, true, frameworkType, frameworkTypeName);//lock target column where flag true
				columnNo++;
					 
				lockTargetAchiveColumn(xssfWorkbook, columnNo, false, frameworkType, frameworkTypeName);//unlock achieve column where flag false
				columnNo++;
			}
				//security purpose
			HSSFSheet metaDataSheet = xssfWorkbook.getSheet(messageSourceNotification.getMessage(Constants.EXCEL_UNIQUE_KEY_SHEET, null, null)); 
			Row currentTimeRow = metaDataSheet.createRow(59);
			Cell uniqueCell = currentTimeRow.createCell(101);
			uniqueCell.setCellValue(txnTemplateDetails.get(0).gettXNTemplateId()+"_"+userName);
			xssfWorkbook.setSheetHidden(1, 2);//hide Per cent sheet
			FileOutputStream outputStream = new FileOutputStream(
					new File(fileName));
			xssfWorkbook.write(outputStream);
			outputStream.close();
			txnTemplateDetails.get(0).getTemplateDetail().setFilePath(fileName);
			txnTemplateDetails.get(0).getTemplateDetail().setUpdatedBy(userDetailModel.getUserName());
			txnTemplateDetails.get(0).getTemplateDetail().setUpdatedDate(new Timestamp(new Date().getTime()));
			TemplateDetail templateDetail2 = templateDetailRepository.save(txnTemplateDetails.get(0).getTemplateDetail());	
			return templateDetail2.getFilePath();
		}
	}
	
	//lock--unlock ******** target--achieved files
	private void lockTargetAchiveColumn(HSSFWorkbook xssfWorkbook, int columnNo, boolean flag, Integer frameworkTypeId, String frameworkName){
		HSSFCellStyle lockingStyle = xssfWorkbook.createCellStyle();
		HSSFSheet xssfSheet = xssfWorkbook.getSheet(messageSourceNotification.getMessage(Constants.EXCEL_CREATE_PROJECT_SHEET, null, null)); 
		Iterator<Row> rowIterator = xssfSheet.iterator();
		lockingStyle = xssfWorkbook.createCellStyle();
		lockingStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		lockingStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
		lockingStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		lockingStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		lockingStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		lockingStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		lockingStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		lockingStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
		if(flag){
			lockingStyle.setLocked(true);
		}else{
			lockingStyle.setLocked(false);
		}
		
		 while (rowIterator.hasNext()) {
			 Row row = rowIterator.next();
			 if(frameworkName.equalsIgnoreCase(Constants.FRAMEWORK_TYPE_PROGRAMME) && (row.getRowNum()==12 || row.getRowNum()==23 || row.getRowNum()==24 || row.getRowNum()==27 || row.getRowNum()==30 ||
					 row.getRowNum()==33 || row.getRowNum()==35 || row.getRowNum()==36 || row.getRowNum()==42 || 
					 row.getRowNum()==45 || row.getRowNum()==46 || row.getRowNum()==49)){
				 continue;
			 }
			 if(frameworkName.equalsIgnoreCase(Constants.FRAMEWORK_TYPE_OPERATION) && (row.getRowNum()==7 || row.getRowNum()==11 || row.getRowNum()==14 || row.getRowNum()==15 || row.getRowNum()==18 ||
					 row.getRowNum()==23 || row.getRowNum()==24 || row.getRowNum()==31 || row.getRowNum()==35|| 
							 row.getRowNum()==38 || row.getRowNum()==45 || //formula cells
					 row.getRowNum()==47 || row.getRowNum()==48)){
				 continue;
			 }
			 if(frameworkName.equalsIgnoreCase(Constants.FRAMEWORK_TYPE_PROGRAMME) && row.getRowNum()>2){
				 Cell cell = row.getCell(columnNo) == null ? row.createCell(columnNo) : row.getCell(columnNo);
				 cell.setCellStyle(lockingStyle);
				 /*CellRangeAddressList addressList = new CellRangeAddressList(row.getRowNum(), row.getRowNum(), columnNo, columnNo);
				 DVConstraint dvConstraint = DVConstraint.createNumericConstraint(ValidationType.INTEGER, OperatorType.BETWEEN, "0", "10000000000000");
				 DataValidation dataValidation = new HSSFDataValidation
					    (addressList, dvConstraint);
				 dataValidation.setSuppressDropDownArrow(true);
				 xssfSheet.addValidationData(dataValidation);*/
			 }
			 if(frameworkName.equalsIgnoreCase(Constants.FRAMEWORK_TYPE_OPERATION) && row.getRowNum()>3){
				 Cell cell = row.getCell(columnNo) == null ? row.createCell(columnNo) : row.getCell(columnNo);
				 cell.setCellStyle(lockingStyle);
			 }
			 if((frameworkName.equalsIgnoreCase(Constants.FRAMEWORK_TYPE_OPERATION) && row.getRowNum()==52) || 
					 frameworkName.equalsIgnoreCase(Constants.FRAMEWORK_TYPE_PROGRAMME) && row.getRowNum()==53)
				 break;
		 }
	}
	
	//when user uploads the target/achieved file
	@Override
	@Transactional(value="webTransactionManager",rollbackFor=Exception.class)
	public String uploadTargetOrAchiveFile(Integer timpePeriodNid, String ipAddress,
			MultipartFile file, String uploadType, Integer frameworkType, String frameworkName) throws Exception{
		
		//get the uploading file and transfer it to server loacation
		String originalName = file.getOriginalFilename();
		String filePath = messageSourceNotification.getMessage(Constants.EXCEL_UPLOAD_FILEPATH,null,null)+"/" + originalName; //write in properties
		File dest = new File(filePath);
		file.transferTo(dest);
		
		boolean result = false; //success--failure
		boolean isAchieve = false; // all achieved cells have valid value***delete the transfered file from server
		boolean isErr = false; //security unique key
		boolean isTarget = false; //at least one target is given for one indicator
		boolean isTargetAllow = true; //all target cells have valid input
		
		String errorMsg = "";
		UserDetailModel userDetailModel = (UserDetailModel) stateManager.getValue(Constants.USER_PRINCIPAL);
		if(null!=userDetailModel){ //for logged in users only
			TemplateDetail templateDetail = templateDetailRepository.findByTimePeriodIdAndFrameworkTypeIdAndUserIdAndIsActive(timpePeriodNid, frameworkType, 
					userDetailModel.getUserId());
			List<TXNTemplateDetail> txnTemplateDetails  = new ArrayList<>();
			
			if(uploadType.equalsIgnoreCase(messageSourceNotification.getMessage(Constants.UPLOAD_TYPE_ACHIEVED, null, null))){
				txnTemplateDetails = tXNTemplateDetailRepository.
						findByUserIdAndTimePeriodIdAndFrameworkAndIsAlive(userDetailModel.getUserId(), timpePeriodNid, frameworkType);
			}
			String [] projectArrs = templateDetail.getProjects().split(","); // list of projects
			
			//check the uploading file
			FileInputStream inputStream = new FileInputStream(filePath);
			HSSFWorkbook xssfWorkbook = null;
			try{
				xssfWorkbook = new HSSFWorkbook(inputStream);
			}catch(Exception e){
				e.printStackTrace();
				return (errorMsg.equals("") ? "Unsupported file format." : errorMsg)+"_"+result;
			}
			
				//get the sheet which has security unique key
				HSSFSheet metaDataSheet = xssfWorkbook.getSheet(messageSourceNotification.getMessage(Constants.EXCEL_UNIQUE_KEY_SHEET, null, null)); 
				
				//check for security unique key
				if (null != metaDataSheet && null != metaDataSheet.getRow(59)
						&& null != metaDataSheet.getRow(59).getCell(101)
						&& Cell.CELL_TYPE_BLANK!=metaDataSheet.getRow(59).getCell(101).getCellType()
						&& ((uploadType.equalsIgnoreCase(messageSourceNotification.getMessage(Constants.UPLOAD_TYPE_TARGET, null, null))
						&& metaDataSheet.getRow(59).getCell(101).getStringCellValue()
								.equals(templateDetail.getTemplateId() + "_"
										+ templateDetail.getUserDetail().getUserName()))
						|| (uploadType.equalsIgnoreCase(messageSourceNotification.getMessage(Constants.UPLOAD_TYPE_ACHIEVED, null, null))
						&& metaDataSheet.getRow(59).getCell(101).getStringCellValue()
								.equals(txnTemplateDetails.get(0).gettXNTemplateId() + "_"
										+ templateDetail.getUserDetail().getUserName())))){
					isErr = true;
					
					//for target upload check at least one value is given for at least one project
					//*********** //for achieved upload check all the corresponding achieved cell of target is not blank or have invalid value
					
					int projectAchieveColumn = frameworkType == 1 ? //operational aded
							uploadType.equalsIgnoreCase(messageSourceNotification.getMessage(Constants.UPLOAD_TYPE_TARGET, null, null)) ? 9 : 10 :
								uploadType.equalsIgnoreCase(messageSourceNotification.getMessage(Constants.UPLOAD_TYPE_TARGET, null, null)) ? 8 : 9; 
					
					outerLoop: //label to break from outer loop once data persisted
					for( int i=0 ; i<projectArrs.length*2; i+=2){
						Iterator<Row> rowIterator = xssfWorkbook.getSheet(messageSourceNotification.getMessage(Constants.EXCEL_CREATE_PROJECT_SHEET, null, null)).iterator();
						 while (rowIterator.hasNext()) {
							 Row row = rowIterator.next();
							 if(row!=null && ((frameworkName.equalsIgnoreCase(Constants.FRAMEWORK_TYPE_PROGRAMME) && row.getRowNum()>2) 
									 || (frameworkName.equalsIgnoreCase(Constants.FRAMEWORK_TYPE_OPERATION) && row.getRowNum()>3))){// user input row started
								 
								 Cell achieveTargetCell = row.getCell(projectAchieveColumn+i);
								 Cell targetCell = row.getCell((projectAchieveColumn-1)+i);
								 
								 DataFormatter dataFormatter = new DataFormatter();
								 String achieveTargetVal = dataFormatter.formatCellValue(achieveTargetCell);
								
								 boolean programmeIsNumberRow =frameworkName.equalsIgnoreCase(Constants.FRAMEWORK_TYPE_PROGRAMME) 
										 && row.getRowNum() !=17 && row.getRowNum()!=18
										 && row.getRowNum()!=19 && row.getRowNum()!=20 && row.getRowNum()!=26
										 && row.getRowNum()!= 37 && row.getRowNum()!=39 && row.getRowNum()!=41;
								 
								 boolean operationalIsNumberRow =frameworkName.equalsIgnoreCase(Constants.FRAMEWORK_TYPE_OPERATION) 
										 && row.getRowNum() !=36 && row.getRowNum()!=37
										 && row.getRowNum()!=38 && row.getRowNum()!=42 && row.getRowNum()!=43
										 && row.getRowNum()!= 44 && row.getRowNum()!=45 && row.getRowNum()!=46
										 && row.getRowNum()!=50 && row.getRowNum()!=52;
								 
								 
								 boolean operationalIsYesNoRow = frameworkName.equalsIgnoreCase(Constants.FRAMEWORK_TYPE_OPERATION) 
										 && (row.getRowNum() ==17 || row.getRowNum()==25);
								 
								 boolean operationalIsFormulaRow = frameworkName.equalsIgnoreCase(Constants.FRAMEWORK_TYPE_OPERATION) 
										 && (row.getRowNum() ==38 || row.getRowNum()==45);
								 
								 CellReference targetReference = new CellReference(targetCell);
								 CellReference achieveReference = new CellReference(achieveTargetCell);
								
								 if(uploadType.equalsIgnoreCase(messageSourceNotification.getMessage(Constants.UPLOAD_TYPE_ACHIEVED, null, null)) ){
									 //reject if target cell has value but achieve has no value or non numeric value or date value
									 //reject if achieve cell has value without corresponding target cell
									 
									 boolean isAchieveBlank = null!=targetCell && targetCell.getCellType()!= Cell.CELL_TYPE_BLANK 
													&&(null==achieveTargetCell
													|| (!operationalIsYesNoRow && !operationalIsFormulaRow && achieveTargetCell.getCellType()!= Cell.CELL_TYPE_NUMERIC )
													|| (!operationalIsYesNoRow && !operationalIsFormulaRow && achieveTargetCell.getCellType()== Cell.CELL_TYPE_BLANK )
													|| (!operationalIsYesNoRow && !operationalIsFormulaRow && DateUtil.isCellDateFormatted(achieveTargetCell))
													|| (!operationalIsYesNoRow && !operationalIsFormulaRow && programmeIsNumberRow && 
													(achieveTargetVal.split("\\.").length > 1 || achieveTargetVal.indexOf("%")!=-1))
													|| (!operationalIsYesNoRow && !operationalIsFormulaRow && operationalIsNumberRow && 
													(achieveTargetVal.split("\\.").length > 1 || achieveTargetVal.indexOf("%")!=-1))
													|| (!operationalIsFormulaRow && operationalIsYesNoRow && 
													(!(achieveTargetVal.equals("Yes") || achieveTargetVal.equals("No"))))
													|| (!operationalIsYesNoRow && !operationalIsFormulaRow &&  achieveTargetVal.indexOf("-")!=-1));
									 
									boolean isTargetBlank = (null==targetCell || targetCell.getCellType()== Cell.CELL_TYPE_BLANK )
													 &&(null!=achieveTargetCell && achieveTargetCell.getCellType()!= Cell.CELL_TYPE_BLANK);
									 
									if((isAchieveBlank) || (isTargetBlank)){
										 isAchieve = true;
										 
										 errorMsg = (isAchieveBlank ? "Achieved cell "+achieveReference.toString().split("CellReference")[1]+
												 " cannot be blank / have invalid value !"
												 : "Target cell "+targetReference.toString().split("CellReference")[1]+
												 " is blank hence the achieved cell "
												 + achieveReference.toString().split("CellReference")[1]+
												 " is not a valid entry.");//"Fill all the target corresponding achieve cells; no achieve can be filled without target.";
										 break outerLoop;
									}
								 }
								 if(isTarget){ //once we know at least one target is given check for valid value
									 if(null!=achieveTargetCell 
											 && (!operationalIsFormulaRow && achieveTargetCell.getCellType()!= Cell.CELL_TYPE_BLANK 
											 && (!operationalIsYesNoRow && 
													 !operationalIsFormulaRow && achieveTargetCell.getCellType()!= Cell.CELL_TYPE_NUMERIC
											 || (!operationalIsYesNoRow &&
													 !operationalIsFormulaRow && DateUtil.isCellDateFormatted(achieveTargetCell))
											 || (!operationalIsYesNoRow && !operationalIsFormulaRow && programmeIsNumberRow && 
											 (achieveTargetVal.split("\\.").length > 1 || achieveTargetVal.indexOf("%")!=-1))
											 || (!operationalIsYesNoRow &&
													 !operationalIsFormulaRow && operationalIsNumberRow && 
											 (achieveTargetVal.split("\\.").length > 1 || achieveTargetVal.indexOf("%")!=-1))
											 || (!operationalIsFormulaRow && operationalIsYesNoRow && 
											 (!(achieveTargetVal.equals("Yes") || achieveTargetVal.equals("No"))))
											 || (!operationalIsFormulaRow && achieveTargetVal.indexOf("-")!=-1)))){
										 isTargetAllow = false;
										 errorMsg = "Provide numeric value only!! Target cell "+achieveReference.toString().split("CellReference")[1]
												 +" is not a valid entry.";
										 break outerLoop;
									 }
								 }
								 //fill at least one indicator target cell
								 else if(uploadType.equalsIgnoreCase(messageSourceNotification.getMessage(Constants.UPLOAD_TYPE_TARGET, null, null)) ){
									 isAchieve = true;
									 if(null!=achieveTargetCell && !operationalIsFormulaRow &&
											 !operationalIsFormulaRow && achieveTargetCell.getCellType()!= Cell.CELL_TYPE_BLANK){
										 
										 isTarget = true;
										 
										 if(!operationalIsFormulaRow &&
												 (operationalIsYesNoRow && 
														 (!(achieveTargetVal.equals("Yes") || achieveTargetVal.equals("No")))) &&
														 	achieveTargetCell.getCellType()== Cell.CELL_TYPE_NUMERIC && 
												 DateUtil.isCellDateFormatted(achieveTargetCell)){ //check date cell type
											 isTargetAllow = false;
											 errorMsg = "Provide numeric value only!! Target cell "+achieveReference.toString().split("CellReference")[1]
													 +" is not a valid entry.";
											 break outerLoop;
										 	}
												 
									if (!operationalIsYesNoRow && 
											!operationalIsFormulaRow && achieveTargetCell.getCellType() != Cell.CELL_TYPE_NUMERIC
											|| (!operationalIsYesNoRow && !operationalIsFormulaRow && programmeIsNumberRow && (achieveTargetVal
													.split("\\.").length > 1 || achieveTargetVal
													.indexOf("%") != -1))
											|| (!operationalIsYesNoRow && !operationalIsFormulaRow && operationalIsNumberRow && (achieveTargetVal
													.split("\\.").length > 1 || achieveTargetVal
													.indexOf("%") != -1))
											|| (!operationalIsFormulaRow && operationalIsYesNoRow && 
													(!(achieveTargetVal.equals("Yes") || achieveTargetVal.equals("No"))))
											|| (!operationalIsYesNoRow && !operationalIsFormulaRow && achieveTargetVal.indexOf("-") != -1)){
												 
												 isTargetAllow = false;
												 errorMsg = "Provide numeric value only!! Target cell "+achieveReference.toString().split("CellReference")[1]
														 +" is not a valid entry.";
												 break outerLoop;
											}
									 }
								 }
							 }
							 if(row!=null && (row.getRowNum()==53 && frameworkName.equalsIgnoreCase(Constants.FRAMEWORK_TYPE_PROGRAMME) ) 
									 || (row.getRowNum()==52 && frameworkName.equalsIgnoreCase(Constants.FRAMEWORK_TYPE_OPERATION))) //last row
								 break;
						 }
						
					}
				}
				if(frameworkName.equalsIgnoreCase(Constants.FRAMEWORK_TYPE_PROGRAMME)  && uploadType.equalsIgnoreCase(messageSourceNotification.getMessage(Constants.UPLOAD_TYPE_TARGET, null, null)) 
						&& isTargetAllow && isTarget){
					
					//*******TARGET******AGGREGATE AND PERSIST IN UT DATA AND SAVE IN TXN -------- PROGRAMME********
					aggregationService.doAggregate(templateDetail.getProjects(),templateDetail
								.getAreaCode(),templateDetail.getTimePeriodId(),filePath, true);
					 
					saveTXNTemplateDetail(userDetailModel, filePath, uploadType, templateDetail);
					result = true;
				}
				if(frameworkName.equalsIgnoreCase(Constants.FRAMEWORK_TYPE_OPERATION) && uploadType.equalsIgnoreCase(messageSourceNotification.getMessage(Constants.UPLOAD_TYPE_TARGET, null, null)) 
						&& isTargetAllow && isTarget){
					//*************TARGET====AGGREGATE AND PERSIST IN UT DATA AND SAVE IN TXN ----OPERATIONAL********
					
					aggregationService.oprationalAggregate(39,"IND021", templateDetail.getTimePeriodId(), filePath, true); //ONLY FOR ODISHA CYSD HO
					 
					saveTXNTemplateDetail(userDetailModel, filePath, uploadType, templateDetail);
					result = true;
					
				}else if(isTargetAllow && isErr == true && uploadType.equalsIgnoreCase(messageSourceNotification.getMessage(Constants.UPLOAD_TYPE_TARGET, null, null))
						&& result==false)
					errorMsg = "Provide at least one indicator's target!!";
				
				if (isAchieve == true && result==false){
					new File(filePath).delete();
					
				}else if(frameworkName.equalsIgnoreCase(Constants.FRAMEWORK_TYPE_PROGRAMME) && uploadType.equalsIgnoreCase(messageSourceNotification.getMessage(Constants.UPLOAD_TYPE_ACHIEVED, null, null))  
						&& isAchieve == false && isErr == true){
					
					//*************ACHIEVED====SAVE IN TXN ONLY----PROGRAMME********
					saveTXNTemplateDetail(userDetailModel, filePath, uploadType, templateDetail);
					result = true;
					
				}else if(frameworkName.equalsIgnoreCase(Constants.FRAMEWORK_TYPE_OPERATION) && uploadType.equalsIgnoreCase(messageSourceNotification.getMessage(Constants.UPLOAD_TYPE_ACHIEVED, null, null)) 
						&& isAchieve == false && isErr == true){
					
					//*************ACHIEVED====AGGREGATE AND PERSIST IN UT DATA AND SAVE IN TXN ----OPERATIONAL********
					aggregationService.oprationalAggregate(39, "IND021", templateDetail.getTimePeriodId(), filePath, false);
					 
					saveTXNTemplateDetail(userDetailModel, filePath, uploadType, templateDetail);
					result = true;
				}
			}
			//result --->true for successful transaction and persistence ********
		return (errorMsg.equals("") ? "Wrong File Uploaded." : errorMsg)+"_"+result;
	}
	
	
	// save txn details
	private void saveTXNTemplateDetail(UserDetailModel userDetailModel, String filePath, String uploadType, TemplateDetail templateDetail){
		TXNTemplateDetail txnTemplateDetail = new TXNTemplateDetail();
		txnTemplateDetail.setCreatedBy(userDetailModel.getUserName());
		txnTemplateDetail.setCreatedDate(new Timestamp(new Date().getTime()));
		txnTemplateDetail.setFilePath(filePath);
		txnTemplateDetail.setTemplateDetail(templateDetail);
		txnTemplateDetail.setUploadType(uploadType);
		txnTemplateDetail.setUserDetail(new UserDetail(userDetailModel.getUserId()));
		txnTemplateDetail.setAreaCode(templateDetail.getAreaCode());
		txnTemplateDetail.setStatus(Constants.TEMPLATE_SUBMISSION_STATUS);
		txnTemplateDetail.setAlive(true);
		
		tXNTemplateDetailRepository.save(txnTemplateDetail);
	}
	
	
	//All framework types
	@Override
	public List<ValueObject> getAllFrameworks(){
		List<ValueObject> valueObjects = new ArrayList<>();
		List<FrameworkType> frameworkTypes = frameworkTypeRepository.findAll();
		UserDetailModel userDetailModel = (UserDetailModel) stateManager.getValue(Constants.USER_PRINCIPAL);
		Integer roleId= userDetailModel.getUserRoleFeaturePermissionMappings().get(0)
				.getRoleFeaturePermissionSchemeModel().getRole()
				.getRoleId();
		for (FrameworkType frameworkType : frameworkTypes) {
			if (((roleId != 4 && frameworkType.getFrameworkName()
					.equalsIgnoreCase(Constants.FRAMEWORK_TYPE_OPERATION)))
					|| (roleId == 4 && frameworkType
							.getFrameworkName().equalsIgnoreCase(
									Constants.FRAMEWORK_TYPE_PROGRAMME))) {
				continue;
			}
			ValueObject valueObject = new ValueObject(
					Integer.toString(frameworkType.getFrameworkTypeId()),
					frameworkType.getFrameworkName());
			valueObjects.add(valueObject);
		}
		return valueObjects;
	}
	
	
	
	//transaction page -- user specific
	@Override
	@Transactional("webTransactionManager")
	public List<TemplateTXNModel> getUserSpecificTXNTemplates(){
		UserDetailModel userDetailModel = (UserDetailModel) stateManager.getValue(Constants.USER_PRINCIPAL);
		List<TXNTemplateDetail> txnTemplateDetails = tXNTemplateDetailRepository.findByUserIdAndIsAlive(userDetailModel.getUserId());
		if(null!=userDetailModel && txnTemplateDetails.size()!=0){
			return getTXNList(txnTemplateDetails, userDetailModel, txnTemplateDetails.get(0).getTemplateDetail().getTimePeriodId());
		}else{
			return new ArrayList<>();
		}
	}
	
	//admin transaction page
	@Override
	public List<TemplateTXNModel> getAllUserTxnTemplates(String timePeriodId){
		UserDetailModel userDetailModel = (UserDetailModel) stateManager.getValue(Constants.USER_PRINCIPAL);
		if(null!=userDetailModel){
			List<TXNTemplateDetail> txnTemplateDetails = tXNTemplateDetailRepository.
					findByTimeperiodIdAndIsAlive(userDetailModel.getUserId(), Integer.parseInt(timePeriodId));
			return getTXNList(txnTemplateDetails, userDetailModel, Integer.parseInt(timePeriodId));
		}else{
			return new ArrayList<>();
		}
	}
	
	
	@Transactional("webTransactionManager")
	private List<TemplateTXNModel> getTXNList(List<TXNTemplateDetail> txnTemplateDetails, UserDetailModel userDetailModel, Integer timePeriodId){
		List<TemplateTXNModel> templateTXNModels = new ArrayList<>();
		Map<String, List<TXNTemplateDetail>> timePeriodUserTXNTempMap = new HashMap<>();
		Map<String,String> areaMap = getAreaListDetails();
		
		UtTimeperiod timeperiod = timeperiodRepository.findByTimePeriodNId(timePeriodId);
		
		LocalDateTime startDate1 = LocalDateTime.parse(sdf1.format(timeperiod.getStartDate()).toString(), dateTimeFormatter);
		LocalDateTime endDate1 = LocalDateTime.parse(sdf1.format(timeperiod.getEndDate()).toString(), dateTimeFormatter);
		LocalDateTime todayDate = LocalDateTime.now();
		
		String achieved = (((todayDate.getMonthValue() <= endDate1.getMonthValue() + 1 && todayDate.getDayOfMonth() <= 16 )
				|| (todayDate.getMonthValue() <= endDate1.getMonthValue()))
						&& ((todayDate.isAfter(startDate1) || todayDate.isEqual(startDate1)))
						&& todayDate.getYear()>=startDate1.getYear() && todayDate.getYear()<=endDate1.getYear()) ? Constants.DISABLE_NONE
										: Constants.DISABLE_DISABLED;
		
		for (TXNTemplateDetail txnTemplateDetail : txnTemplateDetails) {
			if (timePeriodUserTXNTempMap.containsKey(txnTemplateDetail.getTemplateDetail().getTimePeriodId() + "_"
					+ txnTemplateDetail.getTemplateDetail().getUserDetail().getUserDetailId()+ "_"
							+txnTemplateDetail.getTemplateDetail().getFrameworkType().getFrameworkName())) {
				timePeriodUserTXNTempMap
						.get(txnTemplateDetail.getTemplateDetail().getTimePeriodId() + "_"
								+ txnTemplateDetail.getTemplateDetail().getUserDetail().getUserDetailId() + "_"
								+txnTemplateDetail.getTemplateDetail().getFrameworkType().getFrameworkName())
						.add(txnTemplateDetail);
			} else {
				List<TXNTemplateDetail> templateDetails = new ArrayList<>();
				templateDetails.add(txnTemplateDetail);
				timePeriodUserTXNTempMap.put(
						txnTemplateDetail.getTemplateDetail().getTimePeriodId() + "_"
								+ txnTemplateDetail.getTemplateDetail().getUserDetail().getUserDetailId()+ "_"
										+txnTemplateDetail.getTemplateDetail().getFrameworkType().getFrameworkName(),
						templateDetails);
			}
		}
		for(String timePeriodUserIdKey: timePeriodUserTXNTempMap.keySet()){
			int size = timePeriodUserTXNTempMap.get(timePeriodUserIdKey).size();
			
			String areaCode = timePeriodUserTXNTempMap.get(timePeriodUserIdKey).get(0).getTemplateDetail().getAreaCode();
			
			TemplateTXNModel templateTXNModel = new TemplateTXNModel();
			templateTXNModel.setUserId(timePeriodUserTXNTempMap.get(timePeriodUserIdKey).get(0).getUserDetail().getUserDetailId());
			templateTXNModel.setUserName(timePeriodUserTXNTempMap.get(timePeriodUserIdKey).get(0).getUserDetail().getUserName());
			templateTXNModel.setAreaCode(areaCode);
			templateTXNModel.setFrameworkType(timePeriodUserTXNTempMap.get(
					timePeriodUserIdKey).get(0).getTemplateDetail().getFrameworkType().getFrameworkName());
			templateTXNModel.setTargetStatus(null!=timePeriodUserTXNTempMap.get(timePeriodUserIdKey).get(0)
					&& timePeriodUserTXNTempMap.get(timePeriodUserIdKey).size()>0 ? 
					timePeriodUserTXNTempMap.get(timePeriodUserIdKey).get(0).getStatus() : Constants.TEMPLATE_NOT_SUBMITTED_STATUS);
			templateTXNModel.setAchieveStatus(timePeriodUserTXNTempMap.get(timePeriodUserIdKey).size()>1 ?
					timePeriodUserTXNTempMap.get(timePeriodUserIdKey).get(1).getStatus() : Constants.TEMPLATE_NOT_SUBMITTED_STATUS);
			templateTXNModel.setTimePeriod(timePeriodUserTXNTempMap.get(timePeriodUserIdKey).get(0).getTemplateDetail().getTimePeriod());
			
			templateTXNModel.setTargetFileName(null!=timePeriodUserTXNTempMap.get(timePeriodUserIdKey).get(0) ?
					timePeriodUserTXNTempMap.get(timePeriodUserIdKey).get(0).getFilePath() : "");
			
			templateTXNModel.setAchieveFileName(timePeriodUserTXNTempMap.get(timePeriodUserIdKey).size()>1 ?
					timePeriodUserTXNTempMap.get(timePeriodUserIdKey).get(1).getFilePath() : "");
//			templateTXNModel.setAreaName(areaCode.equals("") ? timePeriodUserTXNTempMap.get(timePeriodUserIdKey).get(0).getCreatedBy().split("_")[1]
//					  : areaCode.equals("IND021") ? "Odisha" : areaMap.get(areaCode));
			templateTXNModel.setAreaName(areaMap.get(areaCode));
			templateTXNModel.setTxnTemplateId(timePeriodUserTXNTempMap.get(timePeriodUserIdKey).get(size-1).gettXNTemplateId());
			templateTXNModel.setTimePeriodId(timePeriodUserTXNTempMap.get(timePeriodUserIdKey).get(0).getTemplateDetail().getTimePeriodId());
			templateTXNModel.setFrameworkId(timePeriodUserTXNTempMap.get(
					timePeriodUserIdKey).get(0).getTemplateDetail().getFrameworkType().getFrameworkTypeId());
			templateTXNModel.setTargetLastStatusDate(null!=timePeriodUserTXNTempMap.get(timePeriodUserIdKey).get(0)
					&& timePeriodUserTXNTempMap.get(timePeriodUserIdKey).size()>0 ? 
					sdf.format(timePeriodUserTXNTempMap.get(timePeriodUserIdKey).get(0).getCreatedDate()) : "");
			templateTXNModel.setAchieveLastStatusDate(timePeriodUserTXNTempMap.get(timePeriodUserIdKey).size()>1 ?
					sdf.format(timePeriodUserTXNTempMap.get(timePeriodUserIdKey).get(1).getCreatedDate()) : "");
			templateTXNModel.setIsTargetAvailable(templateTXNModel.getTargetFileName()=="" ?  Constants.DISABLE_DISABLED : "");
			templateTXNModel.setIsAchieveAvailable(templateTXNModel.getAchieveFileName()=="" ? Constants.DISABLE_DISABLED : "");
			
			templateTXNModel.setDisabled(timePeriodUserTXNTempMap.get(
					timePeriodUserIdKey).size() > 1
					&& timePeriodUserTXNTempMap
							.get(timePeriodUserIdKey)
							.get(1)
							.getTemplateDetail()
							.getFrameworkType()
							.getFrameworkName()
							.equalsIgnoreCase(
									Constants.FRAMEWORK_TYPE_PROGRAMME)
					&& timePeriodUserTXNTempMap
							.get(timePeriodUserIdKey)
							.get(1)
							.getStatus()
							.equalsIgnoreCase(
									Constants.TEMPLATE_SUBMISSION_STATUS) ? ""
					: Constants.DISABLE_DISABLED);
			
			templateTXNModel
					.setIsEnableChooseFile(achieved
							.equalsIgnoreCase(Constants.DISABLE_DISABLED) ? Constants.DISABLE_DISABLED
							: achieved.equalsIgnoreCase(Constants.DISABLE_NONE)
									&& timePeriodUserTXNTempMap
											.get(timePeriodUserIdKey)
											.get(0)
											.getTemplateDetail()
											.getFrameworkType()
											.getFrameworkName()
											.equalsIgnoreCase(
													Constants.FRAMEWORK_TYPE_PROGRAMME)
									&& timePeriodUserTXNTempMap.get(
											timePeriodUserIdKey).size() > 0
									&& !(timePeriodUserTXNTempMap
											.get(timePeriodUserIdKey).get(size-1).getStatus().equalsIgnoreCase(Constants.TEMPLATE_APPROVAL_STATUS)) ?
							"": Constants.DISABLE_DISABLED);
			templateTXNModels.add(templateTXNModel);
		}
		return templateTXNModels;
	}
	
	//get areaId and Name from a list of area
	private Map<String,String> getAreaListDetails(){
		
		List<UtAreaEn> areaDetails= areaRepository.findAll();
		
		Map<String,String> areaMap = new HashMap<>(areaDetails.size());
		
		for(UtAreaEn area : areaDetails){
			areaMap.put(area.getArea_ID(), area.getArea_Name());
		}
	
		
		return areaMap;
	}

	//superAdmin ****** project management page ****** create new project
	@Override
	public void saveIndicatorClassification(Integer icParentNId, String icGid, String icName,
			Byte icGlobal, String icType, Integer icOrder) {
		UtIndicatorClassificationsEn utIndicatorClassification = new UtIndicatorClassificationsEn();
		utIndicatorClassification.setIC_Parent_NId(icParentNId);
		utIndicatorClassification.setIC_GId(icGid);
		utIndicatorClassification.setIC_Name(icName);
		utIndicatorClassification.setIC_Global(icGlobal);
		utIndicatorClassification.setIC_Type(icType.equalsIgnoreCase("state")? Constants.STATE_PROJECT: Constants.DISTRICT_PROJECT);
//		utIndicatorClassification.setIC_Order(icOrder);
		indicatorClassificationsRepository.save(utIndicatorClassification);
	}

	//set active class for workspace ********* for every functionality UI 
	@Override
	public IsActiveModel setIsActiveClass(Integer timePeriodId, Integer frameworkTypeId, String frameworkTypeName){
		UserDetailModel userDetailModel = (UserDetailModel) stateManager.getValue(Constants.USER_PRINCIPAL);
		IsActiveModel isActiveModel = null;
		if(null!=userDetailModel){
			isActiveModel = new IsActiveModel();
			TemplateDetail templateDetail =templateDetailRepository.
					findByTimePeriodIdAndFrameworkTypeIdAndUserIdAndIsActive(timePeriodId, frameworkTypeId, userDetailModel.getUserId());
			
			//upload target button will remain active till 15th of starting quarter month
			//upload achieve button will remain active till 15th of post quarter 
			
			UtTimeperiod timeperiod = timeperiodRepository.findByTimePeriodNId(timePeriodId);
			
			LocalDateTime startDate1 = LocalDateTime.parse(sdf1.format(timeperiod.getStartDate()).toString(), dateTimeFormatter);
			LocalDateTime endDate1 = LocalDateTime.parse(sdf1.format(timeperiod.getEndDate()).toString(), dateTimeFormatter);
			LocalDateTime todayDate = LocalDateTime.now();
			
			String target = (todayDate.getDayOfMonth() <= 16 && todayDate.getMonthValue() == startDate1.getMonthValue()
				&& todayDate.getYear()<=startDate1.getYear()) ?  Constants.DISABLE_NONE :  Constants.DISABLE_DISABLED;
			
			String achieved = (((todayDate.getMonthValue() <= endDate1.getMonthValue() + 1 && todayDate.getDayOfMonth() <= 16 )
					|| (todayDate.getMonthValue() <= endDate1.getMonthValue()))
							&& ((todayDate.isAfter(startDate1) || todayDate.isEqual(startDate1)))
							&& todayDate.getYear()>=startDate1.getYear()) ? Constants.DISABLE_NONE
											:  Constants.DISABLE_DISABLED;
			
			
			List<TXNTemplateDetail> txnTemplateDetails = tXNTemplateDetailRepository.
					findByUserIdAndTimePeriodIdAndFrameworkAndIsAlive(userDetailModel.getUserId(), timePeriodId, frameworkTypeId);
			isActiveModel.setSaveActive(txnTemplateDetails.size()>0 ? false : true);
			if(frameworkTypeName.equalsIgnoreCase(Constants.FRAMEWORK_TYPE_OPERATION))
				isActiveModel.setDownloadActive(true);
			else
				isActiveModel.setDownloadActive(templateDetail!=null ? true : false);
			isActiveModel.setProjectActive(txnTemplateDetails.size()>0 ? false : true);
			isActiveModel.setAchieveUploadActive(achieved.equalsIgnoreCase(Constants.DISABLE_DISABLED) ? false :
				achieved.equalsIgnoreCase(Constants.DISABLE_NONE) && txnTemplateDetails.size()!=2 && templateDetail!=null && templateDetail.getFilePath()!=null ? true : false);
			isActiveModel.setTargetUploadActive(target.equalsIgnoreCase(Constants.DISABLE_DISABLED) ? false : txnTemplateDetails.size()>=1 ?
					false : templateDetail!=null && templateDetail.getFilePath()!=null ? true : false);
//			isActiveModel.setAchieveUploadActive(txnTemplateDetails.size()!=2 && templateDetail!=null && templateDetail.getFilePath()!=null ? true : false);
//			isActiveModel.setTargetUploadActive(txnTemplateDetails.size()>=1 ? false : templateDetail!=null && templateDetail.getFilePath()!=null ? true : false);
			isActiveModel.setTargetActive(true);
			isActiveModel.setAchieveDownload(txnTemplateDetails.size()>1 ? true : false );
			isActiveModel.setAchieveActive(txnTemplateDetails.size()>0 ? true : false);
		}
		return isActiveModel;
	}

	
	@Override
	public List<ValueObject> getCurrentTimeperiod() {
		List<UtTimeperiod> getAllTimeperiods = timeperiodRepository.findMaxTimePeriods();
		List<ValueObject> valueObjects = new ArrayList<>();
		for (UtTimeperiod utTimeperiod : getAllTimeperiods) {
			ValueObject currentTimeperiod = new ValueObject();
			currentTimeperiod.setKey(Integer.toString(utTimeperiod
					.getTimePeriod_NId()));
			currentTimeperiod.setValue(utTimeperiod.getTimePeriod());
			valueObjects.add(currentTimeperiod);
		}
		return valueObjects;
	}
	
	//save achieved file by admin
	// ********* constraints same as user workspace achieved file upload
	@Override
	@Transactional("webTransactionManager")
	public String saveAchieveByAdmin(Integer userId, Integer txnTemplateId, 
			MultipartFile file, Integer timePeriodId, Integer frameworkId) throws Exception{
		UserDetailModel userDetailModel = (UserDetailModel) stateManager.getValue(Constants.USER_PRINCIPAL);
		TXNTemplateDetail txnTemplate = tXNTemplateDetailRepository.findByTXNTemplateId(txnTemplateId);
		boolean result = false;
		String errorMsg = "";
		if(!txnTemplate.getStatus().equals(Constants.TEMPLATE_APPROVAL_STATUS)){
			 if(null!=userDetailModel){
				 	String originalName = file.getOriginalFilename();
					String filePath = messageSourceNotification.getMessage(Constants.EXCEL_UPLOAD_FILEPATH,null,null)+"/" + originalName; //write in properties
					File dest = new File(filePath);
					file.transferTo(dest);
					
					List<TXNTemplateDetail> txnTemplateDetails = tXNTemplateDetailRepository.
							findByUserIdAndTimePeriodIdAndFrameworkAndIsAlive(userId, timePeriodId, frameworkId);
					
					//check the uploading file
					FileInputStream inputStream = new FileInputStream(filePath);
					HSSFWorkbook xssfWorkbook = null;
					try{
						xssfWorkbook = new HSSFWorkbook(inputStream);
					}catch(Exception e){
						e.printStackTrace();
						return (errorMsg.equals("") ? "Wrong file or unsupported file format." : errorMsg)+"_"+result;
					}
					HSSFSheet metaDataSheet = xssfWorkbook.getSheet(messageSourceNotification.getMessage(Constants.EXCEL_UNIQUE_KEY_SHEET, null, null)); 
					
					boolean isAchieve = false;
					String [] projectArrs = txnTemplateDetails.get(0).getTemplateDetail().getProjects().split(","); // list of projects
				if (null != metaDataSheet && null != metaDataSheet.getRow(59)
						&& null != metaDataSheet.getRow(59).getCell(101) 
						&& Cell.CELL_TYPE_BLANK!=metaDataSheet.getRow(59).getCell(101).getCellType()
						&&((metaDataSheet.getRow(59).getCell(101).getStringCellValue()
						.equals(txnTemplateDetails.get(0).gettXNTemplateId() + "_"
								+ txnTemplateDetails.get(0).getTemplateDetail().getUserDetail().getUserName())))){
					
					outerLoop: //label to break from outer loop once data persisted
					for( int i=0 ; i<projectArrs.length*2; i+=2){
						Iterator<Row> rowIterator = xssfWorkbook.getSheet(messageSourceNotification.getMessage(Constants.EXCEL_CREATE_PROJECT_SHEET, null, null)).iterator();
					while (rowIterator.hasNext()) {
						 Row row = rowIterator.next();
						 if(row.getRowNum()>2){
							 Cell achieveCell = row.getCell(10+i);
							 //reject if target cell has value but achieve has no value
							 //reject if achieve cell has value without corresponding target cell
							 Cell targetCell = row.getCell(9+i);
							 DataFormatter dataFormatter = new DataFormatter();
							 String achieveTargetVal = dataFormatter.formatCellValue(achieveCell);
							 boolean isNumberRow = row.getRowNum() !=17 && row.getRowNum()!=18
									 && row.getRowNum()!=19 && row.getRowNum()!=20 && row.getRowNum()!=26
									 && row.getRowNum()!= 37 && row.getRowNum()!=39 && row.getRowNum()!=41;
							 
							 boolean isAchieveBlank = null!=targetCell && targetCell.getCellType()!= Cell.CELL_TYPE_BLANK 
									 &&(null==achieveCell 
									 || achieveCell.getCellType()!= Cell.CELL_TYPE_NUMERIC
									 || achieveCell.getCellType()== Cell.CELL_TYPE_BLANK
									 || DateUtil.isCellDateFormatted(achieveCell)
									 || (isNumberRow && (achieveTargetVal.split("\\.").length > 1 || achieveTargetVal.indexOf("%")!=-1))
									 || achieveTargetVal.indexOf("-")!=-1);
							 
							 boolean isTargetBlank = ( null==targetCell || targetCell.getCellType()== Cell.CELL_TYPE_BLANK )
									 	&&(null!=achieveCell && achieveCell.getCellType()!= Cell.CELL_TYPE_BLANK);
							 if(isAchieveBlank || isTargetBlank){
								 isAchieve = true;
								 CellReference targetReference = new CellReference(targetCell);
								 CellReference achieveReference = new CellReference(achieveCell);
								 errorMsg = (isAchieveBlank ? "Achieved cell "+achieveReference.toString().split("CellReference")[1]+
										 " cannot be blank / have invalid value!"
										 : "Target cell "+targetReference.toString().split("CellReference")[1]+
										 " is blank hence the achieved cell "
										 + achieveReference.toString().split("CellReference")[1]+
										 " is not a valid entry.");//"Fill all the target corresponding achieve cells; no achieve can be filled without target.";
								 break outerLoop;
							 }
						 	}
						}
					}
					
				if(isAchieve==false){
					if(txnTemplateDetails.size() > 1){
						txnTemplateDetails.get(txnTemplateDetails.size()-1).setAlive(false);
						tXNTemplateDetailRepository.save(txnTemplateDetails.get(txnTemplateDetails.size()-1));
					}
					
					TXNTemplateDetail txnTemplateDetail = new TXNTemplateDetail();
					txnTemplateDetail.setAlive(true);
					txnTemplateDetail.setCreatedBy(userDetailModel.getUserName());
					txnTemplateDetail.setCreatedDate(new Timestamp(new Date().getTime()));
					txnTemplateDetail.setFilePath(filePath);
					txnTemplateDetail.setStatus(Constants.TEMPLATE_APPROVAL_STATUS);
					txnTemplateDetail.setTemplateDetail(txnTemplateDetails.get(0).getTemplateDetail());
					txnTemplateDetail.setUploadType(messageSourceNotification.getMessage(Constants.UPLOAD_TYPE_ACHIEVED, null, null));
					txnTemplateDetail.setUserDetail(new UserDetail(userDetailModel.getUserId()));
					txnTemplateDetail.setAreaCode(txnTemplateDetails.get(0).getTemplateDetail().getAreaCode());
					TXNTemplateDetail txnTemplateDetail2=tXNTemplateDetailRepository.save(txnTemplateDetail);
					
					aggregationService.doAggregate(txnTemplateDetail2.getTemplateDetail().getProjects(),
							txnTemplateDetail.getTemplateDetail().getAreaCode(),
							txnTemplateDetail2.getTemplateDetail().getTimePeriodId(),
							txnTemplateDetail2.getFilePath(), false);
					
					result = true;
				}
				
			   }
				if(result==false)
					new File(filePath).delete();
			 }
		}else
			errorMsg = "File Upload failed!!";
		
		 return (errorMsg.equals("") ? "Wrong File Uploaded." : errorMsg)+"_"+result;
	}
	
	//approve achieved file
	@Override
	@Transactional(value="webTransactionManager",rollbackFor=Exception.class)
	public boolean approveAchieveFileByAdmin(Integer userId, Integer txnTemplateId, Integer timePeriodId, Integer frameworkId) throws Exception{
		UserDetailModel userDetailModel = (UserDetailModel) stateManager.getValue(Constants.USER_PRINCIPAL);
		boolean result = false;
		if(null!=userDetailModel){
			TXNTemplateDetail txnTemplateDetail = tXNTemplateDetailRepository.findByTXNTemplateId(txnTemplateId);
			if(!txnTemplateDetail.getStatus().equals(Constants.TEMPLATE_APPROVAL_STATUS)){ //if not already approved
				txnTemplateDetail.setAlive(false);
				tXNTemplateDetailRepository.save(txnTemplateDetail);
				
				//*************aggregate and persist in ut data********
				
				aggregationService.doAggregate(txnTemplateDetail.getTemplateDetail().getProjects(),txnTemplateDetail.getTemplateDetail()
						.getAreaCode(),txnTemplateDetail.getTemplateDetail().getTimePeriodId(),txnTemplateDetail.getFilePath(), false);
				
				TXNTemplateDetail txnTemplateDetail2 = new TXNTemplateDetail();
				txnTemplateDetail2.setAlive(true);
				txnTemplateDetail2.setCreatedBy(userDetailModel.getUserName());
				txnTemplateDetail2.setCreatedDate(new Timestamp(new Date().getTime()));
				txnTemplateDetail2.setFilePath(txnTemplateDetail.getFilePath());
				txnTemplateDetail2.setStatus(Constants.TEMPLATE_APPROVAL_STATUS);
				txnTemplateDetail2.setTemplateDetail(txnTemplateDetail.getTemplateDetail());
				txnTemplateDetail2.setUploadType(messageSourceNotification.getMessage(Constants.UPLOAD_TYPE_ACHIEVED, null, null));
				txnTemplateDetail2.setUserDetail(new UserDetail(userDetailModel.getUserId()));
				txnTemplateDetail2.setAreaCode(txnTemplateDetail.getTemplateDetail().getAreaCode());
				tXNTemplateDetailRepository.save(txnTemplateDetail2);
				result = true;
			}
		}
		return result;
	}
	
	//All framework types
	@Override
	public List<ValueObject> getAllFrameworksForFactsheet(){
		List<ValueObject> valueObjects = new ArrayList<>();
		List<FrameworkType> frameworkTypes = frameworkTypeRepository.findAll();
			for (FrameworkType frameworkType : frameworkTypes) {
				ValueObject valueObject = new ValueObject(
						Integer.toString(frameworkType.getFrameworkTypeId()),
						frameworkType.getFrameworkName());
				valueObjects.add(valueObject);
			}

		return valueObjects;
	}
	
//	@Override
	@Transactional(value="webTransactionManager",rollbackFor=Exception.class)
	public String setAreaId() {

	/*List<TemplateDetail> templateDetails = templateDetailRepository.findAll();
		
		for (TemplateDetail templateDetail : templateDetails) {
			templateDetail.setAreaCode(templateDetail.getUserDetail().
					getUserRoleFeaturePermissionMappings().get(0).getRoleFeaturePermissionScheme().getAreaCode());
			templateDetailRepository.save(templateDetail);
		}*/
		
		List<TXNTemplateDetail> txnTemplateDetails = tXNTemplateDetailRepository.findAll();
		
		for (TXNTemplateDetail txnTemplateDetail : txnTemplateDetails) {
			txnTemplateDetail.setAreaCode(txnTemplateDetail.getTemplateDetail().getAreaCode());
			tXNTemplateDetailRepository.save(txnTemplateDetail);
		}
		
		return "done";
	}
}
