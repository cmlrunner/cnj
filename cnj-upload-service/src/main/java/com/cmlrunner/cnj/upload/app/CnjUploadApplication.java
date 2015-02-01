package com.cmlrunner.cnj.upload.app;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableMongoRepositories(basePackages = "com.cmlrunner.cnj.upload")
@ComponentScan(basePackages = "com.cmlrunner.cnj.upload")
@EnableScheduling
@SpringBootApplication
public class CnjUploadApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(CnjUploadApplication.class);
	}

	public static void main(String[] args) {
		new CnjUploadApplication().configure(new SpringApplicationBuilder(CnjUploadApplication.class)).run(args);
	}
}
