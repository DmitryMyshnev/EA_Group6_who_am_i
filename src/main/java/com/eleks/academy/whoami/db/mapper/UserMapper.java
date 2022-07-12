package com.eleks.academy.whoami.db.mapper;

import com.eleks.academy.whoami.db.dto.CreateUserCommand;
import com.eleks.academy.whoami.db.dto.CreateUserCommandDto;
import com.eleks.academy.whoami.db.dto.UserDto;
import com.eleks.academy.whoami.db.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDTO(User user);

    @Mapping(target = "name", source = "userName")
    CreateUserCommand toModel(CreateUserCommandDto dto);
}
