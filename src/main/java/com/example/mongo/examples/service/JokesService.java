package com.example.mongo.examples.service;

import com.example.mongo.examples.model.domain.JokeModel;
import com.example.mongo.examples.model.entity.JokeEntity;
import com.mongodb.client.result.DeleteResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public final class JokesService {

    private static final Logger log = LoggerFactory.getLogger(JokesService.class);

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

    public void insertAll(List<JokeModel> models) {
        List<JokeEntity> entities = models.stream().map(mapperToEntity::convert).collect(Collectors.toList());
        mongoTemplate.insertAll(entities);
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

    public void delete(String request) {
        Query query = new Query();
        String mongoRequest = "/" + request + "/";
        query.addCriteria(
                new Criteria().orOperator(getCriteria("setup", mongoRequest), getCriteria("punchline", mongoRequest))
        );
        DeleteResult result = mongoTemplate.remove(query, JokeEntity.class);
        log.info(result.toString());
    }

    private Criteria getCriteria(String field, String request) {
        return Criteria.where(field).is(request);
    }
}
