package com.example.mongo.examples.model.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "jokes")
public class JokeEntity {

    @Id
    private final Long id;
    private final String type;
    private final String setup;
    private final String punchline;

    /**
     * @param id joke's id in DB
     * @param type joke's type
     * @param setup setup
     * @param punchline punchline
     */
    public JokeEntity(Long id, String type, String setup, String punchline) {
        this.id = id;
        this.type = type;
        this.setup = setup;
        this.punchline = punchline;
    }

    /**
     * @param type joke's type
     * @param setup setup
     * @param punchline punchline
     */
    public JokeEntity(String type, String setup, String punchline) {
        this.id = null;
        this.type = type;
        this.setup = setup;
        this.punchline = punchline;
    }

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getSetup() {
        return setup;
    }

    public String getPunchline() {
        return punchline;
    }

    JokeEntity withId(Long id) {
        return new JokeEntity(id, this.type, this.setup, this.punchline);
    }
}
