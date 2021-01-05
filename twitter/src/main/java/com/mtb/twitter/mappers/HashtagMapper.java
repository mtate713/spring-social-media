package com.mtb.twitter.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.mtb.twitter.dtos.HashtagResponseDto;
import com.mtb.twitter.entities.Hashtag;

@Mapper(componentModel = "spring")
public interface HashtagMapper {
	HashtagResponseDto entityToResponseDto(Hashtag hashtag);
	
	List<HashtagResponseDto> entitiesToResponseDtos(List<Hashtag> hashtags);
}
