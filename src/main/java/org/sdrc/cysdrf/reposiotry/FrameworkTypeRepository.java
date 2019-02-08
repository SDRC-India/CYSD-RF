package org.sdrc.cysdrf.reposiotry;

import java.util.List;

import org.sdrc.cysdrf.domain.FrameworkType;

public interface FrameworkTypeRepository {

	List<FrameworkType> findAll();
}
