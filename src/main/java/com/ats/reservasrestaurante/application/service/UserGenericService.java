package com.ats.reservasrestaurante.application.service;

import com.ats.reservasrestaurante.domain.dto.UserDto;
import java.util.List;
import java.util.Optional;

public interface UserGenericService {
    public void save(Object id, UserDto userDto);
    public UserDto findByUsername (String userName) throws Exception;
    public UserDto findById (Object id) throws Exception;
    public List<UserDto> findAll ();
    public void remove(Object id);
}
