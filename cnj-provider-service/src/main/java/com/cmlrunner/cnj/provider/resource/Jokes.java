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
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmlrunner.cnj.model.Joke;
import com.cmlrunner.cnj.model.Rate;
import com.cmlrunner.cnj.provider.repository.JokeRepository;
import com.cmlrunner.cnj.provider.repository.RateRepository;

@Service
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
@Path("/jokes")
public class Jokes {

	private static final String CHUCK = "Chuck";
	private static final String NORRIS = "Norris";

	@Autowired
	private JokeRepository jokesRepository;

	@Autowired
	private RateRepository ratesRepository;

	@Context
	private UriInfo uriInfo;

	// @GET
	public Response jokes() {
		List<Joke> jokes = jokesRepository.findAll();
		return jokes.isEmpty() ? Response.status(Status.NO_CONTENT).build() : Response.ok().entity(jokes).build();
	}

	@GET
	@Path("/{id : \\d{1,3}}")
	public Response jokes(@PathParam("id") String id) {
		Joke joke = jokesRepository.findOne(id);
		if (joke != null) {
			joke.setRating(calculateRating(id));

			CacheControl cacheControl = new CacheControl();
			cacheControl.setMustRevalidate(true);
			cacheControl.setMaxAge(5);

			UriBuilder builder = uriInfo.getAbsolutePathBuilder().clone();

			Link selfLink = Link.fromUri(builder.build()).rel("self").build();
			Link scoreLink = Link.fromUri(builder.path("score").build()).rel("joke").build("score");

			return Response.ok().entity(joke).cacheControl(cacheControl).links(selfLink, scoreLink).build();
		}
		return Response.status(Status.NO_CONTENT).build();
	}

	@GET
	@Path("/categories")
	public Response categories() {
		List<String> categories = jokesRepository.findDistinctCategories();
		return categories.isEmpty() ? Response.status(Status.NO_CONTENT).build() : Response.ok().entity(categories).build();
	}

	@GET
	@Path("/count")
	public Response count() {
		long count = jokesRepository.count();
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

		Iterable<Joke> jokes = new ArrayList<Joke>();

		if (!limitTo.isEmpty()) {
			jokes = jokesRepository.findByCategoriesIn(limitTo);
		}
		if (!exclude.isEmpty()) {
			jokes = jokesRepository.findByCategoriesNotIn(exclude);
		}

		if (limitTo.isEmpty() && exclude.isEmpty()) {
			long jokesCount = jokesRepository.count();
			Random random = new Random();
			List<String> ids = new ArrayList<String>();
			for (int i = 0; i < count; i++) {
				ids.add(String.valueOf(random.nextInt((int) jokesCount)));
			}
			jokes = jokesRepository.findAll(ids);
		}

		if (!jokes.iterator().hasNext()) {
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

		for (Joke joke : jokes) {
			joke.setRating(calculateRating(joke.getId()));
		}

		return Response.ok().entity(jokes).build();
	}

	@Path("/{id}/score")
	public Scores rate(@PathParam("id") String id) {
		if (jokesRepository.exists(id)) {
			return new Scores(ratesRepository, id);
		}
		throw new WebApplicationException(Status.NOT_FOUND);
	}

	@GET
	@Path("/echo")
	public Response echo() {
		return Response.ok().entity("echo").build();
	}

	private List<Joke> replaceName(Iterable<Joke> jokes, Map<String, String> replacments) {
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

	private int calculateRating(String jokeId) {
		List<Rate> rates = ratesRepository.findByJokeId(jokeId);
		if (rates.isEmpty()) {
			return 0;
		}
		int scores = 0;
		for (Rate rate : rates) {
			scores += rate.getScore();
		}
		return scores / rates.size();
	}

}
