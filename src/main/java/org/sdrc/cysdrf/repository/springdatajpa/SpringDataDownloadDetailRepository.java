package org.sdrc.cysdrf.repository.springdatajpa;

import org.sdrc.cysdrf.domain.DownloadDetail;
import org.sdrc.cysdrf.reposiotry.DownloadDetailRepository;
import org.springframework.data.repository.Repository;

public interface SpringDataDownloadDetailRepository extends DownloadDetailRepository, Repository<DownloadDetail, Integer> {

}
