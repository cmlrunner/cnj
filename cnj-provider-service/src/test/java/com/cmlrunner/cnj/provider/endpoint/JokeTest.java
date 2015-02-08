package com.cmlrunner.cnj.provider.endpoint;

import static com.lordofthejars.nosqlunit.mongodb.MongoDbRule.MongoDbRuleBuilder.newMongoDbRule;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import com.cmlrunner.cnj.model.Joke;
import com.cmlrunner.cnj.model.Rate;
import com.cmlrunner.cnj.provider.app.CnjServerApplication;
import com.cmlrunner.cnj.provider.repository.JokeRepository;
import com.cmlrunner.cnj.provider.repository.RateRepository;
import com.cmlrunner.cnj.provider.resource.Scores.Score;
import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.mongodb.MongoDbRule;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = CnjServerApplication.class, locations = "classpath:/test-applicationContext.xml")
@IntegrationTest("server.port=0")
@PropertySource("classpath:application.properties")
@WebAppConfiguration
public class JokeTest {

	@Value("${local.server.port}")
	private int port;

	private RestTemplate restTemplate = new TestRestTemplate();

	@Rule
	public MongoDbRule mongoDbRule = newMongoDbRule().defaultSpringMongoDb("test");

	@Autowired
	private JokeRepository jokeRepository;

	@Autowired
	private RateRepository rateRepository;

	// nosql-unit requirement
	@Autowired
	private ApplicationContext applicationContext;

	@Test
	@UsingDataSet(locations = { "/testData/jokes.json" })
	public void testJokeById() {
		String path = "/jokes/1";
		ResponseEntity<Joke> entity = executeQuery(path, Joke.class);
		assertEquals(HttpStatus.OK, entity.getStatusCode());
		Assert.assertEquals("joke text1", entity.getBody().getJoke());
	}

	@Test
	@UsingDataSet(locations = { "/testData/jokes.json", "/testData/rates.json" })
	public void testScoreByJokeId() {
		String path = "/jokes/1/score";
		ResponseEntity<Score> entity = executeQuery(path, Score.class);
		assertEquals(HttpStatus.OK, entity.getStatusCode());
		Assert.assertEquals(2, entity.getBody().getValue());
	}

	@Test
	@UsingDataSet(locations = { "/testData/jokes.json", "/testData/rates.json" })
	public void testUpdateScore() {
		Rate rate = rateRepository.findOne("1");
		Assert.assertEquals(2, rate.getScore());

		String path = "http://localhost:" + this.port + "/jokes/1/score";

		restTemplate.put(path, new Score(1));

		rate = rateRepository.findOne("1");
		Assert.assertEquals(1, rate.getScore());
	}

	@Test
	@UsingDataSet(locations = { "/testData/jokes.json" })
	@SuppressWarnings("rawtypes")
	public void testCategories() {
		String path = "/jokes/categories";
		ResponseEntity<List> entity = executeQuery(path, List.class);
		assertEquals(HttpStatus.OK, entity.getStatusCode());
		Assert.assertEquals(2, entity.getBody().size());
	}

	@Test
	@UsingDataSet(locations = { "/testData/jokes.json" })
	@SuppressWarnings("rawtypes")
	public void testRandom() {
		String path = "/jokes/random/1";
		ResponseEntity<List> entity = executeQuery(path, List.class);
		assertEquals(HttpStatus.OK, entity.getStatusCode());
	}

	@Test
	@UsingDataSet(locations = { "/testData/jokes.json" })
	@SuppressWarnings("rawtypes")
	public void testRandomExcludeCategory() {
		String path = "/jokes/random/1?exclude=explicit,foo";
		ResponseEntity<List> entity = executeQuery(path, List.class);
		assertEquals(HttpStatus.OK, entity.getStatusCode());
		Assert.assertEquals(2, entity.getBody().size());
	}

	@Test
	@UsingDataSet(locations = { "/testData/jokes.json" })
	@SuppressWarnings("rawtypes")
	public void testRandomLimitToCategory() {
		String path = "/jokes/random/1?limitTo=explicit";
		ResponseEntity<List> entity = executeQuery(path, List.class);
		assertEquals(HttpStatus.OK, entity.getStatusCode());
		Assert.assertEquals(1, entity.getBody().size());
	}

	private <T> ResponseEntity<T> executeQuery(String path, Class<T> responseType) {
		return restTemplate.getForEntity("http://localhost:" + this.port + path, responseType);
	}

}
