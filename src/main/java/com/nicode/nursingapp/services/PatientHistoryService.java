package com.nicode.nursingapp.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nicode.nursingapp.entities.PatientEntity;
import com.nicode.nursingapp.entities.PatientHistoryEntity;
import com.nicode.nursingapp.exceptions.AlreadyExistsException;
import com.nicode.nursingapp.exceptions.NotFoundException;
import com.nicode.nursingapp.repositories.PatientHistoryRepository;
import com.nicode.nursingapp.repositories.PatientRepository;

/**
 * Servicio que gestiona las operaciones relacionadas con el historial médico de
 * los pacientes.
 */
@Service
public class PatientHistoryService {

    private final PatientHistoryRepository repository;
    private final PatientRepository patientRepository;

    /**
     * Constructor que inicializa las dependencias del servicio.
     *
     * @param repository        Repositorio para operaciones CRUD en entidades
     *                          PatientHistoryEntity.
     * @param patientRepository Repositorio para operaciones CRUD en entidades
     *                          PatientEntity.
     */
    @Autowired
    public PatientHistoryService(PatientHistoryRepository repository, PatientRepository patientRepository) {
        this.repository = repository;
        this.patientRepository = patientRepository;
    }

    /**
     * Obtiene una Historia Clínica por su identificador.
     *
     * @param historyId Identificador de la Historia Clínica.
     * @return Un Optional que puede contener la Historia Clínica si es encontrada,
     *         o estar vacío si no se encuentra.
     */
    @Transactional(readOnly = true)
    public Optional<PatientHistoryEntity> findById(Long historyId) {
        return this.repository.findById(historyId);
    }

    /**
     * Obtiene la Historia Clínica asociada a un paciente por su identificador.
     *
     * @param patientId Identificador del paciente.
     * @return La Historia Clínica asociada al paciente encontrado.
     * @throws NotFoundException Si el paciente no cuenta con Historia Clínica.
     */
    @Transactional(readOnly = true)
    public PatientHistoryEntity findByPatientId(Long patientId) {
        return this.repository.getPatientHistoryEntityByPatientId(patientId)
                .orElseThrow(() -> new NotFoundException("El paciente no cuenta con Historia Clínica."));
    }

    /**
     * Guarda una nueva Historia Clínica asociándola a un paciente.
     *
     * @param history   Historia Clínica a guardar.
     * @param patientId Identificador del paciente.
     * @return Mensaje indicando el éxito de la operación.
     * @throws AlreadyExistsException Si el paciente ya cuenta con una Historia
     *                                Clínica registrada.
     */
    @Transactional
    public String save(PatientHistoryEntity history, Long patientId) {

        Optional<PatientEntity> existingPatient = this.patientRepository.findById(patientId);

        Optional<PatientHistoryEntity> existingHistory = this.repository.getPatientHistoryEntityByPatientId(patientId);

        if (existingPatient.isPresent() && !existingHistory.isPresent()) {
            history.setPatient(existingPatient.get());

            this.repository.save(history);
            return ("Guardado con éxito!");
        } else {
            throw new AlreadyExistsException("El paciente " + history.getPatient().getFirstName() +
                    " " + history.getPatient().getLastName() +
                    " ya cuenta con una Historia Clínica registrada. Por favor modifique la existente.");
        }
    }

    /**
     * Actualiza los datos de una Historia Clínica existente.
     *
     * @param updates   Datos actualizados de la Historia Clínica.
     * @param patientId Identificador del paciente.
     * @return Mensaje indicando el éxito de la operación.
     * @throws NotFoundException Si el paciente no existe o no cuenta con Historia
     *                           Clínica.
     */
    @Transactional
    public String update(PatientHistoryEntity updates, Long patientId) {

        Optional<PatientEntity> existingPatient = this.patientRepository.findById(patientId);

        Optional<PatientHistoryEntity> existingHistory = this.repository.getPatientHistoryEntityByPatientId(patientId);

        if (existingPatient.isPresent() && existingHistory.isPresent()) {
            updates.setPatient(existingPatient.get());

            this.repository.save(updates);

            return ("Se actualizó la información con éxito.");
        } else {
            throw new NotFoundException("El paciente no existe o no cuenta con Historia Clínica.");
        }
    }

    /**
     * Elimina una Historia Clínica por su identificador.
     *
     * @param historyId Identificador de la Historia Clínica a eliminar.
     * @return Mensaje indicando el éxito de la operación.
     * @throws NotFoundException Si la Historia Clínica no es encontrada.
     */
    @Transactional
    public String delete(Long historyId) {
        Optional<PatientHistoryEntity> existingPatienHistory = this.repository.findById(historyId);

        if (existingPatienHistory.isPresent()) {
            this.repository.deleteById(historyId);
            return ("Eliminado con éxito!");
        } else {
            throw new NotFoundException("No se encontró la historia clínica con el ID: " + historyId);
        }
    }
}