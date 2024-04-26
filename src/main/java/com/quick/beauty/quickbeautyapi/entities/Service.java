package com.quick.beauty.quickbeautyapi.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_service")
public class Service {

	@Id
	@Column(unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String serviceDescription;
	
	@Column(nullable = false)
	private String gatheringPlace;
	
	@Column(nullable = false)
	private LocalDateTime dateOfMakingTheRequest;
	
	@Column(nullable = false)
	private String distance;
	
	private boolean serviceFinished;

	public Service() {
		// It Doesn't need a parameter;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getServiceDescription() {
		return serviceDescription;
	}

	public void setServiceDescription(String serviceDescription) {
		this.serviceDescription = serviceDescription;
	}

	public String getGatheringPlace() {
		return gatheringPlace;
	}

	public void setGatheringPlace(String gatheringPlace) {
		this.gatheringPlace = gatheringPlace;
	}

	public LocalDateTime getDateOfMakingTheRequest() {
		return dateOfMakingTheRequest;
	}

	public void setDateOfMakingTheRequest(LocalDateTime dateOfMakingTheRequest) {
		this.dateOfMakingTheRequest = dateOfMakingTheRequest;
	}
	
	public String getDistance() {
		return distance;
	}
	
	public void setDistance(String distance) {
		this.distance = distance;
		this.distance += "km";
	}

	public boolean isServiceFinished() {
		return serviceFinished;
	}

	public void setServiceFinished(boolean serviceFinished) {
		this.serviceFinished = serviceFinished;
	}

}
