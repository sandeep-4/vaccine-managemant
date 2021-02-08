package io.com.vaccine.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;

import lombok.Data;


@Entity
@Data
public class Appointment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
    private String apoId;
    private String fullname;
	@Email
	private String username;
//	@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER,mappedBy = "vaccine")
//	List<Vaccine> vaccine;
	String vaccines[];
	private Date appointmentDate;
	private Boolean completed;
	
	
	
}
