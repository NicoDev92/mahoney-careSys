package com.nicode.nursingapp.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Clase que representa la entidad de historial médico de un paciente.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "patients_histories")
public class PatientHistoryEntity {

    /**
     * Identificador único del historial médico del paciente.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patient_history_id")
    private Long id;

    /**
     * Género del paciente (masculino, femenino, etc.).
     */
    @Column(length = 15)
    private String sex;

    /**
     * Altura del paciente en metros.
     */
    @Column(columnDefinition = "Decimal(3,2)")
    private Double height;

    /**
     * Peso del paciente en kilogramos.
     */
    @Column(columnDefinition = "Decimal(5,2)")
    private Double weight;

    /**
     * Tipo de sangre del paciente.
     */
    @Column(length = 5)
    private String bloodType;

    /**
     * Observaciones adicionales sobre el historial médico del paciente.
     */
    @Column(length = 250)
    private String observations;

    /**
     * Relación uno a uno con la entidad de paciente.
     * Representa al paciente asociado a este historial médico.
     * Se utiliza @JsonBackReference para evitar ciclos infinitos durante la
     * serialización.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    @JsonBackReference
    private PatientEntity patient;

    /**
     * Relación uno a muchos con la entidad de controles de enfermería.
     * Representa los controles de enfermería asociados a este historial médico.
     * Se utiliza @JsonManagedReference para evitar ciclos infinitos durante la
     * serialización.
     */
    @OneToMany(mappedBy = "patientHistoryEntity", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<NursingControlEntity> nursingControls;
}