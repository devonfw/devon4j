package com.devonfw.module.kafka.common.messaging.api.config;

import org.springframework.beans.factory.annotation.Autowired;
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
 * @param <K>
 *
 */
@Configuration
@Import(MessageCommonConfig.class)
public class MessageSenderConfig {

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
   *
   * @param messageKafkaCommonProperties the {@link MessageCommonConfig#messageKafkaCommonProperties()}
   * @param messageKafkaProducerProperties the {@link #messageKafkaProducerProperties()}
   *
   * @return the ProducerFactory
   */
  @Bean
  public ProducerFactory<Object, Object> messageKafkaProducerFactory(KafkaCommonProperties messageKafkaCommonProperties,
      KafkaProducerProperties messageKafkaProducerProperties) {

    return createProducerFactory(messageKafkaCommonProperties, messageKafkaProducerProperties);
  }

  /**
   * Creates bean for {@link KafkaTemplate}.
   *
   *
   * @param messageProducerLoggingListener the {@link #messageProducerLoggingListener(MessageLoggingSupport, Tracer)}
   * @param messageKafkaProducerFactory the
   *        {@link #messageKafkaProducerFactory(KafkaCommonProperties, KafkaProducerProperties)}
   *
   * @return the KafkaTemplate
   */
  @Bean
  public KafkaTemplate<Object, Object> messageKafkaTemplate(
      ProducerLoggingListener<Object, Object> messageProducerLoggingListener,
      ProducerFactory<Object, Object> messageKafkaProducerFactory) {

    return createKafkaTemplate(messageProducerLoggingListener, messageKafkaProducerFactory);
  }

  /**
   * Creates bean for {@link MessageSenderImpl}.
   *
   *
   * @param messageKafkaTemplate the {@link #messageKafkaTemplate(ProducerLoggingListener, ProducerFactory)}
   * @param messageLoggingSupport the {@link MessageCommonConfig#messageLoggingSupport()}
   * @param messageSenderProperties the {@link #messageSenderProperties()}
   * @param diagnosticContextFacade the {@link DiagnosticContextFacade}
   * @param messageSpanInjector the {@link MessageSpanInjector}
   * @param tracer the {@link Tracer}
   * @return the MessageSenderImpl.
   */
  @Bean
  public MessageSenderImpl messageSender(KafkaTemplate<Object, Object> messageKafkaTemplate,
      MessageLoggingSupport messageLoggingSupport, MessageSenderProperties messageSenderProperties,
      DiagnosticContextFacade diagnosticContextFacade, MessageSpanInjector messageSpanInjector,
      @Autowired(required = false) Tracer tracer) {

    MessageSenderImpl bean = new MessageSenderImpl();
    bean.setKafkaTemplate(messageKafkaTemplate);
    bean.setLoggingSupport(messageLoggingSupport);
    bean.setSenderProperties(messageSenderProperties);
    bean.setSpanInjector(messageSpanInjector);
    bean.setTracer(tracer);
    bean.setDiagnosticContextFacade(diagnosticContextFacade);
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
  public ProducerLoggingListener<Object, Object> messageProducerLoggingListener(
      MessageLoggingSupport messageLoggingSupport, @Autowired(required = false) Tracer tracer) {

    return new ProducerLoggingListener<>(messageLoggingSupport, tracer);
  }

  /**
   * This method is used to create {@link KafkaTemplate}.
   *
   *
   * @param producerLogListener the {@link ProducerLoggingListener}
   * @param producerFactory the {@link ProducerFactory}
   * @return KafkaTemplate
   */
  public static KafkaTemplate<Object, Object> createKafkaTemplate(
      ProducerLoggingListener<Object, Object> producerLogListener, ProducerFactory<Object, Object> producerFactory) {

    KafkaTemplate<Object, Object> template = new KafkaTemplate<>(producerFactory);
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
  public static ProducerFactory<Object, Object> createProducerFactory(KafkaCommonProperties kafkaCommonProperties,
      KafkaProducerProperties kafkaProducerProperties) {

    KafkaPropertyMapper mapper = new KafkaPropertyMapper();
    return new DefaultKafkaProducerFactory<>(mapper.producerProperties(kafkaCommonProperties, kafkaProducerProperties));
  }
}
