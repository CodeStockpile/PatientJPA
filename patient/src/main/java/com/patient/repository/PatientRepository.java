package com.patient.repository;

import java.util.List;

import com.patient.model.Patient;

public interface PatientRepository {
	
	public void addPatientToDB(Patient patient);
	public void updatePatient(String id,String columnName,Object updatedValue);
	public List<Patient> getAllPatient();

}
