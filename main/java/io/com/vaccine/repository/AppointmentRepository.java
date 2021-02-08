package io.com.vaccine.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.com.vaccine.model.Appointment;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long>{

	public Appointment findByUsername(String username);
	
	public Page<Appointment> findAll(Pageable pageable);
	
	public Appointment findByApoId(String apoId);
	
	public Appointment getById(Long id);
}
