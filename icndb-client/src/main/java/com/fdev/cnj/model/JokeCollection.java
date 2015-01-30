package com.fdev.cnj.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JokeCollection {

	private List<Joke> value;

	public List<Joke> getValue() {
		return value;
	}

}
