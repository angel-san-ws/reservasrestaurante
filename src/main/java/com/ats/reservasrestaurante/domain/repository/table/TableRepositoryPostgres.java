package com.ats.reservasrestaurante.domain.repository.table;

import com.ats.reservasrestaurante.domain.entity.table.TablePostgres;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TableRepositoryPostgres extends JpaRepository<TablePostgres,Integer> {
    Optional<TablePostgres> findTablePostgresByTableNumber(int tableNumber);
    Optional<TablePostgres> findTopByOrderByTableNumberDesc();
}
