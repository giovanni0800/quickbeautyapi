package com.quick.beauty.quickbeautyapi.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

//import java.time.LocalDateTime;	PARA A VERSÃO 4.4.0
//import java.time.ZoneOffset;		PARA A VERSÃO 4.4.0
import java.util.Date;

import org.springframework.stereotype.Service;

import com.quick.beauty.quickbeautyapi.entities.User;

@Service
public class TokenService {

	public String generateToken(User usuario) {
		return JWT.create()
				.withIssuer("UserToken")
				.withSubject(usuario.getUsername())
				.withIssuedAt( new Date() )
				.withClaim("id", usuario.getId())
				//.withExpiresAt(LocalDateTime.now() PARA A VERSÃO 4.4.0
						//.plusMinutes(240)			PARA A VERSÃO 4.4.0
						//.toInstant(ZoneOffset.of("-03:00")))	PARA A VERSÃO 4.4.0
				.sign(Algorithm.HMAC256("secreto"));
	}

	public String getSubject(String token) {
		
		
		return JWT.require( Algorithm.HMAC256("secreto") )
				.withIssuer("UserToken")
				.build().verify(token).getSubject();
		
	}

}
