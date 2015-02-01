package com.cmlrunner.cnj.model;

import org.springframework.util.Assert;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Joke category type.
 */
public enum Category {
	NERDY("nerdy"), EXPLICIT("explicit");

	private String value;

	private Category(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@JsonCreator
	public static Category create(@JsonProperty("category") String value) {
		Assert.hasText(value);

		for (Category category : values()) {
			if (value.equals(category.value)) {
				return category;
			}
		}
		throw new IllegalArgumentException();
	}

}
