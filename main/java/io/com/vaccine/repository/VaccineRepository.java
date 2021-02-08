package io.com.vaccine.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import io.com.vaccine.model.Vaccine;

@Repository
public interface VaccineRepository extends JpaRepository<Vaccine, Long>{

	public Vaccine findByVacId(String vaccineId);
	
	public Vaccine getById(Long id);
	
	public Page<Vaccine> findAll(Pageable pageable);
	
	@Query("SELECT v FROM Vaccine v WHERE CONCAT(v.name,' ',v.vacId,' ',v.type) LIKE %?1%")
	public Page<Vaccine> findAllBySearchVaccineName(String filter,Pageable pageable);
	
	@Query("SELECT type,COUNT(type) FROM Vaccine GROUP BY type")
	public List<Object[]> findByTypeAndTypeCount();
}
