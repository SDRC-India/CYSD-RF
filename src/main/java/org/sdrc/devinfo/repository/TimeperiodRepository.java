package org.sdrc.devinfo.repository;

import java.util.List;

import org.sdrc.devinfo.domain.UtTimeperiod;
import org.springframework.transaction.annotation.Transactional;

public interface TimeperiodRepository {
	List<UtTimeperiod>getAllTimeperiod();
	
	@Transactional
	UtTimeperiod save(UtTimeperiod utTimeperiod);
	
	UtTimeperiod findByTimePeriodNId(Integer timePeriodNId);

	
	List<UtTimeperiod> findByPeriodicity(String periodicity);
	


	List<String> findDistinctPeriodicity();

	List<UtTimeperiod> findMaxTimePeriods();

}
