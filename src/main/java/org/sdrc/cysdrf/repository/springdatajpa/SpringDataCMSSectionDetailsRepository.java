package org.sdrc.cysdrf.repository.springdatajpa;

import java.util.List;

import org.sdrc.cysdrf.domain.SectionDetails;
import org.sdrc.cysdrf.reposiotry.CMSSectionDetailsRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface SpringDataCMSSectionDetailsRepository extends
		CMSSectionDetailsRepository, JpaRepository<SectionDetails, Integer> {

	@Override
	@Query("Select sd from SectionDetails sd")
	public List<SectionDetails> findAllSections();

	@Override
	@Modifying
	@Transactional
	@Query("UPDATE SectionDetails sd SET sd.sectionName=:sectionName ,"
			+ "sd.description=:description where sd.sectionId=:sectionId")
	public void updateSectionById(@Param("sectionName") String sectionName,
			@Param("sectionId") Integer sectionId,
			@Param("description") String description);

	@Override
	@Query("Select sd from SectionDetails sd where sd.parentId=:parentId and sd.sectionName=:sectionName")
	public SectionDetails getSectionDetailByParentIdAndSectionName(
			@Param("parentId") Integer parentId,
			@Param("sectionName") String sectionName);

}
