package org.sdrc.devinfo.repository.springdatajpa;

import java.util.List;

import org.sdrc.devinfo.domain.UtIndicatorClassificationsEn;
import org.sdrc.devinfo.repository.IndicatorClassificationsRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface DevInfoIndicatorClassificationsRepository
		extends IndicatorClassificationsRepository, Repository<UtIndicatorClassificationsEn, Integer> {
/*	@Override
	@Query("SELECT utin FROM UtIndicatorClassificationsEn utin WHERE ut.IC_Type LIKE 'PR%'")
	List<UtIndicatorClassificationsEn> findByIc_Type();*/
	
	@Override
	@Query("SELECT utin FROM UtIndicatorClassificationsEn utin where utin.IC_Parent_NId in(38,39) ")
//	@Query("SELECT ut FROM UtIndicatorClassificationsEn ut WHERE ut.IC_Type LIKE 'PR_%'")
	List<UtIndicatorClassificationsEn> findAllProject();
	
	@Override
	@Query("SELECT utin FROM UtIndicatorClassificationsEn utin WHERE utin.IC_NId IN (:icNids)")
	List<UtIndicatorClassificationsEn> findByIC_NIdIn(@Param("icNids") List<Integer> icNids);
	
	@Override
	@Query("select uice from UtIndicatorClassificationsEn uice where uice.IC_NId in (39)")
	List<UtIndicatorClassificationsEn> getOperationalProjects();
	
	
	
	
}
