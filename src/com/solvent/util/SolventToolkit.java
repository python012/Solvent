package com.solvent.util;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SolventToolkit {

    public static void silentlySaveScreenshotTo(File file, String format) {
        try {
            saveScreenshotTo(file, format);
        } catch (Exception e) {
            System.err.println("Failed to screenshot to " + file + ", " + e);
        }
    }

    private static BufferedImage takeScreenshot() throws AWTException {
        Robot robot = new Robot();
        Rectangle captureSize = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        return robot.createScreenCapture(captureSize);
    }

    private static void saveScreenshotTo(File file, String format) throws AWTException, IOException {
        ImageIO.write(takeScreenshot(), format, file);
    }
}
