package io.com.vaccine.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import io.com.vaccine.expception.PateintException;
import io.com.vaccine.model.Patient;
import io.com.vaccine.repository.PatientRepository;

@Service
public class PatientService {
	
	@Autowired
	PatientRepository patientRepository;
	
	public Patient savePatient(Patient patient) {
		String patId=(patient.getName().substring(0, 3)+UUID.randomUUID().toString().substring(0,3)).toUpperCase();
		patient.setPatId(patId);
		Patient savedPatient= patientRepository.save(patient);
		if(savedPatient==null) {
			throw new PateintException("Patient could not be saved");
		}
		return savedPatient;	
	}

	public Patient getOnePatient(String patientId) {
		Patient patiet=patientRepository.findByPatId(patientId);
		if(patiet==null) {
			throw new PateintException("Patient could not be saved");
		}
		return patiet;
	}
	
	public List<Patient> getAllPatient() {
		List<Patient> patient=patientRepository.findAll();
		if(patient.isEmpty()) {
			throw new PateintException("Patient could not be saved");
		}
		return patient;
	}
	
	public ResponseEntity<?> updatePatient(String patId,Patient patient){
		Patient updtPatient=getOnePatient(patId);
		
		updtPatient.setPatId(patId);
		updtPatient.setUsername(patient.getUsername());
		updtPatient.setName(patient.getName());
		return ResponseEntity.ok().body(updtPatient);
	}
	
	public void delete(String patId) {
		Patient del=getOnePatient(patId);
		patientRepository.delete(del);
		
	}
	
	public Patient getById(Long id) {
		Patient patient=patientRepository.getById(id);
		if(patient==null) {
			throw new PateintException("Patient could not be saved");
		}
		return patient;
	}
	
	
	
}
