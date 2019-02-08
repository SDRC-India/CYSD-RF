package org.sdrc.cysdrf.repository.springdatajpa;

import java.util.List;

import org.sdrc.cysdrf.domain.TagDetails;
import org.sdrc.cysdrf.reposiotry.CMSTagDetailsRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SpringDataCMSTagDetailsRepository extends
		CMSTagDetailsRepository, JpaRepository<TagDetails, Integer> {

	@Override
	@Query("Select td from TagDetails td")
	public List<TagDetails> findAllTag();

	@Override
	@Query("Select td from TagDetails td where td.sectionDetails.sectionId=:sectionId")
	public List<TagDetails> findAllTagBySectionID(
			@Param("sectionId") Integer sectionId);
}
