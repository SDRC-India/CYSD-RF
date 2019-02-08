package org.sdrc.cysdrf.repository.springdatajpa;

import java.util.List;

import org.sdrc.cysdrf.domain.CategoryDetail;
import org.sdrc.cysdrf.reposiotry.CategoryDetailsRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

public interface SpringDataCategoryDetailsRepository extends CategoryDetailsRepository,Repository<CategoryDetail, Integer> {

	@Override
	@Query("SELECT cd FROM CategoryDetail cd")
	List<CategoryDetail> findAll();
}
