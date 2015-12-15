package com.albiworks.test.spdatamongo;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

/**
 * Class to configure the databse when we are in development 
 * environment
 * @author Alexandro Blanco <alex@albiworks.com>
 *
 */
@Configuration
@Profile(value="dev")
@EnableMongoRepositories
public class DbConfigDev extends AbstractMongoConfiguration {

	@Value(value = "${com.albiworks.test.mongodb.user}")
	private String user;
	
	@Value(value = "${com.albiworks.test.mongodb.pass}")
	private char[] password;
	
	@Value(value = "${com.albiworks.test.mongodb.host}")
	private String host;
	
	@Value(value = "${com.albiworks.test.mongodb.port}")
	private String port;
	
	@Override
	protected String getDatabaseName() {
		return "messages";
	}

	@Override
	public Mongo mongo() throws Exception {
		Mongo mongo = new Mongo();
		return mongo;
	}

	@Override
	@Bean(name="mongoDbFactory")
	public MongoDbFactory mongoDbFactory() throws Exception {
		MongoCredential cred = MongoCredential.createScramSha1Credential(user, getDatabaseName(), password);
		ServerAddress addr = new ServerAddress(host, Integer.valueOf(port));
		
		MongoClient client = new MongoClient(addr, Arrays.asList(cred));
		
		return new SimpleMongoDbFactory(client, getDatabaseName());
	}
	
}
