package com.quick.beauty.quickbeautyapi.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quick.beauty.quickbeautyapi.service.PaymentService;

import jakarta.annotation.security.RolesAllowed;

@RestController
@RequestMapping(value = "/payment")
public class PaymentController {
	
	@Autowired
	PaymentService payService;
	
	@RolesAllowed({ "ROLE_CLIENT", "ROLE_PROFESSIONAL" })
	@PostMapping(value = "/pix")
	public ResponseEntity<?> realizePixPayment(@RequestBody Map<String, Object> requestData) {
		
		return payService.realizePixPayment(requestData);
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
