# Testing in Spring Boot

This repository covers examples of following test scenarios:

* Testing external HTTP API interaction using `MockRestServiceServer`
* Not loading problematic beans in test context
* Handling context caching issues
* What to do with `@Async` methods?
* Test Slices : Strip out beans that are not relevant to test slice


### Why MockRestServiceServer?

What is wrong with mocking `RestTemplate` calls?


### What to do with `@Async` methods?

Execution of `@Async` annotated-methods is controlled by JVM scheduler. It is very much possible that **verification** step in tests for such method can run before the method actually executes. As a result, the test will fail.

One heuristic is to put `Thread.sleep()` with a big time sleep time. This again can fail in some cases when there are too many tests, or when `Async` annotated method is taking too much time to complete.

For example:

	Thread.sleep(10000);
	Mockito.verify(mockBean, times(1)).doSomething();

One sure-fire way is to disable `Async` behavior in tests. This can be done by restricting loading `Executor` bean in context using `Profile` annotation.

	@Configuration
	@EnableAsync
	@Profile("!test")
	public class MyAsyncConfiguration() {
		
		@Bean("taskExecutor")
     	public Executor getExecutor() {
         		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
         		...
         		...
         		return executor;
     	}
	}

	
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
