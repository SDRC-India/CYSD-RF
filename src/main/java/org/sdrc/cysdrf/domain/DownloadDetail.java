package org.sdrc.cysdrf.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class DownloadDetail implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="DownloadDeatailId")
	private int downloadDeatailId;
	
	@Column(name="DownloadDate")
	private Timestamp downloadDate;
	
	@Column(name="IpAddress")
	private String ipAddress;
	
	public int getDownloadDeatailId() {
		return downloadDeatailId;
	}

	public Timestamp getDownloadDate() {
		return downloadDate;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setDownloadDeatailId(int downloadDeatailId) {
		this.downloadDeatailId = downloadDeatailId;
	}

	public void setDownloadDate(Timestamp downloadDate) {
		this.downloadDate = downloadDate;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

}
