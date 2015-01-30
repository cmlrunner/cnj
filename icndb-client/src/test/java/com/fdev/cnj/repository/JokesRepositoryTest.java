package com.fdev.cnj.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fdev.cnj.app.IncdbClientApplication;
import com.fdev.cnj.model.Category;
import com.fdev.cnj.model.Joke;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = IncdbClientApplication.class, locations = "classpath:/META-INF/test-applicationContext.xml")
public class JokesRepositoryTest {

	@Autowired
	private JokesRepository repository;

	@Autowired
	private MongoOperations mongoOperations;

	@Test
	public void testSaveJoke() {
		List<Joke> jokes = new ArrayList<Joke>();
		jokes.add(createJoke("12", "Chuck Norris doesn't cheat death. He wins fair and square.", Arrays.asList(Category.EXPLICIT, Category.NERDY)));
		jokes.add(createJoke("14",
				"In an average living room there are 1,242 objects Chuck Norris could use to kill you, including the room itself.",
				Collections.singletonList(Category.NERDY)));
		jokes.add(createJoke("459", "Chuck Norris can solve the Towers of Hanoi in one move.", Collections.singletonList(Category.NERDY)));
		List<Joke> answer = repository.save(jokes);
		Assert.assertNotNull(answer);
	}

	@Test
	public void testFind() {
		String joke = "Chuck Norris doesn't cheat death. He wins fair and square.";
		Joke answer = repository.findOne("12");
		Assert.assertEquals(joke, answer.getJoke());
		Assert.assertEquals(Category.EXPLICIT, answer.getCategories().get(0));
		Assert.assertEquals(Category.NERDY, answer.getCategories().get(1));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testCategories() {
		List<String> categories = new ArrayList<String>();

		categories.addAll(mongoOperations.getCollection("jokes").distinct("categories"));

		Assert.assertFalse(categories.isEmpty());
	}

	@Test
	public void testFindLimitedTo() {
		Criteria criteria = Criteria.where("id").ne(null);
		criteria.and("categories").in(Category.EXPLICIT, Category.NERDY);

		Query query = Query.query(criteria);

		List<Joke> jokes = new ArrayList<Joke>();

		jokes.addAll(mongoOperations.find(query, Joke.class));

		Assert.assertFalse(jokes.isEmpty());
		System.out.println(jokes.get(0).getJoke());
	}

	private Joke createJoke(String id, String text, List<Category> categories) {
		Joke joke = new Joke(id, text);
		joke.getCategories().addAll(categories);
		return joke;
	}
}
