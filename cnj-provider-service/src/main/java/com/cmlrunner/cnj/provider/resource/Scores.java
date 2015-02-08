package com.cmlrunner.cnj.provider.resource;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

import com.cmlrunner.cnj.model.Rate;
import com.cmlrunner.cnj.provider.repository.RateRepository;

@Produces(MediaType.APPLICATION_JSON)
public class Scores {

	private final String jokeId;
	private final RateRepository ratesRepository;

	public Scores(RateRepository ratesRepository, String jokeId) {
		this.jokeId = jokeId;
		this.ratesRepository = ratesRepository;
	}

	@GET
	public Response score(@Context HttpServletRequest request) {
		String user = resolveUserId(request);
		Rate rate = ratesRepository.findByJokeIdAndUser(jokeId, user);
		Score score = rate != null ? new Score(rate.getScore()) : new Score();

		return Response.ok().entity(score)
				.links(Link.fromUri(UriBuilder.fromResource(Jokes.class).path(jokeId).path("score").build()).rel("joke").build()).build();
	}

	@PUT
	@Consumes({ MediaType.APPLICATION_JSON, "text/json" })
	public Response updateScore(Score score, @Context HttpServletRequest request) {
		int value = score.getValue();
		if (value < 0 || value > 5) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		String user = resolveUserId(request);

		Rate rate = new Rate(jokeId, user, value);

		ratesRepository.upsert(rate);

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