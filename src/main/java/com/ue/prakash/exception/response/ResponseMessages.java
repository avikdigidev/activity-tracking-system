package com.ue.prakash.exception.response;

import org.springframework.http.HttpStatus;

public enum ResponseMessages {

	NOT_FOUND(HttpStatus.NOT_FOUND, "Expected Input JSONs not found"),
	OK(HttpStatus.OK, "Request Processed Successfully");


	ResponseMessages(HttpStatus code, String customErrorMessage) {
		this.errorCode = code;
		this.errorMessage = customErrorMessage;

	}

	public HttpStatus getCode() {
		return errorCode;
	}

	public String getCustomErrorMessage() {
		return errorMessage;
	}

	private HttpStatus errorCode;
	private String errorMessage;

}
