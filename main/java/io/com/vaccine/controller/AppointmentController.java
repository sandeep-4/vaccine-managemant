package io.com.vaccine.controller;

import java.security.Principal;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.com.vaccine.expception.AppointmentException;
import io.com.vaccine.model.Appointment;
import io.com.vaccine.service.AppointmentService;
import io.com.vaccine.utils.EmailUtilImpl;
import io.com.vaccine.utils.MapValidationError;

@RestController
@RequestMapping("/api/appointment")
@CrossOrigin
public class AppointmentController {

	@Autowired
	AppointmentService appointmentService;
	
	@Autowired
	MapValidationError mapValidationError;
	
	@Autowired
	EmailUtilImpl emailUtil;
	
	@GetMapping("")
	public ResponseEntity<?> getAllAppointments(
			@RequestParam(defaultValue = "0") int pageNo, 
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy){
		
		ResponseEntity<?> appointments=appointmentService.findAllAppointments(pageNo, pageSize, sortBy);
		if(appointments==null) {
			throw new AppointmentException("No any appointments");
		}
		return appointments;
	}
	
	@PostMapping("")
	public ResponseEntity<?> createAppointment(@Valid @RequestBody Appointment appointment,
			BindingResult result,Principal principal){
		appointment.setVaccines(appointment.getVaccines());
		ResponseEntity<?> errorMap=mapValidationError.errorMapping(result);
		if(errorMap!=null) {
			return errorMap;
		}
		appointment.setUsername(principal.getName());
		
		emailUtil.sendEmail(principal.getName(), "Appointment", "Sir,Your appointment is all set !");
		
		Appointment save=appointmentService.createAppointment(appointment);
		if(save==null) {
			throw new AppointmentException("could not be saved");
		}
		return ResponseEntity.ok().body(save);
	}
	
	@GetMapping("/{apoId}")
	public ResponseEntity<?> getOneByApoId(@PathVariable(name = "apoId") String apoId){
		Appointment appo=appointmentService.getOneByAppoId(apoId);
		if(appo==null) {
			throw new AppointmentException("could not be saved");
		}
		return ResponseEntity.ok().body(appo);
	}
	
	@PutMapping("/{apoId}")
	public ResponseEntity<?> updateAppointment(@PathVariable(name = "apoId") String apoId,
			@Valid @RequestBody Appointment appointment,BindingResult result,Principal principal){
		ResponseEntity<?> errorMap=mapValidationError.errorMapping(result);
		if(errorMap!=null) {
			return errorMap;
		}
		appointment.setUsername(principal.getName());
		ResponseEntity<?> update =appointmentService.updateAppointment(apoId, appointment);
		return update;
	}
	
	@DeleteMapping("/{apoId}")
	public ResponseEntity<?> deleteMapping(@PathVariable(name = "apoId") String apoId){
		appointmentService.delete(apoId);
		return ResponseEntity.ok().body("appointment deleted sucessfully");

	}
	
	
	
	
	
	
	
}
