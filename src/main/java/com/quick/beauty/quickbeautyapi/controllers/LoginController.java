package com.quick.beauty.quickbeautyapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quick.beauty.quickbeautyapi.dto.Login;
import com.quick.beauty.quickbeautyapi.entities.User;
import com.quick.beauty.quickbeautyapi.repositories.UserRepository;
import com.quick.beauty.quickbeautyapi.service.TokenService;

@RestController // Controlador Rest para controlar o envio das APIs solicitadas para trabalho
				// com dados
@RequestMapping(value = "/login") // Reuqest Mapping vai ouvir um determinado caminho da url para gerenciar as
									// requisições e responder
public class LoginController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private TokenService tokenService;

	@Autowired
	private UserRepository repoUser;

	@PostMapping
	public String login(@RequestBody Login userLogin) {

		if (repoUser.emailExists(userLogin.email()) > 0) {
			
			if( repoUser.isUserActive( userLogin.email() ) ) {
				UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
						userLogin.email(), userLogin.password());

				Authentication authentiication = this.authenticationManager.authenticate(authenticationToken);

				var usuario = (User) authentiication.getPrincipal();

				return tokenService.generateToken(usuario);
			
			} else {
				return "Usuário bloqueado! Não será possível realizar o login até efetuar o desbloqueio...";
			}

		} else {
			return "Usuário inexistente ou senha inválida";
		}
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
