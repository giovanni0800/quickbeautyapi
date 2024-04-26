package com.quick.beauty.quickbeautyapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.quick.beauty.quickbeautyapi.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	// Todos os Repositories são interfaces para trabalhar diretamente com
	// as entities do DB
	
	@Query(value = "SELECT username FROM TB_USER WHERE email ilike :email", nativeQuery = true)
	String findUsername(String email);
	
	@Query(value = "SELECT * FROM TB_USER WHERE username ilike :username", nativeQuery = true)
	User findByUsername(String username);
	
	@Query(value = "SELECT * FROM TB_USER WHERE email ilike :email", nativeQuery = true)
	User findByEmail(String email);
	
	@Query(value = "SELECT COUNT(*) FROM TB_USER WHERE cpf = :cpf", nativeQuery = true)
	Integer cpfExists(String cpf);
	
	@Query(value = "SELECT COUNT(*) FROM TB_USER WHERE email ilike :email", nativeQuery = true)
	Integer emailExists(String email);
	
	@Query(value = "SELECT active FROM TB_USER SET WHERE email ilike :email", nativeQuery = true)
	boolean isUserActive(String email);
	
	@Modifying
	@Query(value = "UPDATE TB_USER SET password = :password WHERE email ilike :email", nativeQuery = true)
	void updatePassword(String email, String password);
	
	@Modifying
	@Query(value = "UPDATE TB_USER SET user_image = :userImage WHERE email ilike :email", nativeQuery = true)
	void updateUserImage(String email, String userImage);
	
	@Modifying
	@Query(value = "UPDATE TB_USER SET type = :type WHERE email ilike :email", nativeQuery = true)
	void updateUserType(String email, String type);
	
	@Modifying
	@Query(value = "UPDATE TB_USER SET evaluation_note = :evaluationNote WHERE email ilike :email", nativeQuery = true)
	void updateUserEvaluationNote(String email, Double evaluationNote);
	
	@Modifying
	@Query(value = "UPDATE TB_USER SET active = true WHERE cpf = :cpf", nativeQuery = true)
	void unlockUser(String cpf);
	
	@Modifying
	@Query(value = "UPDATE TB_USER SET active = false WHERE cpf = :cpf", nativeQuery = true)
	void lockUser(String cpf);
	
	@Modifying
	@Query(value = "UPDATE TB_USER SET username = :username, date_of_birth = :dateOfBirth, email = :email, phone_number = :phoneNumber, cep = :cep, house_number = :houseNumber WHERE cpf = :cpf", nativeQuery = true)
	void updateUser(String username, String cpf, String dateOfBirth, String email, String phoneNumber, String cep, String houseNumber);
}
