package com.fdev.cnj.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.fdev.cnj.model.Joke;

public interface JokesRepository extends MongoRepository<Joke, String> {

}
