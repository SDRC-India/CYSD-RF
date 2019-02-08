package org.sdrc.cysdrf.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class IUSDetail implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private int id;
	@Column(name = "PlannedIusId")
	private String plannedIusId;
	@Column(name = "AchievedIusId")
	private String achievedIusId;
	@Column(name = "AchievmentIusId")
	private String achievmentIusId;
	
	@Column(name = "SubSectorIusId")
	private String subSectorIusId;
	
	@Column(name = "IndicatorId")
	private String indicatorId;
	
	@Column(name = "UnitId")
	private String unitId;
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPlannedIusId() {
		return plannedIusId;
	}

	public void setPlannedIusId(String plannedIusId) {
		this.plannedIusId = plannedIusId;
	}

	public String getAchievedIusId() {
		return achievedIusId;
	}

	public void setAchievedIusId(String achievedIusId) {
		this.achievedIusId = achievedIusId;
	}

	public String getAchievmentIusId() {
		return achievmentIusId;
	}

	public void setAchievmentIusId(String achievmentIusId) {
		this.achievmentIusId = achievmentIusId;
	}

	public String getSubSectorIusId() {
		return subSectorIusId;
	}

	public void setSubSectorIusId(String subSectorIusId) {
		this.subSectorIusId = subSectorIusId;
	}

	public String getIndicatorId() {
		return indicatorId;
	}

	public void setIndicatorId(String indicatorId) {
		this.indicatorId = indicatorId;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	
	
	
	
}
