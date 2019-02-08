package org.sdrc.devinfo.repository;

import java.util.List;

import org.sdrc.devinfo.domain.UtIndicatorEn;

public interface IndicatorRepository {

	List<UtIndicatorEn> findByshort_Name(String sectorId);
	
	UtIndicatorEn findByIndicator_NId(Integer indicatorNid);
	
}
