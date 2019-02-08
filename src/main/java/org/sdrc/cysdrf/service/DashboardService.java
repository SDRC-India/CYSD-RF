package org.sdrc.cysdrf.service;

import java.util.List;
import java.util.Map;

import org.sdrc.cysdrf.model.ChloropathModel;
import org.sdrc.cysdrf.model.TrendModel;
import org.sdrc.cysdrf.util.AreaObject;
import org.sdrc.cysdrf.util.IusValueObject;
import org.sdrc.cysdrf.util.ValueObject;



public interface DashboardService {
	List<ValueObject>getAllSector(int frameworkType);

	List<ValueObject> getAllSubSector(String sectorNid);

	Map<String, Map<String, String>>getAllTimeperiod();

	List<AreaObject>getAllAreaDetails();

	TrendModel getTrendValue(Integer icNid, Integer indicatorNid,
			String subGroupNid, String timePeriod,Integer projectId);

	List<IusValueObject> getAllSubIndicator(String key, Integer unit,
			Integer subGroup, String areaId, Integer timePeriod,
			Integer projectId);
	
	List<ChloropathModel> getCholropathData(String projectId, String timePeriodId, Integer subSectorKey);

	List<ValueObject> getAreaSpecificProjects(String areaCode);

	List<String> getDistinctPeriodicty();

	String downloadExcel(int periodicity, String timePeriodVal,String frameworkType) throws Exception;

	List<IusValueObject> getSubSectorAsGrid(String sectorId,
			Integer timePeriod, Integer projectId);
	
	List<IusValueObject> getAllOperationIndicatorValue(String key, Integer unit,
			Integer subGroup, String areaId, Integer timePeriod,
			Integer projectId);
	
//	List<ValueObject> getOperationalProjects();
}
