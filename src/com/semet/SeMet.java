package com.semet;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.solvent.Solvent;
import com.solvent.SolventLogger;

import java.util.List;

public abstract class SeMet extends Solvent {

    private static final Logger log = SolventLogger.getLogger(SeMet.class);
    private String id = null;
    private String name = null;
    private String xPath = null;
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
        this((RemoteWebDriver) null);
        log.debug("\n\n called:: 'SeMet()'");
    }


    protected SeMet(RemoteWebDriver driver) {
        log.debug("\n\n called:: 'SeMet(RemoteWebDriver driver)'");
        if (null != driver) {
            SetMetWebDriverSession.set(driver);
        }
        driver = SetMetWebDriverSession.get();
    } // this help driver register in SetMetWebDriverSession

    public SeMet(WebElement element) {
        this();
        this.element = element;
        elementTag = element.getTagName();
    }

    public SeMet(String... params) {
        this();
        prepare();
        log.debug("\n\n called:: 'SeMet(String ... params)'");
        parseParams(params);
        log.debug("\nparse::" + params);

        if (paramDefined("id")) {
            setId(getParam("id"));
        }

        if (paramDefined("name")) {
            setId(getParam("name"));
        }

        if (paramDefined("xpath")) {
            setId(getParam("xpath"));
        }

        if (getId() != null && getId().length() > 0) {
            rootXPath = "//" + elementTag + "[@id=']" + getId() + "']";
            return;
        }

        if (this.name != null && this.name.length() > 0) {
            rootXPath = "//" + elementTag + "[@name'" + this.name + "']";
            return;
        }

        if (this.xPath != null && this.xPath.length() > 0) {
            rootXPath = this.xPath;
            return;
        }
        rootXPath = null;
        return;
    }

    public String getRootXpath() {
        return rootXPath;
    }

    protected WebElement getElementUsingXpath(String xpathLocator) {
        return getElement(By.xpath(xpathLocator));
    }

    protected List<WebElement> getElementsUsingXpath(String xpathLocator) {
        return getElements(By.xpath(xpathLocator));
    }

    public WebElement getElement(By by) {
        try {
            // element = (WebElement) driver.findElement(by);
            element = driver.findElement(by);
        } catch (NoSuchElementException e) {
            log.error("Fail to find element:" + by.toString());
            element = null;
        }
        return element;
    }

    public List<WebElement> getElements(By by) {
        List<WebElement> elements;
        try {
            elements = driver.findElements(by);
        } catch (NoSuchElementException e) {
            log.error("Fail to find elements:" + by.toString());
            elements = null;
        }
        return elements;
    }

    public WebElement getElement(String param) {
        try {
            return getElement(getLocator(param));
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("getElement returns a NULL");
            return null;
        }
    }

    public List<WebElement> getElements(String param) {
        try {
            return getElements(getLocator(param));
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("getElements returns a NULL");
            return null;
        }
    }

    public WebElement getElement() {
        return element;
    } // return the root element

    public static By getLocator(String param) throws Exception {
        String locator = param;
        String locatorType = locator.substring(0, locator.indexOf('=')).trim();
        String locatorValue = locator.substring(locator.indexOf('=') + 1).trim();
        if (locatorType.toLowerCase().equals("id")) {
            return By.id(locatorValue);
        } else if (locatorType.toLowerCase().equals("name")) {
            return By.name(locatorValue);
        } else if (locatorType.toLowerCase().equals("xpath")) {
            return By.xpath(locatorValue);
        } else if ((locatorType.toLowerCase().equals("classname"))
                || (locatorType.toLowerCase().equals("class"))) {
            return By.className(locatorValue);
        } else if ((locatorType.toLowerCase().equals("tagname"))
                || (locatorType.toLowerCase().equals("tag"))) {
            return By.tagName(locatorValue);
        } else if ((locatorType.toLowerCase().equals("linktext"))
                || (locatorType.toLowerCase().equals("link"))) {
            return By.linkText(locatorValue);
        } else if (locatorType.toLowerCase().equals("partiallinktext")) {
            return By.partialLinkText(locatorValue);
        } else if ((locatorType.toLowerCase().equals("cssselector"))
                || (locatorType.toLowerCase().equals("css"))) {
            return By.cssSelector(locatorValue);
        } else {
            throw new Exception("Locator type '" + locatorType + "' not defined!");
        }
    }

    public abstract void prepare();

    public void scrollTo(String locator) throws Exception {
        scrollTo(getElement(locator));
    }

    public static void scrollTo(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public void setId(String value) {
        this.id = value;
    }

    public String getId() {
        return this.id;
    }

    public boolean isElementPresent(String locator, int wait) {
        boolean present = (getElementUsingXpath(locator) != null) ? true:false;
        while (!present && wait > 0) {
            waitfor(500);
            wait -= 500;
            present = (getElementUsingXpath(locator) != null) ? true:false;
        }
        if (!present) {
            log.debug("Element not present, locator: " + locator);
        }
        return present;
    }

    public boolean isElementPresentAndVisible(String locator, int wait) {
        boolean present = (getElementUsingXpath(locator) != null) ? true:false;
        while (!present && wait > 0) {
            waitfor(500);
            wait -= 500;
            present = (getElementUsingXpath(locator) != null) ? true:false;
            if (getElementsUsingXpath(locator) !=null) {
                present = getElementUsingXpath(locator).isDisplayed();
            }
        }
        if (!present) {
            log.debug("Element not present, locator: " + locator);
        }
        return present;
    }
}
