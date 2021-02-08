package io.com.vaccine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.com.vaccine.model.Patient;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long>{

	public Patient findByPatId(String patientId);
	
	public Patient getById(Long id);
}
