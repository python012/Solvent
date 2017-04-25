package com.solvent.datasets;

import java.io.InputStream;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.solvent.SolventLogger;

public class InputFileDigester {
	private static final Logger log = SolventLogger.getLogger(InputFileDigester.class.getName());
	private Document doc;
	private SolventTestDataSet workingDataSet;

	public InputFileDigester(InputStream in) {
		SAXReader reader = new SAXReader();
		try {
			doc = reader.read(in);
		} catch (DocumentException e) {
			log.error("Error when trying to parse input file.", e);
		}
	}

	public SolventTestDataSet getWorkingDataSet() {
		return workingDataset;
	}

	public ArrayList<SolventTestDataSet> parseDataSets(String suiteDataSetName) {
		ArrayList<SolventTestDataSet> dataSetCollection = new ArrayList<SolventTestDataSet>();
		Element workingDS = (Element)this.doc.selectSingleNode("/testConfig/workingDataSet");
		String workingDSName = null;

		if (suiteDataSetName != null && suiteDataSetName.trim().length() != 0) {
			workingDSName = suiteDataSetName.trim();
		} else {
			if (workingDS != null) {
				workingDSName = workingDS.attributeValue("name");
			}
		}
		log.debug("========== Working Data Set: " + workingDSName);
		
		if (workingDSName != null) {
			SolventTestCase.setDataSetOverride(workingDSName);
		}
	}

	
	
	
	
	
	
	
}
