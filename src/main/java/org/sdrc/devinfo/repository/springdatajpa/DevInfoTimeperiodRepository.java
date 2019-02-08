package org.sdrc.devinfo.repository.springdatajpa;

import java.util.List;

import org.sdrc.devinfo.domain.UtTimeperiod;
import org.sdrc.devinfo.repository.TimeperiodRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface DevInfoTimeperiodRepository extends TimeperiodRepository,Repository<UtTimeperiod, Integer> {

	@Override
	@Query("Select ut from UtTimeperiod ut")
	List<UtTimeperiod>getAllTimeperiod();
	
	@Override
	@Query("SELECT time FROM UtTimeperiod time WHERE time.timePeriod_NId = :timePeriodNId")
	UtTimeperiod findByTimePeriodNId(@Param("timePeriodNId")Integer timePeriodNId);
	//select All where  parrameterised periodicity
	@Override
	@Query("select utnid from UtTimeperiod utnid where utnid.periodicity=:periodicity")
	List<UtTimeperiod> findByPeriodicity(@Param("periodicity")String periodicity);
	

	@Override
	@Query("SELECT DISTINCT time.periodicity FROM UtTimeperiod time")
	List<String> findDistinctPeriodicity();

	@Override
	@Query(value=("SELECT TOP(2) * FROM ut_timeperiod utime "
			+ "ORDER BY utime.timePeriod_NId DESC"),nativeQuery=true)
	public List<UtTimeperiod> findMaxTimePeriods();
}
