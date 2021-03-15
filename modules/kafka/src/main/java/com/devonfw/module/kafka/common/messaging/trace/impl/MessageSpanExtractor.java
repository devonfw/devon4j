package com.devonfw.module.kafka.common.messaging.trace.impl;

import static brave.internal.codec.HexCodec.lowerHexToUnsignedLong;
import static com.devonfw.module.kafka.common.messaging.util.MessageUtil.getHeaderValue;

import java.util.Optional;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import brave.propagation.TraceContext;
import brave.propagation.TraceContext.Builder;
import brave.propagation.TraceContext.Extractor;
import brave.propagation.TraceContextOrSamplingFlags;

/**
 * This is an implementation class for the {@link Extractor} used to extract the {@link MessageTraceHeaders} from
 * {@link ConsumerRecord}.
 *
 * @param <K> the key type
 * @param <V> the value type
 *
 */
public class MessageSpanExtractor<K, V> implements Extractor<ConsumerRecord<K, V>> {

  @Override
  public TraceContextOrSamplingFlags extract(ConsumerRecord<K, V> consumerRecord) {

    Builder tcBuilder = TraceContext.newBuilder();
    boolean traceIdExists = false;

    if (consumerRecord != null) {

      String traceId = getHeaderValue(consumerRecord.headers(), MessageTraceHeaders.TRACE_ID);
      if (traceId != null && !traceId.isEmpty()) {
        traceIdExists = true;
        tcBuilder.traceId(lowerHexToUnsignedLong(traceId));
        String spanId = getHeaderValue(consumerRecord.headers(), MessageTraceHeaders.SPAN_ID);

        Optional.ofNullable(spanId).ifPresent(id -> tcBuilder.spanId(lowerHexToUnsignedLong(id)));

        String parentId = getHeaderValue(consumerRecord.headers(), MessageTraceHeaders.PARENT_ID);

        Optional.ofNullable(parentId).ifPresent(id -> tcBuilder.parentId(lowerHexToUnsignedLong(id)));
      }
    }

    if (traceIdExists) {
      return TraceContextOrSamplingFlags.create(tcBuilder.build());
    }

    return null;
  }

}
