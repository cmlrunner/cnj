package com.fdev.cnj.cron;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fdev.cnj.model.Joke;
import com.fdev.cnj.repository.JokesRepository;
import com.fdev.cnj.service.IncdbService;

@Component
public class JokesUpdateTask {

	@Autowired
	private IncdbService service;

	@Autowired
	private JokesRepository repository;

	@Scheduled(cron = "${incdb.jokes.update}")
	public void update() {
		List<Joke> jokes = service.getJokeList();
		repository.save(jokes);
	}
}
