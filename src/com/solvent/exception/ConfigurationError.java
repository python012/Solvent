package com.solvent.exception;

import java.io.IOException;

public class ConfigurationError extends Exception {
	private String message;
	
	public ConfigurationError(String message) {
		super(message);
		this.message = message;
	}

	public ConfigurationError(String message, IOException e) {
		super(message + "\n" + e.getMessage());
		this.message = message + "\n" + e.getMessage();
	}
}
