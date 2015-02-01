package com.cmlrunner.cnj.provider.resource;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.data.mongodb.core.MongoOperations;

import com.cmlrunner.cnj.model.Rate;

@Produces(MediaType.APPLICATION_JSON)
public class Rates {

	private final MongoOperations mongoOperations;
	private final String jokeId;

	public Rates(MongoOperations mongoOperations, String jokeId) {
		this.jokeId = jokeId;
		this.mongoOperations = mongoOperations;
	}

	@GET
	public String rate(@Context HttpServletRequest request) {
		// Joke joke =
		// mongoOperations.findOne(Query.query(Criteria.where("_id").is(id)),
		// Joke.class);
		//
		// String userId = resolveUserId(request);
		// Rate rate =
		// mongoOperations.findOne(Query.query(Criteria.where("joke").is(id).and("user").is(userId)),
		// Rate.class);
		// joke.setScore(rate.getScore());
		//
		// return joke == null ? Response.status(Status.NO_CONTENT).build() :
		// Response.ok().entity(joke).build();

		return "rates " + jokeId + " " + resolveUserId(request);
	}

	@PUT
	@Consumes({ MediaType.APPLICATION_JSON, "text/json" })
	public Response updateRate(Rate rate, @Context HttpServletRequest request) {
		rate.setUser(resolveUserId(request));
		rate.setId(jokeId);
		mongoOperations.save(rate);
		return Response.ok(rate).build();
	}

	private String resolveUserId(HttpServletRequest request) {
		return getClientIpAddr(request);
	}

	private String getClientIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
}
