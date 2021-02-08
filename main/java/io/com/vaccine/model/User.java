package io.com.vaccine.model;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User implements UserDetails{
	
	//Image upload remaining

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotBlank(message = "Fullname cant be blank")
	private String fullname;
	@NotBlank(message = "Email cant be blank")
	@Email(message = "Email must contain @ and .")
	@Column(unique = true)
	private String username;
	@NotBlank(message = "Password cant be blank")
	private String password;
	
	@Transient
	private String confirmPassword;
//	@Transient
//	private Set<GrantedAuthority> authority;

	
	@Enumerated(EnumType.STRING)
	@Column(name = "role")
	private Role role;
	
	private String image;	
	
	private Date created_At;
	private Date updated_At;
	
	@Override
	@JsonIgnore
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singleton(new SimpleGrantedAuthority("DOCTOR"));

		
//		User user;
//		List<Role> roles = user.role;
//		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
//		
//		for (Role role : roles) {
//			authorities.add(new SimpleGrantedAuthority(user.role));
//		}
//		
//		return authorities;
		
		
	}
	
	@Override
	@JsonIgnore
	public boolean isAccountNonExpired() {
		return true;
	}
	@JsonIgnore
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	@JsonIgnore
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	@JsonIgnore
	@Override
	public boolean isEnabled() {
		return true;
	}
	@PrePersist
	public void onCreate() {
		this.created_At = new Date();
	}

	@PreUpdate
	public void onUpdate() {
		this.updated_At = new Date();
	}

	public Role getRole() {
		return role;
	}


//
//	public User(String username, String password, Set<GrantedAuthority> authority) {
//		this.username=username;
//		this.password=password;
//		this.authority=authority;
//	}

}
