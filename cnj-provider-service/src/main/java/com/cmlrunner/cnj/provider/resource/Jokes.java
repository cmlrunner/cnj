package com.cmlrunner.cnj.provider.resource;

import static org.springframework.util.StringUtils.hasText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.cmlrunner.cnj.model.Joke;

@Service
@Produces(MediaType.APPLICATION_JSON)
@Path("/jokes")
public class Jokes {

	private static final String CHUCK = "Chuck";
	private static final String NORRIS = "Norris";

	@Autowired
	private MongoOperations mongoOperations;

	@GET
	public Response jokes() {
		List<Joke> jokes = mongoOperations.findAll(Joke.class);
		return jokes.isEmpty() ? Response.status(Status.NO_CONTENT).build() : Response.ok().entity(jokes).build();
	}

	@GET
	@Path("/{id}")
	public Response jokes(@PathParam("id") String id) {
		Joke joke = mongoOperations.findOne(Query.query(Criteria.where("_id").is(id)), Joke.class);
		return joke == null ? Response.status(Status.NO_CONTENT).build() : Response.ok().entity(joke).build();
	}

	@SuppressWarnings("unchecked")
	@GET
	@Path("/categories")
	public Response categories() {
		List<String> categories = mongoOperations.getCollection("jokes").distinct("categories");
		return categories.isEmpty() ? Response.status(Status.NO_CONTENT).build() : Response.ok().entity(categories).build();
	}

	@GET
	@Path("/count")
	public Response count() {
		long count = mongoOperations.count(null, Jokes.class);
		return Response.ok().entity(count).build();
	}

	@GET
	@Path("/random/{count : \\d*}")
	public Response random(@PathParam("count") String requested, @QueryParam("firstName") String firstName, @QueryParam("lastName") String lastName,
			@QueryParam("limitTo") List<String> limitTo, @QueryParam("exclude") List<String> exclude) {

		int count = Integer.valueOf(hasText(requested) ? requested : "1");

		if ((!limitTo.isEmpty() && !exclude.isEmpty()) || count < 1) {
			return Response.status(Status.BAD_REQUEST).build();
		}

		Criteria criteria = Criteria.where("_id").ne(null);

		if (!limitTo.isEmpty()) {
			criteria.and("categories").in(limitTo);
		}
		if (!exclude.isEmpty()) {
			criteria.and("categories").nin(exclude);
		}

		if (!criteria.getCriteriaObject().containsField("categories")) {
			long jokesCount = mongoOperations.count(null, Jokes.class);
			Random random = new Random();
			List<String> ids = new ArrayList<String>();
			for (int i = 0; i < count; i++) {
				ids.add(String.valueOf(random.nextInt((int) jokesCount)));
			}
			criteria = Criteria.where("_id").in(ids);
		}

		Query query = Query.query(criteria);
		query.limit(count);

		List<Joke> jokes = mongoOperations.find(query, Joke.class);
		if (jokes.isEmpty()) {
			return Response.status(Status.NO_CONTENT).build();
		}

		Map<String, String> replacments = new HashMap<String, String>();
		if (hasText(firstName)) {
			replacments.put(CHUCK, firstName);
		}
		if (hasText(lastName)) {
			replacments.put(NORRIS, lastName);
		}
		if (!replacments.isEmpty()) {
			jokes = replaceName(jokes, replacments);
		}
		return Response.ok().entity(jokes).build();
	}

	@Path("/{id}/rate")
	public Rates rate(@PathParam("id") String id) {
		return new Rates(id);
	}

	@GET
	@Path("/echo")
	public Response echo() {
		return Response.ok().entity("echo").build();
	}

	private List<Joke> replaceName(List<Joke> jokes, Map<String, String> replacments) {
		List<Joke> changedJokes = new ArrayList<Joke>();
		for (Joke joke : jokes) {
			String newName = joke.getJoke();
			for (Map.Entry<String, String> entry : replacments.entrySet()) {
				newName = newName.replace(entry.getKey(), entry.getValue());
			}
			Joke newJoke = new Joke(joke.getId(), newName);
			newJoke.getCategories().addAll(joke.getCategories());
			changedJokes.add(newJoke);
		}
		return changedJokes;
	}

	// @PUT
	// @Path("/{id}")
	// @Consumes({ MediaType.APPLICATION_JSON, "text/json" })
	// public Response update(@PathParam("id") String id, Joke joke) {
	// return Response.ok(joke).build();
	// }

}
