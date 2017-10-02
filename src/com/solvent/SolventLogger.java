package com.solvent;

import org.apache.log4j.Appender;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import com.solvent.util.Configurator;

import java.io.File;
import java.util.UUID;

/**
 * Created by reed on 16/10/13.
 */
public class SolventLogger {

    public static final String CONSOLE_APPENDER_NAME = UUID.randomUUID().toString();
    public static final String FILE_APPENDER_NAME = UUID.randomUUID().toString();
    private static Logger root = LogManager.getRootLogger();
    private static boolean configureExternally = root.getAllAppenders().hasMoreElements();
    private static String defaultPattern = Configurator.getSolventOutputPattern();
    private static String defaultVerbosity = Configurator.getSolventOutputVerbosity();

    static {
        initializeLogging(defaultPattern, defaultVerbosity, null);
    }

    public static void initializeLogging(String pattern, String verbosity, File logFile) {
        if (!configureExternally) {
            root.removeAppender(CONSOLE_APPENDER_NAME);

            Appender conApp = new ConsoleAppender((new PatternLayout(pattern)));
            conApp.setName(CONSOLE_APPENDER_NAME);

            root.addAppender(conApp);
            root.setLevel(Level.toLevel(verbosity, Level.ALL));

            if (logFile != null) {
                try {
                    Appender fileApp = new FileAppender(new PatternLayout(pattern), logFile.getPath());
                    fileApp.setName(FILE_APPENDER_NAME);
                    root.addAppender(fileApp);
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        }
    }

    public static void disableFileLogging() {
        root.removeAppender(FILE_APPENDER_NAME);
    }

    public static Logger getLogger(Class<?> clazz) {
        return getLogger(clazz.getName());
    }

    public static Logger getLogger(String name) {
        return LogManager.getLogger("solvent." + name);
    }
}
