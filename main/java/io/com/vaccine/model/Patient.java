package io.com.vaccine.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;

import lombok.Data;

@Entity
@Data
public class Patient {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; 
	@Column(unique = true,nullable = false)
	private String patId;
	private String name;
	@Email
	private String username;
//	@OneToMany(cascade = CascadeType.ALL,mappedBy = "patient",fetch = FetchType.EAGER)
//	List<Vaccine> vaccine;
}
