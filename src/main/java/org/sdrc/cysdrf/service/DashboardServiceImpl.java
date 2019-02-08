package org.sdrc.cysdrf.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.sdrc.cysdrf.model.ChloropathModel;
import org.sdrc.cysdrf.model.TrendModel;
import org.sdrc.cysdrf.util.AreaObject;
import org.sdrc.cysdrf.util.Constants;
import org.sdrc.cysdrf.util.IusValueObject;
import org.sdrc.cysdrf.util.ValueObject;
import org.sdrc.devinfo.domain.UtAreaEn;
import org.sdrc.devinfo.domain.UtData;
import org.sdrc.devinfo.domain.UtIndicatorClassificationsEn;
import org.sdrc.devinfo.domain.UtIndicatorEn;
import org.sdrc.devinfo.domain.UtTimeperiod;
import org.sdrc.devinfo.repository.AreaRepository;
import org.sdrc.devinfo.repository.DataRepository;
import org.sdrc.devinfo.repository.IndicatorRepository;
import org.sdrc.devinfo.repository.SectorReposiotory;
import org.sdrc.devinfo.repository.TimeperiodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;
@Service
public class DashboardServiceImpl implements DashboardService {
	
	@Autowired
	private SectorReposiotory sectorRepository;

	@Autowired
	private IndicatorRepository indicatorRepository;

	@Autowired
	private TimeperiodRepository timeperiodRepository;
	
	@Autowired
	private AreaRepository areaRepository;

	@Autowired
	private DataRepository dataRepository;
	
	@Autowired
	private  ResourceBundleMessageSource delimeterMessageSource;
	
	@Autowired ResourceBundleMessageSource colourMessageSource;
	
	@Autowired
	private ResourceBundleMessageSource messageSourceNotification;
	
	@Autowired
	private ServletContext context;
	
	private Integer areaNid;
	
	@Override
	public List<ValueObject> getAllSector(int frameworkType) {
		
		List<UtIndicatorClassificationsEn> sectorList=sectorRepository.getAllSector(String.valueOf(frameworkType));
		System.out.println(sectorList);
		
		final List<ValueObject> valueObjectList=new ArrayList<>();
		
		for (UtIndicatorClassificationsEn sector : sectorList) {
			ValueObject valueObject=new ValueObject();
			if(sector.getIC_Name().equals("MIS")) continue;
			valueObject.setKey(String.valueOf(sector.getIC_NId()));
			valueObject.setValue(sector.getIC_Name());
			valueObjectList.add(valueObject);
		}
		
		
		
		
		
//		sectorList.forEach(utIndicatorClassificationsEn->{
//			ValueObject sector=new ValueObject();
//			sector.setKey(String.valueOf(utIndicatorClassificationsEn.getIC_NId()));
//			sector.setValue(utIndicatorClassificationsEn.getIC_Name());
//			valueObjectList.add(sector);
//		});
		return valueObjectList;
	}
	
	@Override
	public List<ValueObject> getAllSubSector(String sectorNid){
		List<UtIndicatorEn> indicatorList=indicatorRepository.findByshort_Name(sectorNid+delimeterMessageSource.getMessage("delimeter.hyphen", null, null));
		List<ValueObject> valueObjectList=null;
		for (UtIndicatorEn indicator : indicatorList) {
			if(valueObjectList==null){
				valueObjectList=new ArrayList<>();
			}
			ValueObject indicators=new ValueObject();
			indicators.setKey(indicator.getShort_Name());
			indicators.setValue(indicator.getIndicator_Name());
			valueObjectList.add(indicators);
		}
		return valueObjectList;
	}
	@Override
    public Map<String, Map<String, String>> getAllTimeperiod() {
            List<UtTimeperiod> timeperiodList=timeperiodRepository.getAllTimeperiod();
            Map<String, Map<String, String>> timePeriodMap=new HashMap<>();
            for(UtTimeperiod timeperiod : timeperiodList){
                    if(timePeriodMap.containsKey(timeperiod.getPeriodicity()))
                                    {
                            timePeriodMap.get(timeperiod.getPeriodicity()).put(String.valueOf(timeperiod.getTimePeriod_NId()),timeperiod.getTimePeriod());
                                    }
                    else{
                            Map<String, String> timePeriodList=new HashMap<>();
                            timePeriodList.put(String.valueOf(timeperiod.getTimePeriod_NId()),timeperiod.getTimePeriod());
                            timePeriodMap.put(timeperiod.getPeriodicity(),timePeriodList);
                    }
            }
            return timePeriodMap;
    }
	
