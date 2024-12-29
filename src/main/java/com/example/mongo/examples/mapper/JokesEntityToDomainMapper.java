package com.example.mongo.examples.mapper;

import com.example.mongo.examples.model.domain.JokeModel;
import com.example.mongo.examples.model.entity.JokeEntity;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class JokesEntityToDomainMapper implements Converter<List<JokeEntity>, List<JokeModel>> {

    @Override
    public List<JokeModel> convert(List<JokeEntity> dbList) {
        return dbList.stream().map(this::toModel).collect(Collectors.toList());
    }

    private JokeModel toModel(JokeEntity entity) {
        return new JokeModel(
                entity.getId(),
                entity.getType(),
                entity.getSetup(),
                entity.getPunchline()
        );
    }
}
