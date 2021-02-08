package io.com.vaccine.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import io.com.vaccine.expception.DoctorException;
import io.com.vaccine.model.Doctor;
import io.com.vaccine.repository.DoctorRepository;

@Service
public class DoctorService {

	@Autowired
	DoctorRepository doctorRepository;
	
	public List<Doctor> getAllDoctors(){
		List<Doctor> doctors=doctorRepository.findAll();
		if(doctors.isEmpty()) {
			throw new DoctorException("No doctors");
		}
		return doctors;
	}
	
	public Doctor getDoctorByDocId(String docId) {
		Doctor doctor=doctorRepository.findByDocId(docId);
		if(doctor==null) {
			throw new DoctorException("No doctor found");
		}
		return doctor;
	}
	
	public Doctor getDoctorById(long id) {
		Doctor doctor=doctorRepository.getById(id);
		if(doctor==null) {
			throw new DoctorException("No doctor found");
		}
		return doctor;
	}
	
	public Doctor saveDoctor(Doctor doctor) {
		String docId=(doctor.getName().substring(0,3)+UUID.randomUUID().toString().substring(0,3)).toUpperCase();
		doctor.setDocId(docId);
	Doctor save=doctorRepository.save(doctor);
	if(save==null) {
		throw new DoctorException("No doctor found");
	}
	return save;
	}
	
	public ResponseEntity<?> updateDoctor(String docId,Doctor doctor){
		Doctor update=getDoctorByDocId(docId);
		
		update.setDocId(docId);
		update.setAvialibe(doctor.getAvialibe());
		update.setDepartment(doctor.getDepartment());
		update.setDescription(doctor.getDescription());
		update.setName(doctor.getName());
//		update.setVaccine(doctor.getVaccine());
		return ResponseEntity.ok().body(update);
	}
	
	public void delete(String docId) {
		Doctor del=getDoctorByDocId(docId);
		doctorRepository.delete(del);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
