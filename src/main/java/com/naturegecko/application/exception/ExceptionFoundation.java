package com.naturegecko.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;


@SuppressWarnings("serial")
public class ExceptionFoundation extends HttpStatusCodeException {
	private ExceptionResponseModel.EXCEPTION_CODE exceptionDetail;
	private String message;

	public ExceptionFoundation(ExceptionResponseModel.EXCEPTION_CODE exceptionDetail, HttpStatus status,
			String message) {
		super(status);
		this.exceptionDetail = exceptionDetail;
		this.message = message;
	}
	
	public ExceptionResponseModel.EXCEPTION_CODE getExceptionCode() {
		return this.exceptionDetail;
	}

	public String getMessage() {
		return this.message;
	}

}
