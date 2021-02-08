package io.com.vaccine.utils;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import io.com.vaccine.model.User;

@Component
public class UserValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		return User.class.equals(clazz);
	}

	@Override
	public void validate(Object object, Errors errors) {
		User user=(User)object;
		
		if(user.getPassword().length()< 8) {
			errors.rejectValue("password","Length","Password must be 8 or more characters");
		}
		
		if(!user.getPassword().equals(user.getConfirmPassword())) {
			errors.rejectValue("password", "match","Password must match");
		}
	}

}
