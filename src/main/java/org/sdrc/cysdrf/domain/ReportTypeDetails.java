package org.sdrc.cysdrf.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="ReportTypeDetails")
public class ReportTypeDetails implements Serializable {

	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="reportTypeId")
	private Integer reportTypeId;
	
	@Column(name="reportTypeName")
	private String reportTypeName;
	
	@Column(name="description")
	private String description;
	
	@Column(name="fileType")
	private String fileType;

	public Integer getReportTypeId() {
		return reportTypeId;
	}

	public void setReportTypeId(Integer reportTypeId) {
		this.reportTypeId = reportTypeId;
	}

	public String getReportTypeName() {
		return reportTypeName;
	}

	public void setReportTypeName(String reportTypeName) {
		this.reportTypeName = reportTypeName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	
	
}
