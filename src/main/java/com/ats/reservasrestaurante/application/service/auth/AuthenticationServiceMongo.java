package com.ats.reservasrestaurante.application.service.auth;

import com.ats.reservasrestaurante.application.exception.SecurityException;
import com.ats.reservasrestaurante.application.lasting.EMessage;
import com.ats.reservasrestaurante.application.lasting.ERole;
import com.ats.reservasrestaurante.application.service.AuthenticationService;
import com.ats.reservasrestaurante.application.service.JwtService;
import com.ats.reservasrestaurante.domain.dto.AuthenticationDto;
import com.ats.reservasrestaurante.domain.dto.UserDto;
import com.ats.reservasrestaurante.domain.entity.user.UserMongo;
import com.ats.reservasrestaurante.domain.entity.user.UserPostgres;
import com.ats.reservasrestaurante.domain.repository.user.UserRepositoryMongo;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

@AllArgsConstructor
public class AuthenticationServiceMongo implements AuthenticationService {

    private final UserRepositoryMongo userRepositoryMongo;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    @Override
    public String register(UserDto userDto) throws SecurityException {
        if(userRepositoryMongo.findUserByUserName(userDto.userName()).isPresent()){
            throw new SecurityException(EMessage.USER_EXISTS);
        }else{
            UserMongo userMongo = UserMongo.builder()
                    .userName(userDto.userName())
                    .firstName(userDto.firstName())
                    .lastName(userDto.lastName())
                    .email(userDto.email())
                    .enable(true)
                    .role(ERole.USER)
                    .password(passwordEncoder.encode(userDto.password()))
                    .phone(userDto.phone())
                    .build();
            userRepositoryMongo.save(userMongo);
            return jwtService.generateToken(userMongo);
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
        UserMongo userMongo = userRepositoryMongo.findUserByUserName(
                authenticationDto.userName()
        ).orElseThrow(
                () -> new SecurityException(EMessage.USER_NOT_FOUND)
        );
        return jwtService.generateToken(userMongo);
    }
}
