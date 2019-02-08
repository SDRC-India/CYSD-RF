package org.sdrc.devinfo.repository;

import java.util.List;

import org.sdrc.devinfo.domain.UtIndicatorClassificationsEn;
import org.springframework.transaction.annotation.Transactional;

public interface IndicatorClassificationsRepository {

//	List<UtIndicatorClassificationsEn> findByIc_Type();
	
	@Transactional
	UtIndicatorClassificationsEn save(UtIndicatorClassificationsEn utIndicatorClassificationsEn);

	List<UtIndicatorClassificationsEn> findAllProject();
	
	List<UtIndicatorClassificationsEn> findByIC_NIdIn(List<Integer> icNids);
	
	List<UtIndicatorClassificationsEn> getOperationalProjects();
}
