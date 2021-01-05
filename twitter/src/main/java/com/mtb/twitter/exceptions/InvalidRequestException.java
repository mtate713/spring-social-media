package com.mtb.twitter.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class InvalidRequestException extends RuntimeException {

	private static final long serialVersionUID = -3986870419968980332L;

	private String message;

}
