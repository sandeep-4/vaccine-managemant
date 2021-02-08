package io.com.vaccine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.com.vaccine.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

	public User findByUsername(String username);
	public User getById(Long id);
}
