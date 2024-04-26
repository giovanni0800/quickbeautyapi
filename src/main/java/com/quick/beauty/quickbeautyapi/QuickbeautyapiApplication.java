package com.quick.beauty.quickbeautyapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.auth0.jwt.exceptions.TokenExpiredException;

@SpringBootApplication
@ComponentScan(basePackages = "com.quick.beauty.quickbeautyapi")
public class QuickbeautyapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuickbeautyapiApplication.class, args);
	}
	
	@ExceptionHandler(TokenExpiredException.class)
	public ResponseEntity<String> handleTokenExpiredException(TokenExpiredException ex) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body("O token de autenticação expirou. Faça login novamente.");
	}

}