	@Override
	public List<AreaObject> getAllAreaDetails(){
		
		List<UtAreaEn> areaList=areaRepository.getAllAreaDetails();
		List<AreaObject> areaDetails=null;
		
		for(UtAreaEn areas : areaList){
			if(areaDetails==null){
				areaDetails=new ArrayList<>();
			}
			AreaObject area = new AreaObject();
			area.setArea_Nid(areas.getArea_NId());
			area.setArea_Name(areas.getArea_Name());
			area.setArea_Id(areas.getArea_ID());
			areaDetails.add(area);
		}
		return areaDetails;
	}

	@Override
	public List<IusValueObject>getAllSubIndicator(String key,Integer unit,Integer subGroup,String areaId,Integer timePeriod,Integer projectId){
		
		areaNid=areaRepository.getAreaNId(areaId).getArea_NId();
		List<Object[]> iusList=dataRepository.getAllIndicatorValueAsNumber( Integer.parseInt(key.split(delimeterMessageSource.getMessage("delimeter.hyphen", null, null))[1]), unit, subGroup, areaNid, timePeriod,projectId);
		List<IusValueObject> iusDetails=new ArrayList<>();
		for (Object[] iusValueObject : iusList) {
			IusValueObject valueObject=new IusValueObject();
				for(Object obj : iusValueObject){
					
					if(obj instanceof Integer){
						valueObject.setIndicatroNid(String.valueOf(obj.toString()));
						
					}
					else if(obj instanceof Double){
						double dataValue=((Double) obj).doubleValue();
						valueObject.setDataValue(String.valueOf(dataValue));
						valueObject.setCssClass(dataValue<60?colourMessageSource.getMessage("colour.red", null, null):dataValue<70?colourMessageSource.getMessage("colour.yellow", null, null):colourMessageSource.getMessage("colour.green", null, null));
						
					}
					else if(obj instanceof String){
						valueObject.setIndicatorName((String)obj);
					}
					else{
						valueObject.setCssClass(colourMessageSource.getMessage("colour.grey", null, null));
					}
					
				}
				iusDetails.add(valueObject);
			}
		return iusDetails;
	}
	@Override
	public TrendModel getTrendValue(Integer icNid,Integer indicatorNid,String subGroupNid,String timePeriod,Integer projectId)
	{
		List<Object[]> dataList=dataRepository.getAllData(icNid,indicatorNid,
												   Arrays.asList(timePeriod.split(delimeterMessageSource.getMessage(Constants.DELIMETR_COMMA, null, null))).stream().map(Integer::valueOf).collect(Collectors.toList()),
												   Arrays.asList(subGroupNid.split(delimeterMessageSource.getMessage(Constants.DELIMETR_COMMA, null, null))).stream().map(Integer::valueOf).collect(Collectors.toList()),
												   areaNid,projectId);
		StringBuilder planned=new StringBuilder();
		StringBuilder achieved=new StringBuilder();
		StringBuilder tempVar;
		String unitName="";
		for (Object[] data : dataList) {
			tempVar=new StringBuilder();
			Boolean flag=false;
			for (Object object : data) {
			
				if(object instanceof UtTimeperiod){
					tempVar.append(((UtTimeperiod)object).getTimePeriod());
				}
				else{
					tempVar.append(delimeterMessageSource.getMessage("delimeter.equal", null, null)+((UtData)object).getData_Value());
					flag=((UtData)object).getSubgroup_Val_NId()==1?true:false;
					
					unitName=unitName.equals("")?getUnitName(((UtData)object).getUnit_NId()):unitName;
				}
			}
			if(flag){
				planned.append(planned.length()==0?tempVar:delimeterMessageSource.getMessage(Constants.DELIMETR_COMMA, null, null)+tempVar);
			}
			else{
				achieved.append(achieved.length()==0?tempVar:delimeterMessageSource.getMessage(Constants.DELIMETR_COMMA, null, null)+tempVar);
			}
		}
				TrendModel trendModel=new TrendModel();
				trendModel.setPlannedValue(planned.toString());
				trendModel.setAchievedValue(achieved.toString());
				trendModel.setUnit(unitName);
				return trendModel;
	}
	
	private String getUnitName(int unit){
		return unit==1?"Number":unit==2?"Percent":unit==3?"Hectare":unit==4?"Days":unit==5?"Rupees":"Yes/No";
	}

