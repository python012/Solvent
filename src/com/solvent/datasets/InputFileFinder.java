package com.solvent.datasets;

import java.io.InputStream;

import org.apache.log4j.Logger;

import com.solvent.Solvent;
import com.solvent.SolventLogger;
import com.solvent.SolventTestCase;

public class InputFileFinder {
	private static final Logger log = SolventLogger.getLogger(InputFileFinder.class.getName());

	public static InputStream getInputFileAsStream(SolventTestCase testClass) {
		return getInputFileAsStream(testClass.getClass());
	}

	public static InputStream getInputFileAsStream(Solvent solventClass) {
		return getInputFileAsStream(solventClass.getClass());
	}

	public static InputStream getInputFileAsStream(Class<?> testClass) {
		String packageName = "";
		if (testClass.getPackage() != null) {
			packageName = testClass.getPackage().getName() + ".";
		}
		String resourceBase = (packageName + testClass.getSimpleName()).replace('.', '/');
		String resourceName = resourceBase + ".xml";
		ClassLoader classLoader = testClass.getClassLoader();
		log.info("Searching for default input file: " + resourceName);

		return classLoader.getResourceAsStream(resourceName);
	}
}
