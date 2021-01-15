package com.devonfw.module.kafka.common.messaging.logging.impl;

import static brave.internal.codec.HexCodec.toLowerHex;

import java.lang.reflect.Method;
import java.time.Instant;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.record.TimestampType;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.util.ObjectUtils;

import com.devonfw.module.kafka.common.messaging.trace.impl.MessageSpanExtractor;
import com.devonfw.module.kafka.common.messaging.trace.impl.MessageTraceSupport;

import brave.Span;
import brave.Tracer;

/**
 * This aspect class is used to listen the {@link KafkaListener} and to log the {@link ConsumerRecord} received.
 *
 * @param <K> the key type
 * @param <V> the value type
 *
 */
@Aspect
@Order(0)
public class MessageListenerLoggingAspect<K, V> {

  private static final Logger LOG = LoggerFactory.getLogger(MessageListenerLoggingAspect.class);

  @Inject
  private MessageLoggingSupport loggingSupport;

  @Inject
  private ConsumerGroupResolver consumerGroupResolver;

  /**
   * The {@link Tracer}.
   */
  protected Tracer tracer;

  /**
   * The {@link MessageSpanExtractor}.
   */
  protected MessageSpanExtractor<K, V> spanExtractor;

  /**
   * Set the {@link Tracer}.
   *
   * @param tracer the {@link Tracer}.
   */
  public void setTracer(Tracer tracer) {

    this.tracer = tracer;
  }

  /**
   * Set the {@link MessageSpanExtractor}.
   *
   * @param spanExtractor .
   */
  public void setSpanExtractor(MessageSpanExtractor<K, V> spanExtractor) {

    this.spanExtractor = spanExtractor;
  }

  /**
   * This method is used to log the processing message.Checks for the method annotated {@link KafkaListener} and logs
   * the message processing with the received {@link ConsumerRecord}
   *
   * @param call {@link ProceedingJoinPoint}
   * @param kafkaRecord {@link ConsumerRecord}
   * @return object the {@link Object}
   * @throws Throwable the {@link Throwable}
   */
  @Around("@annotation(org.springframework.kafka.annotation.KafkaListener) && args(kafkaRecord,..)")
  public Object logMessageProcessing(ProceedingJoinPoint call, ConsumerRecord<K, V> kafkaRecord) throws Throwable {

    openSpan(kafkaRecord);

    String consumerGroup = identifyConsumerGroup(call);
    this.loggingSupport.logMessageReceived(LOG, consumerGroup, kafkaRecord.topic(), kafkaRecord.partition(),
        kafkaRecord.offset(), determineLengthOfStayInTopic(kafkaRecord));

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
        this.loggingSupport.logMessageProcessed(LOG, consumerGroup, kafkaRecord.topic(), kafkaRecord.partition(),
            kafkaRecord.offset(), duration);
      } else {
        this.loggingSupport.logMessageNotProcessed(LOG, consumerGroup, kafkaRecord.topic(), kafkaRecord.partition(),
            kafkaRecord.offset());
      }

      MessageTraceSupport.finishSpan();
    }
  }

  private void openSpan(ConsumerRecord<K, V> kafkaRecord) {

    if (ObjectUtils.isEmpty(this.tracer)) {
      return;
    }

    MessageTraceSupport.startSpan(kafkaRecord, this.spanExtractor);

    Span span = this.tracer.currentSpan();

    LOG.debug("Generated new span before processing the example message: Trace-Id={}, Span-Id={}, Parent-Span-Id={}",
        span.context().traceIdString(), toLowerHex(span.context().spanId()),
        (span.context().parentId() != null ? toLowerHex(span.context().parentId()) : "null"));
  }

  private long determineLengthOfStayInTopic(ConsumerRecord<K, V> kafkaRecord) {

    if (kafkaRecord.timestampType() != TimestampType.NO_TIMESTAMP_TYPE) {
      return Instant.now().toEpochMilli() - kafkaRecord.timestamp();
    }
    return 0;
  }

  private String identifyConsumerGroup(ProceedingJoinPoint call) {

    MethodSignature signature = (MethodSignature) call.getSignature();
    Method method = signature.getMethod();
    KafkaListener annotation = AnnotationUtils.findAnnotation(method, KafkaListener.class);

    if (ObjectUtils.isEmpty(annotation)) {
      return this.consumerGroupResolver.getConsumerGroup("");
    }
    return this.consumerGroupResolver.getConsumerGroup(annotation.containerFactory());
  }

  /**
   * This method is annotated with {@link PostConstruct} to validate the properties {@link Tracer} and
   * {@link MessageSpanExtractor}.
   */
  @PostConstruct
  public void checkTraceConfiguration() {

    if (this.tracer != null) {

      if (ObjectUtils.isEmpty(this.spanExtractor)) {
        throw new IllegalStateException("Span extractor must not be null."
            + "If there is a sleuth tracer, then there must also be a span extractor.");
      }
    }
  }
}
