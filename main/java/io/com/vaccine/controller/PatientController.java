package io.com.vaccine.controller;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.com.vaccine.model.Patient;
import io.com.vaccine.service.PatientService;
import io.com.vaccine.utils.MapValidationError;

@RestController
@RequestMapping("/api/patient")
@CrossOrigin
public class PatientController {
	
	@Autowired
	PatientService patientService;
	
	@Autowired
	MapValidationError mapValidationError;
	
	@GetMapping("/get")
	public ResponseEntity<?> getVaccine(){
		return ResponseEntity.ok().body("patient api");
	}
	
	
	
	@GetMapping("")
	public ResponseEntity<?> getAllPatients(){
		List<Patient> patients=patientService.getAllPatient();
		return new ResponseEntity<>(patients,HttpStatus.OK);
	}
	
	@PostMapping("")
	public ResponseEntity<?> savePatient(@Valid @RequestBody Patient patient,BindingResult result
			,Principal principal){
		ResponseEntity<?> errorMap=mapValidationError.errorMapping(result);
		if(errorMap!=null) {
			return errorMap;
		}
		patient.setUsername(principal.getName());
		Patient save=patientService.savePatient(patient);	
		return ResponseEntity.ok().body(save);
	}

	@GetMapping("/{patId}")
	public ResponseEntity<?> getPatientByPatId(@PathVariable(name = "patId") String patId){
		Patient patient=patientService.getOnePatient(patId);
		if(patient==null) {
		return ResponseEntity.ok().body("No patient with such id");
		}
		return ResponseEntity.ok().body(patient);
	}
	

	@GetMapping("/id/{id}")
	public ResponseEntity<?> getById(@PathVariable(name = "id") Long id){
		Patient patient=patientService.getById(id);
		if(patient==null) {
		return ResponseEntity.ok().body("No patient with such id");
		}
		return ResponseEntity.ok().body(patient);
	}
	
	@PutMapping("/{patId}")
	public ResponseEntity<?> updatePatient(@PathVariable(name = "patId") String patId,
			@Valid @RequestBody Patient patient,BindingResult result){
		ResponseEntity<?> errorMap=mapValidationError.errorMapping(result);
		if(errorMap!=null) {
			return errorMap;
		}
		ResponseEntity<?> docs=patientService.updatePatient(patId, patient);
		return docs;
	}
	
	@DeleteMapping("/{patId}")
	public ResponseEntity<?> deletePatient(@PathVariable(name = "patId") String patId){	
		patientService.delete(patId);
		return new ResponseEntity<>("The patient has been deleted",HttpStatus.OK);
	}
	

}
