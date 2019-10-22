package com.narenchoudhary.testdemo.httpclients.analytics;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Metric representation
 * 
 * @author narenchoudhary
 *
 */
public class Metric {
	
	private String name;
	private String value;
	
	public Metric() {
		super();
	}
	
	public Metric(String name, String value) {
		super();
		this.name = name;
		this.value = value;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("name: ");
		sb.append(name);
		sb.append("; value: ");
		sb.append(value);
		
		return sb.toString();
	}
	
	public String generateJson() throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(this);
	}
}