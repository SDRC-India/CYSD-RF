package org.sdrc.cysdrf.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table
public class UserLoginMeta implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="UserLogInMetaId")
	private long userLogInMetaId;
	
	@Column(name="LoggedInDateTime")
	private Timestamp loggedInDateTime;
	
	@Column(name="LoggedOutDateTime")
	private Timestamp loggedOutDateTime;
	
	@Column(name="UserIpAddress")
	private String userIpAddress;
	
	@Column(name="UserAgent")
	private String userAgent;
	
	@Column(name="IsAlive")
	private boolean isAlive;
	
	@ManyToOne
	@JoinColumn(name="UserId")
	private UserDetail userDetail;
	
	public long getUserLogInMetaId() {
		return userLogInMetaId;
	}

	public Timestamp getLoggedInDateTime() {
		return loggedInDateTime;
	}

	public Timestamp getLoggedOutDateTime() {
		return loggedOutDateTime;
	}

	public String getUserIpAddress() {
		return userIpAddress;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public boolean isAlive() {
		return isAlive;
	}

	public UserDetail getUserDetail() {
		return userDetail;
	}

	public void setUserLogInMetaId(long userLogInMetaId) {
		this.userLogInMetaId = userLogInMetaId;
	}

	public void setLoggedInDateTime(Timestamp loggedInDateTime) {
		this.loggedInDateTime = loggedInDateTime;
	}

	public void setLoggedOutDateTime(Timestamp loggedOutDateTime) {
		this.loggedOutDateTime = loggedOutDateTime;
	}

	public void setUserIpAddress(String userIpAddress) {
		this.userIpAddress = userIpAddress;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}

	public void setUserDetail(UserDetail userDetail) {
		this.userDetail = userDetail;
	}

	public UserLoginMeta(long userLogInMetaId) {
		super();
		this.userLogInMetaId = userLogInMetaId;
	}

	public UserLoginMeta() {
		super();
	}

}
