package com.ats.reservasrestaurante.domain.repository.user;

import com.ats.reservasrestaurante.domain.entity.user.UserMongo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepositoryMongo extends MongoRepository<UserMongo, String> {
    Optional<UserMongo> findUserByUserName(String userName);

}