package com.mtb.twitter.dtos;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TweetResponseDto {
	private Long id;
	private UserResponseDto author;
	private Timestamp posted;
	private String content;
	private TweetResponseDto inReplyTo;
	private TweetResponseDto repostOf;
}
