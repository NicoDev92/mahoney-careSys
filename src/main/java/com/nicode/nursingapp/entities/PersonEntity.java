package com.nicode.nursingapp.entities;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Clase abstracta que sirve como base para entidades que representan personas.
 *
 * Esta clase define los atributos comunes para las entidades que representan
 * individuos, como enfermeros/as, pacientes, etc. se declara abstracta con la
 * finalidad de heredarla en caso de que el sistema se amplie a más sectores del
 * hospital
 *
 * Atributos:
 * - id: Identificador único de la persona.
 * - firstName: Nombre de la persona.
 * - lastName: Apellido de la persona.
 * - idNumber: Número de identificación único y obligatorio.
 * - phoneNumber: Número de teléfono de la persona.
 * - dateOfBirth: Fecha de nacimiento de la persona.
 * - email: Dirección de correo electrónico de la persona.
 * - address: Dirección física de la persona.
 *
 * Esta clase utiliza la anotación JPA `MappedSuperclass` para indicar que no
 * debe
 * crearse una tabla para esta clase, pero se utilizará en las clases que la
 * extienden.
 *
 * Se utiliza la estrategia de herencia `TABLE_PER_CLASS`.
 *
 * Anotaciones Lombok utilizadas:
 * - @Getter: Genera automáticamente métodos getters para los atributos.
 * - @Setter: Genera automáticamente métodos setters para los atributos.
 * - @NoArgsConstructor: Genera automáticamente un constructor sin argumentos.
 */

@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Getter
@Setter
@NoArgsConstructor
public abstract class PersonEntity implements java.io.Serializable {

    /**
     * Identificador único de la persona.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre de la persona.
     */
    @Column(length = 75, nullable = false)
    private String firstName;

    /**
     * Apellido de la persona.
     */
    @Column(length = 75, nullable = false)
    private String lastName;

    /**
     * Número de identificación único y obligatorio.
     */
    @Column(length = 20, nullable = false, unique = true)
    private String idNumber;

    /**
     * Número de teléfono de la persona.
     */
    @Column(length = 20, nullable = false)
    private String phoneNumber;

    /**
     * Fecha de nacimiento de la persona.
     */
    @Column(nullable = false, columnDefinition = "DATE")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateOfBirth;

    /**
     * Dirección de correo electrónico de la persona.
     */
    @Column(length = 75)
    private String email;

    /**
     * Dirección física de la persona.
     */
    @Column(length = 150)
    private String address;

}
