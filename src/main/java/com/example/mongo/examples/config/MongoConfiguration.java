package com.example.mongo.examples.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

import java.util.Collection;
import java.util.Collections;

@Configuration
public class MongoConfiguration extends AbstractMongoClientConfiguration {

    private final Environment env;

    public MongoConfiguration(Environment env) {
        this.env = env;
    }

    @Override
    protected String getDatabaseName() {
        return "test";
    }

    @Override
    public MongoClient mongoClient() {
        String host = env.getProperty("spring.data.mongodb.host");
        String port = env.getProperty("spring.data.mongodb.port");
        String connectionURI = "mongodb://" + host + ":" + port + "/" + getDatabaseName();
        ConnectionString connectionString = new ConnectionString(connectionURI);
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();

        return MongoClients.create(mongoClientSettings);
    }

    @Override
    public Collection<String> getMappingBasePackages() {
        return Collections.singleton("jokes");
    }
}