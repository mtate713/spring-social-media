package com.mtb.twitter.entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class UserCredentials {

	@Column(unique = true, nullable = false)
	private String username;

	@Column(nullable = false)
	private String password;

}