	@Override
	public List<ChloropathModel> getCholropathData(String projectId,
			String timePeriodId, Integer subSectorKey) {
		/*projectId="21";
		timePeriodId="2";
		i=186;*/
		List<UtData> chloropathList=dataRepository.getChloropathData(Integer.parseInt(projectId), Integer.parseInt(timePeriodId), subSectorKey);
		List<ChloropathModel> chloropathData=new ArrayList<ChloropathModel>();
		
		 
		
//		for(UtData data : chloropathList){
		chloropathList.forEach(UtData ->{
//			if(chloropathData==null){
//				chloropathData=
//			}
			ChloropathModel chloropathObject=new ChloropathModel();
			String areaCode = areaRepository.getAreaId(UtData.getArea_NId()).getArea_ID();
			Double dataValue=UtData.getData_Value();
			chloropathObject.setAreaCode(areaCode);
			chloropathObject.setDataValue(dataValue);
			chloropathObject.setCssColor(dataValue<60?colourMessageSource.getMessage("colour.red", null, null):dataValue<70?colourMessageSource.getMessage("colour.yellow", null, null):colourMessageSource.getMessage("colour.green", null, null));
			
			chloropathData.add(chloropathObject);
		});
		return chloropathData;
	}
	
	
	
	//Fetching project for selected area
	@Override
	public List<ValueObject> getAreaSpecificProjects(String areaCode){
		int areaNid=areaRepository.getAreaNId(areaCode).getArea_NId();
		List<UtIndicatorClassificationsEn> projectRawList = dataRepository.getAreaSpecificProject(areaNid);
		
		List<ValueObject> projectList=new ArrayList<ValueObject>();
		
		projectRawList.forEach(project ->{
			ValueObject projectObject=new ValueObject();
			projectObject.setKey(String.valueOf(project.getIC_NId()));
			projectObject.setValue(project.getIC_Name());
			
			projectList.add(projectObject);
		});
		
		return projectList;
		
	}
	
//Create ExcelFile ,Every Sheets by AreaName 
	@Override
	public String downloadExcel(int quarter, String timePeriodVal,String frameworkType) throws Exception
	{
	    List<UtAreaEn> utAreaEns = areaRepository.findByAreaLevel();
	    int sourceNId=39;
	    int[] sbgroupsNid={1,2};
	    
	    List<Object[]> utdatas=new ArrayList<>();
	    String filepath=frameworkType.equalsIgnoreCase(Constants.FRAMEWORK_TYPE_PROGRAMME) ? context.getRealPath(messageSourceNotification.
	    		getMessage(Constants.FACTSHEET_EXCEL_READ_FILEPATH, null, null)) :
	    			context.getRealPath(messageSourceNotification.
	    		    		getMessage(Constants.FACTSHEET_OPERATIONAL_EXCEL_READ_FILEPATH, null, null));
	   
	    FileInputStream inputStream = new FileInputStream(new File(filepath));
	    
	    XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
	    String filename="";
	    int count=5; //cell count
	    int sheetCount = 0; // sheet count
	    for(UtAreaEn utAreaEn:utAreaEns ) {
	    	utdatas=dataRepository.getbysourceNId(utAreaEn.getArea_NId(),quarter, 
	    			sbgroupsNid, sourceNId,frameworkType.equalsIgnoreCase(Constants.FRAMEWORK_TYPE_PROGRAMME) ? "0" : "1");
	    	
	    	if(!utdatas.isEmpty()){
	    		Map<Integer,ValueObject> indicatorMap= new HashMap<Integer,ValueObject>();
	    		Integer key=0;
	    		for(Object[] objects:utdatas){
	    			
	    			key=(((UtIndicatorEn)objects[0]).getIndicator_NId());
	    			if(!indicatorMap.containsKey(key))
	    			{
	    				ValueObject valueObject= new ValueObject();
	    				
		    			if(((UtData)objects[1]).getSubgroup_Val_NId()==1)
		    			{
		    				valueObject.setKey(Double.toString(((UtData)objects[1]).getData_Value()));
		    				
		    			}
		    			else if(((UtData)objects[1]).getSubgroup_Val_NId()==2)
		    			{
		    				valueObject.setValue(Double.toString(((UtData)objects[1]).getData_Value()));
		    				
		    			}
		    			valueObject.setDescription(utAreaEn.getArea_Name());
		    			indicatorMap.put(key,valueObject);
	    			}
	    			else
	    			{
	    				if(((UtData)objects[1]).getSubgroup_Val_NId()==1)
		    			{
		    				indicatorMap.get(key).setKey(Double.toString(((UtData)objects[1]).getData_Value()));
		    			}
		    			else if(((UtData)objects[1]).getSubgroup_Val_NId()==2)
		    			{
		    				indicatorMap.get(key).setValue(Double.toString(((UtData)objects[1]).getData_Value()));
		    			}
	    				indicatorMap.get(key).setDescription(utAreaEn.getArea_Name());
	    				
	    			}
	    		}
	    		Sheet spreadSheet = workbook.getSheetAt(sheetCount);
	    		//rename pre defined sheet present in template
	    		workbook.setSheetName(workbook.getSheetIndex(spreadSheet), indicatorMap.get(key).getDescription());
	    		
	    		Iterator<Row> iterator = spreadSheet.iterator();
	    		while (iterator.hasNext()) {
	    			Row nextRow = iterator.next();
	                    if(nextRow.getRowNum()==0)
	                    {
	                    	Cell cell=nextRow.getCell(5)==null? nextRow.createCell(5):nextRow.getCell(5);
	                        cell.setCellValue("All Project");
	                    }
	                    if(nextRow.getRowNum()==1){
	                    	Cell cell=nextRow.getCell(5)==null? nextRow.createCell(5):nextRow.getCell(5);
	                        cell.setCellValue("Planned");
	                        cell=nextRow.getCell(6)==null? nextRow.createCell(6):nextRow.getCell(6);
	                        cell.setCellValue("Achieved");
	                    }
	    			
	    			if(frameworkType.equalsIgnoreCase(Constants.FRAMEWORK_TYPE_PROGRAMME) && 
	    					nextRow.getRowNum() == 12 || nextRow.getRowNum() ==23 ||nextRow.getRowNum() ==24 || 
	    					nextRow.getRowNum() ==27 ||nextRow.getRowNum() ==30 ||nextRow.getRowNum() ==33 ||
	    					nextRow.getRowNum() ==35 || nextRow.getRowNum() ==36 ||nextRow.getRowNum() ==42 ||
	    					nextRow.getRowNum() ==45 || nextRow.getRowNum() ==46 || nextRow.getRowNum() ==49)
	    				continue;
	    			
	    			if(frameworkType.equalsIgnoreCase(Constants.FRAMEWORK_TYPE_OPERATION) && 
	    					nextRow.getRowNum() == 7 || nextRow.getRowNum() ==11 ||nextRow.getRowNum() ==14 || 
	    					nextRow.getRowNum() ==15 ||nextRow.getRowNum() ==18 ||nextRow.getRowNum() ==23 ||
	    					nextRow.getRowNum() ==24 || nextRow.getRowNum() ==31 ||nextRow.getRowNum() ==35 ||
	    					nextRow.getRowNum() ==47 || nextRow.getRowNum() ==48)
	    				continue;
	    		
	    			      
	    			
	    			if(nextRow.getRowNum()>=3 && null!= nextRow.getCell(4)){
	        			int indicatorNid = (int) nextRow.getCell(4).getNumericCellValue();
		    			ValueObject valueObject = indicatorMap.get(indicatorNid);
		    			
		    			
			    		
		    			if(null!=valueObject){
		    				if(!valueObject.getKey().equals("")){
		    					Cell plannedCell =nextRow.getCell(count)==null? nextRow.createCell(count):nextRow.getCell(count);
		    					plannedCell.setCellStyle(plannedCell.getCellStyle());
		    					plannedCell.setCellValue(valueObject.getKey());
		    					
		    					
		    				}if(null!=valueObject){
		    					Cell achievedCell =nextRow.getCell(count+1)==null? nextRow.createCell(count+1):nextRow.getCell(count+1);
		    					achievedCell.setCellStyle(achievedCell.getCellStyle());
		    					achievedCell.setCellValue(valueObject.getValue() == null ? "" : valueObject.getValue());
		    				}
		    			}
	    			}
	    		}
	    		sheetCount++;
	}
	    	
	 	   }
	    
	    
	    //remove the rest sheets which have no data to fill in reverse order
	    for(int i=workbook.getNumberOfSheets()-1; i >=sheetCount; i--){
	    	workbook.removeSheetAt(i);
	    }
	    
	    if(sheetCount==0){
	    	Sheet spreadSheet = workbook.createSheet("Factsheet");
    		//rename pre defined sheet present in template
    		Row rowNoVal = spreadSheet.createRow(0);
    		Cell cellNoVal = rowNoVal.createCell(0);
    		cellNoVal.setCellValue("NO DATA AVAILABLE");
	    }
	    filename = messageSourceNotification.getMessage(Constants.FACTSHEET_FILEPATH, null,null)+
				"/"+"CRVS_Factsheet_"+timePeriodVal.replaceAll("-", "_")+"_"+ new Date().getTime()+".xlsx";
		   try( FileOutputStream fileOutputStream = new FileOutputStream(new File(filename)))
		   {
		  workbook.write(fileOutputStream);
		  
		    fileOutputStream.close();
		    workbook.close();
		   }
			return filename;
	    }
	

