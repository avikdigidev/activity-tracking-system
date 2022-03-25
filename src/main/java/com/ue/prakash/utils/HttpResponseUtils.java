package com.ue.prakash.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

public class HttpResponseUtils {
	private HttpResponseUtils() {
	}

	public static MultiValueMap<String, String> getMap(String message) {

		MultiValueMap<String, String> map = new HttpHeaders();
		map.add("message", message);
		map.add("Access-Control-Allow-Origin", "*");
		map.add("Access-Control-Expose-Headers", "message");
		return map;
	}

	public static <T> ResponseEntity<T> getResponse(HttpStatus status, String message, T object) {
		return new ResponseEntity<T>(object, getMap(message), status);

	}

	public static <T> ResponseEntity<T> getResponse(T object) {
		return new ResponseEntity<T>(HttpStatus.OK);

	}

	public static <T> ResponseEntity<T> getErrorResponse(HttpStatus status, String message) {
		return new ResponseEntity<T>(getMap(message), status);

	}

	public static <T> ResponseEntity<T> getBadRequestErrorResponse(HttpStatus status, String message) {
		return getErrorResponse(HttpStatus.BAD_REQUEST, "Invalid request");

	}

}