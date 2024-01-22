package com.patient.repository;

import java.util.List;

import com.patient.model.Patient;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

public class PatientRepositoryImpl implements PatientRepository {

	private EntityManager entityManager;
	private EntityManagerFactory emf;

	public PatientRepositoryImpl() {
		this.emf = Persistence.createEntityManagerFactory("pu");
		this.entityManager = emf.createEntityManager();
	}

	@Override
	public void addPatientToDB(Patient patient) {
		entityManager.getTransaction().begin();
		entityManager.persist(patient);
		entityManager.getTransaction().commit();
	}

	@Override
	public void updatePatient(String id, String columnName, Object updatedValue) {

		Patient patient = entityManager.find(Patient.class, id);
		entityManager.getTransaction().begin();

		switch (columnName.toLowerCase()) {
		case "id":
			patient.setId(id);
		case "firstname":
			patient.setFirstName((String) updatedValue);
		case "lastname":
			patient.setLastName((String) updatedValue);
		case "Gender":
			patient.setGender((String) updatedValue);
		case "address":
			patient.setAddress((String) updatedValue);
		case "hasisurance":
			patient.setHasInsurance((String) updatedValue);
		}
		entityManager.merge(patient);
		entityManager.getTransaction().commit();
	}

	@Override
	public List<Patient> getAllPatient() {
		String jpql = "SELECT p FROM Patient p";
			TypedQuery<Patient> query = entityManager.createQuery(jpql, Patient.class);
			return query.getResultList();
			
	}

	public void deletePatient(String patientId) {
		Patient patient = entityManager.find(Patient.class, patientId);

		entityManager.getTransaction().begin();
		entityManager.remove(patient);
		entityManager.getTransaction().commit();
	}

}
