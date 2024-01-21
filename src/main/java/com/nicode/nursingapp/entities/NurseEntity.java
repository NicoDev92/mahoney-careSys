package com.nicode.nursingapp.entities;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * NurseEntity representa la clase de entidad para el personal de enfermería.
 * Extiende la clase PersonEntity e incluye campos adicionales específicos
 * para los/las enfermeros/as.
 * 
 * Esta clase se implementó a modo de ejemplo para ver la simplificacion de
 * código
 * a través de la herencia de Person.
 * 
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "nurses")
public class NurseEntity extends PersonEntity {

    /**
     * Fecha en que la enfermera comenzó a trabajar.
     */
    @Column(nullable = false, columnDefinition = "DATETIME")
    private LocalDate ingressDate;

    /**
     * Especialización o campo de experiencia de la enfermera.
     */
    @Column(nullable = false)
    private String specialty;

    /**
     * Departamento o unidad a la que está asignada la enfermera.
     */
    @Column(nullable = false)
    private String department;

    /**
     * Estado laboral de la enfermera (por ejemplo, tiempo completo, medio tiempo).
     */
    @Column(nullable = false)
    private String status;

    /**
     * Número de licencia de enfermería único asignado a la enfermera.
     */
    @Column(nullable = false, unique = true)
    private Integer nursingLicense;
}
