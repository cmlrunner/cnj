package com.fdev.cnj.app;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@ImportResource("classpath:/META-INF/applicationContext.xml")
@EnableMongoRepositories(basePackages = "com.fdev.cnj")
@EnableScheduling
@SpringBootApplication
public class IncdbClientApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(IncdbClientApplication.class);
	}

	public static void main(String[] args) {
		new IncdbClientApplication().configure(new SpringApplicationBuilder(IncdbClientApplication.class)).run(args);
	}
}
