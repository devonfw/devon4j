package com.devonfw.module.kafka.common.messaging.trace.impl;

import brave.Tracer;

/**
 * A custom Header class for the {@link Tracer}
 *
 */
public abstract class MessageTraceHeaders {

  /**
   * SPAN ID
   */
  public static final String SPAN_ID = "spanId";

  /**
   * SAMPLED
   */
  public static final String SAMPLED = "spanSampled";

  /**
   * PROCESS_ID
   */
  public static final String PROCESS_ID = "spanProcessId";

  /**
   * PARENT_ID
   */
  public static final String PARENT_ID = "spanParentSpanId";

  /**
   * TRACEID
   */
  public static final String TRACE_ID = "traceId";

  /**
   * SPANNAME
   */
  public static final String SPAN_NAME = "spanName";

}
