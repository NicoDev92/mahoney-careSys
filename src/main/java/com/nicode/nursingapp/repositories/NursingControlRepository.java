package com.nicode.nursingapp.repositories;

import java.util.List;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import com.nicode.nursingapp.entities.NursingControlEntity;

/**
 * Interfaz de repositorio que proporciona operaciones CRUD (Crear, Leer,
 * Actualizar, Eliminar)
 * para entidades de tipo NursingControlEntity, utilizando el tipo Long como
 * identificador.
 */
@Repository
public interface NursingControlRepository extends ListCrudRepository<NursingControlEntity, Long> {

    /**
     * Recupera una lista de registros de NursingControlEntity relacionados con un
     * historial médico específico.
     *
     * @param id Identificador del historial médico asociado a los registros.
     * @return Lista de registros de NursingControlEntity.
     */
    List<NursingControlEntity> findByPatientHistoryEntityId(Long id);
}
