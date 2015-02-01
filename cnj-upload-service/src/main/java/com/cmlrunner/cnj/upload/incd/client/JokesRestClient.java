package com.cmlrunner.cnj.upload.incd.client;

import java.util.ArrayList;
import java.util.List;

import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cmlrunner.cnj.model.Joke;
import com.cmlrunner.cnj.model.JokeCollection;
import com.cmlrunner.cnj.model.JokeResponse;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

@Component
public class JokesRestClient {

	private final RestClientProperties clientProperties;
	private final List<Object> providers = new ArrayList<Object>();

	@Autowired
	public JokesRestClient(RestClientProperties clientProperties) {
		this.clientProperties = clientProperties;
		providers.add(new JacksonJaxbJsonProvider());
	}

	public Joke getJokeResponse(String id) {
		WebClient client = createWebClient(clientProperties.getUrl()).accept("application/json").type("application/json")
				.path(clientProperties.getJokesPath()).path("/" + id);

		JokeResponse joke = client.get(JokeResponse.class);
		return joke.getValue();
	}

	public List<Joke> getJokeResponseList() {
		WebClient client = createWebClient(clientProperties.getUrl()).accept("application/json").type("application/json")
				.path(clientProperties.getJokesPath());

		return getJokeList(client);
	}

	public List<Joke> getJokeResponseList(int count) {
		WebClient client = createWebClient(clientProperties.getUrl()).accept("application/json").type("application/json")
				.path(clientProperties.getJokesPath()).path("/random/" + count);

		return getJokeList(client);
	}

	private List<Joke> getJokeList(WebClient client) {
		JokeCollection jokes = client.get(JokeCollection.class);
		return jokes.getValue();
	}

	private WebClient createWebClient(String uri) {
		return WebClient.create(uri, providers);
	}
}
