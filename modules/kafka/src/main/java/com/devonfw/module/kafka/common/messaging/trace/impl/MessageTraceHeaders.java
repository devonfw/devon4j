package com.devonfw.module.kafka.common.messaging.trace.impl;

/**
 * @author ravicm
 *
 */
public abstract class MessageTraceHeaders {

  public static final String SPAN_ID = "spanId";

  public static final String SAMPLED = "spanSampled";

  public static final String PROCESS_ID = "spanProcessId";

  public static final String PARENT_ID = "spanParentSpanId";

  public static final String TRACE_ID = "spanTraceId";

  public static final String SPAN_NAME = "spanName";

}
