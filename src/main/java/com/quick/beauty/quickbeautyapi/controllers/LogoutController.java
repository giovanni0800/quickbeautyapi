package com.quick.beauty.quickbeautyapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quick.beauty.quickbeautyapi.configurations.JwtBlacklist;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "/logout")
public class LogoutController {
	
	@Autowired
	private JwtBlacklist jwtBlackList;
	
	@PostMapping
	public ResponseEntity<String> logout(@RequestHeader("Authorization") String token, HttpServletResponse response) {

		String jwtToken = token.substring(7); // Remove "Bearer "
		jwtBlackList.invalidateToken(jwtToken);
		
		response.setHeader("Authorization", null);
		
		return ResponseEntity.status(HttpStatus.OK).body("Logout realizado com sucesso!");
	}
	
	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<String> handleUsernameNotFound(UsernameNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Usuário não encontrado...");
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
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
	}

}
