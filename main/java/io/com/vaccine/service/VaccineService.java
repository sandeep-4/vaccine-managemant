package io.com.vaccine.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import io.com.vaccine.expception.VaccineException;
import io.com.vaccine.model.Vaccine;
import io.com.vaccine.repository.VaccineRepository;
import io.com.vaccine.utils.EmailUtil;
import io.com.vaccine.utils.EmailUtilImpl;
import io.com.vaccine.utils.PDFGenerator;

@Service
public class VaccineService {

	@Autowired
	VaccineRepository vaccineRepository;
	
	@Autowired
	EmailUtilImpl emailUtil;
	
	@Autowired
	PDFGenerator pdfGenerator;
	
	public ResponseEntity<?> getAllVaccine(int pageNo,int pageSize ,String sortBy){
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        
      	Page<Vaccine> vaccines= vaccineRepository.findAll(paging);
		if(vaccines.hasContent()) {
            List<Vaccine> vacList= vaccines.getContent();
            Map<String, Object> response = new HashMap<>();
  	      response.put("vaccines", vacList);
  	      response.put("currentPage", vaccines.getNumber());
  	      response.put("totalItems", vaccines.getTotalElements());
  	      response.put("totalPages", vaccines.getTotalPages());
			
  	      return new ResponseEntity<>(response,HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No any responses",HttpStatus.OK);
        }
		
	}
	
	public List<Vaccine> getAllVaccineList(int pageNo,int pageSize ,String sortBy){
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        
        List<Vaccine> vaccines=vaccineRepository.findAll(paging).getContent();
        return vaccines;
	}

		
	
	
	public Vaccine getVaccineByVacId(String vacId) {
		Vaccine vaccine=vaccineRepository.findByVacId(vacId);
		if(vaccine==null) {
			throw new VaccineException("No such vaccine found");
		}
		return vaccine;
	}
	
	public Vaccine getById(long id) {
		Vaccine vaccine=vaccineRepository.getById(id);
		if(vaccine==null) {
			throw new VaccineException("No such vaccine found");
		}
		return vaccine;
	}
	
	public Vaccine saveVaccine(Vaccine vaccine) {
		String vacId=(vaccine.getName().substring(0,3)+UUID.randomUUID().toString().substring(0,3)).toUpperCase();
		vaccine.setVacId(vacId);
		Vaccine save=vaccineRepository.save(vaccine);
		
		String filePath = "D:\\workspace\\vaccine-api\\images\\attachments"+save.getName()+save.getId()+".pdf";
		pdfGenerator.generateItetenary(save, filePath);
		
		
//		if(save==null) {
//			throw new VaccineException("No such vaccine found");
//		}
	
		emailUtil.sendEmailPdf(vaccine.getDoctor(), filePath);
		
		return save;
	}
	
	public ResponseEntity<?> updateVaccine(String vacId,Vaccine vaccine){
		Vaccine update=getVaccineByVacId(vacId);
	
		update.setVacId(vacId);
		update.setAvialiablity(vaccine.getAvialiablity());
		update.setDescription(vaccine.getDescription());
		update.setNoOfVacine(vaccine.getNoOfVacine());
		update.setType(vaccine.getType());
		update.setName(vaccine.getName());
		
		return ResponseEntity.ok().body(update);
		
	}
	
	public void delete(String vacId) {
		Vaccine del=getVaccineByVacId(vacId);
		vaccineRepository.delete(del);
	}
	
	
	
	public ResponseEntity<?> findBySearchFilter(String filter,int pageNo,int pageSize,String sortBy){
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
		Page<Vaccine> vaccines=vaccineRepository.findAllBySearchVaccineName(filter,paging);
		
		 if(vaccines.hasContent()) {
	            List<Vaccine> vacList= vaccines.getContent();
	            Map<String, Object> response = new HashMap<>();
	  	      response.put("vaccines", vacList);
	  	      response.put("currentPage", vaccines.getNumber());
	  	      response.put("totalItems", vaccines.getTotalElements());
	  	      response.put("totalPages", vaccines.getTotalPages());
	  	      
	  	      return new ResponseEntity<>(response,HttpStatus.OK);
	        } else {
	            return new ResponseEntity<>("No any vaccine of this type or name",HttpStatus.OK);
	        }
			}
	

	
	
}
