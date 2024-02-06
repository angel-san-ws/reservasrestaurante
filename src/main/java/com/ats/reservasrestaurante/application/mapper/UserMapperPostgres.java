package com.ats.reservasrestaurante.application.mapper;

import com.ats.reservasrestaurante.application.mapper.base.IBaseMapper;
import com.ats.reservasrestaurante.domain.dto.UserDto;
import com.ats.reservasrestaurante.domain.entity.user.UserPostgres;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapperPostgres extends IBaseMapper {
    @Mapping(source = "id", target = "id", qualifiedByName = "objectToInteger")
    UserPostgres toEntity(UserDto dto);
    UserDto toDto(UserPostgres entity);
    List<UserPostgres> toEntityList(List<UserDto> dtoList);
    List<UserDto> toDtoList(List<UserPostgres> entityList);
    @Named("objectToInteger")
    default Integer objectToInteger(Object obj){
        return Integer.valueOf(obj.toString());
    }
}
