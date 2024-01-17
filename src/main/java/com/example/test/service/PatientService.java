package com.example.test.service;

import com.example.test.controller.dto.PatientDto;
import com.example.test.model.entity.Patient;

import java.util.List;
import java.util.UUID;

/**
 * Service for working with patients
 */
public interface PatientService {

    /**
     * Method for add new patient.
     *
     * @param patientDto new patient
     * @return added patient
     */
    PatientDto addNewPatient(PatientDto patientDto);

    /**
     * Method for add new patient.
     *
     * @param patientDto information to change
     * @param patientId patient id
     * @return changed patient
     */
    PatientDto changeInformationAboutPatient(UUID patientId, PatientDto patientDto);

    /**
     * Method for add new patient.
     *
     * @param id id of patient for discharge
     * @return discharged patient
     */
    PatientDto dischargePatient(UUID id);

    /**
     * Method for getting all not discharged patients
     *
     * @return list of not discharged patients
     */
    List<PatientDto> getAllNotDischargedPatients();
}