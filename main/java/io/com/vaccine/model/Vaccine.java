package io.com.vaccine.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;

@Entity
@Data
public class Vaccine {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true,nullable = false)
	private String vacId;
	private String name;
	private String type;
	private int noOfVacine;
	private Boolean avialiablity;
	@Column(length = 1000000)
	private String description;
	String doctor;
//	@ManyToOne(cascade = CascadeType.ALL)
//	@JoinColumn(name = "doctor_id")
//	Doctor doctor;
//	@ManyToOne(cascade = CascadeType.ALL)
//	@JoinColumn(name = "patient_id")
//	Patient patient;
	
	
}
