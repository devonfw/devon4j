package com.devonfw.module.kafka.common.messaging.api.config;

import javax.inject.Inject;

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
import com.devonfw.module.logging.common.api.DiagnosticContextFacade;
import com.devonfw.module.logging.common.api.LoggingConstants;
import com.devonfw.module.logging.common.impl.DiagnosticContextFacadeImpl;

import brave.Tracer;

/**
 * A configuration class for the {@link MessageSender}
 *
 * @deprecated The implementation of Devon4Js Kafka module will be abandoned. It is superseeded by Springs Kafka
 *             implementation.
 */
@Deprecated
@Configuration
@Import(MessageCommonConfig.class)
public class MessageSenderConfig {

  @Inject
  private Tracer tracer;

  /**
   * Creates bean for {@link DiagnosticContextFacadeImpl} and used for setting and retrieving correlation-id from
   * {@link LoggingConstants}.
   *
   * @return DiagnosticContextFacade.
   */
  @Bean
  public DiagnosticContextFacade messageDiagnosticContextFacade() {

    return new DiagnosticContextFacadeImpl();
  }

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
   * @param <K> the key type
   * @param <V> the value type
   *
   *
   * @param messageKafkaCommonProperties the {@link MessageCommonConfig#messageKafkaCommonProperties()}
   * @param messageKafkaProducerProperties the {@link #messageKafkaProducerProperties()}
   *
   * @return the ProducerFactory
   */
  @Bean
  public <K, V> ProducerFactory<K, V> messageKafkaProducerFactory(KafkaCommonProperties messageKafkaCommonProperties,
      KafkaProducerProperties messageKafkaProducerProperties) {

    return createProducerFactory(messageKafkaCommonProperties, messageKafkaProducerProperties);
  }

  /**
   * Creates bean for {@link KafkaTemplate}.
   *
   * @param <K> the key type
   * @param <V> the value type
   *
   *
   * @param messageProducerLoggingListener the {@link #messageProducerLoggingListener(MessageLoggingSupport)}
   * @param messageKafkaProducerFactory the
   *        {@link #messageKafkaProducerFactory(KafkaCommonProperties, KafkaProducerProperties)}
   *
   * @return the KafkaTemplate
   */
  @Bean
  public <K, V> KafkaTemplate<K, V> messageKafkaTemplate(ProducerLoggingListener<K, V> messageProducerLoggingListener,
      ProducerFactory<K, V> messageKafkaProducerFactory) {

    return createKafkaTemplate(messageProducerLoggingListener, messageKafkaProducerFactory);
  }

  /**
   * Creates bean for {@link MessageSenderImpl}.
   *
   * @param <K> the key type
   * @param <V> the value type
   *
   *
   * @param messageKafkaTemplate the {@link #messageKafkaTemplate(ProducerLoggingListener, ProducerFactory)}
   * @param messageLoggingSupport the {@link MessageCommonConfig#messageLoggingSupport()}
   * @param messageSenderProperties the {@link #messageSenderProperties()}
   * @param diagnosticContextFacade the {@link DiagnosticContextFacade}
   * @param messageSpanInjector the {@link MessageSpanInjector}
   * @return the MessageSenderImpl.
   */
  @Bean
  public <K, V> MessageSenderImpl<?, ?> messageSender(KafkaTemplate<K, V> messageKafkaTemplate,
      MessageLoggingSupport messageLoggingSupport, MessageSenderProperties messageSenderProperties,
      DiagnosticContextFacade diagnosticContextFacade, MessageSpanInjector messageSpanInjector) {

    MessageSenderImpl<K, V> bean = new MessageSenderImpl<>();
    bean.setKafkaTemplate(messageKafkaTemplate);
    bean.setLoggingSupport(messageLoggingSupport);
    bean.setSenderProperties(messageSenderProperties);
    bean.setSpanInjector(messageSpanInjector);
    bean.setTracer(this.tracer);
    bean.setDiagnosticContextFacade(diagnosticContextFacade);
    return bean;
  }

  /**
   * Creates bean for {@link ProducerLoggingListener}.
   *
   * @param messageLoggingSupport {@link MessageLoggingSupport}
   * @return the ProducerLoggingListener
   */
  @Bean
  public ProducerLoggingListener<?, ?> messageProducerLoggingListener(MessageLoggingSupport messageLoggingSupport) {

    return new ProducerLoggingListener<>(messageLoggingSupport);
  }

  /**
   * This method is used to create {@link KafkaTemplate}.
   *
   * @param <K>
   * @param <V>
   *
   *
   * @param producerLogListener the {@link ProducerLoggingListener}
   * @param producerFactory the {@link ProducerFactory}
   * @return KafkaTemplate
   */
  private <K, V> KafkaTemplate<K, V> createKafkaTemplate(ProducerLoggingListener<K, V> producerLogListener,
      ProducerFactory<K, V> producerFactory) {

    KafkaTemplate<K, V> template = new KafkaTemplate<>(producerFactory);
    template.setProducerListener(producerLogListener);
    return template;
  }

  /**
   * This method is used to create {@link ProducerFactory}.
   *
   *
   * @param kafkaCommonProperties the {@link KafkaCommonProperties}
   * @param kafkaProducerProperties the {@link KafkaProducerProperties}
   * @return ProducerFactory
   */
  private <K, V> ProducerFactory<K, V> createProducerFactory(KafkaCommonProperties kafkaCommonProperties,
      KafkaProducerProperties kafkaProducerProperties) {

    KafkaPropertyMapper mapper = new KafkaPropertyMapper();
    return new DefaultKafkaProducerFactory<>(mapper.producerProperties(kafkaCommonProperties, kafkaProducerProperties));
  }
}
