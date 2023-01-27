package com.naturegecko.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@ComponentScan({"com.naturegecko.application"})
@EntityScan("com.naturegecko.application")
@EnableMongoRepositories("com.naturegecko.application.repositories")
public class CountryRoleplayApplication {

	public static void main(String[] args) {
		SpringApplication.run(CountryRoleplayApplication.class, args);
	}

}
