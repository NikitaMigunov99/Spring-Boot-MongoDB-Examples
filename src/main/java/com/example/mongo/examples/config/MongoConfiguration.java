package com.example.mongo.examples.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

import java.util.Collection;
import java.util.Collections;

@Configuration
@ConfigurationProperties(
        prefix = "spring.data.mongodb"
)
public class MongoConfiguration extends AbstractMongoClientConfiguration {

    private String host;
    private String port;

    @Override
    protected String getDatabaseName() {
        return "test";
    }

    @Override
    public MongoClient mongoClient() {
        String connectionURI = "mongodb://" + host + ":" + port + "/" + getDatabaseName();
        System.out.println("MongoConfiguration host " + host);
        System.out.println("MongoConfiguration port " + port);
        System.out.println("connectionURI " + connectionURI);
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