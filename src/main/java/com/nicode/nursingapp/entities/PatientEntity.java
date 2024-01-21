package com.nicode.nursingapp.entities;

import java.time.LocalDate;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * PatientEntity representa la clase de entidad para los pacientes en el
 * sistema.
 * Contiene información personal y detalles médicos del paciente.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "patients")
public class PatientEntity extends PersonEntity {

    /**
     * Fecha de ingreso del paciente al centro médico.
     */
    @Column(nullable = false, columnDefinition = "DATE")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate admissionDate;

    /**
     * Número de habitación asignada al paciente.
     */
    @Column(nullable = false, length = 10)
    private Integer room;

    /**
     * Número de cama asignada al paciente.
     */
    @Column(nullable = false, length = 10)
    private Integer bed;

    /**
     * Servicio médico al que está asignado el paciente.
     */
    @Column(nullable = false, length = 35)
    private String service;

    /**
     * Fecha de alta médica del paciente.
     */
    @Column(columnDefinition = "DATE")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate medicalDischargeDate;

    /**
     * Entidad de historial de paciente asociada al paciente.
     * La anotación `@JsonManagedReference` se utiliza para manejar la serialización
     * a JSON.
     * Indica que la propiedad `patientHistory` actúa como el extremo "adelante" de
     * la relación
     * y se serializa cuando se procesa a JSON. Además, ayuda a evitar ciclos
     * infinitos al
     * serializar objetos relacionados.
     */
    @OneToOne(mappedBy = "patient", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonManagedReference
    private PatientHistoryEntity patientHistory;
}
