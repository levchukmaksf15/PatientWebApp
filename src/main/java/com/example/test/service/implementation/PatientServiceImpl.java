package com.example.test.service.implementation;

import com.example.test.controller.dto.PatientDto;
import com.example.test.controller.mapper.PatientMapper;
import com.example.test.exception.NoPatientWithSuchIdException;
import com.example.test.exception.NotUniquePatientNameException;
import com.example.test.model.entity.Patient;
import com.example.test.repository.PatientRepository;
import com.example.test.service.PatientService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    public static final String NO_PATIENT_BY_ID_OR_IS_DISCHARGED = "There no patient with such id or this patient has been discharged!";
    public static final String NOT_UNIQUE_NAME = "New patient's name is not unique!";

    @Override
    @Transactional
    public PatientDto addNewPatient(PatientDto patientDto) {
        Patient patient = patientMapper.toPatient(patientDto);

        if (patientRepository.existsByName(patient.getName())) {
            throw new NotUniquePatientNameException(NOT_UNIQUE_NAME);
        }

        return patientMapper.toPatientDto(patientRepository.save(patient));
    }

    @Override
    @Transactional
    public PatientDto changeInformationAboutPatient(UUID patientId, PatientDto patientDto) {
        if (!patientRepository.existsByIdAndIsDischargedIsFalse(patientId)) {
            throw new NoPatientWithSuchIdException(NO_PATIENT_BY_ID_OR_IS_DISCHARGED);
        }

        if (patientRepository.existsByName(patientDto.getName())) {
            throw new NotUniquePatientNameException(NOT_UNIQUE_NAME);
        }

        Patient patient = patientRepository.getReferenceById(patientId);
        patientMapper.updatePatient(patientDto, patient);

        return patientMapper.toPatientDto(patientRepository.save(patient));
    }

    @Override
    @Transactional
    public PatientDto dischargePatient(UUID patientId) {
        if (!patientRepository.existsByIdAndIsDischargedIsFalse(patientId)) {
            throw new NoPatientWithSuchIdException(NO_PATIENT_BY_ID_OR_IS_DISCHARGED);
        }

        Patient patient = patientRepository.getReferenceById(patientId);
        patient.setDischarged(true);

        return patientMapper.toPatientDto(patientRepository.save(patient));
    }

    @Override
    public List<PatientDto> getAllNotDischargedPatients() {
        List<Patient> patientList = patientRepository.findByIsDischargedIsFalseOrderByNameAsc();

        if (patientList.isEmpty()) {
            return new ArrayList<>();
        }

        return patientList.stream().map(patientMapper::toPatientDto).toList();
    }
}