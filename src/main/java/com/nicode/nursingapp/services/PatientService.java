package com.nicode.nursingapp.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nicode.nursingapp.entities.PatientEntity;
import com.nicode.nursingapp.exceptions.AlreadyExistsException;
import com.nicode.nursingapp.exceptions.NotFoundException;
import com.nicode.nursingapp.repositories.PatientPagingAndSortingRepository;
import com.nicode.nursingapp.repositories.PatientRepository;

/**
 * Servicio que gestiona las operaciones relacionadas con los pacientes.
 */
@Service
public class PatientService {

    private final PatientRepository repository;
    private final PatientPagingAndSortingRepository pagingAndSorting;

    /**
     * Constructor que inicializa las dependencias del servicio.
     *
     * @param repository       Repositorio para operaciones CRUD en entidades
     *                         PatientEntity.
     * @param pagingAndSorting Repositorio para operaciones de paginación y
     *                         ordenamiento en entidades PatientEntity.
     */
    @Autowired
    public PatientService(PatientRepository repository, PatientPagingAndSortingRepository pagingAndSorting) {
        this.repository = repository;
        this.pagingAndSorting = pagingAndSorting;
    }

    /**
     * Verifica si un paciente ya está registrado en base al número de
     * identificación.
     *
     * @param idNumber Número de identificación del paciente.
     * @return true si el paciente ya está registrado, false de lo contrario.
     */
    private boolean checkRegisteredStatus(String idNumber) {
        return this.repository.existsByidNumber(idNumber);
    }

    /**
     * Obtiene todos los pacientes.
     *
     * @return Lista de todos los pacientes.
     */
    @Transactional(readOnly = true)
    public List<PatientEntity> getAll() {
        return this.repository.findAll();
    }

    /**
     * Obtiene una página de pacientes.
     *
     * @param pageNumber       Número de página.
     * @param elementsQuantity Cantidad de elementos por página.
     * @return Página de pacientes.
     */
    @Transactional(readOnly = true)
    public Page<PatientEntity> getAllPaged(int pageNumber, int elementsQuantity) {
        Pageable pageRequest = PageRequest.of(pageNumber, elementsQuantity);
        return this.pagingAndSorting.findAll(pageRequest);
    }

    /**
     * Busca pacientes por nombre, apellido o servicio.
     *
     * @param pageNumber       Número de página.
     * @param elementsQuantity Cantidad de elementos por página.
     * @param keyword          Palabra clave para la búsqueda en los campos de
     *                         nombre, apellido o servicio.
     * @return Página de pacientes que coinciden con la palabra clave.
     */
    @Transactional(readOnly = true)
    public Page<PatientEntity> findBy(int pageNumber, int elementsQuantity, String keyword) {
        Pageable pageRequest = PageRequest.of(pageNumber, elementsQuantity);
        return this.pagingAndSorting.findAllByFirstNameContainingOrLastNameContainingOrServiceContaining(
                keyword, keyword, keyword, pageRequest);
    }

    /**
     * Obtiene un paciente por su identificador.
     *
     * @param id Identificador del paciente.
     * @return Paciente encontrado.
     * @throws NotFoundException Si el paciente no es encontrado.
     */
    @Transactional(readOnly = true)
    public PatientEntity findById(Long id) {
        return this.repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Paciente no encontrado. ID: " + id));
    }

    /**
     * Guarda un nuevo paciente verificando si ya está registrado.
     *
     * @param patient Paciente a guardar.
     * @return Mapa que contiene un mensaje y el paciente registrado.
     * @throws AlreadyExistsException Si el paciente ya está registrado.
     */
    @Transactional
    public Map<String, Object> save(PatientEntity patient) {
        boolean isRegistered = checkRegisteredStatus(patient.getIdNumber());
        Map<String, Object> response = new HashMap<>();
        if (!isRegistered) {
            response.put("message", "El paciente se ha registrado correctamente.");
            response.put("patient", this.repository.save(patient));
            return response;
        } else {
            throw new AlreadyExistsException("El paciente con el DNI: " + patient.getIdNumber() +
                    " ya está registrado. Por favor, verifique los datos ingresados.");
        }
    }

    /**
     * Actualiza los datos de un paciente existente.
     *
     * @param updates Datos actualizados del paciente.
     * @return Mensaje indicando el éxito de la operación.
     * @throws NotFoundException Si el paciente no es encontrado.
     */
    @Transactional
    public String update(PatientEntity updates) {
        Optional<PatientEntity> patientToUpdate = this.repository.findById(updates.getId());
        if (patientToUpdate.isPresent()) {
            this.repository.save(updates);
            return "Se han actualizado los datos del paciente de manera exitosa.";
        } else {
            throw new NotFoundException("No se pudo actualizar la información. No se encontró el " +
                    "paciente: " + updates.toString());
        }
    }

    /**
     * Elimina un paciente por su identificador.
     *
     * @param id Identificador del paciente a eliminar.
     * @return Mensaje indicando el éxito de la operación.
     * @throws NotFoundException Si el paciente no es encontrado.
     */
    @Transactional
    public String delete(Long id) {
        Optional<PatientEntity> patientToDelete = this.repository.findById(id);
        if (patientToDelete.isPresent()) {
            this.repository.deleteById(id);
            return "Se eliminó con éxito el paciente con el ID n°: " + id;
        } else {
            throw new NotFoundException("No se encontró el paciente con el ID n°: " + id);
        }
    }
}