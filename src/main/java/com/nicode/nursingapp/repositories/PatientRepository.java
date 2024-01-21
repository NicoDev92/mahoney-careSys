package com.nicode.nursingapp.repositories;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import com.nicode.nursingapp.entities.PatientEntity;

/**
 * Interfaz de repositorio que proporciona operaciones CRUD (Crear, Leer,
 * Actualizar, Eliminar)
 * para entidades de tipo PatientEntity, utilizando el tipo Long como
 * identificador.
 */
@Repository
public interface PatientRepository extends ListCrudRepository<PatientEntity, Long> {

    /**
     * Verifica si existe un paciente con el número de identificación especificado.
     *
     * @param idNumber Número de identificación del paciente.
     * @return true si existe un paciente con el número de identificación
     *         especificado, false de lo contrario.
     */
    boolean existsByidNumber(String idNumber);
}