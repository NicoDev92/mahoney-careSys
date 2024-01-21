package com.nicode.nursingapp.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nicode.nursingapp.entities.PatientEntity;
import com.nicode.nursingapp.exceptions.AlreadyExistsException;
import com.nicode.nursingapp.exceptions.NotFoundException;
import com.nicode.nursingapp.services.PatientService;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;

/**
 * Controlador que maneja las operaciones relacionadas con los pacientes.
 */
@RestController
@RequestMapping("/patients")
@CrossOrigin("*")
public class PatientController {

    @Autowired
    private PatientService patientService;

    /**
     * Obtiene todos los pacientes.
     *
     * @return ResponseEntity con la lista de pacientes.
     */
    @GetMapping
    public ResponseEntity<List<PatientEntity>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAll());
    }

    /**
     * Obtiene una página de pacientes.
     *
     * @param pageNumber       Número de página.
     * @param elementsQuantity Cantidad de elementos por página.
     * @return ResponseEntity con la página de pacientes.
     */
    @GetMapping("/paged-patients")
    public ResponseEntity<Page<PatientEntity>> getAllPatientsPaged(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int elementsQuantity) {

        Page<PatientEntity> pagedPatients = patientService.getAllPaged(pageNumber, elementsQuantity);

        return ResponseEntity.ok(pagedPatients);
    }

    /**
     * Busca pacientes que coincidan con el término proporcionado.
     *
     * @param keyword          Término de búsqueda.
     * @param pageNumber       Número de página.
     * @param elementsQuantity Cantidad de elementos por página.
     * @return ResponseEntity con la página de resultados de búsqueda.
     */
    @GetMapping("/search/{keyword}")
    public ResponseEntity<Page<PatientEntity>> getPatientsBy(@PathVariable String keyword,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int elementsQuantity) {

        Page<PatientEntity> pagedResults = patientService.findBy(pageNumber, elementsQuantity, keyword);

        return ResponseEntity.ok(pagedResults);
    }

    /**
     * Obtiene un paciente por su identificador.
     *
     * @param id Identificador del paciente.
     * @return ResponseEntity con el paciente o mensaje de error si no se encuentra.
     */
    @GetMapping("/patient/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            PatientEntity patient = patientService.findById(id);
            return ResponseEntity.ok(patient);

        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Guarda un nuevo paciente.
     *
     * @param patient Paciente a guardar.
     * @return ResponseEntity con el paciente guardado o mensaje de error si ya
     *         existe.
     */
    @PostMapping("/save")
    public ResponseEntity<Map<String, Object>> savePatient(@RequestBody PatientEntity patient) {

        Map<String, Object> response = new HashMap<>();

        try {
            return ResponseEntity.ok(patientService.save(patient));
        } catch (AlreadyExistsException e) {
            response.put("fail", "Error: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Actualiza la información de un paciente existente.
     *
     * @param id      Identificador del paciente a actualizar.
     * @param patient Paciente con la información actualizada.
     * @return ResponseEntity con el mensaje de éxito o mensaje de error si ya
     *         existe.
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, String>> updatePatient(@PathVariable Long id,
            @RequestBody PatientEntity patient) {

        Map<String, String> response = new HashMap<>();

        try {
            String successMessage = patientService.update(patient);
            response.put("success", successMessage);
            return ResponseEntity.ok(response);
        } catch (AlreadyExistsException e) {
            response.put("fail", "Error: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Elimina un paciente por su identificador.
     *
     * @param id Identificador del paciente a eliminar.
     * @return ResponseEntity con el mensaje de éxito o mensaje de error si no se
     *         encuentra.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {

        Map<String, String> response = new HashMap<>();

        try {
            String successMessage = patientService.delete(id);
            response.put("success", successMessage);
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            response.put("error", "Error: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
