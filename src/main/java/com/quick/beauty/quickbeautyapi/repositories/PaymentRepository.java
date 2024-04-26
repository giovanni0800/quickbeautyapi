package com.quick.beauty.quickbeautyapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.quick.beauty.quickbeautyapi.entities.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

	// Todos os Repositories são interfaces para trabalhar diretamente com
	// as entities do DB

}
