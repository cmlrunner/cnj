package com.cmlrunner.cnj.provider.repository;

import com.cmlrunner.cnj.model.Rate;

public interface RateRepositoryCustom {

	void upsert(Rate rate);
}
