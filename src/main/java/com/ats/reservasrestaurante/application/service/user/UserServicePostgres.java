package com.ats.reservasrestaurante.application.service.user;

import com.ats.reservasrestaurante.application.service.JwtService;
import com.ats.reservasrestaurante.application.service.UserGenericService;
import com.ats.reservasrestaurante.domain.dto.UserDto;
import com.ats.reservasrestaurante.domain.entity.user.UserPostgres;
import com.ats.reservasrestaurante.domain.repository.user.UserRepositoryPostgres;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServicePostgres implements UserGenericService {
    private final UserRepositoryPostgres userRepositoryPostgres;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public UserServicePostgres(
            @Lazy UserRepositoryPostgres userRepositoryPostgres,
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
    public void save(Object id, UserDto userDto) {
        UserPostgres user = UserPostgres.builder()
                .id( Integer.parseInt(String.valueOf(id)) )
                .phone( userDto.phone() )
                .userName(userDto.userName())
                .firstName(userDto.firstName())
                .lastName(userDto.lastName())
                .email(userDto.email())
                .enable(true)
                .role(userDto.role())
                .password(passwordEncoder.encode(userDto.password()))
                .build();
        userRepositoryPostgres.save(user);
    }

    @Override
    public UserDto findByUsername(String userName) throws Exception{
        UserPostgres user = userRepositoryPostgres.findUserByUserName(userName).orElseThrow(() -> new Exception("User not found by username:"+userName));
        UserDto userDto = new UserDto(user.getId(),user.getUsername(),user.getPassword(),user.getEmail(),user.getPhone(),user.getFirstName()
                                ,user.getLastName(),user.getEnable(),user.getRole());
        return userDto;
    }

    public UserPostgres findUserPostgressByUsername(String userName) throws Exception{
        return userRepositoryPostgres.findUserByUserName(userName).orElseThrow(() -> new Exception("User not found by username:"+userName));
    }

    @Override
    public List<UserDto> findAll() {
        List<UserPostgres> users =userRepositoryPostgres.findAll();
        List<UserDto> listUserDto=new ArrayList<>();
        for(int i=0;i<users.size();i++){
            UserPostgres user=users.get(i);
            UserDto userDto = new UserDto(user.getId(),user.getUsername(),user.getPassword(),user.getEmail(),user.getPhone(),user.getFirstName()
                    ,user.getLastName(),user.getEnable(),user.getRole());
            listUserDto.add(userDto);
        }
        return listUserDto;
    }

    @Override
    public void remove(Object id) {
        userRepositoryPostgres.deleteById(Integer.valueOf(id.toString()));
    }

    @Override
    public UserDto findById (Object id) throws Exception{
        UserPostgres user = userRepositoryPostgres.findById(Integer.valueOf(id.toString()))
                .orElseThrow(() -> new Exception("User not found by id:"+id));
        UserDto userDto = new UserDto(user.getId(),user.getUsername(),user.getPassword(),user.getEmail(),user.getPhone(),user.getFirstName()
                ,user.getLastName(),user.getEnable(),user.getRole());
        return userDto;
    }
}
