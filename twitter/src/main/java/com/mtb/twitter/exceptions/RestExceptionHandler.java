package com.mtb.twitter.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler
	public ResponseEntity<Object> handleInvalidRequestException(InvalidRequestException invalidRequestException,
			WebRequest request) {
		Map<String, String> res = new HashMap<>();
		res.put("error", invalidRequestException.getMessage());
		return handleExceptionInternal(invalidRequestException, res, null, HttpStatus.BAD_REQUEST, request);
	}
}
