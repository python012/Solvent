package com.semet;

import com.solvent.SolventLogger;
import org.apache.log4j.Logger;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * Created by reed on 2017/10/2.
 */
public class SetMetWebDriverSession {
    private static ThreadLocal<RemoteWebDriver> rwd = new ThreadLocal<RemoteWebDriver>();
    private static Logger log = SolventLogger.getLogger(SetMetWebDriverSession.class.getName());

    public static RemoteWebDriver get() {
        return rwd.get();
    }

    public static void set(RemoteWebDriver instance) {
        rwd.set(instance);
    }
}
