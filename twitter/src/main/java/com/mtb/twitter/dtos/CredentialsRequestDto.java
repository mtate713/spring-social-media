package com.mtb.twitter.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CredentialsRequestDto {

	private String username;
	private String password;
}
