package com.quick.beauty.quickbeautyapi.controllers;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.quick.beauty.quickbeautyapi.entities.Scheduling;
import com.quick.beauty.quickbeautyapi.entities.Service;
import com.quick.beauty.quickbeautyapi.repositories.SchedulingRepository;
import com.quick.beauty.quickbeautyapi.repositories.ServiceRepository;
import com.quick.beauty.quickbeautyapi.repositories.UserRepository;
import com.quick.beauty.quickbeautyapi.service.GoogleMapsService;
import com.quick.beauty.quickbeautyapi.service.PaymentService;

import jakarta.annotation.security.RolesAllowed;

@RestController
@RequestMapping(value = "/servicesolicitation")
public class ServiceController {
	
	@Autowired
	Scheduling scheduling;
	
	@Autowired
	SchedulingRepository repoScheduling;
	
	@Autowired
	ServiceRepository serviceRepo;
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	PaymentController payController;
	
	@Autowired
	PaymentService payService;
	
	@Autowired
	GoogleMapsService distanceCalculation;
	
	@RolesAllowed( { "ROLE_CLIENT", "ROLE_PROFESSIONAL" } )
	@PostMapping
	//Adicionar os Params da request
	public ResponseEntity<String> registerServices(
			@RequestParam String clientEmail,
			@RequestParam String professionalEmail,
			@RequestParam String origins,
			@RequestParam String destination,
			@RequestParam String serviceDescription,
			@RequestParam String gatheringPlace,
			@RequestParam String price,
			@RequestParam String description){
		
		Service service = new Service();
		String distance = distanceCalculation.getDistance( distanceCalculation.getMapsObject(origins, destination ) ).replaceAll(" mi", "");
		
		if ( ( Double.parseDouble(distance) * 1.60934) <= 5 ) {
			
			if( repoScheduling.searchClientId( repoScheduling.searchUserId(clientEmail) ) == null ) {
				return ResponseEntity.status(HttpStatusCode.valueOf(400)).body( "Cliente não existe na base de dados..." );
			
			} else if( repoScheduling.searchProfessionalId( repoScheduling.searchUserId(professionalEmail) ) == null ) {
				return ResponseEntity.status(HttpStatusCode.valueOf(400)).body( "Profissional não existe na base de dados...");
				
			} else {
				Long clientId = repoScheduling.searchUserId(clientEmail);
				Long professionalId = repoScheduling.searchUserId(professionalEmail);
				
				Map<String, Object> serviceInformations = new HashMap<>();
				LocalDateTime now = LocalDateTime.now();

				//SE EU QUISER, POSSO ALTERAR OS VALORES DO LOCAL AQUI EMBAIXO DO GOOGLE MAPS DESTINATION
		        // Adicionar os valores ao mapa
				serviceInformations.put("email", clientEmail);
				serviceInformations.put("description", description);
				serviceInformations.put("firstName", userRepo.findUsername(clientEmail));
				serviceInformations.put("lastName", "");
				serviceInformations.put("cep", destination);
		        serviceInformations.put("streetName", "Av. das Nações Unidas");
		        serviceInformations.put("streetNumber", "3003");
		        serviceInformations.put("neighborhood", "Bonfim");
		        serviceInformations.put("city", "Osasco");
		        serviceInformations.put("federalUnit", "SP");
		        serviceInformations.put("time", now);
		        serviceInformations.put("price", price);
		        serviceInformations.put("client", clientId);
		        serviceInformations.put("professional", professionalId);
				
				payService.realizePixPayment(serviceInformations);
				
				service.setServiceDescription(serviceDescription);
				service.setGatheringPlace(gatheringPlace);
				service.setDateOfMakingTheRequest( now );
				service.setDistance(distance);
				service.setServiceFinished(false);
				
				serviceRepo.save(service);
				
				scheduling.setClientId( repoScheduling.searchClientId( clientId ) );
				scheduling.setProfessionalId( repoScheduling.searchProfessionalId( professionalId ) );
				scheduling.setServiceId( repoScheduling.searchServiceId(serviceDescription, gatheringPlace, now, distance + "km") );
				scheduling.setMarkedDate( now );
				scheduling.setPaymentId( repoScheduling.searchPaymentId(service.getDateOfMakingTheRequest(), scheduling.getClientId(), scheduling.getProfessionalId() ) );
				
				repoScheduling.save( scheduling );
				
				return ResponseEntity.status(HttpStatus.OK).body( "Serviço agendado com sucesso! Usuários encontram-se dentro do raio de atendimento." );
			}
		
		} else {
			return ResponseEntity.status(HttpStatusCode.valueOf(400)).body( "Serviço não agendado... Usuários encontram-se em distância superior a 5km!" );
		}
		
	}
	
	@GetMapping(value = "getdistance")
	public ResponseEntity<String> getDistanceInformation(@RequestParam String origins, @RequestParam String destination){
		
		Service service = new Service();
		String distance = distanceCalculation.getDistance( distanceCalculation.getMapsObject(origins, destination ) ).replaceAll(" mi", "");
		service.setDistance(distance);
		
		return ResponseEntity.status(HttpStatus.OK).body( "Distância do destino para a origem é de: " + service.getDistance() );
	}
	
	@GetMapping(value = "wholedistanceinformations")
	public ResponseEntity<String> showWholeDistanceInformations(@RequestParam String origins, @RequestParam String destination){
		
		return ResponseEntity.status(HttpStatus.OK).body( distanceCalculation.getMapsObject(origins, destination ) );
	}
	
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
		System.out.println(ex);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body("Algo deu errado com o processamento da solicitação...");
	}

}
