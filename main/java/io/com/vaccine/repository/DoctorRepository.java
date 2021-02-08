package io.com.vaccine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.com.vaccine.model.Doctor;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long>{

	public Doctor findByDocId(String doctorId);
	
	public Doctor getById(Long id);
}
