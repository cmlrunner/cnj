package com.cmlrunner.cnj.provider.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.cmlrunner.cnj.model.Rate;

public class RateRepositoryImpl implements RateRepositoryCustom {

	@Autowired
	private MongoTemplate template;

	@Override
	public void upsert(Rate rate) {
		template.upsert(Query.query(Criteria.where("jokeId").is(rate.getJokeId()).and("user").is(rate.getUser())),
				Update.update("score", rate.getScore()), Rate.class);
	}

}
