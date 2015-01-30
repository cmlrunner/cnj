package com.fdev.cnj.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "jokes")
public class Joke {

	private String id;
	private String joke;
	private List<Category> categories = new ArrayList<Category>();

	public Joke() {
	}

	public Joke(String id, String joke) {
		this.id = id;
		this.joke = joke;
	}

	public String getId() {
		return id;
	}

	public String getJoke() {
		return joke;
	}

	public List<Category> getCategories() {
		return categories;
	}
}
