package com.cmlrunner.cnj.model;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "rates")
public class Rate {

	private String id;
	private String user;
	private int score;

	public Rate(){}
	
	public Rate(String id, String user, int score) {
		this.id = id;
		this.user = user;
		this.score = score;
	}
	
	public String getId() {
		return id;
	}

	public String getUser() {
		return user;
	}
	
	public int getScore() {
		return score;
	}

}
