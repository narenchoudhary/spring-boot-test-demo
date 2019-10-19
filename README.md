# Testing in Spring Boot

This repository covers examples of following test scenarios:

* Testing external HTTP API interaction using `MockRestServiceServer`
* Not loading problematic beans in test context
* Handling context caching issues
* What to do with `@Async` methods?
* Test Slices : Strip out beans that are not relevant to test slice


### Why MockRestServiceServer?

What is wrong with mocking `RestTemplate` calls?


### Why context caching can be a problem?

To speed up execution of tests, Spring Boot will cache context across test-classes with same configuration.

Why this can be a problem?

	@RunWith(SpringRunner.class)
	@SpringBootTest
	@ActiveProfiles("test")
	public class ServiceATests { 
		
		@Autowired
		ServiceC serviceC;
		
		@Autowired
		ServiceA serviceA;
	}
	
	
	@RunWith(SpringRunner.class)
	@SpringBootTest
	@ActiveProfiles("test")
	public class ServiceBTests { 
		
		@Mock
		ServiceC serviceC;
		
		@Autowired
		@InjectMocks
		ServiceC serviceC;
	}
	

Spring will use the same context for both test classes, but the application context will be somewhat polluted for one of these two test classes (depending on which class ran first) because of behavior of `ServiceC` object.

**Solution**: Use `@DirtiesContext` to control when context should be loaded.
