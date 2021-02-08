package io.com.vaccine.utils;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

	@NotBlank(message = "Email cant be blank")
	private String username;
	@NotBlank(message = "Password cant be blank")
	private String password;
}
