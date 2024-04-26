package com.quick.beauty.quickbeautyapi.entities;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_user")
public class User implements UserDetails {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, nullable = false)
	private String username;

	@Column(unique = true, nullable = false)
	private String cpf;

	private String dateOfBirth;

	@Column(unique = true, nullable = false)
	private String email;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private String type;
	
	@Column(unique = true, nullable = false)
	private String phoneNumber;
	
	@Column(nullable = false)
	private String cep;
	
	@Column(nullable = false)
	private String houseNumber;
	
	@Column(nullable = false)
	private Double evaluationNote;
	
	@Column(nullable = false)
	private boolean active;
	
	@Column(columnDefinition = "TEXT")
	private String userImage;

	public User() {
		// It Doesn't need a parameter;
	}
	
	public User(String username, String cpf, String dateOfBirth, String email,
			String password, String type, String phoneNumber, String cep,
			String houseNumber, Double evaluationNote) {
		
		this.username = username;
		this.cpf = cpf;
		this.dateOfBirth = dateOfBirth;
		this.email = email;
		this.password = password;
		this.type = type;
		this.phoneNumber = phoneNumber;
		this.cep = cep;
		this.houseNumber = houseNumber;
		this.evaluationNote = evaluationNote;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getCEP() {
		return cep;
	}

	public void setCEP(String cep) {
		this.cep = cep;
	}

	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}

	public String getHouseNumber() {
		return houseNumber;
	}

	public Double getEvaluationNote() {
		return evaluationNote;
	}

	public void setEvaluationNote(Double evaluationNote) {
		this.evaluationNote = evaluationNote;
	}
	
	public String getUserImage() {
		return userImage;
	}
	
	public void setUserImage(String userImage) {
		this.userImage = userImage;
	}
	
	public boolean getActive() {
		return active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority("ROLE_" + type));
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return active;
	}

	@Override
	public boolean isAccountNonLocked() {
		return active;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return active;
	}

	@Override
	public boolean isEnabled() {
		return active;
	}
	
	public boolean cpfValidation(String cpf) {
		
		int[] multiplicador1 = { 10, 9, 8, 7, 6, 5, 4, 3, 2 };
		int[] multiplicador2 = { 11, 10, 9, 8, 7, 6, 5, 4, 3, 2 };
		String tempCpf;
		String digito;
		int soma;
		int resto;

		cpf = cpf.trim();
		cpf = cpf.replace(".", "").replace("-", "");

		if (cpf.length() != 11)
		    return false;

		tempCpf = cpf.substring(0, 9);
		soma = 0;

		for (int i = 0; i < 9; i++)
		    soma += Integer.parseInt(String.valueOf(tempCpf.charAt(i))) * multiplicador1[i];

		resto = soma % 11;
		if (resto < 2)
		    resto = 0;
		else
		    resto = 11 - resto;

		digito = String.valueOf(resto);
		tempCpf = tempCpf + digito;
		soma = 0;

		for (int i = 0; i < 10; i++)
		    soma += Integer.parseInt(String.valueOf(tempCpf.charAt(i))) * multiplicador2[i];

		resto = soma % 11;
		if (resto < 2)
		    resto = 0;
		else
		    resto = 11 - resto;

		digito = digito + resto;
		return cpf.endsWith(digito);
	}

}
