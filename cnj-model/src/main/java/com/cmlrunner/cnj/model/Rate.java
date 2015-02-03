package com.cmlrunner.cnj.model;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "rates")
public class Rate {

	private String jokeId;
	private String user;
	private int score;

	public Rate(){}
	
	public Rate(String jokeId, String user, int score) {
		this.jokeId = jokeId;
		this.user = user;
		this.score = score;
	}
	
	public String getJokeId() {
		return jokeId;
	}

	public String getUser() {
		return user;
	}
	
	public int getScore() {
		return score;
	}

}
