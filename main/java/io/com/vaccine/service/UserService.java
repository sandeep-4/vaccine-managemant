package io.com.vaccine.service;



import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import io.com.vaccine.expception.UsernameAlreadyExistException;
import io.com.vaccine.model.Role;
import io.com.vaccine.model.User;
import io.com.vaccine.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	BCryptPasswordEncoder bcrypt;
	
	@Autowired
	FileService fileService;
	
	public User saveUser(User user){
		
		try {
			
			User checkDub=userRepository.findByUsername(user.getUsername());
			if(checkDub!=null) {
				throw new UsernameAlreadyExistException("Username already exists");
			}
			
			user.setPassword(bcrypt.encode(user.getPassword()));
			user.setConfirmPassword("");
			user.setRole(Role.PATIENT);
			
			//image upload
			if(user.getImage()!=null) {
				String savedImageName;
				try {
					savedImageName = fileService.saveProfileImage(user.getImage());
					fileService.deleteProfileImage(user.getImage());
					user.setImage(savedImageName);

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			
			User save=userRepository.save(user);
			return save;
		} catch (Exception e) {
			throw new UsernameAlreadyExistException("Username already exists !!!");
		}
	}
	
	public User findByUsername(String username) {
		User user=userRepository.findByUsername(username);
		if(user==null) {
			throw new UsernameAlreadyExistException("User does not exists");
		}
		return user;
	}
	
	
	public User getById(Long id) {
		User user=userRepository.getById(id);
		if(user==null) {
			throw new UsernameAlreadyExistException("User does not exists");
		}
		return user;
	}
	
	public ResponseEntity<?> updateUser(long id,User user){
		User update=userRepository.getById(id);
		if(user==null) {
			throw new UsernameAlreadyExistException("User does not exists");
		}
		update.setFullname(user.getFullname());
		update.setUsername(user.getUsername());
		update.setPassword(user.getPassword());
		update.setConfirmPassword(user.getConfirmPassword());
		update.setRole(user.getRole());
		
		if(user.getImage()!=null) {
			String savedImageName;
			try {
				savedImageName = fileService.saveProfileImage(user.getImage());
				fileService.deleteProfileImage(update.getImage());
				update.setImage(savedImageName);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
			return ResponseEntity.ok().body(update);
	}
	
	
	
	
}
