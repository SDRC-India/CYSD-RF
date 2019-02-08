package org.sdrc.cysdrf.util;

public class ValueObject {
	private String key;
	private String value;
	private String description;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ValueObject(String key, String value) {
		super();
		this.key = key;
		this.value = value;
	}

	public ValueObject() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "ValueObject [key=" + key + ", value=" + value
				+ ", description=" + description + "]";
	}


}
