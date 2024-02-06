package com.ats.reservasrestaurante.application.config;

import com.ats.reservasrestaurante.application.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

  private final UserDetailsService userDetailsService;
  private final JwtService jwtService;

  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain
  ) throws ServletException, IOException {
    // Obtener el header de Authorization(Bearer ---)
    final var authHeader = request.getHeader("Authorization");
    final String jwt;
    final String userName;
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    jwt = authHeader.substring(7);
    userName = jwtService.extractUserName(jwt);
    System.out.println("userName jwt:"+userName);

    if (userName != null &&
      SecurityContextHolder.getContext().getAuthentication() == null) {
      UserDetails userDetails = this.userDetailsService
        .loadUserByUsername(userName);
      setAuthenticationToContext(request, jwt, userDetails);
    }
    filterChain.doFilter(request,response);
  }

  private void setAuthenticationToContext(HttpServletRequest request,
                                          String jwt,
                                          UserDetails userDetails) {
    if (jwtService.isTokenValid(jwt, userDetails)){
      UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
        userDetails,
        null,
        userDetails.getAuthorities()
      );
      // Asignar el usuario al contexto de seguridad para usar los recursos
      // del API
      authenticationToken.setDetails(
        new WebAuthenticationDetailsSource().buildDetails(request)
      );
      SecurityContextHolder.getContext().setAuthentication(
        authenticationToken
      );
    }
  }
}
