package com.solvent.exception;

public class SolventException extends Exception {

    private String message;

    public SolventException(String message) {
        this.message = message;
    }
}