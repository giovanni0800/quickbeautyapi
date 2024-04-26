package com.quick.beauty.quickbeautyapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.quick.beauty.quickbeautyapi.entities.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long>{
	
	@Query(value = "SELECT * FROM tb_client WHERE user_id = :user_id", nativeQuery = true)
	Client findByUserId(Long user_id);
	
	//CRIAR UMA QUERY PARA RETORNAR O ID DO CLIENTE, NÃO DO USER
	@Query(value = "SELECT id FROM tb_client WHERE user_id = :user_id", nativeQuery = true)
	Long getLongClientId(Long user_id);

}