	//get distinct periodicity for factsheet download
	@Override
	public List<String> getDistinctPeriodicty(){
		return timeperiodRepository.findDistinctPeriodicity();
	}

/*	@Override
	public List<IusValueObject> getSubSectorAsGrid(String sectorId,int timeperiodNid,int projectId) {
		List<UtIndicatorEn> indicatorList=indicatorRepository.findByshort_Name(sectorId+delimeterMessageSource.getMessage("delimeter.hyphen", null, null));
		List<ValueObject> valueObjectList=null;
		for (UtIndicatorEn indicator : indicatorList) {
			if(valueObjectList==null){
				valueObjectList=new ArrayList<>();
			}
			ValueObject indicators=new ValueObject();
			indicators.setKey(indicator.getShort_Name());
			indicators.setValue(indicator.getIndicator_Name());
			valueObjectList.add(indicators);
		}
		
		
		return null;
	}*/

	
	
	
	
	@Override
	public List<IusValueObject>getSubSectorAsGrid(String sectorId,Integer timePeriod,Integer projectId){
		
//		areaNid=areaRepository.getAreaNId(areaId).getArea_NId();
//		Integer sectorIdAsInt=Integer.parseInt(sectorId);
//		List<Object[]> iusList=dataRepository.getAllSubSectorValueAsNumber( sectorId, 2, 3, 2, timePeriod,projectId,sectorIdAsInt);
		List<Object[]> iusList=dataRepository.findAllSubSectorValueAsNumber(sectorId, 2, 3, 2, timePeriod, projectId);
		List<IusValueObject> iusDetails=new ArrayList<>();
		for (Object[] iusValueObject : iusList) {
			IusValueObject valueObject=new IusValueObject();
				for(Object obj : iusValueObject){
					
					if(obj instanceof Integer){
//						valueObject.setIndicatroNid(String.valueOf(obj.toString()));
						UtIndicatorEn uie=indicatorRepository.findByIndicator_NId(Integer.valueOf(obj.toString()));
						String shortName=uie.getShort_Name();
						valueObject.setIndicatroNid(shortName);
					}
					else if(obj instanceof Double){
						double dataValue=((Double) obj).doubleValue();
						valueObject.setDataValue(String.valueOf(dataValue));
						valueObject.setCssClass(dataValue<60?colourMessageSource.getMessage("colour.red", null, null):dataValue<70?colourMessageSource.getMessage("colour.yellow", null, null):colourMessageSource.getMessage("colour.green", null, null));
						
					}
					else if(obj instanceof String){
						valueObject.setIndicatorName((String)obj);
					}
					else{
						valueObject.setCssClass(colourMessageSource.getMessage("colour.grey", null, null));
					}
					
				}
				iusDetails.add(valueObject);
			}
		return iusDetails;
	}
	
