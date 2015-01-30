package com.fdev.cnj.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdev.cnj.client.JokesRestClient;
import com.fdev.cnj.model.Joke;
import com.fdev.cnj.service.IncdbService;

@Service
public class IncdbServiceImpl implements IncdbService {

	@Autowired
	private JokesRestClient restClient;

	@Override
	public Joke getJoke(String id) {
		return restClient.getJokeResponse(id);
	}

	@Override
	public List<Joke> getJokeList() {
		return restClient.getJokeResponseList();
	}

	@Override
	public List<Joke> getJokeRange(int count) {
		return restClient.getJokeResponseList(count);
	}

}
