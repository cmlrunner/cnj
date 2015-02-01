package com.cmlrunner.cnj.upload.service;

import java.util.List;

import com.cmlrunner.cnj.model.Joke;

public interface IncdbService {

	Joke getJoke(String id);

	List<Joke> getJokeList();

	List<Joke> getJokeRange(int count);

}
