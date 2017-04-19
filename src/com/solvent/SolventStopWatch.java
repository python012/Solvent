package com.solvent;

import org.apache.commons.lang3.time.StopWatch;

public class SolventStopWatch extends StopWatch {

	private String id;

	public SolventStopWatch() {
		super();
		this.id = "";
	}

	public SolventStopWatch(String id) {
		this.id = id;
	}
	
	public String getID() {
		return this.id;
	}
}
