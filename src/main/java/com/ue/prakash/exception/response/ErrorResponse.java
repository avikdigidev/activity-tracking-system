package com.ue.prakash.exception.response;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Data
public class ErrorResponse {
	private HttpStatus status;
	private String message;
	private Instant timestamp;

	public ErrorResponse(HttpStatus status, String message, Instant timestamp) {
		this.status = status;
		this.message = message;
		this.timestamp = timestamp;
	}

}
