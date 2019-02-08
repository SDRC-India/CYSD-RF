package org.sdrc.devinfo.repository.springdatajpa;

import org.sdrc.devinfo.domain.UtIcIus;
import org.sdrc.devinfo.repository.IndicatorUnitSubgroupRepository;
import org.springframework.data.repository.Repository;

public interface DevInfoIndicatorUnitSubgroupRepository extends IndicatorUnitSubgroupRepository,Repository<UtIcIus, Integer> {
	
	
}
