package com.quick.beauty.quickbeautyapi.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quick.beauty.quickbeautyapi.entities.AdherenceOfRegistrationsPerDay;
import com.quick.beauty.quickbeautyapi.repositories.AdherenceOfRegistrationsPerDayRepository;

import jakarta.annotation.security.RolesAllowed;

@RestController
@RequestMapping(value = "/AdherenceOfRegistrations")
public class AdherenceOfRegistrationsPerDayController {
	
	@Autowired
	private AdherenceOfRegistrationsPerDayRepository adherencesRepo;
	
	@RolesAllowed( { "ROLE_ADMIN" } )
	@GetMapping(value = "today")
	@Transactional(propagation = Propagation.REQUIRED)
	public ResponseEntity<AdherenceOfRegistrationsPerDay> allAdherence() {
		AdherenceOfRegistrationsPerDay adherences = new AdherenceOfRegistrationsPerDay();
		
		adherences.setHowManyClients( adherencesRepo.countClients() );
		adherences.setHowManyProfessionals( adherencesRepo.countProfessionals() );
		adherences.setHowManyUsers( adherencesRepo.countUsers() );
		adherences.setHowManyAdmins( adherencesRepo.countAdmins() );
		
		if( adherencesRepo.countTodayAdherences( adherences.getToday() ) <= 0 ) {
			
			adherencesRepo.save( adherences );
		
		} else {
			adherencesRepo.updateAdherences(
					adherences.getToday(), adherences.getHowManyAdmins(),
					adherences.getHowManyClients(), adherences.getHowManyProfessionals(),
					adherences.getHowManyUsers() );
		}
		
		return ResponseEntity.status( HttpStatus.OK ).body( adherences );
	}

	@RolesAllowed( { "ROLE_ADMIN" } )
	@GetMapping
	public ResponseEntity<?> todayAdherence() {
		List<AdherenceOfRegistrationsPerDay> adherences = adherencesRepo.findAll();
		
		return ResponseEntity.status(HttpStatus.OK).body(adherences);
	}
	
	//Fazer uma pesquisa no banco por datas específicas

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN)
				.body("Usuário não possui nível de autenticação para acesso dos dados...");
	}

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<String> handleResourceNotFoundException(ConfigDataResourceNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Caminho solicitado não existe...");
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleGenericException(Exception ex) {
		System.out.println("\n\n" + ex);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body("Algo deu errado com o processamento da solicitação... " + ex.getMessage());
	}

}
