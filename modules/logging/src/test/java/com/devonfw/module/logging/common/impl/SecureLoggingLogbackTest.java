package com.devonfw.module.logging.common.impl;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.owasp.security.logging.filter.ExcludeClassifiedMarkerFilter;
import org.owasp.security.logging.mask.MaskingConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

import com.devonfw.module.test.common.base.BaseTest;
import com.devonfw.module.test.common.base.ModuleTest;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.spi.FilterReply;

/**
 * Test class for {@link SecureLogging}, when used with logback. <br>
 * Tests Marker initialization, logging of events with and without Markers, masking and filtering.
 * <P>
 * Main functionality is adapted from test classes of OWASP: <br>
 * owasp-security-logging-logback/src/test/java/org/owasp/security/logging/mask/MaskingConverterTest and
 * ../filter/ExcludeClassifiedMarkerFilterTest
 */
@ExtendWith(MockitoExtension.class)
public class SecureLoggingLogbackTest extends ModuleTest {

  private static final LoggerContext LOGGER_CONTEXT = (LoggerContext) LoggerFactory.getILoggerFactory();

  private static final Logger LOG = LoggerFactory.getLogger(SecureLoggingLogbackTest.class);

  private PatternLayoutEncoder encoder;

  private ExcludeClassifiedMarkerFilter filterExclClassif;

  @Mock
  private RollingFileAppender<ILoggingEvent> mockAppender = new RollingFileAppender<>();

  // Captor is genericised with ch.qos.logback.classic.spi.LoggingEvent
  @Captor
  private ArgumentCaptor<LoggingEvent> captorLoggingEvent;

  /**
   * {@inheritDoc}
   * <p>
   * Called by {@code final} method {@link BaseTest#setUp()}.
   */
  @Override
  protected void doSetUp() {

    super.doSetUp();

    // This converter masks all arguments of a confidential message with ***.
    // It overwrites the message field %m, so the log pattern can stay unchanged.
    PatternLayout.defaultConverterMap.put("m", MaskingConverter.class.getName());

    this.encoder = new PatternLayoutEncoder();
    this.encoder.setContext(LOGGER_CONTEXT);
    this.encoder.setPattern("[%marker] %-4relative [%thread] %-5level %logger{35} - %m%n");
    this.encoder.start();

    this.filterExclClassif = new ExcludeClassifiedMarkerFilter();
    this.filterExclClassif.setContext(LOGGER_CONTEXT);
    this.filterExclClassif.start();
    assertThat(this.filterExclClassif.isStarted()).isTrue();

    this.mockAppender.setContext(LOGGER_CONTEXT);
    this.mockAppender.setEncoder(this.encoder);
    this.mockAppender.start();

    ((ch.qos.logback.classic.Logger) LOG).addAppender(this.mockAppender);
  }

  /**
   * {@inheritDoc}
   * <p>
   * Called by {@code final} method {@link BaseTest#tearDown()}.
   */
  @Override
  protected void doTearDown() {

    super.doTearDown();

    ((ch.qos.logback.classic.Logger) LOG).detachAppender(this.mockAppender);
  }

  private LoggingEvent getLastLogEvent() {

    // Verify our logging interactions
    verify(this.mockAppender).doAppend(this.captorLoggingEvent.capture());
    // Get the logging event from the captor
    return this.captorLoggingEvent.getValue();
  }

  /**
   * Test if logging works at all, without using any Markers.
   */
  @Test
  public void testDefaultLogEvent() {

    // given
    String logmsg = "simple log message";

    // when
    LOG.info(logmsg);

    // then
    // Retrieve log event
    final LoggingEvent loggingEvent = getLastLogEvent();
    // Check log level is correct
    assertThat(loggingEvent.getLevel()).isEqualTo(Level.INFO);

    // Check the message being logged is reasonable
    String layoutMessage = this.encoder.getLayout().doLayout(loggingEvent);
    assertThat(layoutMessage.isEmpty()).as("formatted log message is empty.").isFalse();
    assertThat(layoutMessage.contains(logmsg)).as("formatted log message contains original message.").isTrue();
  }

