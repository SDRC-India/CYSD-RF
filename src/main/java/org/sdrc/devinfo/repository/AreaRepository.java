package org.sdrc.devinfo.repository;

import java.util.List;

import org.sdrc.devinfo.domain.UtAreaEn;

public interface AreaRepository {
	List<UtAreaEn>getAllAreaDetails();
	UtAreaEn getAreaNId(String areaId);
	UtAreaEn getAreaId(Integer areaNid);
	List<UtAreaEn> findByAreaLevel();
	List<UtAreaEn> findAll();
	
	
}
