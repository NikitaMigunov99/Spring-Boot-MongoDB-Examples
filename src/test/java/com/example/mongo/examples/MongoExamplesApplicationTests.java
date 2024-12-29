package com.example.mongo.examples;

import com.example.mongo.examples.mapper.JokesDomainToEntityMapper;
import com.example.mongo.examples.mapper.JokesEntityToDomainMapper;
import com.example.mongo.examples.model.domain.JokeModel;
import com.example.mongo.examples.model.entity.JokeEntity;
import com.example.mongo.examples.service.JokesService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class MongoExamplesApplicationTests extends AbstractBaseIntegrationTest {

    private static final String JOKES = "jokes";
    private static final int ZERO = 0;

    @Autowired
    private JokesDomainToEntityMapper jokesDomainToEntityMapper;
    @Autowired
    private JokesEntityToDomainMapper jokesEntityToDomainMapper;
    @Autowired
    private JokesService jokesService;
    @Autowired
    private MongoTemplate mongoTemplate;

    @AfterEach
    public void tearDown() {
        mongoTemplate.dropCollection(JOKES);
    }

   // @Test
    public void getJokeByType() {
        List<JokeModel> jokes = getModels();
        mongoTemplate.insertAll(getEntities());
        List<JokeModel> programming = jokes.stream().filter(jokeEntity -> jokeEntity.type().equals("programming")).toList();
        List<JokeModel> general = jokes.stream().filter(jokeEntity -> jokeEntity.type().equals("general")).toList();

        Assertions.assertEquals(programming, jokesService.getJokeByType("programming"));
        Assertions.assertEquals(general, jokesService.getJokeByType("general"));
        Assertions.assertEquals(new ArrayList<JokeModel>(), jokesService.getJokeByType("wiiwiui"));
    }

    @Test
    public void getJokesList() {
        List<JokeModel> expected = getModels();
        expected.forEach(jokesService::saveJoke);

        List<JokeModel> actual = jokesService.getJokesList();
        Assertions.assertEquals(expected.size(), actual.size());
        for (int i = 0; i < actual.size(); i++) {
            compareJokes(expected.get(i), actual.get(i));
        }
    }

    //@Test
    public void getJokesBySearch() {
        mongoTemplate.insertAll(getEntities());
        Assertions.assertEquals(3, jokesService.getJokesBySearch("was").size());
    }

    //@Test
    public void saveJoke() {
        JokeModel model = getModels().get(ZERO);
        jokesService.saveJoke(model);
        JokeModel jokeModel = jokesService.getJokesList().get(ZERO);
        compareJokes(model, jokeModel);
    }

   // @Test
    public void updateTypeByIndex() {
        mongoTemplate.insertAll(getEntities());
        jokesService.updateTypeById(2, "new type");
        JokeModel jokeModel = jokesService.getJokesList().get(2);
        Assertions.assertEquals("new type", jokeModel.type());
    }

    //@Test
    public void getJokesCount() {
        mongoTemplate.insertAll(getEntities());
        Assertions.assertEquals(10, jokesService.getJokesCount());
    }

    //@Test
    public void deleteField() {
        mongoTemplate.insertAll(getEntities());
        Assertions.assertEquals(10, jokesService.getJokesCount());

        System.out.println("Models before deleting setup field \n" + getModels());

        Query query = new Query();
        Update update = new Update().unset("type");
        mongoTemplate.updateMulti(query, update, JokeEntity.class);

        List<JokeModel> modifiedModels = jokesEntityToDomainMapper.convert(mongoTemplate.findAll(JokeEntity.class));
        System.out.println("Models after deleting setup field \n" + modifiedModels);
    }

   // @Test
    public void deleteDocument() {
        jokesService.insertAll(getModels());
        jokesService.delete("Why did the cookie cry?");

        List<JokeModel> actual = jokesService.getJokesList();
        Assertions.assertEquals(9, actual.size());
    }

    private void compareJokes(JokeModel expected, JokeModel actual) {
        Assertions.assertEquals(expected.type(), actual.type());
        Assertions.assertEquals(expected.setup(), actual.setup());
        Assertions.assertEquals(expected.punchline(), actual.punchline());
    }

    private List<JokeModel> getModels() {
        return Arrays.asList(
                new JokeModel(
                        "96",
                        "general",
                        "Did you hear that David lost his ID in prague?",
                        "Now we just have to call him Dav."
                ),
                new JokeModel(
                        "185",
                        "general",
                        "What did the traffic light say to the car as it passed?",
                        "Don't look I'm changing!"
                ),
                new JokeModel(
                        "162",
                        "general",
                        "Where was the Declaration of Independence signed?",
                        "At the bottom! "
                ),
                new JokeModel(
                        "438",
                        "programming",
                        "Why did the developer go broke?",
                        "They kept spending all their cache."
                ),
                new JokeModel(
                        "361",
                        "general",
                        "Why does Waldo only wear stripes?",
                        "Because he doesn't want to be spotted."
                ),
                new JokeModel(
                        "321",
                        "general",
                        "Why did the cookie cry?",
                        "It was feeling crumby."
                ),
                new JokeModel(
                        "412",
                        "programming",
                        "What do you get when you cross a React developer with a mathematician?",
                        "A function component."
                ),
                new JokeModel(
                        "131",
                        "general",
                        "What do you call a pig that knows karate?",
                        "You will see one later and one in a while."
                ),
                new JokeModel(
                        "212",
                        "general",
                        "How do you tell the difference between a crocodile and an alligator?",
                        "A pork chop!"
                ),
                new JokeModel(
                        "214",
                        "general",
                        "What do you call a pile of cats?",
                        "A Meowtain."
                ),
                new JokeModel(
                        "321",
                        "general",
                        "Why did the cookie cry?",
                        "It was feeling crumby."
                )
        );
    }

    private List<JokeEntity> getEntities() {
        return getModels().stream().map(jokesDomainToEntityMapper::convert).collect(Collectors.toList());
    }
}
