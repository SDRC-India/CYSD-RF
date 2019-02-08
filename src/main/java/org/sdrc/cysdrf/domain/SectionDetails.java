package org.sdrc.cysdrf.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "SectionDetails")
public class SectionDetails implements Serializable {

	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "sectionId")
	private Integer sectionId;

	@Column(name = "sectionName")
	private String sectionName;

	@Column(name = "description",length=320)
	private String description;

	@Column(name = "parentId")
	private Integer parentId;

	@Column(name = "type")
	private String type;

	@OneToMany(mappedBy = "sectionDetails")
	List<TagDetails> tagDetails;

	@OneToMany(mappedBy = "sectionDetails")
	List<ReportDetails> reportDetails;
	
	@Column(name = "isAlive")
	private boolean isAlive;
	

	public SectionDetails(Integer sectionId) {
		super();
		this.sectionId = sectionId;
	}

	public boolean isAlive() {
		return isAlive;
	}

	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}

	public List<TagDetails> getTagDetails() {
		return tagDetails;
	}

	public void setTagDetails(List<TagDetails> tagDetails) {
		this.tagDetails = tagDetails;
	}

	public List<ReportDetails> getReportDetails() {
		return reportDetails;
	}

	public void setReportDetails(List<ReportDetails> reportDetails) {
		this.reportDetails = reportDetails;
	}

	public Integer getSectionId() {
		return sectionId;
	}

	public void setSectionId(Integer sectionId) {
		this.sectionId = sectionId;
	}

	public String getSectionName() {
		return sectionName;
	}

	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
	
	public SectionDetails() {
		super();
		// TODO Auto-generated constructor stub
	}


	/**
	 * 
	 */
}
