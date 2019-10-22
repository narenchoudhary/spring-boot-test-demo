package com.narenchoudhary.testdemo.httpclients.analytics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

/**
 * HTTP client for analytics service.
 * 
 * @author narenchoudhary
 *
 */
@Component
public class AnalyticsServiceClient {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(AnalyticsServiceClient.class);
	
	@Autowired
	RestTemplate restTemplate;
	
	@Value("${analytics-service.hostname}")
	private String hostname;
	
	public String getHostname() {
		return this.hostname;
	}
	
	@Async("appThreadPoolTaskExecutor")
	public void createMetric(Metric metric) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		HttpEntity<Metric> requestEntity = new HttpEntity<>(metric, headers);
		
		try {
			String url = hostname + "/metric";
			LOGGER.info("Calling {} for body: {}", url, metric);
			
			ResponseEntity<String> responseEntity = this.restTemplate.postForEntity(url, requestEntity, String.class);
			
			LOGGER.info("Status code: {}", responseEntity.getStatusCodeValue());
			LOGGER.info("Response: {}", responseEntity.getBody());
		} catch (HttpStatusCodeException e) {
			LOGGER.error("Status code: {}", e.getRawStatusCode());
			LOGGER.error("Response: {}", e.getResponseBodyAsString());
			LOGGER.error("Exception occured!", e);
		}
	}
}