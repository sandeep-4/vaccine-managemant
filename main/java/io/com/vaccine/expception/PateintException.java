package io.com.vaccine.expception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PateintException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	String message;
     
	public PateintException(String message) {
		super(message);
		
	}
	
	
}
