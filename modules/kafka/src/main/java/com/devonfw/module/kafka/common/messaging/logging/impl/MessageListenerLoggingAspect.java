package com.devonfw.module.kafka.common.messaging.logging.impl;

import static brave.internal.HexCodec.toLowerHex;

import java.lang.reflect.Method;
import java.time.Instant;

import javax.annotation.PostConstruct;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.record.TimestampType;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import com.devonfw.module.kafka.common.messaging.trace.impl.MessageSpanExtractor;
import com.devonfw.module.kafka.common.messaging.trace.impl.MessageTraceSupport;

import brave.Span;
import brave.Tracer;

/**
 * @author ravicm
 *
 */
@Aspect
@Order(0)
public class MessageListenerLoggingAspect {

  private static final Logger LOG = LoggerFactory.getLogger(MessageListenerLoggingAspect.class);

  @Autowired
  private MessageLoggingSupport loggingSupport;

  @Autowired
  private ConsumerGroupResolver consumerGroupResolver;

  /**
   *
   */
  @Autowired(required = false)
  protected Tracer tracer;

  /**
   *
   */
  @Autowired(required = false)
  protected MessageSpanExtractor spanExtractor;

  /**
   * @param call
   * @param kafkaRecord
   * @return
   * @throws Throwable
   */
  @Around("@annotation(org.springframework.kafka.annotation.KafkaListener) && args(kafkaRecord,..)")
  public Object logMessageProcessing(ProceedingJoinPoint call, ConsumerRecord<String, String> kafkaRecord)
      throws Throwable {

    openSpan(kafkaRecord);

    String consumerGroup = identifyConsumerGroup(call);
    this.loggingSupport.logMessageReceived(LOG, kafkaRecord.key(), consumerGroup, kafkaRecord.topic(),
        kafkaRecord.partition(), kafkaRecord.offset(), determineRetentionTimeOfQueue(kafkaRecord));

    boolean isCallSucessFull = false;
    long startTime = Instant.now().toEpochMilli();
    try {
      Object result = call.proceed();

      isCallSucessFull = true;
      return result;
    } finally {

      long endTime = Instant.now().toEpochMilli();
      long duration = endTime - startTime;

      if (isCallSucessFull) {
        this.loggingSupport.logMessageProcessed(LOG, kafkaRecord.key(), consumerGroup, kafkaRecord.topic(),
            kafkaRecord.partition(), kafkaRecord.offset(), duration);
      } else {
        this.loggingSupport.logMessageNotProcessed(LOG, kafkaRecord.key(), consumerGroup, kafkaRecord.topic(),
            kafkaRecord.partition(), kafkaRecord.offset());
      }

      MessageTraceSupport.finishSpan();
    }
  }

  /**
   * @param kafkaRecord
   */
  protected void openSpan(ConsumerRecord<String, String> kafkaRecord) {

    if (ObjectUtils.isEmpty(this.tracer)) {
      return;
    }

    // MessageVersion version = MessageUtil.getMessageVersion(kafkaRecord);
    // if (MessageVersion.V1 != version) {
    // return;
    // }

    MessageTraceSupport.startSpan(kafkaRecord, this.spanExtractor);

    Span span = this.tracer.currentSpan();

    LOG.debug("Generated new span before processing the example message: Trace-Id={}, Span-Id={}, Parent-Span-Id={}",
        span.context().traceIdString(), toLowerHex(span.context().spanId()),
        (span.context().parentId() != null ? toLowerHex(span.context().parentId()) : "null"));
  }

  /**
   * @param kafkaRecord
   * @return
   */
  protected long determineRetentionTimeOfQueue(ConsumerRecord<String, String> kafkaRecord) {

    if (kafkaRecord.timestampType() != TimestampType.NO_TIMESTAMP_TYPE) {
      return Instant.now().toEpochMilli() - kafkaRecord.timestamp();
    }
    return 0;
  }

  /**
   * @param call
   * @return
   */
  protected String identifyConsumerGroup(ProceedingJoinPoint call) {

    MethodSignature signature = (MethodSignature) call.getSignature();
    Method method = signature.getMethod();
    KafkaListener annotation = AnnotationUtils.findAnnotation(method, KafkaListener.class);

    if (ObjectUtils.isEmpty(annotation)) {
      return this.consumerGroupResolver.getConsumerGroup("");
    }
    return this.consumerGroupResolver.getConsumerGroup(annotation.containerFactory());
  }

  /**
   *
   */
  @PostConstruct
  public void checkTraceConfiguration() {

    if (this.tracer != null) {
      Assert.notNull(this.spanExtractor, "Span extractor must not be null."
          + "If there is a sleuth tracer, then there must also be a span extractor.");
    }
  }
}
