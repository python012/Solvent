package com.solvent;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import org.apache.log4j.Logger;

public class Configurator {
	public static final String DEFAULT_PROPERTIES_RESOURCE = "/com/solvent/conf/solvent-defaults.properties";
	public static final String SOLVENT_PROPERTIES = "solvent.conf";
	private static Properties defaults = new Properties();

	private HashMap<String, String> confMap = new HashMap<String, String>();
	private static Configurator INSTANCE = null;

	private static Logger log = SolventLogger.getLogger(Configurator.class);

	private static String propFileName = System.getProperty(SOLVENT_PROPERTIES);

	static {
		System.out.println("Loading defaults......");
		try {
			defaults.load(Configurator.class.getResourceAsStream(DEFAULT_PROPERTIES_RESOURCE));
		} catch (IOException e) {
			throw new ConfigurationError(e.getMessage(), e);
		}
		if (null != propFileName) {
			File propFile = new File(propFileName);
			log.info("\nusing propFile: " + propFile.getName());
			try {
				Properties props = new Properties();
				props.load(new FileInputStream(propFile));
				initialize(props);
			} catch (IOException e) {
				throw new ConfigurationError("Error while loading configuration file: " + propFile.getAbsolutePath(),
						e);
			}
		}
	}
	
	//*************************************************************************
	
	public static final String SOLVENT_OUTPUT_VERBOSITY = "solvent.output.verbosity";
	public static final String SOLVENT_OUTPUT_PATTERN = "solvent.output.pattern";
	public static final String SOLVENT_OUTPUT_FILENAME = "solvent.output.file";
	
	//*************************************************************************

	public static String getSolventOutputVerbosity() {
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
