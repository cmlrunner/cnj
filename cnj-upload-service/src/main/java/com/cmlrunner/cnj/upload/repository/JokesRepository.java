package com.cmlrunner.cnj.upload.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cmlrunner.cnj.model.Joke;

public interface JokesRepository extends MongoRepository<Joke, String> {

}
