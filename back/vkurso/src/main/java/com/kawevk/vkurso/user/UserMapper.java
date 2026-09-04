package com.kawevk.vkurso.user;

import com.kawevk.vkurso.user.dtos.UserResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toResponse(User user);
}
