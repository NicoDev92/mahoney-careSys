package com.nicode.nursingapp.entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * NursingControlEntity representa la clase de entidad para el control de
 * enfermería.
 * Contiene información sobre los parámetros vitales y observaciones realizadas
 * durante
 * un control de enfermería.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "nursing_controls")
public class NursingControlEntity {

    /**
     * Identificador único del control de enfermería.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nursing_control_id")
    private Long id;

    /**
     * Temperatura registrada durante el control.
     */
    @Column(columnDefinition = "Decimal(4,2)")
    private Double temperature;

    /**
     * Presión arterial registrada durante el control.
     */
    @Column(length = 10)
    private String bloodPressure;

    /**
     * Ritmo cardíaco registrado durante el control.
     */
    @Column(length = 5)
    private String heartRate;

    /**
     * Ritmo respiratorio registrado durante el control.
     */
    @Column(length = 5)
    private String respiratoryRate;

    /**
     * Observaciones adicionales hechas durante el control.
     */
    @Column(length = 250)
    private String observations;

    /**
     * Fecha y hora en que se realizó el control.
     */
    @Column(columnDefinition = "TIMESTAMP")
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime controlDate;

    /**
     * Entidad de historial de paciente asociada al control de enfermería.
     * La anotación `@JsonBackReference` se utiliza para evitar ciclos infinitos
     * al serializar a JSON, ya que la relación entre `NursingControlEntity` y
     * `PatientHistoryEntity` es bidireccional. Indica que la propiedad
     * `patientHistoryEntity` actúa como el extremo "atrás" de la relación.
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "patient_history_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonBackReference
    private PatientHistoryEntity patientHistoryEntity;
}
