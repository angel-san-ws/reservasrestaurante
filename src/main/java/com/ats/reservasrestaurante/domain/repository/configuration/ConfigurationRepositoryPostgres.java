package com.ats.reservasrestaurante.domain.repository.configuration;

import com.ats.reservasrestaurante.domain.entity.configuration.ConfigurationPostgres;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository

public interface ConfigurationRepositoryPostgres extends JpaRepository<ConfigurationPostgres,Integer> {
    Optional<ConfigurationPostgres> findByConfigKey(String key);
}
