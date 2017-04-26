package com.solvent.datasets;

import java.util.ArrayList;
import java.util.Hashtable;

public class SolventTestDataSetBean {
	private Hashtable<String, String> myVars = new Hashtable<String, String>();
	private Hashtable<String, ArrayList<String>> myVarLists =
			new Hashtable<String, ArrayList<String>>();
	private Hashtable<String, String> myFiles = new Hashtable<String, String>();
	private Hashtable<String, ArrayList<String>> myFileLists =
			new Hashtable<String, ArrayList<String>>();
	private String name;

	public SolventTestDataSetBean(String name) {
		this.name = name; // this name is the name of some <dataset> in the XML file
	}

	public void addVar(String varName, String varValue) {
		myVars.put(varName, varValue);
	}

	public void addVarList(String varName, ArrayList<String> valueList) {
		myVarLists.put(varName, valueList);
	}

	public void addFile(String fileName, String filePath) {
		myFiles.put(fileName, filePath);
	}

	public void addFiles(String fileName, ArrayList<String> fileList) {
		myFileLists.put(fileName, fileList);
	}

	public String getName() {
		return this.name;
	}

	public String getVarValue(String varName) {
		if (myVars.contains(varName)) {
			return myVars.get(varName);
		}
		return null;
	}

	public ArrayList<String> getVarValues(String listName) {
		if (myVarLists.contains(listName)) {
			return myVarLists.get(listName);
		}
		return null;
	}

	public String getFile(String fileName) {
		if (myFiles.contains(fileName)) {
			return myFiles.get(fileName);
		}
		return null;
	}

	public ArrayList<String> getFiles(String listName) {
		if (myFileLists.contains(listName)) {
			return myFileLists.get(listName);
		}
		return null;
	}
}
