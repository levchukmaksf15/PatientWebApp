package com.example.test.controller;

import com.example.test.controller.dto.PatientDto;
import com.example.test.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/patient")
public class PatientController {

    private final PatientService patientService;

    @PatchMapping(path = "/add")
    public ResponseEntity<PatientDto> addNewPatient(@RequestBody PatientDto patientDto) {
        return ResponseEntity.ok(patientService.addNewPatient(patientDto));
    }

    @PatchMapping(path = "/{id}/update")
    public ResponseEntity<PatientDto> changeInformationAboutPatient(@PathVariable UUID id, @RequestBody PatientDto patientDto) {
        return ResponseEntity.ok(patientService.changeInformationAboutPatient(id, patientDto));
    }

    @PatchMapping(path = "/{id}/discharge")
    public ResponseEntity<PatientDto> dischargePatient(@PathVariable UUID id) {
        return ResponseEntity.ok(patientService.dischargePatient(id));
    }

    @GetMapping(path = "/all-not-discharged")
    public ResponseEntity<List<PatientDto>> getAllNotDischargedPatients() {
        return ResponseEntity.ok(patientService.getAllNotDischargedPatients());
    }
}