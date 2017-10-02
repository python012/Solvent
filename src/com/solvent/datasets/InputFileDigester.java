package com.solvent.datasets;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.solvent.SolventLogger;
import com.solvent.SolventTestCase;
import com.solvent.exception.SolventException;

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
        return workingDataSet;
    }

    public ArrayList<SolventTestDataSet> parseDataSets(String suiteDataSetName) throws SolventException {
        ArrayList<SolventTestDataSet> dataSetCollection = new ArrayList<SolventTestDataSet>();
        Element workingDS = (Element) this.doc.selectSingleNode("/testConfig/workingDataSet");
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
            Element datasets = (Element) this.doc.selectSingleNode("//dataset");
            for (Iterator d = datasets.elementIterator("dataset"); d.hasNext(); ) {
                Element data = (Element) d.next();
                String dataSetName = data.attributeValue("name");
                SolventTestDataSetBean dataSetBean = new SolventTestDataSetBean(dataSetName);
                for (Iterator v = data.elementIterator("var"); v.hasNext(); ) {
                    Element elem = (Element) v.next();
                    dataSetBean.addVar(elem.valueOf("@name"), elem.valueOf("."));
                }
                for (Iterator f = data.elementIterator("file"); f.hasNext(); ) {
                    Element elem = (Element) f.next();
                    dataSetBean.addVar(elem.valueOf("@name"), elem.valueOf("."));
                }

                for (Iterator l = data.elementIterator("file"); l.hasNext(); ) {
                    Element elem = (Element) l.next();
                    String listName = elem.attributeValue("name");
                    ArrayList<String> varList = new ArrayList<String>();
                    for (Iterator v = elem.elementIterator("var"); v.hasNext(); ) {
                        Element listElem = (Element) v.next();
                        varList.add(listElem.getText());
                    }
                    if (varList.size() > 0) {
                        dataSetBean.addVarList(listName, varList);
                    } else {
                        ArrayList<String> fileList = new ArrayList<String>();
                        for (Iterator f = elem.elementIterator("file"); f.hasNext(); ) {
                            Element listElem = (Element) f.next();
                            fileList.add(listElem.getText());
                        }
                        dataSetBean.addFiles(listName, fileList);
                    }
                }

                SolventTestDataSet dataSet = new SolventTestDataSet(dataSetBean);
                dataSetCollection.add(dataSet);
                if (dataSetName.equals(workingDSName)) {
                    this.workingDataSet = dataSet;
                } else {
                    throw new SolventException("No Data Set defined!");
                }
            }
        }
        return dataSetCollection;
    }
}
