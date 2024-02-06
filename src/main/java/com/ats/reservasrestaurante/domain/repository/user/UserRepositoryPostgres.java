package com.ats.reservasrestaurante.domain.repository.user;

import com.ats.reservasrestaurante.domain.entity.user.UserPostgres;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepositoryPostgres extends JpaRepository<UserPostgres, Integer> {

    Optional<UserPostgres> findUserByUserName(String userName);

}
