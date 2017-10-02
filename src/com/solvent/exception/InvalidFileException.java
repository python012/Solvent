package com.solvent.exception;

public class InvalidFileException extends Exception {
    private String message;

    public InvalidFileException(String message) {
        this.message = message;
    }
}
