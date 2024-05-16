package com.quick.beauty.quickbeautyapi.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.quick.beauty.quickbeautyapi.entities.Professional;

@Repository
public interface ProfessionalRepository extends JpaRepository<Professional, Long>{

	@Modifying
	@Query(value = "DELETE FROM tb_professional WHERE user_id = :user_id", nativeQuery = true)
	void deleteUserById(Long user_id);
	
	@Query(value = "SELECT * FROM tb_professional WHERE user_id = :user_id", nativeQuery = true)
	Professional findByUserId(Long user_id);
	
	@Query(value = "SELECT id FROM tb_professional WHERE user_id = :user_id", nativeQuery = true)
	Long getLongProfessionalId(Long user_id);
	
	@Query(value = "SELECT user_id  FROM tb_professional WHERE specialty ilike :specialty", nativeQuery = true)
	List<Long> getProfessionalService(String specialty);
}
