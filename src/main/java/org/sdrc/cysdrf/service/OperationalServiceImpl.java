//package org.sdrc.cysdrf.service;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.math.BigDecimal;
//import java.util.Date;
//
//import org.apache.poi.hssf.usermodel.HSSFCell;
//import org.apache.poi.hssf.usermodel.HSSFRow;
//import org.apache.poi.hssf.usermodel.HSSFSheet;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.apache.poi.hssf.util.CellReference;
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.CellValue;
//import org.apache.poi.ss.usermodel.FormulaEvaluator;
//import org.sdrc.cysdrf.model.RawData;
//import org.sdrc.devinfo.domain.UtAreaEn;
//import org.sdrc.devinfo.domain.UtData;
//import org.sdrc.devinfo.repository.DataRepository;
//import org.sdrc.devinfo.repository.springdatajpa.DevInfoAreaRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.support.ResourceBundleMessageSource;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//public class OperationalServiceImpl implements OperationalService {
//
//	@Autowired
//	private ResourceBundleMessageSource delimeterMessageSource; 
//	
//	@Autowired
//	private DevInfoAreaRepository areaRepository;
//	
//	@Autowired
//	private DataRepository dataRepository;
//	
//	Boolean flag;
//	String projectId;
//	int areaId;
//	int timePeriod_Nid;
//	BigDecimal confidenceIntervalLower=null;
//	BigDecimal confidenceIntervalUpper=null;
//	Integer dataDenominator=0;
//	String dataValue;
//	Date endDate=null;
//	int footNoteNId=-1;
//	Integer icIusOrder=null;
//	byte isMRD=1;
//	byte isPlannedValue=0;
//	byte isTextualData=0;
//	String iuNid=null;
//	byte multipleSource=1;
//	Integer sourceNId;
//	Date startDate=null;
//	String textualDataValue=null;
//	Integer iusNid;
//	String totalPlanned;
//	String totalAchieved;
//	String achievement;
//	
//	@SuppressWarnings("resource")
//	@Transactional(rollbackFor=Exception.class)
//	@Override
//	public String oprationalAggregate(int sourceNId,String areaCode,int timeperiodId,String fileName,Boolean flag) throws IOException {
//		// TODO Auto-generated method stub
//		timePeriod_Nid=timeperiodId;
//		
//		
//		UtAreaEn area=areaRepository.getAreaNId(areaCode);
//		areaId=area.getArea_NId();
//		
//		FileInputStream  fis=new FileInputStream(new File(fileName));
//		
//		HSSFWorkbook workbook=null;
//		try{
//		workbook=new HSSFWorkbook(fis);
//		}
//		catch(Exception e){
//			e.printStackTrace();
//			return ("Unsupported file format.");
//		}
//		
//		HSSFSheet percentSheet=workbook.getSheetAt(1);
//		
//		HSSFSheet sheet=workbook.getSheetAt(0);
//		
//		FormulaEvaluator evaluator=workbook.getCreationHelper().createFormulaEvaluator();
//		
//		for(int i=4;i<sheet.getLastRowNum();i++){
//			
//			
//			
//			HSSFRow row=sheet.getRow(i);
//			RawData rawdata=new RawData();
//			for(int j=3;j<8;j++){
//				HSSFCell cell=row.getCell(j);
//				String val=j%5==0?rawdata.setPlannedIusNid((int)cell.getNumericCellValue())
//						  :j%6==0?rawdata.setAchivedIusNid((int)cell.getNumericCellValue())
//						  :j%7==0?rawdata.setAchivmentIusNid((int)cell.getNumericCellValue())
//						  :j%4==0?rawdata.setUnitId((int)cell.getNumericCellValue())
//						  :rawdata.setIndicatorId((int)cell.getNumericCellValue());
//			}
//			
//			
//			
//			//For Planned
//			if(flag){
//			
//			if(row.getCell(8).getCellType()==HSSFCell.CELL_TYPE_BLANK||row.getCell(8).getCellType()==HSSFCell.CELL_TYPE_STRING && row.getCell(8).getStringCellValue()==null
//					||row.getCell(8).getCellType()==HSSFCell.CELL_TYPE_FORMULA);
//			else{
////			dataValue=row.getCell(8).getCellType()==HSSFCell.CELL_TYPE_FORMULA && !row.getCell(8).getStringCellValue().equals("")? row.getCell(8).getStringCellValue()
////						:row.getCell(8).getCellType()==HSSFCell.CELL_TYPE_NUMERIC?Double.toString(row.getCell(8).getNumericCellValue())
////						:Double.toString(0.0);
//				  dataValue=row.getCell(8).getCellType()==HSSFCell.CELL_TYPE_STRING && !row.getCell(8).getStringCellValue().equals("")? 
//							row.getCell(8).getStringCellValue().equals("Yes")?Double.toString(1.0):Double.toString(0.0)
//							:Double.toString(row.getCell(8).getNumericCellValue());
//				
//			iusNid=rawdata.getPlannedIusNid();
//			UtData utData=putValues(rawdata, 1, dataValue, sourceNId, iusNid);
//			dataRepository.save(utData);
//			}
//		}
//			
//			
//			//For achieved
//			else{
//				
//				if(row.getCell(9).getCellType()!=HSSFCell.CELL_TYPE_BLANK && row.getCell(8).getCellType()==HSSFCell.CELL_TYPE_BLANK){
//					CellReference cr=new CellReference(row.getRowNum(),9);
//					System.out.println("*******************"+cr.formatAsString().replace("$", "")+" should be empty*********************");
//					return ""+cr.formatAsString().replace("$", "")+" should be empty";
//				}
//				else if(row.getCell(8).getCellType()!=HSSFCell.CELL_TYPE_BLANK && row.getCell(9).getCellType()==HSSFCell.CELL_TYPE_BLANK){
//					CellReference cr=new CellReference(row.getRowNum(),9);
//					System.out.println("*******************"+cr.formatAsString().replace("$", "")+" could not be blank.*********************");
//					return ""+cr.formatAsString().replace("$", "")+" could not be blank.";
//				}
//				else{
//				if(row.getCell(9).getCellType()==HSSFCell.CELL_TYPE_BLANK||row.getCell(9).getCellType()==HSSFCell.CELL_TYPE_STRING && row.getCell(9).getStringCellValue()==null
//						||row.getCell(9).getCellType()==HSSFCell.CELL_TYPE_FORMULA);
//				else{
//					dataValue=row.getCell(9).getCellType()==HSSFCell.CELL_TYPE_STRING && !row.getCell(9).getStringCellValue().equals("")? 
//							row.getCell(9).getStringCellValue()=="Yes"?Double.toString(1.0):Double.toString(0.0)
//					:Double.toString(row.getCell(9).getNumericCellValue());
//					
//					iusNid=rawdata.getAchivedIusNid();
//					UtData utData=putValues(rawdata, 2, dataValue, sourceNId, iusNid);
//					dataRepository.save(utData);
//				}
//				
//				
//				
//			//For achievement
//				HSSFRow percentRow=percentSheet.getRow(i);
//				if(row.getCell(9).getCellType()==HSSFCell.CELL_TYPE_BLANK||row.getCell(9).getCellType()==HSSFCell.CELL_TYPE_STRING && row.getCell(9).getStringCellValue()==null
//						||row.getCell(9).getCellType()==HSSFCell.CELL_TYPE_FORMULA);
//				else{
//					dataValue=Double.toString(percentRow.getCell(8).getNumericCellValue());
//					iusNid=rawdata.getAchivmentIusNid();
//					
//					UtData utData=putValues(rawdata, 3, dataValue, sourceNId, iusNid);
//					dataRepository.save(utData);
//					}
////				}
//			}
//				
//			}
//		}
//		if(!flag){
//		subSectorAggregation(timePeriod_Nid,percentSheet,evaluator);
//		}
//		
//		return "success";
//	}
//	
//	
//	public void subSectorAggregation(int timePeriod_Nid,HSSFSheet percentSheet,FormulaEvaluator evaluator) throws IOException{
//		for(int z=3;z<=percentSheet.getLastRowNum();z++){
//			HSSFRow subSectorRow=percentSheet.getRow(z);
//			
//			RawData rawData=new RawData();
//			for(int y=3;y<=7;y++){
//				String val=y%3==0?rawData.setIndicatorId((int)subSectorRow.getCell(y).getNumericCellValue())
//						:y%4==0?rawData.setUnitId((int)subSectorRow.getCell(y).getNumericCellValue())
//						:y%7==0?rawData.setAchivmentIusNid((int)subSectorRow.getCell(y).getNumericCellValue()):"";
//			}
//			
//			if(subSectorRow.getCell(5).getNumericCellValue()==0 && subSectorRow.getCell(6).getNumericCellValue()==0){
//			Cell subSectorCell=subSectorRow.getCell(8);
//			if(subSectorCell!=null){
//				switch(evaluator.evaluateInCell(subSectorCell).getCellType()){
//				case Cell.CELL_TYPE_NUMERIC :
//					dataValue=Double.toString(subSectorCell.getNumericCellValue());
//					iusNid=(int) subSectorRow.getCell(7).getNumericCellValue();
//					
//					UtData utData=putValues(rawData, 3, dataValue, 15, iusNid);
//					dataRepository.save(utData);
//					break;
//
//				default:
//					break;
//					}
//				}
//			}
//		}
//	}
//	
//	
//	
//	public UtData putValues(RawData rawData,Integer subgroupNid,String dataValue,Integer sourceNId,Integer iusNid){
//		UtData utData=new UtData();
//		utData.setArea_NId(areaId);
//		utData.setIndicator_NId(rawData.getIndicatorId());
//		utData.setTimePeriod_NId(timePeriod_Nid);
//		utData.setIUSNId(iusNid);
//		utData.setUnit_NId(rawData.getUnitId());
//		utData.setConfidenceIntervalLower(confidenceIntervalLower);
//		utData.setConfidenceIntervalUpper(confidenceIntervalUpper);
//		utData.setData_Denominator(dataDenominator);
//		utData.setEnd_Date(endDate);
//		utData.setStart_Date(startDate);
//		utData.setFootNote_NId(footNoteNId);
//		utData.setIC_IUS_Order(icIusOrder);
//		utData.setIsMRD(isMRD);
//		utData.setIsPlannedValue(isPlannedValue);
//		utData.setIsTextualData(isTextualData);
//		utData.setMultipleSource(multipleSource);
//		utData.setData_Value(Double.valueOf(dataValue));
//		utData.setTextual_Data_Value(textualDataValue);
//		utData.setSubgroup_Val_NId(subgroupNid);
//		utData.setIUNId(rawData.getIndicatorId()+delimeterMessageSource.getMessage("delimeter.underscore",null,null)+rawData.getUnitId());
//		utData.setSource_NId(sourceNId);
//		return utData;
//		
//	}
//	
//}
