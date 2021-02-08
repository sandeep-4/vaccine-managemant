package io.com.vaccine.controller;

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

import io.com.vaccine.model.Doctor;
import io.com.vaccine.service.DoctorService;
import io.com.vaccine.utils.MapValidationError;

@RestController
@RequestMapping("/api/doctor")
@CrossOrigin
public class DoctorController {
 
	@Autowired
	DoctorService doctorService;
	
	@Autowired
	MapValidationError mapValidationError;
	
	
	@GetMapping("/get")
	public ResponseEntity<?> getVaccine(){
		return ResponseEntity.ok().body("Doctor api");
	}
	
	@GetMapping("")
	public ResponseEntity<?> getAllDoctors(){
		List<Doctor> doctors=doctorService.getAllDoctors();
		return new ResponseEntity<>(doctors,HttpStatus.OK);
	}
	
	@PostMapping("")
	public ResponseEntity<?> saveDoctor(@Valid @RequestBody Doctor doctor,BindingResult result){
		ResponseEntity<?> errorMap=mapValidationError.errorMapping(result);
		if(errorMap!=null) {
			return errorMap;
		}
		Doctor save=doctorService.saveDoctor(doctor);	
		return ResponseEntity.ok().body(save);
	}

	@GetMapping("/{docId}")
	public ResponseEntity<?> getDoctorByDocId(@PathVariable(name = "docId") String docId){
		Doctor doctor=doctorService.getDoctorByDocId(docId);
		if(doctor==null) {
		return ResponseEntity.ok().body("No doctor with such id");
		}
		return ResponseEntity.ok().body(doctor);
	}
	

	@GetMapping("/id/{id}")
	public ResponseEntity<?> getById(@PathVariable(name = "id") Long id){
		Doctor doctor=doctorService.getDoctorById(id);
		if(doctor==null) {
		return ResponseEntity.ok().body("No doctor with such id");
		}
		return ResponseEntity.ok().body(doctor);
	}
	
	@PutMapping("/{docId}")
	public ResponseEntity<?> updateDoctor(@PathVariable(name = "docId") String docId,
			@Valid @RequestBody Doctor doctor,BindingResult result){
		ResponseEntity<?> errorMap=mapValidationError.errorMapping(result);
		if(errorMap!=null) {
			return errorMap;
		}
		ResponseEntity<?> docs=doctorService.updateDoctor(docId, doctor);
		return docs;
	}
	
	@DeleteMapping("/{docId}")
	public ResponseEntity<?> deleteDoctor(@PathVariable(name = "docId") String docId){
		
		doctorService.delete(docId);
		return new ResponseEntity<>("The doctor has been deleted",HttpStatus.OK);
	}
	
	
	

}
