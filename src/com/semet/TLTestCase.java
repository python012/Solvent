package com.semet;

import com.solvent.SolventLogger;
import com.solvent.SolventTestCase;
import org.apache.log4j.Logger;
import com.solvent.util.Configurator;
import org.junit.After;
import org.openqa.selenium.remote.RemoteWebDriver;


/**
 * Created by reed on 2017/10/2.
 */
public class TLTestCase extends SolventTestCase {

    protected static Logger log;
    private static RemoteWebDriver rwd = null;

    public TLTestCase() {
        log = SolventLogger.getLogger(this.getClass());
    }

    public void start() {
        if (null == SetMetWebDriverSession.get()) {
            rwd = (RemoteWebDriver) WebDrivers.getDriver();
        }
        start(rwd);
        waitfor(3000);
    }

    public void start(RemoteWebDriver wd) {
        String appURL = Configurator.getAppURL();
        wd.get(appURL);
        log.info("\n\n settings Selenium with URL:: " + appURL);
        SetMetWebDriverSession.set(wd);
    }

    @After
    public void cleanUP() {
        SetMetWebDriverSession.get().quit();
    }

}
