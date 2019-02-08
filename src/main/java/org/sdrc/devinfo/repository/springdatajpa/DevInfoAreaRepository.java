package org.sdrc.devinfo.repository.springdatajpa;

import java.util.List;

import org.sdrc.devinfo.domain.UtAreaEn;
import org.sdrc.devinfo.repository.AreaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface DevInfoAreaRepository extends AreaRepository,Repository<UtAreaEn, Integer> {
	
	@Override
	@Query("select ua from UtAreaEn ua where ua.area_Level=3")
	List<UtAreaEn>getAllAreaDetails();
	
	@Override
	@Query("select uae from UtAreaEn uae where uae.area_ID like :areaId")
	UtAreaEn getAreaNId(@Param("areaId")String areaId);
	
	@Override
	@Query("select uae from UtAreaEn uae where uae.area_NId=:areaNid")
	UtAreaEn getAreaId(@Param("areaNid")Integer areaNid);
	
	//select all in areaLevel(2,3)
	@Override
	@Query("SELECT uae FROM UtAreaEn uae WHERE uae.area_Level IN (2,3)")
	List<UtAreaEn> findByAreaLevel();
}
