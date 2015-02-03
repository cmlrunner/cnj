package com.cmlrunner.cnj.provider.resource;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.cmlrunner.cnj.model.Rate;

@Produces(MediaType.APPLICATION_JSON)
public class Scores {

	private final MongoOperations mongoOperations;
	private final String jokeId;

	public Scores(MongoOperations mongoOperations, String jokeId) {
		this.jokeId = jokeId;
		this.mongoOperations = mongoOperations;
	}

	@GET
	public Response score(@Context HttpServletRequest request) {
		String user = resolveUserId(request);
		Rate rate = mongoOperations.findOne(Query.query(Criteria.where("jokeId").is(jokeId).and("user").is(user)), Rate.class);
		if (rate != null) {
			return Response.ok().entity(new Score(rate.getScore())).build();
		} else {
			return Response.ok().entity(new Score()).build();
		}
	}

	@PUT
	@Consumes({ MediaType.APPLICATION_JSON, "text/json" })
	public Response updateScore(Score score, @Context HttpServletRequest request) {
		int value = score.getValue();
		if (value < 0 || value > 5) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		String user = resolveUserId(request);
		mongoOperations.upsert(Query.query(Criteria.where("jokeId").is(jokeId).and("user").is(user)), Update.update("score", value), Rate.class);

		return Response.ok(score).build();
	}

	public static class Score {

		private int value;

		public Score() {
		}

		public Score(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
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