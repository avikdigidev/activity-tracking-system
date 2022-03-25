package com.ue.prakash.exception.advice;


import com.ue.prakash.exception.ActivityTrackerException;
import com.ue.prakash.exception.NoDataFoundException;
import com.ue.prakash.exception.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.Instant;
import java.util.Arrays;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	@ExceptionHandler(value = { NoDataFoundException.class })
	public ResponseEntity<Object> handleException(NoDataFoundException e) {
		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND, e.getMessage(), Instant.now());
		log.error(Arrays.asList(e.getStackTrace()).toString());
		return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);

	}

	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(value = { ActivityTrackerException.class })
	public ResponseEntity<Object> handleException(ActivityTrackerException e) {
		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), Instant.now());
		log.error(Arrays.asList(e.getStackTrace()).toString());
		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@ExceptionHandler(value = { Exception.class })
	public ResponseEntity<Object> handleException(Exception e) {
		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), Instant.now());
		log.error(Arrays.asList(e.getStackTrace()).toString());
		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);

	}



	
}
