package com.example.mongo.examples;

import org.springframework.boot.SpringApplication;

public class TestMongoExamplesApplication {

    public static void main(String[] args) {
        SpringApplication.from(MongoExamplesApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
