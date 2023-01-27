package com.naturegecko.application.exception;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class HttpResponseModel {

	private int exceptionCode;
	private HttpStatus httpStatus;
	private String details;
	private String message;

}
