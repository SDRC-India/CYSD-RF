package org.sdrc.cysdrf.reposiotry;

import java.util.List;

import org.sdrc.cysdrf.domain.IUSDetail;

public interface IUSDetailRepository {

	List<IUSDetail> getAlIUS();

	List<String> getDistinctSubSectorIusId();

	
	void save(IUSDetail iusDetail);
}
