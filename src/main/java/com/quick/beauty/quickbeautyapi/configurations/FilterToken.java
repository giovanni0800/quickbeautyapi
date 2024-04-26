package com.quick.beauty.quickbeautyapi.configurations;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.quick.beauty.quickbeautyapi.repositories.UserRepository;
import com.quick.beauty.quickbeautyapi.service.TokenService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterToken extends OncePerRequestFilter {
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private UserRepository userRepository;
	
	private JwtBlacklist jwtBlacklist;
	
	public FilterToken(JwtBlacklist jwtBlacklist) {
		this.jwtBlacklist = jwtBlacklist;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {		
		
		String token;
		
		var authorizationHeader = request.getHeader("Authorization");
		
		if(authorizationHeader != null) {
			token = authorizationHeader.replaceAll("Bearer ", "");
			
			var subject = this.tokenService.getSubject(token);
			
			var user = this.userRepository.findByUsername(subject);
			
			if (user != null) {
				var authentication = new UsernamePasswordAuthenticationToken( user, null, user.getAuthorities() );
				SecurityContextHolder.getContext().setAuthentication(authentication);
				
			} else {
				System.out.println(subject + "\n\n");
				System.out.println(user);
			}
			
			if(token != null && jwtBlacklist.isTokenBlacklisted(token) ) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			}
		
		} else {
			ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Requisição Mal sucedida...!");
		}
		
		filterChain.doFilter(request, response);
		
	}
	
}