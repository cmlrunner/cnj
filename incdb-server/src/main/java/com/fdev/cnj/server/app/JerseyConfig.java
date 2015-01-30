package com.fdev.cnj.server.app;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import com.fdev.cnj.server.rest.Jokes;

@Component
public class JerseyConfig extends ResourceConfig {

	public JerseyConfig() {
		super(Jokes.class);
	}
}
