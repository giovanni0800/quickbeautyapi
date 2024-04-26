package com.quick.beauty.quickbeautyapi.entities;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "tb_adherence_of_registrations_per_day")
public class AdherenceOfRegistrationsPerDay {

	@Id
	@Column(unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Temporal(TemporalType.DATE)
	private LocalDate today;

	private Long howManyAdmins;
	private Long howManyProfessionals;
	private Long howManyClients;
	private Long howManyUsers;

	public AdherenceOfRegistrationsPerDay() {
		this.today = LocalDate.now();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getToday() {
		return today;
	}
	
	public Long getHowManyAdmins() {
		return howManyAdmins;
	}
	
	public void setHowManyAdmins(Long howManyAdmins) {
		this.howManyAdmins = howManyAdmins;
	}

	public Long getHowManyProfessionals() {
		return howManyProfessionals;
	}

	public void setHowManyProfessionals(Long howManyProfessionals) {
		this.howManyProfessionals = howManyProfessionals;
	}

	public Long getHowManyClients() {
		return howManyClients;
	}

	public void setHowManyClients(Long howManyClients) {
		this.howManyClients = howManyClients;
	}
	
	public Long getHowManyUsers() {
		return howManyUsers;
	}
	
	public void setHowManyUsers(Long howManyUsers) {
		this.howManyUsers = howManyUsers;
	}
}
