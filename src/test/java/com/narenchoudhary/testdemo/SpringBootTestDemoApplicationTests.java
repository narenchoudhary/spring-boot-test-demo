package com.narenchoudhary.testdemo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.narenchoudhary.testdemo.httpclients.PollServiceClient;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class SpringBootTestDemoApplicationTests {

	@Autowired
	PollServiceClient pollServiceClient;
	
	@Test
	public void contextLoads() {
		assertThat(pollServiceClient).isNotNull();
	}
}