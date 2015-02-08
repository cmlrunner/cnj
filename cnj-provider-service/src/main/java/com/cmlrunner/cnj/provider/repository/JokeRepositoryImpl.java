package com.cmlrunner.cnj.provider.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;

public class JokeRepositoryImpl implements JokeRepositoryCustom {

	@Autowired
	private MongoOperations mongoOperations;

	@Override
	@SuppressWarnings("unchecked")
	public List<String> findDistinctCategories() {
		return mongoOperations.getCollection("jokes").distinct("categories");
	}

}
