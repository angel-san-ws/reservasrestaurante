package com.ats.reservasrestaurante.domain.repository.configuration;

import com.ats.reservasrestaurante.domain.entity.configuration.ConfigurationMongo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ConfigurationRepositoryMongo extends MongoRepository<ConfigurationMongo,String> {
    Optional<ConfigurationMongo> findByConfigKey(String key);
}
