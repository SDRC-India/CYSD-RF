package org.sdrc.cysdrf.reposiotry;

import java.util.List;

import org.sdrc.cysdrf.domain.SectionDetails;
import org.springframework.transaction.annotation.Transactional;

public interface CMSSectionDetailsRepository {

	public List<SectionDetails> findAllSections();
	
	public void updateSectionById(String sectionName, Integer sectionId,String description);

	@Transactional
	public  SectionDetails save(SectionDetails sectionDetails);
	
	public SectionDetails getSectionDetailByParentIdAndSectionName(Integer parentId,String sectionName);
}
