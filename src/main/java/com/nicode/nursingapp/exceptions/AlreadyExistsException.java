package com.nicode.nursingapp.exceptions;

/**
 * Excepción personalizada lanzada cuando se intenta agregar un elemento que ya
 * existe.
 * Extiende de RuntimeException para indicar que es una excepción no verificada.
 */
public class AlreadyExistsException extends RuntimeException {

    /**
     * Construye una nueva instancia de la excepción con un mensaje específico.
     *
     * @param message El mensaje que describe la excepción.
     */
    public AlreadyExistsException(String message) {
        super(message);
    }
}