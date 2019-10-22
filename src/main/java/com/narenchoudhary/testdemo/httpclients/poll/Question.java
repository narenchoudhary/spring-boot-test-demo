package com.narenchoudhary.testdemo.httpclients.poll;

import java.util.Date;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Question {
	
	private String question;
	
	private Date publicationDate;
	
	public Question() {
		super();
	}
	
	public Question(String question, Date publicationDate) {
		super();
		this.question = question;
		this.publicationDate = publicationDate;
	}
	
	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
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
		sb.append("question: ");
		sb.append(question);
		sb.append("; publicationDate: ");
		sb.append(publicationDate);
		return sb.toString();
	}
	
	public String generateJson() throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(this);
	}
}
