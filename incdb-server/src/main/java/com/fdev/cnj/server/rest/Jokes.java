package com.fdev.cnj.server.rest;

import java.util.ArrayList;
import java.util.List;

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

import com.fdev.cnj.model.Joke;
import com.fdev.cnj.repository.JokesRepository;

@Path("/jokes")
public class Jokes {

	@Autowired
	private JokesRepository repository;

	@Autowired
	private MongoOperations mongoOperations;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response jokes() {
		List<Joke> jokes = repository.findAll();
		return Response.ok().entity(jokes).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id}")
	public Response jokes(@PathParam("id") String id) {
		Joke joke = repository.findOne(id);
		return Response.ok().entity(joke).build();
	}

	@SuppressWarnings("unchecked")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/categories")
	public Response categories() {
		List<String> categories = new ArrayList<String>();
		categories.addAll(mongoOperations.getCollection("jokes").distinct("categories"));

		return Response.ok().entity(categories).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/count")
	public Response count() {
		long count = repository.count();
		return Response.ok().entity(count).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/random")
	public Response random(@QueryParam("firstName") String firstName, @QueryParam("lastName") String lastName,
			@QueryParam("limitTo") List<String> limitTo, @QueryParam("exclude") List<String> exclude) {

		if (!limitTo.isEmpty() && !exclude.isEmpty()) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		Criteria criteria = Criteria.where("id").ne(null);

		if (!limitTo.isEmpty()) {
			criteria.and("categories").in(limitTo);
		}

		if (!exclude.isEmpty()) {
			criteria.and("categories").nin(exclude);
		}

		Query query = Query.query(criteria);
		if (criteria.getCriteriaObject().keySet().size() == 1) {
			query.limit(1);
		}

		List<Joke> jokes = new ArrayList<Joke>();
		jokes.addAll(mongoOperations.find(query, Joke.class));

		return jokes.isEmpty() ? Response.status(Status.NO_CONTENT).build() : Response.ok().entity(jokes).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/echo")
	public Response echo() {
		return Response.ok().entity("echo").build();
	}

}
