package org.sdrc.cysdrf.reposiotry;

import java.util.List;

import org.sdrc.cysdrf.domain.CategoryDetail;

public interface CategoryDetailsRepository {

	List<CategoryDetail> findAll();

}