	@Override
	public List<IusValueObject>getAllOperationIndicatorValue(String key,Integer unit,Integer subGroup,String areaId,Integer timePeriod,Integer projectId){
		
		areaNid=areaRepository.getAreaNId(areaId).getArea_NId();
		List<Object[]> iusList=dataRepository.findAllOperationIndicatorValue( Integer.parseInt(key.split(delimeterMessageSource.getMessage("delimeter.hyphen", null, null))[1]), unit, subGroup, areaNid, timePeriod,projectId);
		List<IusValueObject> iusDetails=new ArrayList<>();
		for (Object[] iusValueObject : iusList) {
			IusValueObject valueObject=new IusValueObject();
				for(Object obj : iusValueObject){
					
					if(obj instanceof Integer){
						valueObject.setIndicatroNid(String.valueOf(obj.toString()));
						
					}
					else if(obj instanceof Double){
						double dataValue=((Double) obj).doubleValue();
						valueObject.setDataValue(String.valueOf(dataValue));
						valueObject.setCssClass(dataValue<60?colourMessageSource.getMessage("colour.red", null, null):dataValue<70?colourMessageSource.getMessage("colour.yellow", null, null):colourMessageSource.getMessage("colour.green", null, null));
						
					}
					else if(obj instanceof String){
						valueObject.setIndicatorName((String)obj);
					}
					else{
						valueObject.setCssClass(colourMessageSource.getMessage("colour.grey", null, null));
					}
					
				}
				iusDetails.add(valueObject);
			}
		return iusDetails;
	}

/*	@Override
	public List<ValueObject> getOperationalProjects() {
		
		return null;
	}*/
	
}
