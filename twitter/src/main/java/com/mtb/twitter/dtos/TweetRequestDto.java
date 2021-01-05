package com.mtb.twitter.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TweetRequestDto {
	private String content;
	private CredentialsRequestDto credentials;
}
