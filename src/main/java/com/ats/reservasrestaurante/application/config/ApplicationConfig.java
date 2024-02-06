package com.ats.reservasrestaurante.application.config;

import com.ats.reservasrestaurante.application.lasting.EMessage;
import com.ats.reservasrestaurante.domain.repository.user.UserRepositoryMongo;
import com.ats.reservasrestaurante.domain.repository.user.UserRepositoryPostgres;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

  private final UserRepositoryPostgres userRepositoryPostgres;
  private final UserRepositoryMongo userRepositoryMongo;

  @Bean
  public UserDetailsService userDetailsService() {
    if (userRepositoryPostgres!=null){
      return username -> userRepositoryPostgres.findUserByUserName(username)
              .orElseThrow(() -> new UsernameNotFoundException(
                      EMessage.USER_NOT_FOUND.getMessage()
              ));
    }else{
      return username -> userRepositoryMongo.findUserByUserName(username)
              .orElseThrow(() -> new UsernameNotFoundException(
                      EMessage.USER_NOT_FOUND.getMessage()
              ));
    }
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authenticationProvider
      = new DaoAuthenticationProvider();
    // Indexar el UserDetailsService
    authenticationProvider.setUserDetailsService(
      userDetailsService()
    );
    // Indexar el cifrado de contraseñas
    authenticationProvider.setPasswordEncoder(
      passwordEncoder()
    );
    return authenticationProvider;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(
    AuthenticationConfiguration configuration
  ) throws Exception {
    return configuration.getAuthenticationManager();
  }

}
