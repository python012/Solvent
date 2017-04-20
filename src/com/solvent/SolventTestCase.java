package com.solvent;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.solvent.timerHelper.Timer;
import com.solvent.l10nHelper.I18NUtil;
import com.solvent.SolventLogger;
import com.solvent.SolventToolkit;

/**
 * Created by reed on 16/10/13.
 */
public abstract class SolventTestCase {

    // private static ArrayList<Timer> timers = null;
    private static ArrayList<CheckPoint> checkPoints = null;
    protected static Logger log;
    private final File directory = new File("/Users/dir_for_failed_screenshots");
    private static ArrayList<SolventStopWatch> timers;

    public SolventTestCase() {
        log = SolventLogger.getLogger(this.getClass());
        I18NUtil.processI18NKeys(this);
        directory.mkdir();
    }

    protected abstract void start();

    public String uniquify(String str) {
        return str + "_" + System.currentTimeMillis();
    }

    public static void waitfor(long milles) {
        try {
            Thread.sleep(milles);
        } catch (Exception e) {
            log.error("\n\n timed out with exception: " + e.getMessage());
        }
    }

    private File filenameFor(Description description) {
        String className = description.getClassName();
        String methodName = description.getMethodName();
        return new File(directory, className + "_" + methodName + ".png");
    }

    private static void checkTimers() {
        StringBuilder sb = new StringBuilder("ID,TIME");
        for (SolventStopWatch timer : timers) {
            sb.append("\n");
            sb.append("\"" + timer.getID() + "\"");
            sb.append(",");
            sb.append("\"" + timer.getTime() + "\"");
        }
        log.info(sb.toString());
    }

    private void checkForFailures() {
        String exceptionMessage = "";
        boolean checkFailureFound = false;
        for (CheckPoint check : checkPoints) {
            if (!check.status()) {
                checkFailureFound = true;
                log.error(check.getStatusMessage());
            }
        }
        if (checkFailureFound) {
            exceptionMessage = exceptionMessage + "Found checkPoint failures! See log for details.";
        }
        if (!exceptionMessage.isEmpty()) {
            throw new SolventException(exceptionMessage);
        }
    }

    public static ArrayList<CheckPoint> getCheckPoints() {
        return checkPoints;
    }

    public CheckPoint newCheckPoint(String id) {
        return newCheckPoint(id, "");
    }

    public CheckPoint newCheckPoint(String id, String description) {
        if (!checkExists(id)) {
            CheckPoint check = new CheckPoint(id, description);
            checkPoints.add(check);
            return check;
        }
        throw new SolventException("Check with id:" + id + " already exists. Specify unique id for your check!");
    }

    private boolean checkExists(String id) {
        for (CheckPoint check : checkPoints) {
            if (check.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    public SolventStopWatch newStopWatch(String id) {
        SolventStopWatch timer = new SolventStopWatch(id);
        timers.add(timer);
        return timer;
    }

    @Rule
    public TestRule watcher = new TestWatcher() {
        @Override
        public void failed(Throwable e, Description description) {
            SolventToolkit.silentlySaveScreenshotTo(filenameFor(description), "png");
        }
    };

    @BeforeClass
    public void setupClass() {
        timers = new ArrayList<SolventStopWatch>();
        startHSQL();
    }

    @Before
    public void setup() {
        checkPoints = new ArrayList<CheckPoint>();
        start();
    }

    @After
    public void tearDown() throws Exception {
        checkForFailures();
        checkTimers();
    }

    @AfterClass
    public static void tearDownClass() {
        checkTimers();
        HsqldbHelper.stopHSQL();
    }

}
