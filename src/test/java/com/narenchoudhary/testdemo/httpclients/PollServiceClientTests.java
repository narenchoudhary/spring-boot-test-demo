package com.narenchoudhary.testdemo.httpclients;

import static org.junit.Assert.fail;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class PollServiceClientTests {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(PollServiceClientTests.class);
	
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	PollServiceClient pollServiceClient;
	
	MockRestServiceServer mockServer;
	
	@PostConstruct
	public void init() {
		mockServer = MockRestServiceServer.createServer(restTemplate);
	}
	
	@Test
	public void testRequestAttributesForCreateQuestion() {
		
		Question que = new Question("question", new Date());
		String queStr;
		try {
			queStr = que.generateJson();
			LOGGER.info("Question: {}", queStr);
		} catch (JsonProcessingException e) {
			fail();
			return;
		}
		
		mockServer.expect(ExpectedCount.once(), requestTo(pollServiceClient.getHostname() + "/create"))
			.andExpect(header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(method(HttpMethod.POST))
			.andExpect(content().json(queStr))
			.andRespond(withSuccess());
		
		pollServiceClient.createQuestion(que);
		
		mockServer.verify();
	}
}
