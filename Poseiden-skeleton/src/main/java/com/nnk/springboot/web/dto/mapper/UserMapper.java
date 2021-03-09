package com.nnk.springboot.web.dto.mapper;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.web.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper( UserMapper.class);

    @Mapping(target = "password", ignore = true)
    UserDTO fromUser(User user);

    User toUser(UserDTO userDTO);

}


//https://mapstruct.org/documentation/stable/reference/html/#adding-custom-methods