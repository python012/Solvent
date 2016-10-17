package com.solvent;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import java.util.ArrayList;

import com.solvent.timerHelper.Timer;
import com.solvent.l10nHelper.I18NUtil;
import com.solvent.SolventLogger;

/**
 * Created by reed on 16/10/13.
 */
public abstract class SolventTestCase {

    private static ArrayList<Timer> timers = null;
    private static ArrayList<CheckPoint> checkPoints = null;
    protected static Logger log;

    public SolventTestCase() {
        log = SolventLogger.getLogger(this.getClass());
        I18NUtil.processI18NKeys(this);
    }

    @BeforeClass
    public void setupClass() {
        startHSQL();
    }

    @Before
    public void setup() {
        checkPoints = new ArrayList<CheckPoint>();
        timers = new ArrayList<Timer>();
        start();
    }

    @After
    public void tearDown() throws Exception {
        checkForFailures();
        checkTimers();
    }

    @AfterClass
    public static void tearDownClass() {
        HsqldbHelper.stopHSQL();
    }

    protected abstract void start();

    public String uniquify(String str) {
        return str + "_" + System.currentTimeMillis();
    }

    public static void waitfor(long milles) {
        try {
            Thread.sleep(milles);
        } catch (Exception e) {
            log.info("\n\n timed out with exception: " + e.getMessage());
        }
    }
}
