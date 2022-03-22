package com.userexperior.prakash.exception;

public class InvalidInputException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private final String errorCode;
	private final String errorMessage;

	public InvalidInputException(String s, String errorCode, String errorMessage) {
		super(s);
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}


}
