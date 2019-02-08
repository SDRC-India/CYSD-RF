package org.sdrc.cysdrf.web;

import java.util.List;
import java.util.Map;

import org.sdrc.cysdrf.model.ChloropathModel;
import org.sdrc.cysdrf.model.TrendModel;
import org.sdrc.cysdrf.service.DashboardService;
import org.sdrc.cysdrf.util.AreaObject;
import org.sdrc.cysdrf.util.IusValueObject;
import org.sdrc.cysdrf.util.ValueObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DashBoardController {

	@Autowired
	private DashboardService dashBoardService;
//	@Autowired
//	private AggregationService aggregationService;
	@Autowired
	private  ResourceBundleMessageSource delimeterMessageSource;
	

	
	@Autowired
	private ResourceBundleMessageSource messageSourceNotification;

	/*
	 * 
	 * Fetching All Sector on first load of Dashboard page
	 */

	@RequestMapping(value = "/getAllSector", method = RequestMethod.GET)
	@ResponseBody
	public List<ValueObject> getAllSector(@RequestParam("frameworkType") int frameworkType) {
//		frameworkType=1;
		return dashBoardService.getAllSector(frameworkType);
	}

	/*
	 * 
	 * Fetching All Quarterly TImeperiod of corespending year
	 */

	@RequestMapping(value = "/getAllTimePeriod", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Map<String, String>> getAllTimePeriod() {
		return dashBoardService.getAllTimeperiod();
	}

	/*
	 * 
	 * Fetching All Area
	 */

	@RequestMapping(value = "/getAllAreaDetails", method = RequestMethod.GET)
	@ResponseBody
	public List<AreaObject> getAllAreaDetails() {
		return dashBoardService.getAllAreaDetails();
	}

	/*
	 * 
	 * Fetching All Sub-sector under a Sector
	 */

	@RequestMapping(value = "/getAllSubsector", method = RequestMethod.GET)
	@ResponseBody
	public List<ValueObject> getAllIndicator(
			@RequestParam("sectorNid") String sectorNid) {
		return dashBoardService.getAllSubSector(sectorNid);
	}

	/*
	 * 
	 * 
	 * @Param : 1>Key:Key is consist of
	 * "Sector Id_SubsectorId_Ius id of the Subsector" .
	 * 
	 * e.g=1-2-41
	 * 
	 * 2>Unit: For this method unit is always Percent.
	 * 
	 * 3>Subgroup: For this method subgroup is always Achieved.
	 * 
	 * 4>Area: Selected Area from Choropleth Map.
	 * 
	 * 5>TimePeriod: Selected Timeperiod.
	 * 
	 * 
	 * @Description: Fetching All Indicator Achieved value as percentage
	 */

	@RequestMapping(value = "/getAllIndicator", method = RequestMethod.POST)
	@ResponseBody
	public List<IusValueObject> getAllSubIndicator(
			@RequestParam("key") String key, @RequestParam("unit") String unit,
			@RequestParam("subgroup") String subGroup,
			@RequestParam("areaid") String areaId,
			@RequestParam("timePeriod") String timePeriod,
			@RequestParam("projectId") String projectId) {

		return dashBoardService.getAllSubIndicator(key, Integer.parseInt(unit),
				Integer.parseInt(subGroup), areaId,
				Integer.parseInt(timePeriod), Integer.parseInt(projectId));
	}
	
	@RequestMapping(value = "/getAllOperationIndicator", method = RequestMethod.POST)
	@ResponseBody
	public List<IusValueObject> getAllOperationIndicator(
			@RequestParam("key") String key, @RequestParam("unit") String unit,
			@RequestParam("subgroup") String subGroup,
			@RequestParam("areaid") String areaId,
			@RequestParam("timePeriod") String timePeriod,
			@RequestParam("projectId") String projectId) {

		return dashBoardService.getAllOperationIndicatorValue(key, Integer.parseInt(unit),
				Integer.parseInt(subGroup), areaId,
				Integer.parseInt(timePeriod), Integer.parseInt(projectId));
	}

	/*
	 * 
	 * 
	 * @Param : 1>Key:Key is consist of
	 * "Sector Id_SubsectorId_Ius id of the Subsector" .
	 * 
	 * e.g=1-2-41
	 * 
	 * 2>Unit: For this method unit is always Percent.
	 * 
	 * 3>Subgroup: For this method subgroup is always Achieved.
	 * 
	 * 4>Indicator: Selected Indicator.
	 * 
	 * 5>TimePeriod: Selected Timeperiod.
	 * 
	 * 
	 * @Description: Fetching All Indicator Achieved,Planned value as number
	 */

	@RequestMapping(value = "/getTrendValue", method = RequestMethod.POST)
	@ResponseBody
	public TrendModel getTrendValue(@RequestParam("key") String icNid,
			@RequestParam("subgroup") String subGroup,
			@RequestParam("timePeriod") String timePeriod,
			@RequestParam("indicator") String indicatorNid,
			@RequestParam("projectId") String projectId) {

		return dashBoardService.getTrendValue(
				Integer.parseInt(icNid.split("-")[1]),
				Integer.parseInt(indicatorNid),
				subGroup, timePeriod,Integer.parseInt(projectId));

	}

	/*@RequestMapping(value = "/getUtData", method = RequestMethod.GET)
	@ResponseBody
	public String doAggregate() throws Exception  {
	 
				aggregationService.doAggregate("22,24,25","IND021024", 2,"D:/CYSD_Testing_r2/Bolangir/Bolangir_P1-P3-P4.xlsx",false);
				return "hi";
//		aggregationService.saveIusDetails();
//				return null;

	}*/
	
	@RequestMapping(value = "/getCholropathData", method=RequestMethod.POST)
	@ResponseBody
	
	public List<ChloropathModel> getCholropathData(@RequestParam("projectId") String projectId,
											 @RequestParam("timePeriodId") String timePeriodId,
											 @RequestParam("subSectorKey") String subSectorKey){
		
		return dashBoardService.getCholropathData(projectId,timePeriodId,Integer.parseInt(subSectorKey.split(delimeterMessageSource.getMessage("delimeter.hyphen", null, null))[2]));
		
	}
	
	
	
	@RequestMapping(value = "/getAreaSpecificProjects" , method=RequestMethod.POST)
	@ResponseBody
	
	public List<ValueObject> getAreaSpecificProjects(@RequestParam("areaCode")String areaCode){
		return dashBoardService.getAreaSpecificProjects(areaCode);
	}
	
	/*@RequestMapping(value = "/getOperationalProjects" , method=RequestMethod.POST)
	@ResponseBody
	
	public List<ValueObject> getOperationalProjects(){
		return dashBoardService.getOperationalProjects();
	}*/
	
	@RequestMapping(value = "/getFactsheet" , method=RequestMethod.GET)
	@ResponseBody
	
	//Passing d value of Periodicity 
	
	public String downloadExcel(@RequestParam("quarter") int quarter, @RequestParam("timePeriodVal") String timePeriodVal
			, @RequestParam("frameworkType") String frameworkType) throws Exception{
//		String frameworkType="1";
//		String icType="SR"; 
		return dashBoardService.downloadExcel(quarter,timePeriodVal,frameworkType);
	}
	
	@RequestMapping(value = "/getPeriodicity")
	@ResponseBody
	
	public List<String> getPeriodicity(){
		return dashBoardService.getDistinctPeriodicty();
	}
	
	@RequestMapping("/getSubSectorAsGrid")
	@ResponseBody
	public List<IusValueObject> getSubSectorAsGrid(String sectorId,String timeperiodNid,Integer projectId){
		//timeperiodNid="1";
		return dashBoardService.getSubSectorAsGrid(sectorId,Integer.parseInt(timeperiodNid),projectId);
	}
	
/*	@RequestMapping("/uploadOperational")
	public String uploadOperational(RedirectAttributes redirectAttributes) throws IOException{
		int sourceNId=15;
		String areaCode="IND021";
		int timeperiodId=2;
		String fileName="D://CYSD_Operational//File Reading//CYSD_OP_RFW_270916_r2.xls";
		Boolean flag = true;
		String errmsg=operationalService.oprationalAggregate(sourceNId, areaCode, timeperiodId, fileName, flag);
		List<String> msgs = new ArrayList<String>();
		msgs.add(errmsg);
		redirectAttributes.addFlashAttribute("formError", msgs);
		redirectAttributes.addFlashAttribute("className",messageSourceNotification.getMessage("bootstrap.alert.success", null, null));
		return "home";
	}*/
}
