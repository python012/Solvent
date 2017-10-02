package com.solvent;

import org.apache.log4j.Logger;

import com.solvent.exception.SolventException;

public class CheckPoint {
    private final String id;
    private String description = "";
    private Throwable error = null;
    private String message = "";
    private boolean result = false;
    protected static Logger log;

    private CheckPoint(String id) throws SolventException {
        if (null == id || id.trim().isEmpty()) {
            throw new SolventException("CheckPoint ID cannot be null or an empty string!");
        }
        this.id = id;
        log.info("CheckPoint: " + id + " created.");
    }

    protected CheckPoint(String id, String description) throws SolventException {
        this(id);
        if (null == description) {
            throw new SolventException("CheckPoint Description cannot be null!");
        }
        this.description = description;
    }

    protected String getId() {
        return this.id;
    }

    public void validate(boolean condition, String failureMessage) {
        if (condition) {
            succeeded();
        } else {
            failed(failureMessage);
        }
    }

    public void validate(boolean condition) {
        validate(condition, null);
    }

    public void failed(String errorMessage) {
        this.result = false;
        this.message = prepareMessage(errorMessage, result);
    }

    public void failed(String message, Throwable t) {
        this.result = false;
        this.message = message;
        this.error = t;
        log.error("CheckPoint:" + this.getId() + ":" + message + " failed.");
    }

    public void succeeded() {
        succeeded(null);
    }

    public void succeeded(String successMessage) {
        this.result = true;
        this.message = prepareMessage(successMessage, result);
        log.info(this.message);
    }

    private String prepareMessage(String message, boolean success) {
        StringBuilder text = new StringBuilder("CheckPotion: " + id);
        if (!description.trim().isEmpty()) {
            text.append(" (" + description + ")");
        }
        text.append(success ? " succeeded." : " failed!");
        if (!(message == null || message.trim().isEmpty())) {
            text.append(success ? " Message: " : " Reason: ");
            text.append(message);
        }
        return text.toString();
    }

    public boolean status() {
        return result;
    }

    public Throwable getError() {
        return this.error;
    }

    public String getStatusMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
