package com.example.test.controller.mapper;

import com.example.test.controller.dto.PatientDto;
import com.example.test.model.entity.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PatientMapper {

    @Mapping(target = "id", ignore = true)
    Patient toPatient(PatientDto patientDto);

    PatientDto toPatientDto(Patient patient);

    void updatePatient(PatientDto patientDto, @MappingTarget Patient patient);
}