package org.sdrc.cysdrf.repository.springdatajpa;

import java.util.List;

import org.sdrc.cysdrf.domain.IUSDetail;
import org.sdrc.cysdrf.reposiotry.IUSDetailRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

public interface SpringDataIUSDetailRepository extends IUSDetailRepository,
		Repository<IUSDetail, Integer> {

	@Override
	@Query("select id from IUSDetail id")
	List<IUSDetail> getAlIUS();
	
	@Override
	@Query("Select distinct id.subSectorIusId from IUSDetail id")
	List<String> getDistinctSubSectorIusId();
	
}
