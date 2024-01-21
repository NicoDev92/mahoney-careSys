package com.nicode.nursingapp.repositories;

import java.util.Optional;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import com.nicode.nursingapp.entities.PatientHistoryEntity;

/**
 * Interfaz de repositorio que proporciona operaciones CRUD (Crear, Leer,
 * Actualizar, Eliminar)
 * para entidades de tipo PatientHistoryEntity, utilizando el tipo Long como
 * identificador.
 */
@Repository
public interface PatientHistoryRepository extends ListCrudRepository<PatientHistoryEntity, Long> {

    /**
     * Recupera un Optional que contiene un objeto PatientHistoryEntity asociado a
     * un paciente específico.
     *
     * @param id Identificador del paciente asociado al historial médico.
     * @return Optional que puede contener un objeto PatientHistoryEntity si se
     *         encuentra, o estar vacío si no se encuentra.
     */
    Optional<PatientHistoryEntity> getPatientHistoryEntityByPatientId(Long id);
}