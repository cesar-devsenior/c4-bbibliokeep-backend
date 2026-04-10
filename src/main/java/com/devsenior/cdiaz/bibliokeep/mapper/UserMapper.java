package com.devsenior.cdiaz.bibliokeep.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.devsenior.cdiaz.bibliokeep.model.dto.auth.AuthResponse;
import com.devsenior.cdiaz.bibliokeep.model.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "email", source = "user.email")
    AuthResponse toAuthResponse(User user, String accessToken, String refreshToken);
}
