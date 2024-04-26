package com.quick.beauty.quickbeautyapi.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_form_of_payment")
public class FormOfPayment {

	@Id
	@Column(unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nameFormOfPayment;
	
	public FormOfPayment() {
		// It Doesn't need a parameter;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNameFormOfPayment() {
		return nameFormOfPayment;
	}

	public void setNameFormOfPayment(String nameFormOfPayment) {
		this.nameFormOfPayment = nameFormOfPayment;
	}

}
