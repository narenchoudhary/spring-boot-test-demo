package com.narenchoudhary.testdemo.httpclients.poll;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.narenchoudhary.testdemo.httpclients.analytics.AnalyticsServiceClient;
import com.narenchoudhary.testdemo.httpclients.analytics.Metric;

/**
 * 
 * Client of an imaginary poll-service
 * 
 * @author narenchoudhary
 *
 */
@Component
public class PollServiceClient {

	public static final Logger LOGGER = LoggerFactory.getLogger(PollServiceClient.class);
	
	@Value("${poll-service.hostname}")
	private String hostname;
	
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	AnalyticsServiceClient analyticsServiceClient;
	
	public String getHostname() {
		return this.hostname;
	}
	
	public void createQuestion(Question question) {
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		HttpEntity<Question> requestEntity = new HttpEntity<>(question, headers);
		
		int statusCode = -1;
		String url = hostname + "/question";
		
		try {			
			LOGGER.info("Calling {} for body: {}", url, question);
			
			ResponseEntity<String> responseEntity = this.restTemplate.postForEntity(url, requestEntity, String.class);
			
			statusCode = responseEntity.getStatusCodeValue();
			LOGGER.info("Status code: {}", statusCode);
			LOGGER.info("Response: {}", responseEntity.getBody());
		} catch (HttpStatusCodeException e) {
			statusCode = e.getRawStatusCode();
			LOGGER.error("Status code: {}", statusCode);
			LOGGER.error("Response: {}", e.getResponseBodyAsString());
			LOGGER.error("Exception occured!", e);
		} finally {
			String metricName = url + "." + Integer.toString(statusCode);
			Metric metric = new Metric(metricName, "1");
			analyticsServiceClient.createMetric(metric);
		}
	}
	
	public List<Question> getQuestions() {
		return Collections.emptyList();
	}
}