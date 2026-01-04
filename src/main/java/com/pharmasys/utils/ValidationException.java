package com.pharmasys.utils;

/**
 * Exception levée lors d'une erreur de validation
 */
public class ValidationException extends Exception {
    public ValidationException(String message) {
        super(message);
    }
}
