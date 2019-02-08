package org.sdrc.cysdrf.reposiotry;

import java.util.List;

import org.sdrc.cysdrf.domain.TagDetails;
import org.springframework.transaction.annotation.Transactional;

public interface CMSTagDetailsRepository {
	List<TagDetails> findAllTag();

	List<TagDetails> findAllTagBySectionID(Integer SectionId);
	
	@Transactional
	public <S extends TagDetails> List<S> save(Iterable<S> entities);
}
