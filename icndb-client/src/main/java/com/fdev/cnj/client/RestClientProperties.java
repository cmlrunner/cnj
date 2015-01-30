package com.fdev.cnj.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RestClientProperties {

	@Value("${icndb.url}")
	private String url;

	@Value("${icndb.api.path.jokes}")
	private String jokesPath;

	@Value("${icndb.api.path.categories}")
	private String categoriesPath;

	public String getUrl() {
		return url;
	}

	public String getJokesPath() {
		return jokesPath;
	}

	public String getCategoriesPath() {
		return categoriesPath;
	}

}
