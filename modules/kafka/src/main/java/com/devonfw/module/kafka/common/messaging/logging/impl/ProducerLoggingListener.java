package com.devonfw.module.kafka.common.messaging.logging.impl;

import static brave.internal.HexCodec.toLowerHex;

import java.util.Optional;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.util.ObjectUtils;

import brave.Tracer;
import brave.propagation.TraceContext;

/**
 * @author ravicm
 *
 */
public class ProducerLoggingListener implements ProducerListener<String, String> {

  private static final Logger LOG = LoggerFactory.getLogger(ProducerLoggingListener.class);

  private Tracer tracer;

  private MessageLoggingSupport loggingSupport;

  /**
   * The constructor.
   *
   * @param loggingSupport
   * @param tracer
   */
  public ProducerLoggingListener(MessageLoggingSupport loggingSupport, Tracer tracer) {

    this.loggingSupport = loggingSupport;
    this.tracer = tracer;
  }

  @Override
  public void onSuccess(String topic, Integer partition, String key, String value, RecordMetadata recordMetadata) {

    setMdc();

    if (recordMetadata != null) {
      this.loggingSupport.logMessageSent(LOG, key, recordMetadata.topic(), recordMetadata.partition(),
          recordMetadata.offset());

    } else {
      this.loggingSupport.logMessageSent(LOG, key, topic, partition, null);
    }
  }

  @Override
  public void onError(String topic, Integer partition, String key, String value, Exception exception) {

    setMdc();

    this.loggingSupport.logMessageNotSent(LOG, key, topic, partition,
        (exception != null ? exception.getLocalizedMessage() : "unbekannt"));
  }

  /**
   *
   */
  protected void setMdc() {

    if (ObjectUtils.isEmpty(this.tracer) || ObjectUtils.isEmpty(this.tracer.currentSpan())) {
      return;
    }

    TraceContext traceContext = this.tracer.currentSpan().context();

    MDC.remove("traceId");
    MDC.remove("spanId");
    MDC.remove("parentId");

    MDC.put("traceId", traceContext.traceIdString());
    MDC.put("spanId", toLowerHex(traceContext.spanId()));
    Long parentId = traceContext.parentId();

    Optional.ofNullable(parentId).ifPresent(id -> MDC.put("parentId", toLowerHex(id)));
  }
}
