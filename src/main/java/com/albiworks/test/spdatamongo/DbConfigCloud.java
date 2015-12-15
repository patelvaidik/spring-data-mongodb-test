package com.albiworks.test.spdatamongo;

import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@Profile(value="cloud")
@EnableMongoRepositories
public class DbConfigCloud extends AbstractCloudConfig{

	@Bean(name="mongoDbFactory")
	public MongoDbFactory createMongoDbFactory(){
		return connectionFactory().mongoDbFactory();
	}
}
