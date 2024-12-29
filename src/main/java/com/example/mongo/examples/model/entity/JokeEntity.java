package com.example.mongo.examples.model.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "jokes")
public class JokeEntity {

    @Id
    private String id;
    private String type;
    private String setup;
    private String punchline;

    public JokeEntity() {}

    /**
     * @param id joke's id in DB
     * @param type joke's type
     * @param setup setup
     * @param punchline punchline
     */
    public JokeEntity(String id, String type, String setup, String punchline) {
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

    public void setId(String id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSetup(String setup) {
        this.setup = setup;
    }

    public void setPunchline(String punchline) {
        this.punchline = punchline;
    }

    public String getId() {
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

    JokeEntity withId(String id) {
        return new JokeEntity(id, this.type, this.setup, this.punchline);
    }
}
