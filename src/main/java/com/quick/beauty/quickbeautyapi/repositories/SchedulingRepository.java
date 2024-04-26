package com.quick.beauty.quickbeautyapi.repositories;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.quick.beauty.quickbeautyapi.entities.Scheduling;

@Repository
public interface SchedulingRepository extends JpaRepository<Scheduling, Long> {

	@Query(value = "SELECT id FROM tb_user WHERE email ilike :email", nativeQuery = true)
	Long searchUserId(String email);
	
	@Query(value = "SELECT id FROM tb_client WHERE user_id = :userId", nativeQuery = true)
	Long searchClientId(Long userId);
	
	@Query(value = "SELECT id FROM tb_professional WHERE user_id = :userId", nativeQuery = true)
	Long searchProfessionalId(Long userId);
	
	@Query(value = "SELECT id FROM tb_service WHERE service_description ilike :serviceDescription AND gathering_place ilike :gatheringPlace AND date_of_making_the_request = :dateOfMakingTheRequest AND distance = :distance", nativeQuery = true)
	Long searchServiceId(String serviceDescription, String gatheringPlace, LocalDateTime dateOfMakingTheRequest, String distance); // não esquecer de colocar "km" no valor
	
	@Query(value = "SELECT id FROM tb_payment WHERE pay_day = :payDay AND client_id = :clientId AND professional_id = :professionalId", nativeQuery = true)
	Long searchPaymentId(LocalDateTime payDay, Long clientId, Long professionalId);
	
}
