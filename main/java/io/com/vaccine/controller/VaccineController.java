package io.com.vaccine.controller;

import java.security.Principal;
import java.util.List;

import javax.servlet.ServletContext;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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

import io.com.vaccine.model.Vaccine;
import io.com.vaccine.repository.VaccineRepository;
import io.com.vaccine.service.VaccineService;
import io.com.vaccine.utils.MapValidationError;
import io.com.vaccine.utils.ReportUtilImpl;

@RestController
@RequestMapping("/api/vaccine")
@CrossOrigin
public class VaccineController {
	
	@Autowired
	VaccineService vaccineService;
	
	@Autowired
	MapValidationError mapValidationError;
	
	@Autowired
	VaccineRepository vaccineRepository;
	
	@Autowired
	ReportUtilImpl reportUtil;
	
	@Autowired
	ServletContext serveletContext;
	
	@GetMapping("/get")
	public ResponseEntity<?> getVaccine(){
		return ResponseEntity.ok().body("Vaccine api");
	}
	
	@GetMapping("/all")
	public ResponseEntity<?> getAllVaccines( 
			@RequestParam(defaultValue = "0") int pageNo, 
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "name") String sortBy){
		List<Vaccine> vaccines=vaccineService.getAllVaccineList(pageNo,pageSize,sortBy);
		
		if(vaccines==null) {
			return new ResponseEntity<>("no vaccines avialiabe",HttpStatus.OK);
		}	
		return ResponseEntity.ok().body(vaccines);
	}
	
	@GetMapping("/{vacId}")
	public ResponseEntity<?> getOneByVacId(@PathVariable(name = "vacId")String vacId){
		Vaccine vaccine=vaccineService.getVaccineByVacId(vacId);
		if(vaccine==null) {
			return new ResponseEntity<>("no vaccine avialiabe of this id",HttpStatus.OK);
		}
		return ResponseEntity.ok().body(vaccine);
	}
	
	@PostMapping("")
	public ResponseEntity<?> saveVaccine(@Valid @RequestBody Vaccine vaccine,BindingResult result,Principal principal){
		ResponseEntity<?> errorMap=mapValidationError.errorMapping(result);
		if(errorMap!=null) {
			return errorMap;
		}
		vaccine.setDoctor(principal.getName());
		Vaccine save=vaccineService.saveVaccine(vaccine);
		return ResponseEntity.ok().body(save);
	}
	
	@PutMapping("/{vacId}")
	public ResponseEntity<?> updateVaccineInfo(@PathVariable(name = "vacId")String vacId,
			@Valid @RequestBody Vaccine vaccine,BindingResult result){
		ResponseEntity<?> update=vaccineService.updateVaccine(vacId, vaccine);
		if(update==null) {
			return new ResponseEntity<>("couldnot be updated",HttpStatus.OK);
		}
		return ResponseEntity.ok().body(update);
	}
	
	@DeleteMapping("/{vacId}")
	public ResponseEntity<?> deleteVaccine(@PathVariable(name = "vacId")String vacId){
		vaccineService.delete(vacId);
		return new ResponseEntity<>("vaccine delted sucessfully",HttpStatus.OK);
	}
	
	
	
	@GetMapping("/search/{filter}")
	public ResponseEntity<?> getByFilter(@PathVariable(name = "filter") String filter,
			 @RequestParam(defaultValue = "0") Integer pageNo, 
             @RequestParam(defaultValue = "10") Integer pageSize,
             @RequestParam(defaultValue = "name") String sortBy){
		ResponseEntity<?> vaccines=vaccineService.findBySearchFilter(filter,pageNo,pageSize,sortBy);
		if(vaccines==null) {
			return new ResponseEntity<>("no vaccines avialiabe",HttpStatus.OK);
		}
		return ResponseEntity.ok().body(vaccines);
	}
	
	@GetMapping("/chart")
	public ResponseEntity<?> generateChart(){
		
		String path=serveletContext.getRealPath("/");
		
		List<Object[]> data=vaccineRepository.findByTypeAndTypeCount();
		reportUtil.generateReport(path, data);
		return ResponseEntity.ok().body(data);
		
		
	}
	
}
