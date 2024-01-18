package com.example.test.controller;

import com.example.test.controller.dto.PatientDto;
import com.example.test.exception.NotUniquePatientNameException;
import com.example.test.exception.PatientExceptionHandler;
import com.example.test.service.implementation.PatientServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static com.example.test.service.implementation.PatientServiceImpl.NOT_UNIQUE_NAME;
import static com.example.test.service.implementation.PatientServiceImpl.NO_PATIENT_BY_ID_OR_IS_DISCHARGED;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class PatientControllerTest {

    @InjectMocks
    private PatientController patientController;
    @Mock
    private PatientServiceImpl patientService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private PatientDto patientDto;
    private String patientDtoString;

    @BeforeEach
    void init() throws Exception{
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders
                .standaloneSetup(patientController)
                .setControllerAdvice(PatientExceptionHandler.class)
                .build();

        patientDto = PatientDto.builder()
                .name("Test name")
                .birthDate(LocalDate.of(2024, 3,5))
                .isDischarged(false)
                .lastPulse(90)
                .lastTemperature(36.6)
                .build();

        patientDtoString = objectMapper.writeValueAsString(patientDto);

    }

    @Test
    void addNewPatient_correctData_returnAddedPatientAndStatusOk() throws Exception{
        when(patientService.addNewPatient(patientDto)).thenReturn(patientDto);

        mockMvc.perform(patch("/api/v1/patient/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(patientDtoString))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(patientDto.getName()))
                .andExpect(jsonPath("$.lastTemperature").value(patientDto.getLastTemperature()));

        verify(patientService, times(1)).addNewPatient(patientDto);
    }

    @Test
    void addNewPatient_notUniqueName_returnBadRequest() throws Exception{
        when(patientService.addNewPatient(patientDto))
                .thenThrow(new NotUniquePatientNameException(NOT_UNIQUE_NAME));

        mockMvc.perform(patch("/api/v1/patient/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patientDtoString))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", is(NOT_UNIQUE_NAME)));

        verify(patientService, times(1)).addNewPatient(patientDto);
    }

    @Test
    void changeInformationAboutPatient_correctData_returnUpdatedPatientAndStatusOk() throws Exception {
        UUID id = UUID.randomUUID();

        when(patientService.changeInformationAboutPatient(id, patientDto)).thenReturn(patientDto);

        mockMvc.perform(patch("/api/v1/patient/{id}/update", String.valueOf(id))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patientDtoString))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(patientDto.getName()))
                .andExpect(jsonPath("$.lastTemperature").value(patientDto.getLastTemperature()));

        verify(patientService, times(1)).changeInformationAboutPatient(id, patientDto);
    }

    @Test
    void changeInformationAboutPatient_notUniqueName_returnBadRequest() throws Exception {
        UUID id = UUID.randomUUID();

        when(patientService.changeInformationAboutPatient(id, patientDto))
                .thenThrow(new NotUniquePatientNameException(NOT_UNIQUE_NAME));

        mockMvc.perform(patch("/api/v1/patient/{id}/update", String.valueOf(id))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patientDtoString))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", is(NOT_UNIQUE_NAME)));

        verify(patientService, times(1)).changeInformationAboutPatient(id, patientDto);
    }

    @Test
    void changeInformationAboutPatient_notCorrectIdOrPatientDischarged_returnBadRequest() throws Exception {
        UUID id = UUID.randomUUID();

        when(patientService.changeInformationAboutPatient(id, patientDto))
                .thenThrow(new NotUniquePatientNameException(NO_PATIENT_BY_ID_OR_IS_DISCHARGED));

        mockMvc.perform(patch("/api/v1/patient/{id}/update", String.valueOf(id))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patientDtoString))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", is(NO_PATIENT_BY_ID_OR_IS_DISCHARGED)));

        verify(patientService, times(1)).changeInformationAboutPatient(id, patientDto);
    }

    @Test
    void dischargePatient_correctData_returnDischargedPatientAndStatusOk() throws Exception {
        UUID id = UUID.randomUUID();

        when(patientService.dischargePatient(id)).thenReturn(patientDto);

        mockMvc.perform(patch("/api/v1/patient/{id}/discharge", String.valueOf(id)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(patientDto.getName()))
                .andExpect(jsonPath("$.lastTemperature").value(patientDto.getLastTemperature()));

        verify(patientService, times(1)).dischargePatient(id);
    }

    @Test
    void dischargePatient_notCorrectIdOrPatientDischarged_returnBadRequest() throws Exception {
        UUID id = UUID.randomUUID();

        when(patientService.dischargePatient(id))
                .thenThrow(new NotUniquePatientNameException(NO_PATIENT_BY_ID_OR_IS_DISCHARGED));

        mockMvc.perform(patch("/api/v1/patient/{id}/discharge", String.valueOf(id)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", is(NO_PATIENT_BY_ID_OR_IS_DISCHARGED)));

        verify(patientService, times(1)).dischargePatient(id);
    }

    @Test
    void getAllNotDischargedPatients_thereAreNoDischargedPatients_returnStatusOkAndListOfPatients() throws Exception {
        List<PatientDto> patientList = List.of(patientDto);

        when(patientService.getAllNotDischargedPatients()).thenReturn(patientList);

        mockMvc.perform(get("/api/v1/patient/all-not-discharged"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(patientDto.getName()))
                .andExpect(jsonPath("$[0].lastTemperature").value(patientDto.getLastTemperature()));

        verify(patientService, times(1)).getAllNotDischargedPatients();
    }

    @Test
    void getAllNotDischargedPatients_thereAreNoPatients_returnStatusOkAndEmptyList() throws Exception {
        List<PatientDto> patientList = List.of();

        when(patientService.getAllNotDischargedPatients()).thenReturn(patientList);

        mockMvc.perform(get("/api/v1/patient/all-not-discharged"))
                .andExpect(status().isOk());

        verify(patientService, times(1)).getAllNotDischargedPatients();
    }
}