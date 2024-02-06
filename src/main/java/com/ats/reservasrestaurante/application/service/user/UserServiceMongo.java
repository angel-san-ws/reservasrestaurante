package com.ats.reservasrestaurante.application.service.user;

import com.ats.reservasrestaurante.application.exception.SecurityException;
import com.ats.reservasrestaurante.application.lasting.EMessage;
import com.ats.reservasrestaurante.application.service.UserGenericService;
import com.ats.reservasrestaurante.domain.dto.UserDto;
import com.ats.reservasrestaurante.domain.entity.user.UserMongo;
import com.ats.reservasrestaurante.domain.repository.user.UserRepositoryMongo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//@Service
public class UserServiceMongo implements UserGenericService {
    private final UserRepositoryMongo userRepositoryMongo;

    public UserServiceMongo(UserRepositoryMongo userRepositoryMongo){
        this.userRepositoryMongo=userRepositoryMongo;
    }

    @Override
    public void save(Object id, UserDto userDto) {
        Optional<UserMongo> userMongo = userRepositoryMongo.findUserByUserName(id.toString());
        if(userMongo.isPresent()){
            userMongo.get().setEmail(userDto.email());
            userMongo.get().setLastName(userDto.lastName());
            userMongo.get().setFirstName(userDto.firstName());
            userMongo.get().setEnable(userDto.enable());
            userMongo.get().setEmail(userDto.email());
            userMongo.get().setPhone(userDto.phone());
            userRepositoryMongo.save( userMongo.get() );
        }
    }

    @Override
    public UserDto findByUsername(String userName) throws Exception {
        Optional<UserMongo> userMongo = userRepositoryMongo.findUserByUserName(userName.toString());
        return getUserDto(userMongo);
    }

    public UserMongo findUserMongoByUsername(String userName) throws Exception{
        return userRepositoryMongo.findUserByUserName(userName).orElseThrow(() -> new Exception("User not found by username:"+userName));
    }

    @Override
    public List<UserDto> findAll() {
        List<UserMongo> users=userRepositoryMongo.findAll();
        List<UserDto> listUserDto=new ArrayList<>();
        for(int i=0;i<users.size();i++){
            UserMongo user=users.get(i);
            UserDto userDto = new UserDto(user.getId(),user.getUsername(),user.getPassword(),user.getEmail(),user.getPhone(),user.getFirstName()
                    ,user.getLastName(),user.getEnable(),user.getRole());
            listUserDto.add(userDto);
        }
        return listUserDto;
    }

    @Override
    public void remove(Object id) {
        userRepositoryMongo.deleteById(id.toString());
    }

    @Override
    public UserDto findById (Object id) throws Exception {
        Optional<UserMongo> userMongo=userRepositoryMongo.findById(id.toString());
        return getUserDto(userMongo);
    }

    private UserDto getUserDto(Optional<UserMongo> userMongo) throws SecurityException {
        if(userMongo.isPresent()){
            UserMongo user = userMongo.get();
            UserDto userDto = new UserDto(user.getId(),user.getUsername(),user.getPassword(),user.getEmail(),user.getPhone(),user.getFirstName()
                    ,user.getLastName(),user.getEnable(),user.getRole());
            return userDto;
        }else{
            throw new SecurityException(EMessage.USER_NOT_FOUND);
        }
    }
}
