package io.github.orczykowski.logback.obfuscator;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;

public class ILoggingEventFactory {
    private static final LoggerContext loggerContext = new LoggerContext();
    private static final Logger logger = loggerContext.getLogger(ILoggingEventFactory.class);

    static ILoggingEvent from(final String msg) {
        return new LoggingEvent("FQCN", logger, Level.INFO, msg, null, null);
    }
}
