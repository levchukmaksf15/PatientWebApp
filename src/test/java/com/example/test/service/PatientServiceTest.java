package com.example.test.service;

import com.example.test.controller.dto.PatientDto;
import com.example.test.controller.mapper.PatientMapper;
import com.example.test.exception.PatientException;
import com.example.test.model.entity.Patient;
import com.example.test.repository.PatientRepository;
import com.example.test.service.implementation.PatientServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static com.example.test.service.implementation.PatientServiceImpl.NOT_UNIQUE_NAME;
import static com.example.test.service.implementation.PatientServiceImpl.NO_PATIENT_BY_ID_OR_IS_DISCHARGED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PatientServiceTest {

    @InjectMocks
    private PatientServiceImpl patientService;
    @Mock
    private PatientRepository patientRepository;
    @Spy
    private PatientMapper patientMapper = Mappers.getMapper(PatientMapper.class);

    private Patient patient;
    private PatientDto patientDto;

    @BeforeEach
    void init() {
        patient = Patient.builder()
                .name("Test name")
                .birthDate(LocalDate.of(2024, 3,5))
                .isDischarged(false)
                .lastPulse(90)
                .lastTemperature(36.6)
                .build();

        patientDto = patientMapper.toPatientDto(patient);
    }

    @Test
    void addNewPatient_correctData_returnPatient() {
        when(patientRepository.save(patient)).thenReturn(patient);
        when(patientRepository.existsByName("Test name")).thenReturn(false);

        PatientDto actualResult = patientService.addNewPatient(patientDto);
        PatientDto expectedResult = patientMapper.toPatientDto(patient);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    void addNewPatient_nameIsNotUnique_throwException() {
        when(patientRepository.existsByName("Test name")).thenReturn(true);

        assertThrows(PatientException.class, () -> patientService.addNewPatient(patientDto),
                NOT_UNIQUE_NAME);
    }

    @Test
    void changeInformationAboutPatient_correctData_returnUpdatedPatient() {
        UUID id = UUID.randomUUID();
//        patient.setId(id);

        when(patientRepository.existsByName("Test name")).thenReturn(false);
        when(patientRepository.existsByIdAndIsDischargedIsFalse(id)).thenReturn(true);
        when(patientRepository.save(patient)).thenReturn(patient);
        when(patientRepository.getReferenceById(id)).thenReturn(patient);

        PatientDto actualResult = patientService.changeInformationAboutPatient(id, patientDto);
        PatientDto expectedResult = patientMapper.toPatientDto(patient);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    void changeInformationAboutPatient_patientIdIsNotCorrect_throwException() {
        UUID id = UUID.randomUUID();

        when(patientRepository.existsByIdAndIsDischargedIsFalse(id)).thenReturn(false);

        assertThrows(PatientException.class, () -> patientService.changeInformationAboutPatient(id, patientDto),
                NO_PATIENT_BY_ID_OR_IS_DISCHARGED);
    }

    @Test
    void changeInformationAboutPatient_nameIsNotUnique_throwException() {
        UUID id = UUID.randomUUID();

        when(patientRepository.existsByIdAndIsDischargedIsFalse(id)).thenReturn(true);
        when(patientRepository.existsByName("Test name")).thenReturn(true);

        assertThrows(PatientException.class, () -> patientService.changeInformationAboutPatient(id, patientDto),
                NOT_UNIQUE_NAME);
    }

    @Test
    void dischargePatient_correctData_returnDischargedPatient() {
        UUID id = UUID.randomUUID();

        when(patientRepository.existsByIdAndIsDischargedIsFalse(id)).thenReturn(true);
        when(patientRepository.getReferenceById(id)).thenReturn(patient);
        when(patientRepository.save(patient)).thenReturn(patient);

        PatientDto actualResult = patientService.dischargePatient(id);
        PatientDto expectedResult = patientMapper.toPatientDto(patient);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    void dischargePatient_patientIdIsNotCorrect_throwException() {
        UUID id = UUID.randomUUID();

        when(patientRepository.existsByIdAndIsDischargedIsFalse(id)).thenReturn(false);

        assertThrows(PatientException.class, () -> patientService.dischargePatient(id),
                NO_PATIENT_BY_ID_OR_IS_DISCHARGED);
    }

    @Test
    void getAllNotDischargedPatients_thereAreNoDischargedPatients_returnPatientList() {
        when(patientRepository.findByIsDischargedIsFalse()).thenReturn(List.of(patient));

        List<PatientDto> actualResult = patientService.getAllNotDischargedPatients();
        List<PatientDto> expectedResult = List.of(patientMapper.toPatientDto(patient));

        assertEquals(expectedResult, actualResult);
    }

    @Test
    void getAllNotDischargedPatients_thereAreNoPatients_returnEmptyList() {
        when(patientRepository.findByIsDischargedIsFalse()).thenReturn(List.of());

        List<PatientDto> actualResult = patientService.getAllNotDischargedPatients();
        List<PatientDto> expectedResult = List.of();

        assertEquals(expectedResult, actualResult);
    }
}
