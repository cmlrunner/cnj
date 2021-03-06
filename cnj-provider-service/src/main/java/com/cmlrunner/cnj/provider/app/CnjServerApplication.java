package com.cmlrunner.cnj.provider.app;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@ComponentScan(basePackages = "com.cmlrunner.cnj.provider")
@EnableMongoRepositories(basePackages = "com.cmlrunner.cnj.provider")
@SpringBootApplication
public class CnjServerApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(CnjServerApplication.class);
	}

	public static void main(String[] args) {
		new CnjServerApplication().configure(new SpringApplicationBuilder(CnjServerApplication.class)).run(args);
	}
}
