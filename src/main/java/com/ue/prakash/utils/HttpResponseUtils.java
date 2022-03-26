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
		return new ResponseEntity<>(object, getMap(message), status);

	}


}