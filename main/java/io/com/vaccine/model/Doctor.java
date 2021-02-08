package io.com.vaccine.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Data;

@Entity
@Data
public class Doctor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; 
	@Column(unique = true,nullable = false)
	private String docId;
	private String name;
//	private String username;
	private String department;
	private Boolean avialibe;
	private String description;
//	@OneToMany(cascade = CascadeType.ALL,mappedBy = "doctor",fetch = FetchType.EAGER)
//	List<Vaccine> vaccine;
}
