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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nicode.nursingapp.entities.NursingControlEntity;
import com.nicode.nursingapp.entities.dto.DateRquestDto;
import com.nicode.nursingapp.exceptions.AlreadyExistsException;
import com.nicode.nursingapp.exceptions.NotFoundException;
import com.nicode.nursingapp.services.NursingControlService;
import com.nicode.nursingapp.services.PatientService;

/**
 * Controlador que maneja las operaciones relacionadas con los controles de
 * enfermería.
 */
@RestController
@RequestMapping("/controls")
@CrossOrigin("*")
public class NursingControlController {

    private final NursingControlService nursingControlService;

    /**
     * Constructor que inicializa las dependencias del controlador.
     *
     * @param nursingControlService Servicio para operaciones relacionadas con los
     *                              controles de enfermería.
     */
    @Autowired
    public NursingControlController(NursingControlService nursingControlService) {
        this.nursingControlService = nursingControlService;
    }

    /**
     * Obtiene todos los controles de enfermería para un paciente.
     *
     * @param patientId Identificador del paciente.
     * @return ResponseEntity con la lista de controles de enfermería o mensaje de
     *         error si no se encuentran.
     */
    @GetMapping("/all/{patientId}")
    public ResponseEntity<?> getAll(@PathVariable Long patientId) {
        try {
            List<NursingControlEntity> result = this.nursingControlService.getAll(patientId);
            return ResponseEntity.ok(result);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Obtiene una página de controles de enfermería para un paciente.
     *
     * @param page     Número de página.
     * @param elements Cantidad de elementos por página.
     * @param id       Identificador del paciente.
     * @return ResponseEntity con la página de controles de enfermería.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Page<NursingControlEntity>> getAllPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int elements,
            @PathVariable Long id) {

        Page<NursingControlEntity> ctrls = this.nursingControlService.getAllPaged(page, elements, id);

        return ResponseEntity.ok(ctrls);
    }

    /**
     * Obtiene controles de enfermería en un rango de fechas descendente para un
     * paciente.
     *
     * @param id          Identificador del paciente.
     * @param dateRequest Objeto que contiene las fechas de inicio y fin del rango.
     * @param page        Número de página.
     * @param elements    Cantidad de elementos por página.
     * @return ResponseEntity con la página de controles de enfermería en el rango
     *         de fechas.
     */
    @GetMapping("/date-range/{id}")
    public ResponseEntity<?> getInDateRangeDesc(
            @PathVariable Long id,
            @RequestBody DateRquestDto dateRequest,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int elements) {

        try {
            Page<NursingControlEntity> result = nursingControlService.getInDateRangeDesc(
                    id, dateRequest.getStartDate(), dateRequest.getEndDate(), page, elements);

            return ResponseEntity.ok(result);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Obtiene un control de enfermería por su identificador.
     *
     * @param id Identificador del control de enfermería.
     * @return ResponseEntity con el control de enfermería o mensaje de error si no
     *         se encuentra.
     */
    @GetMapping("/control-id/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            NursingControlEntity result = nursingControlService.findById(id);
            return ResponseEntity.ok(result);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Guarda un nuevo control de enfermería asociado a un historial médico.
     *
     * @param historyId Identificador del historial médico.
     * @param control   Control de enfermería a guardar.
     * @return ResponseEntity con el mensaje de éxito o mensaje de error si ya
     *         existe.
     */
    @PostMapping("/save/{historyId}")
    public ResponseEntity<Map<String, String>> save(@PathVariable Long historyId,
            @RequestBody NursingControlEntity control) {

        Map<String, String> response = new HashMap<>();

        try {
            String successMessage = this.nursingControlService.save(control, historyId);

            response.put("message", successMessage);

            return ResponseEntity.ok(response);
        } catch (AlreadyExistsException e) {

            response.put("error", "Error: " + e.getMessage());

            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Actualiza un control de enfermería existente asociado a un historial médico.
     *
     * @param historyId Identificador del historial médico.
     * @param control   Control de enfermería a actualizar.
     * @return ResponseEntity con el mensaje de éxito o mensaje de error si ya
     *         existe.
     */
    @PutMapping("/update/{historyId}")
    public ResponseEntity<Map<String, String>> update(@PathVariable Long historyId,
            @RequestBody NursingControlEntity control) {

        Map<String, String> response = new HashMap<>();

        try {
            String successMessage = this.nursingControlService.update(control, historyId);

            response.put("message", successMessage);

            return ResponseEntity.ok(response);
        } catch (AlreadyExistsException e) {

            response.put("error", "Error: " + e.getMessage());

            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Elimina un control de enfermería por su identificador.
     *
     * @param id Identificador del control de enfermería a eliminar.
     * @return ResponseEntity con el mensaje de éxito o mensaje de error si no se
     *         encuentra.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {

        Map<String, String> response = new HashMap<>();

        try {
            String successMessage = this.nursingControlService.delete(id);

            response.put("message", successMessage);

            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {

            response.put("error", "Error: " + e.getMessage());

            return ResponseEntity.badRequest().body(response);
        }
    }
}