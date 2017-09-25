package com.semet;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.solvent.Solvent;
import com.solvent.SolventLogger;

public abstract class SeMet extends Solvent {
	
	private static final Logger log  = SolventLogger.getLogger(SeMet.class);	
	private String id = null;
	private String name = null;
	private String XPath = null;
	private String rootXPath = null;
	private WebElement element = null;
	private String elementTag = "*";
	
	protected static RemoteWebDriver driver = null;
	
	public RemoteWebDriver getRemoteWebDriver() {
		return SetMetWebDriverSession.get();
	}
	
	public void setRemoteWebDriver(RemoteWebDriver remotewebdriver) {
		SetMetWebDriverSession.set(remotewebdriver);
		driver = SetMetWebDriverSession.get();
	}
	
	protected SeMet() {
		this((RemoteWebDriver)null);
		log.debug("\n\n called:: 'SeMet()'");
	}
	
	
	protected SeMet(RemoteWebDriver driver) {
		log.debug("\n\n called:: 'SeMet(RemoteWebDriver driver)'");
		if (null != driver) {
			SetMetWebDriverSession.set(driver);
		}
		driver  = SetMetWebDriverSession.get();
	} // this help driver register in SetMetWebDriverSession

	public SeMet(WebElement element) {
		this();
		this.element = element;
		elementTag = element.getTagName();
	}
	
	public SeMet(String ... params) {
		this();
		prepare();
		log.debug("\n\n called:: 'SeMet(String ... params)'");
		parseParams(params);
		log.debug("\nparse::" + params);
		
		if (paramDefined("id")) {
			setId(getParam("id"));
		}
	}
	
	@Override
	public void prepare() {
		// TODO Auto-generated method stub

	}

}
