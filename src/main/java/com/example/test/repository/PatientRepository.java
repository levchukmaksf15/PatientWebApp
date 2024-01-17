package com.example.test.repository;

import com.example.test.model.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {
    boolean existsByName(String name);
    boolean existsByIdAndIsDischargedIsFalse(UUID id);
    List<Patient> findByIsDischargedIsFalse();
}