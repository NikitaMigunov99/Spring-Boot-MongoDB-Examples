package com.example.mongo.examples.model.domain;

/**
 * Model of joke
 */
public record JokeModel(
        String id,
        String type,
        String setup,
        String punchline) {

    @Override
    public String toString() {
        return "{" +
                "type='" + type + '\'' +
                ", setup='" + setup + '\'' +
                ", punchline='" + punchline + '\'' +
                '}';
    }
}