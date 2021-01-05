package com.mtb.twitter.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mtb.twitter.dtos.TweetRequestDto;
import com.mtb.twitter.dtos.TweetResponseDto;
import com.mtb.twitter.entities.Tweet;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface TweetMapper {
	Tweet requestDtoToEntity(TweetRequestDto tweetRequestDto);

	@Mapping(target = "posted", source = "postedAt")
	TweetResponseDto entityToResponseDto(Tweet tweet);

	List<TweetResponseDto> entitiesToResponseDtos(List<Tweet> tweets);
}
