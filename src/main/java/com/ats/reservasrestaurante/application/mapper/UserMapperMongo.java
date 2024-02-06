package com.ats.reservasrestaurante.application.mapper;

import com.ats.reservasrestaurante.application.mapper.base.IBaseMapper;
import com.ats.reservasrestaurante.domain.dto.UserDto;
import com.ats.reservasrestaurante.domain.entity.user.UserMongo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapperMongo extends IBaseMapper {
       @Mapping(source = "id", target = "id", qualifiedByName = "objectToString")
       UserMongo toEntity(UserDto dto);
       UserDto toDto(UserMongo entity);
       List<UserMongo> toEntityList(List<UserDto> dtoList);
       List<UserDto> toDtoList(List<UserMongo> entityList);
       @Named("objectToString")
       default String objectToString(Object obj) {
              return obj.toString();
       }
}
