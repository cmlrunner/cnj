package com.fdev.cnj.service;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fdev.cnj.app.IncdbClientApplication;
import com.fdev.cnj.model.Joke;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = IncdbClientApplication.class)
public class IncdbServiceTest {

	@Autowired
	private IncdbService service;

	@Test
	public void testFindJokeById() {
		String id = "563";
		Joke answer = service.getJoke(id);
		Assert.assertNotNull(answer);
		Assert.assertEquals(id, answer.getId());
	}

	@Test
	public void testLoadRange() {
		int count = 3;
		List<Joke> answer = service.getJokeRange(count);
		for (Joke joke : answer) {
			Assert.assertNotNull(joke.getJoke());
		}
		Assert.assertEquals(count, answer.size());
	}

	@Test
	public void testLoad() {
		List<Joke> answer = service.getJokeList();
		Assert.assertTrue(answer.size() > 0);
		for (Joke joke : answer) {
			Assert.assertNotNull(joke.getJoke());
		}
	}
}
