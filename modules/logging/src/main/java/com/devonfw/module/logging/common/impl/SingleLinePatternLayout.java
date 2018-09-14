package com.devonfw.module.logging.common.impl;

import java.util.regex.Pattern;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.core.CoreConstants;

/**
 * Custom {@link PatternLayout} for logging entries.
 * 
 */
public class SingleLinePatternLayout extends PatternLayout {

  // --------------- FIELDS ---------------

  /** The separator used as replacement for newlines. */
  private static final String LINE_SEP = " | ";

  /** OS specific line separator. */
  private static final String NEWLINE = CoreConstants.LINE_SEPARATOR;

  /** Regular expression for line breaks. */
  private static final Pattern LINEBREAK_PATTERN = Pattern.compile("[\\r\\n|\\n]");

  /** Average buffer size per line of stack trace. */
  private static final int BUFFER_PER_LINE = 50;

  // --------------- CONSTRUCTORS ---------------

  /**
   * Default constructor.
   */
  public SingleLinePatternLayout() {

    super();
  }

  // --------------- METHODS ---------------

  /**
   * Creates formatted String, using conversion pattern.
   * 
   * @param event ILoggingEvent
   * @return String Formatted event as string in one line
   */
  @Override
  public String doLayout(ILoggingEvent event) {

    // Format message
    String msg = super.doLayout(event).trim();
    // prevent log forging
    msg = preventLogForging(msg);

    // Formatting of exception, remove line breaks
    IThrowableProxy throwableProxy = event.getThrowableProxy();
    if (throwableProxy != null) {
      StackTraceElementProxy[] s = throwableProxy.getStackTraceElementProxyArray();
      if (s != null && s.length > 0) {
        // Performance: Initialize StringBuilder with (number of StackTrace-Elements + 1)*50 (+1 for Message)
        StringBuilder sb = new StringBuilder(s.length * BUFFER_PER_LINE);
        sb.append(msg);

        int len = s.length;
        for (int i = 0; i < len; i++) {
          sb.append(LINE_SEP).append(s[i]);
        }
        msg = sb.toString();
      }
    }
    return msg + NEWLINE;
  }

  /**
   * Method to prevent log forging.
   * 
   * @param logMsg
   * @return Encoded message
   */
  private String preventLogForging(String logMsg) {

    String result = logMsg;
    // use precompiled pattern for performance reasons
    result = LINEBREAK_PATTERN.matcher(logMsg).replaceAll(SingleLinePatternLayout.LINE_SEP);
    return result;
  }

}
