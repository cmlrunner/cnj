package com.fdev.cnj.service;

import java.util.List;

import com.fdev.cnj.model.Joke;

public interface IncdbService {

	Joke getJoke(String id);

	List<Joke> getJokeList();

	List<Joke> getJokeRange(int count);

}
