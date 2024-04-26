package com.quick.beauty.quickbeautyapi.repositories;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.quick.beauty.quickbeautyapi.entities.AdherenceOfRegistrationsPerDay;

@Repository
public interface AdherenceOfRegistrationsPerDayRepository extends JpaRepository<AdherenceOfRegistrationsPerDay, Long> {

	@Query(value = "SELECT COUNT(*) FROM tb_admin", nativeQuery = true)
	Long countAdmins();
	
	@Query(value = "SELECT COUNT(*) FROM tb_client", nativeQuery = true)
	Long countClients();
	
	@Query(value = "SELECT COUNT(*) FROM tb_professional", nativeQuery = true)
	Long countProfessionals();
	
	@Query(value = "SELECT COUNT(*) FROM tb_user", nativeQuery = true)
	Long countUsers();
	
	@Query(value = "SELECT COUNT(*) FROM tb_adherence_of_registrations_per_day WHERE today = :today", nativeQuery = true)
	Long countTodayAdherences(LocalDate today);
	
	@Modifying
	@Query(value = "UPDATE tb_adherence_of_registrations_per_day SET today = :today, how_many_admins = :howManyAdmins, how_many_clients = :howManyClients, how_many_professionals = :howManyProfessionals, how_many_users = :howManyUsers WHERE today = :today", nativeQuery = true)
	void updateAdherences(LocalDate today, Long howManyAdmins, Long howManyClients, Long howManyProfessionals, Long howManyUsers);
}
