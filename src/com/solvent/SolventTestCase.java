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

/**
 * Created by reed on 16/10/13.
 */
public abstract class SolventTestCase {

    private static ArrayList<Timer> timers = null;
    private static ArrayList<CheckPoint> checkPoints = null;
    protected static Logger log;
    private final File directory = new File("/Users/reed/Documents/tmp1");

    public SolventTestCase() {
        log = SolventLogger.getLogger(this.getClass());
        I18NUtil.processI18NKeys(this);
        directory.mkdir();
    }

    private File filenameFor(Description description) {
        String className = description.getClassName(); // get the test class
                                                       // name
        String methodName = description.getMethodName();

        return new File(directory, className + "_" + methodName + ".png");
    }

    private void silentlySaveScreenshotTo(File file, String format) {
        try {
            saveScreenshotTo(file, format);
        } catch (Exception e) {
            System.err.println("Failed to screenshot to " + file + ", " + e);
        }
    }

    private static BufferedImage BufferedImagetakeScreenshot() throws AWTException {
        Robot robot = new Robot();
        Rectangle captureSize = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        return robot.createScreenCapture(captureSize);
    }

    private static void saveScreenshotTo(File file, String format) throws AWTException, IOException {
        ImageIO.write(takeScreenshotTo(), format, file);
    }

    @Rule
    public TestRule watcher = new TestWatcher() {
        @Override
        public void failed(Throwable e, Description description) {
            silentlySaveScreenshotTo(filenameFor(description), "png");
        }
    };

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
