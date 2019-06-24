package com.katalon.kata.helper;

import ch.qos.logback.classic.LoggerContext;
import com.github.sbabcoc.logback.testng.ReporterAppender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogHelper {

    static {
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        ReporterAppender reporterAppender = new ReporterAppender();
        reporterAppender.setContext(lc);
        reporterAppender.start();
    }

    public static Logger getLogger() {


        final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
    /*
     * stackTrace[0] is for Thread.currentThread().getStackTrace() stackTrace[1] is for this method log()
     */
        String className = stackTrace[2].getClassName();
        return LoggerFactory.getLogger(className);
    }
}
