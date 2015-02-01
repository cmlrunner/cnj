package com.cmlrunner.cnj.upload.job;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.cmlrunner.cnj.model.Joke;
import com.cmlrunner.cnj.upload.repository.JokesRepository;
import com.cmlrunner.cnj.upload.service.IncdbService;

@Component
public class JokesUpdateTask {

	@Autowired
	private IncdbService service;

	@Autowired
	private JokesRepository repository;

	@Scheduled(cron = "${cnj.jokes.update}")
	public void update() {
		List<Joke> jokes = service.getJokeList();
		repository.save(jokes);
	}
}
