package com.ue.prakash.exception.response;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Data
public class ErrorResponse {
	private HttpStatus status;
	private String message;
	private Instant timestamp;
	private String errorStack;

	public ErrorResponse(HttpStatus status, String message, Instant timestamp, String errorStack) {
		this.status = status;
		this.message = message;
		this.timestamp = timestamp;
		this.errorStack = errorStack;
	}

}
