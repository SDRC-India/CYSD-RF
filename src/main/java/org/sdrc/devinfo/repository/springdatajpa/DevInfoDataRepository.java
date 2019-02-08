package org.sdrc.devinfo.repository.springdatajpa;

import java.util.List;



import org.sdrc.devinfo.domain.UtData;
import org.sdrc.devinfo.domain.UtIndicatorClassificationsEn;
import org.sdrc.devinfo.repository.DataRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface DevInfoDataRepository extends DataRepository,Repository<UtData, Integer> {
	
	@Override
	@Query( "select utt,ud from UtData ud,UtTimeperiod utt "+
			"where ud.IUSNId in (select uii.IUSNId from UtIcIus uii where uii.IC_NId=:ic_nid) "+
			"and ud.indicator_NId=:indicator_nid and ud.timePeriod_NId in (:timeperiod_nid) " +
			"and ud.subgroup_Val_NId in(:subgroup_val_nid)"+
			"and ud.timePeriod_NId=utt.timePeriod_NId and ud.area_NId=:area_nid "+
			"and ud.source_NId=:projectId "+
			"order by ud.timePeriod_NId"
			)
	
	List<Object[]>getAllData(@Param("ic_nid") Integer ic_nid,
							 @Param("indicator_nid") Integer indicator_nid, 
							 @Param("timeperiod_nid") List<Integer> timeperiod_nid,
							 @Param("subgroup_val_nid") List<Integer> subgroup_val_nid,
							 @Param("area_nid") Integer area_nid,
							 @Param("projectId") Integer projectId);
	
			@Override
			@Query(value="select ius.Indicator_NId as c ,data.Data_Value as d ,cast(uie.Indicator_Name as varchar(1000)) as e from ut_indicator_en uie,"+
					" ut_indicator_unit_subgroup ius left join ut_data data on" +
					" ius.IUSNId = data.IUSNId"+
					" and data.Area_NId=:area"+
					" and data.TimePeriod_NId=:timePeriod"+
					" and data.Source_NId=:projectid"+
					" where ius.Unit_NId in(:unit) and ius.Subgroup_Val_NId=:subGroup"+
					" and ius.IUSNId in (select IUSNId from ut_ic_ius where ic_NId=:subSectorNid)"+
					" and ius.Indicator_NId=uie.Indicator_NId",nativeQuery=true)

			List<Object[]>getAllIndicatorValueAsNumber(
									@Param("subSectorNid")Integer subSectorNid,
									@Param("unit")Integer unit,
									@Param("subGroup")Integer subGroup,
									@Param("area")Integer area,
									@Param("timePeriod")Integer timePeriod,
									@Param("projectid")Integer projectid
									);
			
			
			@Override
			@Query("select ud from UtData ud where ud.source_NId=:projectId AND ud.timePeriod_NId=:timePeriodId AND ud.IUSNId=:subSectorKey")
			List<UtData> getChloropathData(@Param("projectId")Integer projectId,
					@Param("timePeriodId") Integer timePeriodId,
					@Param("subSectorKey")Integer subSectorKey);
			
			
			@Override
			@Query("select ud from UtData ud where ud.timePeriod_NId=:timePeriod AND ud.source_NId=:sourceNid AND ud.IUSNId=:IusNid AND ud.area_NId NOT in(2)")
			List<UtData>getProjectWiseData(@Param("timePeriod")Integer timePeriod,
											@Param("sourceNid")Integer sourceNid,
											@Param("IusNid")Integer IusNid);
			
			
			@Override
			@Query("select ud from UtData ud where ud.timePeriod_NId=:timePeriod AND ud.source_NId=:sourceNid AND ud.IUSNId=:IusNid and ud.area_NId=:areaNid")
			UtData getDataForAreaAndTimePeriodAndProject(@Param("timePeriod")Integer timePeriod,
			@Param("sourceNid")Integer sourceNid,
			@Param("areaNid")Integer areaNid,
			@Param("IusNid")Integer IusNid);
		
			
			@Override
			@Modifying
			@Query("update UtData ud set ud.data_Value=:dataValue where ud.timePeriod_NId=:timePeriod_Nid"
					+ " AND ud.IUSNId=:iusNid"
					+ " AND ud.source_NId=:sourceNid"
					+ " AND ud.area_NId=:areaId")
			void update(@Param("areaId")Integer areaId,
						@Param("timePeriod_Nid")Integer timePeriod_Nid,
						@Param("iusNid")Integer iusNid,
						@Param("sourceNid")Integer sourceNid,
						@Param("dataValue")Double dataValue);
			
//			@Override
//			@Query("select ud from UtData ud where ud.timePeriod_NId=:timePeriod AND ud.source_NId=:sourceNid AND ud.IUSNId=:IusNid and ud.area_NId=:areaNid")
//			UtData getDataForAreaAndTimePeriodAndProject(@Param("timePeriod")Integer timePeriod,
//			@Param("sourceNid")Integer sourceNid,
//			@Param("areaNid")Integer areaNid,
//			@Param("IusNid")Integer IusNid);
			
			
			@Override
			@Query("select ud from UtData ud where ud.timePeriod_NId=:timePeriod AND ud.source_NId NOT in(15) AND ud.IUSNId=:IusNid AND ud.area_NId=2")
			List<UtData>getDataExcludingTotalSource(@Param("timePeriod")Integer timePeriod,
											@Param("IusNid")Integer IusNid);
			
			@Override
			@Query("select ud.data_Value from UtData ud where ud.timePeriod_NId=:timePeriod AND ud.source_NId=:sourceNid AND ud.IUSNId in(:iusNids) AND ud.area_NId=2")
			List<Double>getSubsectroAchievementForIndividualProject(@Param("timePeriod")Integer timePeriod,
											@Param("iusNids")List<Integer> iusNids,
											@Param("sourceNid")Integer sourceNid);
			
			
			@Override
			@Query("select uice from UtIndicatorClassificationsEn uice"+
			" where uice.IC_NId in (select ud.source_NId from UtData ud where ud.area_NId in (:areaNid))")
			List<UtIndicatorClassificationsEn> getAreaSpecificProject(@Param("areaNid")Integer areaNid);
			

//Select By areaId,timeperiodNid,subvalNid,sourceNid
			
           @Override
//           @Query("select iu,ud from UtIndicatorUnitSubgroup uius,UtIndicatorEn iu,UtData ud "
//           		+ " WHERE ud.IUSNId=uius.IUSNId AND uius.indicator_NId = iu.indicator_NId "
//           		+ "AND ud.area_NId=:areaId AND ud.timePeriod_NId IN (:timeperiodNid) "
//           		+ "AND ud.subgroup_Val_NId IN (:subvalNid) AND ud.source_NId=:sourceNid order by  iu.indicator_NId")  
//         List<Object[]> getbysourceNId
//                  (@Param("areaId")Integer areaId,
//        		   @Param("timeperiodNid")int timeperiodNid,
//        		   @Param("subvalNid")int[] subvalNid,
//        		   @Param("sourceNid")Integer sourceNid);
           @Query("select iu,ud from UtIndicatorUnitSubgroup uius,UtIndicatorEn iu,UtData ud,UtIndicatorClassificationsEn uice,UtIcIus ui"
        		   +" where uice.IC_NId=ui.IC_NId"
        		   +" AND uice.IC_Info like :frameworkType"
        		   +" AND ui.IUSNId=ud.IUSNId"
        		   +" AND uice.IC_Type like 'SC'"
        		   +" AND ud.IUSNId=uius.IUSNId"
        		   +" AND uius.indicator_NId=iu.indicator_NId"
        		   + " AND ud.area_NId=:areaId AND ud.timePeriod_NId IN (:timeperiodNid) "
        		   + " AND ud.subgroup_Val_NId IN (:subvalNid) AND ud.source_NId=:sourceNid order by  iu.indicator_NId") 
           
            List<Object[]> getbysourceNId
                     (@Param("areaId")Integer areaId,
           		   @Param("timeperiodNid")int timeperiodNid,
           		   @Param("subvalNid")int[] subvalNid,
           		   @Param("sourceNid")Integer sourceNid,
           		   @Param("frameworkType")String frameworkType);
         
         
         @Override
         @Query(value="select ius.Indicator_NId as c ,data.Data_Value as d ,cast(uie.Indicator_Name as varchar(1000)) as e from ut_indicator_en uie,"+
					" ut_indicator_unit_subgroup ius left join ut_data data on" +
					" ius.IUSNId = data.IUSNId"+
					" and data.Area_NId=:area"+
					" and data.TimePeriod_NId=:timePeriod"+
					" and data.Source_NId=:projectid"+
					" where ius.Unit_NId in(:unit) and ius.Subgroup_Val_NId=:subGroup"+
					" and ius.IUSNId in (select IUSNId from ut_ic_ius ui where ui.Ic_NId like :subSectorNid)"+
					" and ius.Indicator_NId=uie.Indicator_NId"+
					" and uie.Short_Name like :subSectorNid + '%' ",nativeQuery=true)
			List<Object[]>findAllSubSectorValueAsNumber(
									@Param("subSectorNid")String subSectorNid,
									@Param("unit")Integer unit,
									@Param("subGroup")Integer subGroup,
									@Param("area")Integer area,
									@Param("timePeriod")Integer timePeriod,
									@Param("projectid")Integer projectid
									);
			
			
			@Override
			@Query(value="select uie.Indicator_NId as a,ud.Data_Value as d,uie.Indicator_NId,cast(uie.Indicator_Name as varchar(1000)) as ie"
					+ " from ut_indicator_en uie,ut_indicator_unit_subgroup ius"
					+ " left join ut_data ud on ud.IUSNId =ius.IUSNId and ud.Unit_NId=:unit and ud.TimePeriod_NId=:timePeriod and ud.Area_NId=:area and ud.Source_NId = :projectid" 
					+ " where ius.IUSNId in (select IUSNId from ut_ic_ius where IC_NId=:subSectorNid)"
					+ " and ius.Indicator_NId=uie.Indicator_NId and ius.Subgroup_Val_NId= :subGroup ",nativeQuery=true)
			List<Object[]> findAllOperationIndicatorValue(
					@Param("subSectorNid")Integer subSectorNid, 
					@Param("unit")Integer unit,
					@Param("subGroup")Integer subGroup, 
					@Param("area")Integer area, 
					@Param("timePeriod")Integer timePeriod, 
					@Param("projectid")Integer projectid);
			
			/*@Override
			@Query("select uice from UtIndicatorClassificationsEn uice where uice.IC_NId in (39)")
			List<UtIndicatorClassificationsEn> getOperationalProjects();*/
}