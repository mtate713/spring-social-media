package com.mtb.twitter.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mtb.twitter.dtos.UserRequestDto;
import com.mtb.twitter.dtos.UserResponseDto;
import com.mtb.twitter.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
	@Mapping(target = "userProfile", source = "profile")
	@Mapping(target = "userCredentials", source = "credentials")
	User requestDtoToEntity(UserRequestDto userRequestDto);

	@Mapping(target = "username", source = "userCredentials.username")
	@Mapping(target = "profile", source = "userProfile")
	UserResponseDto entityToResponseDto(User user);

	List<UserResponseDto> entitiesToResponseDtos(List<User> users);
}
