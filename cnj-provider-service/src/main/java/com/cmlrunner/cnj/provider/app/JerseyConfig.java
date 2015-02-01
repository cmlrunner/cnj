package com.cmlrunner.cnj.provider.app;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import com.cmlrunner.cnj.provider.resource.Jokes;

@Component
public class JerseyConfig extends ResourceConfig {

	public JerseyConfig() {
		super(Jokes.class);
	}
}
