package io.com.vaccine.expception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvalidLoginResponse {

	private String username;
	private String password;
	
	public InvalidLoginResponse() {
		this.username = "Invalid username";
		this.password = "Invalid password";
	}
	
	
}
