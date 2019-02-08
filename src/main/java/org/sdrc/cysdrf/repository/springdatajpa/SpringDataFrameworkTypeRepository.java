package org.sdrc.cysdrf.repository.springdatajpa;

import org.sdrc.cysdrf.domain.FrameworkType;
import org.sdrc.cysdrf.reposiotry.FrameworkTypeRepository;
import org.springframework.data.repository.Repository;

public interface SpringDataFrameworkTypeRepository extends FrameworkTypeRepository, Repository<FrameworkType, Integer> {

}
