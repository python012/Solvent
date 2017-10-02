package com.semet;

import com.solvent.SolventLogger;
import com.solvent.util.Configurator;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;

/**
 * Created by reed on 2017/10/2.
 */
public class WebDrivers {
    private static final Logger log = SolventLogger.getLogger(WebDrivers.class);
    private static WebDriver driver;

    static {
        log.debug("called WebDrivers static");
        setDriver();
    }

    public static void setDriver() {
        String browserName = Configurator.getBrowserName();
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setBrowserName(browserName);
        driver = setDriver(capabilities);
    }

    private static WebDriver setDriver(DesiredCapabilities capabilities) {
        String browserType = capabilities.getBrowserName();
        if (browserType.equals(BrowserType.FIREFOX)) {
            return setDriverFirefox(capabilities);
        }
        if (browserType.equals(BrowserType.IE)) {
            return setDriverIE(capabilities);
        }

        log.info("\n Browser type not supported! Using IE now");
        return setDriverIE(capabilities);
    }

    public static WebDriver getDriver() {
        return driver;
    }

    public static WebDriver setDriverFirefox(DesiredCapabilities capabilities) {
        log.debug("\n set Firefox driver");
        driver = new FirefoxDriver(capabilities);
        return driver;
    }

    public static WebDriver setDriverIE(DesiredCapabilities capabilities) {
        log.debug("\n set IE driver");
        File file = Configurator.getIEDriver();
        System.setProperty("webdriver.ie.driver", file.getAbsolutePath());
        capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
        driver = new InternetExplorerDriver(capabilities);
        return driver;
    }
}

