package com.cmlrunner.cnj.provider.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cmlrunner.cnj.model.Rate;

public interface RateRepository extends RateRepositoryCustom, MongoRepository<Rate, String> {

	List<Rate> findByJokeId(String jokeId);

	Rate findByJokeIdAndUser(String jokeId, String user);
}
