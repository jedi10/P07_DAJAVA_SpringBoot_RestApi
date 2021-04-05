package com.nnk.springboot.web.dto.mapper;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.web.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel="spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper( UserMapper.class);

    @Mapping(target = "password", ignore = true)
    UserDTO fromUser(User user);

    User toUser(UserDTO userDTO);

}

//https://ibootweb.com/questions/2088112/quand-dois-je-utiliser-mapstruct-ou-des-convertisseurs-avec-java-8-pour-eviter-les-erreurs
//https://mapstruct.org/documentation/stable/reference/html/#adding-custom-methods