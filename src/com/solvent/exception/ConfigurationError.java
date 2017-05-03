package com.solvent.exception;

import java.io.IOException;

public class ConfigurationError extends Exception {
	
	public ConfigurationError(String message, IOException e) {
		super(message, e);
	}
}
