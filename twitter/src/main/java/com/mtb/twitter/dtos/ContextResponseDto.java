package com.mtb.twitter.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContextResponseDto {
	private TweetResponseDto target;
	private List<TweetResponseDto> before;
	private List<TweetResponseDto> after;
}
