package com.nicode.nursingapp.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.nicode.nursingapp.entities.PatientEntity;

/**
 * Interfaz de repositorio que proporciona operaciones de paginación y
 * ordenamiento
 * para entidades de tipo PatientEntity, utilizando el tipo Long como
 * identificador.
 */
public interface PatientPagingAndSortingRepository extends PagingAndSortingRepository<PatientEntity, Long> {

    /**
     * Recupera una página de registros de PatientEntity que coinciden con los
     * criterios de búsqueda en los campos
     * de nombre, apellido o servicio, ordenados y paginados según la información
     * proporcionada por Pageable.
     *
     * @param keyword1 Palabra clave para la búsqueda en el campo de primer nombre.
     * @param keyword2 Palabra clave para la búsqueda en el campo de apellido.
     * @param keyword3 Palabra clave para la búsqueda en el campo de servicio.
     * @param pageable Objeto que encapsula información de paginación y
     *                 ordenamiento.
     * @return Página de registros de PatientEntity que cumplen con los criterios de
     *         búsqueda.
     */
    Page<PatientEntity> findAllByFirstNameContainingOrLastNameContainingOrServiceContaining(
            String keyword1, String keyword2, String keyword3, Pageable pageable);
}
