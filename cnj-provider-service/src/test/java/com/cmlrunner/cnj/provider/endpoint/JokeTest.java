package com.cmlrunner.cnj.provider.endpoint;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import com.cmlrunner.cnj.model.Joke;
import com.cmlrunner.cnj.provider.app.CnjServerApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = CnjServerApplication.class)
@IntegrationTest("server.port=0")
@WebAppConfiguration
public class JokeTest {

	@Value("${local.server.port}")
	private int port;

	private RestTemplate restTemplate = new TestRestTemplate();

	@Test
	public void testJokeById() {
		ResponseEntity<Joke> entity = this.restTemplate.getForEntity("http://localhost:" + this.port + "/jokes/1", Joke.class);
		assertEquals(HttpStatus.OK, entity.getStatusCode());
	}

	@Test
	public void testScoreByJokeId() {
		ResponseEntity<Joke> entity = this.restTemplate.getForEntity("http://localhost:" + this.port + "/jokes/1/score", Joke.class);
		assertEquals(HttpStatus.OK, entity.getStatusCode());
	}

}
