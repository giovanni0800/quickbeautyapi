package com.quick.beauty.quickbeautyapi.entities;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "tb_scheduling")
@Component
public class Scheduling {

	@Id
	@Column(unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Long id_client;
	
	@Column(nullable = false)
	private Long id_professional;
	
	@Column(nullable = false)
	private Long id_service;
	
	@Column(nullable = false)
	private Long id_payment;

	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime markedDate;

	public Scheduling() {
		// It Doesn't need a parameter;		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getClientId() {
		return id_client;
	}

	public void setClientId(Long id_client) {
		this.id_client = id_client;
	}

	public Long getProfessionalId() {
		return id_professional;
	}

	public void setProfessionalId(Long id_professional) {
		this.id_professional = id_professional;
	}

	public Long getServiceId() {
		return id_service;
	}

	public void setServiceId(Long id_service) {
		this.id_service = id_service;
	}

	public Long getPaymentId() {
		return id_payment;
	}

	public void setPaymentId(Long id_payment) {
		this.id_payment = id_payment;
	}

	public void setMarkedDate(LocalDateTime marked_date) {
		this.markedDate = marked_date;
	}

	public LocalDateTime getMarkedDate() {
		return markedDate;
	}

}
