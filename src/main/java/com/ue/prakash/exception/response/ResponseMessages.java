package com.ue.prakash.exception.response;

import org.springframework.http.HttpStatus;

public enum ResponseMessages {

	RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "Input JSONs Not Found"),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error"),
	CREATED(HttpStatus.CREATED, "Created Successfully"),
	NOT_ACCEPTABLE(HttpStatus.NOT_ACCEPTABLE, "Invalid Data Not Acceptable"),
	OK(HttpStatus.OK, "Request Processed Successfully"),
	DATA_RETRIEVED_SUCCESSFULLY(HttpStatus.OK, "Data Retrieved Successfully");

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

	private final HttpStatus errorCode;
	private final String errorMessage;

}
