package org.sdrc.devinfo.repository;

import java.util.List;

import org.sdrc.devinfo.domain.UtIndicatorClassificationsEn;

public interface SectorReposiotory {
	List<UtIndicatorClassificationsEn>getAllSector(String frameworkType);
}
