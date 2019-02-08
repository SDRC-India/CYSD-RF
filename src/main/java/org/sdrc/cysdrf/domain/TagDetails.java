package org.sdrc.cysdrf.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="TagDetails")
public class TagDetails implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="tagId")
	private Integer tagId;
	
	@Column(name="tagName")
	private String tagName;
	
	public boolean isAlive() {
		return isAlive;
	}

	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}

	@ManyToOne
	@JoinColumn(name="SectionId")
	private SectionDetails sectionDetails;
	
	@Column(name="isAlive")
	private boolean isAlive;
	
	public Integer getTagId() {
		return tagId;
	}

	public void setTagId(Integer tagId) {
		this.tagId = tagId;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public SectionDetails getSectionDetails() {
		return sectionDetails;
	}

	public void setSectionDetails(SectionDetails sectionDetails) {
		this.sectionDetails = sectionDetails;
	}


}
