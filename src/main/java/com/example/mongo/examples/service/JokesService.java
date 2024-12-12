package com.example.mongo.examples.service;

import com.example.mongo.examples.model.domain.JokeModel;
import com.example.mongo.examples.model.entity.JokeEntity;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public final class JokesService {

    private final Converter<List<JokeEntity>, List<JokeModel>> mapper;
    private final Converter<JokeModel, JokeEntity> mapperToEntity;
    private final MongoTemplate mongoTemplate;

    public JokesService(Converter<List<JokeEntity>, List<JokeModel>> mapper,
                        Converter<JokeModel, JokeEntity> mapperToEntity,
                        MongoTemplate mongoTemplate) {
        this.mapperToEntity = mapperToEntity;
        this.mapper = mapper;
        this.mongoTemplate = mongoTemplate;
    }

    public List<JokeModel> getJokeByType(String type) {
        Query query = new Query();
        query.addCriteria(getCriteria("type", type));
        List<JokeEntity> entityList = mongoTemplate.find(query, JokeEntity.class);
        return mapper.convert(entityList);
    }

    public List<JokeModel> getJokesList() {
        List<JokeEntity> entityList = mongoTemplate.findAll(JokeEntity.class);
        return mapper.convert(entityList);
    }

    public List<JokeModel> getJokesBySearch(String request) {
        Query query = new Query();
        String mongoRequest = "/" + request + "/";
        query.addCriteria(new Criteria().orOperator(
                getCriteria("setup", mongoRequest), getCriteria("punchline", mongoRequest)
                )
        );
        List<JokeEntity> entities = mongoTemplate.find(query, JokeEntity.class);
        return mapper.convert(entities);
    }

    public void saveJoke(JokeModel model) {
        JokeEntity entity = mapperToEntity.convert(model);
        mongoTemplate.save(entity);
    }

    public void updateTypeById(int id, String type) {
        List<JokeEntity> entityList = mongoTemplate.findAll(JokeEntity.class);
        JokeEntity entity = entityList.get(id);
        JokeEntity updatedEntity = new JokeEntity(entity.getId(), type, entity.getSetup(), entity.getPunchline());
        mongoTemplate.save(updatedEntity);
    }

    public long getJokesCount() {
        return mongoTemplate.count(new Query(), "jokes");
    }

    private Criteria getCriteria(String field, String request) {
        return Criteria.where(field).is(request);
    }
}
