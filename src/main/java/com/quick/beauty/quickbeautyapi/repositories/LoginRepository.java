package com.quick.beauty.quickbeautyapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.quick.beauty.quickbeautyapi.entities.User;

@Repository
public interface LoginRepository extends JpaRepository<User, Long> {
	
	@Query(value = "SELECT * FROM TB_USER WHERE email ilike :email AND password = :password", nativeQuery = true)
	public User logar(String email, String password);

}
