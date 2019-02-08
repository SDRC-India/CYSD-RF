package org.sdrc.devinfo.repository.springdatajpa;

import java.util.List;

import org.sdrc.devinfo.domain.UtIndicatorClassificationsEn;
import org.sdrc.devinfo.repository.SectorReposiotory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface DevInfoSectorReposiotory extends SectorReposiotory,
		Repository<UtIndicatorClassificationsEn, Integer> {

	@Override
	@Query("Select uice from UtIndicatorClassificationsEn uice where uice.IC_Parent_NId=-1 and uice.IC_Info Like :frameworkType order by uice.IC_Order")
	List<UtIndicatorClassificationsEn>getAllSector(@Param("frameworkType")String frameworkType);
	
}
