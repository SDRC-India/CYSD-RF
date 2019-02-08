package org.sdrc.devinfo.repository.springdatajpa;

import java.util.List;

import org.sdrc.devinfo.domain.UtIndicatorEn;
import org.sdrc.devinfo.repository.IndicatorRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface DevInfoIndicatorReposiotry extends
		Repository<UtIndicatorEn, Integer>, IndicatorRepository {

@Override
@Query("Select uie from UtIndicatorEn uie where uie.short_Name like :sectorId||'%' ")
List<UtIndicatorEn> findByshort_Name(@Param("sectorId")String sectorId);

@Override
@Query("Select uie from UtIndicatorEn uie where uie.indicator_NId like :indicatorNid")
UtIndicatorEn findByIndicator_NId(@Param("indicatorNid")Integer indicatorNid);
}
