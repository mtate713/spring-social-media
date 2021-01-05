package com.mtb.twitter.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProfileDto {
	private String firstName;
	private String lastName;
	private String email;
	private String phoneNumber;
}
