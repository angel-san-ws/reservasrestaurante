package com.ats.reservasrestaurante.application.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtFilter jwtFilter;
  private final AuthenticationProvider authenticationProvider;

  // Lista blanca de acceso (Rutas que no requieren autenticacion)
  private static final String[] WHITE_LIST_URLS = {
    "/api/v1/auth/**",
    "/api/v1/public",
    "/reservasrest-api-docs/*"
  };

  // Cadena de filtros de seguridad
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
      .authorizeHttpRequests(
        req -> req.requestMatchers(WHITE_LIST_URLS)
          .permitAll()
          .anyRequest()
          .authenticated()
      )
      .sessionManagement(
        sess -> sess.sessionCreationPolicy(
          SessionCreationPolicy.STATELESS
        )
      )
      .authenticationProvider(authenticationProvider)
      .addFilterBefore(
        jwtFilter,
        UsernamePasswordAuthenticationFilter.class
      );
    return http.build();
  }
}
