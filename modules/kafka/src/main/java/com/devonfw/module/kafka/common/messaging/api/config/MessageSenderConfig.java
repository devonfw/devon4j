package com.devonfw.module.kafka.common.messaging.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import com.devonfw.module.kafka.common.messaging.api.client.MessageSender;
import com.devonfw.module.kafka.common.messaging.impl.MessageSenderImpl;
import com.devonfw.module.kafka.common.messaging.logging.impl.MessageLoggingSupport;
import com.devonfw.module.kafka.common.messaging.logging.impl.ProducerLoggingListener;
import com.devonfw.module.kafka.common.messaging.trace.impl.MessageSpanInjector;
import com.devonfw.module.kafka.common.messaging.util.KafkaPropertyMapper;
import com.fasterxml.jackson.databind.ObjectMapper;

import brave.Tracer;

/**
 * A configuration class for the {@link MessageSender}
 *
 */
@Configuration
@Import(MessageCommonConfig.class)
public class MessageSenderConfig {

  /**
   * Creates bean for {@link KafkaProducerProperties} and looks for external properties with the prefix
   * <code>messaging.kafka.producer</code>.
   *
   * @return the KafkaProducerProperties
   */
  @Bean
  @ConfigurationProperties(prefix = "messaging.kafka.producer")
  public KafkaProducerProperties messageKafkaProducerProperties() {

    return new KafkaProducerProperties();
  }

  /**
   * Creates bean for {@link MessageSenderProperties} and looks for external properties with the prefix
   * <code>messaging.sender</code>.
   *
   * @return the MessageSenderProperties
   */
  @Bean
  @ConfigurationProperties(prefix = "messaging.sender")
  public MessageSenderProperties messageSenderProperties() {

    return new MessageSenderProperties();
  }

  /**
   * Creates bean for {@link ProducerFactory}.
   *
   * @param messageKafkaCommonProperties the {@link MessageCommonConfig#messageKafkaCommonProperties()}
   * @param messageKafkaProducerProperties the {@link #messageKafkaProducerProperties()}
   *
   * @return the ProducerFactory
   */
  @Bean
  public ProducerFactory<String, String> messageKafkaProducerFactory(
      @Qualifier("messageKafkaCommonProperties") KafkaCommonProperties messageKafkaCommonProperties,
      @Qualifier("messageKafkaProducerProperties") KafkaProducerProperties messageKafkaProducerProperties) {

    return createProducerFactory(messageKafkaCommonProperties, messageKafkaProducerProperties);
  }

  /**
   * Creates bean for {@link KafkaTemplate}.
   *
   * @param messageProducerLoggingListener the {@link #messageProducerLoggingListener(MessageLoggingSupport, Tracer)}
   * @param messageKafkaProducerFactory the
   *        {@link #messageKafkaProducerFactory(KafkaCommonProperties, KafkaProducerProperties)}
   *
   * @return the KafkaTemplate
   */
  @Bean
  public KafkaTemplate<String, String> messageKafkaTemplate(
      @Qualifier("messageProducerLoggingListener") ProducerLoggingListener messageProducerLoggingListener,
      @Qualifier("messageKafkaProducerFactory") ProducerFactory<String, String> messageKafkaProducerFactory) {

    return createKafkaTemplate(messageProducerLoggingListener, messageKafkaProducerFactory);
  }

  /**
   * Creates bean for {@link ObjectMapper}
   *
   * @return ObjectMapper.
   */
  @Bean
  public ObjectMapper objectMapper() {

    return new ObjectMapper();
  }

  /**
   * Creates bean for {@link MessageSenderImpl}.
   *
   * @param objectMapper the {@link #objectMapper}
   * @param messageKafkaTemplate the {@link #messageKafkaTemplate(ProducerLoggingListener, ProducerFactory)}
   * @param messageLoggingSupport the {@link MessageCommonConfig#messageLoggingSupport()}
   * @param messageSenderProperties the {@link #messageSenderProperties()}
   * @param spanInjector the {@link MessageSpanInjector}
   * @param tracer the {@link Tracer}
   * @return the MessageSenderImpl.
   */
  @Bean
  public MessageSenderImpl messageSender(@Qualifier("objectMapper") ObjectMapper objectMapper,
      @Qualifier("messageKafkaTemplate") KafkaTemplate<String, String> messageKafkaTemplate,
      @Qualifier("messageLoggingSupport") MessageLoggingSupport messageLoggingSupport,
      @Qualifier("messageSenderProperties") MessageSenderProperties messageSenderProperties,
      @Autowired(required = false) MessageSpanInjector spanInjector, @Autowired(required = false) Tracer tracer) {

    MessageSenderImpl bean = new MessageSenderImpl();
    bean.setJacksonMapper(objectMapper);
    bean.setKafkaTemplate(messageKafkaTemplate);
    bean.setLoggingSupport(messageLoggingSupport);
    bean.setSenderProperties(messageSenderProperties);
    bean.setSpanInjector(spanInjector);
    bean.setTracer(tracer);
    return bean;
  }

  /**
   * Creates bean for {@link ProducerLoggingListener}.
   *
   * @param messageLoggingSupport {@link MessageLoggingSupport}
   * @param tracer {@link Tracer}
   * @return the ProducerLoggingListener
   */
  @Bean
  public ProducerLoggingListener messageProducerLoggingListener(MessageLoggingSupport messageLoggingSupport,
      @Autowired(required = false) Tracer tracer) {

    return new ProducerLoggingListener(messageLoggingSupport, tracer);
  }

  /**
   * This method is used to create {@link KafkaTemplate}.
   *
   * @param producerLogListener the {@link ProducerLoggingListener}
   * @param producerFactory the {@link ProducerFactory}
   * @return KafkaTemplate
   */
  public static KafkaTemplate<String, String> createKafkaTemplate(ProducerLoggingListener producerLogListener,
      ProducerFactory<String, String> producerFactory) {

    KafkaTemplate<String, String> template = new KafkaTemplate<>(producerFactory);
    template.setProducerListener(producerLogListener);
    return template;
  }

  /**
   * This method is used to create {@link ProducerFactory}.
   *
   * @param kafkaCommonProperties the {@link KafkaCommonProperties}
   * @param kafkaProducerProperties the {@link KafkaProducerProperties}
   * @return ProducerFactory
   */
  public static ProducerFactory<String, String> createProducerFactory(KafkaCommonProperties kafkaCommonProperties,
      KafkaProducerProperties kafkaProducerProperties) {

    KafkaPropertyMapper mapper = new KafkaPropertyMapper();
    return new DefaultKafkaProducerFactory<>(mapper.producerProperties(kafkaCommonProperties, kafkaProducerProperties));
  }
}