  /**
   * Test the output of a log event with a marker.
   */
  @Test
  public void testLogEventWithMarker() {

    // given
    Marker marker = SecureLogging.SECURITY_SUCCESS;
    String logmsg = "security log message";

    // when
    LOG.info(marker, logmsg);

    // then
    // Retrieve log event
    final LoggingEvent loggingEvent = getLastLogEvent();
    // Check log level is correct
    assertThat(loggingEvent.getLevel()).isEqualTo(Level.INFO);

    // Check the message being logged is reasonable
    String layoutMessage = this.encoder.getLayout().doLayout(loggingEvent);
    assertThat(layoutMessage.contains(logmsg)).as("formatted log message contains original message.").isTrue();
    assertThat(layoutMessage.contains(marker.getName())).as("log message contains name of marker.").isTrue();
  }

  /**
   * Test the output of a log event with a classification Marker and an argument that shall be masked. Note: the console
   * will show the 'password' content when running this test.
   */
  @Test
  public void testLogEventWithMasking() {

    // given
    Marker marker = SecureLogging.CONFIDENTIAL;
    String password = "classified!";

    // when
    LOG.info(marker, "confidential message with password = '{}'", password);

    // then
    // Retrieve log event
    final LoggingEvent loggingEvent = getLastLogEvent();
    // Check log level is correct
    assertThat(loggingEvent.getLevel()).isEqualTo(Level.INFO);

    // Check the message being logged is reasonable
    String layoutMessage = this.encoder.getLayout().doLayout(loggingEvent);
    assertThat(layoutMessage.contains(password)).as("formatted log message contains classified information.").isFalse();
    assertThat(layoutMessage.contains(marker.getName())).as("log message contains name of marker.").isTrue();
  }

  /**
   * Test the ExcludeClassifiedMarkerFilter on an event with MultiMarker (other tests are done within OWASP).
   */
  @Test
  public void testExclClassifMarkerFilter() {

    // given
    Marker marker = SecureLogging.SECURITY_SUCCESS_CONFIDENTIAL;

    // when
    LOG.info(marker, "confidential security message with MultiMarker.");

    // then
    // Retrieve log event
    final LoggingEvent loggingEvent = getLastLogEvent();
    // Check log level is correct
    assertThat(loggingEvent.getLevel()).isEqualTo(Level.INFO);

    // Check the stand-alone filter decision for this event
    assertThat(this.filterExclClassif.decide(loggingEvent)).isEqualTo(FilterReply.DENY);

    // Check the filter chain decision for this event
    // (does not work) assertThat(this.mockAppender.getFilterChainDecision(loggingEvent)).isEqualTo(FilterReply.DENY);
  }

  /**
   * Test if a combined Marker contains the names of its constituent Markers. This test is useful in particular if the
   * dependency org.owasp is not available, but also works when it is present. To test the fall back solution in
   * {@link SecureLogging}, one has to create a separate logging module strictly without the OWASP dependency.
   */
  @Test
  public void testInitMarkersByName() {

    // given & when
    // setup: SecureLogging.initMarkers() is called by the loc below.
    Marker multiMarker = SecureLogging.SECURITY_SUCCESS_CONFIDENTIAL;
    Marker securMarker = SecureLogging.SECURITY_SUCCESS;
    Marker confidMarker = SecureLogging.CONFIDENTIAL;

    // then
    // verify that the combined Marker or MultiMarker contains the names of its constituent Markers.
    assertThat(multiMarker.getName().isEmpty()).isFalse();
    assertThat(multiMarker.getName().contains(securMarker.getName())).isTrue();
    assertThat(multiMarker.getName().contains(confidMarker.getName())).isTrue();
  }

  /**
   * Test Marker creation if the dependency org.owasp is available, which provides the class
   * {@link org.owasp.security.logging.MultiMarker}.
   */
  @Test
  public void testInitWithMultiMarkerClass() {

    // skip test if the dependency is not available.
    if (!SecureLogging.hasExtClass()) {
      return;
    }
    assertThat(SecureLogging.hasExtClass()).as("dependency org.owasp is available.").isTrue();

    // given & when
    // SecureLogging.initMarkers() is called by the loc below:
    Marker multiMarker = SecureLogging.SECURITY_SUCCESS_CONFIDENTIAL;
    Marker securMarker = SecureLogging.SECURITY_SUCCESS;
    Marker confidMarker = SecureLogging.CONFIDENTIAL;

    // then
    // verify that the MultiMarker contains both simple Markers.
    assertThat(multiMarker.hasReferences()).as("MultiMarker has references.").isTrue();
    assertThat(multiMarker.contains(securMarker)).as("MultiMarker contains Security Marker.").isTrue();
    assertThat(multiMarker.contains(confidMarker)).as("MultiMarker contains Confidential Marker.").isTrue();
  }

}
