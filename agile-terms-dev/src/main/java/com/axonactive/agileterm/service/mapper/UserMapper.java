package com.axonactive.agileterm.service.mapper;

import com.axonactive.agileterm.entity.UserEntity;
import com.axonactive.agileterm.rest.model.UserDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(UserEntity userEntity);

    List<UserDto> toDtos(List<UserEntity> userEntityList);



}
