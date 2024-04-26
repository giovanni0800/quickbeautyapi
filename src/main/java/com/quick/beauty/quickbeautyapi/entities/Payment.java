package com.quick.beauty.quickbeautyapi.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "tb_payment")
public class Payment {

	@Id
	@Column(unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime payDay;
	
	@Column(nullable = false)
	private Double price;

	@Column(nullable = false)
	private Long idFormOfPayment;
	
	@Column(nullable = false)
	private Long clientId;
	
	@Column(nullable = false)
	private Long professionalId;

	public Payment() {
		// It Doesn't need a parameter;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getPayDay() {
		return payDay;
	}

	public void setPayDay(LocalDateTime payDay) {
		this.payDay = payDay;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Long getFormOfPayment() {
		return idFormOfPayment;
	}

	public void setFormOfPayment(Long idFormOfPayment) {
		this.idFormOfPayment = idFormOfPayment;
	}

	public Long getClientId() {
		return clientId;
	}

	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}

	public Long getProfessionalId() {
		return professionalId;
	}

	public void setProfessionalId(Long professionalId) {
		this.professionalId = professionalId;
	}

}
