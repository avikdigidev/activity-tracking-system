package com.ue.prakash.exception;

public class ActivityTrackerException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private final String errorCode;
	private final String errorMessage;

	public ActivityTrackerException(String s,String errorCode, String errorMessage) {
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
