package com.ats.reservasrestaurante.application.service.auth;

import com.ats.reservasrestaurante.application.exception.SecurityException;
import com.ats.reservasrestaurante.application.lasting.EMessage;
import com.ats.reservasrestaurante.application.lasting.ERole;
import com.ats.reservasrestaurante.application.service.AuthenticationService;
import com.ats.reservasrestaurante.application.service.JwtService;
import com.ats.reservasrestaurante.domain.dto.AuthenticationDto;
import com.ats.reservasrestaurante.domain.dto.UserDto;
import com.ats.reservasrestaurante.domain.entity.user.UserPostgres;
import com.ats.reservasrestaurante.domain.repository.user.UserRepositoryPostgres;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServicePostgres implements AuthenticationService{
    private final UserRepositoryPostgres userRepositoryPostgres;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthenticationServicePostgres(
            UserRepositoryPostgres userRepositoryPostgres,
            JwtService jwtService,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager
    ) {
        this.userRepositoryPostgres = userRepositoryPostgres;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public String register(UserDto userDto) throws SecurityException {
        if(userRepositoryPostgres.findUserByUserName(userDto.userName()).isPresent()){
            throw new SecurityException(EMessage.USER_EXISTS);
        }else{
            UserPostgres userPostgres = UserPostgres.builder()
                    .userName(userDto.userName())
                    .firstName(userDto.firstName())
                    .lastName(userDto.lastName())
                    .email(userDto.email())
                    .enable(true)
                    .role(ERole.USER)
                    .password(passwordEncoder.encode(userDto.password()))
                    .phone(userDto.phone())
                    .build();
            userRepositoryPostgres.save(userPostgres);
            return jwtService.generateToken(userPostgres);
        }
    }

    @Override
    public String login(AuthenticationDto authenticationDto) throws SecurityException {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationDto.userName(),
                        authenticationDto.password()
                )
        );
        UserPostgres userPostgres = userRepositoryPostgres.findUserByUserName(
                authenticationDto.userName()
        ).orElseThrow(
                () -> new SecurityException(EMessage.USER_NOT_FOUND)
        );
        return jwtService.generateToken(userPostgres);
    }
}
