package com.nicode.nursingapp.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nicode.nursingapp.entities.NursingControlEntity;
import com.nicode.nursingapp.entities.PatientHistoryEntity;
import com.nicode.nursingapp.exceptions.NotFoundException;
import com.nicode.nursingapp.repositories.NursingControlPagingAndSortingRepository;
import com.nicode.nursingapp.repositories.NursingControlRepository;
import com.nicode.nursingapp.repositories.PatientHistoryRepository;

/**
 * Servicio que gestiona las operaciones relacionadas con los controles de
 * enfermería.
 */
@Service
public class NursingControlService {

    private final NursingControlRepository repository;
    private final NursingControlPagingAndSortingRepository pagingAndSorting;
    private final PatientHistoryRepository historyRepository;
    private final PatientService patientService;

    /**
     * Constructor que inicializa las dependencias del servicio.
     *
     * @param repository        Repositorio para operaciones CRUD en entidades
     *                          NursingControlEntity.
     * @param pagingAndSorting  Repositorio para operaciones de paginación y
     *                          ordenamiento en entidades NursingControlEntity.
     * @param historyRepository Repositorio para operaciones CRUD en entidades
     *                          PatientHistoryEntity.
     * @param patientService    Servicio para operaciones relacionadas con
     *                          pacientes.
     */
    @Autowired
    public NursingControlService(NursingControlRepository repository,
            NursingControlPagingAndSortingRepository pagingAndSorting, PatientHistoryRepository historyRepository,
            PatientService patientService) {
        this.repository = repository;
        this.pagingAndSorting = pagingAndSorting;
        this.historyRepository = historyRepository;
        this.patientService = patientService;
    }

    /**
     * Obtiene todos los controles de enfermería asociados a un paciente.
     *
     * @param patientId Identificador del paciente.
     * @return Lista de controles de enfermería asociados al historial médico del
     *         paciente.
     * @throws NotFoundException Si no se encuentra ningún control de enfermería.
     */
    @Transactional(readOnly = true)
    public List<NursingControlEntity> getAll(Long patientId) {
        Long historyId = this.patientService.findById(patientId).getPatientHistory().getId();
        List<NursingControlEntity> nursingControls = this.repository.findByPatientHistoryEntityId(historyId);

        if (nursingControls.isEmpty()) {
            throw new NotFoundException("No se encontró ningún control de enfermería con el ID: " + historyId);
        }

        return nursingControls;
    }

    /**
     * Obtiene una página de controles de enfermería asociados a un historial
     * médico.
     *
     * @param pageNumber       Número de página.
     * @param elementsQuantity Cantidad de elementos por página.
     * @param historiId        Identificador del historial médico.
     * @return Página de controles de enfermería ordenados por fecha de control
     *         ascendente.
     */
    @Transactional(readOnly = true)
    public Page<NursingControlEntity> getAllPaged(int pageNumber, int elementsQuantity, Long historiId) {
        Pageable pageRequest = PageRequest.of(pageNumber, elementsQuantity);

        return this.pagingAndSorting.findByPatientHistoryEntityIdOrderByControlDateAsc(historiId, pageRequest);
    }

    /**
     * Obtiene una página de controles de enfermería asociados a un historial médico
     * y dentro de un rango de fechas.
     *
     * @param patientHistoryId Identificador del historial médico.
     * @param startDate        Fecha de inicio del rango.
     * @param endDate          Fecha de fin del rango.
     * @param page             Número de página.
     * @param elements         Cantidad de elementos por página.
     * @return Página de controles de enfermería ordenados por fecha de control
     *         ascendente.
     * @throws IllegalArgumentException Si las fechas de búsqueda están después de
     *                                  la fecha y hora actual.
     */
    @Transactional(readOnly = true)
    public Page<NursingControlEntity> getInDateRangeDesc(
            Long patientHistoryId, LocalDateTime startDate,
            LocalDateTime endDate, int page, int elements) {

        Pageable pageRequest = PageRequest.of(page, elements);

        LocalDateTime currentDate = LocalDateTime.now();
        if (startDate.isAfter(currentDate) || endDate.isAfter(currentDate)) {
            throw new IllegalArgumentException("Las fechas de búsqueda deben estar antes de la fecha y hora actual.");
        }

        return this.pagingAndSorting
                .findByPatientHistoryEntityIdAndControlDateBetweenOrderByControlDateAsc(
                        patientHistoryId, startDate, endDate, pageRequest);
    }

    /**
     * Obtiene un control de enfermería por su identificador.
     *
     * @param id Identificador del control de enfermería.
     * @return Control de enfermería encontrado.
     * @throws NotFoundException Si el control de enfermería no es encontrado.
     */
    @Transactional(readOnly = true)
    public NursingControlEntity findById(Long id) {
        return this.repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Control de enfermería no encontrado"));
    }

    /**
     * Guarda un nuevo control de enfermería asociándolo a un historial médico.
     *
     * @param nursingControl Control de enfermería a guardar.
     * @param historyId      Identificador del historial médico.
     * @return Mensaje indicando el éxito de la operación.
     * @throws NotFoundException Si el paciente no existe o no cuenta con historial
     *                           médico donde guardar los controles.
     */
    @Transactional
    public String save(NursingControlEntity nursingControl, Long historyId) {

        Optional<PatientHistoryEntity> history = this.historyRepository.findById(historyId);

        if (history.isPresent()) {
            nursingControl.setPatientHistoryEntity(history.get());
            this.repository.save(nursingControl);
            return ("Se guardó correctamente el control de enfermería");
        } else {
            throw new NotFoundException(
                    "El paciente no existe o no cuenta con historia clínica donde guardar los controles.");
        }
    }

    /**
     * Actualiza los datos de un control de enfermería existente.
     *
     * @param updates   Datos actualizados del control de enfermería.
     * @param historyId Identificador del historial médico.
     * @return Mensaje indicando el éxito de la operación.
     * @throws NotFoundException Si el paciente no existe o no cuenta con historial
     *                           médico donde guardar los controles.
     */
    @Transactional
    public String update(NursingControlEntity updates, Long historyId) {

        Optional<PatientHistoryEntity> history = this.historyRepository.findById(historyId);
        Optional<NursingControlEntity> nursingControl = this.repository.findById(updates.getId());

        if (history.isPresent() && nursingControl.isPresent()) {
            this.repository.save(updates);
            return ("Se actualizaron correctamente los datos del control de enfermería.");
        } else {
            throw new NotFoundException(
                    "El paciente no existe o no cuenta con historia clínica donde guardar los controles.");
        }
    }

    /**
     * Elimina un control de enfermería por su identificador.
     *
     * @param controlId Identificador del control de enfermería a eliminar.
     * @return Mensaje indicando el éxito de la operación.
     * @throws NotFoundException Si el control de enfermería no es encontrado.
     */
    @Transactional
    public String delete(Long controlId) {
        Optional<NursingControlEntity> existingControl = this.repository.findById(controlId);

        if (existingControl.isPresent()) {
            this.repository.deleteById(controlId);
            return ("Control de enfermería borrado con éxito.");
        } else {
            throw new NotFoundException("No se encontró un control de enfermería con el ID: " + controlId);
        }
    }
}
