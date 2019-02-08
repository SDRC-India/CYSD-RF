package org.sdrc.devinfo.repository;

import java.util.List;



import org.sdrc.devinfo.domain.UtData;
import org.sdrc.devinfo.domain.UtIndicatorClassificationsEn;

public interface DataRepository {
//	@Transactional
	void  save(UtData utData);
	List<Object[]> getAllData(Integer ic_nid, Integer indicator_nid,List<Integer> timeperiod_nid,List<Integer> subgroup_val_nid,Integer area_nid, Integer projectId);

	List<Object[]> getAllIndicatorValueAsNumber(Integer subSectorNid, Integer unit,
			Integer subGroup, Integer area, Integer timePeriod, Integer projectid);
	
	List<UtData> getChloropathData(Integer projectId,Integer timePeriodId,Integer subSectorKey);

	
	void update(Integer areaId,Integer timePeriod_Nid,Integer iusNid,Integer sourceNid,Double dataValue);
	
	List<UtData>getProjectWiseData(Integer timePeriod,Integer sourceNid,Integer IusNid);
	
	UtData getDataForAreaAndTimePeriodAndProject(Integer timePeriod,Integer sourceNid,Integer areaNid,Integer iusNid);
	
	List<UtData> getDataExcludingTotalSource(Integer timePeriod,
			 Integer IusNid);
	List<Double> getSubsectroAchievementForIndividualProject(
			Integer timePeriod, List<Integer> iusNids, Integer sourceNid);
	List<UtIndicatorClassificationsEn> getAreaSpecificProject(Integer areaNid);
	
	List<Object[]> getbysourceNId(Integer areaId,int timeperiodNid,int[] subvalNid,Integer sourceNid,String frameworkType);
	
	List<Object[]> findAllSubSectorValueAsNumber(String subSectorNid,
			Integer unit, Integer subGroup, Integer area, Integer timePeriod,
			Integer projectid);
	
	List<Object[]> findAllOperationIndicatorValue(Integer subSectorNid, Integer unit,
			Integer subGroup, Integer area, Integer timePeriod, Integer projectid);
	
//	List<UtIndicatorClassificationsEn> getOperationalProjects();
}
