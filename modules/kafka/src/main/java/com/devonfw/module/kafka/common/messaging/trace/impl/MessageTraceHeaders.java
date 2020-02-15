package com.devonfw.module.kafka.common.messaging.trace.impl;

/**
 * @author ravicm
 *
 */
public abstract class MessageTraceHeaders {

  public static final String SPAN_ID_NAME = "spanId";

  public static final String SAMPLED_NAME = "spanSampled";

  public static final String PROCESS_ID_NAME = "spanProcessId";

  public static final String PARENT_ID_NAME = "spanParentSpanId";

  public static final String TRACE_ID_NAME = "spanTraceId";

  public static final String SPAN_NAME_NAME = "spanName";

}
