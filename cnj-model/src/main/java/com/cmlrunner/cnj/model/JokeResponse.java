package com.cmlrunner.cnj.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JokeResponse {

	private Joke value;

	public Joke getValue() {
		return value;
	}

}
