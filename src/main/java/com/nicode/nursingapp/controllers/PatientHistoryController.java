package com.nicode.nursingapp.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nicode.nursingapp.entities.PatientHistoryEntity;
import com.nicode.nursingapp.exceptions.AlreadyExistsException;
import com.nicode.nursingapp.exceptions.NotFoundException;
import com.nicode.nursingapp.services.PatientHistoryService;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controlador que maneja las operaciones relacionadas con la historia clínica
 * de los pacientes.
 */
@RestController
@RequestMapping("/histories")
@CrossOrigin("*")
public class PatientHistoryController {

    @Autowired
    private PatientHistoryService patientHistoryService;

    /**
     * Obtiene una historia clínica por su identificador.
     *
     * @param id Identificador de la historia clínica.
     * @return ResponseEntity con la historia clínica o mensaje de error si no se
     *         encuentra.
     */
    @GetMapping("/history-id/{id}")
    public ResponseEntity<?> getByHistoryId(@RequestParam Long id) {
        try {
            Optional<PatientHistoryEntity> result = this.patientHistoryService.findById(id);

            return ResponseEntity.ok(result);

        } catch (NotFoundException e) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

        }
    }

    /**
     * Obtiene la historia clínica de un paciente por su identificador.
     *
     * @param id Identificador del paciente.
     * @return ResponseEntity con la historia clínica del paciente o mensaje de
     *         error si no se encuentra.
     */
    @GetMapping("/history-patient-id/{id}")
    public ResponseEntity<?> getByPatientId(@PathVariable Long id) {
        try {
            PatientHistoryEntity result = this.patientHistoryService.findByPatientId(id);

            return ResponseEntity.ok(result);

        } catch (NotFoundException e) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

        }
    }

    /**
     * Guarda una nueva historia clínica asociada a un paciente.
     *
     * @param history Historia clínica a guardar.
     * @param id      Identificador del paciente al que se asociará la historia
     *                clínica.
     * @return ResponseEntity con el mensaje de éxito o mensaje de error si ya
     *         existe.
     */
    @PostMapping("/save/{id}")
    public ResponseEntity<Map<String, String>> save(@RequestBody PatientHistoryEntity history, @PathVariable Long id) {

        Map<String, String> response = new HashMap<>();

        try {
            String successMessage = this.patientHistoryService.save(history, id);

            response.put("message", successMessage);

            return ResponseEntity.ok(response);

        } catch (AlreadyExistsException e) {

            response.put("error", "Error: " + e.getMessage());

            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Actualiza la información de una historia clínica existente.
     *
     * @param history Historia clínica con la información actualizada.
     * @param id      Identificador de la historia clínica a actualizar.
     * @return ResponseEntity con el mensaje de éxito o mensaje de error si no se
     *         encuentra.
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, String>> update(@RequestBody PatientHistoryEntity history,
            @PathVariable Long id) {

        Map<String, String> response = new HashMap<>();

        try {
            String successMessage = this.patientHistoryService.update(history, id);

            response.put("message", successMessage);

            return ResponseEntity.ok(response);

        } catch (NotFoundException e) {

            response.put("error", "Error: " + e.getMessage());

            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Elimina una historia clínica por su identificador.
     *
     * @param id Identificador de la historia clínica a eliminar.
     * @return ResponseEntity con el mensaje de éxito o mensaje de error si no se
     *         encuentra.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {

        Map<String, String> response = new HashMap<>();

        try {
            String successMessage = this.patientHistoryService.delete(id);

            response.put("message", successMessage);

            return ResponseEntity.ok(response);

        } catch (NotFoundException e) {

            response.put("error", e.getMessage());

            return ResponseEntity.badRequest().body(response);
        }
    }
}