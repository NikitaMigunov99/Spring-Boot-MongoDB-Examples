package com.example.mongo.examples.mapper;

import com.example.mongo.examples.model.domain.JokeModel;
import com.example.mongo.examples.model.entity.JokeEntity;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


@Component
public class JokesDomainToEntityMapper implements Converter<JokeModel, JokeEntity> {

    @Override
    public JokeEntity convert(JokeModel source) {
        return new JokeEntity(source.type(), source.setup(), source.punchline());
    }
}
