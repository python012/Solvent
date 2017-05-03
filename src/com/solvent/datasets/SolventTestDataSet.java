package com.solvent.datasets;

import java.io.File;
import java.security.InvalidParameterException;
import java.util.ArrayList;

public class SolventTestDataSet {

	private SolventTestDataSetBean myBean;

	public SolventTestDataSet(SolventTestDataSetBean bean) {
		this.myBean = bean;
	}

	public String getName() {
		return myBean.getName();
	}

	public String getVariable(String varName) {
		String varValue = myBean.getVarValue(varName);
		if (null == varValue) {
			throw new InvalidParameterException("Input variable \"" + varName + "\" not found");
		}
		return varValue;
	}

	public ArrayList<String> getVariables(String listName) {
		ArrayList<String> vars = myBean.getFiles(listName);
		if (null == vars) {
			throw new InvalidParameterException("Input variable list \"" + listName + "\" not found");
		}
		return vars;
	}

	public File getFile(String fileName) {
		String filePath = myBean.getFile(fileName);
		if (null == filePath) {
			throw new InvalidParameterException("Input file variable \"" + fileName + "\" not found");
		}
		File file = new File(filePath);
		if (!file.exists()) {
			throw new InvalidParameterException("Input file \"" + filePath + "\" not found");
		}
		return file;
	}

	public ArrayList<File> getFiles(String listName) {
		ArrayList<String> filePaths = myBean.getFiles(listName);
		if (null == filePaths) {
			throw new InvalidParameterException("Input list variable \"" + listName + "\" not found");
		}
		ArrayList<File> files = new ArrayList<File>();
		for (String filePath : filePaths) {
			File file = new File(filePath);
			if (!file.exists()) {
				throw new InvalidParameterException("Input file \"" + filePath + "\" not found");
			}
			files.add(file);
		}
		return files;
	}
}
