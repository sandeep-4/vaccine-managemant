package io.com.vaccine.controller;

import java.io.IOException;
import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import io.com.vaccine.expception.UsernameAlreadyExistException;
import io.com.vaccine.model.User;
import io.com.vaccine.security.JwtTokenProvider;
import io.com.vaccine.service.UserService;
import io.com.vaccine.utils.FileUploadUtil;
import io.com.vaccine.utils.JwtLoginResponse;
import io.com.vaccine.utils.LoginRequest;
import io.com.vaccine.utils.MapValidationError;
import io.com.vaccine.utils.UserValidator;

@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	MapValidationError mapValidationError;
	
	@Autowired
	UserValidator userValidator;
	
	@Autowired
	JwtTokenProvider tokenProvider;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	FileUploadUtil fileUploadUtil;

	@RequestMapping(value = "/get",method = RequestMethod.GET)
	public ResponseEntity<?> getString(){
		return ResponseEntity.ok().body("Hello from spring");
	}

	
	@PostMapping("/register")
//	public ResponseEntity<?> registerUser(@Valid @RequestBody User user,
//	@RequestPart(name = "image")MultipartFile multipart,BindingResult result){
	public ResponseEntity<?> registerUser(@Valid @RequestBody User user,BindingResult result){
		
		userValidator.validate(user, result);
		
//		 String fileName = StringUtils.cleanPath(multipart.getOriginalFilename());
//	        user.setImage(fileName);
//	        String uploadDir = "/images/profile/" + user.getId();
//	        try {
//				fileUploadUtil.saveFile(uploadDir, fileName, multipart);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
		
		
		ResponseEntity<?> errorMap=mapValidationError.errorMapping(result);
		if(errorMap!=null) {
			return errorMap;
		}
		
		User savedUser=userService.saveUser(user);
		if(savedUser==null) {
			return ResponseEntity.ok().body("user is not able to saved");
		}
		return ResponseEntity.ok().body(savedUser);
	}
	
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody LoginRequest login,BindingResult result){
		ResponseEntity<?> errorMap=mapValidationError.errorMapping(result);
		if(errorMap!=null) {
			return errorMap;
		}
		
		Authentication authentication=authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						login.getUsername()
						, login.getPassword())
				);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt="Bearer "+tokenProvider.generateToken(authentication);
		
		return ResponseEntity.ok().body(new JwtLoginResponse(true,jwt));	
		
	}
	
	
	@PutMapping("/{id}")
	public ResponseEntity<?> updateUser(@PathVariable(name = "id") long id,
			@Valid @RequestBody User user,BindingResult result){
		ResponseEntity<?> errorMap=mapValidationError.errorMapping(result);
		if(errorMap!=null) {
			return errorMap;
		}
		ResponseEntity<?> update=userService.updateUser(id, user);
		return update;
	}
	
	@GetMapping("/profile")
	public ResponseEntity<?> getCurrentUser(Principal principal){
		String username=principal.getName();
		User user=userService.findByUsername(username);
		if(user==null) {
			throw new UsernameAlreadyExistException("User not logged in");
		}
		return ResponseEntity.ok().body(user);
	}
	
	
	
}
