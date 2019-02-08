package org.sdrc.cysdrf.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.sdrc.cysdrf.model.RawData;
import org.sdrc.cysdrf.util.Constants;
import org.sdrc.devinfo.domain.UtAreaEn;
import org.sdrc.devinfo.domain.UtData;
import org.sdrc.devinfo.repository.DataRepository;
import org.sdrc.devinfo.repository.springdatajpa.DevInfoAreaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AggregationServiceImpl implements AggregationService {

	@Autowired
	private DataRepository dataRepository;
	
	@Autowired
	private ResourceBundleMessageSource delimeterMessageSource; 
	
//	@Autowired
//	private IUSDetailRepository iusDetailRepository;
	
	@Autowired
	private DevInfoAreaRepository areaRepository;
	
	@Autowired 
	private ResourceBundleMessageSource subSectorIusListMessage;
	
	
	Boolean flag;
	String projectId;
	int areaId;
	int timePeriod_Nid;
	BigDecimal confidenceIntervalLower=null;
	BigDecimal confidenceIntervalUpper=null;
	Integer dataDenominator=0;
	String dataValue;
	Date endDate=null;
	int footNoteNId=-1;
	Integer icIusOrder=null;
	byte isMRD=1;
	byte isPlannedValue=0;
	byte isTextualData=0;
	String iuNid=null;
	byte multipleSource=1;
	Integer sourceNId;
	Date startDate=null;
	String textualDataValue=null;
	Integer iusNid;
	String totalPlanned;
	String totalAchieved;
	String achievement;
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public void doAggregate(String projectList,String areaCode,int timeperiodId,String fileName,Boolean flag) throws Exception{
	
		this.flag=flag;
		projectId=projectList;
		timePeriod_Nid=timeperiodId;	
		
		UtAreaEn area=areaRepository.getAreaNId(areaCode);
		areaId=area.getArea_NId();
		//Declaring XSSFWorkbook varriable ;
		HSSFWorkbook workbook = null;
		
		//Reading file from specified location and storing it in FileInputStream
		FileInputStream  fis=new FileInputStream(new File(fileName));

		//Initializing XSSFWorkbook varriable with FileInputStream;
		workbook = new HSSFWorkbook(fis);
		
		//Declaring and Intializing worksheet varriable
		HSSFSheet workSheet=workbook.getSheetAt(0);
		
		HSSFSheet percentWorkSheet=null;
		
		
		if(!flag){
			percentWorkSheet=workbook.getSheetAt(1);
		}
		
		
		
			for(int i=3;i<=workSheet.getLastRowNum();i++){
				
				Double allProjectAchievedValue=0.0;
				
				//Initializing row Varriable
				HSSFRow row=workSheet.getRow(i);
				//Declaring Percentrow
				HSSFRow percentRow=null;
				
				// Refer section @1.1
				if(!flag){
					percentRow=percentWorkSheet.getRow(i);
				}
				
					
				
				if(row.getCell(4).getCellType()==HSSFCell.CELL_TYPE_BLANK 
						|| (row.getCell(4).getCellType()==HSSFCell.CELL_TYPE_STRING 
						&& row.getCell(4).getStringCellValue()==null )
						|| row.getCell(6).getNumericCellValue()==0){
					continue ;
				}
				
				RawData rawData=new RawData();
				
				
				for (int j=4; j<9; j++) {
					HSSFCell cell=row.getCell(j);
					String val=j%6==0?rawData.setPlannedIusNid((int)(cell.getNumericCellValue()))
							:j%(4*2)==0?rawData.setAchivmentIusNid((int)(cell.getNumericCellValue()))//tobe change
							:j%5==0?rawData.setUnitId((int)(cell.getNumericCellValue()))
							:j%4==0?rawData.setIndicatorId((int)(cell.getNumericCellValue()))	
							:rawData.setAchivedIusNid((int)(cell.getNumericCellValue()));
//							System.out.println(val);
				}
				
				
		  int projectCount=-1;
				
				if (flag) {
					
					
					if(rawData.getAchivmentIusNid()==0){
						continue ;
					}
					
				
				int plannedCounter;

				for (plannedCounter = 9; plannedCounter < 9 + projectId
						.split(",").length * 2; plannedCounter+=2) {//+=(Suggestion)
					projectCount++;
					if (row.getCell(plannedCounter).getCellType()==HSSFCell.CELL_TYPE_BLANK ||
					(row.getCell(plannedCounter).getCellType()==HSSFCell.CELL_TYPE_STRING
					&& row.getCell(plannedCounter).getStringCellValue() == null)); //Critical issue
					
					//Step-1 And Step-5
					else {
					
						dataValue = Double.toString(row.getCell(plannedCounter).getNumericCellValue());
						iusNid=rawData.getPlannedIusNid();
						rawData.setUnitId((int)row.getCell(5).getNumericCellValue());
						sourceNId = Integer.valueOf(projectId.split(",")[projectCount]);//Remarks Source in DI database by Avinash sir
						UtData utData = putValues(rawData,1,dataValue, sourceNId,iusNid);
						dataRepository.save(utData);
						
						//persisting for upper level Area
						//Step-3
						if(areaId!=2){
						UtData aggregateData=dataRepository.getDataForAreaAndTimePeriodAndProject(timePeriod_Nid, sourceNId, 2, iusNid);
						if(aggregateData!=null){
							dataRepository.update(2, timePeriod_Nid, iusNid,sourceNId, aggregateData.getData_Value()+Double.parseDouble(dataValue));
						}
						else{
							UtData aggregateDataValueForPlanned=putValues(rawData,1,dataValue, sourceNId,iusNid);
							aggregateDataValueForPlanned.setArea_NId(2);
							dataRepository.save(aggregateDataValueForPlanned);
						}
						}
					}
				}
				
				//Step-2 Consolidation at district level
				if(areaId!=2){
				if(row.getCell(69).getCachedFormulaResultType()==HSSFCell.CELL_TYPE_NUMERIC
						|| (row.getCell(69).getCachedFormulaResultType()==HSSFCell.CELL_TYPE_STRING 
						&& row.getCell(69).getStringCellValue()!=null
						&& !row.getCell(69).getStringCellValue().equals(""))){
				dataValue = Double.toString(row.getCell(69).getNumericCellValue());
				rawData.setUnitId((int)row.getCell(5).getNumericCellValue());
				iusNid=rawData.getPlannedIusNid();//Critical
				//To be done
				sourceNId = Integer.valueOf(39);
				UtData utData = putValues(rawData,1,dataValue, sourceNId,iusNid);
				dataRepository.save(utData);
				}
				}
				//persisting for upper level Area
				//Step-4 Consolidation at district level
				if(row.getCell(69).getCachedFormulaResultType()==HSSFCell.CELL_TYPE_NUMERIC 
						|| (row.getCell(69).getCachedFormulaResultType()==HSSFCell.CELL_TYPE_STRING 
						&& row.getCell(69).getStringCellValue()!=null 
						&& !row.getCell(69).getStringCellValue().equals(""))){
				sourceNId = Integer.valueOf(39);
				UtData aggregateData=dataRepository.getDataForAreaAndTimePeriodAndProject(timePeriod_Nid, sourceNId, 2, iusNid);
				if(aggregateData!=null){
					dataRepository.update(2, timePeriod_Nid, iusNid, sourceNId, aggregateData.getData_Value()+Double.parseDouble(dataValue));
				}
				else{
					UtData aggregateDataValueForTotal=putValues(rawData,1,dataValue, sourceNId,iusNid);
					aggregateDataValueForTotal.setArea_NId(2);
					dataRepository.save(aggregateDataValueForTotal);
				}
				}
				
				
			}
			//This else for Achieved Value
			else {
			//Declaring achivedCounter
				int achivedCounter;
				
				
					for (achivedCounter = 10; achivedCounter < 9 + projectId.split(",").length * 2; achivedCounter+=2) {
					projectCount++;
					
					// to be changed
//					if (row.getCell(achivedCounter).getRawValue() == null || percentRow.getCell(achivedCounter).getRawValue().equals("#DIV/0!")) {

					Double individualProjectAchievedValue=0.0;
					
						if (rawData.getAchivedIusNid()==0? percentRow.getCell(achivedCounter).getCellType()==HSSFCell.CELL_TYPE_STRING && 
								percentRow.getCell(achivedCounter).getStringCellValue().equals("#DIV/0!"):
									row.getCell(achivedCounter).getCellType()==HSSFCell.CELL_TYPE_BLANK ||
									(row.getCell(achivedCounter).getCellType()==HSSFCell.CELL_TYPE_STRING &&
									row.getCell(achivedCounter).getStringCellValue() == null));
						 else {
						
							dataValue =rawData.getAchivedIusNid()==0? Double.toString(percentRow.getCell(achivedCounter).getNumericCellValue()):
								Double.toString(row.getCell(achivedCounter).getNumericCellValue());
							iusNid=rawData.getAchivedIusNid()==0 ?rawData.getAchivmentIusNid():rawData.getAchivedIusNid();
							/*String unitVal=rawData.getAchivedIusNid()==0?rawData.setUnitId(2):
								rawData.setUnitId((int)row.getCell(5).getNumericCellValue());*/
							sourceNId = Integer.valueOf(projectId.split(",")[projectCount]);
							Integer subGroup=rawData.getAchivedIusNid()==0?3:2;	 
							individualProjectAchievedValue=Double.parseDouble(dataValue);
							 
							 
							 
							 
						/*Step-1*/
						UtData utData = putValues(rawData,subGroup,dataValue, sourceNId,iusNid);
						dataRepository.save(utData);
						//Step-8
						if(areaId!=2){
						UtData aggregateData=dataRepository.getDataForAreaAndTimePeriodAndProject(timePeriod_Nid, sourceNId, 2, iusNid);
						if(aggregateData!=null){
							dataRepository.update(2, timePeriod_Nid, iusNid, 
												  sourceNId, aggregateData.getData_Value()+Double.parseDouble(dataValue));
						}
						else{
							
							UtData aggregateData2= putValues(rawData,subGroup,dataValue, sourceNId,iusNid);
							aggregateData2.setArea_NId(2);
							dataRepository.save(aggregateData2);
						}
						}
						
						
						
						//To be changed //done
						if(rawData.getAchivedIusNid()!=0 && !( percentRow.getCell(achivedCounter).getCellType()==HSSFCell.CELL_TYPE_BLANK
								|| (percentRow.getCell(achivedCounter).getCellType()==HSSFCell.CELL_TYPE_STRING && 
								percentRow.getCell(achivedCounter).getStringCellValue()==null))){
							dataValue=Double.toString(percentRow.getCell(achivedCounter).getNumericCellValue());
							iusNid=(int) percentRow.getCell(8).getNumericCellValue();
							sourceNId = Integer.valueOf(projectId.split(",")[projectCount]);
							rawData.setUnitId((int)percentRow.getCell(5).getNumericCellValue());
							//Step-3
							UtData districtAchievementForIndividualProject = putValues(rawData,3,dataValue, sourceNId,iusNid);
							dataRepository.save(districtAchievementForIndividualProject);
							//Step-10
							//State AggreagateData for all District Achievement data
							if(areaId!=2){
							UtData stateAggregateDataForIndividualProjectAchievement=dataRepository.getDataForAreaAndTimePeriodAndProject(timePeriod_Nid, sourceNId, 2, iusNid);
							if(stateAggregateDataForIndividualProjectAchievement!=null){
								UtData projectSpecificTotalPlannedData=dataRepository.getDataForAreaAndTimePeriodAndProject(timePeriod_Nid, sourceNId,2,rawData.getPlannedIusNid());
								UtData projectSpecificTotalAchievedData=dataRepository.getDataForAreaAndTimePeriodAndProject(timePeriod_Nid, sourceNId,2,rawData.getAchivedIusNid());
//								System.out.println("Planned Value:"+projectSpecificTotalPlannedData.getData_Value());
//								System.out.println("Achieved Value:"+(projectSpecificTotalAchievedData.getData_Value()+individualProjectAchievedValue)+"::"+individualProjectAchievedValue);
//								System.out.println("t.p::"+timePeriod_Nid+"Source::"+sourceNId+"PlannedIUS::"+rawData.getPlannedIusNid());
								Double neumerator=(projectSpecificTotalAchievedData.getData_Value()+individualProjectAchievedValue);
								Double denomenator=projectSpecificTotalPlannedData.getData_Value()==0?1:projectSpecificTotalPlannedData.getData_Value();
								Double result=neumerator/denomenator;	
								Double achievementValue=getDataValue(result*100);
//								System.out.println("TotalValue::"+achievementValue);
								dataRepository.update(2, timePeriod_Nid, iusNid, sourceNId, Double.valueOf(achievementValue));
							}
							else{
								
								UtData districtAchievementForIndividualProject2 = putValues(rawData,3,dataValue, sourceNId,iusNid);
								districtAchievementForIndividualProject2.setArea_NId(2);
								dataRepository.save(districtAchievementForIndividualProject2);
							}
							}
							
						}
					}
				}
					
					if(rawData.getAchivedIusNid()!=0){
				
					if(row.getCell(70).getCachedFormulaResultType() == Cell.CELL_TYPE_NUMERIC ||
							(row.getCell(70).getCachedFormulaResultType() == Cell.CELL_TYPE_STRING 
							&& row.getCell(70).getStringCellValue()!=null 
							&& !row.getCell(70).getStringCellValue().equals(""))){
						//changed here
						dataValue = Double.toString(row.getCell(70).getNumericCellValue());
						allProjectAchievedValue=Double.parseDouble(dataValue);
						iusNid=rawData.getAchivedIusNid();
						if(areaId!=2){
						rawData.setUnitId((int)row.getCell(5).getNumericCellValue());
//						iusNid=rawData.getAchivedIusNid();
						sourceNId = 39;
						UtData utData = putValues(rawData,2,dataValue, sourceNId,iusNid);
						/*UtData totalData=dataRepository.getDataForAreaAndTimePeriodAndProject(timePeriod_Nid,sourceNId,2, iusNid);*/
						//Step-2
						dataRepository.save(utData);
						}
						
						//Step-9
						sourceNId = 39;
						UtData aggregateData=dataRepository.getDataForAreaAndTimePeriodAndProject(timePeriod_Nid, sourceNId, 2, iusNid);
						if(aggregateData!=null){
							dataRepository.update(2, timePeriod_Nid, iusNid, sourceNId, aggregateData.getData_Value()+Double.parseDouble(dataValue));
						}
						else{
							UtData utData2= putValues(rawData,2,dataValue, sourceNId,iusNid);
							utData2.setArea_NId(2);
							dataRepository.save(utData2);
						}
					}	
					if((!(percentRow.getCell(69).getCachedFormulaResultType()==Cell.CELL_TYPE_ERROR))
							&&!(percentRow.getCell(69).getCachedFormulaResultType()== Cell.CELL_TYPE_STRING 
							&& percentRow.getCell(69).getStringCellValue().equals(""))){
						
//						System.out.println(percentRow.getCell(69).getCellType()+"--row num-->"+percentRow.getRowNum());
						dataValue = Double.toString(percentRow.getCell(69).getNumericCellValue());
						rawData.setUnitId((int)percentRow.getCell(5).getNumericCellValue());
						iusNid=rawData.getAchivmentIusNid();
						sourceNId = 39;
						UtData individualIndicatorspecificTotalAchievement = putValues(rawData,3,dataValue,sourceNId,iusNid);
						if(areaId!=2){
							//Step-4
							dataRepository.save(individualIndicatorspecificTotalAchievement);
								}
					
							UtData stateAggregrateIndicatorWiseAchievement=dataRepository.getDataForAreaAndTimePeriodAndProject(timePeriod_Nid, sourceNId, 2, iusNid);
							//Step-11
							if(stateAggregrateIndicatorWiseAchievement !=null){
							UtData projectSpecificTotalPlannedData=dataRepository.getDataForAreaAndTimePeriodAndProject(timePeriod_Nid, 39,2,rawData.getPlannedIusNid());
							UtData projectSpecificTotalAchievedData=dataRepository.getDataForAreaAndTimePeriodAndProject(timePeriod_Nid, 39,2,rawData.getAchivedIusNid());
							try{
								
								Double neumerator=(projectSpecificTotalAchievedData.getData_Value()+allProjectAchievedValue);
								Double denomenator=projectSpecificTotalPlannedData.getData_Value()==0?1:projectSpecificTotalPlannedData.getData_Value();
								Double result=neumerator/denomenator;	
								Double totalAchievementValue=getDataValue(result*100);
//							Double totalAchievementValue=getDataValue(((projectSpecificTotalAchievedData.getData_Value()+allProjectAchievedValue)/projectSpecificTotalPlannedData.getData_Value()==0?1:projectSpecificTotalPlannedData.getData_Value())*100);
							dataRepository.update(2, timePeriod_Nid, iusNid, 39, Double.valueOf(totalAchievementValue));
							}
							catch(NullPointerException npe){
								UtData individualIndicatorspecificTotalAchievement2 = putValues(rawData,3,dataValue,sourceNId,iusNid);
								individualIndicatorspecificTotalAchievement2.setArea_NId(2);
								/*Step-4 nd Step-11*/
								dataRepository.save(individualIndicatorspecificTotalAchievement2);
							}
							}
							else{
								UtData individualIndicatorspecificTotalAchievement2 = putValues(rawData,3,dataValue,sourceNId,iusNid);
								individualIndicatorspecificTotalAchievement2.setArea_NId(2);
								/*Step-4 nd Step-11*/
								dataRepository.save(individualIndicatorspecificTotalAchievement2);
							}
					}
					}
			
			}
}
			if(!flag){
				subSectorAggregation(timePeriod_Nid,fileName);
			}
		}
	
	
	
		private Double getDataValue(Double val){
//			System.out.println("Error::::::::::"+val);
		DecimalFormat df = new DecimalFormat("#.#");
		return Double.parseDouble(df.format(val));
		}
	
	
	
	
	public UtData putValues(RawData rawData,Integer subgroupNid,String dataValue,Integer sourceNId,Integer iusNid){
		UtData utData=new UtData();
		utData.setArea_NId(areaId);
		utData.setIndicator_NId(rawData.getIndicatorId());
		utData.setTimePeriod_NId(timePeriod_Nid);
		utData.setIUSNId(iusNid);
		utData.setUnit_NId(rawData.getUnitId());
		utData.setConfidenceIntervalLower(confidenceIntervalLower);
		utData.setConfidenceIntervalUpper(confidenceIntervalUpper);
		utData.setData_Denominator(dataDenominator);
		utData.setEnd_Date(endDate);
		utData.setStart_Date(startDate);
		utData.setFootNote_NId(footNoteNId);
		utData.setIC_IUS_Order(icIusOrder);
		utData.setIsMRD(isMRD);
		utData.setIsPlannedValue(isPlannedValue);
		utData.setIsTextualData(isTextualData);
		utData.setMultipleSource(multipleSource);
		utData.setData_Value(Double.valueOf(dataValue));
		utData.setTextual_Data_Value(textualDataValue);
		utData.setSubgroup_Val_NId(subgroupNid);
		utData.setIUNId(rawData.getIndicatorId()+delimeterMessageSource.getMessage("delimeter.underscore",null,null)+rawData.getUnitId());
		utData.setSource_NId(sourceNId);
		return utData;
		
	}
	
	@Transactional(rollbackFor=Exception.class)
	public void subSectorAggregation(int timePeriod,String fileName) throws IOException {
		HSSFWorkbook workbook = null;
		// Reading file from specified location and storing it in
		// FileInputStream
		FileInputStream fis = new FileInputStream(new File(fileName));
		workbook = new HSSFWorkbook(fis);
		HSSFSheet workSheet = workbook.getSheetAt(1);
		for (int i = 2; i < workSheet.getLastRowNum(); i++) {
			if(i==23 || i==35 || i==45)
				continue;
			HSSFRow row = workSheet.getRow(i);
			if (null==row || (row.getCell(4).getCellType() == Cell.CELL_TYPE_STRING && row.getCell(4).getStringCellValue() == "" )
					|| (row.getCell(4).getCellType() == Cell.CELL_TYPE_NUMERIC && row.getCell(6).getNumericCellValue() != 0.0)) {
				continue;
			}
			/*Double totalAchievementValue = 0.0;*/
			RawData rawData = new RawData();
			for (int j = 4; j < 9; j++) {
				HSSFCell cell = row.getCell(j);
				String val = j % (4 * 2) == 0 ? rawData.setAchivmentIusNid((int)cell.getNumericCellValue())
						: j % 5 == 0 ? rawData.setUnitId((int)cell.getNumericCellValue()) :
							j % 4 == 0 ? rawData.setIndicatorId((int)cell.getNumericCellValue()) : "";
			}
			int projectCount = -1;
			for (int subGroupAchievementCounter = 10; subGroupAchievementCounter < 9 + projectId.split(",").length * 2; subGroupAchievementCounter += 2) {
				projectCount++;
				
				
						
				if ((!(row.getCell(subGroupAchievementCounter).getCachedFormulaResultType()==Cell.CELL_TYPE_ERROR))
						&&!(row.getCell(subGroupAchievementCounter).getCachedFormulaResultType()== Cell.CELL_TYPE_STRING 
						&& row.getCell(subGroupAchievementCounter).getStringCellValue().equals(""))) {
					
					dataValue = Double.toString(row.getCell(subGroupAchievementCounter).getNumericCellValue());
					iusNid = rawData.getAchivmentIusNid();
					rawData.setUnitId((int)row.getCell(5).getNumericCellValue());
					sourceNId = Integer.valueOf(projectId.split(",")[projectCount]);// Remarks Source inDIdatabasebyAvinashsir
					UtData utData = putValues(rawData, 3, dataValue, sourceNId,iusNid);
					dataRepository.save(utData);
					
					
					
//					   Arrays.asList(subGroupNid.split(delimeterMessageSource.getMessage(Constants.DELIMETR_COMMA, null, null))).stream().map(Integer::valueOf).collect(Collectors.toList()),
	
					
					Double value=aggregateDataForSubsector(timePeriod_Nid,sourceNId,Arrays.asList((subSectorIusListMessage.getMessage(String.valueOf(iusNid), null, null)).split(delimeterMessageSource.getMessage(Constants.DELIMETR_COMMA, null, null))).stream().map(Integer::valueOf).collect(Collectors.toList()));
					UtData aggregateDataValue = putValues(rawData, 3,String.valueOf(value), sourceNId,iusNid);
					aggregateDataValue.setArea_NId(2);
					//to be update
					UtData resultValue=dataRepository.getDataForAreaAndTimePeriodAndProject(timePeriod_Nid, sourceNId, 2, iusNid);
					if(resultValue!=null){
						dataRepository.update(2, timePeriod_Nid, iusNid, sourceNId, value);
					}
					else{
						dataRepository.save(aggregateDataValue);	
					}
					
					
					
					
//SPECIFIC PROJECT WISE AGGREGATION AT STATE LEVEL
					
					/*if (areaId != 2) {
						List<UtData> subSectorAchievementListValue = dataRepository.getProjectWiseData(timePeriod, sourceNId,rawData.getAchivmentIusNid());
						if (subSectorAchievementListValue == null|| subSectorAchievementListValue.size() == 1) {
							UtData achievementTotalUtData = putValues(rawData,3, dataValue, sourceNId, iusNid);
							achievementTotalUtData.setArea_NId(2);
							dataRepository.save(achievementTotalUtData);
						} else {
							Double dValue = 0.0;

							for (UtData achievementValue : subSectorAchievementListValue) {
								dValue += achievementValue.getData_Value();
							}
							dValue = getDataValue(dValue/subSectorAchievementListValue.size());
							
							// should be Update Query
							dataRepository.update(2, timePeriod, iusNid,sourceNId, dValue);
						}
					}*/
				}
			}
//			if (areaId != 2) {
			
			
				if ((!(row.getCell(69).getCachedFormulaResultType()==Cell.CELL_TYPE_ERROR))
						&&!(row.getCell(69).getCachedFormulaResultType()== Cell.CELL_TYPE_STRING 
						&& row.getCell(69).getStringCellValue().equals(""))) {
					
					dataValue =Double.toString(row.getCell(69).getNumericCellValue());
					iusNid = rawData.getAchivmentIusNid();
					sourceNId = 39;
					if (areaId != 2) {
					UtData utData = putValues(rawData, 3, dataValue, sourceNId,iusNid);
					dataRepository.save(utData);
					}
					//FOR ALL PROJECTWISE AGGREGATION AT STATE LEVEL
					
					
					Double value=aggregateDataForSubsector(timePeriod_Nid,sourceNId,Arrays.asList((subSectorIusListMessage.getMessage(String.valueOf(iusNid), null, null)).
							split(delimeterMessageSource.getMessage(Constants.DELIMETR_COMMA, null, null))).stream().map(Integer::valueOf).collect(Collectors.toList()));
					UtData aggregateDataValue = putValues(rawData, 3,String.valueOf(value), sourceNId,iusNid);
					aggregateDataValue.setArea_NId(2);
					//to be update
					
					UtData resultValue=dataRepository.getDataForAreaAndTimePeriodAndProject(timePeriod_Nid, sourceNId, 2, iusNid);
					if(resultValue!=null){
						dataRepository.update(2, timePeriod_Nid, iusNid, sourceNId, value);
					}
					else{
						dataRepository.save(aggregateDataValue);	
					}
					
				}			
//				}
		/*	if (row.getCell(101).getRawValue() != null && !row.getCell(101).getRawValue().equals("#DIV/0!")) {
				UtData subSectorAchievementtValue = dataRepository.getDataForAreaAndTimePeriodAndProject(timePeriod, 15,2, rawData.getAchivmentIusNid());
				dataValue = row.getCell(101).getRawValue();
				iusNid = rawData.getAchivmentIusNid();
				sourceNId = 15;
				if (subSectorAchievementtValue == null) {
//					areaId = 2;
					UtData utData = putValues(rawData, 3, dataValue, sourceNId,iusNid);
					utData.setArea_NId(2);
					dataRepository.save(utData);
				} else {
					Double value = 0.0;
					List<UtData> dataValueOfSubsectorAchievementExcludingTotalSource = dataRepository.getDataExcludingTotalSource(timePeriod, iusNid);
					for (UtData utData : dataValueOfSubsectorAchievementExcludingTotalSource) {
						value += utData.getData_Value();
					}
					value =getDataValue(value/dataValueOfSubsectorAchievementExcludingTotalSource.size());
					subSectorAchievementtValue.setData_Value(value);
					dataRepository.update(2, timePeriod_Nid, iusNid, 15, value);
				}
			}*/	
			
		}
	}
	
private Double aggregateDataForSubsector(int timePeriod,int sourceNid,List<Integer> iusNids){
	
	List<Double> dataValues=dataRepository.getSubsectroAchievementForIndividualProject(timePeriod, iusNids, sourceNid);
	Double value=0.0;
		for (Double dataValue : dataValues) {
					value+=dataValue;
			}
		value=value/dataValues.size();
		return getDataValue(value);
}





@SuppressWarnings("resource")
@Transactional(rollbackFor=Exception.class)
@Override
public String oprationalAggregate(int sourceNId,String areaCode,int timeperiodId,String fileName,Boolean flag) throws IOException {
	// TODO Auto-generated method stub
	timePeriod_Nid=timeperiodId;
	
	
	UtAreaEn area=areaRepository.getAreaNId(areaCode);
	areaId=area.getArea_NId();
	
	FileInputStream  fis=new FileInputStream(new File(fileName));
	
	HSSFWorkbook workbook=null;
	try{
	workbook=new HSSFWorkbook(fis);
	}
	catch(Exception e){
		e.printStackTrace();
		return ("Unsupported file format.");
	}
	
	HSSFSheet percentSheet=workbook.getSheetAt(1);
	
	HSSFSheet sheet=workbook.getSheetAt(0);
	
	FormulaEvaluator evaluator=workbook.getCreationHelper().createFormulaEvaluator();
	
	for(int i=4;i<sheet.getLastRowNum();i++){
		
		
		
		HSSFRow row=sheet.getRow(i);
		RawData rawdata=new RawData();
		for(int j=3;j<8;j++){
			HSSFCell cell=row.getCell(j);
			String val=j%5==0?rawdata.setPlannedIusNid((int)cell.getNumericCellValue())
					  :j%6==0?rawdata.setAchivedIusNid((int)cell.getNumericCellValue())
					  :j%7==0?rawdata.setAchivmentIusNid((int)cell.getNumericCellValue())
					  :j%4==0?rawdata.setUnitId((int)cell.getNumericCellValue())
					  :rawdata.setIndicatorId((int)cell.getNumericCellValue());
		}
		
		
		
		//For Planned
		if(flag){
			
			if(row.getCell(8).getCellType()==HSSFCell.CELL_TYPE_BLANK||row.getCell(8).getCellType()==HSSFCell.CELL_TYPE_STRING && row.getCell(8).getStringCellValue()==null);
			else if(row.getCell(8).getCellType()==HSSFCell.CELL_TYPE_FORMULA){
				 switch(row.getCell(8).getCachedFormulaResultType()) {
		            case Cell.CELL_TYPE_NUMERIC:
//		                System.out.println("Last evaluated as: " + row.getCell(8).getNumericCellValue());
		                dataValue=Double.toString(row.getCell(8).getNumericCellValue());
		                iusNid=rawdata.getPlannedIusNid();
						UtData utData=putValues(rawdata, 1, dataValue, sourceNId, iusNid);
						dataRepository.save(utData);
		                break;
		                
		            default :
		            	break;
		            /*case Cell.CELL_TYPE_STRING:
		                System.out.println("Last evaluated as \"" + row.getCell(8).getRichStringCellValue() + "\"");
		                break;*/
		        }
				 
			}
			else{
	//		dataValue=row.getCell(8).getCellType()==HSSFCell.CELL_TYPE_FORMULA && !row.getCell(8).getStringCellValue().equals("")? row.getCell(8).getStringCellValue()
	//					:row.getCell(8).getCellType()==HSSFCell.CELL_TYPE_NUMERIC?Double.toString(row.getCell(8).getNumericCellValue())
	//					:Double.toString(0.0);
	//			if(row.getCell(8).getCellType()==HSSFCell.CELL_TYPE_FORMULA){
	//				dataValue=row.getCell(8).getStringCellValue();
	//			}
	//			else{
				  dataValue=row.getCell(8).getCellType()==HSSFCell.CELL_TYPE_STRING && !row.getCell(8).getStringCellValue().equals("")? 
							row.getCell(8).getStringCellValue().equals("Yes")?Double.toString(1.0):Double.toString(0.0)
							:Double.toString(row.getCell(8).getNumericCellValue());
	//			}
				
							iusNid=rawdata.getPlannedIusNid();
							UtData utData=putValues(rawdata, 1, dataValue, sourceNId, iusNid);
							dataRepository.save(utData);
			}
			
	}
		
		
		//For achieved
		else{
			
			if(row.getCell(9).getCellType()!=HSSFCell.CELL_TYPE_BLANK && row.getCell(8).getCellType()==HSSFCell.CELL_TYPE_BLANK){
				CellReference cr=new CellReference(row.getRowNum(),9);
//				System.out.println("*******************"+cr.formatAsString().replace("$", "")+" should be empty*********************");
				return ""+cr.formatAsString().replace("$", "")+" should be empty";
			}
			else if(row.getCell(8).getCellType()!=HSSFCell.CELL_TYPE_BLANK && row.getCell(9).getCellType()==HSSFCell.CELL_TYPE_BLANK){
				CellReference cr=new CellReference(row.getRowNum(),9);
//				System.out.println("*******************"+cr.formatAsString().replace("$", "")+" could not be blank.*********************");
				return ""+cr.formatAsString().replace("$", "")+" could not be blank.";
			}
			else{
				if(row.getCell(9).getCellType()==HSSFCell.CELL_TYPE_FORMULA){
					 switch(row.getCell(9).getCachedFormulaResultType()) {
			            case Cell.CELL_TYPE_NUMERIC:
//			                System.out.println("Last evaluated as: " + row.getCell(9).getNumericCellValue());
			                dataValue=Double.toString(row.getCell(9).getNumericCellValue());
			                iusNid=rawdata.getPlannedIusNid();
							UtData utData=putValues(rawdata, 2, dataValue, sourceNId, iusNid);
							dataRepository.save(utData);
			                break;
			                
			            default :
			            	break;
			        }
					
				}
				else if(row.getCell(9).getCellType()==HSSFCell.CELL_TYPE_BLANK||row.getCell(9).getCellType()==HSSFCell.CELL_TYPE_STRING && row.getCell(9).getStringCellValue()==null){
				dataValue=Double.toString(row.getCell(9).getNumericCellValue());
			}
			else{
				dataValue=row.getCell(9).getCellType()==HSSFCell.CELL_TYPE_STRING && !row.getCell(9).getStringCellValue().equals("")? 
						row.getCell(9).getStringCellValue().equals("Yes")?Double.toString(1.0):Double.toString(0.0)
				:Double.toString(row.getCell(9).getNumericCellValue());
				
				iusNid=rawdata.getAchivedIusNid();
				UtData utData=putValues(rawdata, 2, dataValue, sourceNId, iusNid);
				dataRepository.save(utData);
			}
			
			
			
		//For achievement
			HSSFRow percentRow=percentSheet.getRow(i);
			if(row.getCell(9).getCellType()==HSSFCell.CELL_TYPE_BLANK||row.getCell(9).getCellType()==HSSFCell.CELL_TYPE_STRING && row.getCell(9).getStringCellValue()==null);
			else if(row.getCell(9).getCellType()==HSSFCell.CELL_TYPE_FORMULA){
				 switch(row.getCell(9).getCachedFormulaResultType()) {
		            case Cell.CELL_TYPE_NUMERIC:
//		                System.out.println("Last evaluated as: " + row.getCell(9).getNumericCellValue());
		                dataValue=Double.toString(percentRow.getCell(8).getNumericCellValue());
		                iusNid=rawdata.getAchivmentIusNid();
						UtData utData=putValues(rawdata, 2, dataValue, sourceNId, iusNid);
						dataRepository.save(utData);
		                break;
		                
		            default :
		            	break;
		        }
			}
			else{
				dataValue=Double.toString(percentRow.getCell(8).getNumericCellValue());
				iusNid=rawdata.getAchivmentIusNid();
				
				rawdata.setUnitId(2); //to be optimize
				
				UtData utData=putValues(rawdata, 3, dataValue, sourceNId, iusNid);
				dataRepository.save(utData);
				}
//			}
		}
			
		}
	}
	if(!flag){
	subSectorAggregation(timePeriod_Nid,percentSheet,evaluator);
	}
	
	return "success";
}


	public void subSectorAggregation(int timePeriod_Nid,HSSFSheet percentSheet,FormulaEvaluator evaluator) throws IOException{
		for(int z=3;z<=52;z++){
			HSSFRow subSectorRow=percentSheet.getRow(z);
			
			RawData rawData=new RawData();
			for(int y=3;y<=7;y++){
				String val=y==3?rawData.setIndicatorId((int)subSectorRow.getCell(y).getNumericCellValue())
						:y%4==0?rawData.setUnitId((int)subSectorRow.getCell(y).getNumericCellValue())
						:y%7==0?rawData.setAchivmentIusNid((int)subSectorRow.getCell(y).getNumericCellValue()):"";
			}
				
			if(subSectorRow.getCell(5).getNumericCellValue()==0 && subSectorRow.getCell(6).getNumericCellValue()==0){
			Cell subSectorCell=subSectorRow.getCell(8);
			if(subSectorCell!=null){
				switch(evaluator.evaluateInCell(subSectorCell).getCellType()){
				case Cell.CELL_TYPE_NUMERIC :
					dataValue=Double.toString(subSectorCell.getNumericCellValue());
					iusNid=(int) subSectorRow.getCell(7).getNumericCellValue();
					
					UtData utData=putValues(rawData, 3, dataValue, 39, iusNid);
					dataRepository.save(utData);
					break;
	
				default:
					break;
					}
				}
			}
			}
	}
}
	

