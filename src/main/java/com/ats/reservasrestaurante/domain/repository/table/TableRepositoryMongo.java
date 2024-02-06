package com.ats.reservasrestaurante.domain.repository.table;

import com.ats.reservasrestaurante.domain.entity.table.TableMongo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TableRepositoryMongo extends MongoRepository<TableMongo,String> {
    Optional<TableMongo> findTableMongoByTableNumber(int tableNumber);
    Optional<TableMongo> findTopByOrderByTableNumberDesc();
}
