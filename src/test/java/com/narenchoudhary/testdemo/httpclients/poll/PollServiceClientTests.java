package com.narenchoudhary.testdemo.httpclients.poll;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.narenchoudhary.testdemo.httpclients.analytics.AnalyticsServiceClient;
import com.narenchoudhary.testdemo.httpclients.analytics.Metric;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class PollServiceClientTests {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(PollServiceClientTests.class);
	
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	PollServiceClient pollServiceClient;
	
	@Autowired
	AnalyticsServiceClient analyticsServiceClient;
	
	MockRestServiceServer mockServer;
	
	@PostConstruct
	public void init() {
		mockServer = MockRestServiceServer.bindTo(restTemplate)
							.ignoreExpectOrder(true)
							.build();
	}
	
	@Test
	public void invokeAnalyticsServiceWhenCreateQuestionReturns200() throws JsonProcessingException {
		
		Question que = new Question("question", new Date());
		String queJson = que.generateJson();
		LOGGER.info("Question JSON: {}", queJson);
		
		// check if request to poll-service is made
		String createQueUrl = pollServiceClient.getHostname() + "/question";
		mockServer.expect(ExpectedCount.once(), requestTo(createQueUrl))
			.andExpect(header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(method(HttpMethod.POST))
			.andExpect(content().json(queJson))
			.andRespond(withSuccess());
		
		String createMetricUrl = analyticsServiceClient.getHostname() + "/metric";
		
		Metric metric = new Metric(createQueUrl + ".200", "1");
		String metricJson = metric.generateJson();
		LOGGER.info("Metric JSON: {}", metricJson);
		
		// check if request to analytics-service is made 
		mockServer.expect(ExpectedCount.once(), requestTo(createMetricUrl))
		.andExpect(header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE))
		.andExpect(method(HttpMethod.POST))
		.andExpect(content().json(metricJson))
		.andRespond(withSuccess());
		
		pollServiceClient.createQuestion(que);
		
		mockServer.verify();
	}
	
	@Test
	public void invokeAnalyticsServiceWhenCreateQuestionReturns404() throws JsonProcessingException {
		
		Question que = new Question("question", new Date());
		String queJson = que.generateJson();
		LOGGER.info("Question JSON: {}", queJson);
		
		// check if request to poll-service is made
		String createQueUrl = pollServiceClient.getHostname() + "/question";
		mockServer.expect(ExpectedCount.once(), requestTo(createQueUrl))
			.andExpect(header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(method(HttpMethod.POST))
			.andExpect(content().json(queJson))
			.andRespond(withStatus(HttpStatus.NOT_FOUND));
		
		String createMetricUrl = analyticsServiceClient.getHostname() + "/metric";
		
		Metric metric = new Metric(createQueUrl + ".404", "1");
		String metricJson = metric.generateJson();
		LOGGER.info("Metric JSON: {}", metricJson);
		
		// check if request to analytics-service is made 
		mockServer.expect(ExpectedCount.once(), requestTo(createMetricUrl))
		.andExpect(header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE))
		.andExpect(method(HttpMethod.POST))
		.andExpect(content().json(metricJson))
		.andRespond(withSuccess());
		
		pollServiceClient.createQuestion(que);
		
		mockServer.verify();
	}
}
