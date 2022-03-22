package com.userexperior.prakash.exception.advice;


import com.userexperior.prakash.exception.InternalServerErrorException;
import com.userexperior.prakash.exception.InvalidInputException;
import com.userexperior.prakash.exception.ReportServiceException;
import com.userexperior.prakash.exception.ResourceNotFoundException;
import com.userexperior.prakash.exception.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.Arrays;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {





    @ExceptionHandler(value = {InternalServerErrorException.class})
    public ResponseEntity<Object> handleException(InternalServerErrorException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), Instant.now(),
                e.getMessage());
        log.error(Arrays.asList(e.getStackTrace()).toString());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @ExceptionHandler(value = {InvalidInputException.class})
    public ResponseEntity<Object> handleException(InvalidInputException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_ACCEPTABLE, e.getMessage(), Instant.now(),
                e.getMessage());
        log.error(Arrays.asList(e.getStackTrace()).toString());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_ACCEPTABLE);

    }


    @ExceptionHandler(value = {ReportServiceException.class})
    public ResponseEntity<Object> handleException(ReportServiceException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), Instant.now(),
                e.getMessage());
        log.error(Arrays.asList(e.getStackTrace()).toString());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @ExceptionHandler(value = {ResourceNotFoundException.class})
    public ResponseEntity<Object> handleException(ResourceNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND, e.getMessage(), Instant.now(),
                e.getMessage());
        log.error(Arrays.asList(e.getStackTrace()).toString());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleException(Exception e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), Instant.now(),
                e.getMessage());
        log.error(Arrays.asList(e.getStackTrace()).toString());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);

    }




}
