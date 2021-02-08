package io.com.vaccine.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import io.com.vaccine.expception.AppointmentException;
import io.com.vaccine.model.Appointment;
import io.com.vaccine.repository.AppointmentRepository;


@Service
public class AppointmentService {

	@Autowired
	AppointmentRepository appointmentRepository;
	
	
	public ResponseEntity<?> findAllAppointments(int pageNo,int pageSize ,String sortBy){
		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

		Page<Appointment> appointments= appointmentRepository.findAll(paging);
		if(appointments.hasContent()) {
            List<Appointment> apoList= appointments.getContent();
            Map<String, Object> response = new HashMap<>();
  	      response.put("vaccines", apoList);
  	      response.put("currentPage", appointments.getNumber());
  	      response.put("totalItems", appointments.getTotalElements());
  	      response.put("totalPages", appointments.getTotalPages());
  	      
  	      return new ResponseEntity<>(response,HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No any responses",HttpStatus.OK);
        }
	}
	
	public Appointment createAppointment(Appointment appointment) {
		String aid=(appointment.getFullname().substring(0,3)+UUID.randomUUID().toString().substring(0,3)).toUpperCase();
		appointment.setApoId(aid);
		appointment.setCompleted(false);
		Appointment saveApointment=appointmentRepository.save(appointment);
		if(saveApointment==null) {
			throw new AppointmentException("No appointment aviliable");
		}
		return saveApointment;
	}
	
	public Appointment getOneByAppoId(String apoId) {
		Appointment appointment=appointmentRepository.findByApoId(apoId);
		if(appointment==null) {
			throw new AppointmentException("No appointment aviliable");
		}
		return appointment;
	}
	
	public Appointment getOneByd(long id) {
		Appointment appointment=appointmentRepository.getById(id);
		if(appointment==null) {
			throw new AppointmentException("No appointment aviliable");
		}
		return appointment;
	}
	
	public ResponseEntity<?> updateAppointment(String apoId,Appointment appointment){
		Appointment update=appointmentRepository.findByApoId(apoId);
		if(update==null) {
			throw new AppointmentException("No appointment aviliable to delete");
		}
		update.setApoId(apoId);
		update.setCompleted(appointment.getCompleted());
		update.setAppointmentDate(appointment.getAppointmentDate());
		update.setFullname(appointment.getFullname());
		
		return ResponseEntity.ok().body(update);
	}
	
	
	public void delete(String apoId) {
		Appointment appointment=appointmentRepository.findByApoId(apoId);
		if(appointment==null) {
			throw new AppointmentException("No appointment aviliable to delete");
		}
		appointmentRepository.delete(appointment);
	}
	
	
	
}
