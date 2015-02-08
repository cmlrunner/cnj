package com.cmlrunner.cnj.provider.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cmlrunner.cnj.model.Joke;

public interface JokeRepository extends JokeRepositoryCustom, MongoRepository<Joke, String> {

	List<Joke> findByCategoriesNotIn(List<String> categories);
	
	List<Joke> findByCategoriesIn(List<String> categories);

}
