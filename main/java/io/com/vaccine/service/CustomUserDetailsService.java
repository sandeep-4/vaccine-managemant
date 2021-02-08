package io.com.vaccine.service;


import java.util.HashSet;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import io.com.vaccine.model.User;

@Service
public class CustomUserDetailsService implements UserDetailsService{

	
	@Autowired
	UserService userService;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user=userService.findByUsername(username);
		if(user==null) {
			throw new UsernameNotFoundException("User doesnt exists");
		}	
//		return user;
		
		//we might need to update this if it doesnt work
		Set<GrantedAuthority> authority=new HashSet<>();
		authority.add(new SimpleGrantedAuthority(user.getRole().name()));
		
//		user.setAuthority(authority);
		
		//ch1
		
//		return new User(user.getUsername(),user.getPassword(),authority);
//		UserDetails users=new org.springframework.security.core.userdetails.User(user.getUsername()
//				,user.getPassword(),authority);
		
			return user;

	}
	
	@Transactional
	public User loadByUserId(Long id) {
		User user=userService.getById(id);
		if(user==null) {
			throw new UsernameNotFoundException("User doesnt exists");
		}	
		return user;
	}
	
	
	
	
	
	
	
	
	

}
