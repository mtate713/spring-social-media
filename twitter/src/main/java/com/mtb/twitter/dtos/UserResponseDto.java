package com.mtb.twitter.dtos;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserResponseDto {
	private String username;
	private ProfileDto profile;
	private Timestamp joinedAt;
}
