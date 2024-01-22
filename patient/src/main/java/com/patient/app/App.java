package com.patient.app;

import com.patient.model.Patient;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class App {
	public static void main(String[] args) {
		System.out.println("Hello World! to JPA");
//		
//		EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
//		EntityManager entityManager ;		
//		entityManager = emf.createEntityManager();
//		Patient patient = new Patient("17", "Surjeet","Kumar", "Male", "No", "some");
//		
//		entityManager.getTransaction().begin();
//		entityManager.persist(patient);
//		entityManager.getTransaction().commit();
	
		new PatientWindow();
		System.out.println("Done");

	}
}
