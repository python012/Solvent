package com.solvent.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.solvent.SolventLogger;
import com.solvent.exception.ConfigurationError;

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

	// *************************************************************************

	public static final String SOLVENT_OUTPUT_VERBOSITY = "solvent.output.verbosity";
	public static final String SOLVENT_OUTPUT_PATTERN = "solvent.output.pattern";
	public static final String SOLVENT_OUTPUT_FILENAME = "solvent.output.file";

	// *************************************************************************

	public static String getSolventOutputVerbosity() {
		return getString(SOLVENT_OUTPUT_VERBOSITY);
	}

	public static String getSolventOutputPattern() {
		return getString(SOLVENT_OUTPUT_PATTERN);
	}

	public static File getSolventOutputFile() {
		return getFile(SOLVENT_OUTPUT_FILENAME, null);
	}

	private Configurator() {
		setDefaults();
	}

	private Configurator(Properties prop) {
		this();
		String pattern = prop.getProperty(SOLVENT_OUTPUT_PATTERN);
		String verbosity = prop.getProperty(SOLVENT_OUTPUT_VERBOSITY);
		File logFile = getLogFile(prop.getProperty(SOLVENT_OUTPUT_FILENAME));
		if (logFile != null) {
			prop.setProperty(SOLVENT_OUTPUT_FILENAME, logFile.getAbsolutePath());
		}
		SolventLogger.initializeLogging(pattern, verbosity, logFile);

		for (Entry<Object, Object> entry : prop.entrySet()) {
			String key = (String) entry.getKey();
			String value = (String) entry.getValue();

			if (value != null && !value.isEmpty()) {
				log.info("Loading configuration property: " + key + " = " + value);
				confMap.put(key, value);
			}
		}
	}

	// *************************************************************************
	public static void initialize(Properties prop) {
		INSTANCE = new Configurator(prop);
	}

	private static Configurator getInstance() {
		return INSTANCE;
	}

	private void setDefaults() {
		// L10N
		setDefault(L10N_LANGUAGE);
		// aut.properties
		setDefault(APP_URL);
		// solvent.log4j.properties (output)
		setDefault(SOLVENT_OUTPUT_VERBOSITY);
		setDefault(SOLVENT_OUTPUT_PATTERN);
		setDefault(SOLVENT_OUTPUT_FILENAME);
	}

	private void setDefault(String option) {
		String value = defaults.getProperty(option);
		if (value != null && value.trim().isEmpty()) {
			value = null;
		}
		confMap.put(option, value);
	}

	private static File getLogFile(String logFileName) {
		File logFile = null;
		if (logFileName != null && !logFileName.trim().isEmpty()) {
			logFile = new File(logFileName);
			if (!logFile.isAbsolute()) {
				logFile = new File(System.getProperty("user.home", logFileName));
			}
		}
		// System.out.println("\nlogfile path:" + logFile.getAbsolutePath());
		log.info("\nlogfile path:" + logFile.getAbsolutePath());
		return logFile;
	}

	private static String getString(String optionName) {
		return getInstance().confMap.get(optionName);
	}

	private static int getIntOption(String optionName) {
		try {
			String value = getInstance().confMap.get(optionName);
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			log.warn("optionName: " + optionName + " cannot be parsed to Integer! 0 is returned.");
			return 0;
		}
	}

	private static File getFile(String optionName, String defaultValue) {
		String value = getInstance().confMap.get(optionName);
		if (null == value) {
			return (null == defaultValue) ? null
					: new File(new File(System.getProperty("user.home"), ".semet"), defaultValue);
		}
		log.info("fileName: " + value);
		return new File(value);
	}

	// *************************************************************************

	public static final String APP_URL = "app.url";
	public static final String SELENIUM_PORT = "selenium.port";
	public static final String BROWSER_COMMAND = "browser.command";
	public static final String SELENIUM_HOST = "selenium.host";
	public static final String WEBDRIVER_NAME = "webdriver.name";
	public static final String TESTCASE_TIMEOUT = "solvent.test.timeout";
	public static final String L10N_LANGUAGE = "l10n.language";

	// *************************************************************************

	public static String getAppURL() {
		return getString(APP_URL);
	}

	public static int getTestCaseTimeout() {
		return getIntOption(TESTCASE_TIMEOUT) * 60 * 1000;
	}

}
