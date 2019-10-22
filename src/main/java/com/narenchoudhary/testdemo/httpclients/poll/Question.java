package com.narenchoudhary.testdemo.httpclients.poll;

import java.util.Date;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Question {
	
	private String text;
	
	private Date publicationDate;
	
	public Question() {
		super();
	}
	
	public Question(String question, Date publicationDate) {
		super();
		this.text = question;
		this.publicationDate = publicationDate;
	}
	
	public String getText() {
		return text;
	}

	public void setText(String question) {
		this.text = question;
	}

	public Date getPublicationDate() {
		return publicationDate;
	}

	public void setPublicationDate(Date publicationDate) {
		this.publicationDate = publicationDate;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("text: ");
		sb.append(text);
		sb.append("; publicationDate: ");
		sb.append(publicationDate);
		return sb.toString();
	}
	
	public String generateJson() throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(this);
	}
}
