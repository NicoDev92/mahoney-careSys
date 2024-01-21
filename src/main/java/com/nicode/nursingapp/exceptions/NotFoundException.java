package com.nicode.nursingapp.exceptions;

/**
 * Excepción personalizada lanzada cuando se intenta acceder a un elemento que
 * no se encuentra.
 * Extiende de RuntimeException para indicar que es una excepción no verificada.
 */
public class NotFoundException extends RuntimeException {

    /**
     * Construye una nueva instancia de la excepción con un mensaje específico.
     *
     * @param message El mensaje que describe la excepción.
     */
    public NotFoundException(String message) {
        super(message);
    }
}