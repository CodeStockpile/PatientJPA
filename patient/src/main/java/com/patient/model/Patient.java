package com.patient.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Patient {
	
	@Id
	private String id;
	@Column
	private String firstName;
	
	@Column
	private String lastName;
	@Column
	private String gender;
	@Column
	private String address;
	@Column
	private String hasInsurance;
	
	
	
	public Patient() {
		super();
	}

	public Patient(String id, String firstName, String lastName, String gender, String address, String checkInsurance) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.address = address;
		this.hasInsurance = checkInsurance;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getHasInsurance() {
		return hasInsurance;
	}

	public void setHasInsurance(String insurance) {
		this.hasInsurance = insurance;
	}

	@Override
	public String toString() {
		return "Patient [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", gender=" + gender
				+ ", address=" + address + ", hasInsurance=" + hasInsurance + "]";
	}
	
	

}
