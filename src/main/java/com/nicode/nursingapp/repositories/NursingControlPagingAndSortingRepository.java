package com.nicode.nursingapp.repositories;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.nicode.nursingapp.entities.NursingControlEntity;

/**
 * Interfaz que extiende de PagingAndSortingRepository para proporcionar
 * operaciones de paginación
 * y ordenamiento para entidades de NursingControlEntity.
 */
public interface NursingControlPagingAndSortingRepository
                extends PagingAndSortingRepository<NursingControlEntity, Long> {

        /**
         * Recupera una página de registros de NursingControlEntity relacionados con un
         * historial médico específico,
         * ordenados por fecha de control de forma ascendente.
         *
         * @param patientHistoryId Identificador del historial médico asociado a los
         *                         registros.
         * @param pageable         Objeto que encapsula información de paginación y
         *                         ordenamiento.
         * @return Página de registros de NursingControlEntity.
         */
        Page<NursingControlEntity> findByPatientHistoryEntityIdOrderByControlDateAsc(Long patientHistoryId,
                        Pageable pageable);

        /**
         * Recupera una página de registros de NursingControlEntity relacionados con un
         * historial médico específico,
         * ordenados por fecha de control de forma descendente.
         *
         * @param patientHistoryId Identificador del historial médico asociado a los
         *                         registros.
         * @param pageable         Objeto que encapsula información de paginación y
         *                         ordenamiento.
         * @return Página de registros de NursingControlEntity.
         */
        Page<NursingControlEntity> findByPatientHistoryEntityIdOrderByControlDateDesc(Long patientHistoryId,
                        Pageable pageable);

        /**
         * Recupera una página de registros de NursingControlEntity relacionados con un
         * historial médico específico,
         * cuyas fechas de control se encuentran dentro de un rango dado, ordenados por
         * fecha de control de forma ascendente.
         *
         * @param patientHistoryId Identificador del historial médico asociado a los
         *                         registros.
         * @param startDate        Fecha de inicio del rango.
         * @param endDate          Fecha de fin del rango.
         * @param pageable         Objeto que encapsula información de paginación y
         *                         ordenamiento.
         * @return Página de registros de NursingControlEntity.
         */
        Page<NursingControlEntity> findByPatientHistoryEntityIdAndControlDateBetweenOrderByControlDateAsc(
                        Long patientHistoryId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}
